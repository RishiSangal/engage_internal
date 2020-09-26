package com.example.sew.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.fragments.NazmsFragment;
import com.example.sew.helpers.MyHelper;

import static com.example.sew.common.MyConstants.NAZM_50_FAMOUS__ID;
import static com.example.sew.common.MyConstants.NAZM_BEGINNER_ID;
import static com.example.sew.common.MyConstants.NAZM_EDITOR_CHOICE_ID;
import static com.example.sew.common.MyConstants.NAZM_HUMOR_ID;

public class NazamsFragmentAdapter extends FragmentStatePagerAdapter {
    private BaseActivity activity;
    private int[] tabs = new int[]{
            R.string.nazms_50,
            R.string.beginners,
            R.string.editors_choice,
            R.string.humoursatire
    };

    private String[] targetIds = new String[]{
            NAZM_50_FAMOUS__ID,
            NAZM_BEGINNER_ID,
            NAZM_EDITOR_CHOICE_ID,
            NAZM_HUMOR_ID,
    };

    public NazamsFragmentAdapter(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return NazmsFragment.getInstance(targetIds[position % getCount()]);
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
