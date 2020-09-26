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
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.adapters.PoetSherAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetCoupletListWithPaging;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.PagingListView;
import com.example.sew.models.ContentType;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.SherContent;
import com.example.sew.models.SherTag;
import com.example.sew.models.WordContainer;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoetSherFragment extends BasePoetProfileFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;
    private String defaultSortContent = Enums.SORT_CONTENT.POPULARITY.getKey();

    public static BasePoetProfileFragment getInstance(PoetDetail poetDetail, ContentType contentType) {
        return getInstance(poetDetail, contentType, new PoetSherFragment());
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        poetSherAdapter = null;
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
            if (getCoupletListWithPaging != null) {
                getCoupletListWithPaging.loadMoreData();
                lstPoetContent.setIsLoading(true);
            }
        });
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getSherContent(defaultSortContent);
        ViewCompat.setNestedScrollingEnabled(lstPoetContent, true);
    }

    private GetCoupletListWithPaging getCoupletListWithPaging;

    private void getSherContent(String defaultSortContent) {
        getCoupletListWithPaging = new GetCoupletListWithPaging();
        getCoupletListWithPaging.setPoetId(getPoetDetail().getPoetId())
                .setPoetId(getPoetDetail().getPoetId()).setSortBy(defaultSortContent)
                .setContentTypeId(getContentType().getContentId())
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetCoupletListWithPaging>) coupletListWithPaging1 -> {
                    if (coupletListWithPaging1.isValidResponse()) {
                        lstPoetContent.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getCoupletListWithPaging.isFirstPage())
                            sherContents.clear();
                        sherContents.addAll(getCoupletListWithPaging.getSherContents());
                        if (getCoupletListWithPaging.getSherContents() == null || getCoupletListWithPaging.getSherContents().size() == 0 || sherContents.size() == getCoupletListWithPaging.getTotalCount())
                            lstPoetContent.setHasMoreItems(false);
                        else
                            lstPoetContent.setHasMoreItems(true);
                        updateUI();
                    } else
                        showToast(coupletListWithPaging1.getErrorMessage());
                });

    }

    private ArrayList<SherContent> sherContents = new ArrayList<>();
    private PoetSherAdapter poetSherAdapter;
    private View.OnClickListener onWordClickListener = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
//        MeaningBottomSheetFragment.getInstance(wordContainer.getWord(), wordContainer.getMeaning()).show(GetActivity().getSupportFragmentManager(), "MEANING");
        new MeaningBottomPopupWindow(GetActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();
    };

    private View.OnClickListener onTagClickListener = v -> {
        SherTag sherTag = (SherTag) v.getTag(R.id.tag_data);
        startActivity(SherTagOccasionActivity.getInstance(GetActivity(), sherTag));
    };
    private View.OnClickListener onGhazalClickListener = v -> {
        SherContent sherContent = (SherContent) v.getTag(R.id.tag_data);
        startActivity(RenderContentActivity.getInstance(GetActivity(), sherContent.getGhazalID()));
    };

    private void updateUI() {
        if (poetSherAdapter == null) {
            Parcelable state = lstPoetContent.onSaveInstanceState();
            poetSherAdapter = new PoetSherAdapter(GetActivity(), sherContents, getPoetDetail(), getContentType(), PoetSherFragment.this, defaultSortContent);
            poetSherAdapter.setTotalContentCount(getCoupletListWithPaging.getTotalCount());
            poetSherAdapter.setOnWordClickListener(onWordClickListener);
            poetSherAdapter.setOnTagClick(onTagClickListener);
            poetSherAdapter.setOnGhazalClickListener(onGhazalClickListener);
            lstPoetContent.setAdapter(poetSherAdapter);
            if (state != null)
                lstPoetContent.onRestoreInstanceState(state);
        } else
            poetSherAdapter.notifyDataSetChanged();
    }

    public void sortContent(Enums.SORT_CONTENT sortBy) {
        defaultSortContent = sortBy.getKey();
        poetSherAdapter = null;
        getSherContent(sortBy.getKey());
    }
}
