package com.example.cbr_manager.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.CBRWorkerManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.google.android.material.dialog.MaterialDialogs;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import at.favre.lib.crypto.bcrypt.BCrypt;

import static com.example.cbr_manager.UI.LoginActivity.currentCBRWorker;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView firstNameTextView, lastNameTextView, emailTextView, zoneTextView;
    private ImageView profilePictureImageView;
    private Button uploadPhoto;

    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;

    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private boolean hasImageChange = false;
    Bitmap thumbnail;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private DatabaseHelper mydb;
    private CBRWorker cbrWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ToolbarButtons();

        mydb = new DatabaseHelper(ProfileActivity.this);
        CBRWorkerManager manager = CBRWorkerManager.getInstance(this);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        cbrWorker = manager.getCBRByUsernameAndPassword(username);

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(ProfileActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);

        profilePageButtons();

        firstNameTextView = findViewById(R.id.profileFname);
        lastNameTextView = findViewById(R.id.profileLname);
        emailTextView = findViewById(R.id.profileUsername);
        zoneTextView = findViewById(R.id.profileZone);
        profilePictureImageView = findViewById(R.id.profilePicture);
        uploadPhoto = findViewById(R.id.uploadButton);

        firstNameTextView.setText(currentCBRWorker.getFirstName());
        lastNameTextView.setText(currentCBRWorker.getLastName());
        emailTextView.setText(currentCBRWorker.getUsername());
        zoneTextView.setText(currentCBRWorker.getZone());

        if (currentCBRWorker.getPhoto() != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(currentCBRWorker.getPhoto(), 0 , currentCBRWorker.getPhoto().length);
            profilePictureImageView.setImageBitmap(bmp);
        }

        uploadPhoto.setOnClickListener(this);

        if(ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            profilePictureImageView.setEnabled(false);
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } else {
            profilePictureImageView.setEnabled(true);
        }
    }

    private void profilePageButtons(){
        Button signoutButton = findViewById(R.id.signoutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.makeIntent(ProfileActivity.this);
                startActivity(intent);
            }
        });

        Button editButton = findViewById(R.id.editBtn);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectedToInternet()) {
                    Toast.makeText(ProfileActivity.this, "Please connect to the internet and try again!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = EditCBRActivity.makeIntent(ProfileActivity.this);
                    startActivity(intent);
                }
            }
        });

    }

    private void saveProfilePicture(){
        profilePictureImageView.setDrawingCacheEnabled(true);
        profilePictureImageView.buildDrawingCache();
        Bitmap bitmap = profilePictureImageView.getDrawingCache();
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas);
        byte[] data = boas.toByteArray();
        cbrWorker.setPhoto(data);
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.uploadButton:
                new MaterialDialog.Builder(this)
                        .title("Set your image")
                        .items(R.array.uploadImages)
                        .itemsIds(R.array.itemIds)
                        .itemsCallback(new MaterialDialog.ListCallback() {

                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which){
                                    case 0:
                                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                        photoPickerIntent.setType("image/*");
                                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                                        break;
                                    case 1:
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, CAPTURE_PHOTO);
                                        break;
                                    case 2:
                                        profilePictureImageView.setImageResource(R.drawable.stockprofileimage);
                                        if (!connectedToInternet()) {
                                            Toast.makeText(ProfileActivity.this, "Please connect to the internet and try again!", Toast.LENGTH_LONG).show();
                                        } else {
                                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
                                            String curr_username = sharedPref.getString("username", null);

                                            cbrWorker.setWorkerId(mydb.getWorkerId(curr_username));
                                            cbrWorker.setFirstName(currentCBRWorker.getFirstName());
                                            cbrWorker.setLastName(currentCBRWorker.getLastName());
                                            cbrWorker.setUsername(currentCBRWorker.getUsername());
                                            cbrWorker.setZone(currentCBRWorker.getZone());
                                            cbrWorker.setPassword(BCrypt.withDefaults().hashToString(12, currentCBRWorker.getPassword().toCharArray()));

                                            cbrWorker.setPhoto(null);

                                            boolean success = mydb.updateWorker(cbrWorker);
                                            if (success) {
                                                cbrWorker.setWorkerId((mydb.getWorkerId(cbrWorker.getUsername())));
                                                sharedPref.edit().putString("username", cbrWorker.getUsername()).apply();
                                            } else {
                                                Toast.makeText(ProfileActivity.this, "Error Occurred." + success, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        break;
                                }
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                profilePictureImageView.setEnabled(true);
            }
        }
    }

    public void setProgressBar(){
        progressBar= new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

        new Thread((Runnable) () -> {
            while(progressBarStatus < 100){
                progressBarStatus +=30;

                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                progressBarHandler.post(() -> {
                    progressBar.setProgress(progressBarStatus);
                });
            }
            if (progressBarStatus >= 100){
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                progressBar.dismiss();
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO){
            if(resultCode == RESULT_OK){
                try{
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    setProgressBar();
                    profilePictureImageView.setImageBitmap(selectedImage);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }else if(requestCode == CAPTURE_PHOTO){
            if(resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        setProgressBar();

        //set profile picture from camera
        profilePictureImageView.setImageBitmap(thumbnail);

        if (!connectedToInternet()) {
            Toast.makeText(ProfileActivity.this, "Please connect to the internet and try again!", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
            String curr_username = sharedPref.getString("username", null);

            cbrWorker.setWorkerId(mydb.getWorkerId(curr_username));
            cbrWorker.setFirstName(currentCBRWorker.getFirstName());
            cbrWorker.setLastName(currentCBRWorker.getLastName());
            cbrWorker.setUsername(currentCBRWorker.getUsername());
            cbrWorker.setZone(currentCBRWorker.getZone());
            cbrWorker.setPassword(BCrypt.withDefaults().hashToString(12, currentCBRWorker.getPassword().toCharArray()));

            saveProfilePicture();

            boolean success = mydb.updateWorker(cbrWorker);
            if (success) {
                cbrWorker.setWorkerId((mydb.getWorkerId(cbrWorker.getUsername())));
                sharedPref.edit().putString("username", cbrWorker.getUsername()).apply();
            } else {
                Toast.makeText(ProfileActivity.this, "Error Occurred." + success, Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean connectedToInternet () {
        //Reference: https://developer.android.com/training/monitoring-device-state/connectivity-status-type
        ConnectivityManager connectManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectManager.getActiveNetworkInfo();
        return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(ProfileActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(ProfileActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(ProfileActivity.this);
                startActivity(intent);
            }
        });
    }

    private void badgeNotification(AdminMessageManager adminMessageManager, TextView badge) {
        int size = adminMessageManager.numUnread();

        if (badge != null) {
            if (size == 0) {
                if (badge.getVisibility() != View.GONE) {
                    badge.setVisibility(View.GONE);
                }
            } else {
                badge.setText(String.valueOf(Math.min(size, 99)));
                if (badge.getVisibility() != View.VISIBLE) {
                    badge.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }
}

