package com.example.cbr_manager.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class NewClientActivity extends AppCompatActivity {

//    private static final String TAG = "INSERTED";

    private static final String TAG = "ERROR";

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

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    Location location_GPS;
    double latitude;
    double longitude;
    TextView textLatLong;
    byte[] imageEntry;


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

        back = (Button) findViewById(R.id.backBtn);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    NewClientActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getCurrentLocation();
        }

        newClient = new Client();
        imageView = new ImageView(this);

        form = (LinearLayout) findViewById(R.id.form);
        progressBar = (ProgressBar) findViewById(R.id.formProgress);
        progressText = (TextView) findViewById(R.id.formProgressText);

        currentPage = 1;
        pages = new ArrayList<>();

        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(NewClientActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);

        createNewClientForm();
        pageCount = pages.size() + 1;

        DisplayFormPage.displayPage(pages.get(currentPage-1), form, this, latitude, longitude);

        progressBar.setMax(pageCount);

        setProgress(currentPage, pageCount);

        next.setOnClickListener(v -> {
            if (currentPage == pageCount) {
                insertClient();
            }

            //make sure all required fields are filled in the page
            else if(!requiredFieldsFilled(pages.get(currentPage - 1))){
                requiredFieldsToast();
            }
            else if(currentPage < pageCount - 1){
                if(currentPage == 1){
                    back.setClickable(true);
                    back.setVisibility(View.VISIBLE);

                    back.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_form_buttons));


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
                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this, latitude, longitude);

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
                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this, latitude, longitude);
                }
                //load previously saved answers if any
                loadAnswers(pages.get(currentPage - 1));
                if(currentPage == 1){
                    back.setClickable(false);
                    back.setVisibility(View.INVISIBLE);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getCurrentLocation();
        } else {
            //Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }

        LocationServices.getFusedLocationProviderClient(NewClientActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(NewClientActivity.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() -1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            textLatLong = new TextView(NewClientActivity.this);
                            textLatLong.setText(
                                    String.format(
                                            "Latitude: %s\nLongitude: %s",
                                            latitude,
                                            longitude
                                    )
                            );
                            location_GPS = new Location("providerNA");
                            location_GPS.setLatitude(latitude);
                            location_GPS.setLongitude(longitude);
                        }

                    }

                }, Looper.getMainLooper());
    }


    private void setProgress(int currentPage, int pageCount){
        progressBar.setProgress(currentPage);
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
                Toast.makeText(NewClientActivity.this, "Please provide consent to continue.", Toast.LENGTH_LONG).show();
                consentCondition = false;
            }
        }

        return isAnswered && caregiverPresentCondition && consentCondition;
    }

    private Boolean isCheckBoxAnswered(Question question){
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        Boolean returnBool = false;
        Boolean otherExplanationRequirement = true;
        CheckBox checkBox;
        for(int i = 0; i < mcq.getAnswers().length; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                returnBool = true;
                if(checkBox.getText().toString().equals("Other")){
                    otherExplanationRequirement = false;
                    EditText input = (EditText) form.findViewWithTag("otherExplanation");
                    if(input.getText().toString().trim().length() > 0){
                        otherExplanationRequirement = true;
                    }
                }
            }
        }
        return returnBool && otherExplanationRequirement;
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
            for(int j = 0; j < 10; j++){
                checkBox = (CheckBox) form.findViewWithTag(j);
                if(checkBox.getText().equals(disabilities.get(i))){

                    checkBox.toggle();
                    if(checkBox.getText().toString().equals("Other")){
                        EditText explanation = (EditText) form.findViewWithTag("otherExplanation");
                        String explanationString = newClient.getOtherExplanation();
                        explanation.setVisibility(View.VISIBLE);
                        explanation.setText(explanationString);
                    }
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
            else if(type == QuestionType.GPS){
                saveGPS(question);
            }
            else if(type == QuestionType.PICTURE){
                savePicture(question);
            }

        }
    }
    private void saveGPS(Question question) {
        String tag = question.getQuestionTag();
        if (tag.equals("GPS")) {
            newClient.setLatitude(latitude);
            newClient.setLongitude(longitude);
        }
    }

    private void savePicture(Question question) {
        String tag = question.getQuestionTag();
        if (tag.equals("photo")) {
            newClient.setPhoto(imageEntry);
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
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        String tag = question.getQuestionTag();
        newClient.clearDisabilities();
        CheckBox checkBox;
        for(int i = 0; i < mcq.getAnswers().length; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                String selected = checkBox.getText().toString();
                newClient.addToDisabilities(selected);
                System.out.println(selected);
                if(checkBox.getText().toString().equals("Other")){
                    EditText explanation = (EditText) form.findViewWithTag("otherExplanation");
                    newClient.setOtherExplanation(explanation.getText().toString());
                }
            }
        }
    }

    private void createNewClientForm(){
        setUniqueClientId();
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

        TextQuestion gps = new TextQuestion("GPS", "GPS: ", QuestionType.GPS, false);

        FormPage pageFour = new FormPage();
        pageFour.addToPage(location);
        pageFour.addToPage(villageNum);
        pageFour.addToPage(contactNum);
        pageFour.addToPage(gps);
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
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        float txtSize = 18;
        reviewTitle(txtSize);
        TextView consentView = new TextView(this);
        consentView.setTextSize(txtSize);
        Boolean consent = newClient.getConsentToInterview();
        if(consent){
            consentView.setText("Consent to Interview: Yes");
        }
        else {
            consentView.setText("Consent to Interview: No");
        }
        layout.addView(consentView);

        TextView dateView = new TextView(this);
        String formDate = newClient.getDate();
        dateView.setText("Date: " + formDate);
        dateView.setTextSize(txtSize);
        layout.addView(dateView);

        insertLineDivide(layout);

        TextView firstNameView = new TextView(this);
        String firstName = newClient.getFirstName();
        firstNameView.setText("First Name: " + firstName);
        firstNameView.setTextSize(txtSize);
        layout.addView(firstNameView);

        TextView lastNameView = new TextView(this);
        String lastName = newClient.getLastName();
        lastNameView.setText("Last Name: " + lastName);
        lastNameView.setTextSize(txtSize);
        layout.addView(lastNameView);

        insertLineDivide(layout);

        TextView ageView = new TextView(this);
        int age = newClient.getAge();
        String ageStr = Integer.toString(age);
        ageView.setText("Age: " + ageStr);
        ageView.setTextSize(txtSize);
        layout.addView(ageView);

        TextView genderView = new TextView(this);
        String gender = newClient.getGender();
        genderView.setText("Gender: " + gender);
        genderView.setTextSize(txtSize);
        layout.addView(genderView);

        insertLineDivide(layout);

        TextView locationView = new TextView(this);
        String location = newClient.getLocation();
        locationView.setText("Location: " + location);
        locationView.setTextSize(txtSize);
        layout.addView(locationView);

        TextView villageNumberView = new TextView(this);
        int villageNumber = newClient.getVillageNumber();
        String villageNumberStr = Integer.toString(villageNumber);
        villageNumberView.setText("Village Number: " + villageNumberStr);
        villageNumberView.setTextSize(txtSize);
        layout.addView(villageNumberView);

        TextView gpsView = new TextView(this);
        double lat = newClient.getLatitude();
        double lon = newClient.getLongitude();
        gpsView.setText("GPS: " + lat + ", " + lon);
        gpsView.setTextSize(txtSize);
        layout.addView(gpsView);

        TextView contactNumberView = new TextView(this);
        String contactNumber = newClient.getContactPhoneNumber();
        contactNumberView.setText("Contact Number: " + contactNumber);
        contactNumberView.setTextSize(txtSize);
        layout.addView(contactNumberView);

        insertLineDivide(layout);

        TextView caregiverPresentView = new TextView(this);
        Boolean caregiverPresent = newClient.getCaregiverPresent();
        if(caregiverPresent){
            caregiverPresentView.setText("Caregiver Present: Yes");
            TextView caregiverNumberView = new TextView(this);
            String caregiverNumber = newClient.getCaregiverPhoneNumber();
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
        ArrayList<String> disabilities = newClient.getDisabilities();
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
        String healthRate = newClient.getHealthRate();
        healthRateView.setText("Rate of Client's health: " + healthRate);
        healthRateView.setTextSize(txtSize);
        layout.addView(healthRateView);

        TextView healthRequireView = new TextView(this);
        String healthRequire = newClient.getHealthRequire();
        healthRequireView.setText("Require: " + healthRequire);
        healthRequireView.setTextSize(txtSize);
        layout.addView(healthRequireView);

        TextView healthGoalView = new TextView(this);
        String healthGoal = newClient.getHealthIndividualGoal();
        healthGoalView.setText("Individual Goal: " + healthGoal);
        healthGoalView.setTextSize(txtSize);
        layout.addView(healthGoalView);

        insertLineDivide(layout);

        TextView educationRateView = new TextView(this);
        String educationRate = newClient.getEducationRate();
        educationRateView.setText("Rate of Client's Education: " + educationRate);
        educationRateView.setTextSize(txtSize);
        layout.addView(educationRateView);

        TextView educationRequireView = new TextView(this);
        String educationRequire = newClient.getEducationRequire();
        educationRequireView.setText("Require: " + educationRequire);
        educationRequireView.setTextSize(txtSize);
        layout.addView(educationRequireView);

        TextView educationGoalView = new TextView(this);
        String educationGoal = newClient.getEducationIndividualGoal();
        educationGoalView.setText("Individual Goal: " + educationGoal);
        educationGoalView.setTextSize(txtSize);
        layout.addView(educationGoalView);

        insertLineDivide(layout);

        TextView socialRateView = new TextView(this);
        String socialRate = newClient.getSocialStatusRate();
        socialRateView.setText("Rate of Client's Social Status: " + socialRate);
        socialRateView.setTextSize(txtSize);
        layout.addView(socialRateView);

        TextView socialRequireView = new TextView(this);
        String socialRequire = newClient.getSocialStatusRequire();
        socialRequireView.setText("Require: " + socialRequire);
        socialRequireView.setTextSize(txtSize);
        layout.addView(socialRequireView);

        TextView socialGoalView = new TextView(this);
        String socialGoal = newClient.getSocialStatusIndividualGoal();
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        reviewTitle.setLayoutParams(params);
        form.addView(reviewTitle);
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

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(NewClientActivity.this);
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

    private void insertClient() {
        newClient.setIsSynced(0);
        boolean success = mydb.registerClient(newClient);
        if(success) {
            ClientManager clientManager = ClientManager.getInstance(this);
            clientManager.addClient(newClient);

            Toast.makeText(NewClientActivity.this, "Entry Successful!", Toast.LENGTH_LONG).show();
            Intent intent = TaskViewActivity.makeIntent(NewClientActivity.this);
            startActivity(intent);
        } else {
            Toast.makeText(NewClientActivity.this, "Entry Failed.", Toast.LENGTH_LONG).show();
        }
    }
    private void setUniqueClientId(){
        DatabaseHelper db =  new DatabaseHelper(NewClientActivity.this);

        // Convert both the integers to string
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        int worker_id = db.getWorkerId(username);
        newClient.setClient_worker_id(worker_id);

        int client_no = db.numberOfClientsPerUser(worker_id);
        client_no++;//next available client id

        // Concatenate both strings
        String uniqueID = String.valueOf(worker_id * 100) + String.valueOf(client_no);

        // Convert the concatenated string
        // to integer
        long uniqueID_long = Long.parseLong(uniqueID);

        newClient.setId(uniqueID_long);
    }
}

