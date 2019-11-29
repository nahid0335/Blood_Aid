package com.example.bloodaid.fragments;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.example.bloodaid.R;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    Group mBloodGroups;
    Button mBloodSearchIcon;
    Boolean bloodGroupShowState = true;
    ImageView mProfilePic;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);
        profileWork();

        //search bar start
        mBloodSearchIcon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                if(bloodGroupShowState){
                    mBloodGroups.setVisibility(View.GONE);
                    mBloodSearchIcon.setBackground(getResources().getDrawable(R.drawable.ic_search));
                    bloodGroupShowState = false;
                }
                else{
                    mBloodGroups.setVisibility(View.VISIBLE);
                    mBloodSearchIcon.setBackground(getResources().getDrawable(R.drawable.ic_cancel));
                    bloodGroupShowState = true;
                }

            }
        });

        //search bar end

        return v;
    }

    private void init(View v) {
        mProfilePic = v.findViewById(R.id.profile_image);
        mBloodGroups = v.findViewById(R.id.blood_groups);
        mBloodSearchIcon = v.findViewById(R.id.search_btn);
    }

    private void profileWork() {
        Picasso.get().load("file:///android_asset/images/profile_pic.jpg").into(mProfilePic);
    }

    public void bloodGroupSearchClicked(View v){

    }


}
