package com.example.joinme.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinme.R;
import com.example.joinme.Model.DetailsForRecycleHistory;
import com.example.joinme.Model.api.RetrofitClient;
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
    //init firebase auth
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    ArrayList<DetailsForRecycleHistory> details = new ArrayList<>();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    JoinedDetailsAdapter adapter = new JoinedDetailsAdapter(this, details, HistoryJoinedActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryJoinedBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot()); //Set the activity content to an explicit view.
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryJoinedActivity.this, MainPageActivity.class));
            }
        });

        setUpDetails();
    }
    // menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                return true;
            case R.id.subitem1:
                startActivity(new Intent(HistoryJoinedActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.subitem2:
                startActivity(new Intent(HistoryJoinedActivity.this, HistoryJoinedActivity.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(HistoryJoinedActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
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
    }

    @Override
    public void onDetailsClick(int position) {
        Intent intent = new Intent(HistoryJoinedActivity.this, ParticipantsActivity.class);
        Log.d("ID ", details.get(position).getId());
        String gid = details.get(position).getId();
        intent.putExtra("ID", gid);
        startActivity(intent);
    }

    @Override
    public void onJoinClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onReportClick(int position) {

    }

    @Override
    public void onHappenedClick(int position, Boolean flag) {

    }
}
