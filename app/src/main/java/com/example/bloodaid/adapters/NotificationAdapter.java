package com.example.bloodaid.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bloodaid.R;
import com.example.bloodaid.models.BloodRequestModelClass;
import com.example.bloodaid.models.NotificationParentModelClass;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationAdapter extends BaseExpandableListAdapter {
    ArrayList<NotificationParentModelClass> parents;
    HashMap<String, BloodRequestModelClass> childs;
    Context context;

    public NotificationAdapter(Context context,ArrayList<NotificationParentModelClass> parents, HashMap<String, BloodRequestModelClass> childs) {
        this.parents = parents;
        this.childs = childs;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return parents.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return parents.get(i);
    }

    @Override
    public BloodRequestModelClass getChild(int i, int i1) {
        return childs.get(parents.get(i).getCreated_at());
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        NotificationParentModelClass parent = (NotificationParentModelClass) getGroup(i);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notificationparent_row,null);
        }

        TextView message = convertView.findViewById(R.id.textView_notification_message);
        message.setText(parent.getMessage());

        TextView seen = convertView.findViewById(R.id.textView_notification_seen);
        if(parent.getSeen().equals("yes")){
            seen.setText("Seen");
            seen.setTextColor(Color.GREEN);
        }
        else{
            seen.setText("Unseen");
        }

        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        BloodRequestModelClass child = getChild(i, i1);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.request_feed_row, null);
        }

        //initializations
        TextView mBloodGroup = view.findViewById(R.id.blood_group_text);
        TextView mName = view.findViewById(R.id.name_text);
        TextView mDistrict = view.findViewById(R.id.district_text);
        TextView mHospital = view.findViewById(R.id.hospital_text);
        TextView mReason = view.findViewById(R.id.reason_text);
        Button mCall = view.findViewById(R.id.call_btn);
        Button mMessage = view.findViewById(R.id.message_btn);


        mBloodGroup.setText(child.getBlood_group());
        mName.setText("Name: "+child.getName());
        mDistrict.setText("District: "+child.getDistrict());
        mHospital.setText("Hospital: "+child.getHospital());
        mReason.setText("Reason: " + child.getReason());

        final String number = child.getPhone();


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

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
