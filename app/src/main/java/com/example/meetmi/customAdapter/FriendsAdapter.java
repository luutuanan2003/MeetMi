package com.example.meetmi.customAdapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.meetmi.R;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {
    private List<String> friendsList;

    public FriendsAdapter(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        String friendUsername = friendsList.get(position);
        holder.friendNameTextView.setText(friendUsername);
        // You can add more logic here, such as an onClickListener for each friend item
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView friendNameTextView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            friendNameTextView = itemView.findViewById(R.id.friend_name);
        }
    }
}
