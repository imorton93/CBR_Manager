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
        //REFACTORING
        DisplayFormPage.displayPage(pages.get(currentPage-1), form, this);
//        displayPage(pages.get(currentPage - 1));
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
                    currentPage++;
                    setProgress(currentPage, pageCount);
                    clearForm();
                    //REFACTORING
                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this);
//                    displayPage(pages.get(currentPage - 1));
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
                    //REFACTORING
                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this);
//                    displayPage(pages.get(currentPage - 1));
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


    
    private void createNewClientForm(){
        Resources res = getResources();
        //page one: consent and date
        MultipleChoiceQuestion consent = new MultipleChoiceQuestion(getString(R.string.consent_newClientForm),QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer));
        TextQuestion date = new TextQuestion(getString(R.string.date_newClientForm), QuestionType.DATE);
        FormPage pageOne = new FormPage();
        pageOne.addToPage(consent);
        pageOne.addToPage(date);
        pages.add(pageOne);

        //page two: first and last name
        TextQuestion firstName = new TextQuestion(getString(R.string.firstName_newClientForm), QuestionType.PLAIN_TEXT);
        TextQuestion lastName = new TextQuestion(getString(R.string.lastName_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageTwo = new FormPage();
        pageTwo.addToPage(firstName);
        pageTwo.addToPage(lastName);
        pages.add(pageTwo);

        //page three: Age gender
        TextQuestion age = new TextQuestion(getString(R.string.age_newClientForm), QuestionType.NUMBER);
        MultipleChoiceQuestion gender = new MultipleChoiceQuestion(getString(R.string.gender_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.gender));
        FormPage pageThree = new FormPage();
        pageThree.addToPage(age);
        pageThree.addToPage(gender);
        pages.add(pageThree);


        //page four: Location Village No. Contact Number   GPS LATER!!!!
        MultipleChoiceQuestion location = new MultipleChoiceQuestion(getString(R.string.location_newClientForm), QuestionType.DROP_DOWN,res.getStringArray(R.array.locations));
        TextQuestion villageNum = new TextQuestion(getString(R.string.villageNumber_newClientForm), QuestionType.NUMBER);
        TextQuestion contactNum = new TextQuestion(getString(R.string.contactNumber_newClientForm), QuestionType.PHONE_NUMBER);
        FormPage pageFour = new FormPage();
        pageFour.addToPage(location);
        pageFour.addToPage(villageNum);
        pageFour.addToPage(contactNum);
        pages.add(pageFour);

        //page five: photo
        TextQuestion photo = new TextQuestion(getString(R.string.photo_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageFive = new FormPage();
        pageFive.addToPage(photo);
        pages.add(pageFive);


        //page six: Caregiver
        MultipleChoiceQuestion caregiverPresent = new MultipleChoiceQuestion(getString(R.string.caregiverPresent_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer));
        TextQuestion caregiverContactNumber = new TextQuestion(getString(R.string.caregiverNumber_newClientForm), QuestionType.PHONE_NUMBER);
        FormPage pageSix = new FormPage();
        pageSix.addToPage(caregiverPresent);
        pageSix.addToPage(caregiverContactNumber);
        pages.add(pageSix);

        //page seven: Type of disability
        MultipleChoiceQuestion disability = new MultipleChoiceQuestion(getString(R.string.disabilityType_newClientForm), QuestionType.CHECK_BOX, res.getStringArray(R.array.disability_types));
        FormPage pageSeven = new FormPage();
        pageSeven.addToPage(disability);
        pages.add(pageSeven);


        //page eight: clients health rate, require individual goal
        MultipleChoiceQuestion clientHealthRate = new MultipleChoiceQuestion(getString(R.string.healthRate_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type));
        TextQuestion healthRequire = new TextQuestion(getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT);
        TextQuestion healthIndividualGoal = new TextQuestion(getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageEight = new FormPage();
        pageEight.addToPage(clientHealthRate);
        pageEight.addToPage(healthRequire);
        pageEight.addToPage(healthIndividualGoal);
        pages.add(pageEight);


        //page nine: clients education rate
        MultipleChoiceQuestion clientEducationRate = new MultipleChoiceQuestion(getString(R.string.educationStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type));
        TextQuestion educationRequire = new TextQuestion(getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT);
        TextQuestion educationIndividualGoal = new TextQuestion(getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageNine = new FormPage();
        pageNine.addToPage(clientEducationRate);
        pageNine.addToPage(educationRequire);
        pageNine.addToPage(educationIndividualGoal);
        pages.add(pageNine);

        //page ten: social status
        MultipleChoiceQuestion clientSocialRate = new MultipleChoiceQuestion(getString(R.string.socialStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type));
        TextQuestion socialRequire = new TextQuestion(getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT);
        TextQuestion socialIndividualGoal = new TextQuestion(getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT);
        FormPage pageTen = new FormPage();
        pageTen.addToPage(clientSocialRate);
        pageTen.addToPage(socialRequire);
        pageTen.addToPage(socialIndividualGoal);
        pages.add(pageTen);
    }
}