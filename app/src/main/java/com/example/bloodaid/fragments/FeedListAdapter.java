package com.example.bloodaid.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bloodaid.R;
import com.example.bloodaid.models.DonorRequestModelClass;

import java.util.ArrayList;

public class FeedListAdapter extends BaseAdapter {

    Context context;
    ArrayList<DonorRequestModelClass> arrayList = new ArrayList<>();
    LayoutInflater mInflater;

    public FeedListAdapter(Context context, ArrayList<DonorRequestModelClass> arrayList) {
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
        TextView mLastDonated = view.findViewById(R.id.last_donated_text);
        TextView mDonateCount = view.findViewById(R.id.donate_count_text);
        Button mCall = view.findViewById(R.id.call_btn);
        Button mMessage = view.findViewById(R.id.message_btn);


        mBloodGroup.setText(arrayList.get(i).getBloodGroup());
        mName.setText(arrayList.get(i).getName());
        mDistrict.setText(arrayList.get(i).getDistrict());
        mLastDonated.setText(arrayList.get(i).getLastDonate());
        mDonateCount.setText(arrayList.get(i).getDonateCount());

        String number = arrayList.get(i).getMobile();


        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call to numebr
            }
        });
        mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sms number
            }
        });
        return view;
    }
}
