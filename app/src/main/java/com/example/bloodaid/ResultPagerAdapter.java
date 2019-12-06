package com.example.bloodaid;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bloodaid.fragments.ListSearchResultFragment;
import com.example.bloodaid.fragments.MapSearchResultFragment;

public class ResultPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public ResultPagerAdapter(FragmentManager fm , int tabCount){
        super(fm);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListSearchResultFragment tab1 = new ListSearchResultFragment();
                return tab1;
            case 1:
                MapSearchResultFragment tab2 = new MapSearchResultFragment();
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
