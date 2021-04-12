package com.example.cbr_manager.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Referral;
import com.example.cbr_manager.Database.ReferralManager;
import com.example.cbr_manager.Forms.DisplayFormPage;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
import com.example.cbr_manager.Forms.Question;
import com.example.cbr_manager.Forms.QuestionType;
import com.example.cbr_manager.Forms.TextQuestion;
import com.example.cbr_manager.R;

import java.io.ByteArrayOutputStream;
import java.text.Normalizer;
import java.util.ArrayList;

public class NewReferralActivity extends AppCompatActivity {
    LinearLayout form;
    int currentPage;
    int pageCount;
    int imagePage;
    //serviceRequirePage is the first page of the new referral form
    FormPage serviceRequirePage;
    ArrayList<FormPage> wheelchairPages;
    ArrayList<FormPage> physioPages;
    ArrayList<FormPage> orthoticPages;
    ArrayList<FormPage> otherExplanation;
    ArrayList<FormPage> selectedForm;
    ArrayList<FormPage> prostheticPages;
    Button next;
    Button back;
    ProgressBar progressBar;
    TextView progressText;
    byte[] imageEntry;

    ImageView imageView;

    Referral referral;

    public static final String R_CLIENT_ID_PASSED_IN = "r_client_id_passed_in";
    public static final String R_CLIENT_POS_PASSED_IN = "r_client_pos_passed_in";
    private static long client_id;
    private static int client_pos;

    private DatabaseHelper mydb;

    public static Intent makeIntent(Context context, int position, long id) {
        Intent intent =  new Intent(context, NewReferralActivity.class);
        intent.putExtra(R_CLIENT_ID_PASSED_IN, id);
        intent.putExtra(R_CLIENT_POS_PASSED_IN, position);
        return intent;
    }

    private void extractIntent(){
        Intent intent = getIntent();
        client_id = intent.getLongExtra(R_CLIENT_ID_PASSED_IN, 0);
        client_pos = intent.getIntExtra(R_CLIENT_POS_PASSED_IN, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_referral);
        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(NewReferralActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);

        extractIntent();

        mydb = new DatabaseHelper(NewReferralActivity.this);

        //Button setup
        next = (Button) findViewById(R.id.nextBtnVisit);
        back = (Button) findViewById(R.id.backBtn);
        imageView = new ImageView(this);

        //referral to save form information
        referral = new Referral();
        referral.setClientID(client_id);

        form = (LinearLayout) findViewById(R.id.form);
        progressBar = (ProgressBar) findViewById(R.id.formProgress);
        progressText = (TextView) findViewById(R.id.formProgressText);

        currentPage = 1;

        //set up FormPages
        serviceRequirePage = new FormPage();
        otherExplanation = new ArrayList<>();
        wheelchairPages = new ArrayList<>();
        physioPages = new ArrayList<>();
        orthoticPages = new ArrayList<>();
        prostheticPages = new ArrayList<>();

        imagePage = -1;

        createNewReferralForm();

        pageCount = 5;

        DisplayFormPage.displayPage(serviceRequirePage, form, this, 0, 0);

        progressBar.setMax(pageCount);

        setProgress(currentPage, pageCount);


        next.setOnClickListener(v -> {
            if(next.getText().toString().equals("Finish")){
                insertReferral();
            }
            else if(!fieldsFilled(currentPage)){
                requiredFieldsToast();
            }
            else if(currentPage < pageCount - 1){
                if(currentPage == 1){
                    getSelectedForm();
                    pageCount = selectedForm.size() + 2;
                    back.setClickable(true);
                    back.setVisibility(View.VISIBLE);
                    savePage(serviceRequirePage);
                }
                else{
                    savePage(selectedForm.get(currentPage - 2));
                }

                currentPage++;
                setProgress(currentPage, pageCount);

                clearForm();

                if(currentPage == imagePage){
                    displayPicture(selectedForm.get(currentPage - 2));
                }
                else{
                    DisplayFormPage.displayPage(selectedForm.get(currentPage - 2), form, NewReferralActivity.this, 0, 0);
                }

                loadAnswers(selectedForm.get(currentPage - 2));
            }
            else if(currentPage == pageCount - 1){
                savePage(selectedForm.get(currentPage - 2));
                currentPage++;
                setProgress(currentPage, pageCount);
                clearForm();
                reviewPage();
                next.setText(R.string.finish);
            }
        });

        //setup back button
        back.setOnClickListener(v -> {
            if(currentPage == pageCount){
                next.setText(R.string.next);
            }
            currentPage--;
            setProgress(currentPage, pageCount);
            clearForm();

            if(currentPage == imagePage){
                displayPicture(selectedForm.get(currentPage - 2));
            }
            else if(currentPage == 1){
                DisplayFormPage.displayPage(serviceRequirePage, form, NewReferralActivity.this, 0, 0);
            }
            else{
                DisplayFormPage.displayPage(selectedForm.get(currentPage - 2), form, NewReferralActivity.this, 0, 0);
            }
            //load previously saved answers if any

            if(currentPage == 1){
                back.setClickable(false);
                back.setVisibility(View.INVISIBLE);
                loadAnswers(serviceRequirePage);
            }
            else{
                loadAnswers(selectedForm.get(currentPage - 2));
            }
        });
        back.setClickable(false);
        back.setVisibility(View.INVISIBLE);

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
        progressBar.setMax(pageCount);
        progressText.setText(currentPage + "/" + pageCount);
    }

