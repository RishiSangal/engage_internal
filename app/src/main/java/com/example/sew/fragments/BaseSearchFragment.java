package com.example.sew.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.sew.R;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;

public abstract class BaseSearchFragment extends BaseFragment {
    abstract void onSearchTextChanged(String newText);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                onSearchTextChanged(intent.getStringExtra(SEARCH_TEXT));
            }
        }, BROADCAST_SEARCH_TEXT_CHANGED);
        super.onViewCreated(view, savedInstanceState);
    }

    public static BaseSearchFragment getInstance(BaseSearchFragment fragment, String searchedText) {
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_TYPE, Enums.CONTENT_TYPE.GHAZAL.name());
        bundle.putString(SEARCH_QUERY, searchedText);
        fragment.setArguments(bundle);
        return fragment;
    }

    static BaseSearchFragment getInstance(BaseSearchFragment fragment, Enums.CONTENT_TYPE contentType, String searchedText) {
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_TYPE, contentType == null ? Enums.CONTENT_TYPE.GHAZAL.name() : contentType.name());
        bundle.putString(SEARCH_QUERY, searchedText);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Enums.CONTENT_TYPE contentType;

    public final Enums.CONTENT_TYPE getContentType() {
        if (contentType != null)
            return contentType;
        if (getArguments() != null)
            return contentType = Enum.valueOf(Enums.CONTENT_TYPE.class, getArguments().getString(CONTENT_TYPE, Enums.CONTENT_TYPE.GHAZAL.name()));
        return contentType = Enums.CONTENT_TYPE.GHAZAL;
    }

    private String searchText;

    public final String getSearchText() {
        if (searchText != null)
            return searchText;
        if (getArguments() != null)
            return searchText = getArguments().getString(SEARCH_QUERY, "");
        return searchText = "";
    }

}
