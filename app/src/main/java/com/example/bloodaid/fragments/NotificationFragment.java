package com.example.bloodaid.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidNotificationInterface;
import com.example.bloodaid.LoginActivity;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.adapters.NotificationAdapter;
import com.example.bloodaid.models.BloodRequestModelClass;
import com.example.bloodaid.models.NotificationModelClass;
import com.example.bloodaid.models.NotificationParentModelClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.bloodaid.fragments.HomeFragment.SHARED_PREFerence_Key;

public class NotificationFragment extends Fragment {
    ExpandableListView mNotificationExpandable;
    NotificationAdapter adapter ;
    ArrayList<NotificationParentModelClass> messages = new ArrayList<>();
    HashMap<String, BloodRequestModelClass> request_details = new HashMap<>();
    ArrayList<NotificationModelClass> s = new ArrayList<>();

    private int lastExpanse = -1;

    public NotificationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        mNotificationExpandable = v.findViewById(R.id.explandable_notification);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("anotherFragment", true);
        editor.apply();


        if(checkInternetConnectivity()){
            fetchNotificationDataFromDatabase();
        }
        else{
            AllToasts.errorToast(getContext(), "Please Check Internet Connection and try again !");

        }



        mNotificationExpandable.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                if(lastExpanse != -1 && lastExpanse != i){
                    mNotificationExpandable.collapseGroup(lastExpanse);
                }
                lastExpanse = i;
                makeSeenNotificationItem(i);
            }
        });

        return v;
    }

    private void makeSeenNotificationItem(int i) {
        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidNotificationInterface.class)
                .makeSeenNotificationItem(s.get(i).getNotificationId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), "Error Code : " + response.code() + " .", Toast.LENGTH_LONG).show();
                        }/*
                        else {
                            try {
                                String s = response.body().string();


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }*/
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage() + " .", Toast.LENGTH_LONG).show();
                    }

                });
            }
        }).start();
    }

    private void fetchNotificationDataFromDatabase() {
        final Call<ArrayList<NotificationModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidNotificationInterface.class)
                .getNotificationList();
        new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<ArrayList<NotificationModelClass>>() {
                    @Override
                    public void onResponse(Call<ArrayList<NotificationModelClass>> call, Response<ArrayList<NotificationModelClass>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), "Error Code : " + response.code() + " .", Toast.LENGTH_LONG).show();
                        }
                        else {
                            s = response.body();

                            for(NotificationModelClass n: s){
                                messages.add( new NotificationParentModelClass(n.getNotificationId(),n.getMessage(),
                                                n.getNeed_date(), n.getCreated_at(), n.getSeen()));

                                request_details.put(n.getCreated_at(),new BloodRequestModelClass(
                                        n.getRequestId(),n.getName(), n.getPhone(), n.getDistrict(),
                                        n.getHospital(), n.getReason(), n.getBlood_group()
                                ));
                            }

                            adapter = new NotificationAdapter(getContext(), messages, request_details);
                            mNotificationExpandable.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<ArrayList<NotificationModelClass>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage() + " .", Toast.LENGTH_LONG).show();
                    }

                });
            }
        }).start();
    }



    public boolean checkInternetConnectivity(){
        boolean connected ;
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }

}
