package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Resources;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);


        form = (LinearLayout) findViewById(R.id.form);
        currentPage = 1;
        pages = new ArrayList<>();

        createNewClientForm();
        pageCount = pages.size();
        displayPage(pages.get(currentPage - 1));

        Button next = (Button) findViewById(R.id.nextBtn);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(currentPage < pageCount){
                    currentPage++;
                    clearForm();
                    displayPage(pages.get(currentPage - 1));
                }
                else if(currentPage >= pageCount){
                    finishForm();
                }
            }
        });
    }

    private void finishForm(){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, "End", duration);
        toast.show();
    }

    private void displayPage(FormPage page){
        ArrayList<Question> questions = page.getQuestions();
        for(Question question : questions){
            TextQuestion txtQ;
            MultipleChoiceQuestion mcQ;
            if(question.getQuestionType().equals(QuestionType.PLAIN_TEXT)){
                txtQ = (TextQuestion) question;
                displayPlainTextQuestion(txtQ);
            }
            else if(question.getQuestionType().equals(QuestionType.PHONE_NUMBER)){
                txtQ = (TextQuestion) question;
                displayPhoneNumberQuestion(txtQ);
            }
            else if(question.getQuestionType().equals(QuestionType.NUMBER)){
                txtQ = (TextQuestion) question;
                displayNumberQuestion(txtQ);
            }
            else if(question.getQuestionType().equals(QuestionType.DATE)){
                txtQ = (TextQuestion) question;
                displayDateQuestion(txtQ);
            }
            else if(question.getQuestionType().equals(QuestionType.CHECK_BOX)){
                mcQ = (MultipleChoiceQuestion) question;
                displayCheckBoxQuestion(mcQ);
            }
            else if(question.getQuestionType().equals(QuestionType.DROP_DOWN)){
                mcQ = (MultipleChoiceQuestion) question;
                displayDropDownQuestion(mcQ);
            }
            else if(question.getQuestionType().equals(QuestionType.RADIO)){
                mcQ = (MultipleChoiceQuestion) question;
                displayRadioQuestion(mcQ);
            }
        }
    }


    private void displayQuestionHeading(String questionString){
        TextView questionText = new TextView(this);
        questionText.setText(questionString);
        form.addView(questionText);
    }

    private void displayPlainTextQuestion(TextQuestion question){
        displayQuestionHeading(question.getQuestionString());

        EditText inputText = new EditText(this);
        inputText.setText("");
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        form.addView(inputText);
    }

    private void displayPhoneNumberQuestion(TextQuestion question){
        displayQuestionHeading(question.getQuestionString());


        EditText inputText = new EditText(this);
        inputText.setText("");
        inputText.setInputType(InputType.TYPE_CLASS_PHONE);
        form.addView(inputText);
    }

    private void displayNumberQuestion(TextQuestion question){
        displayQuestionHeading(question.getQuestionString());

        EditText inputText = new EditText(this);
        inputText.setText("");
        inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        form.addView(inputText);
    }

    private void displayDateQuestion(TextQuestion question){
        displayQuestionHeading(question.getQuestionString());

        TextView selectDate = new TextView(this);
        selectDate.setText("Select Date");

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                selectDate.setText(date);
            }
        };

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NewClientActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener, year, month, day);
                dialog.show();
            }
        });
        form.addView(selectDate);
    }

    private void displayRadioQuestion(MultipleChoiceQuestion question){
        displayQuestionHeading(question.getQuestionString());

        String[] answers = question.getAnswers();
        RadioGroup radioAnswers = new RadioGroup(this);
        RadioButton rButton;
        for (String answer : answers) {
            rButton = new RadioButton(this);
            rButton.setText(answer);
            radioAnswers.addView(rButton);
        }

        form.addView(radioAnswers);
    }

    private void displayDropDownQuestion(MultipleChoiceQuestion question){
        displayQuestionHeading(question.getQuestionString());

        Spinner spinner = new Spinner(this);
        String[] answers = question.getAnswers();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, answers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        form.addView(spinner);
    }

    private void displayCheckBoxQuestion(MultipleChoiceQuestion question){
        displayQuestionHeading(question.getQuestionString());

        CheckBox checkBox;
        String[] answers = question.getAnswers();
        for (String answer : answers) {
            checkBox = new CheckBox(this);
            checkBox.setText(answer);
            form.addView(checkBox);
        }
    }


    private void clearForm(){
        form.removeAllViews();
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