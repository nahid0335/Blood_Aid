package com.example.bloodaid.fragments;


import android.os.Bundle;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bloodaid.R;

public class HomeFragment extends Fragment {
    Group mBloodGroups;
    Button mBloodSearchIcon;
    Boolean bloodGroupShowState = true;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);

        //search bar start
        mBloodSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bloodGroupShowState){
                    mBloodGroups.setVisibility(View.GONE);
                    mBloodSearchIcon.setBackground(getResources().getDrawable(R.drawable.ic_search));
                    bloodGroupShowState = false;
                }
                else{
                    mBloodGroups.setVisibility(View.VISIBLE);
                    mBloodSearchIcon.setBackground(getResources().getDrawable(R.drawable.ic_cancel));
                    bloodGroupShowState = true;
                }
            }
        });

        //search bar end

        return v;
    }

    private void init(View v) {
        mBloodGroups = v.findViewById(R.id.blood_groups);
        mBloodSearchIcon = v.findViewById(R.id.search_btn);
    }

}
