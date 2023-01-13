package com.example.joinme.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinme.Model.Category;
import com.example.joinme.R;
import com.example.joinme.databinding.ActivityDeleteUserBinding;
import com.example.joinme.Model.User;
import com.example.joinme.Model.UserRow;
import com.example.joinme.Model.api.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteUserActivity extends AppCompatActivity implements RecycleViewInterface {
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

    // menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                onButtonShowPopupWindowClick();
                return true;
            case R.id.item2:
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
                startActivity(new Intent(DeleteUserActivity.this, MainActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    public void onButtonShowPopupWindowClick(){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //Inflate a new view hierarchy from the specified xml resource.
        View popupView = inflater.inflate(R.layout.add_category_popup, null);
        //confirm the deletion of the user
        EditText tvCategory = popupView.findViewById(R.id.add_categoryTxt);
        Button addBtn = popupView.findViewById(R.id.addBtn);
        Button xBtn = popupView.findViewById(R.id.xBtn);


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
        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = tvCategory.getText().toString();
                if(category.isEmpty()){
                    tvCategory.setError("please enter category");
                }
                else{
                    Call<ArrayList<Category>> call = RetrofitClient.getInstance().getAPI().getCategories();
                    call.enqueue(new Callback<ArrayList<Category>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                            for(int i=0; i<response.body().size(); i++){
                                Log.d("category", response.body().get(i).getName());
                                if(category.equals(response.body().get(i).getName())){
                                    tvCategory.setError("this category already exist");
                                    return;
                                }
                            }
                            Call<ResponseBody> call2 = RetrofitClient.getInstance().getAPI().addCategory(category);
                            call2.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.d("add", "add category");
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d("fail", t.getMessage());
                                }
                            });
                            popupWindow.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                            Log.d("fail", t.getMessage());
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
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
        tvMsg.setText("Are you sure you want to block " + userRows.get(pos).getName() + "?");

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
                deleteUser(curr_uid);
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

    private void deleteUser(String uid){
        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().blockThisUser(uid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("block", "done");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("fail", t.getMessage());
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




