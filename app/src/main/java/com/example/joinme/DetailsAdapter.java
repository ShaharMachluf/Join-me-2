package com.example.joinme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    ArrayList<DetailsForRecycleHistory> details;

    public DetailsAdapter(Context context, ArrayList<DetailsForRecycleHistory> details, RecycleViewInterface rvi){
        this.context = context;
        this.details = details;
        this.recycleViewInterface = rvi;
    }
    @NonNull
    @Override
    public DetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout (Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_row_history_created, parent, false);
        return new DetailsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.MyViewHolder holder, int position) {
        // assigning values to the views we created in the rcycle_view layout file
        // base on the position of the rcycle view
        holder.tvCategory.setText(details.get(position).getCategory());
        holder.tvLocation.setText(details.get(position).getLocation());
        holder.tvDate.setText(details.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        // num of items to display
        return details.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvCategory, tvLocation, tvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.categoryTxt);
            tvLocation = itemView.findViewById(R.id.locationTxt);
            tvDate = itemView.findViewById(R.id.dateTxt);
        }
    }
}
