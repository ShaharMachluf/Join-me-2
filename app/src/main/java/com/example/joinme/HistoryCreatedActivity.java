package com.example.joinme;

import androidx.annotation.NonNull;
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
    //init firebase auth
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private GoogleSignInClient mGoogleSignInClient;
    List<DetailsForRecycleHistory> details = new ArrayList<>();
    DetailsAdapter adapter = new DetailsAdapter(this, details, HistoryCreatedActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryCreatedBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot()); //Set the activity content to an explicit view.
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryCreatedActivity.this, MainPageActivity.class));
            }
        });
        setUpDetails();
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
                startActivity(new Intent(HistoryCreatedActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.item2:
                Toast.makeText(this,"update my details",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HistoryCreatedActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
                Toast.makeText(this,"log out",Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                //startActivityForResult(signInIntent, RC_SIGN_IN);
                //checkUser();
                //startActivity(new Intent(MainPageActivity.this, MainActivity.class));
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
                .presentMyCreatedHistory(firebaseUser.getUid());
        call.enqueue(new Callback<ArrayList<DetailsForRecycleHistory>>() {
            @Override
            public void onResponse(Call<ArrayList<DetailsForRecycleHistory>> call, Response<ArrayList<DetailsForRecycleHistory>> response) {
                if(response.isSuccessful()) {
                    Log.d("here", "response");
                    for(int i=0; i<response.body().size(); i++){
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
        Intent intent = new Intent(HistoryCreatedActivity.this, ParticipantsActivity.class);
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
}