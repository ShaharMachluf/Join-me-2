package com.example.joinme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.joinme.databinding.ActivityAdminMainPageBinding;
import com.example.joinme.databinding.ActivityFillDetailsBinding;
import com.example.joinme.databinding.ActivityMainPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FillDetailsActivity extends AppCompatActivity {

    //view binding
    private ActivityFillDetailsBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFillDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        //get the spinner from the xml.
        Spinner daySpnr = findViewById(R.id.daySpnr);
//create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        daySpnr.setAdapter(adapter);

        //handle click, logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });
    }

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
            binding.ConfirmBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    String name = binding.FNameTxt.getText().toString() + binding.LNameTxt.getText().toString();
                    String phone = binding.PhoneTxt.getText().toString();
                    DateFormat birthday = binding.BirthDateTxt.;
                }
            });
        }
    }
}