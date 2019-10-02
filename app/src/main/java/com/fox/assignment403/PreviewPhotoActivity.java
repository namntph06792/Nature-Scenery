package com.fox.assignment403;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fox.assignment403.model.Photo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class PreviewPhotoActivity extends AppCompatActivity {

    private Photo photo;
    private String url_m, url_z, url_c, url_l, url_o;
    private FloatingActionsMenu menuMultipleActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);
        initViews();
        Intent intent = getIntent();
        if(intent != null){
            photo = (Photo) intent.getSerializableExtra("photo");
        }
        //Add button download <m,z,c,l,o>
        getPhotoUrl();
        if(url_m != null){
            FloatingActionButton d_m = new FloatingActionButton(PreviewPhotoActivity.this);
            d_m.setTitle(photo.getWidthM() + " x " + photo.getHeightM());
            d_m.setIcon(R.drawable.ic_file_download_black_24dp);
            d_m.setColorNormalResId(R.color.pink);
            d_m.setColorPressedResId(R.color.pink_pressed);
            d_m.setStrokeVisible(false);
            d_m.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            menuMultipleActions.addButton(d_m);
        }

        if(url_z != null){
            FloatingActionButton d_z = new FloatingActionButton(PreviewPhotoActivity.this);
            d_z.setTitle(photo.getWidthZ() + " x " + photo.getHeightZ());
            d_z.setIcon(R.drawable.ic_file_download_black_24dp);
            d_z.setColorNormalResId(R.color.pink);
            d_z.setColorPressedResId(R.color.pink_pressed);
            d_z.setStrokeVisible(false);
            d_z.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            menuMultipleActions.addButton(d_z);
        }

        if(url_c != null){
            FloatingActionButton d_c = new FloatingActionButton(PreviewPhotoActivity.this);
            d_c.setTitle(photo.getWidthC() + " x " + photo.getHeightC());
            d_c.setIcon(R.drawable.ic_file_download_black_24dp);
            d_c.setColorNormalResId(R.color.pink);
            d_c.setColorPressedResId(R.color.pink_pressed);
            d_c.setStrokeVisible(false);
            d_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            menuMultipleActions.addButton(d_c);
        }

        if(url_l != null){
            FloatingActionButton d_l = new FloatingActionButton(PreviewPhotoActivity.this);
            d_l.setTitle(photo.getWidthL() + " x " + photo.getHeightL());
            d_l.setIcon(R.drawable.ic_file_download_black_24dp);
            d_l.setColorNormalResId(R.color.pink);
            d_l.setColorPressedResId(R.color.pink_pressed);
            d_l.setStrokeVisible(false);
            d_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            menuMultipleActions.addButton(d_l);
        }

        if(url_o != null){
            FloatingActionButton d_o = new FloatingActionButton(PreviewPhotoActivity.this);
            d_o.setTitle(photo.getWidthO() + " x " + photo.getHeightO());
            d_o.setIcon(R.drawable.ic_file_download_black_24dp);
            d_o.setColorNormalResId(R.color.pink);
            d_o.setColorPressedResId(R.color.pink_pressed);
            d_o.setStrokeVisible(false);
            d_o.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            menuMultipleActions.addButton(d_o);
        }


    }



    private void initViews(){
        menuMultipleActions = findViewById(R.id.multiple_actions);
    }

    private void getPhotoUrl(){
        url_m = photo.getUrlM();
        url_z = photo.getUrlZ();
        url_c = photo.getUrlC();
        url_l = photo.getUrlL();
        url_o = photo.getUrlO();
    }
}
