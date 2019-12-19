package com.example.bloodaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.bloodaid.adapters.ResultPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SearchResultActivity extends AppCompatActivity {
    private TabLayout mSearchTab;
    private ViewPager mSearchPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mSearchTab = findViewById(R.id.search_tabLayout);
        mSearchPager = findViewById(R.id.search_pager);

        String searchfor = getIntent().getStringExtra("searchfor");
        String district = getIntent().getStringExtra("district");
        String bloodGroup = getIntent().getStringExtra("bloodgroup");



        mSearchTab.addTab(mSearchTab.newTab().setText(searchfor+" List"));
        mSearchTab.addTab(mSearchTab.newTab().setText(searchfor+" Map"));

        Bundle bundle = new Bundle();
        bundle.putString("searchfor", searchfor);
        bundle.putString("district", district);
        bundle.putString("bloodgroup", bloodGroup);

        ResultPagerAdapter adapter = new ResultPagerAdapter(getSupportFragmentManager(), mSearchTab.getTabCount(), bundle);
        mSearchPager.setAdapter(adapter);

        mSearchTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSearchPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
