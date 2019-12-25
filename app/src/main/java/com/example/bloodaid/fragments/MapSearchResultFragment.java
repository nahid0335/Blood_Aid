package com.example.bloodaid.fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.adapters.DonorSearchResultAdapter;
import com.example.bloodaid.models.DonorModelClass;
import com.example.bloodaid.models.DonorPositionModelClass;
import com.example.bloodaid.models.UserModelClass;
import com.example.bloodaid.utils.AreaData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MapSearchResultFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    String district, bloodGroup;
    long area_id;
    AreaData data = new AreaData();

    private MarkerOptions markerOptions = new MarkerOptions();
    private ArrayList<LatLng> pinpoint = new ArrayList<>();


    public static final String SHARED_PREFerence_Key = "BloodAid_Alpha_Version";
    public static final String DONOR_LOCATION = "donor_location";


    public MapSearchResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_search_result, container, false);

        district = getArguments().getString("district");
        area_id = data.getAreaId(district);
        bloodGroup = getArguments().getString("bloodgroup");
//        pinpoint = (ArrayList<LatLng>) getArguments().getSerializable("position");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(DONOR_LOCATION,null);
        Type type = new TypeToken<ArrayList<LatLng>>() {}.getType();
        pinpoint = gson.fromJson(json,type);
        Log.d("Tag",pinpoint.toString());



        mMapView = rootView.findViewById(R.id.mapView_userSearchDonor_list);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);


                for(LatLng point : pinpoint){
                    markerOptions.position(point);
                    markerOptions.anchor(0.5f,0.5f);
                    markerOptions.title("Blood Group :"+bloodGroup);
                    markerOptions.snippet("District :"+district);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    googleMap.addMarker(markerOptions);

                   // AllToasts.infoToast(getContext(),point.toString());
                }

                LatLng init = new LatLng(pinpoint.get(0).latitude,pinpoint.get(0).longitude);



                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(-34, 151);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(init).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        return rootView;
    }







    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }






}
