package com.example.bloodaid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ErrorToast {

    public static void errorToast(Context context, String error_text){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        View v = LayoutInflater.from(context)
                .inflate(R.layout.error_toast, null);
        TextView t = v.findViewById(R.id.error_text);
        t.setText(error_text);
        toast.setView(v);
        toast.show();
    }

    public static void successToast(Context context, String success_text){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        View v = LayoutInflater.from(context)
                .inflate(R.layout.error_toast, null);
        TextView t = v.findViewById(R.id.error_text);
        ImageView i = v.findViewById(R.id.toast_icon);
        LinearLayout linearLayout = v.findViewById(R.id.toast_view);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.green));
        i.setImageDrawable(context.getDrawable(R.drawable.ic_success));
        t.setText(success_text);
        toast.setView(v);
        toast.show();
    }
}
