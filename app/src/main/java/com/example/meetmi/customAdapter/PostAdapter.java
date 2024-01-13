package com.example.meetmi.customAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meetmi.R;
import com.example.meetmi.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends BaseAdapter {


    private List<Users> users;
    private LayoutInflater inflater;
    private Context context;
    private DatabaseReference mDatabase =  FirebaseDatabase.getInstance().getReference() ;

    public PostAdapter(Context context, List<Users> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
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


            //will the textview and imageview being edited below overwritten?
            holder.imageView = convertView.findViewById(R.id.userAvatar_post);
            //TODO: solve the problem below that has been commented out
            //holder.imageView = convertView.findViewById(R.id.userImage_post);
            holder.textView = convertView.findViewById(R.id.username_post);
            holder.textView = convertView.findViewById(R.id.caption_post);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserManager.getCurrentUserDetail(new UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    // Now that we have the user, we can create and post
                    String nickname = user.getNickname();
                    String avatar = user.getAvatar(); // Assuming getAvatar() method exists

                    // Creating post object
                    Posts post = new Posts(nickname, avatar, photo, video, caption, dateTime, comments, reaction);

                    // Saving to Firebase
                    mDatabase.child("posts").push().setValue(post);
                } else {

                }
            }
        });
        Users user = users.get(position);
        holder.textView.setText(user.getNickname());
        Glide.with(context).load(user.getAvatar()).into(holder.imageView);


        //TODO: have a post class so that we can call the post and get the information here
//        holder.textView.setText(post.getCaption());
//        holder.textView.setText(post.getImage());


        return convertView;
    }

    static class ViewHolder {
        CircleImageView imageView;
        TextView textView;
    }
}
