package com.example.joinme.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinme.R;
import com.example.joinme.Model.DetailsForRecycleHistory;

import java.util.List;

public class JoinedDetailsAdapter extends RecyclerView.Adapter<JoinedDetailsAdapter.MyViewHolder>{
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    List<DetailsForRecycleHistory> details;

    public JoinedDetailsAdapter(Context context, List<DetailsForRecycleHistory> details, RecycleViewInterface rvi){
        this.context = context;
        this.details = details;
        this.recycleViewInterface = rvi;
    }



    @NonNull
    @Override
    public JoinedDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout (Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_row_history_joined, parent, false);
        return new JoinedDetailsAdapter.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedDetailsAdapter.MyViewHolder holder, int position) {
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

        public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.categoryTxt);
            tvLocation = itemView.findViewById(R.id.locationTxt);
            tvDate = itemView.findViewById(R.id.dateTxt);

            itemView.findViewById(R.id.participantsBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recycleViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos!= RecyclerView.NO_POSITION){
                            recycleViewInterface.onDetailsClick(pos);
                        }
                    }
                }
            });
        }
    }
}
