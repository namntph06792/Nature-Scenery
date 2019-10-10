package com.fox.assignment403.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fox.assignment403.R;
import com.fox.assignment403.model.Photo;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;


public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.ImageSliderAdapterViewHolder> {

    private Context context;
    private List<Photo> list;
    private String [] link = new String[10];

    public ImageSliderAdapter(Context context, List<Photo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ImageSliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.image_slider_layout_item, null);
        return new ImageSliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ImageSliderAdapterViewHolder holder, int i) {
        Photo photo = list.get(i);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.dummy);
        Glide.with(context)
                .load(photo.getUrlO() != null ? photo.getUrlO().trim() : photo.getUrlL().trim())
                .error(R.drawable.dummy)
                .apply(requestOptions)
                .transition(new DrawableTransitionOptions().crossFade())
                .skipMemoryCache(false)
                .into(holder.imageView);
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return list.size();
    }

    class ImageSliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {

        private View itemView;
        private ImageView imageView;
        private FloatingActionsMenu menuMultipleActions;

        public ImageSliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_widget);
            menuMultipleActions = itemView.findViewById(R.id.multiple_actions);

            //Pinch to zoom image (3rd lib)
            imageView.setOnTouchListener(new ImageMatrixTouchHandler(itemView.getContext()));
            this.itemView = itemView;
        }
    }
}
