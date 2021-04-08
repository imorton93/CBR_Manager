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
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ImageView;

import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.google.android.material.dialog.MaterialDialogs;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ToolbarButtons();

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

        firstNameTextView.setText(currentCBRWorker.getFirstName());
        lastNameTextView.setText(currentCBRWorker.getLastName());
        emailTextView.setText(currentCBRWorker.getUsername());
        zoneTextView.setText(currentCBRWorker.getZone());

        profilePictureImageView = findViewById(R.id.profilePicture);
        uploadPhoto = findViewById(R.id.uploadButton);

        uploadPhoto.setOnClickListener(this);
// COME BACK TOOOO
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

    }
    private void saveProfilePicture(){
            profilePictureImageView.setDrawingCacheEnabled(true);
            profilePictureImageView.buildDrawingCache();
            Bitmap bitmap = profilePictureImageView.getDrawingCache();
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas);
            byte[] data = boas.toByteArray();
            DatabaseHelper.saveCBRPhoto(data);
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
                                        profilePictureImageView.setImageResource(R.drawable.ic_launcher_background);
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

    private void onCaptureImageResult(Intent data){
        thumbnail = (Bitmap) data.getExtras().get("data");
        setProgressBar();
        //set profile picture from camera
        profilePictureImageView.setMaxWidth(226); // TODO REMOVE?
        profilePictureImageView.setImageBitmap(thumbnail);
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
        int size = adminMessageManager.size();

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

