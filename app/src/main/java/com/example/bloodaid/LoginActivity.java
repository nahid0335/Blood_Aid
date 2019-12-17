package com.example.bloodaid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodaid.models.UserModelClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout mPhone, mPassword;
    Button mLogin, mRegister;
    TextView mForgotPass;
    String userphone, userpass;
    Integer user_id = -1;
    Boolean isFinished = false;



    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_DATA = "user_data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        if(sharedPreferences.contains(USER_DATA)){
            finish();
            startActivity( new Intent(LoginActivity.this, MainActivity.class) );
        }



        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Login API response call

                //Take inputs
                userphone = mPhone.getEditText().getText().toString().trim();
                userpass = mPassword.getEditText().getText().toString().trim();

                //check validation
                if(userphone.isEmpty()){
                    mPhone.setError("Phone number can't be empty !");
                }
                else if(userpass.isEmpty()){
                    mPassword.setError("Password can't be empty !");
                }
                else {
                    //do actual login check
                    mPassword.setError(null);
                    mPhone.setError(null);
                    final Call<UserModelClass> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .loginUser(userphone, userpass);


                    Thread T = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            call.enqueue(new Callback<UserModelClass>() {
                                @Override
                                public void onResponse(Call<UserModelClass> call, Response<UserModelClass> response) {
                                    if (!response.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Code : " + response.code() + " .", Toast.LENGTH_LONG).show();
                                    }
                                    UserModelClass userDetails = null;
                                    if(response.body() != null){
                                        userDetails = response.body();
                                        Log.d("TAG",userDetails.getName());
                                    }

                                    //Response parsing
                                    Boolean status;
                                    if (userDetails == null) {
                                        status = false;
                                    } else {
                                        status = true;
                                    }

                                    if (status) {

                                        //Share preference save data
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(userDetails);
                                        editor.putString(USER_DATA, json);
                                        editor.apply();
                                        // end share preference

                                        finish();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        AllToasts.successToast(LoginActivity.this, "Successfully Logged In");
                                    } else {
                                        AllToasts.errorToast(LoginActivity.this, "Phone or Password is not correct!");
                                    }
                                }


                                @Override
                                public void onFailure(Call<UserModelClass> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this, t.getMessage() + " .", Toast.LENGTH_LONG).show();
                                }

                            });
                        }
                    });

                    T.start();
                    try {
                        T.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        //divert to sign up
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }

    private void init() {
        mPhone = findViewById(R.id.textInputLoginPhone);
        mPassword = findViewById(R.id.textInputLoginPass);
        mLogin = findViewById(R.id.login_btn);
        mRegister = findViewById(R.id.sign_up_btn);
        mForgotPass = findViewById(R.id.forgot_text);
    }


    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

}
