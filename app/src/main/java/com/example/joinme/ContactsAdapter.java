package com.example.joinme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>{
    Context context;
    ArrayList<Contact> contacts;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts){
        this.context = context;
        this.contacts = contacts;
    }
    @NonNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_row, parent, false);
        return new ContactsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.MyViewHolder holder, int position) {
        holder.tvCategory.setText(contacts.get(position).getCategory());
        holder.tvLocation.setText(contacts.get(position).getLocation());
        holder.tvDate.setText(contacts.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
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
