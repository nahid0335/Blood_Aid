package com.example.bloodaid.backend.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.bloodaid.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminOrganizationListHolder extends RecyclerView.ViewHolder {

    TextView nametxt,mobiletxt,districttxt,emailtxt,detailstxt;

    public AdminOrganizationListHolder(@NonNull View itemView) {
        super(itemView);


        nametxt = itemView.findViewById(R.id.textView_adminOrganizationList_organizationName);
        mobiletxt = itemView.findViewById(R.id.textView_adminOrganizationList_mobile);
        districttxt = itemView.findViewById(R.id.textView_adminOrganizationList_district);
        emailtxt = itemView.findViewById(R.id.textView_adminOrganizationList_email);
        detailstxt = itemView.findViewById(R.id.textView_adminOrganizationList_details);
    }
}
