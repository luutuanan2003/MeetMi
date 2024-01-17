package com.example.meetmi.customAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.meetmi.R;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private LayoutInflater inflater;
    public final int FRAME_TYPE_0 = 0;
    public final int FRAME_TYPE_1 = 1;
    public final int FRAME_TYPE_2 = 2;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public FrameAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return FRAME_TYPE_0;
            case 1:
                return FRAME_TYPE_1;
            case 2:
                return FRAME_TYPE_2;
            // Add more cases as needed for additional frame types
        }
        return position;
    }


    @Override
    public FrameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case FRAME_TYPE_0:
                view = inflater.inflate(R.layout.list_item_post, parent, false);
                break;
            case FRAME_TYPE_1:
                view = inflater.inflate(R.layout.list_item_post1, parent, false);
                break;
            case FRAME_TYPE_2:
                view = inflater.inflate(R.layout.list_item_post2, parent, false);
                break;
            // Add more cases as needed for additional frame types
        }
        return new ViewHolder(view, listener);
    }






    @Override
    public void onBindViewHolder(FrameAdapter.ViewHolder holder, int position) {
        // Bind data to the view based on the item type
    }

    @Override
    public int getItemCount() {
        return 3; // Adjust this number based on your data
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define views in the list item

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            // Initialize views
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
