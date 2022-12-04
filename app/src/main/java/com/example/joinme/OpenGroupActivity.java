package com.example.joinme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.joinme.databinding.ActivityMainPageBinding;
import com.example.joinme.databinding.ActivityOpenGroupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OpenGroupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // create array of Strings
    // and store name of courses
    String[] meetings = {"Category", "Minnian", "Football", "Basketball", "Groups game", "Volunteer"};
    //todo: check that "category" not chosen.
    //view binding
    private ActivityOpenGroupBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;
    String date;
    int hour, minute;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
//        checkUser();

        //handle click, logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
//                checkUser();
            }
        });
        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpenGroupActivity.this, MainPageActivity.class));
            }
        });


        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        Spinner spino = findViewById(R.id.spinner);
        spino.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of meetings
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                meetings);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spino.setAdapter(ad);

        //get date of the meeting
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        binding.selectDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(OpenGroupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month+1;
                        date = dayOfMonth+"/"+month+"/"+year;
                        binding.selectDateEt.setText(date);

                    }
                },year, month,day);
                dialog.show();
            }
        });
        binding.SelectTimeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        binding.SelectTimeEdt.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                        }
                    };
                    TimePickerDialog timePicker = new TimePickerDialog(v.getContext(), onTimeSetListener, hour, minute, true);
                    timePicker.setTitle("Select Time");
                    timePicker.show();
            }
        });
        binding.CreateBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //get all the details
                String city = binding.cityTxt.getText().toString();
                String time = binding.SelectTimeEdt.getText().toString();
                String date = binding.selectDateEt.getText().toString();
                int min = Integer.parseInt(binding.minParticipentsTxt.getText().toString());
                int max = Integer.parseInt(binding.maxParticipentsTxt.getText().toString());
                FirebaseUser head = firebaseAuth.getCurrentUser(); //todo: how change this to User object
                Group currGroup = new Group(title, city, time, date, head, min, max);

                //add group to database
                db.collection("groups").add(currGroup);

                startActivity(new Intent(OpenGroupActivity.this, MainPageActivity.class));
                // todo: maybe change the next page
                finish();

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        title = meetings[i];
        if(title.equals("Category")){
            onNothingSelected(adapterView); //todo:check this
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(),
                        "please choose category",
                        Toast.LENGTH_LONG)
                .show();
    }
}

//}