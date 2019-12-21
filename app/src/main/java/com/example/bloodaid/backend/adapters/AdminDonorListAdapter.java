package com.example.bloodaid.backend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodaid.R;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AdminDonorListAdapter extends RecyclerView.Adapter<AdminDonorListHolder> {
    private Context context;
    private ArrayList<HashMap<String, String>> donorlist;


    public AdminDonorListAdapter(Context context, ArrayList<HashMap<String, String>> donorlist) {
        this.context = context;
        this.donorlist=donorlist;
    }

    @NonNull
    @Override
    public AdminDonorListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_donorlist,parent,false);
        return new AdminDonorListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminDonorListHolder holder, int position) {
        HashMap<String,String> hashMap = donorlist.get(position);

        holder.nametxt.setText(hashMap.get("name"));
        holder.mobiletxt.setText(hashMap.get("mobile"));
        holder.districttxt.setText(hashMap.get("district"));
        holder.bloodgrptxt.setText(hashMap.get("bloodgrp"));
        holder.counttxt.setText(hashMap.get("donatecount"));
        holder.lastdonatetxt.setText(hashMap.get("lastdonate"));

        if(hashMap.get("status").equals("1")){
            String status = "Available";
            holder.statustxt.setText(status);
            holder.statustxt.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        else{
            String status = "Not Available";
            holder.statustxt.setText(status);
            holder.statustxt.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return donorlist.size();
    }
}
