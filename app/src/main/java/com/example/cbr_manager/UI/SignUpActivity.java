package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameTextBox, lastNameTextBox, emailTextBox, password1TextBox, password2TextBox;
    private Button submitButton;
    private DatabaseHelper mydb;
    private CBRWorker cbrWorker;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, SignUpActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameTextBox = findViewById(R.id.firstnameTextBox);
        lastNameTextBox = findViewById(R.id.lastnameTextBox);
        emailTextBox = findViewById(R.id.emailTextBox);
        password1TextBox = findViewById(R.id.password1TextBox);
        password2TextBox = findViewById(R.id.password2TextBox);

        submitButton = findViewById(R.id.submitButton);

        mydb = new DatabaseHelper(SignUpActivity.this);

        backButton();
        insertWorker();
    }

    private void insertWorker() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEntries()) {
                    if (validatePasswords()) {
                        cbrWorker = new CBRWorker(firstNameTextBox.getText().toString(), lastNameTextBox.getText().toString(),
                                emailTextBox.getText().toString(), BCrypt.withDefaults().hashToString(12, password1TextBox.getText().toString().toCharArray()));
                        boolean success = mydb.registerWorker(cbrWorker);
                        if(success) {
                            cbrWorker.setWorkerId((mydb.getWorkerId(cbrWorker.getUsername())));
                            Toast.makeText(SignUpActivity.this, "Sign Up Successful!", Toast.LENGTH_LONG).show();
                            Intent intent = LoginActivity.makeIntent(SignUpActivity.this);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(SignUpActivity.this, "Error Occured."+ success, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateEntries(){
        boolean bool = true;
        if(firstNameTextBox.length() == 0|| lastNameTextBox.length()==0
                ||password2TextBox.length()==0||emailTextBox.length()==0) {
            bool = false;
        }
        return bool;
    }


    private boolean validatePasswords(){
        return password2TextBox.getText().toString().equals(password1TextBox.getText().toString());
    }

    private void backButton(){
        Button backbtn = (Button) findViewById(R.id.backButtonSignup);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.makeIntent(SignUpActivity.this);
                startActivity(intent);
            }
        });
    }
}
