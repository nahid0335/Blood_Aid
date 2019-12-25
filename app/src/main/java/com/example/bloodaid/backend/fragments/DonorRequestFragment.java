package com.example.bloodaid.backend.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.backend.adapters.AdminDonorListAdapter;
import com.example.bloodaid.models.DonorRequestModelClass;

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

public class DonorRequestFragment extends Fragment {
    private ArrayList<HashMap<String, String>> donorRequestList;
    private AdminDonorListAdapter adminDonorListAdapter;
    private RecyclerView recyclerView;
    private Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_donorrequest,container,false);

        recyclerView = rootView.findViewById(R.id.recyclerView_adminDonor_requestItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        donorRequestList = new ArrayList<>();

       // Log.v("Tag","1st");
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final Call<List<DonorRequestModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorRequestList();

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {

                call.enqueue(new Callback<List<DonorRequestModelClass>>() {
                    @Override
                    public void onResponse(Call<List<DonorRequestModelClass>> call, Response<List<DonorRequestModelClass>> response) {

                        if(!response.isSuccessful()){
                            Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }


                        List<DonorRequestModelClass> arrayObjects = response.body();

                        //Response parsing
                        for(DonorRequestModelClass value : arrayObjects){

                            Integer donorRequestId = value.getDonorRequestId();
                            String name = value.getName();
                            String mobile = value.getMobile();
                            String district = value.getDistrict();
                            String bloodgrp = value.getBloodGroup();
                            Integer donatecount = value.getDonateCount();
                            String lastdonate = value.getLastDonate();
                            Integer status = value.getStatus();

                            HashMap<String,String> donorDetails = new HashMap<>();
                            donorDetails.put("donorrequestid",Integer.toString(donorRequestId));
                            donorDetails.put("name",name);
                            donorDetails.put("mobile",mobile);
                            donorDetails.put("district",district);
                            donorDetails.put("bloodgrp",bloodgrp);
                            donorDetails.put("donatecount",Integer.toString(donatecount));
                            donorDetails.put("lastdonate",lastdonate);
                            donorDetails.put("status",Integer.toString(status));

                            donorRequestList.add(donorDetails);

                        }
                        progressDialog.dismiss();
                        adminDonorListAdapter = new AdminDonorListAdapter(getContext(),donorRequestList);
                        recyclerView.setAdapter(adminDonorListAdapter);


                    }

                    @Override
                    public void onFailure(Call<List<DonorRequestModelClass>> call, Throwable t) {
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

    private final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.popup_neutral);
            ImageView closepopupimg = dialog.findViewById(R.id.imageView_popupNeutral_close);
            Button deletebtn = dialog.findViewById(R.id.button_popupNeutral_delete);
            Button acceptbtn = dialog.findViewById(R.id.button_popupNeutral_accept);
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
                    String donarrequestId = donorRequestList.get(viewHolder.getAdapterPosition()).get("donorrequestid");
                    final int idx = donorRequestList.indexOf(donorRequestList.get(viewHolder.getAdapterPosition()));


                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .deleteDonorRequest(Integer.valueOf(donarrequestId));

                    Thread thread =  new Thread(new Runnable() {
                        @Override
                        public void run() {
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String s = response.body().string();

                                        //Response parsing
                                        String status;
                                        if (s.isEmpty()) {
                                            status = "Failed";
                                            Toast.makeText(getContext(), status + " .", Toast.LENGTH_LONG).show();

                                        } else {
                                            JSONObject object = new JSONObject(s);
                                            status = object.getString("message");
                                            Toast.makeText(getContext(), status + " .", Toast.LENGTH_LONG).show();
                                            adminDonorListAdapter.notifyDataSetChanged();
                                            donorRequestList.remove(donorRequestList.get(idx));
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

            acceptbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String donarrequestId = donorRequestList.get(viewHolder.getAdapterPosition()).get("donorrequestid");
                    final int idx = donorRequestList.indexOf(donorRequestList.get(viewHolder.getAdapterPosition()));


                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .acceptDonorRequest(Integer.valueOf(donarrequestId));

                    Thread thread =  new Thread(new Runnable() {
                        @Override
                        public void run() {
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String s = response.body().string();

                                        //Response parsing
                                        String status;
                                        if (s.isEmpty()) {
                                            status = "Failed";
                                            Toast.makeText(getContext(), status + " .", Toast.LENGTH_LONG).show();

                                        } else {
                                            JSONObject object = new JSONObject(s);
                                            status = object.getString("message");
                                            Toast.makeText(getContext(), status + " .", Toast.LENGTH_LONG).show();
                                            adminDonorListAdapter.notifyDataSetChanged();
                                            donorRequestList.remove(donorRequestList.get(idx));
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
