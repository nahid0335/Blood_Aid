package com.example.bloodaid.backend;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.LoginActivity;
import com.example.bloodaid.MainActivity;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.models.UserModelClass;
import com.google.gson.Gson;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AdminHome extends AppCompatActivity {
    TextView adminName,notificationCount;

    Dialog dialog;


    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_DATA = "user_data";
    public static final String ADMIN_LOGIN = "admin_login";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        adminName = findViewById(R.id.textView_adminHome_adminName);
        notificationCount = findViewById(R.id.textView_adminHome_notificationNumber);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        Gson gson = new Gson();
        if(sharedPreferences.contains(USER_DATA)){
            String json = sharedPreferences.getString(USER_DATA,null);
            UserModelClass userDetails = gson.fromJson(json,UserModelClass.class);
            adminName.setText(userDetails.getName());
        }

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
                            AllToasts.errorToast(AdminHome.this,"Code : "+response.code()+" .");
                        }
                        else{
                            int sum = 0;
                            try {
                                String s = response.body().string();

                                JSONObject object = new JSONObject(s);
                                int donorcount = object.getInt("DonorRequest");
                                sum += donorcount;

                                int hospitalcount = object.getInt("HospitalRequest");
                                sum += hospitalcount;

                                int ambulancecount = object.getInt("AmbulanceRequest");
                                sum += ambulancecount;

                                int organizationcount = object.getInt("OrganizationRequest");
                                sum += organizationcount;

                                int admincount = object.getInt("AdminRequest");
                                sum += admincount;

                                int donorreportcount = object.getInt("ReportDonor");
                                sum += donorreportcount;

                                int hospitalreportcount = object.getInt("ReportHospital");
                                sum += hospitalreportcount;

                                int ambulancereportcount = object.getInt("ReportAmbulance");
                                sum += ambulancereportcount;

                                int organizationreportcount = object.getInt("ReportOrganization");
                                sum += organizationreportcount;

                                notificationCount.setText(String.valueOf(sum));

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(AdminHome.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
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


    }


    public void notification_imageView(View view) {
        startActivity(new Intent(AdminHome.this,AdminNotificationActivity.class));
        finish();
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
        dialog = new Dialog(AdminHome.this);
        dialog.setContentView(R.layout.popup_negative);
        ImageView closepopupimg = dialog.findViewById(R.id.imageView_popupNegative_close);
        Button deletebtn = dialog.findViewById(R.id.button_popupNegative_delete);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        closepopupimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
                final Gson gson = new Gson();
                String json = sharedPreferences.getString(USER_DATA,null);
                final UserModelClass userDetails = gson.fromJson(json,UserModelClass.class);
                Integer userid = userDetails.getUserId();


                final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                        .create(BloodAidService.class)
                        .adminPrivilegeDelete(userid);

                Thread thread =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    String s = response.body().string();

                                    //Response parsing
                                    String status;
                                    if (s.isEmpty()) {
                                        status = "Failed";
                                        Toast.makeText(AdminHome.this, status + " .", Toast.LENGTH_LONG).show();

                                    } else {
                                        JSONObject object = new JSONObject(s);
                                        status = object.getString("message");
                                        Toast.makeText(AdminHome.this, status + " .", Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove(ADMIN_LOGIN);
                                        userDetails.setAdminStatus(0);
                                        String json = gson.toJson(userDetails);
                                        editor.putString(USER_DATA, json);
                                        editor.apply();
                                        startActivity(new Intent(AdminHome.this, AdminLoginActivity.class));
                                        finish();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(AdminHome.this, e.getMessage() + " - JSON", Toast.LENGTH_LONG).show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(AdminHome.this, e.getMessage() + " - IO", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(AdminHome.this, t.getMessage() + " .", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    public void logOut_imageView(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ADMIN_LOGIN);
        editor.apply();
        startActivity(new Intent(AdminHome.this, AdminLoginActivity.class));
        finish();
    }
}
