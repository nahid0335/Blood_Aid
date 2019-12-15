package com.example.bloodaid.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.adapters.DonorSearchResultAdapter;
import com.example.bloodaid.adapters.TopDonorAdapter;
import com.example.bloodaid.models.DonorModelClass;
import com.example.bloodaid.models.TopDonorModelClass;
import com.example.bloodaid.utils.AreaData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSearchResultFragment extends Fragment {

    String searchfor, district, bloodGroup;
    long area_id;
    AreaData data = new AreaData();
    ArrayList<DonorModelClass> donorList = new ArrayList<>();
    DonorSearchResultAdapter donorSearchResultAdapter;
    RecyclerView searchResultRecycler;
    LinearLayoutManager searchResultLayoutManager;


    public ListSearchResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_search_result, container, false);
        searchResultRecycler =view.findViewById(R.id.recylerView_donorSearchResult);

        searchfor = getArguments().getString("searchfor");
        district = getArguments().getString("district");
        area_id = data.getAreaId(district);
        bloodGroup = getArguments().getString("bloodgroup");

        if(searchfor.equals("donor")){
            renameBloodGroup();
            fetchDonorSearchResultFromDatabase();
        }


        /*Log.d("TAGG0", getArguments().getString("searchfor"));
        Log.d("TAGG1", getArguments().getString("district"));
        Log.d("TAGG2", getArguments().getString("bloodgroup"));*/
        return view;
    }

    private void renameBloodGroup() {

        if(bloodGroup.equals("A+")){
            bloodGroup = "A_pos";
        }
        else if(bloodGroup.equals("A-")){
            bloodGroup = "A_neg";
        }
        else if(bloodGroup.equals("B+")){
            bloodGroup = "B_pos";
        }
        else if(bloodGroup.equals("B-")){
            bloodGroup = "B_neg";
        }
        else if(bloodGroup.equals("O+")){
            bloodGroup = "O_pos";
        }
        else if(bloodGroup.equals("O-")){
            bloodGroup = "O_neg";
        }
        else if(bloodGroup.equals("AB+")){
            bloodGroup = "AB_pos";
        }
        else if(bloodGroup.equals("AB-")){
            bloodGroup = "AB_neg";
        }
        else{
            AllToasts.errorToast(getContext(), "Something went wrong on blood group selection !");
        }


    }

    private  void fetchDonorSearchResultFromDatabase(){


        final Call<ArrayList<DonorModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorSearchResult(bloodGroup,district);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<DonorModelClass>>() {
            @Override
            public void onResponse(Call<ArrayList<DonorModelClass>> call, Response<ArrayList<DonorModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                }
                else {

                    ArrayList<DonorModelClass> responseList = response.body();
                    //Response parsing
                    for (DonorModelClass value : responseList) {
                        donorList.add(value);
                    }
                    donorList = responseList;
                    searchResultLayoutManager = new LinearLayoutManager(getContext());
                    searchResultRecycler.setLayoutManager(searchResultLayoutManager);
                    donorSearchResultAdapter = new DonorSearchResultAdapter(getContext(),
                            donorList);
                    searchResultRecycler.setAdapter(donorSearchResultAdapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DonorModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "OPPSS!! Failded to fetch database: "+t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("TAGG", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

}
