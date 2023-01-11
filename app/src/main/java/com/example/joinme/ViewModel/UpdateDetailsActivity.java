package com.example.joinme.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.joinme.R;
import com.example.joinme.Model.Logic;
import com.example.joinme.Model.User;
import com.example.joinme.Model.api.RetrofitClient;
import com.example.joinme.databinding.ActivityUpdateDetailsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDetailsActivity extends AppCompatActivity {
    String date = "";
    //view binding
    private ActivityUpdateDetailsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth firebaseAuth;
    Logic logic = new Logic();
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    User curr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDetailsBinding.inflate(getLayoutInflater());  //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot());                                  //Set the activity content to an explicit view.
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        Call<User> call = RetrofitClient.getInstance().getAPI().getUserDetails(firebaseAuth.getUid());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                curr_user = response.body();
                binding.PhoneTxt.setText(curr_user.getPhone());
                String name = curr_user.getName();
                String[] splitName = name.split(" ");
                binding.FNameTxt.setText(splitName[0]);
                binding.LNameTxt.setText(splitName[1]);
                binding.etSelectDate.setText(curr_user.getBirth_date());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
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
                DatePickerDialog dialog = new DatePickerDialog(UpdateDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        binding.UpdateBtn.setOnClickListener(new View.OnClickListener(){

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
                    date = curr_user.getBirth_date();
                }
                String[] groupDate = date.split("/");
                if(logic.checkDate(Integer.parseInt(today[0]), Integer.parseInt(today[1]), Integer.parseInt(today[2]), Integer.parseInt(groupDate[2]), Integer.parseInt(groupDate[1]), Integer.parseInt(groupDate[0]))) {
                    Log.d("birthday", String.valueOf(TextUtils.isEmpty(date)));
                    binding.etSelectDate.setError("invalid date");
                    return;
                }
                //get all the details
                String name = binding.FNameTxt.getText().toString() + " " + binding.LNameTxt.getText().toString();
                String phone = binding.PhoneTxt.getText().toString();
                Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().updateUserDetails(firebaseAuth.getUid(),name,date,phone);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Log.d("done", "done");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });
                startActivity(new Intent(UpdateDetailsActivity.this, MainPageActivity.class));
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
                startActivity(new Intent(UpdateDetailsActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.subitem2:
                startActivity(new Intent(UpdateDetailsActivity.this, HistoryJoinedActivity.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(UpdateDetailsActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
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