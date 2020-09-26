package com.example.sew.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.PoetsFragmentAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoetsActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    public static final int TAB_ALL = 0;
    public static final int TAB_FAMOUS = 1;
    private int initialTab = TAB_ALL;

    public static Intent getInstance(Activity activity) {
        return getInstance(activity, TAB_ALL);
    }

    public static Intent getInstance(Activity activity, int initialTab) {
        Intent intent = new Intent(activity, PoetsActivity.class);
        intent.putExtra(INITIAL_TAB, initialTab);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poets);
        ButterKnife.bind(this);
        setHeaderTitle("");
        initialTab = getIntent().getIntExtra(INITIAL_TAB, TAB_ALL);
        viewpager.setOffscreenPageLimit(5);
        viewpager.setAdapter(new PoetsFragmentAdapter(getSupportFragmentManager(), this));
        tabLayout.setViewPager(viewpager);
        viewpager.setCurrentItem(initialTab);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lazyRefreshTabPositioning(tabLayout, viewpager);
            }
        }, BROADCAST_LANGUAGE_CHANGED);
    }

}
