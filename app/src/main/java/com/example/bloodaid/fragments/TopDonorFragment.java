package com.example.bloodaid.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.adapters.FeedListAdapter;
import com.example.bloodaid.adapters.TopDonorAdapter;
import com.example.bloodaid.models.DonorRequestModelClass;
import com.example.bloodaid.models.TopDonorModelClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class TopDonorFragment extends Fragment {
    ArrayList<TopDonorModelClass> topDonorList = new ArrayList<>();
    TopDonorAdapter topDonorAdapter;
    RecyclerView recyclerView;

    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";

    public TopDonorFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_donor, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_topDonor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("anotherFragment", true);
        editor.apply();


        //data ready
        dataFetchFromDatabase();

        return view;
    }

    private void dataFetchFromDatabase() {
        //dataReady();
        final Call<ArrayList<TopDonorModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .topDonor();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<TopDonorModelClass>>() {
            @Override
            public void onResponse(Call<ArrayList<TopDonorModelClass>> call, Response<ArrayList<TopDonorModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                }
                else {

                    ArrayList<TopDonorModelClass> responseList = response.body();
                    //Response parsing
                    for (TopDonorModelClass value : responseList) {
                        topDonorList.add(value);
                    }
                    //Log.d("TAG", responseList.toString());
                    topDonorList = responseList;
                    topDonorAdapter = new TopDonorAdapter(getContext(), topDonorList);
                    recyclerView.setAdapter(topDonorAdapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TopDonorModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "OPPSS!! Failded to fetch database.", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }


}
