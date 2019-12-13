package com.example.bloodaid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bloodaid.models.UserModelClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity {
    TextView userName,userPhone,userEmail,userPassword,userBloodGroup,userDistrict,userDonorStatus,userlastDonate,userDonateCount;
    ImageView userDonateStatus;
    TextInputEditText txtboxusername,txtboxoldpassword,txtboxupdatepassword;
    Button saveBloodState;

    private Dialog dialog;

    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_DATA = "user_data";

    Boolean button_popupUpdateBlood_searchstate = false;



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


                final String userBloodgroup = userDetails.getBloodGroup();
                if(btnapos.getText().equals(userBloodgroup)){
                    btnapos.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnapos.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_aPos);
                }else if(btnaneg.getText().equals(userBloodgroup)){
                    btnaneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnaneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_aNeg);
                }else if(btnbpos.getText().equals(userBloodgroup)){
                    btnbneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnbneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_bPos);
                }else if(btnbneg.getText().equals(userBloodgroup)){
                    btnbneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnbneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_bNeg);
                }else if(btnopos.getText().equals(userBloodgroup)){
                    btnopos.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnopos.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_oPos);
                }else if(btnoneg.getText().equals(userBloodgroup)){
                    btnoneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnoneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_oNeg);
                }else if(btnabpos.getText().equals(userBloodgroup)){
                    btnabpos.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnabpos.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_abPos);
                }else if(btnabneg.getText().equals(userBloodgroup)){
                    btnabneg.setBackground(getDrawable(R.drawable.button_register_ui));
                    btnabneg.setTextColor(Color.WHITE);
                    saveBloodState = dialog.findViewById(R.id.button_popupUpdateBlood_abNeg);
                }





                updatetxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String updateBloodGroup = saveBloodState.getText().toString();
                        if(!updateBloodGroup.equals(userBloodgroup)){

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
}
