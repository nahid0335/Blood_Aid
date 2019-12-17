package com.example.bloodaid.backend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bloodaid.R;
import com.example.bloodaid.backend.fragments.DonorListFragment;
import com.example.bloodaid.backend.fragments.DonorRequestFragment;
import com.example.bloodaid.backend.fragments.ReportAmbulanceFragment;
import com.example.bloodaid.backend.fragments.ReportDonorFragment;
import com.example.bloodaid.backend.fragments.ReportHospitalFragment;
import com.example.bloodaid.backend.fragments.ReportOrganizationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        BottomNavigationView ReportNavBer = findViewById(R.id.bottomNavigationView_adminReport_navBer);
        ReportNavBer.setOnNavigationItemSelectedListener(navListener);
        ReportNavBer.setSelectedItemId(R.id.icon_adminReport_donor);

        if(getIntent().getIntExtra("ReportDonorActivity",0)==16)
        {
            loadFragment(new ReportDonorFragment());
            ReportNavBer.setSelectedItemId(R.id.icon_adminReport_donor);
        }else if(getIntent().getIntExtra("ReportHospitalActivity",0)==17){
            loadFragment(new ReportHospitalFragment());
            ReportNavBer.setSelectedItemId(R.id.icon_adminReport_hospital);
        }else if(getIntent().getIntExtra("ReportAmbulanceActivity",0)==18){
            loadFragment(new ReportAmbulanceFragment());
            ReportNavBer.setSelectedItemId(R.id.icon_adminReport_ambulance);
        }else if(getIntent().getIntExtra("ReportOrganizationActivity",0)==18){
            loadFragment(new ReportOrganizationFragment());
            ReportNavBer.setSelectedItemId(R.id.icon_adminReport_organization);
        }else if (savedInstanceState == null) {
            loadFragment(new DonorListFragment());
        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.icon_adminReport_home:
                    startActivity(new Intent(ReportActivity.this,AdminHome.class));
                    finish();
                case R.id.icon_adminReport_donor:
                    selectedFragment = new ReportDonorFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.icon_adminReport_ambulance:
                    selectedFragment = new ReportAmbulanceFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.icon_adminReport_hospital:
                    selectedFragment = new ReportHospitalFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.icon_adminReport_organization:
                    selectedFragment = new ReportOrganizationFragment();
                    loadFragment(selectedFragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_adminReport_show, fragment).addToBackStack(null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReportActivity.this,AdminHome.class));
        finish();
    }
    

}
