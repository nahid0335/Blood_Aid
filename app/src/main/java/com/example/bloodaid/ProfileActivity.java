package com.example.bloodaid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodaid.models.UserModelClass;
import com.example.bloodaid.utils.AreaData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProfileActivity extends AppCompatActivity {
    TextView userName,userPhone,userEmail,userPassword,userBloodGroup,userDistrict,userDonorStatus,userlastDonate,userDonateCount;
    ImageView userDonateStatus;
    TextInputEditText txtboxusername,txtboxoldpassword,txtboxupdatepassword;
    Button saveBloodState;

    private Dialog dialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_DATA = "user_data";


    Integer userId , donatestatusSave=0;


    String districtName = null;
    int areaId = 1;
    AreaData areaData = new AreaData();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.textView_userProfile_name);
        userPhone = findViewById(R.id.textView_userProfile_mobile);
        userEmail = findViewById(R.id.textView_userProfile_email);
        userPassword = findViewById(R.id.textView_userProfile_password);
        userBloodGroup = findViewById(R.id.textView_userProfile_bloodGroup);
        userDistrict = findViewById(R.id.textView_userProfile_district);
        userDonorStatus = findViewById(R.id.textView_userProfile_donorStatus);
        userlastDonate = findViewById(R.id.textView_userProfile_lastDonate);
        userDonateCount = findViewById(R.id.textView_userProfile_donateCount);

        userDonateStatus = findViewById(R.id.imageView_userProfile_donateStatus);


        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USER_DATA,null);
        final UserModelClass userDetails = gson.fromJson(json,UserModelClass.class);
        userName.setText(userDetails.getName());
        userPhone.setText(userDetails.getMobile());
        userEmail.setText(userDetails.getEmail());
        userPassword.setText("********");
        userBloodGroup.setText(userDetails.getBloodGroup());
        userDistrict.setText(userDetails.getDistrict());
        final int donorStatus = userDetails.getDonorStatus();
        if(donorStatus == 1){
            userDonorStatus.setText("Donor");
            userlastDonate.setText(userDetails.getLastDonate());
            userDonateCount.setText(userDetails.getDonateCount().toString());
            if(userDetails.getStatus() == 1){
                donatestatusSave = 1;
                userDonateStatus.setBackgroundResource(R.drawable.ic_check_circle_black_24dp);
            }
            else{
                donatestatusSave = 0;
                userDonateStatus.setBackgroundResource(R.drawable.ic_cancel);
            }
        }
        else{
            donatestatusSave = 0;
            userDonorStatus.setText("Not a Donor");
            userlastDonate.setText("0-0-0000");
            userlastDonate.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.red));
            userDonateCount.setText("0");
            userDonateCount.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.red));
            userDonateStatus.setBackgroundResource(R.drawable.ic_cancel);
        }

        userId = userDetails.getUserId();


        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.popup_updateprofile_textview);
                TextView updatetxt = dialog.findViewById(R.id.textView_popupUpdateTextview_updateButton);
                TextView canceltxt = dialog.findViewById(R.id.textView_popupUpdateTextview_cancelButton);
                TextView olddatatxt = dialog.findViewById(R.id.textView_popupUpdateTextview_olddata);
                txtboxusername = dialog.findViewById(R.id.textInputEdittext_popupUpdateTextview_updateData);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);

                olddatatxt.setText(userDetails.getName());

                updatetxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String updateUserName = txtboxusername.getText().toString();

                        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                                .create(BloodAidService.class)
                                .updateUserName(userId, updateUserName);


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
                                                userName.setText(updateUserName);
                                                userDetails.setName(updateUserName);

                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                Gson gson = new Gson();
                                                String json = gson.toJson(userDetails);
                                                editor.putString(USER_DATA, json);
                                                editor.apply();
                                                AllToasts.successToast(ProfileActivity.this, "Successfully UserName Updated !!");
                                            }
                                            else{
                                                AllToasts.errorToast(ProfileActivity.this,"UserName can't be Updated !!" );
                                            }

                                        }catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(ProfileActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                        }catch (IOException e) {
                                            e.printStackTrace();
                                            Toast.makeText(ProfileActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(ProfileActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).start();

                        dialog.dismiss();

                    }
                });

                canceltxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }

        });

        userPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.popup_updateprofile_textview);
                TextView updatetxt = dialog.findViewById(R.id.textView_popupUpdateTextview_updateButton);
                TextView canceltxt = dialog.findViewById(R.id.textView_popupUpdateTextview_cancelButton);
                TextView olddatatxt = dialog.findViewById(R.id.textView_popupUpdateTextview_olddata);
                txtboxusername = dialog.findViewById(R.id.textInputEdittext_popupUpdateTextview_updateData);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);

                olddatatxt.setText(userDetails.getMobile());

                updatetxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String updateUserPhone = txtboxusername.getText().toString();
                        if(updateUserPhone.length()>= 11){
                            final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                                    .create(BloodAidService.class)
                                    .updateUserPhone(userId, updateUserPhone);


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
                                                    userPhone.setText(updateUserPhone);
                                                    userDetails.setMobile(updateUserPhone);

                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(userDetails);
                                                    editor.putString(USER_DATA, json);
                                                    editor.apply();
                                                    AllToasts.successToast(ProfileActivity.this, "Successfully UserMobile Updated !!");
                                                }
                                                else{
                                                    AllToasts.errorToast(ProfileActivity.this,"UserMobile can't be Updated !!" );
                                                }

                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                            }catch (IOException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(ProfileActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).start();
                            dialog.dismiss();
                        }else{
                            AllToasts.errorToast(ProfileActivity.this,"Input is less than 11 digit !!");
                        }
                    }
                });

                canceltxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });


        userEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.popup_updateprofile_textview);
                TextView updatetxt = dialog.findViewById(R.id.textView_popupUpdateTextview_updateButton);
                TextView canceltxt = dialog.findViewById(R.id.textView_popupUpdateTextview_cancelButton);
                TextView olddatatxt = dialog.findViewById(R.id.textView_popupUpdateTextview_olddata);
                txtboxusername = dialog.findViewById(R.id.textInputEdittext_popupUpdateTextview_updateData);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);

                olddatatxt.setText(userDetails.getEmail());

                updatetxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String updateUserEmail = txtboxusername.getText().toString();


                        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                                .create(BloodAidService.class)
                                .updateUserName(userId, updateUserEmail);


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
                                                userEmail.setText(updateUserEmail);
                                                userDetails.setEmail(updateUserEmail);

                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                Gson gson = new Gson();
                                                String json = gson.toJson(userDetails);
                                                editor.putString(USER_DATA, json);
                                                editor.apply();
                                                AllToasts.successToast(ProfileActivity.this, "Successfully UserEmail Updated !!");
                                            }
                                            else{
                                                AllToasts.errorToast(ProfileActivity.this,"UserEmail can't be Updated !!" );
                                            }

                                        }catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(ProfileActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                        }catch (IOException e) {
                                            e.printStackTrace();
                                            Toast.makeText(ProfileActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(ProfileActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).start();

                        dialog.dismiss();
                    }
                });

                canceltxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });


        userDonorStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                intent.putExtra("TransferActivity",1);
                startActivity(intent);
                finish();
            }
        });


        userPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.popup_updateprofile_password);
                TextView updatetxt = dialog.findViewById(R.id.textView_popupUpdatePassword_updateButton);
                TextView canceltxt = dialog.findViewById(R.id.textView_popupUpdatePassword_cancelButton);
                txtboxoldpassword = dialog.findViewById(R.id.textInputEdittext_popupUpdatePassword_oldPassword);
                txtboxupdatepassword = dialog.findViewById(R.id.textInputEdittext_popupUpdatePassword_updatePassword);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);

                updatetxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String oldpassword = txtboxoldpassword.getText().toString();
                        String updatepassword = txtboxupdatepassword.getText().toString();

                        if(updatepassword.length()>= 6){
                            final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                                    .create(BloodAidService.class)
                                    .updateUserPassword(userId, oldpassword,updatepassword);


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
                                                    AllToasts.successToast(ProfileActivity.this, "Successfully UserPassword Updated !!");
                                                }
                                                else{
                                                    AllToasts.errorToast(ProfileActivity.this,"UserPassword can't be Updated !!" );
                                                }

                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                            }catch (IOException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(ProfileActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).start();

                            dialog.dismiss();
                        }else{
                            AllToasts.errorToast(ProfileActivity.this,"Update Password is less than 6 Letter !!");
                        }


                    }
                });

                canceltxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });

        userBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.popup_updateprofile_bloodgroup);
                TextView updatetxt = dialog.findViewById(R.id.textView_popupUpdateBlood_updateButton);
                TextView canceltxt = dialog.findViewById(R.id.textView_popupUpdateBlood_cancelButton);
                Button btnapos = dialog.findViewById(R.id.button_popupUpdateBlood_aPos);
                Button btnaneg = dialog.findViewById(R.id.button_popupUpdateBlood_aNeg);
                Button btnbpos = dialog.findViewById(R.id.button_popupUpdateBlood_bPos);
                Button btnbneg = dialog.findViewById(R.id.button_popupUpdateBlood_bNeg);
                Button btnopos = dialog.findViewById(R.id.button_popupUpdateBlood_oPos);
                Button btnoneg = dialog.findViewById(R.id.button_popupUpdateBlood_oNeg);
                Button btnabpos = dialog.findViewById(R.id.button_popupUpdateBlood_abPos);
                Button btnabneg = dialog.findViewById(R.id.button_popupUpdateBlood_abNeg);

                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);


                final String userPreviousBloodgroup = userDetails.getBloodGroup();
                if(btnapos.getText().equals(userPreviousBloodgroup)){
                    btnapos.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnapos.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_aPos);
                }else if(btnaneg.getText().equals(userPreviousBloodgroup)){
                    btnaneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnaneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_aNeg);
                }else if(btnbpos.getText().equals(userPreviousBloodgroup)){
                    btnbneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnbneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_bPos);
                }else if(btnbneg.getText().equals(userPreviousBloodgroup)){
                    btnbneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnbneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_bNeg);
                }else if(btnopos.getText().equals(userPreviousBloodgroup)){
                    btnopos.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnopos.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_oPos);
                }else if(btnoneg.getText().equals(userPreviousBloodgroup)){
                    btnoneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnoneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_oNeg);
                }else if(btnabpos.getText().equals(userPreviousBloodgroup)){
                    btnabpos.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnabpos.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_abPos);
                }else if(btnabneg.getText().equals(userPreviousBloodgroup)){
                    btnabneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnabneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_abNeg);
                }





                updatetxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String updateBloodGroup = saveBloodState.getText().toString();
                        if(!updateBloodGroup.equals(userPreviousBloodgroup)){
                            final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                                    .create(BloodAidService.class)
                                    .updateUserBloodGroup(userId, updateBloodGroup);


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
                                                    userBloodGroup.setText(updateBloodGroup);
                                                    userDetails.setBloodGroup(updateBloodGroup);

                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(userDetails);
                                                    editor.putString(USER_DATA, json);
                                                    editor.apply();
                                                    AllToasts.successToast(ProfileActivity.this, "Successfully UserBloodGroup Updated !!");
                                                }
                                                else{
                                                    AllToasts.errorToast(ProfileActivity.this,"UserBloodGroup can't be Updated !!" );
                                                }

                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                            }catch (IOException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(ProfileActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).start();
                        }
                        dialog.dismiss();
                    }
                });

                canceltxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });


        userDonateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                String currentdate = dateformat.format(new Date());
                try {
                    Date Day1 = dateformat.parse(userlastDonate.getText().toString());
                    Date Day2 = dateformat.parse(currentdate);
                    long difference = Day2.getTime()-Day1.getTime();
                    int daycount = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);


                    if(daycount>=120){
                        dialog = new Dialog(ProfileActivity.this);
                        dialog.setContentView(R.layout.popup_updateprofile_donatestatus);
                        TextView updatetxt = dialog.findViewById(R.id.textView_popupUpdateStatus_updateButton);
                        TextView canceltxt = dialog.findViewById(R.id.textView_popupUpdateStatus_cancelButton);
                        final RadioButton radioavail = dialog.findViewById(R.id.radioButton_popupUpdateStatus_available);
                        final RadioButton radiounavail = dialog.findViewById(R.id.radioButton_popupUpdateStatus_unavailable);
                        final RadioGroup radiogroup = dialog.findViewById(R.id.radioGroup_popupUpdate_donateStatus);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);


                        if(donatestatusSave == 1){
                            radioavail.setChecked(true);
                        }else{
                            radiounavail.setChecked(true);
                        }


                        updatetxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final int updatedonatestatus ;
                                if(donorStatus==1){
                                    int radioId = radiogroup.getCheckedRadioButtonId();

                                    if(radioavail.getId()==radioId){
                                        updatedonatestatus = 1;
                                    }
                                    else{
                                        updatedonatestatus = 0;
                                    }

                                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                                            .create(BloodAidService.class)
                                            .updateUserDonateStatus(userId, updatedonatestatus);


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

                                                            if(updatedonatestatus == 1){
                                                                userDonateStatus.setBackgroundResource(R.drawable.ic_check_circle_black_24dp);
                                                            }
                                                            else{
                                                                userDonateStatus.setBackgroundResource(R.drawable.ic_cancel);
                                                            }

                                                            userDetails.setStatus(updatedonatestatus);

                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            Gson gson = new Gson();
                                                            String json = gson.toJson(userDetails);
                                                            editor.putString(USER_DATA, json);
                                                            editor.apply();
                                                            AllToasts.successToast(ProfileActivity.this, "Successfully UserDonateStatus Updated !!");
                                                        }
                                                        else{
                                                            AllToasts.errorToast(ProfileActivity.this,"UserDonateStatus can't be Updated !!" );
                                                        }

                                                    }catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(ProfileActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                                    }catch (IOException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(ProfileActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(ProfileActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }).start();

                                    dialog.dismiss();

                                }else{
                                    AllToasts.errorToast(ProfileActivity.this, "You are not a Donor !!");
                                }

                            }
                        });

                        canceltxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.show();
                    }else{
                        AllToasts.errorToast(ProfileActivity.this,"You are not eligible to be Available !");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        userlastDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.popup_updateprofile_lastdonate);
                TextView updatetxt = dialog.findViewById(R.id.textView_popupUpdateTime_updateButton);
                TextView canceltxt = dialog.findViewById(R.id.textView_popupUpdateTime_cancelButton);
                TextView olddata = dialog.findViewById(R.id.textView_popupUpdateTime_oldDate);
                final TextView updatedata = dialog.findViewById(R.id.textView_popupUpdateTime_updateData);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);

                olddata.setText(userDetails.getLastDonate());
                updatedata.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(calendar.YEAR);
                        int month = calendar.get(calendar.MONTH);
                        int day = calendar.get(calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                ProfileActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                dateSetListener,
                                year,month,day
                        );

                        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        datePickerDialog.show();
                    }
                });

                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        String date = day+"-"+month+"-"+year;
                        updatedata.setText(date);
                    }
                };


                updatetxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(donorStatus == 1){
                            final String updatelastdonate = updatedata.getText().toString();

                            final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                                    .create(BloodAidService.class)
                                    .updateUserLastDonate(userId, updatelastdonate);


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
                                                    userlastDonate.setText(updatelastdonate);
                                                    userDetails.setLastDonate(updatelastdonate);

                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(userDetails);
                                                    editor.putString(USER_DATA, json);
                                                    editor.apply();
                                                    AllToasts.successToast(ProfileActivity.this, "Successfully UserLastDonate Updated !!");
                                                }
                                                else{
                                                    AllToasts.errorToast(ProfileActivity.this,"UserLastDonate can't be Updated !!" );
                                                }

                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                            }catch (IOException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(ProfileActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).start();

                            dialog.dismiss();
                        }else{
                            AllToasts.errorToast(ProfileActivity.this, "You are not a Donor !!");
                        }
                    }
                });

                canceltxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });



        userDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.popup_updateprofile_district);
                TextView updatetxt = dialog.findViewById(R.id.textView_popupUpdateDistrict_updateButton);
                TextView canceltxt = dialog.findViewById(R.id.textView_popupUpdateDistrict_cancelButton);
                TextView olddatatxt = dialog.findViewById(R.id.textView_popupUpdateDistrict_oldData);
                Spinner districtspinner = dialog.findViewById(R.id.spinner_popupUpdateDistrict_updateData);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);

                olddatatxt.setText(userDetails.getDistrict());


                HashMap<String, Integer> areaDataHashMap = areaData.getAreaData();
                ArrayList<String> districtArrayList = new ArrayList<>();

                for(Map.Entry mapdata : areaDataHashMap.entrySet()){
                    districtArrayList.add(mapdata.getKey().toString());
                }

                ArrayAdapter districtAdapter = new ArrayAdapter<String>(
                        ProfileActivity.this,
                        R.layout.color_spinner_layout,
                        districtArrayList);

                districtAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                districtspinner.setAdapter(districtAdapter);

                districtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        districtName = adapterView.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                updatetxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        areaId = (int) areaData.getAreaId(districtName);
                        if(areaId>0){
                            final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                                    .create(BloodAidService.class)
                                    .updateUserDistrict(userId, areaId);


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
                                                    userDistrict.setText(districtName);
                                                    userDetails.setDistrict(districtName);

                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(userDetails);
                                                    editor.putString(USER_DATA, json);
                                                    editor.apply();
                                                    AllToasts.successToast(ProfileActivity.this, "Successfully UserDistrict Updated !!");
                                                }
                                                else{
                                                    AllToasts.errorToast(ProfileActivity.this,"UserDistrict can't be Updated !!" );
                                                }

                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - JSON", Toast.LENGTH_LONG).show();

                                            }catch (IOException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ProfileActivity.this, e.getMessage()+" - IO", Toast.LENGTH_LONG).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(ProfileActivity.this, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).start();

                            dialog.dismiss();
                        }else{
                            AllToasts.errorToast(ProfileActivity.this,"Please select your district !!");
                        }
                    }
                });

                canceltxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });



    }








    public void bloodGroupSelectClicked(View view) {
        saveBloodState.setBackground(getDrawable(R.drawable.red_border));
        saveBloodState.setTextColor(Color.RED);
        if(R.id.button_popupUpdateBlood_aPos == view.getId()){
            view.setBackground(getDrawable(R.drawable.button_register_ui));
            ((Button) view).setTextColor(Color.WHITE);
            saveBloodState = view.findViewById(R.id.button_popupUpdateBlood_aPos);
        }
        else if(R.id.button_popupUpdateBlood_aNeg == view.getId()){
            view.setBackground(getDrawable(R.drawable.button_register_ui));
            ((Button) view).setTextColor(Color.WHITE);
            saveBloodState = view.findViewById(R.id.button_popupUpdateBlood_aNeg);
        }else if(R.id.button_popupUpdateBlood_bPos == view.getId()){
            view.setBackground(getDrawable(R.drawable.button_register_ui));
            ((Button) view).setTextColor(Color.WHITE);
            saveBloodState = view.findViewById(R.id.button_popupUpdateBlood_bPos);
        }else if(R.id.button_popupUpdateBlood_bNeg == view.getId()){
            view.setBackground(getDrawable(R.drawable.button_register_ui));
            ((Button) view).setTextColor(Color.WHITE);
            saveBloodState = view.findViewById(R.id.button_popupUpdateBlood_bNeg);
        }else if(R.id.button_popupUpdateBlood_oPos == view.getId()){
            view.setBackground(getDrawable(R.drawable.button_register_ui));
            ((Button) view).setTextColor(Color.WHITE);
            saveBloodState = view.findViewById(R.id.button_popupUpdateBlood_oPos);
        }else if(R.id.button_popupUpdateBlood_oNeg == view.getId()){
            view.setBackground(getDrawable(R.drawable.button_register_ui));
            ((Button) view).setTextColor(Color.WHITE);
            saveBloodState = view.findViewById(R.id.button_popupUpdateBlood_oNeg);
        }else if(R.id.button_popupUpdateBlood_abPos == view.getId()){
            view.setBackground(getDrawable(R.drawable.button_register_ui));
            ((Button) view).setTextColor(Color.WHITE);
            saveBloodState = view.findViewById(R.id.button_popupUpdateBlood_abPos);
        }else if(R.id.button_popupUpdateBlood_abNeg == view.getId()){
            view.setBackground(getDrawable(R.drawable.button_register_ui));
            ((Button) view).setTextColor(Color.WHITE);
            saveBloodState = view.findViewById(R.id.button_popupUpdateBlood_abNeg);
        }
    }





    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
        intent.putExtra("BackPressActivity",2);
        startActivity(intent);
        finish();
    }



}
