package com.example.cbr_manager.Database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class CBRWorkerManager {
    private List<Visit> cbrWorkers = new ArrayList<>();
    private static CBRWorkerManager instance;
    private DatabaseHelper databaseHelper;
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

//    public CBRWorker getCBRById(long id){
//        for(CBRWorker cbrWorker : cbrWorkers){
//            if(cbrWorker.getId() == id){
//                return cbrWorker;
//            }
//        }
//        return new CBRWorker();
//    }
}
