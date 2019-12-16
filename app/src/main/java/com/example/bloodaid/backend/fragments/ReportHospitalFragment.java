package com.example.bloodaid.backend.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.backend.adapters.AdminReportListAdapter;
import com.example.bloodaid.models.ReportDonorModelClass;
import com.example.bloodaid.models.ReportHospitalModelClass;

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

public class ReportHospitalFragment extends Fragment {

    private ArrayList<HashMap<String, String>> reportHospitalList;
    private AdminReportListAdapter adminReportListAdapter;
    private RecyclerView recyclerView;
    private Dialog dialog;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reporthospital,container,false);

        recyclerView = rootView.findViewById(R.id.recyclerView_adminReport_hospitalList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reportHospitalList = new ArrayList<>();

        // Log.v("Tag","1st");
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final Call<List<ReportHospitalModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .reportHospitalList();

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {

                call.enqueue(new Callback<List<ReportHospitalModelClass>>() {
                    @Override
                    public void onResponse(Call<List<ReportHospitalModelClass>> call, Response<List<ReportHospitalModelClass>> response) {

                        if(!response.isSuccessful()){
                            Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }


                        List<ReportHospitalModelClass> arrayObjects = response.body();

                        //Response parsing
                        for(ReportHospitalModelClass value : arrayObjects){

                            Integer reportHospitalId = value.getReportHospitalId();
                            String name = value.getName();
                            String mobile = value.getMobile();
                            String district = value.getDistrict();
                            String email = value.getEmail();
                            Integer count = value.getReportCount();

                            HashMap<String,String> reportHospitalDetails = new HashMap<>();
                            reportHospitalDetails.put("reporthospitalid",Integer.toString(reportHospitalId));
                            reportHospitalDetails.put("name",name);
                            reportHospitalDetails.put("mobile",mobile);
                            reportHospitalDetails.put("district",district);
                            reportHospitalDetails.put("email",email);
                            reportHospitalDetails.put("reportcount",Integer.toString(count));

                            reportHospitalList.add(reportHospitalDetails);

                        }
                        adminReportListAdapter = new AdminReportListAdapter(getContext(),reportHospitalList);
                        recyclerView.setAdapter(adminReportListAdapter);
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Call<List<ReportHospitalModelClass>> call, Throwable t) {
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
                    String reportHospitalId = reportHospitalList.get(viewHolder.getAdapterPosition()).get("reporthospitalid");
                    final int idx = reportHospitalList.indexOf(reportHospitalList.get(viewHolder.getAdapterPosition()));


                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .deleteReportHospital(Integer.valueOf(reportHospitalId));

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
                                            reportHospitalList.remove(reportHospitalList.get(idx));
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
                    String reportHospitalId = reportHospitalList.get(viewHolder.getAdapterPosition()).get("reporthospitalid");
                    final int idx = reportHospitalList.indexOf(reportHospitalList.get(viewHolder.getAdapterPosition()));


                    final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                            .create(BloodAidService.class)
                            .deleteReportHospitalAccount(Integer.valueOf(reportHospitalId));

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
                                            reportHospitalList.remove(reportHospitalList.get(idx));
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
