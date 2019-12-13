package com.example.bloodaid.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
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

public class OrganizationAddFragment extends Fragment {

    TextInputLayout mOrgName, mPhoneNo, mEmail;
    Spinner mDistrictSpinner;
    EditText mDetails;
    Button mAddOrg;
    String districtStr = null;
    long areaId = 1;
    AreaData data = new AreaData();

    public OrganizationAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_organization_add, container, false);
        init(v);
        districtSpinnerWork();

        mAddOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String name = mOrgName.getEditText().getText().toString();
                    String phone = mPhoneNo.getEditText().getText().toString();
                    String email = mEmail.getEditText().getText().toString();
                    String bloodDetails = mDetails.getText().toString();

                    HashMap<String, Integer> districts = data.getAreaData();

                    areaId = (long) data.getAreaId(districtStr);

                    addOrgDataSendToDatabase(name, phone, email, bloodDetails, areaId);

                }
            }
        });
        return v;
    }

    private void init(View v) {
        mOrgName = v.findViewById(R.id.name_input);
        mPhoneNo = v.findViewById(R.id.phone_input);
        mEmail = v.findViewById(R.id.email_input);
        mDistrictSpinner = v.findViewById(R.id.district_spinner);
        mDetails = v.findViewById(R.id.org_details);
        mAddOrg = v.findViewById(R.id.org_add_button);
    }

    private void districtSpinnerWork() {
        HashMap<String, Integer> ddd = data.getAreaData();
        ArrayList<String> districtItems = new ArrayList<>();

        for(Map.Entry mp : ddd.entrySet()){
            districtItems.add(mp.getKey().toString());
        }

        ArrayAdapter districtAdapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.color_spinner_layout,
                districtItems);

        districtAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        mDistrictSpinner.setAdapter(districtAdapter);

        mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                districtStr = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean validate() {
        mOrgName.setError(null);
        mPhoneNo.setError(null);
        Boolean s = true;
        if(mOrgName.getEditText().getText().toString().isEmpty()){
            mOrgName.setError("Please enter Organization name !");
            s = false;
        }
        if(mPhoneNo.getEditText().getText().toString().isEmpty()){
            mPhoneNo.setError("Please enter a phone number of the organization.");
            s = false;
        }

        return s;
    }

    private void addOrgDataSendToDatabase(String name, String phone, String email, String bloodDetails, long areaId) {
        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .addOrganization(name , phone, email, bloodDetails, areaId);

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
                                    AllToasts.successToast(getContext(), "Organization Successfully Added !");
                                }
                                else{
                                    AllToasts.errorToast(getContext(), "Sorry ! Organization hasn't added !"+response.errorBody());
                                }
                            }
                            else{
                                AllToasts.errorToast(getContext(), "OPPSS ! API Response failed !");
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


