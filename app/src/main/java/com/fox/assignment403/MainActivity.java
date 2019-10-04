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
import com.fox.assignment403.listener.EndlessRecyclerViewScrollListener;
import com.fox.assignment403.model.FavoritePhoto;
import com.fox.assignment403.model.Photo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.fox.assignment403.constant.Constants.FAVOURITE_PHOTO_API;
import static com.fox.assignment403.constant.Constants.FORMAT;
import static com.fox.assignment403.constant.Constants.NUM_COLUMNS;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private StaggeredRecycleViewAdapter staggeredRecyclerViewAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String url;
    private int per_page = 20;
    private int current_page = 1;


    private List<Photo> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initViews();
        //Init url to load image
        url = FAVOURITE_PHOTO_API + "&per_page=" + per_page + "&page=" + current_page + FORMAT;
        fetchPhotoFromApi(0);
        EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchNextPhotoFromApi(page);
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPhotoFromApi(1);
            }
        });
    }

    private void fetchNextPhotoFromApi(int page) {
        url = FAVOURITE_PHOTO_API + "&per_page=" + per_page + "&page=" + page + FORMAT;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
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
    }

    private void fetchPhotoFromApi(int i) {
        //0 : Fetch new data
        //1: Refresh and load new data
        if(i == 0){
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    FavoritePhoto favoritePhoto = new Gson().fromJson(response,FavoritePhoto.class);
                    Log.d("Response",favoritePhoto.getPhotos().getPhoto() + "");
                    for(int i = 0;i < favoritePhoto.getPhotos().getPhoto().size();i++){
                        mImageUrls.add(favoritePhoto.getPhotos().getPhoto().get(i));
                        Log.d("aa",favoritePhoto.getPhotos().getPhoto().get(i) + "");
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
            initProgressDialog();
        }else {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {

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
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, 1);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Waiting");
        progressDialog.setMessage("Loading image from network ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        //progressDialog.setMax(100);
        //progressDialog.setProgress(0);
        progressDialog.show();
    }

}
