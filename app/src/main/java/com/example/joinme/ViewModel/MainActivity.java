//used this video https://www.youtube.com/watch?v=gD9uQf5UU-g&ab_channel=AtifPervaiz

package com.example.joinme.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.joinme.Model.api.RetrofitClient;
import com.example.joinme.R;
import com.example.joinme.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int RC_SIGN_IN = 100;              //Request code used to invoke sign in user interactions.
    private GoogleSignInClient googleSignInClient;          //A client for interacting with the Google Sign In API.
    public static GoogleSignInOptions googleSignInOptions;  //GoogleSignInOptions contains options used to configure the Auth.GOOGLE_SIGN_IN_API
    private FirebaseAuth firebaseAuth;                      //The entry point of the Firebase Authentication SDK.

    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** onCreate() is called when the when the activity is first created.
         *  @param  savedInstanceState –is a reference to the Bundle object that is passed to the onCreate method of each Android activity.
         *                             Activities have the ability, under special circumstances,to restore themselves to a previous state
         *                             using the data stored in this package.
         */

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot());                          //Set the activity content to an explicit view.

        //confirm google signin
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //google SignInButton: Click to begin Google SignIn
        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //begin google sign in
                Log.d(TAG, "onClick: begin Google SignIn");
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }

    private void checkUser() {
        /**
         This function checks the user and starts profile activity according to the type of user-administrator or normal user.
         */
        //if user is already signed in then go to main page
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            String email = firebaseUser.getEmail();
            String uid = firebaseUser.getUid();
            //admin logged in
            if(email.equals("jiayhb@gmail.com")){
                Log.d(TAG, "onSuccess: Admin logged in...\n"+email);
                Toast.makeText(MainActivity.this, "Admin logged in...\n"+email, Toast.LENGTH_SHORT).show();
                //start profile activity
                startActivity(new Intent(MainActivity.this, AdminMainPageActivity.class));
                finish();
            }
            else {
                blocked_user(uid);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * onActivityResult is a callback when there is an action done with intent such as a selection, button click, etc.
         * @param requestCode – The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
         * @param  resultCode – The integer result code returned by the child activity through its setResult().
         * @param  data – An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
         */

        //super().onActivityResult()  is use to handle the error or exception during execution of Intent.
        super.onActivityResult(requestCode, resultCode, data);

        //result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        //requestCode – The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
        if(requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google Signin intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                //Google sign in success, now auth with firebase
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            }
            catch (Exception e){
                //failed google sign in
                Log.d(TAG, "onActivityResult: "+e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        /**
         * firebaseAuthWithGoogleAccount begin firebase auth with google account
         * @param account –holds the basic account information of the signed in Google user.
         */

        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //login success
                        Log.d(TAG, "onSuccess: Logged In");

                        //get logged user
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        //get user info
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();

                        Log.d(TAG, "onSuccess: Email: "+email);
                        Log.d(TAG, "onSuccess: UID: "+uid);

                        //admin logged in
                        if(email.equals("jiayhb@gmail.com")){
                            Log.d(TAG, "onSuccess: Admin logged in...\n"+email);
                            Toast.makeText(MainActivity.this, "Admin logged in...\n"+email, Toast.LENGTH_SHORT).show();
                            //start profile activity
                            startActivity(new Intent(MainActivity.this, AdminMainPageActivity.class));
                            finish();
                        }

                        //check if user is new or existing
                        else if(authResult.getAdditionalUserInfo().isNewUser()){
                            //user is new- account created
                            Log.d(TAG, "onSuccess: Account Created...\n"+email);
                            Toast.makeText(MainActivity.this, "Account Created...\n"+email, Toast.LENGTH_SHORT).show();
                            //start profile activity
                            startActivity(new Intent(MainActivity.this, FillDetailsActivity.class));
                            finish();
                        }
                        else {
                            //existing account
                            blocked_user(uid);
                        }
                    }
                })
                //adds a listener that is called if the Task fails.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //login failed
                        Log.d(TAG, "onFailure: Loggin failed "+e.getMessage());
                    }
                });
    }

    private void blocked_user(String uid) {
        /**
         * This function looks for the user id of the user in the blocked users collection,
         * if the user is indeed in the collection he will not be allowed to enter the application.
         *
         * @param uid – holds the user id.
         */
        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().checkBlockedUser(uid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    final String ans = response.body().string();
                    if(ans.equals("blocked")){
                        Toast.makeText(MainActivity.this, "You are blocked", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        googleSignInClient.signOut();
                    }else{
                        startActivity(new Intent(MainActivity.this, MainPageActivity.class));
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("block", t.getMessage());
            }
        });
    }
}
