//used this video https://www.youtube.com/watch?v=gD9uQf5UU-g&ab_channel=AtifPervaiz
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

import com.example.joinme.Model.User;
import com.example.joinme.Model.api.RetrofitClient;
import com.example.joinme.R;
import com.example.joinme.databinding.ActivityMainPageBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends AppCompatActivity {

    //view binding
    private ActivityMainPageBinding binding;
    private GoogleSignInClient mGoogleSignInClient;     // A client for interacting with the Google Sign In API.
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;          // Request code used to invoke sign in user interactions.
    private static final String TAG = "WELCOME_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** onCreate() is called when the when the activity is first created.
         *  @param  savedInstanceState –is a reference to the Bundle object that is passed to the onCreate method of each Android activity.
         *                             Activities have the ability, under special circumstances,to restore themselves to a previous state
         *                             using the data stored in this package.
         */
        super.onCreate(savedInstanceState);
        binding = ActivityMainPageBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot());                              //Set the activity content to an explicit view.

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        checkUser();

        binding.createGroupBtn.setOnClickListener(new View.OnClickListener() {
            //open group button
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPageActivity.this, OpenGroupActivity.class));
                finish();
            }
        });

        binding.joinGroupBtn.setOnClickListener(new View.OnClickListener() {
            //find group button
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPageActivity.this, FindGroupActivity.class));
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
                startActivity(new Intent(MainPageActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.subitem2:
                startActivity(new Intent(MainPageActivity.this, HistoryJoinedActivity.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(MainPageActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
                checkUser();
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

    private void checkUser() {
        /**
         This function checks the current user, if it is null then the user is not logged in and therefore he will have to log in.
         Note: CurrentUser may also return null because the authentication object has not finished initializing.
         */
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //user not logged in
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            //user logged in
            //get user info
            String uid = firebaseUser.getUid();
            Call<User> call = RetrofitClient.getInstance().getAPI().getUserDetails(uid);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    binding.nameTv.setText(response.body().getName());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("mainPage", t.getMessage());
                }
            });
        }
    }


}
