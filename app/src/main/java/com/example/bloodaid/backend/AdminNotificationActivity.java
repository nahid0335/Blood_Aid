package com.example.bloodaid.backend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AdminNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);

        final TextView donorrequest = findViewById(R.id.textView_adminNotification_donorNumber);
        final TextView hospitalrequest = findViewById(R.id.textView_adminNotification_hospitalNumber);
        final TextView ambulancerequset = findViewById(R.id.textView_adminNotification_ambulanceNumber);
        final TextView organizationrequest = findViewById(R.id.textView_adminNotification_organizationNumber);
        final TextView adminrequest = findViewById(R.id.textView_adminNotification_adminNumber);
        final TextView donorreport = findViewById(R.id.textView_adminNotification_reportDonorNumber);
        final TextView hospitalreport = findViewById(R.id.textView_adminNotification_reportHospitalNumber);
        final TextView ambulancereport = findViewById(R.id.textView_adminNotification_reportAmbulanceNumber);
        final TextView organizationreport = findViewById(R.id.textView_adminNotification_reportOrganizationNumber);


        final CardView donor = findViewById(R.id.cardView_adminNotification_donor);
        final CardView hospital = findViewById(R.id.cardView_adminNotification_hospital);
        final CardView ambulance = findViewById(R.id.cardView_adminNotification_ambulance);
        final CardView organization = findViewById(R.id.cardView_adminNotification_organization);
        final CardView admin = findViewById(R.id.cardView_adminNotification_admin);
        final CardView reportdonor = findViewById(R.id.cardView_adminNotification_reportDonor);
        final CardView reporthospital = findViewById(R.id.cardView_adminNotification_reportHospital);
        final CardView reportambulance = findViewById(R.id.cardView_adminNotification_reportAmbulance);
        final CardView reportorganization = findViewById(R.id.cardView_adminNotification_reportOrganization);


        donor.setVisibility(View.GONE);
        donorrequest.setVisibility(View.GONE);

        hospital.setVisibility(View.GONE);
        hospitalrequest.setVisibility(View.GONE);

        ambulance.setVisibility(View.GONE);
        ambulancerequset.setVisibility(View.GONE);

        organization.setVisibility(View.GONE);
        organizationrequest.setVisibility(View.GONE);

        admin.setVisibility(View.GONE);
        adminrequest.setVisibility(View.GONE);

        reportdonor.setVisibility(View.GONE);
        donorreport.setVisibility(View.GONE);

        reporthospital.setVisibility(View.GONE);
        hospitalreport.setVisibility(View.GONE);

        reportambulance.setVisibility(View.GONE);
        ambulancereport.setVisibility(View.GONE);

        reportorganization.setVisibility(View.GONE);
        organizationreport.setVisibility(View.GONE);


        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .countNotification();

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if(!response.isSuccessful()){
                            AllToasts.errorToast(AdminNotificationActivity.this,"Code : "+response.code()+" .");
                        }
                        else{
                            try {
                                String s = response.body().string();

                                JSONObject object = new JSONObject(s);
                                int donorcount = object.getInt("DonorRequest");
                                if(donorcount>0){
                                    donor.setVisibility(View.VISIBLE);
                                    donorrequest.setVisibility(View.VISIBLE);
                                    donorrequest.setText(String.valueOf(donorcount));
                                }

                                int hospitalcount = object.getInt("HospitalRequest");
                                if(hospitalcount>0){
                                    hospital.setVisibility(View.VISIBLE);
                                    hospitalrequest.setVisibility(View.VISIBLE);
                                    hospitalrequest.setText(String.valueOf(hospitalcount));
                                }

                                int ambulancecount = object.getInt("AmbulanceRequest");
                                if(ambulancecount>0){
                                    ambulance.setVisibility(View.VISIBLE);
                                    ambulancerequset.setVisibility(View.VISIBLE);
                                    ambulancerequset.setText(String.valueOf(ambulancecount));
                                }

                                int organizationcount = object.getInt("OrganizationRequest");
                                if(organizationcount>0){
                                    organization.setVisibility(View.VISIBLE);
                                    organizationrequest.setVisibility(View.VISIBLE);
                                    organizationrequest.setText(String.valueOf(organizationcount));
                                }

                                int admincount = object.getInt("AdminRequest");
                                if(admincount>0){
                                    admin.setVisibility(View.VISIBLE);
                                    adminrequest.setVisibility(View.VISIBLE);
                                    adminrequest.setText(String.valueOf(admincount));
                                }

                                int donorreportcount = object.getInt("ReportDonor");
                                if(donorreportcount>0){
                                    reportdonor.setVisibility(View.VISIBLE);
                                    donorreport.setVisibility(View.VISIBLE);
                                    donorreport.setText(String.valueOf(donorreportcount));
                                }

                                int hospitalreportcount = object.getInt("ReportHospital");
                                if(hospitalreportcount>0){
                                    reporthospital.setVisibility(View.VISIBLE);
                                    hospitalreport.setVisibility(View.VISIBLE);
                                    hospitalreport.setText(String.valueOf(hospitalreportcount));
                                }

                                int ambulancereportcount = object.getInt("ReportAmbulance");
                                if(ambulancereportcount>0){
                                    reportambulance.setVisibility(View.VISIBLE);
                                    ambulancereport.setVisibility(View.VISIBLE);
                                    ambulancereport.setText(String.valueOf(ambulancereportcount));
                                }

                                int organizationreportcount = object.getInt("ReportOrganization");
                                if(organizationreportcount>0){
                                    reportorganization.setVisibility(View.VISIBLE);
                                    organizationreport.setVisibility(View.VISIBLE);
                                    organizationreport.setText(String.valueOf(organizationreportcount));
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(AdminNotificationActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        donor.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(AdminNotificationActivity.this,DonorActivity.class);
                 intent.putExtra("DonorActivity",11);
                 startActivity(intent);
                 finish();
             }
        });


        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNotificationActivity.this,HospitalActivity.class);
                intent.putExtra("HospitalActivity",12);
                startActivity(intent);
                finish();
            }
        });


        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNotificationActivity.this,AmbulanceAdmin.class);
                intent.putExtra("AmbulanceActivity",13);
                startActivity(intent);
                finish();
            }
        });


        organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNotificationActivity.this,OrganizationActivity.class);
                intent.putExtra("OrganizationActivity",14);
                startActivity(intent);
                finish();
            }
        });


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNotificationActivity.this,AdminManage.class);
                intent.putExtra("AdminActivity",15);
                startActivity(intent);
                finish();
            }
        });


        reportdonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNotificationActivity.this,ReportActivity.class);
                intent.putExtra("ReportDonorActivity",16);
                startActivity(intent);
                finish();
            }
        });


        reporthospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNotificationActivity.this,ReportActivity.class);
                intent.putExtra("ReportHospitalActivity",17);
                startActivity(intent);
                finish();
            }
        });


        reportambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNotificationActivity.this,ReportActivity.class);
                intent.putExtra("ReportAmbulanceActivity",18);
                startActivity(intent);
                finish();
            }
        });


        reportorganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNotificationActivity.this,ReportActivity.class);
                intent.putExtra("ReportOrganizationActivity",19);
                startActivity(intent);
                finish();
            }
        });
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminNotificationActivity.this,AdminHome.class));
        finish();
    }
}
