package com.example.bloodaid.fragments;


import android.app.ProgressDialog;
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
import com.example.bloodaid.adapters.FeedListAdapter;
import com.example.bloodaid.models.BloodRequestModelClass;
import com.example.bloodaid.models.DonorRequestModelClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {
    ListView mFeedList;
    ArrayList<BloodRequestModelClass> arrayList = new ArrayList<>();

    public FeedFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_feed, container, false);

        mFeedList = view.findViewById(R.id.list_feed);

        ///
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //dataReady();
        final Call<ArrayList<BloodRequestModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorRequestsFeed();
        call.enqueue(new Callback<ArrayList<BloodRequestModelClass>>() {
            @Override
            public void onResponse(Call<ArrayList<BloodRequestModelClass>> call, Response<ArrayList<BloodRequestModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                }


                ArrayList<BloodRequestModelClass> responseList = response.body();

                //Response parsing
                for(BloodRequestModelClass value : responseList){
                    arrayList.add(value);
                }
                Log.d("TAG", responseList.toString());
                arrayList = responseList;
                FeedListAdapter feedListAdapter = new FeedListAdapter(getContext(), arrayList);
                mFeedList.setAdapter(feedListAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<BloodRequestModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "FAilded .", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

}
