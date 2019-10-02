package com.fox.assignment403;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
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
import static com.fox.assignment403.constant.Constants.NUM_COLUMNS;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private StaggeredRecycleViewAdapter staggeredRecyclerViewAdapter;

    private List<Photo> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initViews();
        fetchPhotoAsync(0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPhotoAsync(1);
            }
        });
    }

    private void fetchPhotoAsync(int i) {
        //0 : Fetch new data
        //1: Refresh and load new data
        if(i == 0){
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.GET, FAVOURITE_PHOTO_API, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    FavoritePhoto favoritePhoto = new Gson().fromJson(response,FavoritePhoto.class);
                    Log.d("Response",favoritePhoto.getPhotos().getPhoto() + "");
                    for(int i = 0;i < favoritePhoto.getPhotos().getPhoto().size();i++){
                        mImageUrls.add(favoritePhoto.getPhotos().getPhoto().get(i));
                        Log.d("aa",favoritePhoto.getPhotos().getPhoto().get(i) + "");
                    }
                    initRecyclerView();
                    progressDialog.dismiss();

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Something wrong happened !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
            queue.add(request);
            initProgressDialog();
        }else {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.GET, FAVOURITE_PHOTO_API, new com.android.volley.Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    FavoritePhoto favoritePhoto = new Gson().fromJson(response,FavoritePhoto.class);
                    Log.d("Response",favoritePhoto.getPhotos().getPhoto() + "");
                    staggeredRecyclerViewAdapter.onClear();
                    for(int i = 0;i < favoritePhoto.getPhotos().getPhoto().size();i++){
                        staggeredRecyclerViewAdapter.onUpdate(favoritePhoto.getPhotos().getPhoto().get(i));
                    }
                    initRecyclerView();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Something wrong happened !", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
        }
    }

    private void initViews(){
        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        recyclerView = findViewById(R.id.recycleView);
        staggeredRecyclerViewAdapter = new StaggeredRecycleViewAdapter(MainActivity.this, mImageUrls);
    }

    private void initRecyclerView(){
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Waiting");
        progressDialog.setMessage("Loading image from network ...");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        //progressDialog.setMax(100);
        //progressDialog.setProgress(0);
        progressDialog.show();
    }

}
