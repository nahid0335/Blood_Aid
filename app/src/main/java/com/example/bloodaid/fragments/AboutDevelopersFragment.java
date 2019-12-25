package com.example.bloodaid.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodaid.R;

import static android.content.Context.MODE_PRIVATE;
import static com.example.bloodaid.fragments.TopDonorFragment.SHARED_PREFerence_Key;

public class AboutDevelopersFragment extends Fragment {


    public AboutDevelopersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about_developers, container, false);


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("anotherFragment", true);
        editor.apply();

        return v;
    }

}
