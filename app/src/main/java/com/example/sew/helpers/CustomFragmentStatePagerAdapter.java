package com.example.sew.helpers;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public abstract class CustomFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    public CustomFragmentStatePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public CustomFragmentStatePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    @Nullable
    public Parcelable saveState() {
        return null;
    }
}
