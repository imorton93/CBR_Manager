package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;

public class LoginActivity extends AppCompatActivity {

    private String username, password;
    private EditText usernameTextBox, passwordTextBox;
    private Button login_btn;
    private DatabaseHelper mydb;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, LoginActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mydb = new DatabaseHelper(LoginActivity.this);

        ClientManager clientManager = ClientManager.getInstance(LoginActivity.this);
        clientManager.clear();
        clientManager.updateList();

        VisitManager visitManager = VisitManager.getInstance(LoginActivity.this);
        visitManager.clear();
        visitManager.updateList();

        buttonsClicked();
    }

    private void buttonsClicked(){
        usernameTextBox = findViewById(R.id.usernameTextBox);
        passwordTextBox = findViewById(R.id.passwordTextBox);

        login_btn = findViewById(R.id.loginButton);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameTextBox.getText().toString();
                password = passwordTextBox.getText().toString();
                // Do something with login button
                if (mydb.checkUser(username, password)) {
                    Intent intent = TaskViewActivity.makeIntent(LoginActivity.this);
                    intent.putExtra("Worker Username", username);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong credentials.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        Button signup_btn = (Button) findViewById(R.id.signupButton);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something with signup button
                Intent intent = SignUpActivity.makeIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }

    // TODO: implement
    private void forgotButton(){
        Button forgotBtn = findViewById(R.id.forgotButton);
        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}