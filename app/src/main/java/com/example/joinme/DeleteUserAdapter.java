package com.example.joinme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeleteUserAdapter extends RecyclerView.Adapter<DeleteUserAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserRow> userRow;

    public DeleteUserAdapter(Context context, ArrayList<UserRow> userRow){
        this.context = context;
        this.userRow = userRow;
    }

    public void setFilteredList(ArrayList<UserRow> filteredList){
        this.userRow = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeleteUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where you inflate the layout (giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.users_recycle_view_row, parent, false);

        return new DeleteUserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteUserAdapter.MyViewHolder holder, int position) {
        //assigning values to the views we created in users_recycle_view_row layout file
        //based on the position of the recycler view

        holder.tvName.setText(userRow.get(position).getName());
        holder.tvMail.setText(userRow.get(position).getMail());
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items we want to display
        return userRow.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing the views from our users_recycle_view_row layout file
        //kinda like in the onCreate method

        TextView tvName, tvMail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.nameTxt);
            tvMail = itemView.findViewById(R.id.mailTxt);
        }
    }
}
