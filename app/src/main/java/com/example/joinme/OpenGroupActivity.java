package com.example.joinme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.joinme.databinding.ActivityMainPageBinding;
import com.example.joinme.databinding.ActivityOpenGroupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OpenGroupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // create array of Strings of categories
    String[] meetings = {"Category", "Minnian", "Football", "Basketball", "Group games", "Volunteer", "Hang out"};
    //todo: check that "category" not chosen.
    //view binding
    private ActivityOpenGroupBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "OPEN_GROUP_TAG";
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
        //when time meeting text field clicks
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
        //when create button clicks
        binding.CreateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //get all the details
                String city = binding.cityTxt.getText().toString();
                String time = binding.SelectTimeEdt.getText().toString();
                String date = binding.selectDateEt.getText().toString();
                int min, max;
                //check that all of the fields had been filled
                try {
                    min = Integer.parseInt(binding.minParticipentsTxt.getText().toString());
                }catch(NumberFormatException e){
                    binding.minParticipentsTxt.setError("please enter minimum participants");
                    return;
                }
                try {
                    max = Integer.parseInt(binding.maxParticipentsTxt.getText().toString());
                }catch(NumberFormatException e){
                    binding.maxParticipentsTxt.setError("please enter maximum participants");
                    return;
                }
                if(min > max){
                    binding.minParticipentsTxt.setError("Minimum participants must be smaller then maximum participants");
                    return;
                }
                if(title.equals("Category")){
                    TextView errorText = (TextView)binding.spinner.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("please enter category");//changes the selected item text to this
                    return;
                }
                if(city.isEmpty()){
                    binding.cityTxt.setError("please enter the location");
                    return;
                }
                FirebaseUser head = firebaseAuth.getCurrentUser(); //todo: how change this to User object
                String head_uid = head.getUid();
                //create group
                Group currGroup = new Group(title, city, time, date, head_uid, min, max);
                currGroup.addParticipant(head_uid);
                //add group to database
                String gid = db.collection("groups").document().getId();
                db.collection("groups").document(gid).set(currGroup);
                addGroupToHeadDb(gid);
            }
        });
    }

    private void addGroupToHeadDb(String gid) {
        db.collection("usersById").document(firebaseAuth.getCurrentUser().getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                user.addGroup(gid);
                                db.collection("usersById").document(user.getUid()).set(user);

                                Toast.makeText(OpenGroupActivity.this, "Group created successfully", Toast.LENGTH_SHORT).show();
                                //move to the main page
                                startActivity(new Intent(OpenGroupActivity.this, MainPageActivity.class));
                                finish();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        title = meetings[i];
        if(title.equals("Category")){
            onNothingSelected(adapterView);
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