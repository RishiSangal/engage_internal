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
import com.example.sew.adapters.PoetVideoAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetVideoListWithPaging;
import com.example.sew.common.PagingListView;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.VideoContent;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoetVideoFragment extends BasePoetProfileFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;

    public static BasePoetProfileFragment getInstance(PoetDetail poetDetail) {
        return getInstance(poetDetail, new PoetVideoFragment());
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        poetVideoAdapter = null;
        updateUI();
    }
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
            if (getVideoListWithPaging != null) {
                getVideoListWithPaging.loadMoreData();
                lstPoetContent.setIsLoading(true);
            }
        });
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getPoetAudios();
        ViewCompat.setNestedScrollingEnabled(lstPoetContent, true);
    }

    private GetVideoListWithPaging getVideoListWithPaging;

    private void getPoetAudios() {

        getVideoListWithPaging = new GetVideoListWithPaging();
        getVideoListWithPaging.setPoetId(getPoetDetail().getPoetId())
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetVideoListWithPaging>) getVideoListWithPaging -> {
                    if (getVideoListWithPaging.isValidResponse()) {
                        lstPoetContent.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getVideoListWithPaging.isFirstPage())
                            videoContents.clear();
                        videoContents.addAll(getVideoListWithPaging.getVideoContents());
                        if (getVideoListWithPaging.getVideoContents() == null ||
                                getVideoListWithPaging.getVideoContents().size() == 0 ||
                                videoContents.size() == getVideoListWithPaging.getTotalCount())
                            lstPoetContent.setHasMoreItems(false);
                        else
                            lstPoetContent.setHasMoreItems(true);
                        updateUI();
                    } else
                        showToast(getVideoListWithPaging.getErrorMessage());
                });
    }

    private ArrayList<VideoContent> videoContents = new ArrayList<>();
    private PoetVideoAdapter poetVideoAdapter;

    private void updateUI() {
        if (poetVideoAdapter == null) {
            Parcelable state= lstPoetContent.onSaveInstanceState();
            poetVideoAdapter = new PoetVideoAdapter(GetActivity(), videoContents, getPoetDetail());
            lstPoetContent.setAdapter(poetVideoAdapter);
            lstPoetContent.onRestoreInstanceState(state);
        } else
            poetVideoAdapter.notifyDataSetChanged();
    }
}
