package com.example.bloodaid.adminend;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.R;
import android.os.Bundle;
import android.view.View;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void donor_cardView(View view) {
        AllToasts.successToast(AdminHome.this, "Donor");
    }

    public void ambulence_cardView(View view) {
        AllToasts.successToast(AdminHome.this, "Ambulence");
    }

    public void hospital_cardView(View view) {
        AllToasts.successToast(AdminHome.this, "Hospital");
    }

    public void admin_cardView(View view) {
        AllToasts.successToast(AdminHome.this, "Admin");
    }

    public void report_cardView(View view) {
        AllToasts.successToast(AdminHome.this, "Report");
    }

    public void profile_cardView(View view) {
        AllToasts.successToast(AdminHome.this, "profile");
    }

    public void logout_button(View view) {
        AllToasts.successToast(AdminHome.this, "Logout");
    }
}
