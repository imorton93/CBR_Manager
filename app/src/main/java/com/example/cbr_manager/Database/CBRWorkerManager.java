package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cbr_manager.R;

import java.util.ArrayList;
import java.util.List;

public class CBRWorkerManager {
    private List<CBRWorker> cbrWorkers = new ArrayList<>();
    private static CBRWorkerManager instance;
    private DatabaseHelper databaseHelper;
    private Context context;
    private SQLiteDatabase database;
    int listLayoutRes;
    private static final String cbr_id = "ID";
    private static final String cbr_first_name = "FIRST_NAME";
    private static final String cbr_last_name = "LAST_NAME";
    private static final String client_location = "LOCATION";
    private static final String cbr_email = "EMAIL";
    private static final String cbr_password = "PASSWORD";


    public CBRWorkerManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public static CBRWorkerManager getInstance(Context context) {
        if (instance == null) {
            instance = new CBRWorkerManager(context);
        }
        return instance;
    }

    public CBRWorker getCBRById(long id){
        for (CBRWorker cbrWorker : cbrWorkers) {
            if(cbrWorker.getId() == id){
                return cbrWorker;
            }
        }
        return new CBRWorker();
    }

}
