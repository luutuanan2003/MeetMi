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

        if (noti.getIsComment().toString().equals("1")) {
            holder.notitype.setText("just commented on your post");
        } else {
            holder.notitype.setText("just liked on your post");
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



