package com.example.bloodaid.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.example.bloodaid.ProfileActivity;
import com.example.bloodaid.R;
import com.example.bloodaid.models.UserModelClass;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {
    Group mBloodGroups;
    Button mBloodSearchIcon;
    Boolean bloodGroupShowState = true;
    ImageView mProfilePic;
    CardView userProfile;
    TextView UserName;


    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_ID = "user_id";
    public static final String USER_DATA = "user_data";

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        Gson gson = new Gson();
        String name = "bal";
        if(sharedPreferences.contains(USER_DATA)){
            String json = sharedPreferences.getString(USER_DATA,null);
            UserModelClass userDetails = gson.fromJson(json,UserModelClass.class);
            name = userDetails.getName();

            UserName.setText(name);
        }


//        profileWork();

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

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });

        return v;
    }

    private void init(View v) {
        mBloodGroups = v.findViewById(R.id.blood_groups);
        mBloodSearchIcon = v.findViewById(R.id.search_btn);
        userProfile = v.findViewById(R.id.main_profile);
        UserName = v.findViewById(R.id.textView_userHome_userName);
    }

    private void profileWork() {
        Picasso.get().load("file:///android_asset/images/profile_pic.jpg").into(mProfilePic);
    }

    public void bloodGroupSearchClicked(View v){

    }


}
