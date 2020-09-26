package com.example.sew.activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.ShayariImagesFragmentAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShayariImagesActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static Intent getInstance(Activity activity) {
        return new Intent(activity, ShayariImagesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shayari_images);
        ButterKnife.bind(this);
        populateData();
//        Dexter.withActivity(this)
//                .withPermissions(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ).withListener(new MultiplePermissionsListener() {
//            @Override
//            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                if (report.areAllPermissionsGranted())
//                    populateData();
//                else {
//                    showToast("Please provide the permissions");
//                    finish();
//                }
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                token.continuePermissionRequest();
//            }
//        }).check();

    }

    void populateData() {
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(new ShayariImagesFragmentAdapter(getSupportFragmentManager(), this));
        tabLayout.setViewPager(viewpager);
        if (imgBack != null)
            imgBack.setVisibility(View.VISIBLE);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lazyRefreshTabPositioning(tabLayout, viewpager);
            }
        }, BROADCAST_LANGUAGE_CHANGED);
    }
}
