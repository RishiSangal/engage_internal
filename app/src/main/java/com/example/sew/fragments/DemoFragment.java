package com.example.sew.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sew.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DemoFragment extends Fragment {

    public static DemoFragment getInstance(CharSequence pageTitle) {
        DemoFragment demoFragment = new DemoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", pageTitle.toString());
        demoFragment.setArguments(bundle);
        return demoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nazams, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
