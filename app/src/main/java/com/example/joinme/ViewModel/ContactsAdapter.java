//https://guides.codepath.com/android/using-the-recyclerview
package com.example.joinme.ViewModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinme.R;
import com.example.joinme.Model.Contact;

import java.util.ArrayList;

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>{
    /**
     * Create the basic adapter extending from RecyclerView.Adapter
     *  Note that we specify the custom ViewHolder which gives us access to our views
     *  Every adapter has three primary methods: onCreateViewHolder to inflate the item layout and create the holder,
     *                                           onBindViewHolder to set the view attributes based on the data
     *                                           and getItemCount to determine the number of items.
     */
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    ArrayList<Contact> contacts;
//    private RecyclerViewClickListener listener;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts, RecycleViewInterface recycleViewInterface){
        // Pass in the contact array into the constructor
        this.context = context;
        this.contacts = contacts;
        this.recycleViewInterface = recycleViewInterface;
    }
    @NonNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /**
          onCreateViewHolder(): RecyclerView calls this method whenever it needs to create a new ViewHolder.
         * The method creates and initializes the ViewHolder and its associated View, but does not fill in the view's contents—the
         * ViewHolder has not yet been bound to specific data.
         * @param parent –The viewgroup is the parent view that will contain your cell that you are about to create.
         *                So, the parent of the ViewGroup is the RecyclerView here (it will hold the cell ).
         * @param viewType –The viewType is useful if there are different types of cells in the list. For example, if there is a title cell and a detail cell.
         * @return: A new ViewHolder that holds a View of the given view type.
         */
        //creates a layout XML file into its corresponding view objects obtains the LayoutInflater from the given context.
        LayoutInflater inflater = LayoutInflater.from(context);
        //inflate a new view hierarchy from the specified xml resource. Throws InflateException if there is an error.
        View view = inflater.inflate(R.layout.recycle_view_row, parent, false);
        return new ContactsAdapter.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.MyViewHolder holder, int position) {
        /**
         *onBindViewHolder(): RecyclerView calls this method to associate a ViewHolder with data.
         *The method fetches the appropriate data and uses the data to fill in the view holder's layout. For example, if the RecyclerView displays a list of names,
         * the method might find the appropriate name in the list and fill in the view holder's TextView widget.
         * @param holder – The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
         * @param   position – The position of the item within the adapter's data set.
         */
        holder.tvCategory.setText(contacts.get(position).getCategory()); //sets the text to be displayed.
        holder.tvLocation.setText(contacts.get(position).getCity());
        holder.tvDate.setText(contacts.get(position).getDate());
        holder.tvId.setText(contacts.get(position).getId());
    }

    @Override
    public int getItemCount() {
        /**
         * Returns the total number of items in the data set held by the adapter.
         * @return The total number of items in this adapter.
         */
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        /**  Provide a direct reference to each of the views within a data item
         * Used to cache the views within the item layout for fast access
         */
        //TextView- a user interface element that displays text to the user.
        TextView tvCategory, tvLocation, tvDate, tvId;

        public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            /**
             * We also create a constructor that accepts the entire item row and does the view lookups to find each subview
             */

            // Stores the itemView in a public final member variable that can be used to access the context from any ViewHolder instance.
            super(itemView);

            //findViewById(...)finds the first descendant view with the given ID, the view itself if the ID matches getId(),
            //or null if the ID is invalid (< 0) or there is no matching view in the hierarchy.
            tvCategory = itemView.findViewById(R.id.categoryTxt); //Android R. java is an auto-generated file by aapt (Android Asset Packaging Tool) that contains resource IDs for all the resources of res/ directory.
            tvLocation = itemView.findViewById(R.id.locationTxt);
            tvDate = itemView.findViewById(R.id.dateTxt);
            tvId = itemView.findViewById(R.id.groupIdTxt);

            // Setup the click listener
            //"see details" button
            itemView.findViewById(R.id.detailsBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                // Handles the row being being clicked
                public void onClick(View view) {
                    // Triggers click upwards to the adapter on click
                    if(recycleViewInterface != null){
                        int pos = getAdapterPosition();          //Returns the Adapter position of the item represented by this ViewHolder.

                        if(pos != RecyclerView.NO_POSITION){    // Check if an item was deleted, but the user clicked it before the UI removed it
                            recycleViewInterface.onDetailsClick(pos);
                        }
                    }
                }
            });
            // "join" button
            itemView.findViewById(R.id.joinBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recycleViewInterface != null){
                        int pos = getAdapterPosition();          //Returns the Adapter position of the item represented by this ViewHolder.

                        if(pos != RecyclerView.NO_POSITION){     // Check if an item was deleted, but the user clicked it before the UI removed it
                            recycleViewInterface.onJoinClick(pos);
                        }
                    }
                }
            });
        }
    }
}
