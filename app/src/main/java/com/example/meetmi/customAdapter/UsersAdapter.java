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
import java.util.Map;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<Map.Entry<String, String>> userList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    private Context context;


    public UsersAdapter(List<Map.Entry<String, String>> userList, Context context, OnItemClickListener listener) {
        this.userList = userList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(itemView, listener); // Passing the listener as the second argument
    }


    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users currentUser = userList.get(position);
        holder.bindTo(currentUser);
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
        private TextView nicknameTextView;
        private TextView emailTextView;
        private Button addButton;

        public UserViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            nicknameTextView = itemView.findViewById(R.id.nickname_text_view);
            emailTextView = itemView.findViewById(R.id.email_text_view);
            addButton = itemView.findViewById(R.id.add_friend_button);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAddClicked(userList.get(position));
                    }
                }
            });
        }

        void bindTo(Users currentUser) {
            nicknameTextView.setText(currentUser.getNickname());
            emailTextView.setText(currentUser.getEmail());
        }
    }
    public interface OnAddFriendListener {
        void onAddFriend(Users user);
    }
    public interface OnItemClickListener {
        void onAddClicked(Users user);
    }


}
