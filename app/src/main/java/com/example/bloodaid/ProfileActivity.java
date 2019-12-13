package com.example.bloodaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodaid.models.UserModelClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity {
    TextView userName,userPhone,userEmail,userPassword,userBloodGroup,userDistrict,userDonorStatus,userlastDonate,userDonateCount;
    ImageView userDonateStatus;
    TextInputEditText txtboxusername;

    private Dialog dialog;

    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_DATA = "user_data";


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



        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USER_DATA,null);
        final UserModelClass userDetails = gson.fromJson(json,UserModelClass.class);
        userName.setText(userDetails.getName());
        userPhone.setText(userDetails.getMobile());
        userEmail.setText(userDetails.getEmail());
        userPassword.setText("********");
        userBloodGroup.setText(userDetails.getBloodGroup());
        userDistrict.setText(userDetails.getDistrict());
        int donorStatus = userDetails.getDonorStatus();
        if(donorStatus == 1){
            userDonorStatus.setText("Donor");
            userlastDonate.setText(userDetails.getLastDonate());
            userDonateCount.setText(userDetails.getDonateCount().toString());
            if(userDetails.getStatus() == 1){
                userDonateStatus.setBackgroundResource(R.drawable.ic_check_circle_black_24dp);
            }
            else{
                userDonateStatus.setBackgroundResource(R.drawable.ic_cancel);
            }
        }
        else{
            userDonorStatus.setText("Not a Donor");
            userlastDonate.setText("0-0-0000");
            userlastDonate.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.red));
            userDonateCount.setText("0");
            userDonateCount.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.red));
            userDonateStatus.setBackgroundResource(R.drawable.ic_cancel);
        }


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
                        String userName = txtboxusername.getText().toString();
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
}
