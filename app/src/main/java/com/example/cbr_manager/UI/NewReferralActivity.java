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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cbr_manager.Forms.DisplayFormPage;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
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
    //serviceReqruiePage is the first page of the new referral form
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
            if(currentPage == 1){
                getSelectedForm();
                pageCount = selectedForm.size() + 2;
                back.setClickable(true);
                back.setVisibility(View.VISIBLE);
                back.setBackgroundColor(Color.parseColor("#6661ED24"));
            }

            //save page

            currentPage++;
            setProgress(currentPage, pageCount);

            clearForm();

            if(currentPage == imagePage){
                displayPicture(selectedForm.get(currentPage - 2));
            }
            else{
                DisplayFormPage.displayPage(selectedForm.get(currentPage - 2), form, NewReferralActivity.this);
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

            if(currentPage == 6){
                displayPicture(pages.get(currentPage - 1));
            }
            else{
                DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this);
            }
            //load previously saved answers if any
//            loadAnswers(pages.get(currentPage - 1));
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
}