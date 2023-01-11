package com.example.joinme.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinme.Model.Category;
import com.example.joinme.Model.Logic;
import com.example.joinme.Model.api.RetrofitClient;
import com.example.joinme.R;
import com.example.joinme.databinding.ActivityAdminMainPageBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMainPageActivity extends AppCompatActivity {
    //view binding
    private ActivityAdminMainPageBinding binding;
    private GoogleSignInClient mGoogleSignInClient;   //A client for interacting with the Google Sign In API.
    private static final int RC_SIGN_IN = 100;        //Request code used to invoke sign in user interactions.
    private FirebaseAuth firebaseAuth;                //The entry point of the Firebase Authentication SDK.
    androidx.constraintlayout.widget.ConstraintLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         /** onCreate() is called when the when the activity is first created.
         *  @param  savedInstanceState –is a reference to the Bundle object that is passed to the onCreate method of each Android activity.
         *                             Activities have the ability, under special circumstances,to restore themselves to a previous state
         *                             using the data stored in this package.
         */
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, MainActivity.googleSignInOptions);
        checkUser();
        parent = findViewById(R.id.admin_page);
        binding.blockUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainPageActivity.this, DeleteUserActivity.class));
            }
        });

        binding.statisticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainPageActivity.this, StatisticsActivity.class));
            }
        });
    }

    // menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                onButtonShowPopupWindowClick();
                return true;
            case R.id.item2:
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
                startActivity(new Intent(AdminMainPageActivity.this, MainActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    public void onButtonShowPopupWindowClick(){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //Inflate a new view hierarchy from the specified xml resource.
        View popupView = inflater.inflate(R.layout.add_category_popup, null);
        //confirm the deletion of the user
        EditText tvCategory = popupView.findViewById(R.id.add_categoryTxt);
        Button addBtn = popupView.findViewById(R.id.addBtn);
        Button xBtn = popupView.findViewById(R.id.xBtn);


        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it

        //This class represents a popup window that can be used to display an arbitrary view.
        //The popup window is a floating container that appears on top of the current activity.
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            xBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
            addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = tvCategory.getText().toString();
                if(category.isEmpty()){
                    tvCategory.setError("please enter category");
                }
                else{
                    Call<ArrayList<Category>> call = RetrofitClient.getInstance().getAPI().getCategories();
                    call.enqueue(new Callback<ArrayList<Category>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                            for(int i=0; i<response.body().size(); i++){
                                Log.d("category", response.body().get(i).getName());
                                if(category.equals(response.body().get(i).getName())){
                                    Log.d("in", "hi");
                                    tvCategory.setError("this category already exist");
                                    return;
                                }
                            }
                            Call<ResponseBody> call2 = RetrofitClient.getInstance().getAPI().addCategory(category);
                            call2.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.d("add", "add category");
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d("fail", t.getMessage());
                                }
                            });
                            popupWindow.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                            Log.d("fail", t.getMessage());
                        }
                    });
                }
            }
        });
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    private void checkUser() {
         /**
         This function checks the current user, if it is null then the user is not logged in and therefore he will have to log in.
         Note: CurrentUser may also return null because the authentication object has not finished initializing.
         */
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //user not logged in
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else{
            //user logged in
            //get user info
            String email = firebaseUser.getEmail();
            //set email
            binding.emailTv.setText("Admin");
        }
    }
}
