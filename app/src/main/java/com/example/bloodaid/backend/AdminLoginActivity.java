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
import com.example.bloodaid.RegisterActivity;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.models.UserModelClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AdminLoginActivity extends AppCompatActivity {
    Button AdminLoginbtn;
    TextInputEditText txtboxphone,txtboxpassword;

    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_DATA = "user_data";
    public static final String ADMIN_LOGIN = "admin_login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        AdminLoginbtn =findViewById(R.id.Adminlogin_btn);
        txtboxphone = findViewById(R.id.editTextAdminLoginPhone);
        txtboxpassword = findViewById(R.id.editTextadminLoginPassword);
        TextView adminbtn = findViewById(R.id.textView_adminLogin_adminRegButton);


        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        final Gson gson = new Gson();
        if(sharedPreferences.contains(ADMIN_LOGIN)){
            finish();
            startActivity( new Intent(AdminLoginActivity.this, AdminHome.class) );
        }

        AdminLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Login API response call

                //Take inputs
                String adminPhone = txtboxphone.getText().toString().trim();
                String adminPass = txtboxpassword.getText().toString().trim();

                //check validation
                if(adminPhone.isEmpty()){
                    txtboxphone.setError("Phone number can't be empty !");
                }
                else if(adminPass.isEmpty()){
                    txtboxpassword.setError("Password can't be empty !");
                }
                else{
                    //do actual login check
                    txtboxphone.setError(null);
                    txtboxpassword.setError(null);
                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .loginAdmin(adminPhone, adminPass);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String s = response.body().string();

                                        //Response parsing
                                        Boolean status;
                                        if(s.isEmpty()){
                                            status = false;
                                        }
                                        else{
                                            JSONObject object = new JSONObject(s);
                                            status = object.getBoolean("validity"); // true or false will be returned as response
                                        }

                                        if(status){

                                            //Share preference save data
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(ADMIN_LOGIN, true);
                                            editor.apply();
                                            // end share preference

                                            finish();
                                            startActivity( new Intent(AdminLoginActivity.this, AdminHome.class) );
                                            AllToasts.successToast(AdminLoginActivity.this, "Successfully Logged In");
                                        }
                                        else{
                                            AllToasts.errorToast(AdminLoginActivity.this,"Phone or Password is not correct!" );
                                        }

                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(AdminLoginActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                    }catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(AdminLoginActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(AdminLoginActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).start();


                }


            }
        });


        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = sharedPreferences.getString(USER_DATA,null);
                UserModelClass userDetails = gson.fromJson(json,UserModelClass.class);

                int adminstatus = userDetails.getAdminStatus();
                if(adminstatus == 1)
                {
                    AllToasts.errorToast(AdminLoginActivity.this,"You are admin Already !!");
                }
                else{
                    int userid = userDetails.getUserId();
                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .adminRequest(userid);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String s = response.body().string();

                                        //Response parsing
                                        Boolean status;
                                        if(s.isEmpty()){
                                            status = false;
                                        }
                                        else{
                                            JSONObject object = new JSONObject(s);
                                            status = object.getBoolean("created"); // true or false will be returned as response
                                        }

                                        if(status){
                                            AllToasts.successToast(AdminLoginActivity.this, "Request has been sent successfully !!");
                                        }
                                        else{
                                            AllToasts.errorToast(AdminLoginActivity.this,"Request can not be sent !!" );
                                        }

                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(AdminLoginActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                    }catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(AdminLoginActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(AdminLoginActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).start();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminLoginActivity.this,MainActivity.class));
        finish();
    }

}
