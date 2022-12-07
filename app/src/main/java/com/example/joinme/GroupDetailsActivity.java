package com.example.joinme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.joinme.databinding.ActivityFillDetailsBinding;
import com.example.joinme.databinding.ActivityFindGroupBinding;
import com.example.joinme.databinding.ActivityGroupDetailsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GroupDetailsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityGroupDetailsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    public String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        String id = getIntent().getStringExtra("ID");
//        TextView title = binding.titleTxt;
//        title.setText(id);
        getDetailsFromDb(id);
//                , new FireStoreCallBack() {
//            @Override
//            public void onCallBack(String id) {
//                db.collection("users").document(id)
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                binding.headTxt.setText("The head of the group is: " + document.getString("name"));
//                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                            } else {
//                                Log.d(TAG, "No such document2");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with2 ", task.getException());
//                        }
//                    }
//                });
//            }
//        });
//        getUserFromDb();


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
                startActivity(new Intent(GroupDetailsActivity.this, RelevantGroupsActivity.class));
                //todo: check if the pass to the relevant groups page ok
            }
        });
    }

    private void getDetailsFromDb(String id) {
        db.collection("groups").document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                binding.titleTxt.setText(document.getString("title"));
                                binding.addressTxt.setText(document.getString("city"));
                                binding.dateTxt.setText(document.getString("date"));
                                binding.timeTxt.setText(document.getString("time"));
//                                binding.headTxt.setText(document.getString("head_of_group"));
                                uid = document.getString("head_of_group");
                                binding.numPartTxt.setText("Current number of participants in the group: " + document.get("num_of_participant").toString());
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                        if (task2.isSuccessful()) {
                                            DocumentSnapshot document2 = task2.getResult();
                                            if (document2.exists()) {
                                                binding.headTxt.setText("The head of the group is: " + document2.getString("name"));
                                            }
                                            else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task2.getException());
                                        }
                                    }
                                });
//                                callBack.onCallBack(uid);
//                                uid = document.getString("head");
//                                getUserFromDb();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
//                        binding.titleTxt.setText(task.getResult().getString("title"));
//                        binding.addressTxt.setText(task.getResult().getString("city"));
//                        binding.dateTxt.setText(task.getResult().getString("date"));
//                        binding.timeTxt.setText(task.getResult().getString("time"));
////                        binding.headTxt.setText("The head of the group is: " +task.getResult().getString("head"));
//                        binding.numPartTxt.setText("Current number of participants in the group: " + task.getResult().getString("num_of_participant"));
                    }
                });
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Group group = documentSnapshot.toObject(Group.class);
//                        binding.titleTxt.setText(group.getTitle());
//                        binding.addressTxt.setText(group.getCity());
//                        binding.dateTxt.setText(group.getDate());
//                        binding.timeTxt.setText(group.getTime());
//                        binding.headTxt.setText("The head of the group is: "+group.getHead_of_group());
//                        binding.numPartTxt.setText("Current number of participants in the group: "+ group.getNum_of_participant());
//                    }
//                });

    }

//    private void getUserFromDb() {
////        String uid = binding.headTxt.getText().toString();
//        db.collection("groups").document(binding.headTxt.getText().toString())
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                binding.headTxt.setText("The head of the group is: " +document.getString("name"));
//                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                            } else {
//                                Log.d(TAG, "No such document");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                        }
//                        binding.titleTxt.setText(task.getResult().getString("title"));
//                        binding.addressTxt.setText(task.getResult().getString("city"));
//                        binding.dateTxt.setText(task.getResult().getString("date"));
//                        binding.timeTxt.setText(task.getResult().getString("time"));
////                        binding.headTxt.setText("The head of the group is: " +task.getResult().getString("head"));
//                        binding.numPartTxt.setText("Current number of participants in the group: " + task.getResult().getString("num_of_participant"));
//                    }
//                });
//    }

//    private interface FireStoreCallBack{
//        void onCallBack(String uid);
//    }

//    private void getUserFromDb(uid) {
//        db.collection("users").document(uid)
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                binding.headTxt.setText("The head of the group is: " + document.getString("name"));
//                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                            } else {
//                                Log.d(TAG, "No such document");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                        }
//                    }
//                });
//    }
}