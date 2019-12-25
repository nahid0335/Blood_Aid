package com.example.bloodaid.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.MainActivity;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.SearchResultActivity;
import com.example.bloodaid.models.DonorPositionModelClass;
import com.example.bloodaid.utils.AreaData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SearchDialog extends BottomSheetDialogFragment {
    Button mSearchBtn;
    Spinner mDistrictSpinner, mBloodSpinner;
    String districtStr="Choose District", bloodGroupStr="Choose Blood Group", searchFor = "donor";
    AreaData data = new AreaData();


    private ArrayList<LatLng> pinpoint = new ArrayList<>();
    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String DONOR_LOCATION = "donor_location";
    public String bloodGroup;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_dialog, container, false);
        init(v);


        bloodGroupStr = getArguments().getString("bloodgroup");
        Log.d("TAGG", bloodGroupStr + " ||||");
        districtAndBloodGroupSpinnerWork();

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mBottomNav.getMenu().getItem(3).setChecked(true);

                if(districtStr.equals("Choose District")){
                    AllToasts.infoToast(getContext(), "Please Select District !");
                }
                else if(bloodGroupStr.equals("Choose Blood Group") && searchFor.equals("donor")){
                    AllToasts.infoToast(getContext(), "Please Select Blood Group !");
                }
                else{

                    bloodGroup = bloodGroupStr;
                    renameBloodGroup();
                    fetchDonorPositionFromDatabase();

//                    Intent i = new Intent(getContext(), SearchResultActivity.class);
//                    i.putExtra("searchfor", searchFor);
//                    i.putExtra("district", districtStr);
//                    i.putExtra("bloodgroup", bloodGroupStr);
//                    //Toast.makeText(getContext(), districtStr, Toast.LENGTH_LONG).show();
//                    startActivity(i);
//                    dismiss();
                }
            }
        });


        return v;
    }

    private void init(View v) {
        mSearchBtn = v.findViewById(R.id.search_text_button);
        mDistrictSpinner = v.findViewById(R.id.district_spinner);
        mBloodSpinner = v.findViewById(R.id.blood_spinner);
    }


    private void districtAndBloodGroupSpinnerWork() {
        //style and populate the spiner
        LinkedHashMap<String, Integer> list = data.getAreaData();

        ArrayList<String> districtItems = new ArrayList<>();
        ArrayList<String> bloodGroupsItems = new ArrayList<String>(
                Arrays.asList(getResources().getStringArray(R.array.blood_items)));

        for(Map.Entry mp : list.entrySet()){
            districtItems.add(mp.getKey().toString());
        }
        ArrayAdapter districtAdapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.color_spinner_layout,
                districtItems);

        ArrayAdapter bloodAdapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.color_spinner_layout,
                bloodGroupsItems);

        //dropdown style design
        districtAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        bloodAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        mDistrictSpinner.setAdapter(districtAdapter);
        mBloodSpinner.setAdapter(bloodAdapter);
        mDistrictSpinner.setSelection(districtItems.indexOf(districtStr));
        mBloodSpinner.setSelection(bloodGroupsItems.indexOf(bloodGroupStr));
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



    private void renameBloodGroup() {

        if(bloodGroup.equals("A+")){
            bloodGroup = "A_pos";
        }
        else if(bloodGroup.equals("A-")){
            bloodGroup = "A_neg";
        }
        else if(bloodGroup.equals("B+")){
            bloodGroup = "B_pos";
        }
        else if(bloodGroup.equals("B-")){
            bloodGroup = "B_neg";
        }
        else if(bloodGroup.equals("O+")){
            bloodGroup = "O_pos";
        }
        else if(bloodGroup.equals("O-")){
            bloodGroup = "O_neg";
        }
        else if(bloodGroup.equals("AB+")){
            bloodGroup = "AB_pos";
        }
        else if(bloodGroup.equals("AB-")){
            bloodGroup = "AB_neg";
        }
        else{
            AllToasts.errorToast(getContext(), "Something went wrong on blood group selection !");
        }
    }


    private  void fetchDonorPositionFromDatabase(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final Call<List<DonorPositionModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorPosition(bloodGroup,districtStr);
        call.enqueue(new Callback<List<DonorPositionModelClass>>() {
            @Override
            public void onResponse(Call<List<DonorPositionModelClass>> call, Response<List<DonorPositionModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                else {

                    List<DonorPositionModelClass> responseList = response.body();
                    if(responseList.get(0).getDonorId() == -1){
                        AllToasts.infoToast(getContext(),
                                "No data found !");
                        progressDialog.dismiss();
                    }else {
                        for(DonorPositionModelClass position : responseList){
                            Double latitude = position.getLatitude();
                            Double longitude = position.getLongitude();
                            pinpoint.add(new LatLng(latitude,longitude));

                        }
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if(sharedPreferences.contains(DONOR_LOCATION)){
                            editor.remove(DONOR_LOCATION);
                            editor.apply();
                        }
                        Gson gson = new Gson();
                        String json = gson.toJson(pinpoint);
                        editor.putString(DONOR_LOCATION, json);
                        editor.apply();
                       // AllToasts.infoToast(getContext(),pinpoint.toString());
                        pinpoint.clear();
                        // AllToasts.successToast(getContext(),"save");
                        progressDialog.dismiss();
                        Intent i = new Intent(getContext(), SearchResultActivity.class);
                        i.putExtra("searchfor", searchFor);
                        i.putExtra("district", districtStr);
                        i.putExtra("bloodgroup", bloodGroupStr);
                        //Toast.makeText(getContext(), districtStr, Toast.LENGTH_LONG).show();
                        startActivity(i);
                        dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DonorPositionModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "OPPSS!! Failded to fetch database: "+t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        progressDialog.show();
    }


}
