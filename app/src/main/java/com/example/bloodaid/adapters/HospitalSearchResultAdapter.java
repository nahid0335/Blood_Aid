package com.example.bloodaid.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.R;
import com.example.bloodaid.models.DonorModelClass;
import com.example.bloodaid.models.HospitalModelClass;

import java.util.ArrayList;

public class HospitalSearchResultAdapter extends RecyclerView.Adapter<HospitalSearchResultAdapter.HospitalSearchResultViewHolder> {
    private Context context;
    private ArrayList<HospitalModelClass> hospitals =  new ArrayList<>();

    public HospitalSearchResultAdapter(Context context, ArrayList<HospitalModelClass> hospitals) {
        this.context = context;
        this.hospitals = hospitals;
    }

    @NonNull
    @Override
    public HospitalSearchResultAdapter.HospitalSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_search_row,parent,false);
        return new HospitalSearchResultAdapter.HospitalSearchResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HospitalSearchResultAdapter.HospitalSearchResultViewHolder holder, int position) {
        final HospitalModelClass hospital = hospitals.get(position);


        holder.nametxt.setText(hospital.getName());
        holder.districttxt.setText(hospital.getDistrict());
        holder.phoneNumberTxt.setText(hospital.getMobile());
        holder.detailsTxt.setText("Details: "+hospital.getDetails());

        holder.msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:"+hospital.getMobile());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                if(i.resolveActivity(context.getPackageManager()) != null){
                    context.startActivity(i);
                }
                AllToasts.infoToast(view.getContext(), "MESSAGE to: "+hospital.getMobile());
            }
        });
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:"+hospital.getMobile());
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                context.startActivity(i);
                AllToasts.infoToast(view.getContext(), "CALL to: "+hospital.getMobile());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public class HospitalSearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView nametxt,districttxt, detailsTxt,
                phoneNumberTxt;

        Button callBtn, msgBtn;

        public HospitalSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);

            nametxt = itemView.findViewById(R.id.name_text);
            districttxt = itemView.findViewById(R.id.district_text);
            phoneNumberTxt = itemView.findViewById(R.id.phone_number_text);
            detailsTxt = itemView.findViewById(R.id.hospital_details_text);

            callBtn = itemView.findViewById(R.id.call_btn);
            msgBtn = itemView.findViewById(R.id.message_btn);
        }
    }



}
