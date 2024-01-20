package com.example.meetmi.customAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetmi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Map.Entry<String, String>> commentsList;



        public CommentAdapter(Map<String, String> commentsMap) {
            if (commentsMap != null) {
                this.commentsList = new ArrayList<>(commentsMap.entrySet());
            } else {
                this.commentsList = new ArrayList<>(); // Use an empty list if the map is null
            }
        }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Map.Entry<String, String> commentEntry = commentsList.get(position);
        String commenter = commentEntry.getKey(); // Get the key representing the commenter's nickname
        String comment = commentEntry.getValue(); // Get the value representing the comment text
        holder.tvComment.setText(commenter + ": " + comment); // Display both in the TextView
    }


    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
