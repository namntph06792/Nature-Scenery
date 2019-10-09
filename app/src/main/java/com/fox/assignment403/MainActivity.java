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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.fox.assignment403.adapter.StaggeredRecycleViewAdapter;
import com.fox.assignment403.listener.EndlessRecyclerViewScrollListener;
import com.fox.assignment403.model.FavoritePhoto;
import com.fox.assignment403.model.Photo;

import java.util.ArrayList;
import java.util.List;

import static com.fox.assignment403.constant.Constants.API_KEY;
import static com.fox.assignment403.constant.Constants.FLICKR_DOMAIN;
import static com.fox.assignment403.constant.Constants.FORMAT;
import static com.fox.assignment403.constant.Constants.METHOD;
import static com.fox.assignment403.constant.Constants.NUM_COLUMNS;
import static com.fox.assignment403.constant.Constants.OPTION;
import static com.fox.assignment403.constant.Constants.USER_ID;

public class MainActivity extends AppCompatActivity {

    //Initial components
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private StaggeredRecycleViewAdapter staggeredRecyclerViewAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    //Initial value
    private int page = 1;


    private List<Photo> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initViews();
        initRecyclerView();
        fetchPhotoFromApi(page,0);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainActivity.this.page = 1;
                mImageUrls.clear();
                fetchPhotoFromApi(MainActivity.this.page,1);
                onScrollToLoadMore();            }
        });

        onScrollToLoadMore();
    }

    private void onScrollToLoadMore(){
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                MainActivity.this.page++;
                fetchPhotoFromApi(MainActivity.this.page++,1);
            }
        });
    }

    private void fetchPhotoFromApi(int page,int status){
        if(status == 0){
            initProgressDialog();
            AndroidNetworking.post(FLICKR_DOMAIN)
                    .addBodyParameter("method", METHOD)
                    .addBodyParameter("api_key", API_KEY)
                    .addBodyParameter("user_id", USER_ID)
                    .addBodyParameter("format", FORMAT)
                    .addBodyParameter("extras", OPTION)
                    .addBodyParameter("nojsoncallback", "1")
                    .addBodyParameter("per_page", "10")
                    .addBodyParameter("page", String.valueOf(page))
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(FavoritePhoto.class, new ParsedRequestListener<Object>() {

                        @Override
                        public void onResponse(Object response) {
                            Log.d("Ressponse",response+ "");
                            swipeRefreshLayout.setRefreshing(false);
                            FavoritePhoto favouritePhoto = (FavoritePhoto) response;
                            List<Photo> photos = favouritePhoto.getPhotos().getPhoto();
                            MainActivity.this.mImageUrls.addAll(photos);
                            staggeredRecyclerViewAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            Toast.makeText(MainActivity.this, anError.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }else{
            AndroidNetworking.post(FLICKR_DOMAIN)
                    .addBodyParameter("method", METHOD)
                    .addBodyParameter("api_key", API_KEY)
                    .addBodyParameter("user_id", USER_ID)
                    .addBodyParameter("format", FORMAT)
                    .addBodyParameter("extras", OPTION)
                    .addBodyParameter("nojsoncallback", "1")
                    .addBodyParameter("per_page", "10")
                    .addBodyParameter("page", String.valueOf(page))
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(FavoritePhoto.class, new ParsedRequestListener<Object>() {

                        @Override
                        public void onResponse(Object response) {
                            Log.d("Ressponse",response+ "");
                            swipeRefreshLayout.setRefreshing(false);
                            FavoritePhoto favouritePhoto = (FavoritePhoto) response;
                            List<Photo> photos = favouritePhoto.getPhotos().getPhoto();
                            MainActivity.this.mImageUrls.addAll(photos);
                            staggeredRecyclerViewAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            Toast.makeText(MainActivity.this, anError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void initViews(){
        recyclerView = findViewById(R.id.recycleView);
        swipeRefreshLayout = findViewById(R.id.swipeContainer);
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
