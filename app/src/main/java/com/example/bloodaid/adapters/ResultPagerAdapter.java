package com.example.bloodaid.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bloodaid.fragments.ListSearchResultFragment;
import com.example.bloodaid.fragments.MapSearchResultFragment;

public class ResultPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    Bundle bundle = new Bundle();

    public ResultPagerAdapter(FragmentManager fm , int tabCount, Bundle bundle){
        super(fm);
        this.tabCount = tabCount;
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListSearchResultFragment tab1 = new ListSearchResultFragment();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                MapSearchResultFragment tab2 = new MapSearchResultFragment();
                tab2.setArguments(bundle);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
