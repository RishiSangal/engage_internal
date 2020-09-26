package com.example.sew.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.adapters.DictionaryRecyclerAdapter;
import com.example.sew.helpers.MyService;
import com.example.sew.models.FavoriteDictionary;
import com.example.sew.views.paging_recycler_view.PagingRecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteDictionaryFragment extends BaseFragment {
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstDictionary)
    PagingRecyclerView lstDictionary;

    public static Intent getInstance(BaseActivity activity) {
        return new Intent(activity, FavoriteDictionaryFragment.class);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
        if (!CollectionUtils.isEmpty(allData)) {
            allData.clear();
            updateUI();
            getDictionaryResult();
        }
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        if (dictionaryRecyclerAdapter != null)
            dictionaryRecyclerAdapter.notifyDataSetChanged();
    }

    public static FavoriteDictionaryFragment getInstance() {
        FavoriteDictionaryFragment favoriteDictionaryFragment = new FavoriteDictionaryFragment();
        Bundle bundle = new Bundle();
        favoriteDictionaryFragment.setArguments(bundle);
        return favoriteDictionaryFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
        lstDictionary.setNestedScrollingEnabled(true);
        getDictionaryResult();
    }

    private void getDictionaryResult() {
        getRekhtaDictionary();
    }

    private ArrayList<FavoriteDictionary> searchDictionaries = new ArrayList<>();
    private ArrayList<Object> allData = new ArrayList<>();

    private void getRekhtaDictionary() {
        searchDictionaries.clear();
        searchDictionaries.addAll(MyService.getAllFavoriteWordDictionary());
        Collections.reverse(searchDictionaries);
        updateUI();
        lstDictionary.onNoMoreData();
        lstDictionary.onHide();
    }

    private DictionaryRecyclerAdapter dictionaryRecyclerAdapter;

    private void updateUI() {
        dismissDialog();
        allData.clear();
        lstDictionary.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if (!CollectionUtils.isEmpty(searchDictionaries)) {
//                allData.add("REKHTA DICTIONARY");
            allData.addAll(searchDictionaries);
        }
        lstDictionary.setAdapter(dictionaryRecyclerAdapter = new DictionaryRecyclerAdapter(GetActivity(), allData));
    }

}
