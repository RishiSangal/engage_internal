package com.example.sew.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.fragments.DemoFragment;
import com.example.sew.fragments.ProseShayariCollectionFragment;
import com.example.sew.models.ContentType;

import java.util.ArrayList;

public class ProseShayariCollectionFragmentAdapter extends FragmentStatePagerAdapter {
    private BaseActivity activity;
    private Enums.COLLECTION_TYPE collectionType;
    private ArrayList<ContentType> contentTypes;

    public ProseShayariCollectionFragmentAdapter(FragmentManager fm, BaseActivity activity, Enums.COLLECTION_TYPE collectionType, ArrayList<ContentType> contentTypes) {
        super(fm);
        this.activity = activity;
        this.collectionType = collectionType;
        this.contentTypes = contentTypes;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ProseShayariCollectionFragment.getInstance(contentTypes.get(position).getContentId(), collectionType);
    }

    @Override
    public int getCount() {
        return contentTypes.size();
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        return contentTypes.get(position).getName().toUpperCase();
    }
}
