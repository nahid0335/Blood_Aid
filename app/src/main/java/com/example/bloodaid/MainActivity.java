package com.example.bloodaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.bloodaid.fragments.FeedFragment;
import com.example.bloodaid.fragments.HomeFragment;
import com.example.bloodaid.fragments.NotificationFragment;
import com.example.bloodaid.fragments.RequestFragment;
import com.example.bloodaid.fragments.SearchDialog;
import com.example.bloodaid.fragments.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    public static BottomNavigationView mBottomNav;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////
        FirebaseMessaging.getInstance().subscribeToTopic("bloodaid");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("TAG", msg);

                    }
                });



        //////

        init();

        mToolbar.setLogo(R.drawable.blood_aid_logo);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBottomNav.setOnNavigationItemSelectedListener(navListener);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_display, new HomeFragment()).commit();
            mBottomNav.getMenu().getItem(2).setChecked(true);
        }

    }

    private void init() {
        mBottomNav = findViewById(R.id.main_bottom_nav);
        mToolbar = findViewById(R.id.main_toolbar);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_feed:
                    selectedFragment = new FeedFragment();
                    break;
                case R.id.nav_request:
                    selectedFragment = new RequestFragment();
                    break;
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    SearchDialog searchDialog = new SearchDialog();
                    searchDialog.show(getSupportFragmentManager(), "Hello");
                    return true;
                case R.id.nav_notifications:
                    selectedFragment = new NotificationFragment();
                    break;
                default:
                    AllToasts.errorToast(MainActivity.this,"Something Goes Wrong !");
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_display, selectedFragment).commit();
            return true;
        }
    };




    public void bloodGroupSearchClicked(View view){
        SearchDialog searchDialog = new SearchDialog();
        Bundle bundle = new Bundle();
        SearchFragment searchFragment = new SearchFragment();

        if(R.id.group_a_pos == view.getId()){
            searchDialog.show(getSupportFragmentManager(), "A+");
            bundle.putString("group", "A+");
            searchFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_display,searchFragment).commit();
        }
        else if(R.id.group_a_neg == view.getId()){
            searchDialog.show(getSupportFragmentManager(), "A-");
        }else if(R.id.group_ab_pos == view.getId()){
            searchDialog.show(getSupportFragmentManager(), "AB+");
        }else if(R.id.group_ab_neg == view.getId()){
            searchDialog.show(getSupportFragmentManager(), "AB-");
        }else if(R.id.group_o_pos == view.getId()){
            searchDialog.show(getSupportFragmentManager(), "O+");
        }else if(R.id.group_o_neg == view.getId()){
            searchDialog.show(getSupportFragmentManager(), "O-");
        }else if(R.id.group_b_pos == view.getId()){
            searchDialog.show(getSupportFragmentManager(), "B+");
        }else if(R.id.group_b_neg == view.getId()){
            searchDialog.show(getSupportFragmentManager(), "B-");
        }else {
            searchDialog.show(getSupportFragmentManager(), "NULL");
        }
    }

}
