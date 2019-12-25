package com.example.bloodaid.fragments;


import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.models.UserModelClass;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonorAddFragment extends Fragment {


    public DonorAddFragment() {
        // Required empty public constructor
    }


    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_DATA = "user_data";
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Integer available ;
    private String oldData = "DD-MM-YYYY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_donor_add, container, false);


        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("anotherFragment", true);
        editor.apply();


        final TextView lastDonate = rootView.findViewById(R.id.textView_donorAddFragment_lastDonate);
        final Switch status = rootView.findViewById(R.id.switch1);
        Button doneBtn = rootView.findViewById(R.id.button_donorAddFragment_done);

         Gson gson = new Gson();
        String json = sharedPreferences.getString(USER_DATA,null);
        UserModelClass userDetails = gson.fromJson(json,UserModelClass.class);
        final int userId = userDetails.getUserId();


        lastDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(calendar.YEAR);
                int month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
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
                lastDonate.setText(date);
            }
        };


        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                        available = 1;
                }else{
                        available = 0;
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateData = lastDonate.getText().toString();
                if(updateData.equals(oldData)){
                    AllToasts.errorToast(getContext(),"Please Enter The Last Date of Blood Donation !");
                }else{
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                    String currentdate = dateformat.format(new Date());
                    try {
                        Date Day1 = dateformat.parse(updateData);
                        Date Day2 = dateformat.parse(currentdate);
                        long difference = Day2.getTime()-Day1.getTime();
                        int daycount = (int)TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
                        if(daycount<120){
                            if(available==1){
                                AllToasts.errorToast(getContext(),"You are not eligible to be Available !");
                            }
                            else{
                                addDonorDataSendToDatabase(userId,updateData,available);
                            }
                        }else{
                            addDonorDataSendToDatabase(userId,updateData,available);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        return rootView;
    }




    private void addDonorDataSendToDatabase(int userid,String lastDate, int Status) {
        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .insertDonor(userid,lastDate,Status);

        new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if(response.isSuccessful()){
                                String s = response.body().string();
                                JSONObject jsonObject = new JSONObject(s);
                                Boolean status = jsonObject.getBoolean("created");
                                if(status){
                                    AllToasts.successToast(getContext(), "Congratulation !! Your data has been sent to process !");
                                }
                                else{
                                    AllToasts.errorToast(getContext(), "Sorry ! Can't be a Donor !"+response.errorBody());
                                }
                            }
                            else{
                                AllToasts.errorToast(getContext(), "OPSS ! API Response failed !");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage()+" .", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();

    }



}
