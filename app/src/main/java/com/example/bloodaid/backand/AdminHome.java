package com.example.bloodaid.backand;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodaid.ErrorToast;
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

    public void donor_cardView(View view) {
        startActivity(new Intent(AdminHome.this,DonorActivity.class));
        finish();
    }

    public void ambulence_cardView(View view) {
        ErrorToast.successToast(AdminHome.this, "Ambulence");
    }

    public void hospital_cardView(View view) {
        ErrorToast.successToast(AdminHome.this, "Hospital");
    }

    public void admin_cardView(View view) {
        ErrorToast.successToast(AdminHome.this, "Admin");
    }

    public void report_cardView(View view) {
        ErrorToast.successToast(AdminHome.this, "Report");
    }

    public void profile_cardView(View view) {
        ErrorToast.successToast(AdminHome.this, "profile");
    }

    public void logout_button(View view) {
        ErrorToast.successToast(AdminHome.this, "Logout");
    }
}
