package com.example.bloodaid.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.backend.adapters.AdminDonorListAdapter;
import com.example.bloodaid.models.DonorRequestModelClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {
    ListView mFeedList;
    FeedListAdapter feedListAdapter;
    ArrayList<DonorRequestModelClass> arrayList = new ArrayList<>();

    public FeedFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_feed, container, false);

        mFeedList = view.findViewById(R.id.list_feed);

        //dataReady();
        final Call<ArrayList<DonorRequestModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorRequestsFeed();
        call.enqueue(new Callback<ArrayList<DonorRequestModelClass>>() {
            @Override
            public void onResponse(Call<ArrayList<DonorRequestModelClass>> call, Response<ArrayList<DonorRequestModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                }


                ArrayList<DonorRequestModelClass> responseList = response.body();

                //Response parsing
                for(DonorRequestModelClass value : responseList){
                    responseList.add(value);
                }
                Log.d("TAG", responseList.toString());
                arrayList = responseList;
                FeedListAdapter feedListAdapter = new FeedListAdapter(getContext(), arrayList);
                mFeedList.setAdapter(feedListAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<DonorRequestModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "FAilded .", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    private void dataReady() {
        final Call<ArrayList<DonorRequestModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorRequestsFeed();

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {

                call.enqueue(new Callback<ArrayList<DonorRequestModelClass>>() {
                    @Override
                    public void onResponse(Call<ArrayList<DonorRequestModelClass>> call, Response<ArrayList<DonorRequestModelClass>> response) {

                        if(!response.isSuccessful()){
                            Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                        }


                        ArrayList<DonorRequestModelClass> responseList = response.body();

                        //Response parsing
                        for(DonorRequestModelClass value : responseList){
                            responseList.add(value);
                        }
                        Log.d("TAG", responseList.toString());
                        arrayList = responseList;
                        FeedListAdapter feedListAdapter = new FeedListAdapter(getContext(), arrayList);
                        mFeedList.setAdapter(feedListAdapter);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<DonorRequestModelClass>> call, Throwable t) {
                        Toast.makeText(getContext(), "FAilded .", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        t.start();
    }

}
