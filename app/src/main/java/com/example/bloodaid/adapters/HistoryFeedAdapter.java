package com.example.bloodaid.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodaid.AllToasts;
import com.example.bloodaid.BloodAidNotificationInterface;
import com.example.bloodaid.BloodAidService;
import com.example.bloodaid.R;
import com.example.bloodaid.RetrofitInstance;
import com.example.bloodaid.models.BloodRequestModelClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFeedAdapter  extends BaseAdapter {

    Context context;
    ArrayList<BloodRequestModelClass> arrayList = new ArrayList<>();
    LayoutInflater mInflater;

    public HistoryFeedAdapter(Context context, ArrayList<BloodRequestModelClass> arrayList) {
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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = mInflater.inflate(R.layout.history_feed_row, null);
        final int requestId = arrayList.get(i).getRequestId();
        final int position = i;

        //initializations
        TextView mBloodGroup = view.findViewById(R.id.blood_group_text);
        TextView mName = view.findViewById(R.id.name_text);
        TextView mDistrict = view.findViewById(R.id.district_text);
        TextView mHospital = view.findViewById(R.id.hospital_text);
        TextView mReason = view.findViewById(R.id.reason_text);
        Button mCall = view.findViewById(R.id.call_btn);
        Button mMessage = view.findViewById(R.id.message_btn);
        Button mGotBlood = view.findViewById(R.id.blood_got_btn);

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

        mGotBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Blood Request");
                builder.setMessage("Have you already found blood or delete your blood request?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendGotBloodData(position, requestId);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
        return view;
    }

    public void sendGotBloodData(final int position, int requestid){
        final Call<ResponseBody> call = RetrofitInstance.getRetrofitInstance()
                .create(BloodAidService.class)
                .deleteHistoryFeed(requestid);

        new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Error Code : " + response.code() + " .", Toast.LENGTH_LONG).show();
                        }
                        else {
                            arrayList.remove(position);
                            notifyDataSetChanged();
                            AllToasts.successToast(context,"Your blood request have just deleted ");
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Error: "+t.getMessage() + " .", Toast.LENGTH_LONG).show();
                    }

                });
            }
        }).start();
    }
}