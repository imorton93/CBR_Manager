package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.Forms.*;

import java.util.ArrayList;
import java.util.Calendar;

public class NewClientActivity extends AppCompatActivity {
    LinearLayout form;
    int currentPage;
    int pageCount;
    ArrayList<FormPage> pages;
    Button next;
    Button back;
    ProgressBar progressBar;
    TextView progressText;

    //structure to save all the answers
    NewClient newClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);

        next = (Button) findViewById(R.id.nextBtn);
        next.setBackgroundColor(Color.BLUE);
        back = (Button) findViewById(R.id.backBtn);
        newClient = new NewClient();

        form = (LinearLayout) findViewById(R.id.form);
        progressBar = (ProgressBar) findViewById(R.id.formProgress);
        progressText = (TextView) findViewById(R.id.formProgressText);

        currentPage = 1;
        pages = new ArrayList<>();


        createNewClientForm();
        pageCount = pages.size();

        DisplayFormPage.displayPage(pages.get(currentPage-1), form, this);

        progressText.setText(currentPage + "/" + pageCount);
        progressBar.setMax(pageCount);
        progressBar.setProgress(currentPage);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(currentPage < pageCount){
                    if(currentPage == 1){
                        back.setClickable(true);
                        back.setBackgroundColor(Color.BLUE);

                    }
                    //save answers
                    savePageAnswers(currentPage);

                    currentPage++;
                    setProgress(currentPage, pageCount);

                    clearForm();
                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this);

                    //load previously saved answers if any
                    loadAnswers(currentPage);
                    if(currentPage == pageCount){
                        next.setText(R.string.finish);
                    }
                }
                else if(currentPage >= pageCount){
                    finishForm();
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

                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this);

                    //load previously saved answers if any
                    loadAnswers(currentPage);
                    if(currentPage == 1){
                        back.setClickable(false);
                        back.setBackgroundColor(Color.DKGRAY);
                    }
            }
        });
        back.setClickable(false);
        back.setBackgroundColor(Color.DKGRAY);
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


    private void loadAnswers(int currentPage){
        switch (currentPage) {
            case 1:
                loadPageOneAnswers();
                break;
            case 2:
                loadPageTwoAnswers();
                break;
            case 3:
                loadPageThreeAnswers();
                break;
            case 4:
                loadPageFourAnswers();
                break;
            case 5:
                loadPageFiveAnswers();
                break;
            case 6:
                loadPageSixAnswers();
                break;
            case 7:
                loadPageSevenAnswers();
                break;
            case 8:
                loadPageEightAnswers();
                break;
            case 9:
                loadPageNineAnswers();
                break;
            case 10:
                loadPageTenAnswers();
                break;
        }
    }

    private void loadPageOneAnswers(){
        Boolean consentToInterview = newClient.getConsentToInterview();
        if(consentToInterview != null){
            RadioGroup consent = (RadioGroup) form.findViewWithTag(1);
            RadioButton button;
            if(consentToInterview){
                button = (RadioButton) consent.getChildAt(0);
            }
            else{
                button = (RadioButton) consent.getChildAt(1);
            }
            button.toggle();
        }
        String savedDate = newClient.getDate();
        if(savedDate != null){
            TextView date = (TextView) form.findViewWithTag(2);
            date.setText(savedDate);
        }


    }

    private void loadPageTwoAnswers(){
        String firstName = newClient.getFirstName();
        if(firstName != null){
            EditText inputFirst = (EditText) form.findViewWithTag(1);
            inputFirst.setText(firstName);
        }

        String lastName = newClient.getLastName();
        if(lastName != null){
            EditText inputSecond = (EditText) form.findViewWithTag(2);
            inputSecond.setText(lastName);
        }
    }

    private void loadPageThreeAnswers(){
        int age = newClient.getAge();
        String ageStr = Integer.toString(age);
        if(!ageStr.isEmpty()){
            EditText ageInput = (EditText) form.findViewWithTag(1);
            ageInput.setText(ageStr);
        }

        String gender = newClient.getGender();
        if(gender != null){
            RadioGroup genderGroup = (RadioGroup) form.findViewWithTag(2);
            for(int i = 0; i < genderGroup.getChildCount(); i++){
                RadioButton button = (RadioButton) genderGroup.getChildAt(i);
                if(button.getText().equals(gender)){
                    button.toggle();
                }
            }
        }
    }

    private void loadPageFourAnswers(){
        String location = newClient.getLocation();

        if(location != null){
            Spinner locationSpinner = (Spinner) form.findViewWithTag(1);
            locationSpinner.setPrompt(location);
        }

        int villageNumber = newClient.getVillageNumber();
        String villageNumberStr = Integer.toString(villageNumber);
        if(!villageNumberStr.isEmpty()){
            EditText editText = (EditText) form.findViewWithTag(2);
            editText.setText(villageNumberStr);
        }

        String contactNumber = newClient.getContactPhoneNumber();
        if(contactNumber != null){
            EditText editText2 = (EditText) form.findViewWithTag(3);
            editText2.setText(contactNumber);
        }

    }

    private void loadPageFiveAnswers(){
        Boolean caregiverPresent = newClient.getCaregiverPresent();
        if(caregiverPresent != null){
            RadioGroup consent = (RadioGroup) form.findViewWithTag(1);
            RadioButton radioButton;
            if(caregiverPresent){
                radioButton = (RadioButton) consent.getChildAt(0);
                radioButton.toggle();
            }
            else{
                radioButton = (RadioButton) consent.getChildAt(1);
                radioButton.toggle();
            }
        }

        String contactNumber = newClient.getCaregiverPhoneNumber();
        if(contactNumber != null){
            EditText input = (EditText) form.findViewWithTag(2);
            input.setText(contactNumber);
        }
    }

    private void loadPageSixAnswers(){
        //photo
    }

    private void loadPageSevenAnswers(){
        ArrayList<String> disabilities = newClient.getDisabilities();
        for(int i = 0; i < disabilities.size(); i++){
            CheckBox checkBox;
            for(int j = 1; j <= 10; j++){
                checkBox = (CheckBox) form.findViewWithTag(j);
                if(checkBox.getText().equals(disabilities.get(i))){
                    checkBox.toggle();
                }
            }
        }
    }

    private void loadPageEightAnswers(){
        String rate = newClient.getHealthRate();
        if(rate != null){
            RadioGroup rateGroup = (RadioGroup) form.findViewWithTag(1);
            for(int i = 0; i < rateGroup.getChildCount(); i++){
                RadioButton button = (RadioButton) rateGroup.getChildAt(i);
                if(button.getText().equals(rate)){
                    button.toggle();
                }
            }
        }

        String require = newClient.getHealthRequire();
        if(require != null){
            EditText input = (EditText) form.findViewWithTag(2);
            input.setText(require);
        }

        String individualGoal = newClient.getHealthIndividualGoal();
        if(individualGoal != null){
            EditText input = (EditText) form.findViewWithTag(3);
            input.setText(individualGoal);
        }

    }

    private void loadPageNineAnswers(){
        String rate = newClient.getEducationRate();
        if(rate != null){
            RadioGroup rateGroup = (RadioGroup) form.findViewWithTag(1);
            for(int i = 0; i < rateGroup.getChildCount(); i++){
                RadioButton button = (RadioButton) rateGroup.getChildAt(i);
                if(button.getText().toString().equals(rate)){
                    button.toggle();
                }
            }
        }

        String require = newClient.getEducationRequire();
        if(require != null){
            EditText input = (EditText) form.findViewWithTag(2);
            input.setText(require);
        }

        String individualGoal = newClient.getEducationIndividualGoal();
        if(individualGoal != null){
            EditText input = (EditText) form.findViewWithTag(3);
            input.setText(individualGoal);
        }
    }

    private void loadPageTenAnswers(){
        String rate = newClient.getSocialStatusRate();
        if(rate != null){
            RadioGroup rateGroup = (RadioGroup) form.findViewWithTag(1);
            for(int i = 0; i < rateGroup.getChildCount(); i++){
                RadioButton button = (RadioButton) rateGroup.getChildAt(i);
                if(button.getText().equals(rate)){
                    button.toggle();
                }
            }
        }

        String require = newClient.getSocialStatusRequire();
        if(require != null){
            EditText input = (EditText) form.findViewWithTag(2);
            input.setText(require);
        }

        String individualGoal = newClient.getSocialStatusIndividualGoal();
        if(individualGoal != null){
            EditText input = (EditText) form.findViewWithTag(3);
            input.setText(individualGoal);
        }
    }




    private void savePageAnswers(int currentPage){
        switch (currentPage) {
            case 1:
                savePageOne();
                break;
            case 2:
                savePageTwo();
                break;
            case 3:
                savePageThree();
                break;
            case 4:
                savePageFour();
                break;
            case 5:
                savePageFive();
                break;
            case 6:
                savePageSix();
                break;
            case 7:
                savePageSeven();
                break;
            case 8:
                savePageEight();
                break;
            case 9:
                savePageNine();
                break;
            case 10:
                savePageTen();
                break;
        }
    }

    private void savePageOne(){
        RadioGroup consent = (RadioGroup) form.findViewWithTag(1);
        int buttonId = consent.getCheckedRadioButtonId();
        RadioButton radioButton = consent.findViewById(buttonId);
        newClient.setConsentToInterview(radioButton.getText().equals("Yes"));

        TextView date = (TextView) form.findViewWithTag(2);
        newClient.setDate(date.getText().toString());
    }

    private void savePageTwo(){
        EditText firstName = (EditText) form.findViewWithTag(1);
        newClient.setFirstName(firstName.getText().toString());

        EditText lastName = (EditText) form.findViewWithTag(2);
        newClient.setLastName(lastName.getText().toString());
    }

    private void savePageThree(){
        EditText age = (EditText) form.findViewWithTag(1);
        String ageStr = age.getText().toString();
        newClient.setAge(Integer.parseInt(ageStr));

        RadioGroup gender = (RadioGroup) form.findViewWithTag(2);
        int buttonId = gender.getCheckedRadioButtonId();
        RadioButton radioButton = gender.findViewById(buttonId);
        if(radioButton.getText().equals("Male")){
            newClient.setGender("Male");
        }
        else{
            newClient.setGender("Female");
        }
    }

    private void savePageFour(){
        Spinner location = (Spinner) form.findViewWithTag(1);
        String selected = location.getItemAtPosition(location.getSelectedItemPosition()).toString();
        newClient.setLocation(selected);

        EditText villageNumber = (EditText) form.findViewWithTag(2);
        String villageNumberStr = villageNumber.getText().toString();
        newClient.setVillageNumber(Integer.parseInt(villageNumberStr));

        EditText contactNumber = (EditText) form.findViewWithTag(3);
        newClient.setContactPhoneNumber(contactNumber.getText().toString());
    }

    private void savePageFive(){
        RadioGroup consent = (RadioGroup) form.findViewWithTag(1);
        int buttonId = consent.getCheckedRadioButtonId();
        RadioButton radioButton = consent.findViewById(buttonId);
        newClient.setCaregiverPresent(radioButton.getText().equals("Yes"));

        EditText contactNumber = (EditText) form.findViewWithTag(2);
        newClient.setCaregiverPhoneNumber(contactNumber.getText().toString());
    }

    private void savePageSix(){
        //photo page
    }

    private void savePageSeven(){
        newClient.clearDisabilities();
        CheckBox checkBox;
        for(int i = 1; i <= 10; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                String selected = checkBox.getText().toString();
                newClient.addToDisabilities(selected);
            }
        }
    }

    private void savePageEight(){
        RadioGroup rate = (RadioGroup) form.findViewWithTag(1);
        RadioButton radioButton;
        for(int i = 0; i < rate.getChildCount(); i++){
            radioButton = (RadioButton) rate.getChildAt(i);
            if(radioButton.isChecked()){
                newClient.setHealthRate(radioButton.getText().toString());
            }
        }

        EditText require = (EditText) form.findViewWithTag(2);
        newClient.setHealthRequire(require.getText().toString());

        EditText individualGoal = (EditText) form.findViewWithTag(3);
        newClient.setHealthIndividualGoal(individualGoal.getText().toString());
    }

    private void savePageNine(){
        RadioGroup rate = (RadioGroup) form.findViewWithTag(1);
        RadioButton radioButton;
        for(int i = 0; i < rate.getChildCount(); i++){
            radioButton = (RadioButton) rate.getChildAt(i);
            if(radioButton.isChecked()){
                newClient.setEducationRate(radioButton.getText().toString());
            }
        }

        EditText require = (EditText) form.findViewWithTag(2);
        newClient.setEducationRequire(require.getText().toString());

        EditText individualGoal = (EditText) form.findViewWithTag(3);
        newClient.setEducationIndividualGoal(individualGoal.getText().toString());
    }

    private void savePageTen(){
        RadioGroup rate = (RadioGroup) form.findViewWithTag(1);
        RadioButton radioButton;
        for(int i = 0; i < rate.getChildCount(); i++){
            radioButton = (RadioButton) rate.getChildAt(i);
            if(radioButton.isChecked()){
                newClient.setSocialStatusRate(radioButton.getText().toString());
            }
        }

        EditText require = (EditText) form.findViewWithTag(2);
        newClient.setSocialStatusRequire(require.getText().toString());

        EditText individualGoal = (EditText) form.findViewWithTag(3);
        newClient.setSocialStatusIndividualGoal(individualGoal.getText().toString());
    }


    private void createNewClientForm(){
        Resources res = getResources();
        //page one: consent and date
        MultipleChoiceQuestion consent = new MultipleChoiceQuestion(1,getString(R.string.consent_newClientForm),QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer));
        TextQuestion date = new TextQuestion(2,getString(R.string.date_newClientForm), QuestionType.DATE);
        FormPage pageOne = new FormPage();
        pageOne.addToPage(consent);
        pageOne.addToPage(date);
        pages.add(pageOne);

        //page two: first and last name
        TextQuestion firstName = new TextQuestion(1,getString(R.string.firstName_newClientForm), QuestionType.PLAIN_TEXT);
        TextQuestion lastName = new TextQuestion(2,getString(R.string.lastName_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageTwo = new FormPage();
        pageTwo.addToPage(firstName);
        pageTwo.addToPage(lastName);
        pages.add(pageTwo);

        //page three: Age gender
        TextQuestion age = new TextQuestion(1,getString(R.string.age_newClientForm), QuestionType.NUMBER);
        MultipleChoiceQuestion gender = new MultipleChoiceQuestion(2,getString(R.string.gender_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.gender));
        FormPage pageThree = new FormPage();
        pageThree.addToPage(age);
        pageThree.addToPage(gender);
        pages.add(pageThree);


        //page four: Location Village No. Contact Number   GPS LATER!!!!
        MultipleChoiceQuestion location = new MultipleChoiceQuestion(1,getString(R.string.location_newClientForm), QuestionType.DROP_DOWN,res.getStringArray(R.array.locations));
        TextQuestion villageNum = new TextQuestion(2,getString(R.string.villageNumber_newClientForm), QuestionType.NUMBER);
        TextQuestion contactNum = new TextQuestion(3,getString(R.string.contactNumber_newClientForm), QuestionType.PHONE_NUMBER);
        FormPage pageFour = new FormPage();
        pageFour.addToPage(location);
        pageFour.addToPage(villageNum);
        pageFour.addToPage(contactNum);
        pages.add(pageFour);

        //page five: Caregiver
        MultipleChoiceQuestion caregiverPresent = new MultipleChoiceQuestion(1,getString(R.string.caregiverPresent_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer));
        TextQuestion caregiverContactNumber = new TextQuestion(2,getString(R.string.caregiverNumber_newClientForm), QuestionType.PHONE_NUMBER);
        FormPage pageFive = new FormPage();
        pageFive.addToPage(caregiverPresent);
        pageFive.addToPage(caregiverContactNumber);
        pages.add(pageFive);


        //page six: photo
        TextQuestion photo = new TextQuestion(1,getString(R.string.photo_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageSix = new FormPage();
        pageSix.addToPage(photo);
        pages.add(pageSix);




        //page seven: Type of disability
        MultipleChoiceQuestion disability = new MultipleChoiceQuestion(1,getString(R.string.disabilityType_newClientForm), QuestionType.CHECK_BOX, res.getStringArray(R.array.disability_types));
        FormPage pageSeven = new FormPage();
        pageSeven.addToPage(disability);
        pages.add(pageSeven);


        //page eight: clients health rate, require individual goal
        MultipleChoiceQuestion clientHealthRate = new MultipleChoiceQuestion(1,getString(R.string.healthRate_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type));
        TextQuestion healthRequire = new TextQuestion(2,getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT);
        TextQuestion healthIndividualGoal = new TextQuestion(3,getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageEight = new FormPage();
        pageEight.addToPage(clientHealthRate);
        pageEight.addToPage(healthRequire);
        pageEight.addToPage(healthIndividualGoal);
        pages.add(pageEight);


        //page nine: clients education rate
        MultipleChoiceQuestion clientEducationRate = new MultipleChoiceQuestion(1,getString(R.string.educationStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type));
        TextQuestion educationRequire = new TextQuestion(2,getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT);
        TextQuestion educationIndividualGoal = new TextQuestion(3,getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageNine = new FormPage();
        pageNine.addToPage(clientEducationRate);
        pageNine.addToPage(educationRequire);
        pageNine.addToPage(educationIndividualGoal);
        pages.add(pageNine);

        //page ten: social status
        MultipleChoiceQuestion clientSocialRate = new MultipleChoiceQuestion(1,getString(R.string.socialStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type));
        TextQuestion socialRequire = new TextQuestion(2,getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT);
        TextQuestion socialIndividualGoal = new TextQuestion(3,getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageTen = new FormPage();
        pageTen.addToPage(clientSocialRate);
        pageTen.addToPage(socialRequire);
        pageTen.addToPage(socialIndividualGoal);
        pages.add(pageTen);
    }
}