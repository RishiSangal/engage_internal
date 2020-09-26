package com.example.sew.models.home_view_holders;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.adapters.HomeBannerCrouselPagerAdapter;
import com.example.sew.models.HomeBannerCollection;
import com.example.sew.views.TitleTextViewType7;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class BannerCarouselViewHolder extends BaseHomeViewHolder {


    @BindView(R.id.viewPagerBanner)
    ViewPager viewPagerBanner;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    private ArrayList<HomeBannerCollection> bannerCollections;

    public static BannerCarouselViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        BannerCarouselViewHolder videoViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_banner_carousel, baseActivity);
            videoViewHolder = new BannerCarouselViewHolder(convertView, baseActivity);
        } else
            videoViewHolder = (BannerCarouselViewHolder) convertView.getTag();
        videoViewHolder.setConvertView(convertView);
        convertView.setTag(videoViewHolder);
        return videoViewHolder;
    }

    private BannerCarouselViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
    }

    private boolean isLoaded;

    public BaseHomeViewHolder loadData(ArrayList<HomeBannerCollection> bannerCollections) {
        this.bannerCollections = bannerCollections;
        if (!isLoaded) {
            isLoaded = true;
            updateUI();
        }
        return this;
    }

    private void updateUI() {
        viewPagerBanner.setAdapter(new HomeBannerCrouselPagerAdapter(getActivity(), bannerCollections));
        indicator.setViewPager(viewPagerBanner);
    }
}
