package com.example.sew.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.fragments.SavedShayariImageFragment;
import com.example.sew.fragments.ShayariImageFragment;
import com.example.sew.helpers.MyHelper;

public class ShayariImagesFragmentAdapter extends FragmentStatePagerAdapter {
    private BaseActivity activity;
    private int[] tabs = new int[]{
            R.string.shayari_gallery,
            R.string.saved_images,
    };

    public ShayariImagesFragmentAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return ShayariImageFragment.getInstance(false);
        else
            return SavedShayariImageFragment.getInstance();
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return MyHelper.getString(tabs[position]);
    }
}
