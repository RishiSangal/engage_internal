package com.example.sew.adapters;


import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.fragments.DemoFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DemoFragmentAdapter extends FragmentStatePagerAdapter {
    private BaseActivity activity;
    private int[] tabs = new int[]{
            R.string.all,
            R.string.top,
            R.string.womans_poets,
            R.string.young_poets,
            R.string.classical_poets
    };
    private String[] targetIds = new String[]{
            "",
            "",
            "",
            "",
            "",
    };

    public DemoFragmentAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int i) {
        return DemoFragment.getInstance(getPageTitle(i));
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return activity.getString(tabs[position]);
    }
}
