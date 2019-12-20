package com.example.bloodaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodaid.adapters.InformationsAdapter;
import com.example.bloodaid.fragments.AppInfoFragment;
import com.example.bloodaid.fragments.DonorAddFragment;
import com.example.bloodaid.fragments.FactsFragment;
import com.example.bloodaid.fragments.FeedFragment;
import com.example.bloodaid.fragments.HistoryFragment;
import com.example.bloodaid.fragments.HomeFragment;
import com.example.bloodaid.fragments.RequestFragment;
import com.example.bloodaid.fragments.SearchDialog;
import com.example.bloodaid.fragments.SearchFragment;
import com.example.bloodaid.fragments.TopDonorFragment;
import com.example.bloodaid.models.UserModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity{
    public static BottomNavigationView mBottomNav;
    public static TextView UserName;



    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_ID = "user_id";
    public static final String USER_DATA = "user_data";
    public static final String TOKEN_DATA = "token_data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        if(!sharedPreferences.contains(TOKEN_DATA) &&
                sharedPreferences.getString(TOKEN_DATA,null).equals("token_saved")){






            //store token
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TOKEN_DATA, "token_saved");
            editor.apply();

        }


        //////

        init();

        mBottomNav.setOnNavigationItemSelectedListener(navListener);

        if(getIntent().getIntExtra("TransferActivity",0)==1)
        {
            Fragment donorAddFragment = new DonorAddFragment();
            loadFragment(donorAddFragment);
        }else if(getIntent().getIntExtra("BackPressActivity",0)==2){

            Fragment homeFragment = new HomeFragment();
            replaceFragments(homeFragment);
        }
        else if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_display, new HomeFragment()).commit();
            mBottomNav.getMenu().getItem(2).setChecked(true);
        }

    }

    private void init() {
        mBottomNav = findViewById(R.id.main_bottom_nav);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_feed:
                    selectedFragment = new FeedFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.nav_request:
                    selectedFragment = new RequestFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.nav_home:
                    selectedFragment = new HomeFragment(MainActivity.this);
                    loadFragment(selectedFragment);
                    return true;
                case R.id.nav_search:
                    /*SearchDialog searchDialog = new SearchDialog();
                    searchDialog.show(getSupportFragmentManager(), "Hello");
                    return true;*/
                    selectedFragment = new SearchFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.nav_logout:
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                default:
                    AllToasts.errorToast(MainActivity.this,"Something Goes Wrong !");
                    break;
            }
            return false;
        }
    };



    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_display, fragment).addToBackStack(null);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    public void bloodGroupSearchClicked(View view){
        SearchDialog searchDialog = new SearchDialog();
        Bundle bundle = new Bundle();
        SearchFragment searchFragment = new SearchFragment();

        if(R.id.group_a_pos == view.getId()){
            bundle.putString("bloodgroup", "A+");
            searchDialog.setArguments(bundle);
            searchDialog.show(getSupportFragmentManager(), "A+");
        }
        else if(R.id.group_a_neg == view.getId()){
            bundle.putString("bloodgroup", "A-");
            searchDialog.setArguments(bundle);
            searchDialog.show(getSupportFragmentManager(), "A-");
        }
        else if(R.id.group_ab_pos == view.getId()){
            bundle.putString("bloodgroup", "AB+");
            searchDialog.setArguments(bundle);
            searchDialog.show(getSupportFragmentManager(), "AB+");
        }
        else if(R.id.group_ab_neg == view.getId()){
            bundle.putString("bloodgroup", "AB-");
            searchDialog.setArguments(bundle);
            searchDialog.show(getSupportFragmentManager(), "AB-");
        }
        else if(R.id.group_o_pos == view.getId()){
            bundle.putString("bloodgroup", "O+");
            searchDialog.setArguments(bundle);
            searchDialog.show(getSupportFragmentManager(), "O+");
        }
        else if(R.id.group_o_neg == view.getId()){
            bundle.putString("bloodgroup", "O-");
            searchDialog.setArguments(bundle);
            searchDialog.show(getSupportFragmentManager(), "O-");
        }
        else if(R.id.group_b_pos == view.getId()){
            bundle.putString("bloodgroup", "B+");
            searchDialog.setArguments(bundle);
            searchDialog.show(getSupportFragmentManager(), "B+");
        }
        else if(R.id.group_b_neg == view.getId()){
            bundle.putString("bloodgroup", "B-");
            searchDialog.setArguments(bundle);
            searchDialog.show(getSupportFragmentManager(), "B-");
        }
        else {
            bundle.putString("bloodgroup", "Choose Blood Group");
            searchDialog.setArguments(bundle);
            searchDialog.show(getSupportFragmentManager(), "NULL");
        }
    }


    public void replaceFragments(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_display, fragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }


}
