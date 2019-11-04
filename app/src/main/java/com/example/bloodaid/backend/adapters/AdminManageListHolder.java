package com.example.bloodaid.backend.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.bloodaid.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminManageListHolder extends RecyclerView.ViewHolder {

    TextView nametxt,mobiletxt,districttxt,emailtxt;

    public AdminManageListHolder(@NonNull View itemView) {
        super(itemView);


        nametxt = itemView.findViewById(R.id.textView_adminManage_name);
        mobiletxt = itemView.findViewById(R.id.textView_adminManage_mobile);
        districttxt = itemView.findViewById(R.id.textView_adminManage_dristict);
        emailtxt = itemView.findViewById(R.id.textView_adminManage_email);

    }
}
