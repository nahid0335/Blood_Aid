package com.example.bloodaid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bloodaid.MainActivity;
import com.example.bloodaid.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SearchDialog extends BottomSheetDialogFragment {
    Button mSearchBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_dialog, container, false);
        init(v);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mBottomNav.getMenu().getItem(3).setChecked(true);
                dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_display, new SearchFragment()).commit();
            }
        });


        return v;
    }

    private void init(View v) {
        mSearchBtn = v.findViewById(R.id.search_text_button);
    }


}
