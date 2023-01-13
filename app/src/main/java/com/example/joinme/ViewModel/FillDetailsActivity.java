package com.example.joinme.ViewModel;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.example.joinme.Model.api.RetrofitClient;
import com.example.joinme.databinding.ActivityFillDetailsBinding;
import com.example.joinme.Model.Logic;
import com.example.joinme.Model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FillDetailsActivity extends AppCompatActivity {
    String date;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //view binding
    private ActivityFillDetailsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth firebaseAuth;
    Logic logic = new Logic();
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** onCreate() is called when the when the activity is first created.
         *  @param  savedInstanceState –is a reference to the Bundle object that is passed to the onCreate method of each Android activity.
         *                             Activities have the ability, under special circumstances,to restore themselves to a previous state
         *                             using the data stored in this package.
         */

        super.onCreate(savedInstanceState);
        binding = ActivityFillDetailsBinding.inflate(getLayoutInflater());  //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot());                                  //Set the activity content to an explicit view.
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        checkUser();
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
            String email = firebaseUser.getEmail();
            String uid = firebaseUser.getUid();
            //prepare calendar
            final Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            binding.etSelectDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**
                     * you can double-click on the date of birth and a pop-up calendar window will open
                     */
                    DatePickerDialog dialog = new DatePickerDialog(FillDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month = month+1;
                            date = dayOfMonth+"/"+month+"/"+year;
                            binding.etSelectDate.setText(date);
                        }
                    },year, month,day);
                    dialog.show();
                }
            });
            binding.ConfirmBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    /**
                     * When creating a new user, a screen opens where he enters the login information
                     * (all details - an empty window will lead to an error message)
                     */
                    //Returns true if the string is null or 0-length.
                    if(TextUtils.isEmpty(binding.FNameTxt.toString())){
                        binding.FNameTxt.setError("please enter your first name");
                        return;
                    }

                    if(TextUtils.isEmpty(binding.PhoneTxt.toString())) {
                        binding.PhoneTxt.setError("please enter your phone number");
                        return;
                    }
                    String [] today = new String[3];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        today = java.time.LocalDate.now().toString().split("-");
                    }
                    if(TextUtils.isEmpty(date)){
                        binding.etSelectDate.setError("please enter your birthday");
                    }
                    String[] groupDate = date.split("/");
                    if(logic.checkDate(Integer.parseInt(today[0]), Integer.parseInt(today[1]), Integer.parseInt(today[2]), Integer.parseInt(groupDate[2]), Integer.parseInt(groupDate[1]), Integer.parseInt(groupDate[0]))) {
                        binding.etSelectDate.setError("invalid date");
                        return;
                    }
                    //get all the details
                    String name = binding.FNameTxt.getText().toString() + " " + binding.LNameTxt.getText().toString();
                    String phone = binding.PhoneTxt.getText().toString();

                    //create user
//                    User user = new User(uid, name, phone, email, date);
                    //add user to database
//                    db.collection("usersById").document(uid).set(user);
                    Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().addUser(uid, name, phone, email, date);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("add user", "success");
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("add user", t.getMessage());
                        }
                    });
                    startActivity(new Intent(FillDetailsActivity.this, MainPageActivity.class));
                    finish();
                }
            });
        }
    }
}
