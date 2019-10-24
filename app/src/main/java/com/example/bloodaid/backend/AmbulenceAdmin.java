package com.example.bloodaid.backend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bloodaid.R;
import com.example.bloodaid.backend.fragments.AmbulenceListFragment;
import com.example.bloodaid.backend.fragments.AmbulenceRequestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AmbulenceAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulence_admin);

        BottomNavigationView DonorNavBer = findViewById(R.id.bottomNavigationView_adminAmbulence_navBer);
        DonorNavBer.setOnNavigationItemSelectedListener(navListener);
        DonorNavBer.setSelectedItemId(R.id.icon_adminMenu_list);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            loadFragment(new AmbulenceListFragment());
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.icon_adminMenu_home:
                    startActivity(new Intent(AmbulenceAdmin.this,AdminHome.class));
                    finish();
                case R.id.icon_adminMenu_list:
                    selectedFragment = new AmbulenceListFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.icon_adminMenu_request:
                    selectedFragment = new AmbulenceRequestFragment();
                    loadFragment(selectedFragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_adminAmbulence_show, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
