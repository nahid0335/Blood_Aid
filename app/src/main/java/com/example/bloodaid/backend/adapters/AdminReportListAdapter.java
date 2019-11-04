package com.example.bloodaid.backend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodaid.R;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminReportListAdapter extends RecyclerView.Adapter<AdminReportListHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> reportList;


    public AdminReportListAdapter(Context context, ArrayList<HashMap<String, String>> reportList) {
        this.context = context;
        this.reportList=reportList;
    }


    @NonNull
    @Override
    public AdminReportListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reportlist,parent,false);
        return new AdminReportListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminReportListHolder holder, int position) {
        HashMap<String,String> hashMap = reportList.get(position);

        holder.nametxt.setText(hashMap.get("name"));
        holder.mobiletxt.setText(hashMap.get("mobile"));
        holder.districttxt.setText(hashMap.get("district"));
        holder.emailtxt.setText(hashMap.get("email"));
        holder.reportcounttxt.setText(hashMap.get("reportcount"));
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
