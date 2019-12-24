package com.example.bloodaid.fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.adapters.FeedListAdapter;
import com.example.bloodaid.adapters.HistoryFeedAdapter;
import com.example.bloodaid.models.BloodRequestModelClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.bloodaid.fragments.RequestFragment.SHARED_PREFerence_Key;
import static com.example.bloodaid.fragments.RequestFragment.USER_DATA;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    ListView mHistoryList;
    ArrayList<BloodRequestModelClass> arrayList = new ArrayList<>();
    Integer userId=0;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_history, container, false);

        mHistoryList = v.findViewById(R.id.listView_history);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFerence_Key, MODE_PRIVATE);
        if(sharedPreferences.contains(USER_DATA)){
            userId = sharedPreferences.getInt("user_id", 0);
        }

        ///
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //dataReady();
        final Call<ArrayList<BloodRequestModelClass>> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .donorHistoryFeed(userId);
        call.enqueue(new Callback<ArrayList<BloodRequestModelClass>>() {
            @Override
            public void onResponse(Call<ArrayList<BloodRequestModelClass>> call, Response<ArrayList<BloodRequestModelClass>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code : "+response.code()+" .", Toast.LENGTH_LONG).show();
                }
                else {

                    ArrayList<BloodRequestModelClass> responseList = response.body();

                    if(responseList.get(0).getRequestId() == -1){
                        AllToasts.infoToast(getContext(), "No Data Found !");
                    }
                    else{
                        //Response parsing
                        for (BloodRequestModelClass value : responseList) {
                            arrayList.add(value);
                            //Log.d("HIS", value.getName());
                        }
                        //Log.d("TAG", responseList.toString());
                        arrayList = responseList;
                        HistoryFeedAdapter historyFeedAdapter = new HistoryFeedAdapter(getContext(), arrayList);
                        mHistoryList.setAdapter(historyFeedAdapter);
                    }

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<BloodRequestModelClass>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: "+t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        return v;
    }

}
