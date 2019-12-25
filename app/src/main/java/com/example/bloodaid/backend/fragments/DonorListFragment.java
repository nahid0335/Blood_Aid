package com.example.bloodaid.backend.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.backend.adapters.AdminDonorListAdapter;
import com.example.bloodaid.models.DonorModelClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonorListFragment extends Fragment {

    ArrayList<HashMap<String, String>> donorList;
    AdminDonorListAdapter adminDonorListAdapter;
    private RecyclerView recyclerView;
    Dialog dialog;
    ImageView closepopupimg;
    Button deletebtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_donorlist,container,false);

        recyclerView = rootView.findViewById(R.id.recyclerView_adminDonor_listItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        donorList = new ArrayList<>();


        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final Call<List<DonorModelClass> > call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorList();

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<List<DonorModelClass>>() {
                    @Override
                    public void onResponse(Call<List<DonorModelClass>> call, Response<List<DonorModelClass>> response) {

                        if(!response.isSuccessful()){
                            Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }else{
                            List<DonorModelClass> arrayObjects = response.body();

                            if(arrayObjects.get(0).getDonorId() == -1){
                                AllToasts.infoToast(getContext(),
                                        "No data found !");
                                progressDialog.dismiss();
                            }else{
                                //Response parsing
                                for(DonorModelClass value : arrayObjects){

                                    Integer donorId = value.getDonorId();
                                    String name = value.getName();
                                    String mobile = value.getMobile();
                                    String district = value.getDistrict();
                                    String bloodgrp = value.getBloodGroup();
                                    Integer donatecount = value.getDonateCount();
                                    String lastdonate = value.getLastDonate();
                                    Integer status = value.getStatus();

                                    HashMap<String,String> donorDetails = new HashMap<>();
                                    donorDetails.put("donorid",Integer.toString(donorId));
                                    donorDetails.put("name",name);
                                    donorDetails.put("mobile",mobile);
                                    donorDetails.put("district",district);
                                    donorDetails.put("bloodgrp",bloodgrp);
                                    donorDetails.put("donatecount",Integer.toString(donatecount));
                                    donorDetails.put("lastdonate",lastdonate);
                                    donorDetails.put("status",Integer.toString(status));

                                    donorList.add(donorDetails);

                                }
                                progressDialog.dismiss();
                                adminDonorListAdapter = new AdminDonorListAdapter(getContext(),donorList);
                                recyclerView.setAdapter(adminDonorListAdapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DonorModelClass>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage()+" .", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
        progressDialog.show();
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        return rootView;
    }



    final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.popup_negative);
            closepopupimg = dialog.findViewById(R.id.imageView_popupNegative_close);
            deletebtn = dialog.findViewById(R.id.button_popupNegative_delete);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);

            closepopupimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adminDonorListAdapter.notifyDataSetChanged();
                    dialog.dismiss();

                }
            });

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String donarId = donorList.get(viewHolder.getAdapterPosition()).get("donorid");
                    final int idx = donorList.indexOf(donorList.get(viewHolder.getAdapterPosition()));


                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .deleteDonor(Integer.valueOf(donarId));

                    Thread thread =  new Thread(new Runnable() {
                        @Override
                        public void run() {
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String status;
                                        if(!response.isSuccessful()){
                                            status = "Failed";
                                            Toast.makeText(getContext(), status + " .", Toast.LENGTH_LONG).show();
                                        } else {
                                            String s = response.body().string();
                                            JSONObject object = new JSONObject(s);
                                            status = object.getString("message");
                                            if(status.equals("Donor Deleted")){
                                                adminDonorListAdapter.notifyDataSetChanged();
                                                donorList.remove(donorList.get(idx));
                                            }
                                            else{
                                                adminDonorListAdapter.notifyDataSetChanged();
                                            }
                                            Toast.makeText(getContext(), status + " .", Toast.LENGTH_LONG).show();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), e.getMessage() + " - JSON", Toast.LENGTH_LONG).show();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), e.getMessage() + " - IO", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getContext(), t.getMessage() + " .", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();
        }
    };
}
