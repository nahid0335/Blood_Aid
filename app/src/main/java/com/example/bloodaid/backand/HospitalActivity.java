package com.example.bloodaid.backand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bloodaid.ErrorToast;
import com.example.bloodaid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


public class HospitalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);


        BottomNavigationView DonorNavBer = findViewById(R.id.bottomNavigationView_adminHospital_navBer);
        DonorNavBer.setOnNavigationItemSelectedListener(navListener);
        DonorNavBer.setSelectedItemId(R.id.icon_adminMenu_list);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            loadFragment(new HospitalListFragment());
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.icon_adminMenu_home:
                    startActivity(new Intent(HospitalActivity.this,AdminHome.class));
                    finish();
                case R.id.icon_adminMenu_list:
                    selectedFragment = new HospitalListFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.icon_adminMenu_request:
                    selectedFragment = new HospitalRequestFragment();
                    loadFragment(selectedFragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_adminHospital_show, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
