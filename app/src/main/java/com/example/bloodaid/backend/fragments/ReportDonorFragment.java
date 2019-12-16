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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.backend.adapters.AdminOrganizationListAdapter;
import com.example.bloodaid.backend.adapters.AdminReportListAdapter;
import com.example.bloodaid.models.OrganizationRequestModelClass;
import com.example.bloodaid.models.ReportDonorModelClass;

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

public class ReportDonorFragment extends Fragment {
    private ArrayList<HashMap<String, String>> reportDonorList;
    private AdminReportListAdapter adminReportListAdapter;
    private RecyclerView recyclerView;
    private Dialog dialog;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reportdonor,container,false);

        recyclerView = rootView.findViewById(R.id.recyclerView_adminReport_donorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reportDonorList = new ArrayList<>();

        // Log.v("Tag","1st");
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final Call<List<ReportDonorModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .reportDonorList();

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {

                call.enqueue(new Callback<List<ReportDonorModelClass>>() {
                    @Override
                    public void onResponse(Call<List<ReportDonorModelClass>> call, Response<List<ReportDonorModelClass>> response) {

                        if(!response.isSuccessful()){
                            Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }


                        List<ReportDonorModelClass> arrayObjects = response.body();

                        //Response parsing
                        for(ReportDonorModelClass value : arrayObjects){

                            Integer reportDonorId = value.getReportDonorId();
                            String name = value.getName();
                            String mobile = value.getMobile();
                            String district = value.getDistrict();
                            String email = value.getEmail();
                            Integer count = value.getReportCount();

                            HashMap<String,String> reportDonorDetails = new HashMap<>();
                            reportDonorDetails.put("reportdonorid",Integer.toString(reportDonorId));
                            reportDonorDetails.put("name",name);
                            reportDonorDetails.put("mobile",mobile);
                            reportDonorDetails.put("district",district);
                            reportDonorDetails.put("email",email);
                            reportDonorDetails.put("reportcount",Integer.toString(count));

                            reportDonorList.add(reportDonorDetails);

                        }
                        adminReportListAdapter = new AdminReportListAdapter(getContext(),reportDonorList);
                        recyclerView.setAdapter(adminReportListAdapter);
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Call<List<ReportDonorModelClass>> call, Throwable t) {
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
            dialog.setContentView(R.layout.popup_reportdismiss);
            ImageView closepopupimg = dialog.findViewById(R.id.imageView_popupReport_cancel);
            TextView deletetxt = dialog.findViewById(R.id.textView_popupReport_delete);
            TextView removetxt = dialog.findViewById(R.id.textView_popupReport_remove);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);

            closepopupimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adminReportListAdapter.notifyDataSetChanged();
                    dialog.dismiss();

                }
            });

            removetxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reportDonorId = reportDonorList.get(viewHolder.getAdapterPosition()).get("reportdonorid");
                    final int idx = reportDonorList.indexOf(reportDonorList.get(viewHolder.getAdapterPosition()));


                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .deleteReportDonor(Integer.valueOf(reportDonorId));

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
                                            adminReportListAdapter.notifyDataSetChanged();
                                            reportDonorList.remove(reportDonorList.get(idx));
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

            deletetxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reportDonorId = reportDonorList.get(viewHolder.getAdapterPosition()).get("reportdonorid");
                    final int idx = reportDonorList.indexOf(reportDonorList.get(viewHolder.getAdapterPosition()));


                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .deleteReportDonorAccount(Integer.valueOf(reportDonorId));

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
                                            adminReportListAdapter.notifyDataSetChanged();
                                            reportDonorList.remove(reportDonorList.get(idx));
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
