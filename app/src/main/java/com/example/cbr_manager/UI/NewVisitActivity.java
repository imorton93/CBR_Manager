package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.Forms.DisplayFormPage;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
import com.example.cbr_manager.Forms.Question;
import com.example.cbr_manager.Forms.QuestionType;
import com.example.cbr_manager.Forms.TextQuestion;
import com.example.cbr_manager.R;

import java.util.ArrayList;

public class NewVisitActivity extends AppCompatActivity {

    private VisitManager visitManager = VisitManager.getInstance(NewVisitActivity.this);
    private static long client_id;
    private static int client_pos;
    public static final String R_CLIENT_ID_PASSED_IN = "r_client_id_passed_in";
    public static final String R_CLIENT_POS_PASSED_IN = "r_client_pos_passed_in";

    public static Intent makeIntent(Context context,int position,long id) {
        Intent intent =  new Intent(context, NewVisitActivity.class);
        intent.putExtra(R_CLIENT_ID_PASSED_IN, id);
        intent.putExtra(R_CLIENT_POS_PASSED_IN, position);
        return intent;
    }

    private void extractIntent(){
        Intent intent = getIntent();
        client_id = intent.getLongExtra(R_CLIENT_ID_PASSED_IN, 0);
        client_pos = intent.getIntExtra(R_CLIENT_POS_PASSED_IN, 0);
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
    Visit newVisit;
    private DatabaseHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);

        mydb = new DatabaseHelper(NewVisitActivity.this);

        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(NewVisitActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);

        extractIntent();

        next = (Button) findViewById(R.id.nextBtnVisit);
        back = (Button) findViewById(R.id.backBtnVisit);
        newVisit = new Visit();
        newVisit.setClientID(client_id);

        form = (LinearLayout) findViewById(R.id.formVisit);
        progressBar = (ProgressBar) findViewById(R.id.formProgressVisit);
        progressText = (TextView) findViewById(R.id.formProgressTextVisit);

        currentPage = 1;
        pages = new ArrayList<>();

        createNewVisitForm();
        pageCount = pages.size() + 1;

        DisplayFormPage.displayPage(pages.get(currentPage - 1), form, this, 0, 0);
        progressBar.setMax(pageCount);

