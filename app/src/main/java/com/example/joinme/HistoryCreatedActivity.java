package com.example.joinme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.joinme.api.RetrofitClient;
import com.example.joinme.databinding.ActivityHistoryCreatedBinding;
import com.example.joinme.databinding.ActivityOpenGroupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryCreatedActivity extends AppCompatActivity implements RecycleViewInterface{
    private ActivityHistoryCreatedBinding binding;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    private GoogleSignInClient mGoogleSignInClient;
    List<DetailsForRecycleHistory> details = new ArrayList<>();
    DetailsAdapter adapter = new DetailsAdapter(this, details, HistoryCreatedActivity.this);
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryCreatedBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot()); //Set the activity content to an explicit view.
        //init firebase auth
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
//        RecyclerView recyclerView = findViewById(R.id.rvBox);

        setUpDetails();
        //DetailsAdapter adapter = new DetailsAdapter(this, details); // todo: check it need to be here acording to the video
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void setUpDetails(){
        Call<ArrayList<DetailsForRecycleHistory>> call = RetrofitClient
                .getInstance()
                .getAPI()
                .presentMyCreatedHistory(firebaseUser.getUid());
        call.enqueue(new Callback<ArrayList<DetailsForRecycleHistory>>() {
            @Override
            public void onResponse(Call<ArrayList<DetailsForRecycleHistory>> call, Response<ArrayList<DetailsForRecycleHistory>> response) {
                if(response.isSuccessful()) {
                    Log.d("here", "response");
                    for(int i=0; i<response.body().size(); i++){
//                        Log.d("Check", response.body().get(i).getCategory());
                        details.add(response.body().get(i));
                    }
                    RecyclerView recyclerView = findViewById(R.id.rvCreatedBox);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HistoryCreatedActivity.this));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DetailsForRecycleHistory>> call, Throwable t) {
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