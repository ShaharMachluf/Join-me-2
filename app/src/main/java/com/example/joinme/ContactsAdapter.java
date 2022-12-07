package com.example.joinme;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>{
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    ArrayList<Contact> contacts;
//    private RecyclerViewClickListener listener;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts, RecycleViewInterface recycleViewInterface){
        this.context = context;
        this.contacts = contacts;
        this.recycleViewInterface = recycleViewInterface;
//        this.listener = listener;
    }
    @NonNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_row, parent, false);
        return new ContactsAdapter.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.MyViewHolder holder, int position) {
        holder.tvCategory.setText(contacts.get(position).getCategory());
        holder.tvLocation.setText(contacts.get(position).getLocation());
        holder.tvDate.setText(contacts.get(position).getDate());
        holder.tvId.setText(contacts.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategory, tvLocation, tvDate, tvId;
        public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.categoryTxt);
            tvLocation = itemView.findViewById(R.id.locationTxt);
            tvDate = itemView.findViewById(R.id.dateTxt);
            tvId = itemView.findViewById(R.id.groupIdTxt);

            itemView.findViewById(R.id.detailsBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recycleViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recycleViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
//            itemView.setOnClickListener(this);
//            itemView.findViewById(R.id.detailsBtn).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startActivity(new Intent(context, GroupDetailsActivity.class));
//                }
//            });
        }

//        @Override
//        public void onClick(View view) {
//            listener.onClick(view , getAdapterPosition());
//        }
//    }
//
//    public interface RecyclerViewClickListener{
//        void onClick(View v, int position);
    }
}
