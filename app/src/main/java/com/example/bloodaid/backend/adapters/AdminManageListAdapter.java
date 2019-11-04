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

public class AdminManageListAdapter extends RecyclerView.Adapter<AdminManageListHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> adminList;


    public AdminManageListAdapter(Context context, ArrayList<HashMap<String, String>> adminList) {
        this.context = context;
        this.adminList=adminList;
    }



    @NonNull
    @Override
    public AdminManageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adminlist,parent,false);
        return new AdminManageListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminManageListHolder holder, int position) {
        HashMap<String,String> hashMap = adminList.get(position);

        holder.nametxt.setText(hashMap.get("name"));
        holder.mobiletxt.setText(hashMap.get("mobile"));
        holder.districttxt.setText(hashMap.get("district"));
        holder.emailtxt.setText(hashMap.get("email"));
    }

    @Override
    public int getItemCount() {
        return adminList.size();
    }
}
