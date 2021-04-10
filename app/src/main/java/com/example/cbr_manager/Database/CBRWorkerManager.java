package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cbr_manager.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CBRWorkerManager {
    private List<CBRWorker> cbrWorkers = new ArrayList<>();
    private static CBRWorkerManager instance;
    private DatabaseHelper databaseHelper;
    private Context context;
    private SQLiteDatabase database;
    int listLayoutRes;
    private static final String cbr_first_name = "FIRST_NAME";
    private static final String cbr_last_name = "LAST_NAME";
    private static final String cbr_email = "USERNAME";
    private static final String cbr_zone ="ZONE";
    private static final String cbr_password = "PASSWORD";
    private static final String cbr_photo = "PHOTO";

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

    public List<CBRWorker> getCbrWorkers() {
        return cbrWorkers;
    }

    public CBRWorker getCBRByUsernameAndPassword(String username){
        for (CBRWorker cbrWorker : cbrWorkers) {
            if(cbrWorker.getUsername().equals(username)){
                return cbrWorker;
            }
        }
        return new CBRWorker();
    }

    public void clear() {
        cbrWorkers.clear();
    }

    public void updateList() {
        Cursor c = databaseHelper.getAllRowsOfCBR();

        int firstI = c.getColumnIndex(cbr_first_name);
        int lastI = c.getColumnIndex(cbr_last_name);
        int emailI = c.getColumnIndex(cbr_email);
        int zoneI = c.getColumnIndex(cbr_zone);
        int passwordI = c.getColumnIndex(cbr_password);
        int photoI = c.getColumnIndex(cbr_photo);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

            String firstName = c.getString(firstI);
            String lastName = c.getString(lastI);
            String email = c.getString(emailI);
            String zone = c.getString(zoneI);
            String password = c.getString(passwordI);
            byte[] photo = c.getBlob(photoI);

            CBRWorker cbrWorker = new CBRWorker(firstName, lastName, email, zone, password, photo);
            cbrWorkers.add(cbrWorker);
        }
    }

}
