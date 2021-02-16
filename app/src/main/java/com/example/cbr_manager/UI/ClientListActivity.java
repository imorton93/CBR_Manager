package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.cbr_manager.R;

import java.util.List;

public class ClientListActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, ClientListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        ListView list = findViewById(R.id.clientList);
        list.setEmptyView(findViewById(android.R.id.empty));
    }
}