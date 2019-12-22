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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.models.HospitalModelClass;
import com.example.bloodaid.models.OrganizationModelClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrgSearchResultAdapter extends RecyclerView.Adapter<OrgSearchResultAdapter.OrgSearchResultViewHolder> {
    private Context context;
    private ArrayList<OrganizationModelClass> organizations =  new ArrayList<>();

    public OrgSearchResultAdapter(Context context, ArrayList<OrganizationModelClass> organizations) {
        this.context = context;
        this.organizations = organizations;
    }

    @NonNull
    @Override
    public OrgSearchResultAdapter.OrgSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_search_row,parent,false);
        return new OrgSearchResultAdapter.OrgSearchResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrgSearchResultAdapter.OrgSearchResultViewHolder holder, int position) {
        final OrganizationModelClass organization = organizations.get(position);


        holder.nametxt.setText(organization.getName());
        holder.districttxt.setText(organization.getDistrict());
        holder.phoneNumberTxt.setText(organization.getMobile());
        holder.detailsTxt.setText("Details: "+organization.getDetails());
        holder.mIcon.setImageResource(R.drawable.organization);

        holder.msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:"+organization.getMobile());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                if(i.resolveActivity(context.getPackageManager()) != null){
                    context.startActivity(i);
                }
                AllToasts.infoToast(view.getContext(), "MESSAGE to: "+organization.getMobile());
            }
        });
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:"+organization.getMobile());
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                context.startActivity(i);
                AllToasts.infoToast(view.getContext(), "CALL to: "+organization.getMobile());
            }
        });

        holder.reportBtn.setVisibility(View.VISIBLE);
        holder.reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReportToDatabase(organization.getOrganizationId());
                holder.reportBtn.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return organizations.size();
    }

    public class OrgSearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView nametxt,districttxt, detailsTxt,
                phoneNumberTxt;

        Button callBtn, msgBtn, reportBtn;
        ImageView mIcon;

        public OrgSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.imageView_search_result_icon);
            nametxt = itemView.findViewById(R.id.name_text);
            districttxt = itemView.findViewById(R.id.district_text);
            phoneNumberTxt = itemView.findViewById(R.id.phone_number_text);
            detailsTxt = itemView.findViewById(R.id.hospital_details_text);

            callBtn = itemView.findViewById(R.id.call_btn);
            msgBtn = itemView.findViewById(R.id.message_btn);
            reportBtn = itemView.findViewById(R.id.report_btn);
        }
    }


    private void sendReportToDatabase(Integer orgId) {

        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .sendOrgReport(orgId);

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
