package com.example.sew.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.activities.BaseActivity;
import com.example.sew.fragments.ShayariImageDetailFragment;
import com.example.sew.models.ShayariImage;

import java.util.ArrayList;

public class ShayariImageDetailFragmentAdapter extends FragmentStatePagerAdapter {
    private BaseActivity activity;
    private ArrayList<ShayariImage> shayariImages;

    public ShayariImageDetailFragmentAdapter(FragmentManager fm, BaseActivity activity, ArrayList<ShayariImage> shayariImages) {
        super(fm);
        this.activity = activity;
        this.shayariImages = shayariImages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ShayariImageDetailFragment.getInstance(shayariImages.get(position));
    }

    @Override
    public int getCount() {
        return shayariImages.size();
    }

}
