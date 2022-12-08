package com.example.joinme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.joinme.databinding.ActivityOpenGroupBinding;
import com.example.joinme.databinding.ActivityRelevantGroupsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class RelevantGroupsActivity extends AppCompatActivity implements RecycleViewInterface{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityRelevantGroupsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    ArrayList<Contact> contacts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRelevantGroupsBinding.inflate(getLayoutInflater());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
//        checkUser();
        setContentView(binding.getRoot());
        setUpContact();
        //handle click, logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
//                checkUser();
            }
        });
        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RelevantGroupsActivity.this, FindGroupActivity.class));
            }
        });

    }

    private void setUpContact(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String city = intent.getStringExtra("City");
        if(city.equals("")) {
            db.collection("groups")
                    .whereEqualTo("title", title)
                    .whereEqualTo("is_happened", false)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                RecyclerView recyclerView = findViewById(R.id.rvBox);
                                if (task.getResult().isEmpty()){
                                    TextView nonResultTxt = findViewById(R.id.nonResultTxt);
                                    nonResultTxt.setText("No matching groups were found");
                                }
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getLong("max_participants") > document.getLong("num_of_participant")) {
                                        contacts.add(new Contact(document.getString("title"), document.getString("city"), document.getString("date") + " " + document.getString("time"), document.getId()));
                                        Log.d(TAG, document.getId() + " => " + document.getString("city"));
                                    }
                                }
                                ContactsAdapter adapter = new ContactsAdapter(RelevantGroupsActivity.this, contacts, RelevantGroupsActivity.this);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(RelevantGroupsActivity.this));
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else{
            db.collection("groups")
                    .whereEqualTo("title", title).whereEqualTo("city", city)
                    .whereEqualTo("is_happened", false)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                RecyclerView recyclerView = findViewById(R.id.rvBox);
                                if (task.getResult().isEmpty()){
                                    TextView nonResultTxt = findViewById(R.id.nonResultTxt);
                                    nonResultTxt.setText("No matching groups were found");
                                }
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getLong("max_participants") > document.getLong("num_of_participant")) {
                                        contacts.add(new Contact(document.getString("title"), document.getString("city"), document.getString("date") + " " + document.getString("time"), document.getId()));
                                        Log.d(TAG, document.getId() + " => " + document.getString("city"));
                                    }
                                }
                                ContactsAdapter adapter = new ContactsAdapter(RelevantGroupsActivity.this, contacts, RelevantGroupsActivity.this);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(RelevantGroupsActivity.this));
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    @Override
    public void onDetailsClick(int position) {
        Intent intent = new Intent(RelevantGroupsActivity.this, GroupDetailsActivity.class);
        intent.putExtra("ID", contacts.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onJoinClick(int position) {
        String groupID = contacts.get(position).getId();
        db.collection("groups")
                .document(groupID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Group group = document.toObject(Group.class);
//                                group.setNum_of_participant(group.getNum_of_participant() + 1);
                                group.addParticipant(firebaseAuth.getCurrentUser().getUid());
                                addParticipantToDb(groupID, group);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void addParticipantToDb(String groupID, Group group) {
        db.collection("groups").document(groupID).set(group);
        Toast.makeText(this, "Joined group successfully", Toast.LENGTH_SHORT).show();
    }

}
