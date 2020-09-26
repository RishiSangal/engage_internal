package com.example.sew.activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.ShayariImageDetailFragmentAdapter;
import com.example.sew.models.ShayariImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShayariImageDetailActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static Intent getInstance(Activity activity, ArrayList<ShayariImage> shayariImages, ShayariImage selectedShayariImage) {
        Intent intent = new Intent(activity, ShayariImageDetailActivity.class);
        JSONArray jsonArray = new JSONArray();
        int selectedIndex = shayariImages.indexOf(selectedShayariImage);
        if (selectedIndex == -1)
            selectedIndex = 0;
        for (ShayariImage shayariImage : shayariImages)
            jsonArray.put(shayariImage.getJsonObject());
        intent.putExtra(SHAYARI_IMAGE_ARRAY, jsonArray.toString());
        intent.putExtra(SHAYARI_IMAGE_SELECTED_INDEX, selectedIndex);
        return intent;
    }

    public static Intent getInstance(Activity activity, ShayariImage selectedShayariImage) {
        Intent intent = new Intent(activity, ShayariImageDetailActivity.class);
        JSONArray jsonArray = new JSONArray();
        int selectedIndex = 0;
        jsonArray.put(selectedShayariImage.getJsonObject());
        intent.putExtra(SHAYARI_IMAGE_ARRAY, jsonArray.toString());
        intent.putExtra(SHAYARI_IMAGE_SELECTED_INDEX, selectedIndex);
        return intent;
    }

    ArrayList<ShayariImage> shayariImages;
    int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shayari_image_detail);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null && getIntent().hasExtra(SHAYARI_IMAGE_ARRAY)) {
            selectedIndex = getIntent().getIntExtra(SHAYARI_IMAGE_SELECTED_INDEX, 0);
            try {
                JSONArray jsonArray = new JSONArray(getIntent().getExtras().getString(SHAYARI_IMAGE_ARRAY, "[]"));
                shayariImages = new ArrayList<>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++)
                    shayariImages.add(new ShayariImage(jsonArray.optJSONObject(i % jsonArray.length())));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        if (imgBack != null)
            imgBack.setVisibility(View.VISIBLE);
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

    private void populateData() {
        viewpager.setAdapter(new ShayariImageDetailFragmentAdapter(getSupportFragmentManager(), this, shayariImages));
        viewpager.setCurrentItem(selectedIndex);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        }, BROADCAST_LANGUAGE_CHANGED);
    }
}
