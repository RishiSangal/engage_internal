package com.example.sew.adapters;


import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.fragments.PoetsFragment;
import com.example.sew.helpers.MyHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PoetsFragmentAdapter extends FragmentStatePagerAdapter {
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
            "3AB1ACC4-E080-4B77-BFC1-41E2908E6C2D",
            "9F15FCDD-C531-4EB9-A25E-F2F7DB8C7CF3",
            "B16B3E22-7881-4FE3-B2BD-94D45CC47139",
            "517093F6-D295-4389-AA02-AEA52E738B97",
    };

    public PoetsFragmentAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PoetsFragment.getInstance(targetIds[position % getCount()], position == 0);
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
