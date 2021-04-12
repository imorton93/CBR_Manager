package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Forms.DisplayFormPage;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
import com.example.cbr_manager.Forms.Question;
import com.example.cbr_manager.Forms.QuestionType;
import com.example.cbr_manager.Forms.TextQuestion;
import com.example.cbr_manager.R;

import java.util.ArrayList;

public class EditClientActivity extends AppCompatActivity {
    LinearLayout form;
    int currentPage;
    int pageCount;
    int imagePage;
    ArrayList<FormPage> pages;
    Button next;
    Button back;
    ProgressBar progressBar;
    TextView progressText;
    ImageView imageView;

    Client client;
    private DatabaseHelper mydb;

    private long id;
    private int position;

    public static final String R_CLIENT_ID_PASSED_IN = "r_client_id_passed_in";
    public static final String R_CLIENT_POS_PASSED_IN = "r_client_POS_passed_in";

    public static Intent makeIntent(Context context, int position, long id) {
        Intent intent =  new Intent(context, EditClientActivity.class);
        intent.putExtra(R_CLIENT_ID_PASSED_IN, id);
        intent.putExtra(R_CLIENT_POS_PASSED_IN, position);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);

        mydb = new DatabaseHelper(EditClientActivity.this);
        next = (Button) findViewById(R.id.nextBtnVisit);


        back = (Button) findViewById(R.id.backBtn);
        imageView = new ImageView(this);

        form = (LinearLayout) findViewById(R.id.form);
        progressBar = (ProgressBar) findViewById(R.id.formProgress);
        progressText = (TextView) findViewById(R.id.formProgressText);

        currentPage = 1;
        pages = new ArrayList<>();

        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(EditClientActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);

        extractIntent();
        getClientInfo();

        createEditForm();
        pageCount = pages.size() + 1;

        DisplayFormPage.displayPage(pages.get(currentPage-1), form, this, 0 , 0);
        loadAnswers(pages.get(currentPage - 1));

        progressBar.setMax(pageCount);
        setProgress(currentPage, pageCount);

