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
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.adapters.TagQuoteAdapter;
import com.example.sew.adapters.TagSherAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetCoupletListWithPaging;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.PagingListView;
import com.example.sew.models.ContentTypeTab;
import com.example.sew.models.CumulatedContentType;
import com.example.sew.models.SherContent;
import com.example.sew.models.SherTag;
import com.example.sew.models.WordContainer;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagSherFragment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;
    CumulatedContentType contentType;
    ContentTypeTab contentTypeTab;
    public static TagSherFragment getInstance(CumulatedContentType contentType, ContentTypeTab contentTypeTab) {
        TagSherFragment tagSherFragment = new TagSherFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CUMULATED_CONTENT_TYPE_OBJ, contentType.getJsonObject().toString());
        bundle.putString(CONTENT_TYPE_TAB, contentTypeTab.getJsonObject().toString());
        tagSherFragment.setArguments(bundle);
        return tagSherFragment;
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        tagSherAdapter = null;
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
        try {
            contentType = new CumulatedContentType(new JSONObject(getArguments().getString(CUMULATED_CONTENT_TYPE_OBJ)));
            contentTypeTab = new ContentTypeTab((new JSONObject(getArguments().getString(CONTENT_TYPE_TAB))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        getSherContent();
        ViewCompat.setNestedScrollingEnabled(lstPoetContent, true);
    }

    private GetCoupletListWithPaging getCoupletListWithPaging;

    private void getSherContent() {
        getCoupletListWithPaging = new GetCoupletListWithPaging();
        getCoupletListWithPaging.setContentTypeId(contentType.getTypeId()).setTargetId(contentTypeTab.getTargetId()).addPagination()
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
    //private PoetSherAdapter poetSherAdapter;
    private TagSherAdapter tagSherAdapter;
    private TagQuoteAdapter tagQuoteAdapter;
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
    private View.OnClickListener onPoetNameClickListener = v -> {
        SherContent sherContent = (SherContent) v.getTag(R.id.tag_data);
        startActivity(PoetDetailActivity.getInstance(GetActivity(), sherContent.getPI()));
    };
    private void updateUI() {
        if (contentType.getListRenderingFormat() == Enums.LIST_RENDERING_FORMAT.SHER){
            if (tagSherAdapter == null) {
                Parcelable state = lstPoetContent.onSaveInstanceState();
                tagSherAdapter = new TagSherAdapter(GetActivity(), sherContents);
                tagSherAdapter.setOnWordClickListener(onWordClickListener);
                tagSherAdapter.setOnTagClick(onTagClickListener);
                tagSherAdapter.setOnGhazalClickListener(onGhazalClickListener);
                tagSherAdapter.setOnPoetNameClickListener(onPoetNameClickListener);
                lstPoetContent.setAdapter(tagSherAdapter);
                if (state != null)
                    lstPoetContent.onRestoreInstanceState(state);
            } else
                tagSherAdapter.notifyDataSetChanged();
    }else if(contentType.getListRenderingFormat() == Enums.LIST_RENDERING_FORMAT.QUOTE){
            if (tagQuoteAdapter == null) {
                Parcelable state = lstPoetContent.onSaveInstanceState();

                tagQuoteAdapter = new TagQuoteAdapter(GetActivity(), sherContents);
                tagQuoteAdapter.setTotalContentCount(getCoupletListWithPaging.getTotalCount());
                tagQuoteAdapter.setOnWordClickListener(onWordClickListener);
                tagQuoteAdapter.setOnTagClick(onTagClickListener);
                tagQuoteAdapter.setOnGhazalClickListener(onGhazalClickListener);
                lstPoetContent.setAdapter(tagQuoteAdapter);
                if (state != null)
                    lstPoetContent.onRestoreInstanceState(state);
            } else
                tagQuoteAdapter.notifyDataSetChanged();
        }
    }
}
