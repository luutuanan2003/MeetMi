package com.example.meetmi.customAdapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.meetmi.R;

import java.util.List;


import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


//public class GalleryAdapter extends BaseAdapter {
//    private Context context;
//    private List<Uri> imageUrls;
//
//    public GalleryAdapter(Context context, List<Uri> imageUrls) {
//        this.context = context;
//        this.imageUrls = imageUrls;
//    }
//
//    @Override
//    public int getCount() {
//        return imageUrls.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return imageUrls.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
//        }
//
//        ImageView imageView = convertView.findViewById(R.id.imageView);
//        Uri imageUri = imageUrls.get(position);
//
//        Glide.with(context)
//                .load(imageUri)
//                .fitCenter() // Optional if scaleType is set in XML
//                .into(imageView);
//
//        return convertView;
//    }
//
//}

// old code for the recycler view
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private Context context;
    private List<Uri> imageUrls;

    public GalleryAdapter(Context context, List<Uri> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(imageUrls.get(position))
                .fitCenter()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
