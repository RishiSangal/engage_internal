package com.example.sew.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.example.sew.R;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.adapters.PoetImageShayriAdapter;
import com.example.sew.adapters.PoetVideoAdapter;
import com.example.sew.adapters.TagShayriImageAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetShayariImageWithSearch;
import com.example.sew.apis.GetVideoListWithPaging;
import com.example.sew.common.Enums;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.ShayariImage;
import com.example.sew.models.VideoContent;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoetImageShayriFragment extends BasePoetProfileFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstShayariImage;

    public static BasePoetProfileFragment getInstance(PoetDetail poetDetail) {
        return getInstance(poetDetail, new PoetImageShayriFragment());
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
        lstShayariImage.setPagingableListener(() -> {
            if (getShayariImageWithSearch != null) {
                getShayariImageWithSearch.loadMoreData();
                lstShayariImage.setIsLoading(true);
            }
        });
        getShayriImage();
        updateLanguageSpecificUi();
        ViewCompat.setNestedScrollingEnabled(lstShayariImage, true);

    }
    private PoetImageShayriAdapter poetImageShayriAdapter;
    private ArrayList<ShayariImage> shayariImages = new ArrayList<>();
    private GetShayariImageWithSearch getShayariImageWithSearch;

    private void getShayriImage() {

        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        lstShayariImage.setIsLoading(true);
        getShayariImageWithSearch = new GetShayariImageWithSearch();
        getShayariImageWithSearch.setTargetType("3");
        getShayariImageWithSearch.setTargetIdSlug(getPoetDetail().getPoetId());
        getShayariImageWithSearch.addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetShayariImageWithSearch>) getShayariImageWithSearch -> {
                    dismissDialog();
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
                    if (getShayariImageWithSearch.isValidResponse()) {
                        lstShayariImage.setIsLoading(false);
                        if (getShayariImageWithSearch.isFirstPage())
                            shayariImages.clear();
                        shayariImages.addAll(getShayariImageWithSearch.getShayariImages());
                        if (CollectionUtils.isEmpty(getShayariImageWithSearch.getShayariImages()) || shayariImages.size() == getShayariImageWithSearch.getTotalCount())
                            lstShayariImage.setHasMoreItems(false);
                        else
                            lstShayariImage.setHasMoreItems(true);
                        if (CollectionUtils.isEmpty(shayariImages)) {
                            lstShayariImage.setVisibility(View.GONE);
                        } else {
                            lstShayariImage.setVisibility(View.VISIBLE);
                        }
                        updateUI();
                    } else
                        showToast(getShayariImageWithSearch.getErrorMessage());
                });


    }
    private void updateUI() {
        if (poetImageShayriAdapter == null) {
            poetImageShayriAdapter = new PoetImageShayriAdapter(GetActivity(), shayariImages,getPoetDetail());
            lstShayariImage.setAdapter(poetImageShayriAdapter);
        } else
            poetImageShayriAdapter.notifyDataSetChanged();

        updateLanguageSpecificUi();
    }

    private void updateLanguageSpecificUi() {
        //txtNoData.setText(MyHelper.getString(R.string.no_records_found));
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
    }
    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }


}