    private void clearForm(){
        form.removeAllViews();
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

    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(captureImage);
            imageEntry = imageViewToByte(imageView);
        }
    }

    private void getSelectedForm(){
        RadioGroup radioGroup = form.findViewWithTag(getString(R.string.serviceRequire));
        int id = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(id);
        String selected = radioButton.getText().toString();
        if(selected.equals("Physiotherapy")){
            selectedForm = physioPages;
            imagePage = 2;
        }
        else if(selected.equals("Prosthetic")){
            selectedForm = prostheticPages;
            imagePage = 3;
        }
        else if(selected.equals("Orthotic")){
            selectedForm = orthoticPages;
            imagePage = 3;
        }
        else if(selected.equals("Wheelchair")){
            selectedForm = wheelchairPages;
            imagePage = 2;
        }
        else if(selected.equals("Other")){
            selectedForm = otherExplanation;
            imagePage = 3;
        }
    }

    private void requiredFieldsToast(){
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(this, "Fill all Required Fields", duration).show();
    }

    private Boolean fieldsFilled(int currentPage){
        if(currentPage == 1){
            return requiredFieldsFilled(serviceRequirePage);
        }
        else{
            return requiredFieldsFilled(selectedForm.get(currentPage - 2));
        }

    }

    private Boolean requiredFieldsFilled(FormPage page){
        ArrayList<Question> questions = page.getQuestions();
        Boolean returnBool = true;
        for(Question question : questions){
            QuestionType type = question.getQuestionType();
            if(question.getRequired()){
                if(type == QuestionType.PLAIN_TEXT){
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
                else if(type == QuestionType.DROP_DOWN){
                    if(!isDropDownAnswered(question)){
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
        boolean repairCondition = true;

        if(radioGroup.getCheckedRadioButtonId() == -1){
            isAnswered = false;
        }

        //test if optional caregiver phone number field is filled if caregiver present
        if(tag.equals(getString(R.string.existingChair)) && isAnswered){
            int id = radioGroup.getCheckedRadioButtonId();
            RadioButton button = (RadioButton) radioGroup.findViewById(id);
            if(button.getText().equals("Yes")){
                RadioGroup beRepaired = (RadioGroup) form.findViewWithTag(getString(R.string.repairChair));
                if(beRepaired.getCheckedRadioButtonId() == -1){
                    repairCondition = false;
                }
            }
        }


        return isAnswered && repairCondition;
    }

    private Boolean isDropDownAnswered(Question question){
        Boolean returnBool = true;
        String tag = question.getQuestionTag();
        Spinner spinner = (Spinner) form.findViewWithTag(tag);
        String selected = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        if(tag.equals(getString(R.string.clientCondition))) {
            if(selected.equals("Other")){
                returnBool = false;
                EditText input = (EditText) form.findViewWithTag(getString(R.string.otherConditionDescribe));
                if(input.getText().toString().trim().length() > 0){
                    returnBool = true;
                }
            }
        }

        return returnBool;

    }



    private void loadAnswers(FormPage page){
        ArrayList<Question> questions = page.getQuestions();
        for(Question question : questions){
            QuestionType type = question.getQuestionType();
            if(type == QuestionType.PLAIN_TEXT){
                loadText(question);
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
        }
    }

    private void loadText(Question question){
        String tag = question.getQuestionTag();
        String data = null;

        if(tag.equals(getString(R.string.otherDescribe))){
            data = referral.getOtherExplanation();
        }

        if(data != null){
            EditText input = (EditText) form.findViewWithTag(tag);
            input.setText(data);
        }

    }

    private void loadRadio(Question question){
        String tag = question.getQuestionTag();
        if(tag.equals(getString(R.string.existingChair)) || tag.equals(getString(R.string.repairChair))){
            loadRadioBool(question);
        }
        else{
            loadRadioMultiple(question);
        }
    }

    private void loadRadioBool(Question question){
        String tag = question.getQuestionTag();
        Boolean data = null;
        if(tag.equals(getString(R.string.existingChair))){
            data = referral.getHasWheelchair();
        }
        else if(tag.equals(getString(R.string.repairChair))){
            data = referral.getWheelchairReparable();
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

        if(tag.equals(getString(R.string.serviceRequire))){
            data = referral.getServiceReq();
        }
        else if(tag.equals(getString(R.string.basicIntermediateUser))){
            data = referral.getBasicOrInter();
        }
        else if(tag.equals(getString(R.string.injuryLocation))){
            data = referral.getInjuryLocation();
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
        if(tag.equals(getString(R.string.hipWidth))){
            data = referral.getHipWidth();
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
        if(tag.equals(getString(R.string.clientCondition))){
            data = referral.getCondition();
            if(data != null){
                if(data.equals("Other")){
                    String explanation = referral.getConditionOtherExplanation();
                    EditText otherExplanationView = form.findViewWithTag(getString(R.string.otherConditionDescribe));
                    otherExplanationView.setText(explanation);
                }
            }
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




    private void savePage(FormPage page){
        ArrayList<Question> questions = page.getQuestions();
        for(Question question : questions){
            QuestionType type = question.getQuestionType();
            if(type == QuestionType.PLAIN_TEXT){
                saveText(question);
            }
            else if(type == QuestionType.RADIO){
                saveRadio(question);
            }
            else if(type == QuestionType.NUMBER){
                saveNumber(question);
            }
            else if(type == QuestionType.DROP_DOWN){
                saveDropDown(question);
            } else if(type == QuestionType.PICTURE){
                referral.setReferralPhoto(imageEntry);
            }
        }
    }

    private void saveText(Question question){
        String tag = question.getQuestionTag();
        EditText input = (EditText) form.findViewWithTag(tag);
        if(tag.equals(getString(R.string.otherDescribe))){
            referral.setOtherExplanation(input.getText().toString());
        }
    }

    private void saveRadio(Question question){
        String tag = question.getQuestionTag();
        if(tag.equals(getString(R.string.existingChair)) || tag.equals(getString(R.string.repairChair))){
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

        if(tag.equals(getString(R.string.existingChair))){
            referral.setHasWheelchair(radioButton.getText().equals("Yes"));
        }
        else if(tag.equals(getString(R.string.repairChair))){
            if(buttonId != -1){
                referral.setWheelchairReparable(radioButton.getText().equals("Yes"));
                referral.setBringToCentre(radioButton.getText().equals("Yes"));
            }

        }
    }

    private void saveRadioMultiple(Question question){
        String tag = question.getQuestionTag();
        RadioGroup radioGroup = (RadioGroup) form.findViewWithTag(tag);
        RadioButton radioButton;
        for(int i = 0; i < radioGroup.getChildCount(); i++){
            radioButton = (RadioButton) radioGroup.getChildAt(i);
            if(radioButton.isChecked()){
                if(tag.equals(getString(R.string.serviceRequire))){
                    referral.setServiceReq(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.basicIntermediateUser))){
                    referral.setBasicOrInter(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.injuryLocation))){
                    referral.setInjuryLocation(radioButton.getText().toString());
                }
            }
        }
    }

    private void saveNumber(Question question){
        String tag = question.getQuestionTag();
        EditText input = (EditText) form.findViewWithTag(tag);
        String inputStr = input.getText().toString();
        int num = Integer.parseInt(inputStr);
        if(tag.equals(getString(R.string.hipWidth))){
            referral.setHipWidth(num);
        }
    }

    private void saveDropDown(Question question){
        String tag = question.getQuestionTag();
        Spinner spinner = (Spinner) form.findViewWithTag(tag);
        String selected = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        if(tag.equals(getString(R.string.clientCondition))) {
            referral.setCondition(selected);
            if(selected.equals("Other")){
                EditText input = (EditText) form.findViewWithTag(getString(R.string.otherConditionDescribe));
                referral.setConditionOtherExplanation(input.getText().toString());
            }
        }
    }


    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(NewReferralActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(NewReferralActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(NewReferralActivity.this);
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

    private void createNewReferralForm(){
        setUniqueReferralId();

        Resources res = getResources();
        //first Page
        MultipleChoiceQuestion require = new MultipleChoiceQuestion(getString(R.string.serviceRequire), getString(R.string.serviceRequireQuestion_newReferralForm), QuestionType.RADIO, res.getStringArray(R.array.serviceType), true);
        serviceRequirePage.addToPage(require);

        //set up wheelchair form pages
        TextQuestion wheelchairPhoto = new TextQuestion(getString(R.string.photoWheelchair), getString(R.string.photoQuestion_newReferralForm), QuestionType.PICTURE, false);
        FormPage wheelchairOne = new FormPage();
        wheelchairOne.addToPage(wheelchairPhoto);
        wheelchairPages.add(wheelchairOne);

        MultipleChoiceQuestion basicIntermediate = new MultipleChoiceQuestion(getString(R.string.basicIntermediateUser), getString(R.string.typeUserQuestion_newReferralForm), QuestionType.RADIO, res.getStringArray(R.array.basicIntermediate), true);
        TextQuestion hipWidth = new TextQuestion(getString(R.string.hipWidth), getString(R.string.hipWidthQuestion_newReferralForm), QuestionType.NUMBER, true);
        FormPage wheelchairTwo = new FormPage();
        wheelchairTwo.addToPage(basicIntermediate);
        wheelchairTwo.addToPage(hipWidth);
        wheelchairPages.add(wheelchairTwo);

        MultipleChoiceQuestion existingChair = new MultipleChoiceQuestion(getString(R.string.existingChair), getString(R.string.existingChairQuestion_newReferralForm), QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer), true);
        MultipleChoiceQuestion repairChair = new MultipleChoiceQuestion(getString(R.string.repairChair), getString(R.string.repairedQuestion_newReferralForm), QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer), false);
        FormPage wheelchairThree = new FormPage();
        wheelchairThree.addToPage(existingChair);
        wheelchairThree.addToPage(repairChair);
        wheelchairPages.add(wheelchairThree);

        //set up physiotherapy form pages
        TextQuestion physioPhoto = new TextQuestion(getString(R.string.photoPhysio), getString(R.string.photoQuestion_newReferralForm), QuestionType.PICTURE, false);
        FormPage physioOne = new FormPage();
        physioOne.addToPage(physioPhoto);
        physioPages.add(physioOne);

        MultipleChoiceQuestion physioCondition = new MultipleChoiceQuestion(getString(R.string.clientCondition),getString(R.string.clientConditionQuestion_newReferralForm), QuestionType.DROP_DOWN, res.getStringArray(R.array.physioConditionType), true);
        TextQuestion otherConditionExplanation = new TextQuestion(getString(R.string.otherConditionDescribe), getString(R.string.conditionOtherExplanation_newReferralForm), QuestionType.PLAIN_TEXT, false);
        FormPage physioTwo = new FormPage();
        physioTwo.addToPage(physioCondition);
        physioTwo.addToPage(otherConditionExplanation);
        physioPages.add(physioTwo);


        //set up orthotic pages
        MultipleChoiceQuestion ortQuestion = new MultipleChoiceQuestion(getString(R.string.injuryLocation), getString(R.string.injuryLocationElbowQuestion_newReferralForm), QuestionType.RADIO, res.getStringArray(R.array.aboveBelow), true);
        FormPage firstPageOrth = new FormPage();
        firstPageOrth.addToPage(ortQuestion);
        orthoticPages.add(firstPageOrth);

        TextQuestion orthPhotoQuestion = new TextQuestion("photoOrth", getString(R.string.photoQuestion_newReferralForm), QuestionType.PICTURE, false);
        FormPage secondPageOrth = new FormPage();
        secondPageOrth.addToPage(orthPhotoQuestion);
        orthoticPages.add(secondPageOrth);


        //set up prosthetic
        MultipleChoiceQuestion prosQuestion = new MultipleChoiceQuestion(getString(R.string.injuryLocation), getString(R.string.injuryLocationKneeQuestion_newReferralForm), QuestionType.RADIO, res.getStringArray(R.array.aboveBelow), true);
        FormPage firstPagePros = new FormPage();
        firstPagePros.addToPage(prosQuestion);
        prostheticPages.add(firstPagePros);

        TextQuestion prosPhotoQuestion = new TextQuestion("photoPros", getString(R.string.photoQuestion_newReferralForm), QuestionType.PICTURE, false);
        FormPage secondPagePros = new FormPage();
        secondPagePros.addToPage(prosPhotoQuestion);
        prostheticPages.add(secondPagePros);

        //Other pages
        TextQuestion explanation = new TextQuestion(getString(R.string.otherDescribe), getString(R.string.pleaseDescribe_newReferralForm), QuestionType.PLAIN_TEXT, true);
        FormPage otherOne = new FormPage();
        otherOne.addToPage(explanation);
        otherExplanation.add(otherOne);

        TextQuestion otherPhotoQuestion = new TextQuestion("photoOther", getString(R.string.photoQuestion_newReferralForm), QuestionType.PICTURE, false);
        FormPage otherTwo = new FormPage();
        otherTwo.addToPage(otherPhotoQuestion);
        otherExplanation.add(otherTwo);
    }

    private void reviewPage(){
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        float txtSize = 18;
        reviewTitle(txtSize);

        TextView serviceTypeView = new TextView(this);
        String serviceType = referral.getServiceReq();
        serviceTypeView.setText(serviceType);
        serviceTypeView.setTextSize(txtSize);
        layout.addView(serviceTypeView);

        insertLineDivide(layout);

        if(serviceType.equals("Physiotherapy")){
            String condition = referral.getCondition();
            String otherCondition = referral.getConditionOtherExplanation();
            TextView conditionView = new TextView(this);
            if(condition.equalsIgnoreCase("Other")){
                conditionView.setText("Condition: " + otherCondition);
            }
            else{
                conditionView.setText("Condition: " + condition);
            }
            conditionView.setTextSize(txtSize);
            layout.addView(conditionView);
        }
        else if(serviceType.equals("Prosthetic")){
            String injuryLocation = referral.getInjuryLocation();
            TextView injuryLocationView = new TextView(this);
            injuryLocationView.setText("Injury above or below the knee: " + injuryLocation);
            injuryLocationView.setTextSize(txtSize);
            layout.addView(injuryLocationView);
        }
        else if(serviceType.equals("Orthotic")){
            String injuryLocation = referral.getInjuryLocation();
            TextView injuryLocationView = new TextView(this);
            injuryLocationView.setText("Injury above or below the elbow: " + injuryLocation);
            injuryLocationView.setTextSize(txtSize);
            layout.addView(injuryLocationView);
        }
        else if(serviceType.equals("Wheelchair")){
            String userType = referral.getBasicOrInter();
            TextView userTypeView = new TextView(this);
            userTypeView.setText("Type of user: " + userType);
            userTypeView.setTextSize(txtSize);
            layout.addView(userTypeView);

            int hipWidth = referral.getHipWidth();
            String hipWidthString = Integer.toString(hipWidth);
            TextView hipWidthView = new TextView(this);
            hipWidthView.setText("Hip Width(inches): " + hipWidthString);
            hipWidthView.setTextSize(txtSize);
            layout.addView(hipWidthView);

            insertLineDivide(layout);

            Boolean existingChair = referral.getHasWheelchair();
            TextView existingChairView = new TextView(this);
            existingChairView.setTextSize(txtSize);
            if(existingChair){
                existingChairView.setText("Existing Chair: Yes");
                layout.addView(existingChairView);
                Boolean canBeFixed = referral.getWheelchairReparable();
                TextView canBeFixedView = new TextView(this);
                canBeFixedView.setTextSize(txtSize);
                if(canBeFixed){
                    canBeFixedView.setText("Can be Fixed: Yes");
                }
                else{
                    canBeFixedView.setText("Can be Fixed: No");
                }
                layout.addView(canBeFixedView);
            }
            else{
                existingChairView.setText("Existing Chair: No");
                layout.addView(existingChairView);
            }


        }
        else if(serviceType.equals("Other")){
            String otherExplanation = referral.getOtherExplanation();
            TextView otherExplanationView = new TextView(this);
            otherExplanationView.setText("Explanation: " + otherExplanation);
            otherExplanationView.setTextSize(txtSize);
            layout.addView(otherExplanationView);
        }

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


    private void insertReferral() {
        referral.setOutcome("UNRESOLVED");//default case

        boolean success = mydb.addReferral(referral);

        if(success) {
            ReferralManager referralManager = ReferralManager.getInstance(this);
            referralManager.addReferral(referral);
            Toast.makeText(NewReferralActivity.this, "Entry Successful!", Toast.LENGTH_LONG).show();
            Intent intent = ClientInfoActivity.makeIntent(NewReferralActivity.this, client_pos,  client_id);
            startActivity(intent);
        } else {
            Toast.makeText(NewReferralActivity.this, "Entry failed.", Toast.LENGTH_LONG).show();
        }
    }

    private void setUniqueReferralId(){
        DatabaseHelper db =  new DatabaseHelper(NewReferralActivity.this);

        int referral_no = db.numberOfReferralsPerClient(referral.getClientID());
        referral_no++;//next available referral id

        // Concatenate both strings
        String uniqueID = String.valueOf(referral.getClientID()) + String.valueOf(referral_no);

        // Convert the concatenated string to integer
        long uniqueID_long = Long.parseLong(uniqueID);

        referral.setId(uniqueID_long);
    }
}