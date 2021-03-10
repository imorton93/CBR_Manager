package com.example.cbr_manager.UI;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

public class ViewDataCBR extends AppCompatActivity {

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        TextView textView = findViewById(R.id.profileData);

        Cursor c = databaseHelper.viewCBRData();

        StringBuilder stringBuilder = new StringBuilder();

        while(c.moveToNext()){
            stringBuilder.append("\nFirst Name: " +c.getString(0) + "\nLast name: " +c.getString(1)
                    + "\nEmail / Username: " + c.getString(2));

        }
        textView.setText(stringBuilder);
    }
}
