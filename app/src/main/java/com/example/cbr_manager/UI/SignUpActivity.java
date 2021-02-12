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

public class SignUpActivity extends AppCompatActivity {

    private EditText firstnameTextBox, lastnameTextBox, emailTextBox, password1TextBox, password2TextBox;
    private Button submitButton;
    private DatabaseHelper mydb;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, SignUpActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstnameTextBox = findViewById(R.id.firstnameTextBox);
        lastnameTextBox = findViewById(R.id.lastnameTextBox);
        emailTextBox = findViewById(R.id.emailTextBox);
        password1TextBox = findViewById(R.id.password1TextBox);
        password2TextBox = findViewById(R.id.password2TextBox);

        submitButton = findViewById(R.id.submitButton);

        mydb = new DatabaseHelper(SignUpActivity.this);

        insertWorker();

    }

    private void insertWorker() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vailidateEntries()) {
                    if (vailidatePasswords()) {
                        CBRWorker cbrWorker;
                        cbrWorker = new CBRWorker(firstnameTextBox.getText().toString(), lastnameTextBox.getText().toString(),
                                emailTextBox.getText().toString(), password1TextBox.getText().toString());
                        boolean success = mydb.registerWorker(cbrWorker);
                        if(success)
                            Toast.makeText(SignUpActivity.this, "Sign Up Successful!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SignUpActivity.this, "Error Occured."+ success, Toast.LENGTH_LONG).show();
                        Intent intent = LoginActivity.makeIntent(SignUpActivity.this);
                        startActivity(intent);
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

    private boolean vailidateEntries(){
        boolean bool = true;
        if(firstnameTextBox.length() == 0||lastnameTextBox.length()==0
                ||password2TextBox.length()==0||emailTextBox.length()==0) {
            bool = false;
        }
        return bool;
    }


    private boolean vailidatePasswords(){
        if(!password2TextBox.getText().toString().equals(password1TextBox.getText().toString())){
            return false;
        }
        return true;
    }

}
