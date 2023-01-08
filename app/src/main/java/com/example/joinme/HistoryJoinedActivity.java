package com.example.joinme;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinme.api.RetrofitClient;
import com.example.joinme.databinding.ActivityHistoryCreatedBinding;
import com.example.joinme.databinding.ActivityHistoryJoinedBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryJoinedActivity extends AppCompatActivity implements RecycleViewInterface {
    private ActivityHistoryJoinedBinding binding;
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    ArrayList<DetailsForRecycleHistory> details = new ArrayList<>();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    DetailsAdapter adapter = new DetailsAdapter(this, details, HistoryJoinedActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryJoinedBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot()); //Set the activity content to an explicit view.
        //init firebase auth
//        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
//        RecyclerView recyclerView = findViewById(R.id.rvBox);
        setUpDetails();
        //DetailsAdapter adapter = new DetailsAdapter(this, details); // todo: check it need to be here acording to the video
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setUpDetails(){
//        String[] categories = {"Football", "Minyan", "Basketball"};
//        String[] location = {"Ariel", "Tel Aviv", "Yad Binyamin"};
//        String[] date = {"15.11.2022", "4.1.2023", "11.1.2023"};
//        for (int i = 0; i < 3; i++) {
//            details.add(new DetailsForRecycleHistory(categories[i], location[i], date[i]));
//
//        }
        Call<ArrayList<DetailsForRecycleHistory>> call = RetrofitClient
                .getInstance()
                .getAPI()
                .presentMyJoinedHistory(firebaseUser.getUid());
        call.enqueue(new Callback<ArrayList<DetailsForRecycleHistory>>() {
            @Override
            public void onResponse(Call<ArrayList<DetailsForRecycleHistory>> call, Response<ArrayList<DetailsForRecycleHistory>> response) {
                if(response.isSuccessful()) {
                    for(int i=0; i<response.body().size(); i++){
                        details.add(response.body().get(i));
                    }
                    RecyclerView recyclerView = findViewById(R.id.rvJoinedBox);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HistoryJoinedActivity.this));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DetailsForRecycleHistory>> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
//        RecyclerView recyclerView = findViewById(R.id.rvBox);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryJoinedActivity.this));
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
