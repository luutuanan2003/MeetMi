package com.example.meetmi.customAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetmi.R;
import com.example.meetmi.Users;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<Users> userList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public UsersAdapter(Context context, List<Users> userList, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users currentUser = userList.get(position);
        holder.bindTo(currentUser);

        holder.addButton.setOnClickListener(v -> {
            String friendUsername = currentUser.getUsername(); // Use username
            if (listener != null) {
                listener.onAddFriendClicked(friendUsername);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateData(List<Users> newUserList) {
        userList = newUserList;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nicknameTextView;
        TextView emailTextView;
        Button addButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            nicknameTextView = itemView.findViewById(R.id.nickname_text_view);
            emailTextView = itemView.findViewById(R.id.email_text_view);
            addButton = itemView.findViewById(R.id.add_friend_button);
        }

        void bindTo(Users currentUser) {
            nicknameTextView.setText(currentUser.getNickname());
            emailTextView.setText(currentUser.getEmail());
        }
    }

    public interface OnItemClickListener {
        void onAddFriendClicked(String friendUsername);
    }

    
}