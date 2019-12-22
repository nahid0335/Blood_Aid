package com.example.bloodaid.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.R;
import com.example.bloodaid.models.BloodRequestModelClass;
import com.example.bloodaid.models.DonorRequestModelClass;

import java.util.ArrayList;

public class FeedListAdapter extends BaseAdapter {

    Context context;
    ArrayList<BloodRequestModelClass> arrayList = new ArrayList<>();
    LayoutInflater mInflater;

    public FeedListAdapter(Context context, ArrayList<BloodRequestModelClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = mInflater.inflate(R.layout.request_feed_row, null);

        //initializations
        TextView mBloodGroup = view.findViewById(R.id.blood_group_text);
        TextView mName = view.findViewById(R.id.name_text);
        TextView mDistrict = view.findViewById(R.id.district_text);
        TextView mHospital = view.findViewById(R.id.hospital_text);
        TextView mReason = view.findViewById(R.id.reason_text);
        Button mCall = view.findViewById(R.id.call_btn);
        Button mMessage = view.findViewById(R.id.message_btn);


        mBloodGroup.setText(arrayList.get(i).getBlood_group());
        mName.setText("Name: "+arrayList.get(i).getName());
        mDistrict.setText("District: "+arrayList.get(i).getDistrict());
        mHospital.setText("Hospital: "+arrayList.get(i).getHospital());
        mReason.setText("Reason: " + arrayList.get(i).getReason());

        final String number = arrayList.get(i).getPhone();


        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call to numebr
                Uri uri = Uri.parse("tel:"+number);
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                context.startActivity(i);
            }
        });
        mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sms number
                Uri uri = Uri.parse("smsto:"+number);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                if(i.resolveActivity(context.getPackageManager()) != null){
                    context.startActivity(i);
                }
            }
        });
        return view;
    }
}
