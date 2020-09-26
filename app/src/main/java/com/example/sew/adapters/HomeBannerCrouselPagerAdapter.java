package com.example.sew.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.activities.BaseActivity;
import com.example.sew.fragments.BannerCrouselFragment;
import com.example.sew.models.HomeBannerCollection;

import java.util.ArrayList;

public class HomeBannerCrouselPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<HomeBannerCollection> bannerCollections;


    public HomeBannerCrouselPagerAdapter(BaseActivity baseActivity, ArrayList<HomeBannerCollection> bannerCollections) {
        super(baseActivity.getSupportFragmentManager());
        this.bannerCollections = bannerCollections;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return BannerCrouselFragment.getInstance(bannerCollections.get(position));
    }

    @Override
    public int getCount() {
        return bannerCollections.size();
    }

}
