package com.example.bloodaid.fragments;

import android.content.Intent;
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

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.MainActivity;
import com.example.bloodaid.R;
import com.example.bloodaid.SearchResultActivity;
import com.example.bloodaid.utils.AreaData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchDialog extends BottomSheetDialogFragment {
    Button mSearchBtn;
    Spinner mDistrictSpinner, mBloodSpinner;
    String districtStr="Choose District", bloodGroupStr="Choose Blood Group", searchFor = "donor";
    AreaData data = new AreaData();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_dialog, container, false);
        init(v);

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
                    Intent i = new Intent(getContext(), SearchResultActivity.class);
                    i.putExtra("searchfor", searchFor);
                    i.putExtra("district", districtStr);
                    i.putExtra("bloodgroup", bloodGroupStr);
                    //Toast.makeText(getContext(), districtStr, Toast.LENGTH_LONG).show();
                    startActivity(i);
                    dismiss();
                }
            }
        });

        bloodGroupStr = getArguments().getString("bloodgroup");
        Log.d("TAGG", bloodGroupStr + " ||||");
        districtAndBloodGroupSpinnerWork();


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


}
