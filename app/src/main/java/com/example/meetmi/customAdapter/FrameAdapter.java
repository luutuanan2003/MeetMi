package com.example.meetmi.customAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.meetmi.R;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private final int FRAME_TYPE_1 = 0;
    private final int FRAME_TYPE_2 = 1;
    private final int FRAME_TYPE_3 = 2;


    public FrameAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return FRAME_TYPE_1;
            case 1:
                return FRAME_TYPE_2;
            case 2:
                return FRAME_TYPE_3;
            // Add more cases as needed for additional frame types
        }
        return position;
    }


    @Override
    public FrameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case FRAME_TYPE_1:
                view = inflater.inflate(R.layout.frame1, parent, false);
                break;
            case FRAME_TYPE_2:
                view = inflater.inflate(R.layout.frame2, parent, false);
                break;
            case FRAME_TYPE_3:
                view = inflater.inflate(R.layout.frame3, parent, false);
                break;
            // Add more cases as needed for additional frame types
        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(FrameAdapter.ViewHolder holder, int position) {
        // Bind data to the view based on the item type
    }

    @Override
    public int getItemCount() {
        return 3; // Adjust this number based on your data
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Define views in the list item

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize views
        }
    }
}
