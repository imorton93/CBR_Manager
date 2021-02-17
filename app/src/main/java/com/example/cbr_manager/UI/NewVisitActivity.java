package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Forms.DisplayFormPage;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
import com.example.cbr_manager.Forms.NewVisit;
import com.example.cbr_manager.Forms.Question;
import com.example.cbr_manager.Forms.QuestionType;
import com.example.cbr_manager.Forms.TextQuestion;
import com.example.cbr_manager.R;

import java.util.ArrayList;

public class NewVisitActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, NewVisitActivity.class);
        return intent;
    }

    Button back;
    Button next;
    LinearLayout form;
    ProgressBar progressBar;
    TextView progressText;

    int currentPage;
    int pageCount;
    ArrayList<FormPage> pages;

    //structure to save all the answers
    NewVisit newVisit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        ToolbarButtons();

        next = (Button) findViewById(R.id.nextBtnVisit);
        next.setBackgroundColor(Color.BLUE);
        back = (Button) findViewById(R.id.backBtnVisit);
        newVisit = new NewVisit();

        form = (LinearLayout) findViewById(R.id.formVisit);
        progressBar = (ProgressBar) findViewById(R.id.formProgressVisit);
        progressText = (TextView) findViewById(R.id.formProgressTextVisit);

        currentPage = 1;
        pages = new ArrayList<>();

        createNewVisitForm();
        pageCount = pages.size() + 1;

        DisplayFormPage.displayPage(pages.get(currentPage - 1), form, this);
        progressBar.setMax(pageCount);

        setProgress(currentPage, pageCount);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requiredFieldsFilled(pages.get(currentPage - 1))){
                    requiredFieldsToast();
                }
                else if(currentPage < pageCount - 1){
                    if(currentPage == 1){
                        back.setClickable(true);
                        back.setBackgroundColor(Color.BLUE);
                    }
                    //save answers
                    //savePage(pages.get(currentPage - 1));

                    currentPage++;
                    setProgress(currentPage, pageCount);

                    clearForm();

                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewVisitActivity.this);

                    //load previously saved answers if any
                    //loadAnswers(pages.get(currentPage - 1));
                }
                else if(currentPage >= pageCount -1){
                    //save answers
                    //savePage(pages.get(currentPage - 1));

                    currentPage++;
                    setProgress(currentPage,pageCount);

                    clearForm();
                    reviewPage();

                    if(currentPage == pageCount){
                        next.setText(getString(R.string.finish));
                    }
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

                DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewVisitActivity.this);

                //load previously saved answers if any
