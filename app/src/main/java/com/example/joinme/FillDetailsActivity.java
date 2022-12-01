package com.example.joinme;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.joinme.databinding.ActivityAdminMainPageBinding;
import com.example.joinme.databinding.ActivityFillDetailsBinding;
import com.example.joinme.databinding.ActivityMainPageBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FillDetailsActivity extends AppCompatActivity {
    String date;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //view binding
    private ActivityFillDetailsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFillDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        checkUser();
//        setSpinners();

        //handle click, logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
                checkUser();
            }
        });
    }

//    private void setSpinners(){
//        //day spinner
//        //get the spinner from the xml.
//        Spinner daySpnr = findViewById(R.id.daySpnr);
////create a list of items for the spinner.
//        String[] days = new String[31];
//        for(int i=1; i <= 31; i++){
//            days[i-1] = String.valueOf(i);
//        }
////create an adapter to describe how the items are displayed, adapters are used in several places in android.
////There are multiple variations of this, but this is the basic variant.
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
////set the spinners adapter to the previously created one.
//        daySpnr.setAdapter(adapter);
//
//        //month spinner
//        //get the spinner from the xml.
//        Spinner monthSpnr = findViewById(R.id.monthSpnr);
////create a list of items for the spinner.
//        String[] months = new String[12];
//        for(int i=1; i <= 12; i++){
//            months[i-1] = String.valueOf(i);
//        }
////create an adapter to describe how the items are displayed, adapters are used in several places in android.
////There are multiple variations of this, but this is the basic variant.
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
////set the spinners adapter to the previously created one.
//        daySpnr.setAdapter(adapter2);
//
//        //year spinner
//        //get the spinner from the xml.
//        Spinner yearSpnr = findViewById(R.id.yearSpnr);
////create a list of items for the spinner.
//        String[] years = new String[12];
//        int currYear = 1960;
//        //let only people that are over 16 in the app
//        for(int i=0; i < 46; i++){
//            days[i] = String.valueOf(currYear);
//            currYear++;
//        }
////create an adapter to describe how the items are displayed, adapters are used in several places in android.
////There are multiple variations of this, but this is the basic variant.
//        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
////set the spinners adapter to the previously created one.
//        daySpnr.setAdapter(adapter3);
//    }


    private void checkUser() {
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
            final Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            binding.etSelectDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    //get all the details
                    String name = binding.FNameTxt.getText().toString() + binding.LNameTxt.getText().toString();
                    String phone = binding.PhoneTxt.getText().toString();


                    //create user
                    User user = new User(uid, name, phone, email, date);
//                    Map<String, Object> currUser = new HashMap<>();
//                    currUser.put("name", name);
//                    currUser.put("phone", phone);
//                    currUser.put("email", email);
//                    currUser.put("birthday", birthday);
//                    currUser.put("my_groups", new ArrayList<>());
//                    currUser.put("num_of_reports", 0);
//                    currUser.put("success_creating_groups", 0);


//                    User currUser = new User(uid, name, phone, email, birthday);.document(uid).set(currUser);
                    //insert
                    // Add a new document with a generated ID
//                    db.collection("users")
//                            .add(currUser);
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Log.d(TAG,"DocumentSnapshot added with ID: " + documentReference.getId());
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Error adding document", e);
//                                }
//                            });
                    startActivity(new Intent(FillDetailsActivity.this, MainPageActivity.class));
                    finish();

                }
            });
        }
    }
}