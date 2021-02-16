package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cbr_manager.R;

public class TaskViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        ImageView newClient = (ImageView) findViewById(R.id.newclient);
        newClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskViewActivity.this, NewClientActivity.class));
            }
        });
    }
}