package com.example.joinme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.joinme.api.RetrofitClient;
import com.example.joinme.databinding.ActivityDeleteUserBinding;
import com.example.joinme.databinding.ActivityHistoryCreatedBinding;
import com.example.joinme.databinding.ActivityParticipantsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParticipantsActivity extends AppCompatActivity implements RecycleViewInterface{
    private ActivityParticipantsBinding binding;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    private GoogleSignInClient mGoogleSignInClient;
    List<UserRow> details = new ArrayList<>();
    ParticipantsAdapter adapter = new ParticipantsAdapter(this, details, ParticipantsActivity.this);
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //The LayoutInflater takes an XML file as input and builds the View objects from it.
        binding = ActivityParticipantsBinding.inflate(getLayoutInflater());

        //Set the activity content to an explicit view. This view is placed directly into the activity's view hierarchy
        setContentView(binding.getRoot());

        gid = getIntent().getStringExtra("ID");

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();      //Returns an instance of this class corresponding to the default FirebaseApp instance
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        setUpUserRows();

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParticipantsActivity.this, HistoryCreatedActivity.class));
            }
        });
    }

    private void setUpUserRows() {
        Call<ArrayList<UserRow>> call = RetrofitClient
                .getInstance()
                .getAPI()
                .presentGroupParticipants(gid);
        call.enqueue(new Callback<ArrayList<UserRow>>() {
            @Override
            public void onResponse(Call<ArrayList<UserRow>>call, Response<ArrayList<UserRow>> response) {
                if(response.isSuccessful()){
                    for(int i=0; i<response.body().size(); i++){
                        details.add(response.body().get(i));
                    }
                    Log.d("name ", details.get(0).getName());
                    RecyclerView recyclerView = findViewById(R.id.usersRcv);
                    recyclerView.setAdapter(adapter); //Set a new adapter to provide child views on demand.
                    recyclerView.setLayoutManager(new LinearLayoutManager(ParticipantsActivity.this)); // Set the RecyclerView.LayoutManager that this RecyclerView will use.
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserRow>> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
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