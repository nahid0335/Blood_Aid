package com.example.bloodaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.bloodaid.fragments.FeedFragment;
import com.example.bloodaid.fragments.HomeFragment;
import com.example.bloodaid.fragments.NotificationFragment;
import com.example.bloodaid.fragments.RequestFragment;
import com.example.bloodaid.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView mProfilePic;
    BottomNavigationView mBottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        profileWork();

        mBottomNav.setOnNavigationItemSelectedListener(navListener);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_display, new HomeFragment()).commit();
            mBottomNav.getMenu().getItem(2).setChecked(true);
        }

    }

    private void init() {
        mProfilePic = findViewById(R.id.profile_image);
        mBottomNav = findViewById(R.id.main_bottom_nav);
    }

    private void profileWork() {

        Picasso.get().load("file:///android_asset/images/profile_pic.jpg").into(mProfilePic);
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
                    selectedFragment = new SearchFragment();
                    break;
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
}
