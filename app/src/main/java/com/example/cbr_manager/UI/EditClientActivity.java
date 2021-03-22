package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Forms.DisplayFormPage;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
import com.example.cbr_manager.Forms.QuestionType;
import com.example.cbr_manager.Forms.TextQuestion;
import com.example.cbr_manager.R;

import java.util.ArrayList;

public class EditClientActivity extends AppCompatActivity {
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

    Client client;
    private DatabaseHelper mydb;

    private long id;
    private int position;

    public static final String R_CLIENT_ID_PASSED_IN = "r_client_id_passed_in";
    public static final String R_CLIENT_POS_PASSED_IN = "r_client_POS_passed_in";

    public static Intent makeIntent(Context context, int position, long id) {
        Intent intent =  new Intent(context, EditClientActivity.class);
        intent.putExtra(R_CLIENT_ID_PASSED_IN, id);
        intent.putExtra(R_CLIENT_POS_PASSED_IN, position);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);

        mydb = new DatabaseHelper(EditClientActivity.this);
        next = (Button) findViewById(R.id.nextBtnVisit);

        next.setBackgroundColor(Color.parseColor("#6661ED24"));

        back = (Button) findViewById(R.id.backBtn);
        imageView = new ImageView(this);
        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imageViewLayoutParams);

        form = (LinearLayout) findViewById(R.id.form);
        progressBar = (ProgressBar) findViewById(R.id.formProgress);
        progressText = (TextView) findViewById(R.id.formProgressText);

        currentPage = 1;
        pages = new ArrayList<>();

        ToolbarButtons();

        extractIntent();
        getClientInfo();

        createEditForm();
        pageCount = pages.size() + 1;

        DisplayFormPage.displayPage(pages.get(currentPage-1), form, this);

        progressBar.setMax(pageCount);
        setProgress(currentPage, pageCount);
    }

    private void setProgress(int currentPage, int pageCount){
        progressBar.setProgress(currentPage);
        progressText.setText(currentPage + "/" + pageCount);
    }

    private void extractIntent(){
        Intent intent = getIntent();
        this.position = intent.getIntExtra(R_CLIENT_POS_PASSED_IN, 0);
        this.id = intent.getLongExtra(R_CLIENT_ID_PASSED_IN, 0);
        //GET RID OF PRINT!!
        System.out.println("Id is " + this.id);
    }

    private void getClientInfo(){
        ClientManager manager = ClientManager.getInstance(this);
        client = manager.getClientById(id);
    }


    private void createEditForm(){
        Resources res = getResources();

        //page one: first and last name
        TextQuestion firstName = new TextQuestion(getString(R.string.firstName),getString(R.string.firstName_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion lastName = new TextQuestion(getString(R.string.lastName),getString(R.string.lastName_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageOne = new FormPage();
        pageOne.addToPage(firstName);
        pageOne.addToPage(lastName);
        pages.add(pageOne);

        //page two: Age gender
        TextQuestion age = new TextQuestion(getString(R.string.age),getString(R.string.age_newClientForm), QuestionType.NUMBER, true);
        MultipleChoiceQuestion gender = new MultipleChoiceQuestion(getString(R.string.gender),getString(R.string.gender_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.gender), true);
        FormPage pageTwo = new FormPage();
        pageTwo.addToPage(age);
        pageTwo.addToPage(gender);
        pages.add(pageTwo);


        //page three: Location Village No. Contact Number   GPS LATER!!!!
        MultipleChoiceQuestion location = new MultipleChoiceQuestion(getString(R.string.location),getString(R.string.location_newClientForm), QuestionType.DROP_DOWN,res.getStringArray(R.array.locations), true);
        TextQuestion villageNum = new TextQuestion(getString(R.string.villageNumber),getString(R.string.villageNumber_newClientForm), QuestionType.NUMBER, true);
        TextQuestion contactNum = new TextQuestion(getString(R.string.contactNumber),getString(R.string.contactNumber_newClientForm), QuestionType.PHONE_NUMBER, true);
        FormPage pageThree = new FormPage();
        pageThree.addToPage(location);
        pageThree.addToPage(villageNum);
        pageThree.addToPage(contactNum);
        pages.add(pageThree);

        //page four: Caregiver
        MultipleChoiceQuestion caregiverPresent = new MultipleChoiceQuestion(getString(R.string.caregiverPresent),getString(R.string.caregiverPresent_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.yes_no_answer), true);
        TextQuestion caregiverContactNumber = new TextQuestion(getString(R.string.caregiverContactNumber),getString(R.string.caregiverNumber_newClientForm), QuestionType.PHONE_NUMBER, false);
        FormPage pageFour = new FormPage();
        pageFour.addToPage(caregiverPresent);
        pageFour.addToPage(caregiverContactNumber);
        pages.add(pageFour);


        //page five: photo
        TextQuestion photo = new TextQuestion(getString(R.string.photo),getString(R.string.photo_newClientForm), QuestionType.PICTURE, false);
        imagePage = 6;
        FormPage pageFive = new FormPage();
        pageFive.addToPage(photo);
        pages.add(pageFive);




        //page six: Type of disability
        MultipleChoiceQuestion disability = new MultipleChoiceQuestion(getString(R.string.disabilityType),getString(R.string.disabilityType_newClientForm), QuestionType.CHECK_BOX, res.getStringArray(R.array.disability_types), true);
        FormPage pageSix = new FormPage();
        pageSix.addToPage(disability);
        pages.add(pageSix);


        //page seven: clients health rate, require individual goal
        MultipleChoiceQuestion clientHealthRate = new MultipleChoiceQuestion(getString(R.string.healthRate),getString(R.string.healthRate_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type), true);
        TextQuestion healthRequire = new TextQuestion(getString(R.string.healthRequire),getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion healthIndividualGoal = new TextQuestion(getString(R.string.healthIndividualGoal),getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageSeven = new FormPage();
        pageSeven.addToPage(clientHealthRate);
        pageSeven.addToPage(healthRequire);
        pageSeven.addToPage(healthIndividualGoal);
        pages.add(pageSeven);


        //page eight: clients education rate
        MultipleChoiceQuestion clientEducationRate = new MultipleChoiceQuestion(getString(R.string.educationRate),getString(R.string.educationStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type), true);
        TextQuestion educationRequire = new TextQuestion(getString(R.string.educationRequire),getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion educationIndividualGoal = new TextQuestion(getString(R.string.educationIndividualGoal),getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageEight = new FormPage();
        pageEight.addToPage(clientEducationRate);
        pageEight.addToPage(educationRequire);
        pageEight.addToPage(educationIndividualGoal);
        pages.add(pageEight);

        //page nine: social status
        MultipleChoiceQuestion clientSocialRate = new MultipleChoiceQuestion(getString(R.string.socialRate),getString(R.string.socialStatus_newClientForm), QuestionType.RADIO, res.getStringArray(R.array.risk_type), true);
        TextQuestion socialRequire = new TextQuestion(getString(R.string.socialRequire),getString(R.string.require_newClientForm), QuestionType.PLAIN_TEXT, true);
        TextQuestion socialIndividualGoal = new TextQuestion(getString(R.string.socialIndividualGoal),getString(R.string.individualGoal_newClientForm), QuestionType.PLAIN_TEXT, true);
        FormPage pageNine = new FormPage();
        pageNine.addToPage(clientSocialRate);
        pageNine.addToPage(socialRequire);
        pageNine.addToPage(socialIndividualGoal);
        pages.add(pageNine);
    }

    private void reviewPage(){

        TextView firstNameView = new TextView(this);
        String firstName = client.getFirstName();
        firstNameView.setText("First Name: " + firstName);
        form.addView(firstNameView);

        TextView lastNameView = new TextView(this);
        String lastName = client.getLastName();
        lastNameView.setText("Last Name: " + lastName);
        form.addView(lastNameView);

        TextView ageView = new TextView(this);
        int age = client.getAge();
        String ageStr = Integer.toString(age);
        ageView.setText("Age: " + ageStr);
        form.addView(ageView);

        TextView genderView = new TextView(this);
        String gender = client.getGender();
        genderView.setText("Gender: " + gender);
        form.addView(genderView);

        TextView locationView = new TextView(this);
        String location = client.getLocation();
        locationView.setText("Location: " + location);
        form.addView(locationView);

        TextView villageNumberView = new TextView(this);
        int villageNumber = client.getVillageNumber();
        String villageNumberStr = Integer.toString(villageNumber);
        villageNumberView.setText("Village Number: " + villageNumberStr);
        form.addView(villageNumberView);

        TextView contactNumberView = new TextView(this);
        String contactNumber = client.getContactPhoneNumber();
        contactNumberView.setText("Contact Number: " + contactNumber);
        form.addView(contactNumberView);

        TextView caregiverPresentView = new TextView(this);
        Boolean caregiverPresent = client.getCaregiverPresent();
        if(caregiverPresent){
            caregiverPresentView.setText("Caregiver Present: Yes");
        }
        else {
            caregiverPresentView.setText("Caregiver Present: No");
        }
        form.addView(caregiverPresentView);

        TextView disabilitiesView = new TextView(this);
        ArrayList<String> disabilities = client.getDisabilities();
        String disabilitiesStr = "Type of Disability: ";
        for(int i = 0; i < disabilities.size(); i++){
            disabilitiesStr = disabilitiesStr.concat(disabilities.get(i));
            disabilitiesStr = disabilitiesStr.concat(", ");
        }
        disabilitiesView.setText(disabilitiesStr);
        form.addView(disabilitiesView);

        TextView healthRateView = new TextView(this);
        String healthRate = client.getHealthRate();
        healthRateView.setText("Rate of Client's health: " + healthRate);
        form.addView(healthRateView);

        TextView educationRateView = new TextView(this);
        String educationRate = client.getEducationRate();
        educationRateView.setText("Rate of Client's Education: " + educationRate);
        form.addView(educationRateView);

        TextView socialRateView = new TextView(this);
        String socialRate = client.getSocialStatusRate();
        socialRateView.setText("Rate of Client's Social Status: " + socialRate);
        form.addView(socialRateView);

    }

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(EditClientActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(EditClientActivity.this);
                startActivity(intent);
            }
        });
    }
}