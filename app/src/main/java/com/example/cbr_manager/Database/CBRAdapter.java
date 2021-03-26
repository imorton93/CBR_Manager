package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cbr_manager.R;

import java.util.List;

public class CBRAdapter extends ArrayAdapter<CBRWorker> {

    Context mCtx;
    int listLayoutRes;
    List<CBRWorker> CBRWorkerList;
    SQLiteDatabase mDatabase;

    public CBRAdapter(Context mCtx, int listLayoutRes, List<CBRWorker> cbrWorkerList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, cbrWorkerList);
        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.CBRWorkerList = cbrWorkerList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        CBRWorker currentCBR;

        currentCBR = CBRWorkerList.get(position);

        TextView firstNameTextView = view.findViewById(R.id.profileFname);
        TextView lastNameTextView = view.findViewById(R.id.profileLname);
        TextView emailTextView = view.findViewById(R.id.profileUsername);

        firstNameTextView.setText(currentCBR.getFirstName());
        lastNameTextView.setText(currentCBR.getLastName());
        emailTextView.setText(currentCBR.getEmail());

        return view;
    }

}
