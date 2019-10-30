package com.example.bloodaid.backend.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.bloodaid.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminAmbulanceListHolder extends RecyclerView.ViewHolder {
    TextView nametxt,mobiletxt,districttxt,emailtxt,detailstxt;

    public AdminAmbulanceListHolder(@NonNull View itemView) {
        super(itemView);


        nametxt = itemView.findViewById(R.id.textView_adminAmbulanceList_ambulanceName);
        mobiletxt = itemView.findViewById(R.id.textView_adminAmbulanceList_mobile);
        districttxt = itemView.findViewById(R.id.textView_adminAmbulanceList_district);
        emailtxt = itemView.findViewById(R.id.textView_adminAmbulanceList_email);
        detailstxt = itemView.findViewById(R.id.textView_adminAmbulanceList_details);
    }
}
