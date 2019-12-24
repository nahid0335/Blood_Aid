package com.example.bloodaid.fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import com.example.bloodaid.models.DonorPositionModelClass;
import com.example.bloodaid.models.HospitalModelClass;
import com.example.bloodaid.models.OrganizationModelClass;
import com.example.bloodaid.models.TopDonorModelClass;
import com.example.bloodaid.utils.AreaData;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ListSearchResultFragment extends Fragment {

    String searchfor, district, bloodGroup;
    long area_id;
    AreaData data = new AreaData();

    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String DONOR_LOCATION = "donor_location";

    //private ArrayList<LatLng> pinpoint = new ArrayList<>();

    //donor
    List<DonorModelClass> donorList ;
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
                    if(responseList.get(0).getOrganizationId() == -1){
                        AllToasts.infoToast(getContext(),
                                "No data found !");
                    }
                    else{
                        organizations = responseList;
                        searchResultLayoutManager = new LinearLayoutManager(getContext());
                        searchResultRecycler.setLayoutManager(searchResultLayoutManager);
                        orgSearchResultAdapter = new OrgSearchResultAdapter(getContext(),
                                organizations);
                        searchResultRecycler.setAdapter(orgSearchResultAdapter);
                    }
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
                    if(responseList.get(0).getAmbulanceId() == -1){
                        AllToasts.infoToast(getContext(),
                                "No data found !");
                    }
                    else{
                        ambulanceList = responseList;
                        searchResultLayoutManager = new LinearLayoutManager(getContext());
                        searchResultRecycler.setLayoutManager(searchResultLayoutManager);
                        ambulanceSearchResultAdapter = new AmbulanceSearchResultAdapter(getContext(),
                                ambulanceList);
                        searchResultRecycler.setAdapter(ambulanceSearchResultAdapter);
                    }
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
                    if(responseList.get(0).getHospitalId() == -1){
                        AllToasts.infoToast(getContext(),
                                "No data found !");
                    }
                    else{
                        hospitalList = responseList;
                        searchResultLayoutManager = new LinearLayoutManager(getContext());
                        searchResultRecycler.setLayoutManager(searchResultLayoutManager);
                        hospitalSearchResultAdapter = new HospitalSearchResultAdapter(getContext(),
                                hospitalList);
                        searchResultRecycler.setAdapter(hospitalSearchResultAdapter);

                    }


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

        final Call<List<DonorModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorSearchResult(bloodGroup,district);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        call.enqueue(new Callback<List<DonorModelClass>>() {
            @Override
            public void onResponse(Call<List<DonorModelClass>> call, Response<List<DonorModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                else {

                    List<DonorModelClass> responseList = response.body();
                    if(responseList.get(0).getDonorId() == -1){
                        AllToasts.infoToast(getContext(),
                                "No data found !");
                    }else {

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

                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<DonorModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "OPPSS!! Failded to fetch database: "+t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("TAGG", t.getMessage());
                progressDialog.dismiss();
            }
        });

        //fetchDonorPositionFromDatabase();

    }



    /*private  void fetchDonorPositionFromDatabase(){

        final Call<List<DonorPositionModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorPosition(bloodGroup,district);
        call.enqueue(new Callback<List<DonorPositionModelClass>>() {
            @Override
            public void onResponse(Call<List<DonorPositionModelClass>> call, Response<List<DonorPositionModelClass>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                }
                else {

                    List<DonorPositionModelClass> responseList = response.body();
                    if(responseList.get(0).getDonorId() == -1){
                        AllToasts.infoToast(getContext(),
                                "No data found !");
                    }else {
                        for(DonorPositionModelClass position : responseList){
                            Double latitude = position.getLatitude();
                            Double longitude = position.getLongitude();
                            pinpoint.add(new LatLng(latitude,longitude));

                        }
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if(sharedPreferences.contains(DONOR_LOCATION)){
                            editor.remove(DONOR_LOCATION);
                            editor.apply();
                        }
                        Gson gson = new Gson();
                        String json = gson.toJson(pinpoint);
                        editor.putString(DONOR_LOCATION, json);
                        editor.apply();
                        AllToasts.successToast(getContext(),"save");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DonorPositionModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "OPPSS!! Failded to fetch database: "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
*/
}
