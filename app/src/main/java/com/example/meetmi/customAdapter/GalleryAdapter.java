package com.example.meetmi.customAdapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GalleryAdapter extends BaseAdapter {
    private Context context;
    private List<Uri> imageUrls; // Assuming image URLs. Change to int[] if you have resource IDs.

    public GalleryAdapter(Context context, List<Uri> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 200)); // Set your dimensions
        } else {
            imageView = (ImageView) convertView;
        }
        Glide.with(context)
                .load(imageUrls.get(position))
                .into(imageView);

        return imageView;
    }
}

