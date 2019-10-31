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

public class AdminHospitalListAdapter extends RecyclerView.Adapter<AdminHospitalListHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> hospitalList;


    public AdminHospitalListAdapter(Context context, ArrayList<HashMap<String, String>> hospitalList) {
        this.context = context;
        this.hospitalList=hospitalList;
    }



    @NonNull
    @Override
    public AdminHospitalListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_hospitallist,parent,false);
        return new AdminHospitalListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminHospitalListHolder holder, int position) {
        HashMap<String,String> hashMap = hospitalList.get(position);

        holder.nametxt.setText(hashMap.get("name"));
        holder.mobiletxt.setText(hashMap.get("mobile"));
        holder.districttxt.setText(hashMap.get("district"));
        holder.emailtxt.setText(hashMap.get("email"));
        holder.detailstxt.setText(hashMap.get("details"));
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }
}
