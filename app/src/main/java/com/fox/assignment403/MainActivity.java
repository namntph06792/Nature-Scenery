package com.fox.assignment403;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fox.assignment403.adapter.StaggeredRecycleViewAdapter;
import com.fox.assignment403.model.FavoritePhoto;
import com.fox.assignment403.model.Photo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.fox.assignment403.constant.Constants.FAVOURITE_PHOTO_API;


public class MainActivity extends AppCompatActivity {

    private static final int NUM_COLUMNS = 2;

    private List<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageBitmaps();
    }

    private void initImageBitmaps(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, FAVOURITE_PHOTO_API, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                FavoritePhoto favoritePhoto = new Gson().fromJson(response,FavoritePhoto.class);
                //Log.d("Response",response);
                //Toast.makeText(MainActivity.this, favoritePhoto.getPhotos().getPhoto() + "", Toast.LENGTH_SHORT).show();
                Log.d("Response",favoritePhoto.getPhotos().getPhoto() + "");
                for(int i = 0;i < favoritePhoto.getPhotos().getPhoto().size();i++){
                    mImageUrls.add(favoritePhoto.getPhotos().getPhoto().get(i).getUrlM());
                    Log.d("aa",favoritePhoto.getPhotos().getPhoto().get(i).getUrlM() + "");
                }
                initRecyclerView();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something wrong happened !", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        StaggeredRecycleViewAdapter staggeredRecyclerViewAdapter = new StaggeredRecycleViewAdapter(this, mImageUrls);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }

}
