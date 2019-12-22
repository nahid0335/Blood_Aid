package com.example.bloodaid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.bloodaid.R;
import com.example.bloodaid.adapters.NotificationAdapter;
import com.example.bloodaid.models.BloodRequestModelClass;
import com.example.bloodaid.models.NotificationParentModelClass;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationFragment extends Fragment {
    ExpandableListView mNotificationExpandable;
    NotificationAdapter adapter ;
    ArrayList<NotificationParentModelClass> messages = new ArrayList<>();
    HashMap<String, BloodRequestModelClass> request_details = new HashMap<>();

    public NotificationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        mNotificationExpandable = v.findViewById(R.id.explandable_notification);

        messages.add(new NotificationParentModelClass(1,
                "Roni needs A+ blood on your area. Requested you to donate blood.",
                "2019-09-22", "2019-02-11 22:01:11"));

        messages.add(new NotificationParentModelClass(1,
                "Roni needs B+ blood on your area. Requested you to donate blood.",
                "2019-09-22", "2019-02-11 22:01:11"));

        messages.add(new NotificationParentModelClass(1,
                "Roni needs C+ blood on your area. Requested you to donate blood.",
                "2019-09-22", "2019-02-11 22:01:11"));
        messages.add(new NotificationParentModelClass(1,
                "Roni needs D+ blood on your area. Requested you to donate blood.",
                "2019-09-22", "2019-02-11 22:01:11"));
        messages.add(new NotificationParentModelClass(1,
                "Roni needs E+ blood on your area. Requested you to donate blood.",
                "2019-09-22", "2019-02-11 22:01:11"));
        messages.add(new NotificationParentModelClass(1,
                "Roni needs F+ blood on your area. Requested you to donate blood.",
                "2019-09-22", "2019-02-11 22:01:11"));

        messages.add(new NotificationParentModelClass(1,
                "Roni needs G+ blood on your area. Requested you to donate blood.",
                "2019-09-22", "2019-02-11 22:01:11"));



        request_details.put("Roni needs A+ blood on your area. Requested you to donate blood.",
                new BloodRequestModelClass("Pranto", "02387438", "Dhaka", "KMC", "Fasdfkj", "O+"));
        request_details.put("Roni needs B+ blood on your area. Requested you to donate blood.",
                new BloodRequestModelClass("Pranto", "02387438", "Dhaka", "KMC", "Fasdfkj", "O+"));
        request_details.put("Roni needs C+ blood on your area. Requested you to donate blood.",
                new BloodRequestModelClass("Pranto", "02387438", "Dhaka", "KMC", "Fasdfkj", "O+"));
        request_details.put("Roni needs D+ blood on your area. Requested you to donate blood.",
                new BloodRequestModelClass("Pranto", "02387438", "Dhaka", "KMC", "Fasdfkj", "O+"));
        request_details.put("Roni needs E+ blood on your area. Requested you to donate blood.",
                new BloodRequestModelClass("Pranto", "02387438", "Dhaka", "KMC", "Fasdfkj", "O+"));
        request_details.put("Roni needs F+ blood on your area. Requested you to donate blood.",
                new BloodRequestModelClass("Pranto", "02387438", "Dhaka", "KMC", "Fasdfkj", "O+"));
        request_details.put("Roni needs G+ blood on your area. Requested you to donate blood.",
                new BloodRequestModelClass("Pranto", "02387438", "Dhaka", "KMC", "Fasdfkj", "O+"));

        adapter = new NotificationAdapter(getContext(), messages, request_details);
        mNotificationExpandable.setAdapter(adapter);

        return v;
    }

}
