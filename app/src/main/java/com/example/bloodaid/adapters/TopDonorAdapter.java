package com.example.bloodaid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bloodaid.R;
import com.example.bloodaid.models.TopDonorModelClass;
import java.util.ArrayList;

public class TopDonorAdapter extends RecyclerView.Adapter<TopDonorViewHolder> {
    private Context context;
    private ArrayList<TopDonorModelClass> topDonorList;

    public TopDonorAdapter(Context context, ArrayList<TopDonorModelClass> topDonorList) {
        this.context = context;
        this.topDonorList = topDonorList;
    }

    @NonNull
    @Override
    public TopDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.top_donor_row,parent,false);
        return new TopDonorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TopDonorViewHolder holder, int position) {
        TopDonorModelClass topDonor = topDonorList.get(position);

        holder.mSerialCount.setText(String.valueOf(position+1));
        holder.nametxt.setText(topDonor.getName());
        holder.districttxt.setText("District: "+topDonor.getDistrict());
        holder.bloodGroupTxt.setText("Blood Group: "+topDonor.getBloodgroup());
        holder.lastDonationTxt.setText("Last Donation: "+topDonor.getLastDonation());
    }

    @Override
    public int getItemCount() {
        return topDonorList.size();
    }



}

class TopDonorViewHolder extends RecyclerView.ViewHolder {
    Button mSerialCount;
    TextView nametxt,districttxt, bloodGroupTxt, lastDonationTxt;

    public TopDonorViewHolder(@NonNull View itemView) {
        super(itemView);
        mSerialCount = itemView.findViewById(R.id.serial_count);
        nametxt = itemView.findViewById(R.id.textView_serial_name);
        districttxt = itemView.findViewById(R.id.textView_serial_district);
        bloodGroupTxt = itemView.findViewById(R.id.textView_serial_bloodgroup);
        lastDonationTxt = itemView.findViewById(R.id.textView_serial_lastdonation);
    }
}
