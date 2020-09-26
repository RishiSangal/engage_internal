package com.example.sew.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.example.sew.R;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.adapters.PoetGhazalAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetContentListWithPaging;
import com.example.sew.common.Enums;
import com.example.sew.common.PagingListView;
import com.example.sew.models.ShayariContent;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public abstract class BasePoetGhazalFragment extends BasePoetProfileFragment {

    public abstract String getContentTypeId();
    private String defaultSortContent= Enums.SORT_CONTENT.POPULARITY.getKey();
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;

    @OnItemClick(R.id.lstPoetContent)
    void onItemClick(View convertView) {
        ShayariContent shayariContent = (ShayariContent) convertView.getTag(R.id.tag_data);
        if (shayariContent != null)
            startActivity(RenderContentActivity.getInstance(getActivity(), shayariContent.getId()));
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        poetGhazalAdapter = null;
        updateUI();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poet_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstPoetContent.setPagingableListener(() -> {
            if (getContentListWithPaging != null) {
                getContentListWithPaging.loadMoreData();
                lstPoetContent.setIsLoading(true);
            }
        });
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);

        getPoetGhazals(defaultSortContent);
        ViewCompat.setNestedScrollingEnabled(lstPoetContent, true);
    }

    private GetContentListWithPaging getContentListWithPaging;

    private void getPoetGhazals(String sortContent) {
        getContentListWithPaging = new GetContentListWithPaging();
        getContentListWithPaging.setPoetId(getPoetDetail().getPoetId())
                .setContentTypeId(getContentTypeId()).setSortBy(sortContent)
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetContentListWithPaging>) contentListWithPaging -> {
                    if (contentListWithPaging.isValidResponse()) {
                        lstPoetContent.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getContentListWithPaging.isFirstPage())
                            poetGhazals.clear();
                        poetGhazals.addAll(getContentListWithPaging.getShayariContents());
                        if (getContentListWithPaging.getShayariContents() == null || getContentListWithPaging.getShayariContents().size() == 0 || poetGhazals.size() == getContentListWithPaging.getTotalCount())
                            lstPoetContent.setHasMoreItems(false);
                        else
                            lstPoetContent.setHasMoreItems(true);
                        updateUI();
                    } else
                        showToast(contentListWithPaging.getErrorMessage());
                });
    }

    private ArrayList<ShayariContent> poetGhazals = new ArrayList<>();
    private PoetGhazalAdapter poetGhazalAdapter;
    private String sortedBy;
    private void updateUI() {
        if (poetGhazalAdapter == null) {
            poetGhazalAdapter = new PoetGhazalAdapter(GetActivity(), poetGhazals, getPoetDetail(), getContentType(),BasePoetGhazalFragment.this,defaultSortContent);
            poetGhazalAdapter.setTotalContentCount(getContentListWithPaging.getTotalCount());
            lstPoetContent.setAdapter(poetGhazalAdapter);
        } else
            poetGhazalAdapter.notifyDataSetChanged();
    }
    public void sortContent(Enums.SORT_CONTENT sortBy){
        defaultSortContent= sortBy.getKey();
        poetGhazalAdapter = null;
        getPoetGhazals(sortBy.getKey());

    }
}
