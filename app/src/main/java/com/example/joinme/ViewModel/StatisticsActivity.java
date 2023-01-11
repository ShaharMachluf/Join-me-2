package com.example.joinme.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.joinme.R;
import com.example.joinme.databinding.ActivityStatisticsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.jjoe64.graphview.GraphView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //view binding
    private ActivityStatisticsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "STATISTICS_TAG";
    PieChart pieChart;
    public ArrayList<BarEntry> barArrayList;
    BarChart barChart;
    BarChart hBarChart;
    ArrayList<BarEntry> hBarArrayList;
    String[] hBarUsers;
    private final String[] categories = {"Minnian", "Football", "Basketball", "Group games", "Volunteer", "Hang out"};
    private final String[] colors = {"#FFBB86FC", "#00FFFF", "#0000FF", "#00FF00", "#800000", "#FFFF00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        pieChart = findViewById(R.id.piechart);
        barChart = findViewById(R.id.barChart);
        barArrayList = new ArrayList<>();
        hBarArrayList = new ArrayList<>();
        hBarChart = findViewById(R.id.fragment_horizontalbarchart_chart);
        hBarUsers = new String[3];

        try {
            createPieChart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            createBarChart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        createHorizontalBarChart();

        //handle click, back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsActivity.this, AdminMainPageActivity.class));
            }
        });
    }

    private void createPieChart() throws InterruptedException {
        //send each category to countCategory
        for(int i=0; i < categories.length; ++i){
            countCategory(categories[i], colors[i]);
        }

        Thread.sleep(3000);
        // To animate the pie chart
        pieChart.startAnimation();
    }

    private void countCategory(String category, String color){
        /**
         * for each category, count the number of groups from it and add to the pie chart
         */
        //extract from db
        CollectionReference collection = db.collection("groups");
        Query query = collection.whereEqualTo("title", category);
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                Log.d(TAG, "Count: " + snapshot.getCount());
                //add to pie chart
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

    private void createBarChart() throws InterruptedException {
        //send to recursive function that creates the bar chart
        getBarData(0, 0);
    }

    private void getBarData(int i, int j) throws InterruptedException {
        if(i == categories.length){//stop condition
            Log.d(TAG, "bar Count: " + barArrayList.toString());
            //create bar chart
            BarDataSet barDataSet = new BarDataSet(barArrayList, "Compare groups that happened and didn't happen");
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.invalidate();
            barDataSet.setColors(Color.BLUE, Color.CYAN, Color.BLUE, Color.CYAN,Color.BLUE, Color.CYAN,Color.BLUE, Color.CYAN,Color.BLUE, Color.CYAN,Color.BLUE, Color.CYAN);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setStackLabels(categories);
            barDataSet.setValueTextSize(10f);

            //set column names
            XAxis xAxis = barChart.getXAxis();
            xAxis.setTextSize(9f);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return categories[(int) value];
                }
            });
            barChart.getDescription().setEnabled(true);
        }
        else{
            CollectionReference collection = db.collection("groups");
            if(j % 2 == 0){//general
                //extract from db
                Query query = collection.whereEqualTo("title", categories[i]);
                AggregateQuery countQuery = query.count();
                countQuery.get(AggregateSource.SERVER)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                AggregateQuerySnapshot snapshot = task.getResult();
                                Log.d(TAG, "bar Count: " + snapshot.getCount());
                                //add to bar chart
                                barArrayList.add(new BarEntry(i, snapshot.getCount()));
                                try {
                                    getBarData(i, j+1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.d(TAG, "Count failed: ", task.getException());
                            }
                        });
            }else{//happened
                //extract from db
                Query query = collection.whereEqualTo("title", categories[i]).whereEqualTo("is_happened", true);
                AggregateQuery countQuery = query.count();
                countQuery.get(AggregateSource.SERVER)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                AggregateQuerySnapshot snapshot = task.getResult();
                                Log.d(TAG, "bar Count: " + snapshot.getCount());
                                //add to bar chart
                                barArrayList.add(new BarEntry(i, snapshot.getCount()));
                                try {
                                    getBarData(i+1, j+1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.d(TAG, "Count failed: ", task.getException());
                            }
                        });
            }
        }
    }

    private void createHorizontalBarChart() {
        /**
         * takes the number of groups the top 3 users opened and present it in horizontal bar chart
         */
        //extract from db
        db.collection("usersById").orderBy("my_groups", Query.Direction.DESCENDING).limit(3).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                hBarUsers[i] = document.getString("name");
                                //add to bar chart
                                List<String> myGroups = (List<String>)document.get("my_groups");
                                hBarArrayList.add(new BarEntry(i, myGroups.size()));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                i++;
                            }

                            //create bar chart
                            BarDataSet barDataSet = new BarDataSet(hBarArrayList, "Top 3 users by number of groups the opened");
                            BarData barData = new BarData(barDataSet);
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(10f);

                            //set column names
                            XAxis xAxis = hBarChart.getXAxis();
                            xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(hBarUsers));

                            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                            dataSets.add(barDataSet);
                            hBarChart.getDescription().setEnabled(true);

                            hBarChart.setData(barData);
                            hBarChart.invalidate();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}