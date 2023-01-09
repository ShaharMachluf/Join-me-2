package com.example.joinme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Arrays;

public class RelevantGroupsActivity extends AppCompatActivity implements RecycleViewInterface{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityRelevantGroupsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;     //A client for interacting with the Google Sign In API.
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;              //Request code used to invoke sign in user interactions.
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    ArrayList<Contact> contacts = new ArrayList<>();
    Logic logic = new Logic();
//    RecyclerView recyclerView;
    ContactsAdapter adapter = new ContactsAdapter(RelevantGroupsActivity.this, contacts, RelevantGroupsActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** onCreate() is called when the when the activity is first created.
         *  @param  savedInstanceState –is a reference to the Bundle object that is passed to the onCreate method of each Android activity.
         *                             Activities have the ability, under special circumstances,to restore themselves to a previous state
         *                             using the data stored in this package.
         */
        super.onCreate(savedInstanceState);
        binding = ActivityRelevantGroupsBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        setContentView(binding.getRoot());
        setUpContact();

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RelevantGroupsActivity.this, FindGroupActivity.class));
            }
        });

    }

    // menu
    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this,"my history groups",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this,"update my details",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RelevantGroupsActivity.this, FillDetailsActivity.class));
                return true;
            case R.id.item3:
                Toast.makeText(this,"log out",Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                startActivity(new Intent(RelevantGroupsActivity.this, MainActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.our_menu, menu);
        return true;
    }

    private void setUpContact(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String city = intent.getStringExtra("City");
        String [] today = new String[3];

        //if true it means the device running the app has Android SDK 26 or up
        //otherwise- the SDK version is lower than 26. (SDK 25 or lower)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            today = java.time.LocalDate.now().toString().split("-");
            Log.d(TAG, "the date is: " + Arrays.toString(today));
        }


        //the conditions of if and else are designed to create a choice - whether to insert the name of the city or not
        if(city.equals("")) {
            String[] finalToday = today;
            db.collection("groups")
                    .whereEqualTo("title", title)
                    .whereEqualTo("is_happened", false)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                presentRelevantGroups(task, finalToday);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else{
            String[] finalToday1 = today;
            db.collection("groups")
                    .whereEqualTo("title", title).whereEqualTo("city", city)
                    .whereEqualTo("is_happened", false)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                presentRelevantGroups(task, finalToday1);
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
                                ArrayList<String> participants = group.getParticipants();
                                String uid = firebaseAuth.getCurrentUser().getUid();

                                //make sure the user is not already in this group
                                for(int i = 0; i < participants.size(); ++i){
                                    if(participants.get(i).equals(uid)){
                                        Toast.makeText(RelevantGroupsActivity.this, "You are already in this group", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                //adding the user to the group
                                group.addParticipant(uid);
                                addParticipantToDb(groupID, group);
                                addGroupToUserDB(groupID, uid);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onReportClick(int position) {

    }

    private void addGroupToUserDB(String groupID, String uid) {
        db.collection("usersById").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                user.addGroupIJoined(groupID);
                                addToUserDb(user, uid);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void addToUserDb(User user, String uid) {
        db.collection("usersById").document(uid).set(user);
    }

    private void addParticipantToDb(String groupID, Group group) {
        db.collection("groups").document(groupID).set(group);
        Toast.makeText(this, "Joined group successfully", Toast.LENGTH_SHORT).show();
    }

    //Displaying the list in which the relevant groups appear
    private void presentRelevantGroups(@NonNull Task<QuerySnapshot> task, String[] finalToday){
        RecyclerView recyclerView = findViewById(R.id.rvBox);
        TextView nonResultTxt = findViewById(R.id.nonResultTxt);
        if (task.getResult().isEmpty()){
            nonResultTxt.setText("No matching groups were found");
        }
        for (QueryDocumentSnapshot document : task.getResult()) {
            String [] groupDate = document.getString("date").split("/");
            //Checking if it is possible to add without exceeding the conditions set for the group
            if(document.getLong("max_participants") > document.getLong("num_of_participant") &&
                    logic.checkDate(Integer.parseInt(finalToday[0]), Integer.parseInt(finalToday[1]), Integer.parseInt(finalToday[2]),
                            Integer.parseInt(groupDate[2]), Integer.parseInt(groupDate[1]), Integer.parseInt(groupDate[0]))) {
                contacts.add(new Contact(document.getString("title"), document.getString("city"), document.getString("date") + " " + document.getString("time"), document.getId()));
                Log.d(TAG, document.getId() + " => " + document.getString("city"));
            }
        }

        if(contacts.isEmpty()){
            nonResultTxt.setText("No matching groups were found");
        }

        //Here we need to create the adapter which will actually populate the data into the RecyclerView.
        // The adapter's role is to convert an object at a position into a list row item to be inserted.
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(RelevantGroupsActivity.this));

    }

}
