package com.example.joinme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.joinme.databinding.ActivityHistoryCreatedBinding;
import com.example.joinme.databinding.ActivityOpenGroupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HistoryCreatedActivity extends AppCompatActivity implements RecycleViewInterface{
    private ActivityHistoryCreatedBinding binding;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ArrayList<DetailsForRecycleHistory> details = new ArrayList<>();
    DetailsAdapter adapter = new DetailsAdapter(this, details, HistoryCreatedActivity.this);

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

    private void setUpDetails(){
        String[] categories = {"Football", "Minyan", "Basketball"};
        String[] location = {"Ariel", "Tel Aviv", "Yad Binyamin"};
        String[] date = {"15.11.2022", "4.1.2023", "11.1.2023"};
        for (int i = 0; i < 3; i++) {
            details.add(new DetailsForRecycleHistory(categories[i], location[i], date[i]));

        }
        RecyclerView recyclerView = findViewById(R.id.rvBox);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryCreatedActivity.this));
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