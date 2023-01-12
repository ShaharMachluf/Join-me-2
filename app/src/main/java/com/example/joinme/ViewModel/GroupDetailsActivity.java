package com.example.joinme.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.joinme.Model.Category;
import com.example.joinme.Model.Group;
import com.example.joinme.Model.User;
import com.example.joinme.Model.api.RetrofitClient;
import com.example.joinme.R;
import com.example.joinme.databinding.ActivityGroupDetailsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailsActivity extends AppCompatActivity {
    private ActivityGroupDetailsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;     //A client for interacting with the Google Sign In API.
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;              //Request code used to invoke sign in user interactions.
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    public String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** onCreate() is called when the when the activity is first created.
         *  @param  savedInstanceState –is a reference to the Bundle object that is passed to the onCreate method of each Android activity.
         *                             Activities have the ability, under special circumstances,to restore themselves to a previous state
         *                             using the data stored in this package.
         */
        super.onCreate(savedInstanceState);
        binding = ActivityGroupDetailsBinding.inflate(getLayoutInflater());//Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot());                                //Set the activity content to an explicit view.
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        String id = getIntent().getStringExtra("ID");
        getDetailsFromDb(id);


        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailsActivity.this, RelevantGroupsActivity.class);
                intent.putExtra("Title", title);
                intent.putExtra("City", "");
                startActivity(intent);

                finish();
            }
        });
    }

    // menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                return true;
            case R.id.subitem1:
                startActivity(new Intent(GroupDetailsActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.subitem2:
                startActivity(new Intent(GroupDetailsActivity.this, HistoryJoinedActivity.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(GroupDetailsActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                startActivity(new Intent(GroupDetailsActivity.this, MainActivity.class));
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

    //get all the details of the group and present them
    private void getDetailsFromDb(String id){
        Call<Group> call = RetrofitClient.getInstance().getAPI().getGroupDetails(id);
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                binding.titleTxt.setText(response.body().getTitle());
                title = response.body().getTitle();
                binding.addressTxt.setText(response.body().getCity());
                binding.dateTxt.setText(response.body().getDate());
                binding.timeTxt.setText(response.body().getTime());
                binding.numPartTxt.setText("Current number of participants in the group: " + response.body().getNum_of_participant());
                binding.headTxt.setText("The head of the group is:\n " + response.body().getHead_of_group());
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });
    }
}
