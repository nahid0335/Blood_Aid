package com.example.bloodaid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bloodaid.R;

public class AppInfoFragment extends Fragment {

    TextView mAppInfoTxt;
    public AppInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_app_info, container, false);
        mAppInfoTxt = v.findViewById(R.id.textView_app_info);
        String txt = "The 'Blood Aid' Blood Donor App puts the power to save lives in the palm of your hand." +
                " Donating blood, platelets and AB Plasma is easier than ever. " +
                "Follow your blood’s journey from donation through delivery " +
                "(when possible), and create or join a lifesaving team and track " +
                "its impact on a national leaderboard.";
        mAppInfoTxt.setText(txt);

        return v;
    }

}
