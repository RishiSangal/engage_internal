package com.example.sew.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.example.sew.R;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.adapters.SearchGhazalNazmSherAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetSearchContentByType;
import com.example.sew.common.Enums;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.SearchContent;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class SearchGhazalNazmSherFragment extends BaseSearchFragment {
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstSearchResult)
    PagingListView lstSearchResult;
    @BindView(R.id.txtNoData)
    TextView txtNoData;

    public static BaseSearchFragment getInstance(Enums.CONTENT_TYPE contentType, String searchedText) {
        return getInstance(new SearchGhazalNazmSherFragment(), contentType, searchedText);
    }

    @OnItemClick(R.id.lstSearchResult)
    void onItemClicked(View convertView) {
        SearchContent searchContent = (SearchContent) convertView.getTag(R.id.tag_data);
        if (searchContent != null && !TextUtils.isEmpty(searchContent.getId()))
            startActivity(RenderContentActivity.getInstance(getActivity(), searchContent.getId()));
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateLanguageSpecificUi();
        if (!CollectionUtils.isEmpty(searchContents)) {
            searchContents.clear();
            updateUI();
            getSearchContent(searchKeyword);
        }
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
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

    private GetSearchContentByType getSearchContentByType;
    private ArrayList<SearchContent> searchContents = new ArrayList<>();

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
                    if (getSearchContentByType.isValidResponse()) {
                        if (getSearchContentByType.isFirstPage())
                            searchContents.clear();
                        searchContents.addAll(getSearchContentByType.getSearchContents());
                        if (CollectionUtils.isEmpty(getSearchContentByType.getSearchContents()) || searchContents.size() == getSearchContentByType.getTotalCount())
                            lstSearchResult.setHasMoreItems(false);
                        else
                            lstSearchResult.setHasMoreItems(true);
                        if (!TextUtils.isEmpty(keyWord) && CollectionUtils.isEmpty(searchContents)) {
                            txtNoData.setVisibility(View.VISIBLE);
                            lstSearchResult.setVisibility(View.GONE);
                            updateLanguageSpecificUi();
                        } else {
                            txtNoData.setVisibility(View.GONE);
                            lstSearchResult.setVisibility(View.VISIBLE);
                        }
                        updateUI();
                    } else
                        showToast(getSearchContentByType.getErrorMessage());
                });
    }

    private void updateLanguageSpecificUi() {
        txtNoData.setText(MyHelper.getString(R.string.no_records_found));
    }

    private SearchGhazalNazmSherAdapter searchGhazalNazmSherAdapter;

    private void updateUI() {
        if (searchGhazalNazmSherAdapter == null) {
            searchGhazalNazmSherAdapter = new SearchGhazalNazmSherAdapter(GetActivity(), searchContents);
            lstSearchResult.setAdapter(searchGhazalNazmSherAdapter);
        } else
            searchGhazalNazmSherAdapter.notifyDataSetChanged();
    }

    private String searchKeyword;

    @Override
    void onSearchTextChanged(String newText) {
        updateUI();
        getSearchContent(newText);
    }
}
