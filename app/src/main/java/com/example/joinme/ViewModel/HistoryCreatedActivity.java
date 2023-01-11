package com.example.joinme.ViewModel;

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

import com.example.joinme.R;
import com.example.joinme.Model.DetailsForRecycleHistory;
import com.example.joinme.Model.api.RetrofitClient;
import com.example.joinme.databinding.ActivityHistoryCreatedBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryCreatedActivity extends AppCompatActivity implements RecycleViewInterface {
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
                return true;
            case R.id.subitem1:
                startActivity(new Intent(HistoryCreatedActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.subitem2:
                startActivity(new Intent(HistoryCreatedActivity.this, HistoryJoinedActivity.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(HistoryCreatedActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivity(new Intent(HistoryCreatedActivity.this, MainActivity.class));
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

    @Override
    public void onHappenedClick(int position, Boolean flag) {
        details.get(position).setIs_happened(flag);
        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().isHappened(details.get(position).getId(), flag);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("done", "done");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });

    }
}