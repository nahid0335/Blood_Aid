package com.example.bloodaid.backend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodaid.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HospitalRequestFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hospitalrequest,container,false);
    }
}