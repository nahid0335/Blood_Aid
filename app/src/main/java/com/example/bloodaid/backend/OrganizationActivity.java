package com.example.bloodaid.backend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bloodaid.R;
import com.example.bloodaid.backend.fragments.HospitalListFragment;
import com.example.bloodaid.backend.fragments.HospitalRequestFragment;
import com.example.bloodaid.backend.fragments.OrganizationListFragment;
import com.example.bloodaid.backend.fragments.OrganizationRequestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrganizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        BottomNavigationView DonorNavBer = findViewById(R.id.bottomNavigationView_adminOrganization_navBer);
        DonorNavBer.setOnNavigationItemSelectedListener(navListener);
        DonorNavBer.setSelectedItemId(R.id.icon_adminMenu_list);


        if(getIntent().getIntExtra("OrganizationActivity",0)==14)
        {
            loadFragment(new OrganizationRequestFragment());
            DonorNavBer.setSelectedItemId(R.id.icon_adminMenu_request);
        }else if (savedInstanceState == null) {
            loadFragment(new OrganizationListFragment());
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.icon_adminMenu_home:
                    startActivity(new Intent(OrganizationActivity.this,AdminHome.class));
                    finish();
                case R.id.icon_adminMenu_list:
                    selectedFragment = new OrganizationListFragment();
                    loadFragment(selectedFragment);
                    return true;
                case R.id.icon_adminMenu_request:
                    selectedFragment = new OrganizationRequestFragment();
                    loadFragment(selectedFragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_adminOrganization_show, fragment).addToBackStack(null);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(OrganizationActivity.this,AdminHome.class));
        finish();
    }

}
