package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.example.cbr_manager.Forms.FormPage;
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

        reviewPage();
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