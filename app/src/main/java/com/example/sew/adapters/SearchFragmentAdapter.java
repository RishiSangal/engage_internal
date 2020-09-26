package com.example.sew.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.DictionaryActivity;
import com.example.sew.common.Enums;
import com.example.sew.fragments.DictionaryFragment;
import com.example.sew.fragments.PoetProfileFragment;
import com.example.sew.fragments.SearchAllFragment;
import com.example.sew.fragments.SearchGhazalNazmSherFragment;
import com.example.sew.fragments.SearchPoetsFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.PoetDetail;

public class SearchFragmentAdapter extends FragmentStatePagerAdapter {
    private BaseActivity activity;
    private String searchedText;

    public SearchFragmentAdapter(FragmentManager fm, BaseActivity activity, String searchedText) {
        super(fm);
        this.activity = activity;
        this.searchedText = searchedText;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position % getCount()) {
            case 0:
                return SearchAllFragment.getInstance(searchedText);
            case 1:
                return SearchPoetsFragment.getInstance(Enums.CONTENT_TYPE.POET,searchedText);

            case 2:
                return DictionaryFragment.getInstance(searchedText);
              //  return SearchGhazalNazmSherFragment.getInstance(Enums.CONTENT_TYPE.GHAZAL, searchedText);
            case 3:
                return SearchGhazalNazmSherFragment.getInstance(Enums.CONTENT_TYPE.GHAZAL, searchedText);
            case 4:
                return SearchGhazalNazmSherFragment.getInstance(Enums.CONTENT_TYPE.NAZM, searchedText);
            case 5:
                return SearchGhazalNazmSherFragment.getInstance(Enums.CONTENT_TYPE.SHER, searchedText);
        }
        return PoetProfileFragment.getInstance(new PoetDetail(null));
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        int resId = 0;
        switch (position % getCount()) {
            case 0:
                resId = R.string.all;
                break;
            case 1:
                resId = R.string.poets;
                break;
            case 2:
                resId = R.string.dictionary;
                break;
            case 3:
                resId = R.string.ghazals;
                break;
            case 4:
                resId = R.string.nazms;
                break;
            case 5:
                resId = R.string.sher;
                break;
        }
        return MyHelper.getString(resId);
    }
}