        setProgress(currentPage, pageCount);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage == 10) {
                    insertVisit();
                }

                else if(!requiredFieldsFilled(pages.get(currentPage - 1))){
                    requiredFieldsToast();
                }
                else if(currentPage < pageCount - 1){
                    if(currentPage == 1){
                        back.setClickable(true);
                        back.setVisibility(View.VISIBLE);
                        back.setBackground(ContextCompat.getDrawable(NewVisitActivity.this, R.drawable.rounded_form_buttons));
                    }
                    //save answers
                    savePage(pages.get(currentPage - 1));

                    currentPage++;
                    setProgress(currentPage, pageCount);

                    clearForm();

                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewVisitActivity.this, 0, 0);

                    //load previously saved answers if any
                    loadAnswers(pages.get(currentPage - 1));
                }
                else if(currentPage == pageCount -1){
                    //save answers
                    savePage(pages.get(currentPage - 1));

                    currentPage++;
                    setProgress(currentPage,pageCount);

                    clearForm();
                    reviewPage();

                    if(currentPage == pageCount){
                        next.setText(getString(R.string.finish));
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

                DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewVisitActivity.this, 0, 0);

                //load previously saved answers if any
                loadAnswers(pages.get(currentPage - 1));
                if(currentPage == 1){
                    back.setClickable(false);
                    back.setVisibility(View.INVISIBLE);
                    back.setBackgroundColor(Color.DKGRAY);
                }
            }
        });
        back.setClickable(false);
        back.setVisibility(View.INVISIBLE);
        back.setBackgroundColor(Color.DKGRAY);

        //make the progress bar blue
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(Color.parseColor("#009fb8"), android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);
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

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(NewVisitActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(NewVisitActivity.this);
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
                    if(!isRadioAnswered(question)){
                        returnBool = false;
                    }
                }
                else if(type == QuestionType.CHECK_BOX){
                    if(!isCheckBoxAnswered(question)){
                        returnBool = false;
                    }
                }
                else if(type == QuestionType.CHECK_BOX_WITH_COMMENT){
                    if(!isCheckBoxCommentAnswered(question)){
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
        boolean radioSelected = false;
        boolean otherInput = true;

        if(radioGroup.getCheckedRadioButtonId() != -1){
            radioSelected = true;
        }

        if(radioSelected){
            //check if purpose question if CBR was answered
            if(tag.equals(getString(R.string.purposeOfVisit))){
                int id  = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(id);
                if(radioButton.getText().equals("CBR")){
                    otherInput = false;
                    CheckBox checkBox;
                    for(int i = 0; i < 3; i++){
                        checkBox = (CheckBox) form.findViewWithTag(i);
                        if(checkBox.isChecked()){
                            otherInput = true;
                        }
                    }
                }
            }
            else if(tag.equals(getString(R.string.healthGoalMet))){
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(id);
                if(radioButton.getText().equals("Concluded")){

                    EditText input = (EditText) form.findViewWithTag(getString(R.string.healthIfConcluded));
                    otherInput = input.getText().toString().trim().length() > 0;
                }
            }
            else if(tag.equals(getString(R.string.socialGoalMet))){
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(id);
                if(radioButton.getText().equals("Concluded")){

                    EditText input = (EditText) form.findViewWithTag(getString(R.string.socialIfConcluded));
                    otherInput = input.getText().toString().trim().length() > 0;
                }
            }
            else if(tag.equals(getString(R.string.educationGoalMet))){
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(id);
                if(radioButton.getText().equals("Concluded")){

                    EditText input = (EditText) form.findViewWithTag(getString(R.string.educationIfConcluded));
                    otherInput = input.getText().toString().trim().length() > 0;
                }
            }
        }

        return radioSelected && otherInput;
    }

    private Boolean isCheckBoxAnswered(Question question){
        boolean returnBool = false;
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        int size = mcq.getAnswers().length;
        CheckBox checkBox;
        for(int i = 0; i < size; i++){
             checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                returnBool = true;
            }
        }
        return returnBool;
    }

    private Boolean isCheckBoxCommentAnswered(Question question){
        if(!isCheckBoxAnswered(question)){
            return false;
        }

        //check make sure all descriptions are filled in that are checked
        boolean returnBool = true;
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        int size = mcq.getAnswers().length;
        CheckBox checkBox;
        for(int i = 0; i < size; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                EditText input = (EditText) form.findViewWithTag(checkBox.getText().toString());
                if(input.getText().toString().trim().length() == 0){
                    returnBool = false;
                }
            }
        }
        return returnBool;
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

        if(tag.equals(getString(R.string.ifCBR))){
            newVisit.clearIfCbr();
        }

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

        if(tag.equals(getString(R.string.healthProvided))){
            newVisit.clearHealthProvided();
        }
        else if(tag.equals(getString(R.string.socialProvided))){
            newVisit.clearSocialProvided();
        }
        else if(tag.equals(getString(R.string.educationProvided))){
            newVisit.clearEducationProvided();
        }


        EditText input;
        CheckBox checkBox;
        for(int i = 0; i < size; i++){
            checkBox = (CheckBox) form.findViewWithTag(i);
            if(checkBox.isChecked()){
                String selected = checkBox.getText().toString();
                input = (EditText) form.findViewWithTag(selected);
                String explanation = input.getText().toString();

                if(tag.equals(getString(R.string.healthProvided))){
                    newVisit.addHealthProvided(selected, explanation);
                }
                else if(tag.equals(getString(R.string.socialProvided))){
                    newVisit.addSocialProvided(selected, explanation);
                }
                else if(tag.equals(getString(R.string.educationProvided))){
                    newVisit.addEducationProvided(selected, explanation);
                }
            }
        }
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
            else if(type == QuestionType.CHECK_BOX_WITH_COMMENT){
                loadCheckBoxWithComment(question);
            }
        }
    }

    private void loadText(Question question){
        String tag = question.getQuestionTag();
        String data = null;

        if(tag.equals(getString(R.string.socialIfConcluded))){
            data = newVisit.getSocialIfConcluded();
        }
        else if(tag.equals(getString(R.string.healthIfConcluded))){
            data = newVisit.getHealthIfConcluded();
        }
        else if(tag.equals(getString(R.string.educationIfConcluded))){
            data = newVisit.getEducationIfConcluded();
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
            data = newVisit.getDate();
        }

        if(data != null){
            TextView date = (TextView) form.findViewWithTag(tag);
            date.setText(data);
        }
    }

    private void loadRadio(Question question){
        String tag = question.getQuestionTag();
        String data = null;
        if(tag.equals(getString(R.string.purposeOfVisit))){
            data = newVisit.getPurposeOfVisit();
        }
        else if(tag.equals(getString(R.string.socialGoalMet))){
            data = newVisit.getSocialGoalMet();
        }
        else if(tag.equals(getString(R.string.educationGoalMet))){
            data = newVisit.getEducationGoalMet();
        }
        else if(tag.equals(getString(R.string.healthGoalMet))){
            data = newVisit.getHealthGoalMet();
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
        if(tag.equals(getString(R.string.villageNumber))){
            data = newVisit.getVillageNumber();
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
            data = newVisit.getLocation();
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
        String tag = question.getQuestionTag();
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        int size = mcq.getAnswers().length;
        ArrayList<String> arrayList = null;
        if(tag.equals(getString(R.string.ifCBR))){
            arrayList = newVisit.getIfCbr();
        }
        for(int i = 0; i < arrayList.size(); i++){
            CheckBox checkBox;
            for(int j = 0; j < size; j++){
                checkBox = (CheckBox) form.findViewWithTag(j);
                if(checkBox.getText().equals(arrayList.get(i))){
                    checkBox.toggle();
                }
            }
        }

    }

    private void loadCheckBoxWithComment(Question question){
        String tag = question.getQuestionTag();
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
        int answersSize = mcq.getAnswers().length;
        ArrayList<Visit.Provided> arrayList = null;
        if(tag.equals(getString(R.string.healthProvided))){
            arrayList = newVisit.getHealthProvided();
        }
        else if(tag.equals(getString(R.string.socialProvided))){
            arrayList = newVisit.getSocialProvided();
        }
        else if(tag.equals(getString(R.string.educationProvided))){
            arrayList = newVisit.getEducationProvided();
        }

        for(int i = 0; i < arrayList.size(); i++){
            CheckBox checkBox;
            for(int j = 0; j < answersSize; j++){
                checkBox = (CheckBox) form.findViewWithTag(j);

                if(checkBox.getText().equals(arrayList.get(i).getCheckBox())){
                    checkBox.setChecked(true);

                    EditText input = (EditText) form.findViewWithTag(checkBox.getText().toString());
                    input.setText(arrayList.get(i).getExplanation());
                }
            }
        }
    }


    private void createNewVisitForm(){
        setUniqueVisitId();

        Resources res = getResources();
        FormPage pageZero = new FormPage();
        String previousOutcome = visitManager.getPreviousVisitOutcome(this.client_id);
        TextQuestion outcome = new TextQuestion("PreviousOutcome", previousOutcome, QuestionType.NONE, false);
        pageZero.addToPage(outcome);
        pages.add(pageZero);

        //Page One: purpose of visit, if CBR, date
        MultipleChoiceQuestion purposeOfVisit = new MultipleChoiceQuestion(getString(R.string.purposeOfVisit),getString(R.string.purposeOfVisit_newVisitForm), QuestionType.RADIO, res.getStringArray(R.array.purposeVisitChoices), true);
        MultipleChoiceQuestion ifCBR = new MultipleChoiceQuestion(getString(R.string.ifCBR), getString(R.string.ifCBR_newVisitForm), QuestionType.CHECK_BOX, res.getStringArray(R.array.ifCBRChoices), false);
        TextQuestion date = new TextQuestion(getString(R.string.date), getString(R.string.dateOfVisit_newVisitForm), QuestionType.DATE, true);
        FormPage pageOne = new FormPage();
        pageOne.addToPage(purposeOfVisit);
        pageOne.addToPage(ifCBR);
        pageOne.addToPage(date);
        pages.add(pageOne);

        //Page Two: Location, village number
        MultipleChoiceQuestion location = new MultipleChoiceQuestion(getString(R.string.location), getString(R.string.location_newVisitForm), QuestionType.DROP_DOWN, res.getStringArray(R.array.locations),true);
        TextQuestion villageNumber = new TextQuestion(getString(R.string.villageNumber), getString(R.string.villageNumber_newVisitForm), QuestionType.NUMBER, true);
        FormPage pageTwo = new FormPage();
        pageTwo.addToPage(location);
        pageTwo.addToPage(villageNumber);
        pages.add(pageTwo);

        //Page Three: health provided, goal met, if concluded outcome
        MultipleChoiceQuestion healthProvided = new MultipleChoiceQuestion(getString(R.string.healthProvided), getString(R.string.healthProvided_newVisitForm), QuestionType.CHECK_BOX_WITH_COMMENT, res.getStringArray(R.array.healthProvidedChoices),true);
        FormPage pageThree = new FormPage();
        pageThree.addToPage(healthProvided);
        pages.add(pageThree);


        //Page Four: goal met health, if concluded outcome
        MultipleChoiceQuestion goalMet = new MultipleChoiceQuestion(getString(R.string.healthGoalMet), getString(R.string.goalMet_newVisitForm), QuestionType.RADIO, res.getStringArray(R.array.goalMetChoices), true);
        TextQuestion ifConcluded = new TextQuestion(getString(R.string.healthIfConcluded), getString(R.string.concluded_newVisitForm), QuestionType.PLAIN_TEXT, false);
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
        TextQuestion ifConcluded1 = new TextQuestion(getString(R.string.educationIfConcluded), getString(R.string.concluded_newVisitForm), QuestionType.PLAIN_TEXT, false);
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
        TextQuestion ifConcluded2 = new TextQuestion(getString(R.string.socialIfConcluded), getString(R.string.concluded_newVisitForm), QuestionType.PLAIN_TEXT, false);
        FormPage pageEight = new FormPage();
        pageEight.addToPage(goalMet2);
        pageEight.addToPage(ifConcluded2);
        pages.add(pageEight);

    }

    private void reviewPage(){
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        float txtSize = 18;
        reviewTitle(txtSize);

        TextView purposeView = new TextView(this);
        String purposeStr = newVisit.getPurposeOfVisit();
        purposeView.setText("Purpose of Visit: " + purposeStr);
        purposeView.setTextSize(txtSize);
        layout.addView(purposeView);

        if(purposeStr.equals("CBR")){
            ArrayList<String> arrayList = newVisit.getIfCbr();
            String ifcbr = "CBR: ";
            for(int i = 0; i < arrayList.size(); i++){
                String data = arrayList.get(i);
                ifcbr = ifcbr.concat(data);
            }
            TextView cbrView = new TextView(this);
            cbrView.setText(ifcbr);
            cbrView.setTextSize(txtSize);
            layout.addView(cbrView);
        }

        TextView dateView = new TextView(this);
        String dateStr = newVisit.getDate();
        dateView.setText("Date of Visit:" + dateStr);
        dateView.setTextSize(txtSize);
        layout.addView(dateView);

        insertLineDivide(layout);

        TextView locationView = new TextView(this);
        String locationStr = newVisit.getLocation();
        locationView.setText("Location: " + locationStr);
        locationView.setTextSize(txtSize);
        layout.addView(locationView);

        TextView villageNumberView = new TextView(this);
        int villageNumber = newVisit.getVillageNumber();
        villageNumberView.setText("Village Number: " + Integer.toString(villageNumber));
        villageNumberView.setTextSize(txtSize);
        layout.addView(villageNumberView);

        insertLineDivide(layout);

        TextView healthProvidedView = new TextView(this);
        healthProvidedView.setText("For Health, what was provided?");
        healthProvidedView.setTextSize(txtSize);
        layout.addView(healthProvidedView);
        ArrayList<Visit.Provided> arrayList = newVisit.getHealthProvided();
        for(int i = 0; i < arrayList.size(); i++){
            String checkbox = arrayList.get(i).getCheckBox();
            String explanation = arrayList.get(i).getExplanation();
            TextView checkboxView = new TextView(this);
            checkboxView.setText("\t" + checkbox + ": " + explanation);
            checkboxView.setTextSize(txtSize);
            layout.addView(checkboxView);
        }

        insertLineDivide(layout);

        TextView healthGoalMetView = new TextView(this);
        String healthGoalMet = newVisit.getHealthGoalMet();
        healthGoalMetView.setText("Goal Met? " + healthGoalMet);
        healthGoalMetView.setTextSize(txtSize);
        layout.addView(healthGoalMetView);

        if(healthGoalMet.equals("Concluded")){
            TextView healthIfConcluded = new TextView(this);
            String ifConcluded = newVisit.getHealthIfConcluded();
            healthIfConcluded.setText("The Outcome: " + ifConcluded);
            healthIfConcluded.setTextSize(txtSize);
            layout.addView(healthIfConcluded);
        }

        insertLineDivide(layout);

        TextView socialProvidedView = new TextView(this);
        socialProvidedView.setText("For Social, what was provided?");
        socialProvidedView.setTextSize(txtSize);
        layout.addView(socialProvidedView);
        ArrayList<Visit.Provided> arrayList1 = newVisit.getSocialProvided();
        for(int i = 0; i < arrayList1.size(); i++){
            String checkbox = arrayList1.get(i).getCheckBox();
            String explanation = arrayList1.get(i).getExplanation();
            TextView checkboxView = new TextView(this);
            checkboxView.setText("\t" + checkbox + ": " + explanation);
            checkboxView.setTextSize(txtSize);
            layout.addView(checkboxView);
        }

        insertLineDivide(layout);

        TextView socialGoalMetView = new TextView(this);
        String socialGoalMet = newVisit.getSocialGoalMet();
        socialGoalMetView.setText("Goal Met? " + socialGoalMet);
        socialGoalMetView.setTextSize(txtSize);
        layout.addView(socialGoalMetView);

        if(socialGoalMet.equals("Concluded")){
            TextView socialIfConcluded = new TextView(this);
            String ifConcluded = newVisit.getSocialIfConcluded();
            socialIfConcluded.setText("The Outcome: " + ifConcluded);
            socialIfConcluded.setTextSize(txtSize);
            layout.addView(socialIfConcluded);
        }

        insertLineDivide(layout);

        TextView educationProvidedView = new TextView(this);
        educationProvidedView.setText("For Education, what was provided?");
        educationProvidedView.setTextSize(txtSize);
        layout.addView(educationProvidedView);
        ArrayList<Visit.Provided> arrayList2 = newVisit.getEducationProvided();
        for(int i = 0; i < arrayList2.size(); i++){
            String checkbox = arrayList2.get(i).getCheckBox();
            String explanation = arrayList2.get(i).getExplanation();
            TextView checkboxView = new TextView(this);
            checkboxView.setText("\t" + checkbox + ": " + explanation);
            checkboxView.setTextSize(txtSize);
            layout.addView(checkboxView);
        }

        insertLineDivide(layout);

        TextView educationGoalMetView = new TextView(this);
        String educationGoalMet = newVisit.getEducationGoalMet();
        educationGoalMetView.setText("Goal Met? " + educationGoalMet);
        educationGoalMetView.setTextSize(txtSize);
        layout.addView(educationGoalMetView);

        if(educationGoalMet.equals("Concluded")){
            TextView educationIfConcluded = new TextView(this);
            String ifConcluded = newVisit.getEducationIfConcluded();
            educationIfConcluded.setText("The Outcome: " + ifConcluded);
            educationIfConcluded.setTextSize(txtSize);
            layout.addView(educationIfConcluded);
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

    private void insertVisit() {
        boolean success = mydb.addVisit(newVisit);

        if(success) {
            VisitManager visitManager = VisitManager.getInstance(this);
            visitManager.addVisit(newVisit);
            Toast.makeText(NewVisitActivity.this, "Entry Successful!", Toast.LENGTH_LONG).show();
            Intent intent = ClientInfoActivity.makeIntent(NewVisitActivity.this, client_pos,  client_id);
            startActivity(intent);
        } else {
            Toast.makeText(NewVisitActivity.this, "Entry failed.", Toast.LENGTH_LONG).show();
        }
    }

    private void setUniqueVisitId(){
        DatabaseHelper db =  new DatabaseHelper(NewVisitActivity.this);

        int visit_no = db.numberOfVisitsPerClient(newVisit.getClientID());
        visit_no++;//next available visit id

        // Concatenate both strings
        String uniqueID = String.valueOf(newVisit.getClientID()) + String.valueOf(visit_no);

        // Convert the concatenated string to integer
        long uniqueID_long = Long.parseLong(uniqueID);

        newVisit.setVisit_id(uniqueID_long);
    }
}