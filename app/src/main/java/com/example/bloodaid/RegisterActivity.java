package com.example.bloodaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodaid.utils.AreaData;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Spinner mDistrictSpinner, mBloodSpinner;
    private String name, phone, email, password, confirm_pass, donar_status="0",  district="",  admin_status="0", blood_group="";
    private long area_id;
    private Double latitude, longitude;
    private TextInputLayout mNameLayout, mPhoneLayout, mEmailLayout, mPasswordLayout,
                            mConfirmPasswordLayout;
    private Button mRegister, mLogin;
    private AreaData data = new AreaData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        districtAndBloodGroupSpinnerWork();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeEditTextInputValues();
                if(validateEditTextInputValues() && validateSpinnersValue()){
                    takeDataFromMap();
                    if(checkInternetConnectivity())
                        dataSendToServer();
                    else{
                        AllToasts.errorToast(RegisterActivity.this, "Please Check Internet Connection and try again !");
                    }
                }

            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void takeDataFromMap() {
        //take latitude longitude from map
        //Suppose
        latitude = 66.3429;
        longitude = 77.43945;
        area_id = data.getAreaId(district);
    }

    private void dataSendToServer() {
        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                        .create(BloodAidService.class)
                        .createUser(name,phone, email,password,
                                donar_status,area_id, admin_status , latitude,
                                longitude, blood_group);

        new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String s = response.body().string();
                            JSONObject jsonObject = new JSONObject(s);
                            Boolean status = jsonObject.getBoolean("created");
                            if(status){
                                AllToasts.successToast(RegisterActivity.this, "Registration Successfully Completed !");
                                finish();
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }
                            else{
                                AllToasts.errorToast(RegisterActivity.this, "Registration Failed");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();

    }

    private boolean validateSpinnersValue() {
        if(district.equals("Choose District")){
            AllToasts.errorToast(RegisterActivity.this, "Please select your district ");
        }
        else if(blood_group.equals("Choose Blood Group")){
            AllToasts.errorToast(RegisterActivity.this, "Please select your blood group ");
        }
        else{
            return true;
        }
        return false;

    }

    private boolean validateEditTextInputValues() {
        mNameLayout.setError(null);
        mPhoneLayout.setError(null);
        mPasswordLayout.setError(null);
        mConfirmPasswordLayout.setError(null);

        String mobile = phone.substring(0,2);

        if(name.isEmpty()){
            mNameLayout.setError("Name can't be empty");
        }
        else if(phone.isEmpty()){
            mPhoneLayout.setError("Phone number can't be empty");
        }
        else if(phone.length()<11){
            mPhoneLayout.setError("Phone number can't be less than 11 digits");
        }
        else if(!mobile.equals("01")){
            mPhoneLayout.setError("Please enter a valid Phone number !");
        }
        else if(email.isEmpty()){
            mEmailLayout.setError("Please enter a valid Email !!");
        }
        else if(password.isEmpty()){
            mPasswordLayout.setError("Password Field can't be empty");
        }
        else if(password.length()<6){
            mPasswordLayout.setError("Password should be greater than 6 digits");
        }
        else if(!password.equals(confirm_pass)){
            mConfirmPasswordLayout.setError("Password doesn't match with above password");
        }
        else {
            return true;
        }
        return false;

    }

    private void takeEditTextInputValues() {
        name = mNameLayout.getEditText().getText().toString().trim();
        phone = mPhoneLayout.getEditText().getText().toString().trim();
        email = mEmailLayout.getEditText().getText().toString().trim();
        password = mPasswordLayout.getEditText().getText().toString();
        confirm_pass = mConfirmPasswordLayout.getEditText().getText().toString();
    }



    private void districtAndBloodGroupSpinnerWork() {

        HashMap<String, Integer> ddd = data.getAreaData();
        ArrayList<String> districtItems = new ArrayList<>();

        for(Map.Entry mp : ddd.entrySet()){
            districtItems.add(mp.getKey().toString());
        }
        //style and populate the spiner
        ArrayAdapter districtAdapter = new ArrayAdapter<String>(
                RegisterActivity.this,
                R.layout.color_spinner_layout,
                districtItems);

        ArrayAdapter bloodAdapter = ArrayAdapter.createFromResource(
                RegisterActivity.this,
                R.array.blood_items,
                R.layout.color_spinner_layout
        );
        //dropdown style design
        districtAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        bloodAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        mDistrictSpinner.setAdapter(districtAdapter);
        mBloodSpinner.setAdapter(bloodAdapter);
        mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                district = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                blood_group = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void init() {
        mDistrictSpinner = findViewById(R.id.district_spinner);
        mBloodSpinner = findViewById(R.id.blood_spinner);

        mNameLayout = findViewById(R.id.name_input);
        mPhoneLayout = findViewById(R.id.phone_input);
        mEmailLayout = findViewById(R.id.email_input);
        mPasswordLayout = findViewById(R.id.password_input);
        mConfirmPasswordLayout = findViewById(R.id.confirm_password_input);

        mRegister = findViewById(R.id.register_button);
        mLogin = findViewById(R.id.login_btn);
    }



    public boolean checkInternetConnectivity(){
        boolean connected ;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }

}
