package com.example.bloodaid.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.R;
import com.example.bloodaid.models.AmbulanceModelClass;
import com.example.bloodaid.models.HospitalModelClass;

import java.util.ArrayList;

public class AmbulanceSearchResultAdapter extends RecyclerView.Adapter<AmbulanceSearchResultAdapter.AmbulanceSearchResultViewHolder> {
    private Context context;
    private ArrayList<AmbulanceModelClass> ambulances =  new ArrayList<>();

    public AmbulanceSearchResultAdapter(Context context, ArrayList<AmbulanceModelClass> ambulances) {
        this.context = context;
        this.ambulances = ambulances;
    }

    @NonNull
    @Override
    public AmbulanceSearchResultAdapter.AmbulanceSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_search_row,parent,false);
        return new AmbulanceSearchResultAdapter.AmbulanceSearchResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AmbulanceSearchResultAdapter.AmbulanceSearchResultViewHolder holder, int position) {
        final AmbulanceModelClass ambulance = ambulances.get(position);


        holder.nametxt.setText(ambulance.getName());
        holder.districttxt.setText(ambulance.getDistrict());
        holder.phoneNumberTxt.setText(ambulance.getMobile());
        holder.detailsTxt.setText("Details: "+ambulance.getDetails());
        holder.mIcon.setImageResource(R.drawable.ambulance);

        holder.msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:"+ambulance.getMobile());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                if(i.resolveActivity(context.getPackageManager()) != null){
                    context.startActivity(i);
                }
                AllToasts.infoToast(view.getContext(), "MESSAGE to: "+ambulance.getMobile());
            }
        });
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:"+ambulance.getMobile());
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                context.startActivity(i);
                AllToasts.infoToast(view.getContext(), "CALL to: "+ambulance.getMobile());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ambulances.size();
    }

    public class AmbulanceSearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView nametxt,districttxt, detailsTxt,
                phoneNumberTxt;

        Button callBtn, msgBtn;
        ImageView mIcon;
        public AmbulanceSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.imageView_search_result_icon);
            nametxt = itemView.findViewById(R.id.name_text);
            districttxt = itemView.findViewById(R.id.district_text);
            phoneNumberTxt = itemView.findViewById(R.id.phone_number_text);
            detailsTxt = itemView.findViewById(R.id.hospital_details_text);

            callBtn = itemView.findViewById(R.id.call_btn);
            msgBtn = itemView.findViewById(R.id.message_btn);
        }
    }



}