        next.setOnClickListener(v -> {
            if (currentPage == pageCount) {
                editClient();
            }

            //make sure all required fields are filled in the page
            else if(!requiredFieldsFilled(pages.get(currentPage - 1))){
                requiredFieldsToast();
            }
            else if(currentPage < pageCount - 1){
                if(currentPage == 1){
                    back.setClickable(true);
                    back.setVisibility(View.VISIBLE);

                    back.setBackground(ContextCompat.getDrawable(EditClientActivity.this, R.drawable.rounded_form_buttons));


                }
                //save answers
                savePage(pages.get(currentPage - 1));

                currentPage++;
                setProgress(currentPage, pageCount);

                clearForm();

                if(currentPage == imagePage){
                    displayPicture(pages.get(currentPage - 1));
                }
                else{
                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, EditClientActivity.this, 0, 0);

                }

                //load previously saved answers if any
                loadAnswers(pages.get(currentPage - 1));

            }
            else if(currentPage == pageCount - 1){
                //save answers
                savePage(pages.get(currentPage - 1));
                currentPage++;

                setProgress(currentPage, pageCount);
                clearForm();
                reviewPage();
                if(currentPage == pageCount){
                    next.setText(R.string.finish);
                }

            }
        });

        back.setOnClickListener(v -> {
            if(currentPage == pageCount){
                next.setText(R.string.next);
            }
            currentPage--;
            setProgress(currentPage, pageCount);
            clearForm();

            if(currentPage == imagePage){
                displayPicture(pages.get(currentPage - 1));
            }
            else{
                DisplayFormPage.displayPage(pages.get(currentPage - 1), form, EditClientActivity.this, 0, 0);
            }
            //load previously saved answers if any
            loadAnswers(pages.get(currentPage - 1));
            if(currentPage == 1){
                back.setClickable(false);
                back.setVisibility(View.INVISIBLE);
                back.setBackgroundColor(Color.DKGRAY);

            }
        });
        back.setClickable(false);
        back.setVisibility(View.INVISIBLE);

        back.setBackgroundColor(Color.DKGRAY);


        //Permission for camera
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(Color.parseColor("#009fb8"), android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);
    }

    private void setProgress(int currentPage, int pageCount){
        progressBar.setProgress(currentPage);
        progressText.setText(currentPage + "/" + pageCount);
    }

    private void clearForm(){
        form.removeAllViews();
    }

    private void extractIntent(){
        Intent intent = getIntent();
        this.position = intent.getIntExtra(R_CLIENT_POS_PASSED_IN, 0);
        this.id = intent.getLongExtra(R_CLIENT_ID_PASSED_IN, 0);
    }

    private void getClientInfo(){
        ClientManager manager = ClientManager.getInstance(this);
        client = manager.getClientById(id);
        byte[] bitmapdata = client.getPhoto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        imageView.setImageBitmap(bitmap);
    }


    private void editClient() {
        client.setIsSynced(0);
        boolean success = mydb.updateClient(client);

        if(success) {
            Toast.makeText(EditClientActivity.this, "Entry Successful!", Toast.LENGTH_LONG).show();
            Intent intent = TaskViewActivity.makeIntent(EditClientActivity.this);
            String current_username = getIntent().getStringExtra("Worker Username");
            intent.putExtra("Worker Username", current_username);
            startActivity(intent);
        } else {
            Toast.makeText(EditClientActivity.this, "Entry failed.", Toast.LENGTH_LONG).show();
        }
    }

    private void displayPicture(FormPage page){
        int imageViewHeight = (int)(form.getHeight() * .7);
        LinearLayout.LayoutParams imageViewLayoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageViewHeight);
        imageView.setLayoutParams(imageViewLayoutParams1);


        ArrayList<Question> questions = page.getQuestions();
        TextQuestion picQuestion = (TextQuestion) questions.get(0);
        TextView questionText = new TextView(this);
        questionText.setText(picQuestion.getQuestionString());
        form.addView(questionText);

        ImageView picButton = new ImageView(this);
        picButton.setImageResource(R.drawable.camera_icon);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.gravity = Gravity.CENTER;
        picButton.setLayoutParams(params);
        picButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        });

        form.addView(imageView);
        form.addView(picButton);
    }

    private void requiredFieldsToast(){
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(this, "Fill all Required Fields", duration).show();
    }

    private Boolean requiredFieldsFilled(FormPage page){
        ArrayList<Question> questions = page.getQuestions();
        Boolean returnBool = true;
        for(Question question : questions){
            QuestionType type = question.getQuestionType();
            if(question.getRequired()){
                if(type == QuestionType.PLAIN_TEXT || type == QuestionType.PHONE_NUMBER){
                    if(!isTextAnswered(question)){
                        returnBool = false;
                    }
                }
                else if(type == QuestionType.NUMBER){
                    if(!isNumberAnswered(question)){
                        returnBool = false;
                    }
                }
                else if(type == QuestionType.RADIO){
                    if (!isRadioAnswered(question)) {
                        returnBool = false;
                    }
                }
                else if(type == QuestionType.CHECK_BOX){
                    if(!isCheckBoxAnswered(question)){
                        returnBool = false;
                    }
                }
            }

        }
        return returnBool;
    }


    private Boolean isTextAnswered(Question question){
        String tag = question.getQuestionTag();
        EditText input = (EditText) form.findViewWithTag(tag);
        return input.getText().toString().trim().length() > 0;
    }

    private Boolean isNumberAnswered(Question question){
        String tag = question.getQuestionTag();
        EditText input = (EditText) form.findViewWithTag(tag);
        String inputStr = input.getText().toString();
        if(inputStr.equals("")){
            return false;
        }
        else{
            return true;
        }

    }

    private Boolean isRadioAnswered(Question question){
        String tag = question.getQuestionTag();
        RadioGroup radioGroup = (RadioGroup) form.findViewWithTag(tag);
        boolean isAnswered = true;
        boolean caregiverPresentCondition = true;
        boolean consentCondition = true;

        if(radioGroup.getCheckedRadioButtonId() == -1){
            isAnswered = false;
        }

        //test if optional caregiver phone number field is filled if caregiver present
        if(tag.equals(getString(R.string.caregiverPresent)) && isAnswered){
            int id = radioGroup.getCheckedRadioButtonId();
            RadioButton button = (RadioButton) radioGroup.findViewById(id);
            if(button.getText().equals("Yes")){
                EditText input = form.findViewWithTag(getString(R.string.caregiverContactNumber));
                if (input.getText().toString().trim().length() == 0) {
                    caregiverPresentCondition = false;
                }
            }
        }

        if(question.getQuestionTag().equals("consent") && isAnswered) {
            RadioGroup consent = (RadioGroup) form.findViewWithTag("consent");
            int buttonId = consent.getCheckedRadioButtonId();
            RadioButton radioButton = consent.findViewById(buttonId);
            if (radioButton.getText().equals("Yes"))
                consentCondition = true;
            else {
                Toast.makeText(EditClientActivity.this, "Please provide consent to continue.", Toast.LENGTH_LONG).show();
                consentCondition = false;
            }
        }

        return isAnswered && caregiverPresentCondition && consentCondition;
    }

    private Boolean isCheckBoxAnswered(Question question){
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        Boolean returnBool = false;
        CheckBox checkBox;
        for(int i = 0; i < mcq.getAnswers().length; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                returnBool = true;
            }
        }
        return returnBool;
    }

    private void loadAnswers(FormPage page){
        ArrayList<Question> questions = page.getQuestions();
        for(Question question : questions){
            QuestionType type = question.getQuestionType();
            if(type == QuestionType.PLAIN_TEXT || type == QuestionType.PHONE_NUMBER){
                loadText(question);
            }
            else if(type == QuestionType.DATE){
                loadDate(question);
            }
            else if(type == QuestionType.RADIO){
                loadRadio(question);
            }
            else if(type == QuestionType.NUMBER){
                loadNumber(question);
            }
            else if(type == QuestionType.DROP_DOWN){
                loadDropDown(question);
            }
            else if(type == QuestionType.CHECK_BOX){
                loadCheckBox(question);
            }
        }
    }

    private void loadText(Question question){
        String tag = question.getQuestionTag();
        String data = null;

        if(tag.equals(getString(R.string.firstName))){
            data = client.getFirstName();
        }
        else if(tag.equals(getString(R.string.lastName))){
            data = client.getLastName();
        }
        else if(tag.equals(getString(R.string.contactNumber))){
            data = client.getContactPhoneNumber();
        }
        else if(tag.equals(getString(R.string.caregiverContactNumber))){
            data = client.getCaregiverPhoneNumber();
        }
        else if(tag.equals(getString(R.string.healthRequire))){
            data = client.getHealthRequire();
        }
        else if(tag.equals(getString(R.string.educationRequire))){
            data = client.getEducationRequire();
        }
        else if(tag.equals(getString(R.string.socialRequire))){
            data = client.getSocialStatusRequire();
        }
        else if(tag.equals(getString(R.string.healthIndividualGoal))){
            data = client.getHealthIndividualGoal();
        }
        else if(tag.equals(getString(R.string.educationIndividualGoal))){
            data = client.getEducationIndividualGoal();
        }
        else if(tag.equals(getString(R.string.socialIndividualGoal))){
            data = client.getSocialStatusIndividualGoal();
        }

        if(data != null){
            EditText input = (EditText) form.findViewWithTag(tag);
            input.setText(data);
        }

    }

    private void loadDate(Question question){
        String tag = question.getQuestionTag();
        String data = null;
        if(tag.equals(getString(R.string.date))){
            data = client.getDate();
        }

        if(data != null){
            TextView date = (TextView) form.findViewWithTag(tag);
            date.setText(data);
        }
    }

    private void loadRadio(Question question){
        String tag = question.getQuestionTag();
        if(tag.equals(getString(R.string.consent)) || tag.equals(getString(R.string.caregiverPresent))){
            loadRadioBool(question);
        }
        else{
            loadRadioMultiple(question);
        }
    }

    private void loadRadioBool(Question question){
        String tag = question.getQuestionTag();
        Boolean data = null;
        if(tag.equals(getString(R.string.consent))){
            data = client.getConsentToInterview();
        }
        else if(tag.equals(getString(R.string.caregiverPresent))){
            data = client.getCaregiverPresent();
        }

        if(data != null){
            RadioGroup consent = (RadioGroup) form.findViewWithTag(tag);
            RadioButton button;
            if(data){
                button = (RadioButton) consent.getChildAt(0);
            }
            else{
                button = (RadioButton) consent.getChildAt(1);
            }
            button.toggle();
        }
    }

    private void loadRadioMultiple(Question question){
        String tag = question.getQuestionTag();
        String data = null;

        if(tag.equals(getString(R.string.gender))){
            data = client.getGender();
        }
        else if(tag.equals(getString(R.string.educationRate))){
            data = client.getEducationRate();
        }
        else if(tag.equals(getString(R.string.healthRate))){
            data = client.getHealthRate();
        }
        else if(tag.equals(getString(R.string.socialRate))){
            data = client.getSocialStatusRate();
        }

        if(data != null){
            RadioGroup rateGroup = (RadioGroup) form.findViewWithTag(tag);
            for(int i = 0; i < rateGroup.getChildCount(); i++){
                RadioButton button = (RadioButton) rateGroup.getChildAt(i);
                if(button.getText().equals(data)){
                    button.toggle();
                }
            }
        }
    }

    private void loadNumber(Question question){
        String tag = question.getQuestionTag();
        int data = -1;
        if(tag.equals(getString(R.string.age))){
            data = client.getAge();
        }
        else if(tag.equals(getString(R.string.villageNumber))){
            data = client.getVillageNumber();
        }

        if(data != -1){
            String dataStr = Integer.toString(data);
            EditText input = (EditText) form.findViewWithTag(tag);
            input.setText(dataStr);
        }

    }

    private void loadDropDown(Question question){
        String tag = question.getQuestionTag();
        String data = null;
        if(tag.equals(getString(R.string.location))){
            data = client.getLocation();
        }

        if(data != null){
            Spinner spinner = (Spinner) form.findViewWithTag(tag);
            for(int i = 0; i < spinner.getCount(); i++){
                if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(data)){
                    spinner.setSelection(i);
                }
            }
        }
    }

    private void loadCheckBox(Question question){
        ArrayList<String> disabilities = client.getDisabilities();
        for(int i = 0; i < disabilities.size(); i++){
            CheckBox checkBox;
            for(int j = 0; j < 9; j++){
                checkBox = (CheckBox) form.findViewWithTag(j);
                if(checkBox.getText().equals(disabilities.get(i))){
                    checkBox.toggle();
                }
            }
        }
    }

    private void savePage(FormPage page){
        ArrayList<Question> questions = page.getQuestions();
        for(Question question : questions){
            QuestionType type = question.getQuestionType();
            if(type == QuestionType.PLAIN_TEXT || type == QuestionType.PHONE_NUMBER){
                saveText(question);
            }
            else if(type == QuestionType.DATE){
                saveDate(question);
            }
            else if(type == QuestionType.RADIO){
                saveRadio(question);
            }
            else if(type == QuestionType.NUMBER){
                saveNumber(question);
            }
            else if(type == QuestionType.DROP_DOWN){
                saveDropDown(question);
            }
            else if(type == QuestionType.CHECK_BOX){
                saveCheckBox(question);
            }
        }
    }

    private void saveText(Question question){
        String tag = question.getQuestionTag();
        EditText input = (EditText) form.findViewWithTag(tag);
        if(tag.equals(getString(R.string.firstName))){
            client.setFirstName(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.lastName))){
            client.setLastName(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.contactNumber))){
            client.setContactPhoneNumber(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.caregiverContactNumber))){
            client.setCaregiverPhoneNumber(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.healthRequire))){
            client.setHealthRequire(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.educationRequire))){
            client.setEducationRequire(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.socialRequire))){
            client.setSocialStatusRequire(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.healthIndividualGoal))){
            client.setHealthIndividualGoal(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.educationIndividualGoal))){
            client.setEducationIndividualGoal(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.socialIndividualGoal))){
            client.setSocialStatusIndividualGoal(input.getText().toString());
        }
    }


    private void saveDate(Question question){
        TextView date = (TextView) form.findViewWithTag(question.getQuestionTag());
        client.setDate(date.getText().toString());
    }

    private void saveRadio(Question question){
        String tag = question.getQuestionTag();
        if(tag.equals(getString(R.string.consent)) || tag.equals(getString(R.string.caregiverPresent))){
            saveRadioBool(question);
        }
        else{
            saveRadioMultiple(question);
        }
    }

    private void saveRadioBool(Question question){
        String tag = question.getQuestionTag();
        RadioGroup consent = (RadioGroup) form.findViewWithTag(tag);
        int buttonId = consent.getCheckedRadioButtonId();
        RadioButton radioButton = consent.findViewById(buttonId);

        if(tag.equals(getString(R.string.consent))){
            client.setConsentToInterview(radioButton.getText().equals("Yes"));
        }
        else if(tag.equals(getString(R.string.caregiverPresent))){
            client.setCaregiverPresent(radioButton.getText().equals("Yes"));
        }
    }

    private void saveRadioMultiple(Question question){
        String tag = question.getQuestionTag();
        RadioGroup radioGroup = (RadioGroup) form.findViewWithTag(tag);
        RadioButton radioButton;
        for(int i = 0; i < radioGroup.getChildCount(); i++){
            radioButton = (RadioButton) radioGroup.getChildAt(i);
            if(radioButton.isChecked()){
                if(tag.equals(getString(R.string.gender))){
                    client.setGender(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.healthRate))){
                    client.setHealthRate(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.educationRate))){
                    client.setEducationRate(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.socialRate))){
                    client.setSocialStatusRate(radioButton.getText().toString());
                }

            }
        }
    }

    private void saveNumber(Question question){
        String tag = question.getQuestionTag();
        EditText input = (EditText) form.findViewWithTag(tag);
        String inputStr = input.getText().toString();
        int num = Integer.parseInt(inputStr);
        if(tag.equals(getString(R.string.age))){
            client.setAge(num);
        }
        else if(tag.equals(getString(R.string.villageNumber))){
            client.setVillageNumber(num);
        }
    }


    private void saveDropDown(Question question){
        String tag = question.getQuestionTag();
        Spinner spinner = (Spinner) form.findViewWithTag(tag);
        String selected = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        if(tag.equals(getString(R.string.location))) {
            client.setLocation(selected);
        }
    }

    private void saveCheckBox(Question question){
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        String tag = question.getQuestionTag();
        client.clearDisabilities();
        CheckBox checkBox;
        for(int i = 0; i < mcq.getAnswers().length; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                String selected = checkBox.getText().toString();
                client.addToDisabilities(selected);
            }
        }
    }


    private void createEditForm(){
        Resources res = getResources();

        //page one: first and last name
        TextQuestion firstName = new TextQuestion(getString(R.string.firstName),getString(R.string.firstName_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion lastName = new TextQuestion(getString(R.string.lastName),getString(R.string.lastName_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageOne = new FormPage();
        pageOne.addToPage(firstName);
        pageOne.addToPage(lastName);
        pages.add(pageOne);

        //page two: Age gender
        TextQuestion age = new TextQuestion(getString(R.string.age),getString(R.string.age_newClientForm), QuestionType.NUMBER, true);
        MultipleChoiceQuestion gender = new MultipleChoiceQuestion(getString(R.string.gender),getString(R.string.gender_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.gender), true);
        FormPage pageTwo = new FormPage();
        pageTwo.addToPage(age);
        pageTwo.addToPage(gender);
        pages.add(pageTwo);


        //page three: Location Village No. Contact Number   GPS LATER!!!!
        MultipleChoiceQuestion location = new MultipleChoiceQuestion(getString(R.string.location),getString(R.string.location_newClientForm), QuestionType.DROP_DOWN,res.getStringArray(R.array.locations), true);
        TextQuestion villageNum = new TextQuestion(getString(R.string.villageNumber),getString(R.string.villageNumber_newClientForm), QuestionType.NUMBER, true);
        TextQuestion contactNum = new TextQuestion(getString(R.string.contactNumber),getString(R.string.contactNumber_newClientForm), QuestionType.PHONE_NUMBER, true);
        FormPage pageThree = new FormPage();
        pageThree.addToPage(location);
        pageThree.addToPage(villageNum);
        pageThree.addToPage(contactNum);
        pages.add(pageThree);

        //page four: Caregiver
        MultipleChoiceQuestion caregiverPresent = new MultipleChoiceQuestion(getString(R.string.caregiverPresent),getString(R.string.caregiverPresent_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer), true);
        TextQuestion caregiverContactNumber = new TextQuestion(getString(R.string.caregiverContactNumber),getString(R.string.caregiverNumber_newClientForm), QuestionType.PHONE_NUMBER, false);
        FormPage pageFour = new FormPage();
        pageFour.addToPage(caregiverPresent);
        pageFour.addToPage(caregiverContactNumber);
        pages.add(pageFour);


        //page five: photo
        TextQuestion photo = new TextQuestion(getString(R.string.photo),getString(R.string.photo_newClientForm), QuestionType.PICTURE, false);
        imagePage = 5;
        FormPage pageFive = new FormPage();
        pageFive.addToPage(photo);
        pages.add(pageFive);




        //page six: Type of disability
        MultipleChoiceQuestion disability = new MultipleChoiceQuestion(getString(R.string.disabilityType),getString(R.string.disabilityType_newClientForm), QuestionType.CHECK_BOX, res.getStringArray(R.array.disability_types), true);
        FormPage pageSix = new FormPage();
        pageSix.addToPage(disability);
        pages.add(pageSix);


        //page seven: clients health rate, require individual goal
        MultipleChoiceQuestion clientHealthRate = new MultipleChoiceQuestion(getString(R.string.healthRate),getString(R.string.healthRate_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type), true);
        TextQuestion healthRequire = new TextQuestion(getString(R.string.healthRequire),getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion healthIndividualGoal = new TextQuestion(getString(R.string.healthIndividualGoal),getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageSeven = new FormPage();
        pageSeven.addToPage(clientHealthRate);
        pageSeven.addToPage(healthRequire);
        pageSeven.addToPage(healthIndividualGoal);
        pages.add(pageSeven);


        //page eight: clients education rate
        MultipleChoiceQuestion clientEducationRate = new MultipleChoiceQuestion(getString(R.string.educationRate),getString(R.string.educationStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type), true);
        TextQuestion educationRequire = new TextQuestion(getString(R.string.educationRequire),getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion educationIndividualGoal = new TextQuestion(getString(R.string.educationIndividualGoal),getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageEight = new FormPage();
        pageEight.addToPage(clientEducationRate);
        pageEight.addToPage(educationRequire);
        pageEight.addToPage(educationIndividualGoal);
        pages.add(pageEight);

        //page nine: social status
        MultipleChoiceQuestion clientSocialRate = new MultipleChoiceQuestion(getString(R.string.socialRate),getString(R.string.socialStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type), true);
        TextQuestion socialRequire = new TextQuestion(getString(R.string.socialRequire),getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion socialIndividualGoal = new TextQuestion(getString(R.string.socialIndividualGoal),getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageNine = new FormPage();
        pageNine.addToPage(clientSocialRate);
        pageNine.addToPage(socialRequire);
        pageNine.addToPage(socialIndividualGoal);
        pages.add(pageNine);
    }

    private void reviewPage(){
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        float txtSize = 18;

        TextView firstNameView = new TextView(this);
        String firstName = client.getFirstName();
        firstNameView.setText("First Name: " + firstName);
        firstNameView.setTextSize(txtSize);
        layout.addView(firstNameView);

        TextView lastNameView = new TextView(this);
        String lastName = client.getLastName();
        lastNameView.setText("Last Name: " + lastName);
        lastNameView.setTextSize(txtSize);
        layout.addView(lastNameView);

        insertLineDivide(layout);

        TextView ageView = new TextView(this);
        int age = client.getAge();
        String ageStr = Integer.toString(age);
        ageView.setText("Age: " + ageStr);
        ageView.setTextSize(txtSize);
        layout.addView(ageView);

        TextView genderView = new TextView(this);
        String gender = client.getGender();
        genderView.setText("Gender: " + gender);
        genderView.setTextSize(txtSize);
        layout.addView(genderView);

        insertLineDivide(layout);

        TextView locationView = new TextView(this);
        String location = client.getLocation();
        locationView.setText("Location: " + location);
        locationView.setTextSize(txtSize);
        layout.addView(locationView);

        TextView villageNumberView = new TextView(this);
        int villageNumber = client.getVillageNumber();
        String villageNumberStr = Integer.toString(villageNumber);
        villageNumberView.setText("Village Number: " + villageNumberStr);
        villageNumberView.setTextSize(txtSize);
        layout.addView(villageNumberView);

        TextView gpsView = new TextView(this);
        double lat = client.getLatitude();
        double lon = client.getLongitude();
        gpsView.setText("GPS: " + lat + ", " + lon);
        gpsView.setTextSize(txtSize);
        layout.addView(gpsView);

        TextView contactNumberView = new TextView(this);
        String contactNumber = client.getContactPhoneNumber();
        contactNumberView.setText("Contact Number: " + contactNumber);
        contactNumberView.setTextSize(txtSize);
        layout.addView(contactNumberView);

        insertLineDivide(layout);

        TextView caregiverPresentView = new TextView(this);
        Boolean caregiverPresent = client.getCaregiverPresent();
        if(caregiverPresent){
            caregiverPresentView.setText("Caregiver Present: Yes");
            TextView caregiverNumberView = new TextView(this);
            String caregiverNumber = client.getCaregiverPhoneNumber();
            caregiverNumberView.setText("Caregiver Phone Number: " + caregiverNumber);
            caregiverPresentView.setTextSize(txtSize);
            caregiverNumberView.setTextSize(txtSize);
            layout.addView(caregiverPresentView);
            layout.addView(caregiverNumberView);
        }
        else {
            caregiverPresentView.setText("Caregiver Present: No");
            caregiverPresentView.setTextSize(txtSize);
            layout.addView(caregiverPresentView);
        }

        insertLineDivide(layout);

        TextView disabilitiesView = new TextView(this);
        ArrayList<String> disabilities = client.getDisabilities();
        String disabilitiesStr = "Type of Disability: ";
        for(int i = 0; i < disabilities.size(); i++){
            disabilitiesStr = disabilitiesStr.concat(disabilities.get(i));
            disabilitiesStr = disabilitiesStr.concat(", ");
        }
        disabilitiesView.setText(disabilitiesStr);
        disabilitiesView.setTextSize(txtSize);
        layout.addView(disabilitiesView);

        insertLineDivide(layout);

        TextView healthRateView = new TextView(this);
        String healthRate = client.getHealthRate();
        healthRateView.setText("Rate of Client's health: " + healthRate);
        healthRateView.setTextSize(txtSize);
        layout.addView(healthRateView);

        TextView healthRequireView = new TextView(this);
        String healthRequire = client.getHealthRequire();
        healthRequireView.setText("Require: " + healthRequire);
        healthRequireView.setTextSize(txtSize);
        layout.addView(healthRequireView);

        TextView healthGoalView = new TextView(this);
        String healthGoal = client.getHealthIndividualGoal();
        healthGoalView.setText("Individual Goal: " + healthGoal);
        healthGoalView.setTextSize(txtSize);
        layout.addView(healthGoalView);

        insertLineDivide(layout);

        TextView educationRateView = new TextView(this);
        String educationRate = client.getEducationRate();
        educationRateView.setText("Rate of Client's Education: " + educationRate);
        educationRateView.setTextSize(txtSize);
        layout.addView(educationRateView);

        TextView educationRequireView = new TextView(this);
        String educationRequire = client.getEducationRequire();
        educationRequireView.setText("Require: " + educationRequire);
        educationRequireView.setTextSize(txtSize);
        layout.addView(educationRequireView);

        TextView educationGoalView = new TextView(this);
        String educationGoal = client.getEducationIndividualGoal();
        educationGoalView.setText("Individual Goal: " + educationGoal);
        educationGoalView.setTextSize(txtSize);
        layout.addView(educationGoalView);

        insertLineDivide(layout);

        TextView socialRateView = new TextView(this);
        String socialRate = client.getSocialStatusRate();
        socialRateView.setText("Rate of Client's Social Status: " + socialRate);
        socialRateView.setTextSize(txtSize);
        layout.addView(socialRateView);

        TextView socialRequireView = new TextView(this);
        String socialRequire = client.getSocialStatusRequire();
        socialRequireView.setText("Require: " + socialRequire);
        socialRequireView.setTextSize(txtSize);
        layout.addView(socialRequireView);

        TextView socialGoalView = new TextView(this);
        String socialGoal = client.getSocialStatusIndividualGoal();
        socialGoalView.setText("Individual Goal: " + socialGoal);
        socialGoalView.setTextSize(txtSize);
        layout.addView(socialGoalView);

        sv.addView(layout);
        form.addView(sv);
    }

    private void insertLineDivide(LinearLayout layout){
        View view = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
        params.topMargin = 20;
        params.bottomMargin = 20;
        params.leftMargin = 10;
        params.rightMargin = 10;
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.BLACK);
        layout.addView(view);
    }

    private void reviewTitle(float txtSize){
        TextView reviewTitle = new TextView(this);
        reviewTitle.setText("Review");
        reviewTitle.setTextSize(txtSize);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        reviewTitle.setLayoutParams(params);
        form.addView(reviewTitle);
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(EditClientActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(EditClientActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(EditClientActivity.this);
                startActivity(intent);
            }
        });
    }

    private void badgeNotification(AdminMessageManager adminMessageManager, TextView badge) {
        int size = adminMessageManager.numUnread();

        if (badge != null) {
            if (size == 0) {
                if (badge.getVisibility() != View.GONE) {
                    badge.setVisibility(View.GONE);
                }
            } else {
                badge.setText(String.valueOf(Math.min(size, 99)));
                if (badge.getVisibility() != View.VISIBLE) {
                    badge.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}