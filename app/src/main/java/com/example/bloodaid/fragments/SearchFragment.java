package com.example.bloodaid.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.MainActivity;
import com.example.bloodaid.R;
import com.example.bloodaid.SearchResultActivity;
import com.example.bloodaid.utils.AreaData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    View v ;
    Button mDonarOption, mAmbulanceOption, mOrgOption, mHospitalOption, mSearchBtn;
    Spinner mDistrictSpinner, mBloodSpinner;
    String districtStr="Choose District", bloodGroupStr="Choose Blood Group", searchFor = "donor";
    AreaData data = new AreaData();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_search, container, false);
        init(v);
        searchOptionHandle(v);
        districtAndBloodGroupSpinnerWork();

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                districtAndBloodGroupSpinnerWork();
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
                }

            }
        });

        return v;
    }

    private void searchOptionHandle(View v) {

        mDonarOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAllSearchButton();
                refreshTextColor();
                visibilityGoneAllDownPart();
                searchFor = "donor";
                mDonarOption.setBackground(getActivity().getDrawable(R.drawable.selected_search_option));
                mDonarOption.setTextColor(Color.WHITE);
                visibilibleAllDownPart();
            }
        });

        mAmbulanceOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAllSearchButton();
                refreshTextColor();
                visibilityGoneAllDownPart();
                searchFor = "ambulance";
                mAmbulanceOption.setBackground(getActivity().getDrawable(R.drawable.selected_search_option));
                mAmbulanceOption.setTextColor(Color.WHITE);
                visibilibleForOtherOptions();
            }
        });

        mOrgOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAllSearchButton();
                refreshTextColor();
                visibilityGoneAllDownPart();
                searchFor = "organization";
                mOrgOption.setBackground(getActivity().getDrawable(R.drawable.selected_search_option));
                mOrgOption.setTextColor(Color.WHITE);
                visibilibleForOtherOptions();
            }
        });

        mHospitalOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAllSearchButton();
                refreshTextColor();
                visibilityGoneAllDownPart();
                searchFor = "hospital";
                mHospitalOption.setBackground(getActivity().getDrawable(R.drawable.selected_search_option));
                mHospitalOption.setTextColor(Color.WHITE);
                visibilibleForOtherOptions();
            }
        });
    }

    private void refreshTextColor() {
        mDonarOption.setTextColor(Color.BLACK);
        mAmbulanceOption.setTextColor(Color.BLACK);
        mOrgOption.setTextColor(Color.BLACK);
        mHospitalOption.setTextColor(Color.BLACK);

    }

    private void refreshAllSearchButton() {
        mDonarOption.setBackground(getActivity().getDrawable(R.drawable.red_border));
        mAmbulanceOption.setBackground(getActivity().getDrawable(R.drawable.red_border));
        mOrgOption.setBackground(getActivity().getDrawable(R.drawable.red_border));
        mHospitalOption.setBackground(getActivity().getDrawable(R.drawable.red_border));

    }

    private void init(View v) {
        mDonarOption = v.findViewById(R.id.donar_search_btn);
        mAmbulanceOption = v.findViewById(R.id.ambulance_search_btn);
        mOrgOption = v.findViewById(R.id.org_search_btn);
        mHospitalOption = v.findViewById(R.id.hospital_search_btn);
        mSearchBtn = v.findViewById(R.id.search_text_button);
        mDistrictSpinner = v.findViewById(R.id.district_spinner);
        mBloodSpinner = v.findViewById(R.id.blood_spinner);
        visibilityGoneAllDownPart();
    }

    private void visibilityGoneAllDownPart(){
        mSearchBtn.setVisibility(View.GONE);
        mDistrictSpinner.setVisibility(View.GONE);
        mBloodSpinner.setVisibility(View.GONE);
    }

    private void visibilibleAllDownPart(){
        mSearchBtn.setVisibility(View.VISIBLE);
        mDistrictSpinner.setVisibility(View.VISIBLE);
        mBloodSpinner.setVisibility(View.VISIBLE);
    }

    private void visibilityGoneForOtherOptions(){
        mSearchBtn.setVisibility(View.GONE);
        mDistrictSpinner.setVisibility(View.GONE);
    }

    private void visibilibleForOtherOptions(){
        mSearchBtn.setVisibility(View.VISIBLE);
        mDistrictSpinner.setVisibility(View.VISIBLE);
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
