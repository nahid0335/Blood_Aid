package com.example.bloodaid.backend.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.bloodaid.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminHospitalListHolder extends RecyclerView.ViewHolder {

    TextView nametxt,mobiletxt,districttxt,emailtxt,detailstxt;

    public AdminHospitalListHolder(@NonNull View itemView) {
        super(itemView);


        nametxt = itemView.findViewById(R.id.textView_adminHospitalList_name);
        mobiletxt = itemView.findViewById(R.id.textView_adminHospitalList_mobile);
        districttxt = itemView.findViewById(R.id.textView_adminHospitalList_district);
        emailtxt = itemView.findViewById(R.id.textView_adminHospitalList_email);
        detailstxt = itemView.findViewById(R.id.textView_adminHospitalList_details);
    }
}
