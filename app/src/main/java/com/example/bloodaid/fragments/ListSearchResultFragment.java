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
import com.example.bloodaid.adapters.AmbulanceSearchResultAdapter;
import com.example.bloodaid.adapters.DonorSearchResultAdapter;
import com.example.bloodaid.adapters.HospitalSearchResultAdapter;
import com.example.bloodaid.adapters.OrgSearchResultAdapter;
import com.example.bloodaid.adapters.TopDonorAdapter;
import com.example.bloodaid.models.AmbulanceModelClass;
import com.example.bloodaid.models.DonorModelClass;
import com.example.bloodaid.models.HospitalModelClass;
import com.example.bloodaid.models.OrganizationModelClass;
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

    //donor
    ArrayList<DonorModelClass> donorList = new ArrayList<>();
    DonorSearchResultAdapter donorSearchResultAdapter;

    //hospital
    ArrayList<HospitalModelClass> hospitalList = new ArrayList<>();
    HospitalSearchResultAdapter hospitalSearchResultAdapter;

    //ambulance
    ArrayList<AmbulanceModelClass> ambulanceList = new ArrayList<>();
    AmbulanceSearchResultAdapter ambulanceSearchResultAdapter ;

    //organization
    ArrayList<OrganizationModelClass> organizations = new ArrayList<>();
    OrgSearchResultAdapter orgSearchResultAdapter ;

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
        else if(searchfor.equals("hospital")){
            fetchHospitalSearchResultFromDatabase();
        }
        else if(searchfor.equals("ambulance")){
            fetchAmbulanceSearchResultFromDatabase();
        }
        else if(searchfor.equals("organization")){
            fetchOrgSearchResultFromDatabase();
        }
        else{
            AllToasts.errorToast(getContext(), "Something went wrong !!!");
        }


        /*Log.d("TAGG0", getArguments().getString("searchfor"));
        Log.d("TAGG1", getArguments().getString("district"));
        Log.d("TAGG2", getArguments().getString("bloodgroup"));*/
        return view;
    }

    private void fetchOrgSearchResultFromDatabase() {

        final Call<ArrayList<OrganizationModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .orgSearchResult(district);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<OrganizationModelClass>>() {
            @Override
            public void onResponse(Call<ArrayList<OrganizationModelClass>> call, Response<ArrayList<OrganizationModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                }
                else {

                    ArrayList<OrganizationModelClass> responseList = response.body();
                    //Response parsing
                    for (OrganizationModelClass value : responseList) {
                        responseList.add(value);
                    }
                    organizations = responseList;
                    searchResultLayoutManager = new LinearLayoutManager(getContext());
                    searchResultRecycler.setLayoutManager(searchResultLayoutManager);
                    orgSearchResultAdapter = new OrgSearchResultAdapter(getContext(),
                            organizations);
                    searchResultRecycler.setAdapter(orgSearchResultAdapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OrganizationModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "OPPSS!! Failded to fetch database: "+t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("TAGG", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void fetchAmbulanceSearchResultFromDatabase() {
        final Call<ArrayList<AmbulanceModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .ambulanceSearchResult(district);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<AmbulanceModelClass>>() {
            @Override
            public void onResponse(Call<ArrayList<AmbulanceModelClass>> call, Response<ArrayList<AmbulanceModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                }
                else {

                    ArrayList<AmbulanceModelClass> responseList = response.body();
                    //Response parsing
                    /*for (HospitalModelClass value : responseList) {
                        hospitalList.add(value);
                    }*/
                    ambulanceList = responseList;
                    searchResultLayoutManager = new LinearLayoutManager(getContext());
                    searchResultRecycler.setLayoutManager(searchResultLayoutManager);
                    ambulanceSearchResultAdapter = new AmbulanceSearchResultAdapter(getContext(),
                            ambulanceList);
                    searchResultRecycler.setAdapter(ambulanceSearchResultAdapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AmbulanceModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "OPPSS!! Failded to fetch database: "+t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("TAGG", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void fetchHospitalSearchResultFromDatabase() {
        final Call<ArrayList<HospitalModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .hospitalrSearchResult(district);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        call.enqueue(new Callback<ArrayList<HospitalModelClass>>() {
            @Override
            public void onResponse(Call<ArrayList<HospitalModelClass>> call, Response<ArrayList<HospitalModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                }
                else {

                    ArrayList<HospitalModelClass> responseList = response.body();
                    //Response parsing
                    /*for (HospitalModelClass value : responseList) {
                        hospitalList.add(value);
                    }*/
                    hospitalList = responseList;
                    searchResultLayoutManager = new LinearLayoutManager(getContext());
                    searchResultRecycler.setLayoutManager(searchResultLayoutManager);
                    hospitalSearchResultAdapter = new HospitalSearchResultAdapter(getContext(),
                            hospitalList);
                    searchResultRecycler.setAdapter(hospitalSearchResultAdapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<HospitalModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "OPPSS!! Failded to fetch database: "+t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("TAGG", t.getMessage());
                progressDialog.dismiss();
            }
        });
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
                    /*for (DonorModelClass value : responseList) {
                        donorList.add(value);
                    }*/
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
