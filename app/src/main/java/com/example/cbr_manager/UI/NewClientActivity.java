package com.example.cbr_manager.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Forms.DisplayFormPage;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
import com.example.cbr_manager.Forms.Question;
import com.example.cbr_manager.Forms.QuestionType;
import com.example.cbr_manager.Forms.TextQuestion;
import com.example.cbr_manager.R;

import java.util.ArrayList;

public class NewClientActivity extends AppCompatActivity {
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

    //structure to save all the answers
    Client newClient;
    private DatabaseHelper mydb;


    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, NewClientActivity.class);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);

        mydb = new DatabaseHelper(NewClientActivity.this);

        next = (Button) findViewById(R.id.nextBtnVisit);
        next.setBackgroundColor(Color.BLUE);
        back = (Button) findViewById(R.id.backBtn);
        newClient = new Client();
        imageView = new ImageView(this);

        form = (LinearLayout) findViewById(R.id.form);
        progressBar = (ProgressBar) findViewById(R.id.formProgress);
        progressText = (TextView) findViewById(R.id.formProgressText);

        currentPage = 1;
        pages = new ArrayList<>();

        ToolbarButtons();

        createNewClientForm();
        pageCount = pages.size() + 1;

        DisplayFormPage.displayPage(pages.get(currentPage-1), form, this);

        progressBar.setMax(pageCount);

        setProgress(currentPage, pageCount);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (currentPage == 11) {
                    insertClient();
                }

                //make sure all required fields are filled in the page
                else if(!requiredFieldsFilled(pages.get(currentPage - 1))){
                    requiredFieldsToast();
                }
                else if(currentPage < pageCount - 1){
                    if(currentPage == 1){
                        back.setClickable(true);
                        back.setBackgroundColor(Color.BLUE);

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
                        DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this);

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
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(currentPage == pageCount){
                        next.setText(R.string.next);
                    }
                    currentPage--;
                    setProgress(currentPage, pageCount);
                    clearForm();

                    if(currentPage == 6){
                        displayPicture(pages.get(currentPage - 1));
                    }
                    else{
                        DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this);
                    }
                    //load previously saved answers if any
                    loadAnswers(pages.get(currentPage - 1));
                    if(currentPage == 1){
                        back.setClickable(false);
                        back.setBackgroundColor(Color.DKGRAY);
                    }
            }
        });
        back.setClickable(false);
        back.setBackgroundColor(Color.DKGRAY);

        //Permission for camera
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    private void setProgress(int currentPage, int pageCount){
        progressBar.setProgress(currentPage);
        progressText.setText(currentPage + "/" + pageCount);
    }

    private void clearForm(){
        form.removeAllViews();
    }

    private void finishForm(){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, "End", duration);
        toast.show();
    }

    private void displayPicture(FormPage page){
        ArrayList<Question> questions = page.getQuestions();
        TextQuestion picQuestion = (TextQuestion) questions.get(0);
        TextView questionText = new TextView(this);
        questionText.setText(picQuestion.getQuestionString());
        form.addView(questionText);

        Button picButton = new Button(this);
        picButton.setText("Take Picture");
        picButton.setBackgroundColor(Color.BLUE);
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        form.addView(picButton);
        form.addView(imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //set capture Image to imageview
            imageView.setImageBitmap(captureImage);
        }
    }

    private void requiredFieldsToast(){
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(this, "Fill all Required Fields", duration).show();
    }

    //returns whether all required fields on the page are filled
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
            }
                else if(type == QuestionType.CHECK_BOX){
                    if(!isCheckBoxAnswered(question)){
                        returnBool = false;
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
                Toast.makeText(NewClientActivity.this, "Please provide consent to continue.", Toast.LENGTH_LONG).show();
                consentCondition = false;
            }
        }

        return isAnswered && caregiverPresentCondition && consentCondition;
    }

    private Boolean isCheckBoxAnswered(Question question){
        Boolean returnBool = false;

        CheckBox checkBox;
        for(int i = 0; i < 10; i++){
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
            data = newClient.getFirstName();
        }
        else if(tag.equals(getString(R.string.lastName))){
            data = newClient.getLastName();
        }
        else if(tag.equals(getString(R.string.contactNumber))){
            data = newClient.getContactPhoneNumber();
        }
        else if(tag.equals(getString(R.string.caregiverContactNumber))){
            data = newClient.getCaregiverPhoneNumber();
        }
        else if(tag.equals(getString(R.string.healthRequire))){
            data = newClient.getHealthRequire();
        }
        else if(tag.equals(getString(R.string.educationRequire))){
            data = newClient.getEducationRequire();
        }
        else if(tag.equals(getString(R.string.socialRequire))){
            data = newClient.getSocialStatusRequire();
        }
        else if(tag.equals(getString(R.string.healthIndividualGoal))){
            data = newClient.getHealthIndividualGoal();
        }
        else if(tag.equals(getString(R.string.educationIndividualGoal))){
            data = newClient.getEducationIndividualGoal();
        }
        else if(tag.equals(getString(R.string.socialIndividualGoal))){
            data = newClient.getSocialStatusIndividualGoal();
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
            data = newClient.getDate();
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
            data = newClient.getConsentToInterview();
        }
        else if(tag.equals(getString(R.string.caregiverPresent))){
            data = newClient.getCaregiverPresent();
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
            data = newClient.getGender();
        }
        else if(tag.equals(getString(R.string.educationRate))){
            data = newClient.getEducationRate();
        }
        else if(tag.equals(getString(R.string.healthRate))){
            data = newClient.getHealthRate();
        }
        else if(tag.equals(getString(R.string.socialRate))){
            data = newClient.getSocialStatusRate();
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
            data = newClient.getAge();
        }
        else if(tag.equals(getString(R.string.villageNumber))){
            data = newClient.getVillageNumber();
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
            data = newClient.getLocation();
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
        ArrayList<String> disabilities = newClient.getDisabilities();
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
            newClient.setFirstName(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.lastName))){
            newClient.setLastName(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.contactNumber))){
            newClient.setContactPhoneNumber(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.caregiverContactNumber))){
            newClient.setCaregiverPhoneNumber(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.healthRequire))){
            newClient.setHealthRequire(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.educationRequire))){
            newClient.setEducationRequire(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.socialRequire))){
            newClient.setSocialStatusRequire(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.healthIndividualGoal))){
            newClient.setHealthIndividualGoal(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.educationIndividualGoal))){
            newClient.setEducationIndividualGoal(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.socialIndividualGoal))){
            newClient.setSocialStatusIndividualGoal(input.getText().toString());
        }
    }


    private void saveDate(Question question){
        TextView date = (TextView) form.findViewWithTag(question.getQuestionTag());
        newClient.setDate(date.getText().toString());
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
                newClient.setConsentToInterview(radioButton.getText().equals("Yes"));
        }
        else if(tag.equals(getString(R.string.caregiverPresent))){
            newClient.setCaregiverPresent(radioButton.getText().equals("Yes"));
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
                    newClient.setGender(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.healthRate))){
                    newClient.setHealthRate(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.educationRate))){
                    newClient.setEducationRate(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.socialRate))){
                    newClient.setSocialStatusRate(radioButton.getText().toString());
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
            newClient.setAge(num);
        }
        else if(tag.equals(getString(R.string.villageNumber))){
            newClient.setVillageNumber(num);
        }
    }


    private void saveDropDown(Question question){
        String tag = question.getQuestionTag();
        Spinner spinner = (Spinner) form.findViewWithTag(tag);
        String selected = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        if(tag.equals(getString(R.string.location))) {
            newClient.setLocation(selected);
        }
    }

    private void saveCheckBox(Question question){
        String tag = question.getQuestionTag();
        newClient.clearDisabilities();
        CheckBox checkBox;
        for(int i = 0; i < 10; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                String selected = checkBox.getText().toString();
                newClient.addToDisabilities(selected);
            }
        }
    }


    private void createNewClientForm(){
        Resources res = getResources();
        //page one: consent and date
        MultipleChoiceQuestion consent = new MultipleChoiceQuestion(getString(R.string.consent),getString(R.string.consent_newClientForm),QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer), true);
        TextQuestion date = new TextQuestion(getString(R.string.date),getString(R.string.date_newClientForm), QuestionType.DATE, false);
        FormPage pageOne = new FormPage();
        pageOne.addToPage(consent);
        pageOne.addToPage(date);
        pages.add(pageOne);

        //page two: first and last name
        TextQuestion firstName = new TextQuestion(getString(R.string.firstName),getString(R.string.firstName_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion lastName = new TextQuestion(getString(R.string.lastName),getString(R.string.lastName_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageTwo = new FormPage();
        pageTwo.addToPage(firstName);
        pageTwo.addToPage(lastName);
        pages.add(pageTwo);

        //page three: Age gender
        TextQuestion age = new TextQuestion(getString(R.string.age),getString(R.string.age_newClientForm), QuestionType.NUMBER, true);
        MultipleChoiceQuestion gender = new MultipleChoiceQuestion(getString(R.string.gender),getString(R.string.gender_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.gender), true);
        FormPage pageThree = new FormPage();
        pageThree.addToPage(age);
        pageThree.addToPage(gender);
        pages.add(pageThree);


        //page four: Location Village No. Contact Number   GPS LATER!!!!
        MultipleChoiceQuestion location = new MultipleChoiceQuestion(getString(R.string.location),getString(R.string.location_newClientForm), QuestionType.DROP_DOWN,res.getStringArray(R.array.locations), true);
        TextQuestion villageNum = new TextQuestion(getString(R.string.villageNumber),getString(R.string.villageNumber_newClientForm), QuestionType.NUMBER, true);
        TextQuestion contactNum = new TextQuestion(getString(R.string.contactNumber),getString(R.string.contactNumber_newClientForm), QuestionType.PHONE_NUMBER, true);
        FormPage pageFour = new FormPage();
        pageFour.addToPage(location);
        pageFour.addToPage(villageNum);
        pageFour.addToPage(contactNum);
        pages.add(pageFour);

        //page five: Caregiver
        MultipleChoiceQuestion caregiverPresent = new MultipleChoiceQuestion(getString(R.string.caregiverPresent),getString(R.string.caregiverPresent_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer), true);
        TextQuestion caregiverContactNumber = new TextQuestion(getString(R.string.caregiverContactNumber),getString(R.string.caregiverNumber_newClientForm), QuestionType.PHONE_NUMBER, false);
        FormPage pageFive = new FormPage();
        pageFive.addToPage(caregiverPresent);
        pageFive.addToPage(caregiverContactNumber);
        pages.add(pageFive);


        //page six: photo
        TextQuestion photo = new TextQuestion(getString(R.string.photo),getString(R.string.photo_newClientForm), QuestionType.PICTURE, false);
        imagePage = 6;
        FormPage pageSix = new FormPage();
        pageSix.addToPage(photo);
        pages.add(pageSix);




        //page seven: Type of disability
        MultipleChoiceQuestion disability = new MultipleChoiceQuestion(getString(R.string.disabilityType),getString(R.string.disabilityType_newClientForm), QuestionType.CHECK_BOX, res.getStringArray(R.array.disability_types), true);
        FormPage pageSeven = new FormPage();
        pageSeven.addToPage(disability);
        pages.add(pageSeven);


        //page eight: clients health rate, require individual goal
        MultipleChoiceQuestion clientHealthRate = new MultipleChoiceQuestion(getString(R.string.healthRate),getString(R.string.healthRate_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type), true);
        TextQuestion healthRequire = new TextQuestion(getString(R.string.healthRequire),getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion healthIndividualGoal = new TextQuestion(getString(R.string.healthIndividualGoal),getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageEight = new FormPage();
        pageEight.addToPage(clientHealthRate);
        pageEight.addToPage(healthRequire);
        pageEight.addToPage(healthIndividualGoal);
        pages.add(pageEight);


        //page nine: clients education rate
        MultipleChoiceQuestion clientEducationRate = new MultipleChoiceQuestion(getString(R.string.educationRate),getString(R.string.educationStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type), true);
        TextQuestion educationRequire = new TextQuestion(getString(R.string.educationRequire),getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion educationIndividualGoal = new TextQuestion(getString(R.string.educationIndividualGoal),getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageNine = new FormPage();
        pageNine.addToPage(clientEducationRate);
        pageNine.addToPage(educationRequire);
        pageNine.addToPage(educationIndividualGoal);
        pages.add(pageNine);

        //page ten: social status
        MultipleChoiceQuestion clientSocialRate = new MultipleChoiceQuestion(getString(R.string.socialRate),getString(R.string.socialStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type), true);
        TextQuestion socialRequire = new TextQuestion(getString(R.string.socialRequire),getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion socialIndividualGoal = new TextQuestion(getString(R.string.socialIndividualGoal),getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageTen = new FormPage();
        pageTen.addToPage(clientSocialRate);
        pageTen.addToPage(socialRequire);
        pageTen.addToPage(socialIndividualGoal);
        pages.add(pageTen);
    }

    private void reviewPage(){

        TextView consentView = new TextView(this);
        Boolean consent = newClient.getConsentToInterview();
        if(consent){
            consentView.setText("Consent to Interview: Yes");
        }
        else {
            consentView.setText("Consent to Interview: No");
        }
        form.addView(consentView);

        TextView dateView = new TextView(this);
        String formDate = newClient.getDate();
        dateView.setText("Date: " + formDate);
        form.addView(dateView);

        TextView firstNameView = new TextView(this);
        String firstName = newClient.getFirstName();
        firstNameView.setText("First Name: " + firstName);
        form.addView(firstNameView);

        TextView lastNameView = new TextView(this);
        String lastName = newClient.getLastName();
        lastNameView.setText("Last Name: " + lastName);
        form.addView(lastNameView);

        TextView ageView = new TextView(this);
        int age = newClient.getAge();
        String ageStr = Integer.toString(age);
        ageView.setText("Age: " + ageStr);
        form.addView(ageView);

        TextView genderView = new TextView(this);
        String gender = newClient.getGender();
        genderView.setText("Gender: " + gender);
        form.addView(genderView);

        TextView locationView = new TextView(this);
        String location = newClient.getLocation();
        locationView.setText("Location: " + location);
        form.addView(locationView);

        TextView villageNumberView = new TextView(this);
        int villageNumber = newClient.getVillageNumber();
        String villageNumberStr = Integer.toString(villageNumber);
        villageNumberView.setText("Village Number: " + villageNumberStr);
        form.addView(villageNumberView);

        TextView contactNumberView = new TextView(this);
        String contactNumber = newClient.getContactPhoneNumber();
        contactNumberView.setText("Contact Number: " + contactNumber);
        form.addView(contactNumberView);

        TextView caregiverPresentView = new TextView(this);
        Boolean caregiverPresent = newClient.getCaregiverPresent();
        if(caregiverPresent){
            caregiverPresentView.setText("Caregiver Present: Yes");
        }
        else {
            caregiverPresentView.setText("Caregiver Present: No");
        }
        form.addView(caregiverPresentView);

        TextView disabilitiesView = new TextView(this);
        ArrayList<String> disabilities = newClient.getDisabilities();
        String disabilitiesStr = "Type of Disability: ";
        for(int i = 0; i < disabilities.size(); i++){
            disabilitiesStr = disabilitiesStr.concat(disabilities.get(i));
            disabilitiesStr = disabilitiesStr.concat(", ");
        }
        disabilitiesView.setText(disabilitiesStr);
        form.addView(disabilitiesView);

        TextView healthRateView = new TextView(this);
        String healthRate = newClient.getHealthRate();
        healthRateView.setText("Rate of Client's health: " + healthRate);
        form.addView(healthRateView);

        TextView educationRateView = new TextView(this);
        String educationRate = newClient.getEducationRate();
        educationRateView.setText("Rate of Client's Education: " + educationRate);
        form.addView(educationRateView);

        TextView socialRateView = new TextView(this);
        String socialRate = newClient.getSocialStatusRate();
        socialRateView.setText("Rate of Client's Social Status: " + socialRate);
        form.addView(socialRateView);

    }

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(NewClientActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(NewClientActivity.this);
                startActivity(intent);
            }
        });
    }

    private void insertClient() {
        boolean success = mydb.registerClient(newClient);

        if(success) {
            Toast.makeText(NewClientActivity.this, "Entry Successful!", Toast.LENGTH_LONG).show();
            Intent intent = TaskViewActivity.makeIntent(NewClientActivity.this);
            startActivity(intent);
        } else {
            Toast.makeText(NewClientActivity.this, "Entry failed.", Toast.LENGTH_LONG).show();
        }
    }
}

