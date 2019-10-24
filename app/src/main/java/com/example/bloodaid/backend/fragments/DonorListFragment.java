package com.example.bloodaid.backend.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.backend.AdminHome;
import com.example.bloodaid.backend.AdminLoginActivity;
import com.example.bloodaid.backend.DonorActivity;
import com.example.bloodaid.backend.adapters.AdminDonorListAdapter;
import com.example.bloodaid.models.DonorModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonorListFragment extends Fragment {

    ArrayList<HashMap<String, String>> donorList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_donorlist,container,false);

        final RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView_adminDonor_listItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        donorList = new ArrayList<>();

        final Call<List<DonorModelClass> > call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorList();

        new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<List<DonorModelClass>>() {
                    @Override
                    public void onResponse(Call<List<DonorModelClass>> call, Response<List<DonorModelClass>> response) {

                            if(!response.isSuccessful()){
                                Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                            }


                        List<DonorModelClass> arrayObjects = response.body();

                            //Response parsing
                            for(DonorModelClass value : arrayObjects){

                                String name = value.getName();
                                String mobile = value.getMobile();
                                String district = value.getDistrict();
                                String bloodgrp = value.getBloodGroup();
                                Integer donatecount = value.getDonateCount();
                                String lastdonate = value.getLastDonate();
                                Integer status = value.getStatus();

                                HashMap<String,String> donorDetails = new HashMap<>();
                                donorDetails.put("name",name);
                                donorDetails.put("mobile",mobile);
                                donorDetails.put("district",district);
                                donorDetails.put("bloodgrp",bloodgrp);
                                donorDetails.put("donatecount",Integer.toString(donatecount));
                                donorDetails.put("lastdonate",lastdonate);
                                donorDetails.put("status",Integer.toString(status));

                                donorList.add(donorDetails);


                                AdminDonorListAdapter adminDonorListAdapter = new AdminDonorListAdapter(getContext(),donorList);
                                recyclerView.setAdapter(adminDonorListAdapter);
                            }

                    }

                    @Override
                    public void onFailure(Call<List<DonorModelClass>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage()+" .", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();




        return rootView;
    }
}
