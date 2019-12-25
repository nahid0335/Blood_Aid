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

import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.backend.adapters.AdminHospitalListAdapter;
import com.example.bloodaid.backend.adapters.AdminOrganizationListAdapter;
import com.example.bloodaid.models.HospitalRequestModelClass;
import com.example.bloodaid.models.OrganizationRequestModelClass;

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

public class OrganizationRequestFragment extends Fragment {

    private ArrayList<HashMap<String, String>> organizationRequestList;
    private AdminOrganizationListAdapter adminOrganizationListAdapter;
    private RecyclerView recyclerView;
    private Dialog dialog;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_organizationrequest,container,false);

        recyclerView = rootView.findViewById(R.id.recyclerView_adminOrganization_requestItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        organizationRequestList = new ArrayList<>();

        // Log.v("Tag","1st");
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final Call<List<OrganizationRequestModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .organizationRequestList();

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {

                call.enqueue(new Callback<List<OrganizationRequestModelClass>>() {
                    @Override
                    public void onResponse(Call<List<OrganizationRequestModelClass>> call, Response<List<OrganizationRequestModelClass>> response) {

                        if(!response.isSuccessful()){
                            Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }


                        List<OrganizationRequestModelClass> arrayObjects = response.body();

                        //Response parsing
                        for(OrganizationRequestModelClass value : arrayObjects){

                            Integer organizationRequestId = value.getOrganizationRequestId();
                            String name = value.getName();
                            String mobile = value.getMobile();
                            String district = value.getDistrict();
                            String email = value.getEmail();
                            String details = value.getDetails();

                            HashMap<String,String> organizationDetails = new HashMap<>();
                            organizationDetails.put("organizationrequestid",Integer.toString(organizationRequestId));
                            organizationDetails.put("name",name);
                            organizationDetails.put("mobile",mobile);
                            organizationDetails.put("district",district);
                            organizationDetails.put("email",email);
                            organizationDetails.put("details",details);

                            organizationRequestList.add(organizationDetails);

                        }
                        progressDialog.dismiss();
                        adminOrganizationListAdapter = new AdminOrganizationListAdapter(getContext(),organizationRequestList);
                        recyclerView.setAdapter(adminOrganizationListAdapter);


                    }

                    @Override
                    public void onFailure(Call<List<OrganizationRequestModelClass>> call, Throwable t) {
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
                    adminOrganizationListAdapter.notifyDataSetChanged();
                    dialog.dismiss();

                }
            });

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String organizationrequestId = organizationRequestList.get(viewHolder.getAdapterPosition()).get("organizationrequestid");
                    final int idx = organizationRequestList.indexOf(organizationRequestList.get(viewHolder.getAdapterPosition()));


                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .deleteOrganizationRequest(Integer.valueOf(organizationrequestId));

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
                                            adminOrganizationListAdapter.notifyDataSetChanged();
                                            organizationRequestList.remove(organizationRequestList.get(idx));
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
                    String organizationId = organizationRequestList.get(viewHolder.getAdapterPosition()).get("organizationrequestid");
                    final int idx = organizationRequestList.indexOf(organizationRequestList.get(viewHolder.getAdapterPosition()));


                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .acceptOrganizationRequest(Integer.valueOf(organizationId));

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
                                            adminOrganizationListAdapter.notifyDataSetChanged();
                                            organizationRequestList.remove(organizationRequestList.get(idx));
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
