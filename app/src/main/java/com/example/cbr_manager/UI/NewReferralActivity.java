package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cbr_manager.Forms.DisplayFormPage;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
import com.example.cbr_manager.Forms.NewReferral;
import com.example.cbr_manager.Forms.Question;
import com.example.cbr_manager.Forms.QuestionType;
import com.example.cbr_manager.Forms.TextQuestion;
import com.example.cbr_manager.R;

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
    ArrayList<FormPage> prostheticOrthoticPages;
    ArrayList<FormPage> otherExplanation;
    ArrayList<FormPage> selectedForm;
    Button next;
    Button back;
    ProgressBar progressBar;
    TextView progressText;

    ImageView imageView;

    NewReferral newReferral;


    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, NewReferralActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_referral);
        ToolbarButtons();

        //Button setup
        next = (Button) findViewById(R.id.nextBtnVisit);
        next.setBackgroundColor(Color.parseColor("#6661ED24"));
        back = (Button) findViewById(R.id.backBtn);
        imageView = new ImageView(this);
        newReferral = new NewReferral();

        form = (LinearLayout) findViewById(R.id.form);
        progressBar = (ProgressBar) findViewById(R.id.formProgress);
        progressText = (TextView) findViewById(R.id.formProgressText);

        currentPage = 1;

        //set up FormPages
        serviceRequirePage = new FormPage();
        otherExplanation = new ArrayList<>();
        wheelchairPages = new ArrayList<>();
        physioPages = new ArrayList<>();
        prostheticOrthoticPages = new ArrayList<>();

        imagePage = -1;

        createNewReferralForm();

        pageCount = 5;

        DisplayFormPage.displayPage(serviceRequirePage, form, this);

        progressBar.setMax(pageCount);

        setProgress(currentPage, pageCount);


        next.setOnClickListener(v -> {
            if(currentPage < pageCount - 1){
                if(currentPage == 1){
                    getSelectedForm();
                    pageCount = selectedForm.size() + 2;
                    back.setClickable(true);
                    back.setVisibility(View.VISIBLE);
                    back.setBackgroundColor(Color.parseColor("#6661ED24"));
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
                    DisplayFormPage.displayPage(selectedForm.get(currentPage - 2), form, NewReferralActivity.this);
                }
            }
            else if(currentPage == pageCount - 1){
                savePage(selectedForm.get(currentPage - 2));
                currentPage++;
                setProgress(currentPage, pageCount);
                clearForm();
                reviewPage();
                next.setText(R.string.finish);
            }




//            if (currentPage == 11) {
//                insertClient();
//            }
//
//            //make sure all required fields are filled in the page
//            else if(!requiredFieldsFilled(pages.get(currentPage - 1))){
//                requiredFieldsToast();
//            }
//            else if(currentPage < pageCount - 1){
//                if(currentPage == 1){
//                    back.setClickable(true);
//                    back.setVisibility(View.VISIBLE);
//
//                    back.setBackgroundColor(Color.parseColor("#6661ED24"));
//
//
//                }
//                //save answers
//                savePage(pages.get(currentPage - 1));
//
//                currentPage++;
//                setProgress(currentPage, pageCount);
//
//                clearForm();
//
//                if(currentPage == imagePage){
//                    displayPicture(pages.get(currentPage - 1));
//                }
//                else{
//                    DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this);
//
//                }
//
//                //load previously saved answers if any
//                loadAnswers(pages.get(currentPage - 1));
//
//            }
//            else if(currentPage == pageCount - 1){
//                //save answers
//                savePage(pages.get(currentPage - 1));
//                currentPage++;
//
//                setProgress(currentPage, pageCount);
//                clearForm();
//                reviewPage();
//                if(currentPage == pageCount){
//                    next.setText(R.string.finish);
//                }
//
//            }
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
                DisplayFormPage.displayPage(serviceRequirePage, form, NewReferralActivity.this);
            }
            else{
                DisplayFormPage.displayPage(selectedForm.get(currentPage - 2), form, NewReferralActivity.this);
            }
            //load previously saved answers if any
//            loadAnswers(pages.get(currentPage - 1));
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

    private void setProgress(int currentPage, int pageCount){
        progressBar.setProgress(currentPage);
        progressBar.setMax(pageCount);
        progressText.setText(currentPage + "/" + pageCount);
    }

    private void clearForm(){
        form.removeAllViews();
    }

    private void displayPicture(FormPage page){
        ArrayList<Question> questions = page.getQuestions();
        TextQuestion picQuestion = (TextQuestion) questions.get(0);
        TextView questionText = new TextView(this);
        questionText.setText(picQuestion.getQuestionString());
        form.addView(questionText);

        Button picButton = new Button(this);
        picButton.setText("Take Picture");
        picButton.setBackgroundColor(Color.parseColor("#6661ED24"));
        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        picButton.setLayoutParams(imageViewLayoutParams);
        picButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        });

        form.addView(picButton);
        form.addView(imageView);
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
        else if(selected.equals("Prosthetic") || selected.equals("Orthotic")){
            selectedForm = prostheticOrthoticPages;
            imagePage = -1;
        }
        else if(selected.equals("Wheelchair")){
            selectedForm = wheelchairPages;
            imagePage = 2;
        }
        else if(selected.equals("Other")){
            selectedForm = otherExplanation;
            imagePage = -1;
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
            }
        }
    }

    private void saveText(Question question){
        String tag = question.getQuestionTag();
        EditText input = (EditText) form.findViewWithTag(tag);
        if(tag.equals(getString(R.string.otherDescribe))){
            newReferral.setOtherDescription(input.getText().toString());
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
            newReferral.setExistingWheelchair(radioButton.getText().equals("Yes"));
        }
        else if(tag.equals(getString(R.string.repairChair))){
            newReferral.setCanChairRepair(radioButton.getText().equals("Yes"));
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
                    newReferral.setService(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.basicIntermediateUser))){
                    newReferral.setLevelWheelchairUser(radioButton.getText().toString());
                }
                else if(tag.equals(getString(R.string.injuryLocation))){
                    newReferral.setInjuryLocation(radioButton.getText().toString());
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
            newReferral.setHipWidth(num);
        }
    }

    private void saveDropDown(Question question){
        String tag = question.getQuestionTag();
        Spinner spinner = (Spinner) form.findViewWithTag(tag);
        String selected = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        if(tag.equals(getString(R.string.clientCondition))) {
            newReferral.setClientCondition(selected);
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

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(NewReferralActivity.this);
                startActivity(intent);
            }
        });
    }


    private void createNewReferralForm(){
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
        FormPage physioTwo = new FormPage();
        physioTwo.addToPage(physioCondition);
        physioPages.add(physioTwo);


        //set up prosthetic and orthotic pages
        MultipleChoiceQuestion injuryLocation = new MultipleChoiceQuestion(getString(R.string.injuryLocation), getString(R.string.injuryLocationQuestion_newReferralForm), QuestionType.RADIO, res.getStringArray(R.array.aboveBelow), true);
        FormPage firstPage = new FormPage();
        firstPage.addToPage(injuryLocation);
        prostheticOrthoticPages.add(firstPage);

        //Other pages
        TextQuestion explanation = new TextQuestion(getString(R.string.otherDescribe), getString(R.string.pleaseDescribe_newReferralForm), QuestionType.PLAIN_TEXT, true);
        FormPage otherOne = new FormPage();
        otherOne.addToPage(explanation);
        otherExplanation.add(otherOne);


    }

    private void reviewPage(){
        TextView serviceTypeView = new TextView(this);
        String serviceType = newReferral.getService();
        serviceTypeView.setText(serviceType);
        form.addView(serviceTypeView);
        if(serviceType.equals("Physiotherapy")){
            String condition = newReferral.getClientCondition();
            TextView conditionView = new TextView(this);
            conditionView.setText("Condition: " + condition);
            form.addView(conditionView);
        }
        else if(serviceType.equals("Prosthetic")){
            String injuryLocation = newReferral.getInjuryLocation();
            TextView injuryLocationView = new TextView(this);
            injuryLocationView.setText("Injury above or below the knee: " + injuryLocation);
            form.addView(injuryLocationView);
        }
        else if(serviceType.equals("Orthotic")){
            String injuryLocation = newReferral.getInjuryLocation();
            TextView injuryLocationView = new TextView(this);
            injuryLocationView.setText("Injury above or below the knee: " + injuryLocation);
            form.addView(injuryLocationView);
        }
        else if(serviceType.equals("Wheelchair")){
            String userType = newReferral.getLevelWheelchairUser();
            TextView userTypeView = new TextView(this);
            userTypeView.setText("Type of user: " + userType);
            form.addView(userTypeView);

            int hipWidth = newReferral.getHipWidth();
            String hipWidthString = Integer.toString(hipWidth);
            TextView hipWidthView = new TextView(this);
            hipWidthView.setText("Hip Width(inches): " + hipWidthString);
            form.addView(hipWidthView);

            Boolean existingChair = newReferral.getExistingWheelchair();
            TextView existingChairView = new TextView(this);
            if(existingChair){
                existingChairView.setText("Existing Chair: Yes");
            }
            else{
                existingChairView.setText("Existing Chair: No");
            }
            form.addView(existingChairView);

            Boolean canBeFixed = newReferral.getCanChairRepair();
            TextView canBeFixedView = new TextView(this);
            if(canBeFixed){
                canBeFixedView.setText("Can be Fixed: Yes");
            }
            else{
                canBeFixedView.setText("Can be Fixed: No");
            }
            form.addView(canBeFixedView);

        }
        else if(serviceType.equals("Other")){
            String otherExplanation = newReferral.getOtherDescription();
            TextView otherExplanationView = new TextView(this);
            otherExplanationView.setText("Explanation" + otherExplanation);
            form.addView(otherExplanationView);
        }
    }
}