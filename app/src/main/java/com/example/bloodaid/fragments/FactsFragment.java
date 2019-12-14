package com.example.bloodaid.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.bloodaid.R;

public class FactsFragment extends Fragment {

    WebView mFactsLoader;
    public FactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_facts, container, false);
        mFactsLoader = v.findViewById(R.id.webView_fragmentFacts_htmlLoader);
        mFactsLoader.loadUrl("file:///android_asset/facts.html");

        return v;
    }

}
