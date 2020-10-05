package com.example.sew.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.adapters.ProseShayariAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetContentListWithPaging;
import com.example.sew.common.Enums;
import com.example.sew.common.PagingListView;
import com.example.sew.models.ContentType;
import com.example.sew.models.ShayariContent;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class ProseShayariCollectionActivity extends BaseActivity {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;
    String targetId,slugId;
    private String defaultSortContent = Enums.SORT_CONTENT.POPULARITY.getKey();
    @OnItemClick(R.id.lstPoetContent)
    void onItemClicked(View convertView) {
        if (convertView.getTag(R.id.tag_data) instanceof ShayariContent) {
            ShayariContent shayariContent = (ShayariContent) convertView.getTag(R.id.tag_data);
            startActivity(RenderContentActivity.getInstance(getActivity(), shayariContent.getId(),slugId));
        }
    }

    public static Intent getInstance(BaseActivity activity, String targetId, ContentType contentType,String slugId) {
        Intent intent = new Intent(activity, ProseShayariCollectionActivity.class);
        intent.putExtra(TARGET_ID, targetId);
        intent.putExtra(CONTENT_TYPE_OBJ, contentType.getJsonObject().toString());
        intent.putExtra(SLUG_ID,slugId);
//        intent.putExtra(SHER_COLLECTION_TYPE, sherCollectionType.toString());
        return intent;
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateLanguageSpecificContent();
        updateUI();
    }

    ContentType contentType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sher_collection);
        ButterKnife.bind(this);
        targetId = getIntent().getStringExtra(TARGET_ID);
        slugId= getIntent().getStringExtra(SLUG_ID);
        try {
            contentType = new ContentType(new JSONObject(getIntent().getStringExtra(CONTENT_TYPE_OBJ)));
        } catch (Exception e) {
            showToast(e.getLocalizedMessage());
            finish();
            return;
        }
        lstPoetContent.setPagingableListener(() -> {
            if (getContentListWithPaging != null) {
                getContentListWithPaging.loadMoreData();
                lstPoetContent.setIsLoading(true);
            }
        });
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
        updateLanguageSpecificContent();
        getNazamsList(defaultSortContent);
    }

    private GetContentListWithPaging getContentListWithPaging;

    // no use shimmer in this page... show only native loader-- Anuj
    private void getNazamsList(String sortBy) {
        showDialog();
        lstPoetContent.setIsLoading(true);
        getContentListWithPaging = new GetContentListWithPaging();
        getContentListWithPaging.setTargetId(targetId).setSortBy(sortBy)
                .setContentTypeId(contentType.getContentId())
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetContentListWithPaging>) contentListWithPaging -> {
                    dismissDialog();
                    if (contentListWithPaging.isValidResponse()) {
                        slugId= getContentListWithPaging.getSlug();
                        lstPoetContent.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getContentListWithPaging.isFirstPage())
                            shayariContents.clear();
                        shayariContents.addAll(getContentListWithPaging.getShayariContents());
                        if (getContentListWithPaging.getShayariContents() == null || getContentListWithPaging.getShayariContents().size() == 0 || shayariContents.size() == getContentListWithPaging.getTotalCount())
                            lstPoetContent.setHasMoreItems(false);
                        else
                            lstPoetContent.setHasMoreItems(true);
                        updateUI();
                    } else
                        showToast(contentListWithPaging.getErrorMessage());
                });
    }

    private ArrayList<ShayariContent> shayariContents = new ArrayList<>();
    private ProseShayariAdapter proseShayariAdapter;

    private void updateLanguageSpecificContent() {
        setHeaderTitle(contentType.getName());
    }

    private void updateUI() {
        if (getContentListWithPaging != null)
            setHeaderTitle(getContentListWithPaging.getName());
        if (proseShayariAdapter == null) {
            proseShayariAdapter = new ProseShayariAdapter(getActivity(), shayariContents, contentType,defaultSortContent);
            if (getContentListWithPaging != null) {
                proseShayariAdapter.setTitle(getContentListWithPaging.getName());
                proseShayariAdapter.setDescription(getContentListWithPaging.getDescription());
            }
            lstPoetContent.setAdapter(proseShayariAdapter);
        } else {
            if (getContentListWithPaging != null) {
                proseShayariAdapter.setTitle(getContentListWithPaging.getName());
                proseShayariAdapter.setDescription(getContentListWithPaging.getDescription());
            }
            proseShayariAdapter.notifyDataSetChanged();
        }
    }
    public void sortContent(Enums.SORT_CONTENT sortBy) {
        defaultSortContent = sortBy.getKey();
        proseShayariAdapter = null;
        getNazamsList(sortBy.getKey());
    }
}
