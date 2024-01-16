package com.example.meetmi.customAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


            holder.avatar = convertView.findViewById(R.id.userAvatar_post);
            holder.userImage_post = convertView.findViewById(R.id.userImage_post);
            holder.nickName = convertView.findViewById(R.id.username_post);
            holder.caption = convertView.findViewById(R.id.caption_post);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // what is this for?
        UserManager.getCurrentUserDetail(new UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    user = users.get(position);
                    holder.nickName.setText(user.getNickname());
                    Glide.with(context).load(user.getAvatar()).into(holder.avatar);
                    // for the photo of the post use the GalleryAdapter in the Activity class

                    //TODO: implement this method
                    //holder.caption.setText(post.getCaption);
                } else {
                    showUserNotFoundDialog();
                }
            }
        });



        //TODO: have a post class so that we can call the post and get the information here
//        holder.textView.setText(post.getCaption());
//        holder.textView.setText(post.getImage());


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
        builder.setPositiveButton("OK", (dialog, id) -> {
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
