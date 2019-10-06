package com.fox.assignment403;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fox.assignment403.model.Photo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.IOException;

import static com.fox.assignment403.constant.Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

public class PreviewPhotoActivity extends AppCompatActivity {

    private Photo photo;
    private ImageView imageView;
    private FloatingActionsMenu menuMultipleActions;
    public static final String MESSAGE_PROGRESS  = "message_progress";
    private String [] link = new String[10];

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
                .load(photo.getUrlO() != null ? photo.getUrlO().trim() : photo.getUrlL().trim())
                .error(R.drawable.dummy)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().crossFade())
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
        link[0] = photo.getUrlO().trim();
        link[1] = photo.getUrlL().trim();
        link[2] = photo.getUrlC().trim();
        link[3] = photo.getUrlZ().trim();
        link[4] = photo.getUrlM().trim();
    }

    private void initDownloadButton(final String [] url){
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
                final int j = i;
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            checkPermission(url[j]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                menuMultipleActions.addButton(floatingActionButton);
            }
        }
    }

    private void downloadPhotoFromApi(String mUrl) throws IOException {
        File rootDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Flickr");
        if(!rootDirectory.exists()){
            rootDirectory.mkdir();
        }
        //String fileName = "image" + " " + System.currentTimeMillis();
        String fileName = URLUtil.guessFileName(mUrl, null, MimeTypeMap.getFileExtensionFromUrl(mUrl));
        //File file = new File(rootDirectory, fileName);
        //file.createNewFile();
        //Create download request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mUrl.trim()));
        //Allow type of network to download files
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("File Download"); //Set title in download notification
        request.setDescription("File is being downloaded..."); ////Set description in download notification
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName); //Get current timestamp as file name
        request.setVisibleInDownloadsUi(true);
        //Get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void checkPermission(String mUrl) throws IOException {
        if(Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(PreviewPhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(PreviewPhotoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(PreviewPhotoActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            } else {
                // Permission has already been granted
                downloadPhotoFromApi(mUrl);
            }
        }else{
            downloadPhotoFromApi(mUrl);
        }
    }

    //Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //downloadPhotoFromApi(mUrl);
                    Toast.makeText(this,"Permission granted ... !",Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"Permission denied ... !",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
