package com.example.joinme.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinme.Model.Category;
import com.example.joinme.Model.api.RetrofitClient;
import com.example.joinme.R;
import com.example.joinme.databinding.ActivityFindGroupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindGroupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{ ;
    // create array of Strings of categories
    String[] categories;
    //view binding
    private ActivityFindGroupBinding binding;
    private GoogleSignInClient mGoogleSignInClient;     //A client for interacting with the Google Sign In API.
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;              //Request code used to invoke sign in user interactions.
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** onCreate() is called when the when the activity is first created.
         *  @param  savedInstanceState –is a reference to the Bundle object that is passed to the onCreate method of each Android activity.
         *                             Activities have the ability, under special circumstances,to restore themselves to a previous state
         *                             using the data stored in this package.
         */
        super.onCreate(savedInstanceState);
        binding = ActivityFindGroupBinding.inflate(getLayoutInflater()); //Using this function this binding variable can be used to access GUI components.
        setContentView(binding.getRoot());                               //Set the activity content to an explicit view.
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindGroupActivity.this, MainPageActivity.class));
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
                spino.setOnItemSelectedListener(FindGroupActivity.this);

                // Create the instance of ArrayAdapter
                // having the list of meetings
                ArrayAdapter ad = new ArrayAdapter(FindGroupActivity.this, android.R.layout.simple_spinner_item,  categories);

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

        //when create button clicks
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user must choose a category
                if(title.equals("Category")){
                    TextView errorText = (TextView)binding.spinner.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Choose category");//changes the selected item text to this
                    return;
                }

                //get all the details
                String city = binding.cityTxt.getText().toString();
                Intent intent = new Intent(FindGroupActivity.this, RelevantGroupsActivity.class);
                intent.putExtra("Title", title);
                intent.putExtra("City", city);
                startActivity(intent);
             
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
                startActivity(new Intent(FindGroupActivity.this, HistoryCreatedActivity.class));
                return true;
            case R.id.subitem2:
                startActivity(new Intent(FindGroupActivity.this, HistoryJoinedActivity.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(FindGroupActivity.this, UpdateDetailsActivity.class));
                return true;
            case R.id.item3:
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                startActivity(new Intent(FindGroupActivity.this, MainActivity.class));
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
        title = categories[i];
        if (title.equals("Category")) {
            onNothingSelected(adapterView);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
