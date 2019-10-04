package com.fox.assignment403;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fox.assignment403.model.Photo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;

import static com.fox.assignment403.constant.Constants.PERMISSION_REQUEST_CODE;
import static com.fox.assignment403.constant.Constants.PROGRESS_UPDATE;

public class PreviewPhotoActivity extends AppCompatActivity {

    private Photo photo;
    private ImageView imageView;
    //private String url_m, url_z, url_c, url_l, url_o;
    private FloatingActionsMenu menuMultipleActions;
    private String [] link = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_preview_photo);
        initViews();
        Intent intent = getIntent();
        if(intent != null){
            photo = (Photo) intent.getSerializableExtra("photo");
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.dummy);
        Glide.with(PreviewPhotoActivity.this)
                .load(photo.getUrlO() != null ? photo.getUrlO() : photo.getUrlL())
                .error(R.drawable.dummy)
                .apply(requestOptions)
                .into(imageView);
        //Add button download <m,z,c,l,o>
        getPhotoUrl();
        initDownloadButton(link);
    }

    private void initViews(){
        imageView = findViewById(R.id.imageView_widget);
        menuMultipleActions = findViewById(R.id.multiple_actions);
    }

    private void getPhotoUrl(){
        link[0] = photo.getUrlO();
        link[1] = photo.getUrlL();
        link[2] = photo.getUrlC();
        link[3] = photo.getUrlZ();
        link[4] = photo.getUrlM();
    }

    private void initDownloadButton(String [] url){
        for(int i = 0;i < url.length;i++){
            if(url[i] != null){
                FloatingActionButton floatingActionButton = new FloatingActionButton(PreviewPhotoActivity.this);
                switch (i){
                    case 0:
                        floatingActionButton.setTitle(photo.getWidthO() + " x " + photo.getHeightO());
                        break;
                    case 1:
                        floatingActionButton.setTitle(photo.getWidthL() + " x " + photo.getHeightL());
                        break;
                    case 2:
                        floatingActionButton.setTitle(photo.getWidthC() + " x " + photo.getHeightC());
                        break;
                    case 3:
                        floatingActionButton.setTitle(photo.getWidthZ() + " x " + photo.getHeightZ());
                        break;
                    case 4:
                        floatingActionButton.setTitle(photo.getWidthM() + " x " + photo.getHeightM());
                        break;
                }
                floatingActionButton.setIcon(R.drawable.ic_file_download_black_24dp);
                floatingActionButton.setColorNormalResId(R.color.blue_semi_transparent);
                floatingActionButton.setColorPressedResId(R.color.blue_semi_transparent_pressed);
                floatingActionButton.setStrokeVisible(false);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkPermission()){
                            onDownloadPhoto();
                        }else{
                            requestPermissions();
                        }
                    }
                });
                registerReceiver();
                menuMultipleActions.addButton(floatingActionButton);
            }
        }
    }

    private void onDownloadPhoto() {
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void registerReceiver() {
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PROGRESS_UPDATE);
        bManager.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(PROGRESS_UPDATE)) {

                boolean downloadComplete = intent.getBooleanExtra("downloadComplete", false);
                //Log.d("API123", download.getProgress() + " current progress");

                if (downloadComplete) {

                    Toast.makeText(getApplicationContext(), "File download completed", Toast.LENGTH_SHORT).show();

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +
                            "journaldev-image-downloaded.jpg");

                    Glide.with(context).load(file).into(imageView);

                }
            }
        }
    };

}
