package com.example.meetmi.customAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meetmi.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ModelClass.Posts;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends BaseAdapter {

    private List<Posts> posts; // Changed from List<Users> to List<Posts>
    private LayoutInflater inflater;
    private Context context;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public PostAdapter(Context context, List<Posts> posts) {
        this.posts = posts; // Changed from users to posts
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return posts.size(); // Changed from users.size() to posts.size()
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position); // Changed from users.get(position) to posts.get(position)
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_noti, parent, false);
            holder = new ViewHolder();

            holder.avatar = convertView.findViewById(R.id.userAvatar_post);
            holder.userImage_post = convertView.findViewById(R.id.userImage_post);
            holder.nickName = convertView.findViewById(R.id.username_post);
            holder.caption = convertView.findViewById(R.id.caption_post);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Posts post = posts.get(position); // Get the post at the current position
        holder.nickName.setText(post.getNickname()); // Assuming Posts class has getNickname method
        Glide.with(context).load(post.getAvatar()).into(holder.avatar); // Assuming Posts class has getAvatar method
        holder.caption.setText(post.getCaption()); // Set the caption of the post
        Glide.with(context).load(post.getPhoto()).into(holder.userImage_post); // Assuming Posts class has getImageUrl method

        return convertView;
    }

    static class ViewHolder {
        CircleImageView avatar;
        ImageView userImage_post;
        TextView nickName, caption;
    }

    private void showUserNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("User Not Found");
        builder.setMessage("The user you are looking for does not exist.");
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
