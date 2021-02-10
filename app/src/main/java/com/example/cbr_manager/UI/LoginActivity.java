package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cbr_manager.R;

public class LoginActivity extends AppCompatActivity {

    private String username, password;

    private EditText usernameTextBox, passwordTextBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameTextBox = (EditText) findViewById(R.id.usernameTextBox);
        passwordTextBox = (EditText) findViewById(R.id.passwordTextBox);

        Button login_btn = (Button) findViewById(R.id.loginButton);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameTextBox.getText().toString();
                password = passwordTextBox.getText().toString();
                // Do something with login button
            }
        });

        Button signup_btn = (Button) findViewById(R.id.signupButton);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something with signup button
            }
        });

    }
}