package com.example.sew.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.fragments.PoetAudioFragment;
import com.example.sew.fragments.PoetGhazalFragment;
import com.example.sew.fragments.PoetImageShayriFragment;
import com.example.sew.fragments.PoetNazmFragment;
import com.example.sew.fragments.PoetProfileFragment;
import com.example.sew.fragments.PoetSherFragment;
import com.example.sew.fragments.PoetVideoFragment;
import com.example.sew.helpers.CustomFragmentStatePagerAdapter;
import com.example.sew.models.ContentType;
import com.example.sew.models.PoetDetail;

import java.util.ArrayList;

public class PoetDetailFragmentAdapter extends CustomFragmentStatePagerAdapter {
    private BaseActivity activity;
    private PoetDetail poetDetail;
    private ArrayList<ContentType> poetProfilesTabs;

    public PoetDetailFragmentAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.activity = activity;
    }

    public PoetDetailFragmentAdapter(FragmentManager fm, BaseActivity activity, PoetDetail poetDetail, ArrayList<ContentType> poetProfilesTabs) {
        super(fm);
        this.activity = activity;
        this.poetDetail = poetDetail;
        this.poetProfilesTabs = poetProfilesTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (poetProfilesTabs.get(position).getListRenderingFormat()) {
            case PROFILE:
                return PoetProfileFragment.getInstance(poetDetail);
            case GHAZAL:
                return PoetGhazalFragment.getInstance(poetDetail, poetProfilesTabs.get(position));
            case NAZM:
                return PoetNazmFragment.getInstance(poetDetail, poetProfilesTabs.get(position));
            case SHER:
            case QUOTE:
                return PoetSherFragment.getInstance(poetDetail, poetProfilesTabs.get(position));
            case AUDIO:
                return PoetAudioFragment.getInstance(poetDetail);
            case VIDEO:
                return PoetVideoFragment.getInstance(poetDetail);
            case IMAGE_SHAYRI:
                return PoetImageShayriFragment.getInstance(poetDetail);
        }
        return PoetProfileFragment.getInstance(poetDetail);
    }

    @Override
    public int getCount() {
        return poetProfilesTabs.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return poetProfilesTabs.get(position).getName().toUpperCase();
    }
}
