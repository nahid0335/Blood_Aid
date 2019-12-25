package com.example.bloodaid.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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

import static android.content.Context.MODE_PRIVATE;
import static com.example.bloodaid.fragments.DonorAddFragment.SHARED_PREFerence_Key;

/**
 * A simple {@link Fragment} subclass.
 */
public class HospitalAddFragment extends Fragment {
    TextInputLayout mHospitalName, mPhoneNo, mEmail;
    Spinner mDistrictSpinner;
    EditText mBloodUnitDetail;
    Button mAddHospital;
    String districtStr = null;
    long areaId = 1;

    public HospitalAddFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_hospital_add, container, false);
        init(v);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("anotherFragment", true);
        editor.apply();


        districtSpinnerWork();

        mAddHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(validate()){
                String name = mHospitalName.getEditText().getText().toString();
                String phone = mPhoneNo.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String bloodDetails = mBloodUnitDetail.getText().toString();
                AreaData data = new AreaData();
                HashMap<String, Integer> districts = data.getAreaData();

                areaId = (long)data.getAreaId(districtStr);

                addHospitalDataSendToDatabase(name , phone, email, bloodDetails, areaId);

            }
            }
        });
        return v;
    }

    private void addHospitalDataSendToDatabase(String name, String phone, String email, String bloodDetails, long areaId) {
        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .addHospital(name , email, phone, bloodDetails, areaId);

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
                                AllToasts.successToast(getContext(), "Hospital Successfully Added !");
                            }
                            else{
                                AllToasts.errorToast(getContext(), "Sorry ! Hospital hasn't added !"+response.errorBody());
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

    private void districtSpinnerWork() {
        AreaData m = new AreaData();

        HashMap<String, Integer> data = m.getAreaData();

        ArrayList<String> districtItems = new ArrayList<>();

        for(Map.Entry mp : data.entrySet()){
            districtItems.add(mp.getKey().toString());
        }

        /*for(int i=0;i<districtItems.size();i++){
            Log.d("DISTRICT", districtItems.get(i));
        }*/

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
        mHospitalName.setError(null);
        mPhoneNo.setError(null);
        Boolean s = true;
        if(mHospitalName.getEditText().getText().toString().isEmpty()){
            mHospitalName.setError("Please enter hospital name !");
            s = false;
        }
        if(mPhoneNo.getEditText().getText().toString().isEmpty()){
            mPhoneNo.setError("Please enter a phone number of the hospital.");
            s = false;
        }

        return s;
    }

    private void init(View v) {
        mHospitalName = v.findViewById(R.id.name_input);
        mPhoneNo = v.findViewById(R.id.phone_input);
        mEmail = v.findViewById(R.id.email_input);
        mDistrictSpinner = v.findViewById(R.id.district_spinner);
        mBloodUnitDetail = v.findViewById(R.id.blood_unit_detail);
        mAddHospital = v.findViewById(R.id.add_hospital_button);
    }

}
