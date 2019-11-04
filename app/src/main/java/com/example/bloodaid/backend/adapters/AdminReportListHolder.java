package com.example.bloodaid.backend.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.bloodaid.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminReportListHolder extends RecyclerView.ViewHolder {

    TextView nametxt,mobiletxt,districttxt,emailtxt,reportcounttxt;

    public AdminReportListHolder(@NonNull View itemView) {
        super(itemView);


        nametxt = itemView.findViewById(R.id.textView_adminReport_listName);
        mobiletxt = itemView.findViewById(R.id.textView_adminReport_mobile);
        districttxt = itemView.findViewById(R.id.textView_adminReport_dristict);
        emailtxt = itemView.findViewById(R.id.textView_adminReport_email);
        reportcounttxt = itemView.findViewById(R.id.textView_adminReport_count);
    }
}
