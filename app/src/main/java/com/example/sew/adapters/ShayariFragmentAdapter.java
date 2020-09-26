package com.example.sew.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.fragments.DemoFragment;
import com.example.sew.fragments.OccasionsFragment;
import com.example.sew.fragments.T20SherFragment;
import com.example.sew.fragments.TagsFragment;
import com.example.sew.helpers.MyHelper;

public class ShayariFragmentAdapter extends FragmentStatePagerAdapter {
    public BaseActivity activity;
    private int[] tabs = new int[]{
            R.string.top_20_sher,
            R.string.occasions,
            R.string.tags,
    };

    public ShayariFragmentAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return T20SherFragment.getInstance();
            case 1:
                return OccasionsFragment.getInstance();
            case 2:
                return TagsFragment.getInstance();
        }
        return DemoFragment.getInstance(getPageTitle(position % getCount()));
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        return MyHelper.getString(tabs[position]);
    }
}
