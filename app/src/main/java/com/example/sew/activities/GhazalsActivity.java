package com.example.sew.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.GhazalsFragmentAdapter;
import com.example.sew.helpers.MyHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GhazalsActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static Intent getInstance(Activity activity) {
        return new Intent(activity, GhazalsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghazals);
        ButterKnife.bind(this);
        setHeaderTitle(MyHelper.getString(R.string.ghazal));
        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(new GhazalsFragmentAdapter(getSupportFragmentManager(), this));
        tabLayout.setViewPager(viewpager);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lazyRefreshTabPositioning(tabLayout, viewpager);
                setHeaderTitle(MyHelper.getString(R.string.ghazal));
            }
        }, BROADCAST_LANGUAGE_CHANGED);
    }

}
