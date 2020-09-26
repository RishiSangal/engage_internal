package com.example.sew.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.example.sew.R;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.adapters.PoetNazmAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetContentListWithPaging;
import com.example.sew.common.Enums;
import com.example.sew.common.PagingListView;
import com.example.sew.common.MyConstants;
import com.example.sew.models.ContentType;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.ShayariContent;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class PoetNazmFragment extends BasePoetProfileFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;
    private String defaultSortContent= Enums.SORT_CONTENT.POPULARITY.getKey();
    @OnItemClick(R.id.lstPoetContent)
    void onItemClick(View convertView) {
        ShayariContent shayariContent = (ShayariContent) convertView.getTag(R.id.tag_data);
        if (shayariContent != null)
            startActivity(RenderContentActivity.getInstance(getActivity(), shayariContent.getId()));
    }

    public static BasePoetProfileFragment getInstance(PoetDetail poetDetail, ContentType contentType) {
        return getInstance(poetDetail, contentType, new PoetNazmFragment());
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        poetNazmAdapter = null;
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

    private void getPoetGhazals(String sortBy) {
        getContentListWithPaging = new GetContentListWithPaging();
        getContentListWithPaging.setPoetId(getPoetDetail().getPoetId()).setSortBy(sortBy)
                .setContentTypeId(MyConstants.NAZM_ID)
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetContentListWithPaging>) contentListWithPaging -> {
                    if (contentListWithPaging.isValidResponse()) {
                        lstPoetContent.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getContentListWithPaging.isFirstPage())
                            poetNazms.clear();
                        poetNazms.addAll(getContentListWithPaging.getShayariContents());
                        if (getContentListWithPaging.getShayariContents() == null || getContentListWithPaging.getShayariContents().size() == 0 || poetNazms.size() == getContentListWithPaging.getTotalCount())
                            lstPoetContent.setHasMoreItems(false);
                        else
                            lstPoetContent.setHasMoreItems(true);
                        updateUI();
                    } else
                        showToast(contentListWithPaging.getErrorMessage());
                });
    }

    private ArrayList<ShayariContent> poetNazms = new ArrayList<>();
    private PoetNazmAdapter poetNazmAdapter;

    private void updateUI() {
        if (poetNazmAdapter == null) {
            Parcelable state= lstPoetContent.onSaveInstanceState();
            poetNazmAdapter = new PoetNazmAdapter(GetActivity(), poetNazms, getPoetDetail(), getContentType(), PoetNazmFragment.this);
            poetNazmAdapter.setTotalContentCount(getContentListWithPaging.getTotalCount());
            lstPoetContent.setAdapter(poetNazmAdapter);
            if(state != null)
                lstPoetContent.onRestoreInstanceState(state);
        } else
            poetNazmAdapter.notifyDataSetChanged();
    }
    public void sortContent(Enums.SORT_CONTENT sortBy){
            getPoetGhazals(sortBy.getKey());
    }
}