package com.example.joinme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinme.databinding.ActivityHistoryCreatedBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HistoryJoinedActivity extends AppCompatActivity implements RecycleViewInterface {
    private ActivityHistoryCreatedBinding binding;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ArrayList<DetailsForRecycleHistory> details = new ArrayList<>();
    DetailsAdapter adapter = new DetailsAdapter(this, details, HistoryJoinedActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryCreatedBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot()); //Set the activity content to an explicit view.
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
//        RecyclerView recyclerView = findViewById(R.id.rvBox);
        setUpDetails();
        //DetailsAdapter adapter = new DetailsAdapter(this, details); // todo: check it need to be here acording to the video
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this,"my history groups",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem1:
                Toast.makeText(this, "created group", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HistoryJoinedActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.item2:
                Toast.makeText(this,"update my details",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HistoryJoinedActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
                Toast.makeText(this,"log out",Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                //startActivityForResult(signInIntent, RC_SIGN_IN);
                //checkUser();
                startActivity(new Intent(HistoryJoinedActivity.this, MainActivity.class));
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

    private void setUpDetails(){
        String[] categories = {"Football", "Minyan", "Basketball"};
        String[] location = {"Ariel", "Tel Aviv", "Yad Binyamin"};
        String[] date = {"15.11.2022", "4.1.2023", "11.1.2023"};
        for (int i = 0; i < 3; i++) {
            details.add(new DetailsForRecycleHistory(categories[i], location[i], date[i]));

        }
        RecyclerView recyclerView = findViewById(R.id.rvBox);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryJoinedActivity.this));
    }

    @Override
    public void onDetailsClick(int position) {

    }

    @Override
    public void onJoinClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }
}
