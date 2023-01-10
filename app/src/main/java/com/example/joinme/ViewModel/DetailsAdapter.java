package com.example.joinme.ViewModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinme.R;
import com.example.joinme.Model.DetailsForRecycleHistory;
import com.example.joinme.Model.Logic;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder>{
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    List<DetailsForRecycleHistory> details;


    public DetailsAdapter(Context context, List<DetailsForRecycleHistory> details, RecycleViewInterface rvi){
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
        return new DetailsAdapter.MyViewHolder(view, recycleViewInterface, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.MyViewHolder holder, int position) {
        // assigning values to the views we created in the rcycle_view layout file
        // base on the position of the rcycle view
        holder.tvCategory.setText(details.get(position).getCategory());
        holder.tvLocation.setText(details.get(position).getLocation());
        holder.tvDate.setText(details.get(position).getDate());
        holder.sw.setChecked(details.get(position).is_happened());
        Log.d("ID ", details.get(position).getId());
    }

    @Override
    public int getItemCount() {
        // num of items to display
        return details.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        Logic logic = new Logic();
        TextView tvCategory, tvLocation, tvDate;
        Switch sw;
        Context context;

        public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface, Context context) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.categoryTxt);
            tvLocation = itemView.findViewById(R.id.locationTxt);
            tvDate = itemView.findViewById(R.id.dateTxt);
            sw = (Switch) itemView.findViewById(R.id.happenedSwich);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String [] today = new String[3];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        today = java.time.LocalDate.now().toString().split("-");
                    }
                    String date = tvDate.getText().toString();
                    String[] groupDate = date.split("/");
                    if(logic.checkDate(Integer.parseInt(today[0]), Integer.parseInt(today[1]),
                            Integer.parseInt(today[2]), Integer.parseInt(groupDate[2]),
                            Integer.parseInt(groupDate[1]), Integer.parseInt(groupDate[0]))){
                        Log.d("here", "date check");
                        sw.setChecked(false);
                        Toast.makeText(context, "wait until group date pass", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(recycleViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recycleViewInterface.onHappenedClick(pos, true);

                        }
                    }
                }else {
                    if(recycleViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recycleViewInterface.onHappenedClick(pos, false);
                        }
                    }
                    }

                }
            });
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
