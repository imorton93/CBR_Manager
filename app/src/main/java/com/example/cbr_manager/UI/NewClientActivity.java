package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.Forms.*;

import java.util.ArrayList;

public class NewClientActivity extends AppCompatActivity {
    LinearLayout form;
    int currentPage;
    ArrayList<FormPage> pages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);


        form = (LinearLayout) findViewById(R.id.form);
        currentPage = 1;
        pages = new ArrayList<>();



        Button next = (Button) findViewById(R.id.nextBtn);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                pageOne();
            }
        });
    }


    private void clearForm(){
        form.removeAllViews();
    }



    private void createNewClientForm(){
        //page one

    }
}