//                loadAnswers(pages.get(currentPage - 1));
                if(currentPage == 1){
                    back.setClickable(false);
                    back.setBackgroundColor(Color.DKGRAY);
                }
            }
        });
        back.setClickable(false);
        back.setBackgroundColor(Color.DKGRAY);
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(NewVisitActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(NewVisitActivity.this);
                startActivity(intent);
            }
        });
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

    private void requiredFieldsToast(){
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(this, "Fill all Required Fields", duration).show();
    }

    private Boolean requiredFieldsFilled(FormPage page){
        return true;
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
            else if(type == QuestionType.CHECK_BOX_WITH_COMMENT){
                saveCheckBoxWithComment(question);
            }
        }
    }

    private void saveText(Question question){
        String tag = question.getQuestionTag();
        EditText input = (EditText) form.findViewWithTag(tag);
        if(tag.equals(getString(R.string.socialIfConcluded))){
            newVisit.setSocialIfConcluded(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.educationIfConcluded))){
            newVisit.setEducationIfConcluded(input.getText().toString());
        }
        else if(tag.equals(getString(R.string.healthIfConcluded))){
            newVisit.setHealthIfConcluded(input.getText().toString());
        }
    }

    private void saveDate(Question question){
        String tag = question.getQuestionTag();
        TextView date = (TextView) form.findViewWithTag(question.getQuestionTag());
        if(tag.equals(getString(R.string.date))){
            newVisit.setDate(date.getText().toString());
        }
    }

    private void saveRadio(Question question){
        String tag = question.getQuestionTag();
        RadioGroup radioGroup = (RadioGroup) form.findViewWithTag(tag);
        RadioButton radioButton;
        for(int i = 0; i < radioGroup.getChildCount(); i++){
            radioButton = (RadioButton) radioGroup.getChildAt(i);
            if(radioButton.isChecked()){
                if(tag.equals(getString(R.string.purposeOfVisit))){
                    newVisit.setPurposeOfVisit(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.socialGoalMet))){
                    newVisit.setSocialGoalMet(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.educationGoalMet))){
                    newVisit.setEducationGoalMet(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.healthGoalMet))){
                    newVisit.setHealthGoalMet(radioButton.getText().toString());
                }
            }
        }
    }

    private void saveNumber(Question question){
        String tag = question.getQuestionTag();
        EditText input = (EditText) form.findViewWithTag(tag);
        String inputStr = input.getText().toString();
        int num = Integer.parseInt(inputStr);
        if(tag.equals(getString(R.string.villageNumber))){
            newVisit.setVillageNumber(num);
        }
    }

    private void saveDropDown(Question question){
        String tag =  question.getQuestionTag();
        Spinner spinner = (Spinner) form.findViewWithTag(tag);
        String selected = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        if(tag.equals(getString(R.string.location))){
            newVisit.setLocation(selected);
        }
    }

    private void saveCheckBox(Question question){
        String tag = question.getQuestionTag();
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        int size = mcq.getAnswers().length;

        CheckBox checkBox;
        for(int i = 0; i < size; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                String selected = checkBox.getText().toString();
                if(tag.equals(getString(R.string.ifCBR))){
                    newVisit.addToIfCbr(selected);
                }
            }
        }
    }

    private void saveCheckBoxWithComment(Question question){
        String tag = question.getQuestionTag();
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        int size = mcq.getAnswers().length;

        EditText input;
        CheckBox checkBox;
        for(int i = 0; i < size; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                String selected = checkBox.getText().toString();
                input = (EditText) form.findViewWithTag(i * 100);
                String explanation = input.getText().toString();

                if(tag.equals(getString(R.string.healthProvided))){
                    newVisit.addHealthProvided(selected, explanation);
                }
                else if(tag.equals(getString(R.string.socialProvided))){
                    newVisit.addSocialProvided(selected, explanation);
                }
                else if(tag.equals(getString(R.string.educationProvided))){
                    newVisit.addeducationProvided(selected, explanation);
                }
            }
        }
    }





    private void createNewVisitForm(){
        Resources res = getResources();
        //Page One: purpose of visit, if CBR, date
        MultipleChoiceQuestion purposeOfVisit = new MultipleChoiceQuestion(getString(R.string.purposeOfVisit),getString(R.string.purposeOfVisit_newVisitForm), QuestionType.RADIO, res.getStringArray(R.array.purposeVisitChoices), true);
        MultipleChoiceQuestion ifCBR = new MultipleChoiceQuestion(getString(R.string.ifCBR), getString(R.string.ifCBR_newVisitForm), QuestionType.CHECK_BOX, res.getStringArray(R.array.ifCBRChoices), false);
        TextQuestion date = new TextQuestion(getString(R.string.date), getString(R.string.dateOfVisit_newVisitForm), QuestionType.DATE, true);
        FormPage pageOne = new FormPage();
        pageOne.addToPage(purposeOfVisit);
        pageOne.addToPage(ifCBR);
        pages.add(pageOne);

        //Page Two: Location, village number
        MultipleChoiceQuestion location = new MultipleChoiceQuestion(getString(R.string.location), getString(R.string.location_newVisitForm), QuestionType.DROP_DOWN, res.getStringArray(R.array.locations),true);
        TextQuestion villageNumber = new TextQuestion(getString(R.string.villageNumber), getString(R.string.villageNumber_newVisitForm), QuestionType.NUMBER, true);
        FormPage pageTwo = new FormPage();
        pageTwo.addToPage(location);
        pageTwo.addToPage(villageNumber);
        pages.add(pageTwo);

        //Page Three: health provided, goal met, if conlcuded outcome
        MultipleChoiceQuestion healthProvided = new MultipleChoiceQuestion(getString(R.string.healthProvided), getString(R.string.healthProvided_newVisitForm), QuestionType.CHECK_BOX_WITH_COMMENT, res.getStringArray(R.array.healthProvidedChoices),true);
        FormPage pageThree = new FormPage();
        pageThree.addToPage(healthProvided);
        pages.add(pageThree);


        //Page Four: goal met health, if concluded outcome
        MultipleChoiceQuestion goalMet = new MultipleChoiceQuestion(getString(R.string.healthGoalMet), getString(R.string.goalMet_newVisitForm), QuestionType.RADIO, res.getStringArray(R.array.goalMetChoices), true);
        TextQuestion ifConcluded = new TextQuestion(getString(R.string.healthIfConcluded), getString(R.string.concluded_newVisitForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageFour = new FormPage();
        pageFour.addToPage(goalMet);
        pageFour.addToPage(ifConcluded);
        pages.add(pageFour);


        //Page Five:Education provided
        MultipleChoiceQuestion educationProvided = new MultipleChoiceQuestion(getString(R.string.educationProvided), getString(R.string.educationProvided_newVisitForm), QuestionType.CHECK_BOX_WITH_COMMENT, res.getStringArray(R.array.educationSocialProvidedChoices),true);
        FormPage pageFive = new FormPage();
        pageFive.addToPage(educationProvided);
        pages.add(pageFive);


        //Page Six: social provided
        MultipleChoiceQuestion goalMet1 = new MultipleChoiceQuestion(getString(R.string.educationGoalMet), getString(R.string.goalMet_newVisitForm), QuestionType.RADIO, res.getStringArray(R.array.goalMetChoices), true);
        TextQuestion ifConcluded1 = new TextQuestion(getString(R.string.educationIfConcluded), getString(R.string.concluded_newVisitForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageSix = new FormPage();
        pageSix.addToPage(goalMet1);
        pageSix.addToPage(ifConcluded1);
        pages.add(pageSix);

        //Page Seven:
        MultipleChoiceQuestion socialProvided = new MultipleChoiceQuestion(getString(R.string.socialProvided), getString(R.string.socialProvided_newVisitForm), QuestionType.CHECK_BOX_WITH_COMMENT, res.getStringArray(R.array.educationSocialProvidedChoices),true);
        FormPage pageSeven = new FormPage();
        pageSeven.addToPage(socialProvided);
        pages.add(pageSeven);

        //Page Eight
        MultipleChoiceQuestion goalMet2 = new MultipleChoiceQuestion(getString(R.string.socialGoalMet), getString(R.string.goalMet_newVisitForm), QuestionType.RADIO, res.getStringArray(R.array.goalMetChoices), true);
        TextQuestion ifConcluded2 = new TextQuestion(getString(R.string.socialIfConcluded), getString(R.string.concluded_newVisitForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageEight = new FormPage();
        pageEight.addToPage(goalMet2);
        pageEight.addToPage(ifConcluded2);
        pages.add(pageEight);

    }

    private void reviewPage(){

    }
}