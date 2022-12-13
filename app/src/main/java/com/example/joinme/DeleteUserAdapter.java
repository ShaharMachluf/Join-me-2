package com.example.joinme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeleteUserAdapter extends RecyclerView.Adapter<DeleteUserAdapter.MyViewHolder> {
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    List<UserRow> userRows;

    public DeleteUserAdapter(Context context, List<UserRow> userRows, RecycleViewInterface rvi){
        this.context = context;
        this.userRows = userRows;
        recycleViewInterface = rvi;
    }

    public void setFilteredList(List<UserRow> filteredList){
        this.userRows = filteredList;
        notifyDataSetChanged();
//        int old_size = getItemCount();
//        this.userRows.clear();
//        this.userRows.addAll(filteredList);
//        notifyItemRangeRemoved(0, old_size);
//        notifyItemRangeInserted(0, getItemCount());

    }

    @NonNull
    @Override
    public DeleteUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where you inflate the layout (giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.users_recycle_view_row, parent, false);

        return new DeleteUserAdapter.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteUserAdapter.MyViewHolder holder, int position) {
        //assigning values to the views we created in users_recycle_view_row layout file
        //based on the position of the recycler view

        holder.tvName.setText(userRows.get(position).getName());
        holder.tvMail.setText(userRows.get(position).getMail());
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

        TextView tvName, tvMail;

        public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);

            tvName = itemView.findViewById(R.id.nameTxt);
            tvMail = itemView.findViewById(R.id.mailTxt);
            itemView.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recycleViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recycleViewInterface.onDeleteClick(pos);
                        }
                    }
                }
            });
        }

    }
}
