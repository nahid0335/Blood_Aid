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

public class AdminOrganizationListAdapter extends RecyclerView.Adapter<AdminOrganizationListHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> organizationList;


    public AdminOrganizationListAdapter(Context context, ArrayList<HashMap<String, String>> organizationList) {
        this.context = context;
        this.organizationList=organizationList;
    }


    @NonNull
    @Override
    public AdminOrganizationListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_organizationlist,parent,false);
        return new AdminOrganizationListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrganizationListHolder holder, int position) {
        HashMap<String,String> hashMap = organizationList.get(position);

        holder.nametxt.setText(hashMap.get("name"));
        holder.mobiletxt.setText(hashMap.get("mobile"));
        holder.districttxt.setText(hashMap.get("district"));
        holder.emailtxt.setText(hashMap.get("email"));
        holder.detailstxt.setText(hashMap.get("details"));
    }

    @Override
    public int getItemCount() {
        return organizationList.size();
    }
}
