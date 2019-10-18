package com.example.bloodaid.backand;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bloodaid.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminLoginActivity extends AppCompatActivity {
    Button AdminLoginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        AdminLoginbtn =findViewById(R.id.Adminlogin_btn);

        AdminLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AdminLoginActivity.this,AdminHome.class));
            }
        });
    }
}
