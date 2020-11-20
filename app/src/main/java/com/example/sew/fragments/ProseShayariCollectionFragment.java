package com.example.sew.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sew.R;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.ProseShayariCollectionActivity;
import com.example.sew.adapters.ProseShayariCollectionAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetCollectionListByCollectionType;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.AppCollection;
import com.example.sew.models.ContentType;
import com.example.sew.views.paging_recycler_view.PagingRecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProseShayariCollectionFragment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.rvSherCollection)
    PagingRecyclerView rvSherCollection;
    private ContentType contentType;
    private Enums.COLLECTION_TYPE collectionType;
    private View.OnClickListener onItemClickListener = v -> {
        AppCollection sherCollection = (AppCollection) v.getTag(R.id.tag_data);
        if (sherCollection != null) {
            if (sherCollection.getProseShayariCategory() == Enums.PROSE_SHAYARI_CATEGORY.POET)
                startActivity(PoetDetailActivity.getInstance(GetActivity(), sherCollection.getPoetId(), MyHelper.getContentById(sherCollection.getContentTypeId())));
            else
                startActivity(ProseShayariCollectionActivity.getInstance(GetActivity(), sherCollection.getId(), MyHelper.getContentById(sherCollection.getContentTypeId()),sherCollection.getsEO_Slug()));

        }
//        startActivity(SherCollectionActivity.getInstance(GetActivity(), sherCollection));
    };

    public static ProseShayariCollectionFragment getInstance(String contentTypeId, Enums.COLLECTION_TYPE collectionType) {
        ProseShayariCollectionFragment proseShayariCollectionFragment = new ProseShayariCollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_TYPE, contentTypeId);
        bundle.putString(COLLECTION_TYPE, collectionType.toString());
        proseShayariCollectionFragment.setArguments(bundle);
        return proseShayariCollectionFragment;
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        getProseShayariCollection();
        proseShayariCollectionAdapter = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t20_sher, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            collectionType = Enums.COLLECTION_TYPE.valueOf(getArguments().getString(COLLECTION_TYPE));
            contentType = MyHelper.getContentById(getArguments().getString(CONTENT_TYPE));
        } else {
            GetActivity().finish();
            return;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (proseShayariCollectionAdapter == null)
                    return 1;
                switch (proseShayariCollectionAdapter.getItemViewType(position)) {
                    case ProseShayariCollectionAdapter.ITEM_TYPE_HEADER:
                        return 2;
                    case ProseShayariCollectionAdapter.ITEM_TYPE_CONTENT:
                        return 1;
                    default:
                        return 1;
                }
            }
        });
        rvSherCollection.setLayoutManager(gridLayoutManager);
        getProseShayariCollection();
    }

    private GetCollectionListByCollectionType getCollectionListByCollectionType;
    private String description;

    private void getProseShayariCollection() {
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getCollectionListByCollectionType = new GetCollectionListByCollectionType();
        getCollectionListByCollectionType.setCollectionType(collectionType)
                .setContentTypeId(contentType.getContentId())
                .addPagination();
        getCollectionListByCollectionType.runAsync((BaseServiceable.OnApiFinishListener<GetCollectionListByCollectionType>) getCollectionListByCollectionType -> {
            if (getCollectionListByCollectionType.isValidResponse()) {
                rvSherCollection.onHide();
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                if (this.getCollectionListByCollectionType.isFirstPage())
                    appCollections.clear();
                appCollections.addAll(getCollectionListByCollectionType.getAppCollections());
                description = getCollectionListByCollectionType.getDescription();
//                if (getT20Shers.getSherContents() == null || getT20Shers.getSherContents().size() == 0 || appCollections.size() == getT20Shers.getTotalCount())
//                    lstSherCollection.setHasMoreItems(false);
//                else
//                    lstSherCollection.setHasMoreItems(true);
                updateUI();
                rvSherCollection.onNoMoreData();
                rvSherCollection.onHide();
            } else
                showToast(getCollectionListByCollectionType.getErrorMessage());
        });

    }

    private ArrayList<AppCollection> appCollections = new ArrayList<>();
    private ProseShayariCollectionAdapter proseShayariCollectionAdapter;

    private void updateUI() {
        if (proseShayariCollectionAdapter == null) {
            proseShayariCollectionAdapter = new ProseShayariCollectionAdapter(GetActivity(), appCollections, contentType, description);
            proseShayariCollectionAdapter.setCollectionType(collectionType);
            proseShayariCollectionAdapter.setOnItemClickListener(onItemClickListener);
            rvSherCollection.setAdapter(proseShayariCollectionAdapter);
            rvSherCollection.setOnPagingListener((view1, direction) -> {
                if (getCollectionListByCollectionType != null) {
                    getCollectionListByCollectionType.loadMoreData();
                    rvSherCollection.onPaging();
                }
            });
        } else
            proseShayariCollectionAdapter.notifyDataSetChanged();

    }
}
