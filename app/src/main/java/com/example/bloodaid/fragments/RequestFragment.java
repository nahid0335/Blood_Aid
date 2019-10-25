package com.example.bloodaid.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.LoginActivity;
import com.example.bloodaid.MainActivity;
import com.example.bloodaid.R;
import com.example.bloodaid.RegisterActivity;
import com.example.bloodaid.RetrofitInstance;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestFragment extends Fragment {
    TextInputLayout mName, mPhoneNo, mBloodUnit, mHospital;
    Spinner mDistrictSpinner, mBloodSpinner;
    EditText mReason;
    Button mNeedDate, mRequest;
    String districtStr, needDateStr, bloodGroupStr, nameStr, phoneStr, hospitalStr, reasonStr;
    int  areaId;
    int bloodUnit = 0;
    Calendar myCalendar;


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_request, container, false);
        init(v);
        districtAndBloodGroupSpinnerWork();
        setNeedBloodDate();

        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeEditTextInputValues();
                if(validateEditTextInputValues() && validateSpinnersValue()){
                    convertDistrictStrToAreaId();
                    dataSendToServer();
                }

            }
        });

        return v;
    }

    private void convertDistrictStrToAreaId() {
        //here district will be convert into area_id
        areaId = 3;
    }

    private void setNeedBloodDate() {
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };
        mNeedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        needDateStr = sdf.format(myCalendar.getTime());
        mNeedDate.setText(needDateStr);
    }


    private void init(View v) {
        mName = v.findViewById(R.id.name_input);
        mPhoneNo = v.findViewById(R.id.phone_input);
        mBloodUnit = v.findViewById(R.id.blood_unit_input);
        mHospital = v.findViewById(R.id.hospital_input);
        mDistrictSpinner = v.findViewById(R.id.district_spinner);
        mBloodSpinner = v.findViewById(R.id.blood_spinner);
        mReason = v.findViewById(R.id.et_reason);
        mNeedDate = v.findViewById(R.id.need_date_btn);
        mRequest = v.findViewById(R.id.register_button);
    }


    private void districtAndBloodGroupSpinnerWork() {
        //style and populate the spiner
        ArrayAdapter districtAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.district_items,
                R.layout.color_spinner_layout
        );
        ArrayAdapter bloodAdapter = ArrayAdapter.createFromResource(
                getContext(),
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
                districtStr = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                bloodGroupStr = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void dataSendToServer() {

        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .bloodRequestSend(nameStr,phoneStr, areaId, bloodUnit,
                        hospitalStr,reasonStr, needDateStr , bloodGroupStr);

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
                                AllToasts.successToast(getContext(), "Request Successfully Made !");
                                //clear all
                                clearALlField();
                            }
                            else{
                                AllToasts.errorToast(getContext(), "Sorry ! Blood Request haven't made !"+response.errorBody());
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

    private void clearALlField() {
        mName.getEditText().setText("");
        mPhoneNo.getEditText().setText("");
        mHospital.getEditText().setText("");
        mReason.setText("");
        mBloodUnit.getEditText().setText("");
    }

    private boolean validateSpinnersValue() {
        if(districtStr.equals("Choose District")){
            AllToasts.errorToast(getContext(), "Please select your district ");
        }
        else if(bloodGroupStr.equals("Choose Blood Group")){
            AllToasts.errorToast(getContext(), "Please select your blood group ");
        }
        else{
            return true;
        }
        return false;

    }

    private boolean validateEditTextInputValues()
    {
        mName.setError(null);
        mPhoneNo.setError(null);
        mHospital.setError(null);
        mReason.setError(null);

        if(nameStr.isEmpty()){
            mName.setError("Name can't be empty");
        }
        else if(phoneStr.isEmpty()){
            mPhoneNo.setError("Phone number can't be empty");
        }
        else if(reasonStr.isEmpty()){
            mReason.setError("Please say the reason. Why did he/she need blood?");
        }
        else if(bloodUnit == 0){
            AllToasts.errorToast(getContext(), "Please enter how many bags of blood is needed !");
        }
        else {
            return true;
        }
        return false;
    }

    private void takeEditTextInputValues() {
        nameStr = mName.getEditText().getText().toString().trim();
        phoneStr = mPhoneNo.getEditText().getText().toString().trim();
        hospitalStr = mHospital.getEditText().getText().toString().trim();
        reasonStr = mReason.getText().toString();
        bloodUnit = Integer.valueOf(mBloodUnit.getEditText().getText().toString().trim());
    }

}
