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

public class AdminAmbulanceListadapter extends RecyclerView.Adapter<AdminAmbulanceListHolder> {
    private Context context;
    private ArrayList<HashMap<String, String>> ambulanceList;


    public AdminAmbulanceListadapter(Context context, ArrayList<HashMap<String, String>> ambulanceList) {
        this.context = context;
        this.ambulanceList=ambulanceList;
    }



    @NonNull
    @Override
    public AdminAmbulanceListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ambulancelist,parent,false);
        return new AdminAmbulanceListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAmbulanceListHolder holder, int position) {
        HashMap<String,String> hashMap = ambulanceList.get(position);

        holder.nametxt.setText(hashMap.get("name"));
        holder.mobiletxt.setText(hashMap.get("mobile"));
        holder.districttxt.setText(hashMap.get("district"));
        holder.emailtxt.setText(hashMap.get("email"));
        holder.detailstxt.setText(hashMap.get("details"));
    }

    @Override
    public int getItemCount() {
        return ambulanceList.size();
    }
}
