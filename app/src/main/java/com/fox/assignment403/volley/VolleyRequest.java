package com.fox.assignment403.volley;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fox.assignment403.constant.Constants;
import com.fox.assignment403.model.FavoritePhoto;
import com.google.gson.Gson;


public class VolleyRequest {
//    private Context mContext;
//    private Constants constants = new Constants(API_KEY, USER_ID);
//
//    public VolleyRequest(Context mContext) {
//        this.mContext = mContext;
//    }
//
//    RequestQueue queue = Volley.newRequestQueue(mContext);
//    StringRequest request = new StringRequest(Request.Method.GET, constants.FAVOURITE_PHOTO_API, new com.android.volley.Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//            FavoritePhoto favoritePhoto = new Gson().fromJson(response,FavoritePhoto.class);
//            Log.d("Response",response);
//            Toast.makeText(mContext, favoritePhoto.getPhotos().getPhoto() + "", Toast.LENGTH_SHORT).show();
//
//        }
//    }, new com.android.volley.Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Toast.makeText(mContext, "Something wrong happened !", Toast.LENGTH_SHORT).show();
//        }
//    }) {
//        @Override
//        protected Map<String, String> getParams()
//        {
//            Map<String, String>  params = new HashMap<>();
//            params.put("username", username);
//            params.put("password", password);
//
//            return params;
//        }
//    };
//    queue.add(request);
}
