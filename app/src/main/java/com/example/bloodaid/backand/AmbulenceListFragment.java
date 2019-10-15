package com.example.bloodaid.backand;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodaid.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AmbulenceListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ambulencelist,container,false);
    }
}
