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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends BaseAdapter {
    private TextView Nickname, Notification;

    private List<Users> users;
    private LayoutInflater inflater;
    private Context context;

    public NotificationAdapter(Context context, List<Users> users) {
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
            holder.imageView = convertView.findViewById(R.id.userAvatar_notification);
            //will the textview being edited below overwritten?
            holder.textView = convertView.findViewById(R.id.userName_notification);
            holder.textView = convertView.findViewById(R.id.userNotification);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Users user = users.get(position);
        holder.textView.setText(user.getNickname());
        // Load image using Glide or Picasso
        Glide.with(context).load(user.getAvatar()).into(holder.imageView);

        //TODO: have a get notification method
        //holder.textView.setText(user.getNotification());

        return convertView;
    }

    static class ViewHolder {
        CircleImageView imageView;
        TextView textView;
    }
}
