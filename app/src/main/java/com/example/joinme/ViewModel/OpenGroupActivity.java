package com.example.joinme.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.joinme.Model.Category;
import com.example.joinme.Model.api.RetrofitClient;
import com.example.joinme.R;
import com.example.joinme.databinding.ActivityOpenGroupBinding;
import com.example.joinme.Model.Group;
import com.example.joinme.Model.Logic;
import com.example.joinme.Model.User;
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
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenGroupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Logic logic = new Logic();
    // create array of Strings of categories
    String[] categories;
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
        /** onCreate() is called when the when the activity is first created.
         *  @param  savedInstanceState –is a reference to the Bundle object that is passed to the onCreate method of each Android activity.
         *                             Activities have the ability, under special circumstances,to restore themselves to a previous state
         *                             using the data stored in this package.
         */

        super.onCreate(savedInstanceState);
        binding = ActivityOpenGroupBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot());                               //Set the activity content to an explicit view.
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpenGroupActivity.this, MainPageActivity.class));
            }
        });
        Call<ArrayList<Category>> call = RetrofitClient.getInstance().getAPI().getCategories();
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                categories = new String[response.body().size()+1];
                categories[0] = "Category";
                for(int i=1; i<=response.body().size(); i++){
                    categories[i]= response.body().get(i-1).getName();
                }
                Spinner spino = findViewById(R.id.spinner);
                spino.setOnItemSelectedListener(OpenGroupActivity.this);

                // Create the instance of ArrayAdapter
                // having the list of meetings
                ArrayAdapter ad = new ArrayAdapter(OpenGroupActivity.this, android.R.layout.simple_spinner_item,  categories);

                // set simple layout resource file
                // for each item of spinner
                ad.setDropDownViewResource(android.R.layout   .simple_spinner_dropdown_item);
                // Set the ArrayAdapter (ad) data on the
                // Spinner which binds data to spinner
                spino.setAdapter(ad);
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                    Log.d("fail", t.getMessage());
            }
        });

        //get date of the meeting
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        binding.selectDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * you can double-click on the date of birth and a pop-up calendar window will open
                 */
                DatePickerDialog dialog = new DatePickerDialog(OpenGroupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        date = dayOfMonth+"/"+month+"/"+year;
                        int [] groupDate = {dayOfMonth, month, year};
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

                String[] groupDate = date.split("/");
                int min, max;
                //check that all of the fields had been filled
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
                String [] today = new String[3];

                //if true it means the device running the app has Android SDK 26 or up
                //otherwise- the SDK version is lower than 26. (SDK 25 or lower)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    today = java.time.LocalDate.now().toString().split("-");
                }
                if(date.isEmpty() || !logic.checkDate(Integer.parseInt(today[0]), Integer.parseInt(today[1]), Integer.parseInt(today[2]), Integer.parseInt(groupDate[2]), Integer.parseInt(groupDate[1]), Integer.parseInt(groupDate[0]))){
                    binding.selectDateEt.setError("please enter legal date");
                    return;
                }
                if(time.isEmpty()){
                    binding.SelectTimeEdt.setError("please enter time");
                    return;
                }
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
                FirebaseUser head = firebaseAuth.getCurrentUser();
                String head_uid = head.getUid();
                //create group
                Call<ResponseBody> call = RetrofitClient.getInstance()
                        .getAPI().addGroup(title,city,time,date,head_uid,min,max);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        startActivity(new Intent(OpenGroupActivity.this, MainPageActivity.class));
                        finish();
                        Log.d("add group", "add group");
                        Toast.makeText(OpenGroupActivity.this, "Group created successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("add group", t.getMessage());
                    }
                });
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
                startActivity(new Intent(OpenGroupActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.subitem2:
                startActivity(new Intent(OpenGroupActivity.this, HistoryJoinedActivity.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(OpenGroupActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                startActivity(new Intent(OpenGroupActivity.this, MainActivity.class));
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        /**
         * Callback method to be invoked when an item in this view has been selected. This callback is invoked only when the newly selected position is different from the previously selected position or if there was no selected item.
         * Implementers can call getItemAtPosition(position) if they need to access the data associated with the selected item.
         * Parameters
         * parent
         * @param AdapterView: The AdapterView where the selection happened
         * @param view - View: The view within the AdapterView that was clicked
         * @param position - int: The position of the view in the adapter
         * @param id - long: The row id of the item that is selected
         */
        title = categories[i];
        if(title.equals("Category")){
            onNothingSelected(adapterView);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
