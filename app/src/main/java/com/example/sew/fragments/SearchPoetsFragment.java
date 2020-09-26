package com.example.sew.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.example.sew.R;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.adapters.SearchPoetsAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetSearchAll;
import com.example.sew.apis.GetSearchContentByType;
import com.example.sew.common.Enums;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.SearchContentAll;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class SearchPoetsFragment extends BaseSearchFragment {
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstSearchResult)
    PagingListView lstSearchResult;
    @BindView(R.id.txtNoData)
    TextView txtNoData;

//    public static BaseSearchFragment getInstance(String searchedText) {
//        return getInstance(new SearchPoetsFragment(), searchedText);
//    }
    public static BaseSearchFragment getInstance(Enums.CONTENT_TYPE contentType, String searchedText) {
        return getInstance(new SearchPoetsFragment(), contentType, searchedText);
    }
    @OnItemClick(R.id.lstSearchResult)
    void onItemClicked(View convertView) {
        SearchContentAll.SearchPoet poet = (SearchContentAll.SearchPoet) convertView.getTag(R.id.tag_data);
        if(!TextUtils.isEmpty(poet.getEntityId()))
            startActivity(PoetDetailActivity.getInstance(GetActivity(), poet.getEntityId()));
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateLanguageSpecificUi();
        if (!CollectionUtils.isEmpty(searchPoets)) {
            searchPoets.clear();
            updateUI();
            getSearchContent(searchKeyword);
        }
    }
    private void updateLanguageSpecificUi() {
        txtNoData.setText(MyHelper.getString(R.string.no_records_found));
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
        lstSearchResult.setPagingableListener(() -> {
            if (getSearchContentByType != null) {
                getSearchContentByType.loadMoreData();
                lstSearchResult.setIsLoading(true);
            }
        });
        if (!TextUtils.isEmpty(getSearchText()))
            getSearchContent(getSearchText());
        ViewCompat.setNestedScrollingEnabled(lstSearchResult, true);
    }

    private GetSearchAll getSearchAll;
    private GetSearchContentByType getSearchContentByType;
    private void getSearchContent(String keyWord) {
        lstSearchResult.setIsLoading(true);
        this.searchKeyword = keyWord;
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getSearchContentByType = new GetSearchContentByType();
        getSearchContentByType.setContentType(getContentType())
                .setKeyword(keyWord)
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetSearchContentByType>) getSearchContentByType -> {
                    lstSearchResult.setIsLoading(false);
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
                    SearchContentAll searchContentAll = getSearchContentByType.getSearchContentAll();
                    if (getSearchContentByType.isFirstPage())
                        searchPoets.clear();
                    searchPoets.addAll(searchContentAll.getPoets());
                    if (CollectionUtils.isEmpty(searchContentAll.getPoets()))
                        lstSearchResult.setHasMoreItems(false);
                    else
                        lstSearchResult.setHasMoreItems(true);
                    if (!TextUtils.isEmpty(keyWord) && CollectionUtils.isEmpty(searchPoets)) {
                        txtNoData.setVisibility(View.VISIBLE);
                        lstSearchResult.setVisibility(View.GONE);
                        updateLanguageSpecificUi();
                    } else {
                        txtNoData.setVisibility(View.GONE);
                        lstSearchResult.setVisibility(View.VISIBLE);
                    }
                    updateUI();

                });
    }
    private void getPoets(String keyWord) {
        this.searchKeyword = keyWord;
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getSearchAll = new GetSearchAll();
        getSearchAll.setKeyword(keyWord)
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetSearchAll>) getAudioListWithPaging -> {
                    if (getAudioListWithPaging.isValidResponse()) {
                        lstSearchResult.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        SearchContentAll searchContentAll = getSearchAll.getSearchContent();
                        searchPoets.clear();
                        searchPoets.addAll(searchContentAll.getPoets());
                        lstSearchResult.setHasMoreItems(false);
                        if (!TextUtils.isEmpty(keyWord) && CollectionUtils.isEmpty(searchPoets)) {
                            txtNoData.setVisibility(View.VISIBLE);
                            lstSearchResult.setVisibility(View.GONE);
                            updateLanguageSpecificUi();
                        } else {
                            txtNoData.setVisibility(View.GONE);
                            lstSearchResult.setVisibility(View.VISIBLE);
                        }
                        updateUI();
                    } else
                        showToast(getAudioListWithPaging.getErrorMessage());
                });
    }

    private ArrayList<SearchContentAll.SearchPoet> searchPoets = new ArrayList<>();
    private SearchPoetsAdapter searchPoetsAdapter;

    private void updateUI() {
        if (searchPoetsAdapter == null) {
            searchPoetsAdapter = new SearchPoetsAdapter(GetActivity(), searchPoets);
            lstSearchResult.setAdapter(searchPoetsAdapter);
        } else
            searchPoetsAdapter.notifyDataSetChanged();
    }

    private String searchKeyword;

    @Override
    void onSearchTextChanged(String newText) {
        updateUI();
        getSearchContent(newText);
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }
}
