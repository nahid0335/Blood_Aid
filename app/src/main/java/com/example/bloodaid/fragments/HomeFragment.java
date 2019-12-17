package com.example.bloodaid.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.MainActivity;
import com.example.bloodaid.ProfileActivity;
import com.example.bloodaid.R;
import com.example.bloodaid.RegisterActivity;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.adapters.InformationsAdapter;
import com.example.bloodaid.backend.AdminLoginActivity;
import com.example.bloodaid.models.UserModelClass;
import com.example.bloodaid.utils.AreaData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements InformationsAdapter.FragmentLoaderInterface {
    Group mBloodGroups;
    Button mBloodSearchIcon;
    Boolean bloodGroupShowState = true;
    ImageView mProfilePic, mRightArrow, mLeftArrow;
    CardView userProfile;
    TextView UserName;
    ImageView mDonorImg, mOrgImg, mHospitalImg, mAmbulanceImg, mTopDonor, mFacts, mAppInfo, mHistory;
    TextView mDonorTxt, mOrgTxt, mHospitalTxt, mAmbulanceTxt;
    Context context ;
    //informations
    RecyclerView mInfoRecycler;
    InformationsAdapter mInfoAdapter;
    Fragment[] infoFragmentList = {new TopDonorFragment(),
            new FactsFragment(),
            new HistoryFragment(),
            new AppInfoFragment()};

    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String USER_DATA = "user_data";


    //Location
    FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSION_ID = 101;
    private Double latitude, longitude;
    UserModelClass userDetails = new UserModelClass();
    AreaData data = new AreaData();

    public HomeFragment() {}

    public HomeFragment(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);

        //Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        Gson gson = new Gson();
        String name = "bal";
        if(sharedPreferences.contains(USER_DATA)){
            String json = sharedPreferences.getString(USER_DATA,null);
            userDetails = gson.fromJson(json,UserModelClass.class);
            name = userDetails.getName();

            UserName.setText(name);
        }

        ///LOCATION//////
        getLastLocation();


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


        additionActions(v);
        informationsActions(v);



        return v;
    }

    private void informationsActions(View v) {
        mInfoAdapter = new InformationsAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false);
        mInfoRecycler.setAdapter(mInfoAdapter);
        mInfoRecycler.setLayoutManager(layoutManager);


    }

    private void additionActions(View v) {
        mDonorImg = v.findViewById(R.id.donar_add_img);
        mOrgImg = v.findViewById(R.id.organization_add_img);
        mHospitalImg = v.findViewById(R.id.hospital_add_img);
        mAmbulanceImg = v.findViewById(R.id.ambulance_add_img);
        mDonorTxt = v.findViewById(R.id.donar_add_text);
        mOrgTxt = v.findViewById(R.id.organization_add_text);
        mHospitalTxt = v.findViewById(R.id.hospital_add_text);
        mAmbulanceTxt = v.findViewById(R.id.ambulance_add_text);


        mDonorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment donorAddFragment = new DonorAddFragment();
                loadFragment(donorAddFragment);
            }
        });

        mHospitalImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment hospitalAddFragment = new HospitalAddFragment();
                loadFragment(hospitalAddFragment);
            }
        });

        mOrgImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment organizationAddFragment = new OrganizationAddFragment();
                loadFragment(organizationAddFragment);
            }
        });

        mAmbulanceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment ambulanceAddFragment = new AmbulanceAddFragment();
                loadFragment(ambulanceAddFragment);
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_display, fragment)
                .commit();
    }

    private void init(View v) {
        mBloodGroups = v.findViewById(R.id.blood_groups);
        mBloodSearchIcon = v.findViewById(R.id.search_btn);
        userProfile = v.findViewById(R.id.main_profile);
        UserName = v.findViewById(R.id.textView_userHome_userName);

        //addition
        mDonorImg = v.findViewById(R.id.donar_add_img);
        mOrgImg = v.findViewById(R.id.organization_add_img);
        mHospitalImg = v.findViewById(R.id.hospital_add_img);
        mAmbulanceImg = v.findViewById(R.id.ambulance_add_img);
        mDonorTxt = v.findViewById(R.id.donar_add_text);
        mOrgTxt = v.findViewById(R.id.organization_add_text);
        mHospitalTxt = v.findViewById(R.id.hospital_add_text);
        mAmbulanceTxt = v.findViewById(R.id.ambulance_add_text);


        //informations
        mInfoRecycler = v.findViewById(R.id.recyclerView_fragmentHome_informations);
        mRightArrow = v.findViewById(R.id.imageView_homeFragment_rightArrow);
        mLeftArrow = v.findViewById(R.id.imageView_homeFragment_leftArrow);
        mRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInfoRecycler.smoothScrollToPosition(mInfoRecycler.getAdapter().getItemCount()-1);
                mRightArrow.setVisibility(View.GONE);
                mLeftArrow.setVisibility(View.VISIBLE);
            }
        });

        mLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInfoRecycler.smoothScrollToPosition(0);
                mRightArrow.setVisibility(View.VISIBLE);
                mLeftArrow.setVisibility(View.GONE);
            }
        });
    }

    private void profileWork() {
        Picasso.get().load("file:///android_asset/images/profile_pic.jpg").into(mProfilePic);
    }

    @Override
    public void loadFragmentFromInterface(int position) {
        if(position==4){
            startActivity(new Intent(getContext(), AdminLoginActivity.class));
            return;
        }
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_display, infoFragmentList[position] )
                .commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


///////////////////LOCATION FUNCTIONS///////////////////////
    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
                getLastLocation();
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    //default value
                                    requestNewLocationData();
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                                dataSendToServer();
                                Log.d("TAGG", latitude+ " "+longitude);
                            }
                        }
                );
            } else {
                Toast.makeText(getContext(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void dataSendToServer() {
        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .updateLocation(userDetails.getUserId(),
                        latitude,
                        longitude);
        Log.d("TAGG", userDetails.getUserId()+" "+
                latitude+ " "+
                longitude);

        new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String s = response.body().string();

                            //Response parsing
                            Boolean status;
                            if(s.isEmpty()){
                                status = false;
                            }
                            else{
                                JSONObject object = new JSONObject(s);
                                status = object.getBoolean("validity"); // true or false will be returned as response
                            }
                            Log.d("TAGG", status.toString());

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }).start();

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000000);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

                mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallBack,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallBack = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if(locationResult != null){
                Location location = locationResult.getLastLocation();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    };

}
