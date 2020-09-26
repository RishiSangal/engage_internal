package com.example.sew.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.fragments.GhazalsFragment;
import com.example.sew.helpers.MyHelper;

import static com.example.sew.common.MyConstants.GHAZAL_100_FAMOUS__ID;
import static com.example.sew.common.MyConstants.GHAZAL_BEGINNER_ID;
import static com.example.sew.common.MyConstants.GHAZAL_EDITOR_CHOICE_ID;
import static com.example.sew.common.MyConstants.GHAZAL_HUMOR_ID;

public class GhazalsFragmentAdapter extends FragmentStatePagerAdapter {
    private BaseActivity activity;
    private int[] tabs = new int[]{
            R.string.top100,
            R.string.beginners,
            R.string.editors_choice,
            R.string.humoursatire
    };

    private String[] targetIds = new String[]{
            GHAZAL_100_FAMOUS__ID,
            GHAZAL_BEGINNER_ID,
            GHAZAL_EDITOR_CHOICE_ID,
            GHAZAL_HUMOR_ID,
    };

    public GhazalsFragmentAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return GhazalsFragment.getInstance(targetIds[position % getCount()]);
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
