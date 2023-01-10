package com.example.joinme.ViewModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinme.R;
import com.example.joinme.Model.UserRow;

import java.util.List;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.MyViewHolder> {
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    List<UserRow> userRows;

    public ParticipantsAdapter(Context context, List<UserRow> userRows, RecycleViewInterface rvi){
        // Pass in the contact array into the constructor
        this.context = context;
        this.userRows = userRows;
        recycleViewInterface = rvi;
    }

    public void setFilteredList(List<UserRow> filteredList){
        this.userRows = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParticipantsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where you inflate the layout (giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        //inflate a new view hierarchy from the specified xml resource. Throws
        View view = inflater.inflate(R.layout.participants_recycle_view_row, parent, false);

        return new ParticipantsAdapter.MyViewHolder(view, recycleViewInterface, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantsAdapter.MyViewHolder holder, int position) {
        //assigning values to the views we created in users_recycle_view_row layout file
        //based on the position of the recycler view
        holder.tvName.setText(userRows.get(position).getName());
        holder.tvMail.setText(userRows.get(position).getMail());
        holder.tvPhone.setText(userRows.get(position).getPhone());
        Log.d("SET TEXT", userRows.get(position).getName());
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items we want to display
        return userRows.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing the views from our users_recycle_view_row layout file
        //kinda like in the onCreate method

        TextView tvName, tvMail, tvPhone;

        public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface, Context context) {
            /**
             * We also create a constructor that accepts the entire item row and does the view lookups to find each subview
             */

            // stores the itemView in a public final member variable that can be used to access the context from any ViewHolder instance.
            super(itemView);
            //  entering the name and email of the user who needs to be blocked
            tvName = itemView.findViewById(R.id.nameTxt);
            tvMail = itemView.findViewById(R.id.mailTxt);
            tvPhone = itemView.findViewById(R.id.phoneTxt);
            itemView.findViewById(R.id.reportBtn).setOnClickListener(new View.OnClickListener() {
                //"Block User" button
                @Override
                public void onClick(View view) {
                    if(recycleViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recycleViewInterface.onReportClick(pos);
                        }
                    }
                }
            });
        }

    }
}
