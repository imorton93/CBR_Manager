package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cbr_manager.Forms.DisplayFormPage;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
import com.example.cbr_manager.Forms.QuestionType;
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
    Button next;
    Button back;
    ProgressBar progressBar;
    TextView progressText;

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


        next.setOnClickListener(v -> {
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
//            if(currentPage == pageCount){
//                next.setText(R.string.next);
//            }
//            currentPage--;
//            setProgress(currentPage, pageCount);
//            clearForm();
//
//            if(currentPage == 6){
//                displayPicture(pages.get(currentPage - 1));
//            }
//            else{
//                DisplayFormPage.displayPage(pages.get(currentPage - 1), form, NewClientActivity.this);
//            }
//            //load previously saved answers if any
//            loadAnswers(pages.get(currentPage - 1));
//            if(currentPage == 1){
//                back.setClickable(false);
//                back.setVisibility(View.INVISIBLE);
//                back.setBackgroundColor(Color.DKGRAY);
//
//            }
        });
        back.setClickable(false);
        back.setVisibility(View.INVISIBLE);

        back.setBackgroundColor(Color.DKGRAY);


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
        MultipleChoiceQuestion require = new MultipleChoiceQuestion(getString(R.string.serviceRequire), getString(R.string.serviceRequireQuestion_newReferralForm), QuestionType.RADIO, res.getStringArray(R.array.))
        serviceRequirePage.addToPage();
    }
}