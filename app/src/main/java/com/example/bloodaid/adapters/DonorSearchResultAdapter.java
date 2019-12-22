package com.example.bloodaid.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.models.DonorModelClass;
import com.example.bloodaid.models.TopDonorModelClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.bloodaid.MainActivity.SHARED_PREFerence_Key;

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
        holder.reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReportToDatabase(donor.getDonorId());
                holder.reportBtn.setVisibility(View.GONE);
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

        Button callBtn, msgBtn, reportBtn;

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
            reportBtn = itemView.findViewById(R.id.report_btn);        }
    }


    private void sendReportToDatabase(Integer donorId) {

        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .sendDonorReport(donorId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String s = response.body().string();
                            JSONObject jsonObject = new JSONObject(s);
                            Boolean status = jsonObject.getBoolean("reported");
                            if(status){
                                AllToasts.successToast(context, "Your have reported .");
                            }
                            else{
                                AllToasts.errorToast(context, "Sorry ! Something wrong when you were reporting !"+response.errorBody());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, t.getMessage()+" .", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();


    }


}

