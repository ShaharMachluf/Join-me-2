package com.example.joinme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinme.api.RetrofitClient;
import com.example.joinme.databinding.ActivityDeleteUserBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteUserActivity extends AppCompatActivity implements RecycleViewInterface{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //view binding
    private ActivityDeleteUserBinding binding;
    private GoogleSignInClient mGoogleSignInClient;   //A client for interacting with the Google Sign In API.
    private static final int RC_SIGN_IN = 100;        //Request code used to invoke sign in user interactions.
    private FirebaseAuth firebaseAuth;                //The entry point of the Firebase Authentication SDK.
    private static final String TAG = "DELETE_USER_TAG";
    List<UserRow> userRows = new ArrayList<>();
    private SearchView searchView;
    DeleteUserAdapter adapter = new DeleteUserAdapter(this, userRows, DeleteUserActivity.this);
    androidx.constraintlayout.widget.ConstraintLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** onCreate() is called when the when the activity is first created.
         *  @param  savedInstanceState –is a reference to the Bundle object that is passed to the onCreate method of each Android activity.
         *                             Activities have the ability, under special circumstances,to restore themselves to a previous state
         *                             using the data stored in this package.
         */

        super.onCreate(savedInstanceState);

        //The LayoutInflater takes an XML file as input and builds the View objects from it.
        binding = ActivityDeleteUserBinding.inflate(getLayoutInflater());

        //Set the activity content to an explicit view. This view is placed directly into the activity's view hierarchy
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();      //Returns an instance of this class corresponding to the default FirebaseApp instance
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        parent = findViewById(R.id.delPage);            //Finds a view that was identified by the android:id XML attribute that was processed in onCreate.
        searchView = findViewById(R.id.searchV);
        searchView.clearFocus();

        //Sets a listener for user actions within the SearchView.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Called when the user submits the query.
                return false;  // we return false to let the SearchView handle the submission by launching any associated intent.

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /** Called when the query text is changed by the user.
                 *  @param newText – the new content of the query text field.
                 *  @return true because we sent the action to the fileList function to handle it.
                 */
               filterList(newText);
                return true;
            }
        });

        setUpUserRows();

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeleteUserActivity.this, AdminMainPageActivity.class));
            }
        });
    }

    private void filterList(String text) {
        /**
         * Search of the user received as input in the list (doesn't matter if written in lowercase or uppercase letters)
         */
        List<UserRow> filteredList = new ArrayList<>();
        for(UserRow user: userRows){
            if(user.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(user);
                Log.d(TAG, user.getName());
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setFilteredList(filteredList);

        }
    }



    @Override
    public void onDetailsClick(int position) {

    }

    @Override
    public void onJoinClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        onButtonShowPopupWindowClick(position);
    }

    @Override
    public void onReportClick(int position) {

    }

    @Override
    public void onHappenedClick(int position, Boolean flag) {

    }

    public void onButtonShowPopupWindowClick(int pos) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //Inflate a new view hierarchy from the specified xml resource.
        View popupView = inflater.inflate(R.layout.delete_user_popup, null);

        //confirm the deletion of the user
        TextView tvMsg = popupView.findViewById(R.id.msgTxt);
        Button yesBtn = popupView.findViewById(R.id.yesBtn);
        Button noBtn = popupView.findViewById(R.id.noBtn);
        tvMsg.setText("Are you sure you want to delete " + userRows.get(pos).getName() + "?");

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it

        //This class represents a popup window that can be used to display an arbitrary view.
        //The popup window is a floating container that appears on top of the current activity.
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * If we click Yes in the window, the user enters the collection of the blocked users
                 */
                popupWindow.dismiss();
                String curr_uid = userRows.get(pos).getUid();
                Log.d(TAG, "yes");
                db.collection("usersById").document(userRows.get(pos).getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        User block_user = document.toObject(User.class);
                        Log.d(TAG, "the user= " + block_user.toString());
                        db.collection("blockUsers").document(curr_uid).set(block_user);
                        }
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                db.collection("usersById").document(curr_uid).delete();
                userRows.remove(pos);
                Toast.makeText(DeleteUserActivity.this, "user blocked successfully", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                    }
                });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * If we click in the window no, the window will close
                 */
                Log.d(TAG, "no");
                popupWindow.dismiss();

            }
        });

    }


    private void setUpUserRows(){
        Call<ArrayList<UserRow>> call = RetrofitClient
                .getInstance()
                .getAPI()
                .presentUsersToBlock();
        call.enqueue(new Callback<ArrayList<UserRow>>() {
            @Override
            public void onResponse(Call<ArrayList<UserRow>>call, Response<ArrayList<UserRow>> response) {
                if(response.isSuccessful()){
                    userRows = response.body();
                    filterList("");
                    RecyclerView recyclerView = findViewById(R.id.usersRcv);
                    recyclerView.setAdapter(adapter); //Set a new adapter to provide child views on demand.
                    recyclerView.setLayoutManager(new LinearLayoutManager(DeleteUserActivity.this)); // Set the RecyclerView.LayoutManager that this RecyclerView will use.
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserRow>> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });

    }
}




