package com.example.joinme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.joinme.databinding.ActivityFillDetailsBinding;
import com.example.joinme.databinding.ActivityUpdateDetailsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateDetailsActivity extends AppCompatActivity {
    String date;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //view binding
    private ActivityUpdateDetailsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth firebaseAuth;
    Logic logic = new Logic();
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDetailsBinding.inflate(getLayoutInflater());  //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot());                                  //Set the activity content to an explicit view.
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);

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
                startActivity(new Intent(UpdateDetailsActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.item2:
                Toast.makeText(this,"update my details",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateDetailsActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
                Toast.makeText(this,"log out",Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
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
}