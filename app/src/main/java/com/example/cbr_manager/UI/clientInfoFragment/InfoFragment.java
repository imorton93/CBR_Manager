package com.example.cbr_manager.UI.clientInfoFragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import java.util.Arrays;

import java.sql.Blob;

public class InfoFragment extends Fragment {

    private ClientInfoActivity infoActivity;

    public InfoFragment() {
    }

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.infoActivity = (ClientInfoActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        getClientInfo(v);
        return v;
    }

    public void getClientInfo(View v){
        ClientManager clientManager = ClientManager.getInstance(infoActivity);
        Client currentClient = clientManager.getClientById(infoActivity.getId());

        ImageView profile = v.findViewById(R.id.image4);

        TextView name = v.findViewById(R.id.name);
        TextView gender = v.findViewById(R.id.genderAns);
        TextView age = v.findViewById(R.id.ageAns);
        TextView location = v.findViewById(R.id.locationAns);
        TextView village_num = v.findViewById(R.id.village_num_ans);
        TextView disability = v.findViewById(R.id.disabilityAns);

        String name_string = currentClient.getFirstName() + " " + currentClient.getLastName();

        String disabilities = Arrays.toString(currentClient.getDisabilities().toArray()).replace("[", "").replace("]", "").replace(",", "\n");
        DatabaseHelper handler = new DatabaseHelper(this.infoActivity);
        Cursor todoCursor = handler.getRow(infoActivity.getId());
        if (currentClient.getPhoto() != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(currentClient.getPhoto(), 0 , currentClient.getPhoto().length);
            profile.setImageBitmap(bmp);
        }



        name.setText(name_string);
        gender.setText(currentClient.getGender());
        age.setText(String.valueOf(currentClient.getAge()));
        location.setText(currentClient.getLocation());
        village_num.setText(Integer.toString(currentClient.getVillageNumber()));
        disability.setText(disabilities);
    }

}