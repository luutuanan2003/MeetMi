package com.example.meetmi.customAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meetmi.R;

import java.util.List;

import ModelClass.Notification;
import de.hdodenhof.circleimageview.CircleImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {
    private List<Notification> notificationLists;
    private LayoutInflater inflater;
    private Context context;

    public NotificationAdapter(Context context, List<Notification> notificationLists) {
        this.notificationLists = notificationLists;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return notificationLists.size();
    }

    @Override
    public NotiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_noti, parent, false);
        return new NotiViewHolder(view);
    }


    @Override
    public void onBindViewHolder(NotiViewHolder holder, int position) {
        Notification noti = notificationLists.get(position);

        if (noti.getIsComment() ==  "1") {
            holder.notitype.setText("Someone just commented on your post");
        } else {
            holder.notitype.setText("Someone just liked on your post");
        }
        holder.nickName.setText(noti.getFromUser());
        // Load image using Picasso
        Picasso.get().load(noti.getFromUserAvatar()).into(holder.avatar);

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class NotiViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView nickName, notitype;

        public NotiViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.userAvatar_notification);
            nickName = itemView.findViewById(R.id.userName_notification);
            notitype = itemView.findViewById(R.id.userNotification);

        }
    }

}


//public class NotificationAdapter extends BaseAdapter {
//    private TextView Nickname, Notification;
//
//    private List<Users> users;
//    private LayoutInflater inflater;
//    private Context context;
//
//    public NotificationAdapter(Context context, List<Users> users) {
//        this.users = users;
//        this.inflater = LayoutInflater.from(context);
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return users.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return users.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.list_item_noti, parent, false);
//            holder = new ViewHolder();
//            holder.imageView = convertView.findViewById(R.id.userAvatar_notification);
//            //will the textview being edited below overwritten?
//            holder.nickName = convertView.findViewById(R.id.userName_notification);
//            holder.notification = convertView.findViewById(R.id.userNotification);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        Users user = users.get(position);
//        holder.nickName.setText(user.getNickname());
//        //TODO: have a get notification method
//        //holder.notification.setText(user.getNotification());
//
//
//        // Load image using Glide or Picasso
//        Glide.with(context).load(user.getAvatar()).into(holder.imageView);
//        return convertView;
//    }
//
//    static class ViewHolder {
//        CircleImageView imageView;
//        TextView nickName, notification;
//    }
//}
