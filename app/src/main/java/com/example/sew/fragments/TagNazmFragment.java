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
import com.example.sew.adapters.PoetNazmAdapter;
import com.example.sew.adapters.TagNazmAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetContentListWithPaging;
import com.example.sew.common.MyConstants;
import com.example.sew.common.PagingListView;
import com.example.sew.models.ContentType;
import com.example.sew.models.ContentTypeTab;
import com.example.sew.models.CumulatedContentType;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.ShayariContent;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class TagNazmFragment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;

    private CumulatedContentType contentType;
    private ContentTypeTab contentTypeTab;
    @OnItemClick(R.id.lstPoetContent)
    void onItemClick(View convertView) {
        ShayariContent shayariContent = (ShayariContent) convertView.getTag(R.id.tag_data);
        if (shayariContent != null)
            startActivity(RenderContentActivity.getInstance(getActivity(), shayariContent.getId()));
    }

    public static TagNazmFragment getInstance(CumulatedContentType contentType, ContentTypeTab contentTypeTab) {
        TagNazmFragment tagNazmFragment = new TagNazmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CUMULATED_CONTENT_TYPE_OBJ, contentType.getJsonObject().toString());
        bundle.putString(CONTENT_TYPE_TAB, contentTypeTab.getJsonObject().toString());
        tagNazmFragment.setArguments(bundle);
        return tagNazmFragment;
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        tagNazmAdapter = null;
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
            if (getContentListWithPaging != null) {
                getContentListWithPaging.loadMoreData();
                lstPoetContent.setIsLoading(true);
            }
        });
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getContentList();
        ViewCompat.setNestedScrollingEnabled(lstPoetContent, true);
    }

    private GetContentListWithPaging getContentListWithPaging;

    private void getContentList() {
        getContentListWithPaging = new GetContentListWithPaging();
        getContentListWithPaging.setContentTypeId(contentType.getTypeId()).setTargetId(contentTypeTab.getTargetId())
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetContentListWithPaging>) contentListWithPaging -> {
                    if (contentListWithPaging.isValidResponse()) {
                        lstPoetContent.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getContentListWithPaging.isFirstPage())
                            tagNazms.clear();
                        tagNazms.addAll(getContentListWithPaging.getShayariContents());
                        if (getContentListWithPaging.getShayariContents() == null || getContentListWithPaging.getShayariContents().size() == 0 || tagNazms.size() == getContentListWithPaging.getTotalCount())
                            lstPoetContent.setHasMoreItems(false);
                        else
                            lstPoetContent.setHasMoreItems(true);
                        updateUI();
                    } else
                        showToast(contentListWithPaging.getErrorMessage());
                });
    }

    private ArrayList<ShayariContent> tagNazms = new ArrayList<>();
    private TagNazmAdapter tagNazmAdapter;

    private void updateUI() {
        if (tagNazmAdapter == null) {
            Parcelable state = lstPoetContent.onSaveInstanceState();
            tagNazmAdapter = new TagNazmAdapter(GetActivity(), tagNazms);
            lstPoetContent.setAdapter(tagNazmAdapter);
            if (state != null)
                lstPoetContent.onRestoreInstanceState(state);
        } else
            tagNazmAdapter.notifyDataSetChanged();
    }
}
