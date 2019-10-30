package com.example.bloodaid.backend.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.bloodaid.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminDonorListHolder extends RecyclerView.ViewHolder {
    TextView nametxt,mobiletxt,statustxt,counttxt,districttxt,bloodgrptxt,lastdonatetxt;

    public AdminDonorListHolder(@NonNull View itemView) {
        super(itemView);

        nametxt = itemView.findViewById(R.id.textView_adminDonorList_donorName);
        mobiletxt = itemView.findViewById(R.id.textView_adminDonorList_mobile);
        districttxt = itemView.findViewById(R.id.textView_adminDonorList_district);
        bloodgrptxt = itemView.findViewById(R.id.textView_adminDonorList_bloodGroup);
        counttxt = itemView.findViewById(R.id.textView_adminDonorList_donateCount);
        lastdonatetxt = itemView.findViewById(R.id.textView_adminDonorList_lastDonate);
        statustxt = itemView.findViewById(R.id.textView_adminDonorList_status);


    }
}
