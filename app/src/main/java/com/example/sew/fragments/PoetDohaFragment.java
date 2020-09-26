package com.example.sew.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.adapters.PoetDohaAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetCoupletListWithPaging;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.PagingListView;
import com.example.sew.common.MyConstants;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.SherContent;
import com.example.sew.models.WordContainer;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoetDohaFragment extends BasePoetProfileFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;
    private String defaultSortContent= Enums.SORT_CONTENT.POPULARITY.getKey();

    public static BasePoetProfileFragment getInstance(PoetDetail poetDetail) {
        return getInstance(poetDetail, new PoetDohaFragment());
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        poetDohaAdapter = null;
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
    }

    private GetCoupletListWithPaging getCoupletListWithPaging;

    private void getSherContent(String defaultSortContent) {
        getCoupletListWithPaging = new GetCoupletListWithPaging();
        getCoupletListWithPaging.setPoetId(getPoetDetail().getPoetId()).setSortBy(defaultSortContent)
                .setPoetId(getPoetDetail().getPoetId())
                .setContentTypeId(MyConstants.DOHA_ID)
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
    private PoetDohaAdapter poetDohaAdapter;
    private View.OnClickListener onWordClickListener = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
//        MeaningBottomSheetFragment.getInstance(wordContainer.getWord(), wordContainer.getMeaning()).show(GetActivity().getSupportFragmentManager(), "MEANING");
        new MeaningBottomPopupWindow(GetActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();
    };

    private void updateUI() {
        if (poetDohaAdapter == null) {
            poetDohaAdapter = new PoetDohaAdapter(GetActivity(), sherContents, getPoetDetail(),PoetDohaFragment.this);
            poetDohaAdapter.setOnWordClickListener(onWordClickListener);
            poetDohaAdapter.setTotalContentCount(getCoupletListWithPaging.getTotalCount());
            lstPoetContent.setAdapter(poetDohaAdapter);
        } else
            poetDohaAdapter.notifyDataSetChanged();
    }
    public void sortContent(Enums.SORT_CONTENT sortBy){
        getSherContent(sortBy.getKey());
    }
}
