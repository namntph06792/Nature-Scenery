package com.fox.assignment403.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fox.assignment403.PreviewPhotoActivity;
import com.fox.assignment403.R;
import com.fox.assignment403.model.Photo;

import java.util.List;

public class StaggeredRecycleViewAdapter extends RecyclerView.Adapter<StaggeredRecycleViewAdapter.ViewHolder> {

    private static final String TAG = "StaggeredRecycleViewAd";

    private List<Photo> mImageUrls;
    private Context mContext;

    private ConstraintSet constraintSet = new ConstraintSet();

    public ConstraintSet getConstraintSet() {
        return constraintSet;
    }

    public StaggeredRecycleViewAdapter(Context mContext, List<Photo> mImageUrls) {
        this.mImageUrls = mImageUrls;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_photo,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Photo photo = mImageUrls.get(position);
        Log.d(TAG,"onBindViewHolder : called.");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.dummy);
        Glide.with(mContext)
                .load(photo.getUrlO() != null ? photo.getUrlO() : photo.getUrlL())
                .error(R.drawable.dummy)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG,"onClick: clicked on:  " + mImageUrls.get(position));
                //Toast.makeText(mContext,photo.getUrlO() != null ? photo.getUrlO() : photo.getUrlL(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, PreviewPhotoActivity.class);
                intent.putExtra("photo",photo);
                mContext.startActivity(intent);
            }
        });

        String ratio = String.format("%d:%d",photo.getUrlO() != null ? Integer.parseInt(photo.getWidthO()) : Integer.parseInt(photo.getWidthL()),photo.getUrlO() != null ? Integer.parseInt(photo.getHeightO()) : Integer.parseInt(photo.getHeightL()));
        constraintSet.clone(holder.constraintLayout);
        constraintSet.setDimensionRatio(holder.imageView.getId(),ratio);
        constraintSet.applyTo(holder.constraintLayout);
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public void onClear(){
        mImageUrls.clear();
        notifyDataSetChanged();
    }

    public void onUpdate(Photo list){
        mImageUrls.add(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout constraintLayout;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.constraintLayout = itemView.findViewById(R.id.constrainContainer);
            this.imageView = itemView.findViewById(R.id.imageView_widget);
        }
    }

}
