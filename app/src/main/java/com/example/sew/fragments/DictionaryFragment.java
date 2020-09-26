package com.example.sew.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.adapters.DictionaryAdapter;
import com.example.sew.adapters.DictionaryRecyclerAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetRekhtaDictionary;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.SearchDictionary;
import com.example.sew.views.paging_recycler_view.PagingRecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DictionaryFragment extends BaseSearchFragment {
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstDictionary)
    PagingRecyclerView lstDictionary;
    @BindView(R.id.txtNoData)
    TextView txtNoData;

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateLanguageSpecificUi();
        updateUI();
        if (!CollectionUtils.isEmpty(allData)) {
            allData.clear();
            updateUI();
            getDictionaryResult(searchKeyword);
        }
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        if(dictionaryRecyclerAdapter!=null)
            dictionaryRecyclerAdapter.notifyDataSetChanged();
    }

    public static BaseSearchFragment getInstance(String searchedText) {
        return getInstance(new DictionaryFragment(), searchedText);
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
        if (!TextUtils.isEmpty(getSearchText()))
            getDictionaryResult(getSearchText());
    }

    private void getDictionaryResult(String keyword) {
        this.searchKeyword = keyword;
        getRekhtaDictionary(keyword);
    }

    private String searchKeyword;
    private ArrayList<SearchDictionary> searchDictionaries;
    private ArrayList<Object> allData = new ArrayList<>();

    private void getRekhtaDictionary(String keyword) {
//        lstDictionary.setHasMoreItems(false);
//        lstDictionary.setIsLoading(false);
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        new GetRekhtaDictionary()
                .setKeyword(keyword)
                .runAsync((BaseServiceable.OnApiFinishListener<GetRekhtaDictionary>) getRekhtaDictionary -> {
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
                    if (getRekhtaDictionary.isValidResponse()) {
                        searchDictionaries = getRekhtaDictionary.getDictionaries();
                        if (!TextUtils.isEmpty(keyword) && CollectionUtils.isEmpty(searchDictionaries)) {
                            txtNoData.setVisibility(View.VISIBLE);
                            lstDictionary.setVisibility(View.GONE);
                            updateLanguageSpecificUi();
                        } else {
                            txtNoData.setVisibility(View.GONE);
                            lstDictionary.setVisibility(View.VISIBLE);
                        }
                        updateUI();
                    } else
                        showToast(getRekhtaDictionary.getErrorMessage());
                    lstDictionary.onNoMoreData();
                    lstDictionary.onHide();
                });
    }
    private void updateLanguageSpecificUi() {
        txtNoData.setText(MyHelper.getString(R.string.no_records_found));
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

    @Override
    void onSearchTextChanged(String newText) {
        updateUI();
        getDictionaryResult(newText);
    }
}
