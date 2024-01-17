package com.example.meetmi.customAdapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.meetmi.R;
import java.util.List;
import ModelClass.Posts;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostAdapter.FeedPostViewHolder> {

    private Context context;
    private List<Posts> postsList;
    private OnPostInteractionListener listener;


    public interface OnPostInteractionListener {
        void onCommentClick(int position);
        void onReactionClick (int position);
    }

    public FeedPostAdapter(Context context, List<Posts> postsList, OnPostInteractionListener listener) {
        this.context = context;
        this.postsList = postsList;
        this.listener = listener;
    }

    @Override
    public FeedPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_post, parent, false);
        return new FeedPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedPostViewHolder holder, int position) {
        Posts post = postsList.get(position);
        holder.captionTextView.setText(post.getCaption());
        holder.nicknameTextView.setText(post.getNickname());
        holder.dateTimeTextView.setText(post.getDateTime());

        // Load image using Picasso
        Picasso.get().load(post.getAvatar()).into(holder.avatarImageView);

        List<String> imageUris = post.getPhoto(); // Assuming Posts class has a method to get photo URIs
        GalleryAdapter2 galleryAdapter = new GalleryAdapter2(context, imageUris);
        holder.galleryRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.galleryRecyclerView.setAdapter(galleryAdapter);
        //set up comment
        holder.commentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCommentClick(holder.getAdapterPosition());
                }
            }
        });

        holder.reactionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCommentClick(holder.getAdapterPosition());
                }
            }
        });
        Log.d("photofromfirebase", "photo" + post.getPhoto());
    }


    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public static class FeedPostViewHolder extends RecyclerView.ViewHolder {
        TextView captionTextView, nicknameTextView, dateTimeTextView;
        ShapeableImageView avatarImageView;
        RecyclerView galleryRecyclerView;
        ImageView commentImageView,reactionImageView;

        public FeedPostViewHolder(View itemView) {
            super(itemView);
            captionTextView = itemView.findViewById(R.id.caption_post);
            nicknameTextView = itemView.findViewById(R.id.username_post);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            avatarImageView = itemView.findViewById(R.id.userAvatar_post);
            commentImageView = itemView.findViewById(R.id.commentP);
            reactionImageView = itemView.findViewById(R.id.reactionB);
            galleryRecyclerView = itemView.findViewById(R.id.galleryRecyclerView);

        }
    }
}