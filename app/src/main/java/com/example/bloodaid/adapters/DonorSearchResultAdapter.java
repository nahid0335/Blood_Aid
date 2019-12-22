package com.example.bloodaid.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.R;
import com.example.bloodaid.models.DonorModelClass;
import com.example.bloodaid.models.TopDonorModelClass;

import java.util.ArrayList;
import java.util.List;

public class DonorSearchResultAdapter extends RecyclerView.Adapter<DonorSearchResultAdapter.DonorSearchResultViewHolder> {
    private Context context;
    private List<DonorModelClass> donorList ;

    public DonorSearchResultAdapter(Context context, List<DonorModelClass> donorList) {
        this.context = context;
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public DonorSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_search_result_row,parent,false);
        return new DonorSearchResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DonorSearchResultViewHolder holder, int position) {
        final DonorModelClass donor = donorList.get(position);

        holder.bloodgroupTxt.setText(donor.getBloodGroup());
        holder.nametxt.setText(donor.getName());
        holder.districttxt.setText(donor.getDistrict());
        holder.phoneNumberTxt.setText(donor.getMobile());
        holder.totalDonationTxt.setText("Total Donate: "+donor.getDonateCount()+ " times");
        holder.lastDonationTxt.setText("Last Donation: "+donor.getLastDonate());

        holder.msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:"+donor.getMobile());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                if(i.resolveActivity(context.getPackageManager()) != null){
                    context.startActivity(i);
                }
                AllToasts.infoToast(view.getContext(), "MESSAGE to: "+donor.getMobile());
            }
        });
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:"+donor.getMobile());
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                context.startActivity(i);
                AllToasts.infoToast(view.getContext(), "CALL to: "+donor.getMobile());
            }
        });
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public class DonorSearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView bloodgroupTxt, nametxt,districttxt, bloodGroupTxt,
                phoneNumberTxt, lastDonationTxt, totalDonationTxt;

        Button callBtn, msgBtn;

        public DonorSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodgroupTxt = itemView.findViewById(R.id.blood_group_text);
            nametxt = itemView.findViewById(R.id.name_text);
            districttxt = itemView.findViewById(R.id.district_text);
            phoneNumberTxt = itemView.findViewById(R.id.phone_number_text);
            totalDonationTxt = itemView.findViewById(R.id.total_donate_text);
            lastDonationTxt = itemView.findViewById(R.id.last_donate_text);
            callBtn = itemView.findViewById(R.id.call_btn);
            msgBtn = itemView.findViewById(R.id.message_btn);
        }
    }



}

