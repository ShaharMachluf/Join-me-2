package com.example.joinme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.joinme.databinding.ActivityFindGroupBinding;
import com.example.joinme.databinding.ActivityStatisticsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;

public class StatisticsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //view binding
    private ActivityStatisticsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "STATISTICS_TAG";
    PieChart pieChart;
    HashMap <String, Long> forPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        pieChart = findViewById(R.id.piechart);
//        forPieChart = new HashMap<String, String>();

        try {
            createPieChart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //handle click, logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsActivity.this, AdminMainPageActivity.class));
            }
        });
    }

    private void createPieChart() throws InterruptedException {
        countCategory("Minnian", "#FFBB86FC");
        countCategory("Football", "#00FFFF");
        countCategory("Basketball", "#0000FF");
        countCategory("Group games", "#00FF00");
        countCategory("Volunteer", "#800000");
        countCategory("Hang out", "#FFFF00");

        Thread.sleep(3000);
        // To animate the pie chart
        pieChart.startAnimation();
    }

    private void countCategory(String category, String color){
        CollectionReference collection = db.collection("groups");
        Query query = collection.whereEqualTo("title", category);
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                Log.d(TAG, "Count: " + snapshot.getCount());
                pieChart.addPieSlice(
                        new PieModel(
                                category,
                                snapshot.getCount(),
                                Color.parseColor(color)));
            } else {
                Log.d(TAG, "Count failed: ", task.getException());
            }
        });
    }
}