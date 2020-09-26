package com.example.sew.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.sew.R;
import com.example.sew.adapters.DemoFragmentAdapter;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MyService.setSelectedLanguage(Enums.LANGUAGE.ENGLISH);
                    sendBroadCast(BROADCAST_LANGUAGE_CHANGED);
                    return true;
                case R.id.navigation_dashboard:
                    MyService.setSelectedLanguage(Enums.LANGUAGE.URDU);
                    sendBroadCast(BROADCAST_LANGUAGE_CHANGED);
                    return true;
                case R.id.navigation_notifications:
//                    Utils.setSelectedLanguage(Utils.LANGUAGE.ENGLISH);
//                    sendBroadCast(BROADCAST_LANGUAGE_CHANGED);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setHeaderTitle(MyHelper.getString(R.string.nazm));
        viewpager.setAdapter(new DemoFragmentAdapter(getSupportFragmentManager(), this));
        tabLayout.setViewPager(viewpager);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lazyRefreshTabPositioning(tabLayout, viewpager);
                setHeaderTitle(MyHelper.getString(R.string.nazm));
            }
        }, BROADCAST_LANGUAGE_CHANGED);
    }

}
