package com.example.joinme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinme.databinding.ActivityDeleteUserBinding;
import com.example.joinme.databinding.ActivityFindGroupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeleteUserActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //view binding
    private ActivityDeleteUserBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "DELETE_USER_TAG";
    ArrayList<UserRow> userRows = new ArrayList<>();
    private SearchView searchView;
    RecyclerView recyclerView;
    DeleteUserAdapter adapter = new DeleteUserAdapter(this, userRows);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fileList(newText);
                return true;
            }
        });
        recyclerView = findViewById(R.id.usersRcv);

        setUpUserRows();

//        DeleteUserAdapter adapter = new DeleteUserAdapter(this, userRows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //handle click, logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeleteUserActivity.this, AdminMainPageActivity.class));
            }
        });
    }

    private void fileList(String text) {
        ArrayList<UserRow> filteredList = new ArrayList<>();
        for(UserRow user: userRows){
            if(user.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(user);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setFilteredList(filteredList);
        }
    }

    private void setUpUserRows(){
//        String[] UserNames = {"Chen Shtyn", "Tavor Levine"};
//        String[] Mails = {"chen@gmail", "tavor@gmail"};
//
//        for(int i=0; i<2; ++i){
//            userRows.add(new UserRow(UserNames[i], Mails[i]));
//        }
        db.collection("usersById").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            RecyclerView recyclerView = findViewById(R.id.usersRcv);
                            if (task.getResult().isEmpty()){
                                TextView nonResultTxt = findViewById(R.id.nonResultTxt);
                                nonResultTxt.setText("No matching users were found");
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userRows.add(new UserRow(document.getString("name"), document.getString("mail")));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            DeleteUserAdapter adapter = new DeleteUserAdapter(DeleteUserActivity.this, userRows);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(DeleteUserActivity.this));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}