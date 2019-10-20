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
import com.google.android.material.textfield.TextInputEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AdminLoginActivity extends AppCompatActivity {
    Button AdminLoginbtn;
    TextInputEditText txtboxphone,txtboxpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        AdminLoginbtn =findViewById(R.id.Adminlogin_btn);
        txtboxphone = findViewById(R.id.editTextAdminLoginPhone);
        txtboxpassword = findViewById(R.id.editTextadminLoginPassword);

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

    }
}
