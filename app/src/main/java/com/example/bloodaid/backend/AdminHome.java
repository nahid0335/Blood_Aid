package com.example.bloodaid.backend;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.MainActivity;
import com.example.bloodaid.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void profile_cardView(View view) {
        AllToasts.successToast(AdminHome.this, "profile");
    }


    public void notification_imageView(View view) {
    }

    public void donor_cardView(View view) {
        startActivity(new Intent(AdminHome.this,DonorActivity.class));
        finish();
    }

    public void ambulence_cardView(View view) {
        startActivity(new Intent(AdminHome.this, AmbulanceAdmin.class));
        finish();
    }

    public void hospital_cardView(View view) {
        startActivity(new Intent(AdminHome.this,HospitalActivity.class));
        finish();
    }

    public void admin_cardView(View view) {
        startActivity(new Intent(AdminHome.this,AdminManage.class));
        finish();
    }

    public void organization_cardView(View view) {
        startActivity(new Intent(AdminHome.this,OrganizationActivity.class));
        finish();
    }

    public void report_cardView(View view) {
        startActivity(new Intent(AdminHome.this,ReportActivity.class));
        finish();
    }


    public void userMode_imageView(View view) {
        startActivity(new Intent(AdminHome.this, MainActivity.class));
        finish();
    }

    public void deleteAccount_imageView(View view) {
    }

    public void logOut_imageView(View view) {
    }
}
