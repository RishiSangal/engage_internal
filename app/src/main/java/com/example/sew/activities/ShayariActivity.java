package com.example.sew.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.ShayariFragmentAdapter;
import com.example.sew.common.Enums;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShayariActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Enums.SHER_COLLECTION_TYPE collectionType;

    public static Intent getInstance(Activity activity) {
        return getInstance(activity, Enums.SHER_COLLECTION_TYPE.TOP_20);
    }

    public static Intent getInstance(Activity activity, Enums.SHER_COLLECTION_TYPE sherCollectionType) {
        Intent intent = new Intent(activity, ShayariActivity.class);
        intent.putExtra(COLLECTION_TYPE, sherCollectionType.toString());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shayari);
        ButterKnife.bind(this);
        collectionType = Enums.SHER_COLLECTION_TYPE.valueOf(getIntent().getStringExtra(COLLECTION_TYPE));
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(new ShayariFragmentAdapter(getSupportFragmentManager(), getActivity()));
        tabLayout.setViewPager(viewpager);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lazyRefreshTabPositioning(tabLayout, viewpager);
            }
        }, BROADCAST_LANGUAGE_CHANGED);
        switch (collectionType) {
            case TOP_20:
            case OTHER:
                break;
            case OCCASIONS:
                viewpager.setCurrentItem(1);
                break;
            case TAG:
                viewpager.setCurrentItem(2);
                break;
        }
    }

}
