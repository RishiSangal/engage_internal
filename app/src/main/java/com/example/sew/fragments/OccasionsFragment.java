package com.example.sew.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sew.R;
import com.example.sew.activities.SherCollectionActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.adapters.OccasionsCollectionAdapter;
import com.example.sew.adapters.T20CollectionAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetOccasions;
import com.example.sew.models.OccasionCollection;
import com.example.sew.models.SherCollection;
import com.example.sew.views.paging_recycler_view.PagingRecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OccasionsFragment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.rvOccasions)
    PagingRecyclerView rvOccasions;
    private Parcelable listState;
    private View.OnClickListener onItemClickListener = v -> {
        OccasionCollection occasionCollection = (OccasionCollection) v.getTag(R.id.tag_data);
        startActivity(SherTagOccasionActivity.getInstance(GetActivity(), occasionCollection));
       // startActivity(SherCollectionActivity.getInstance(GetActivity(), occasionCollection));
    };
    public static OccasionsFragment getInstance() {
        OccasionsFragment occasionsFragment = new OccasionsFragment();
        Bundle bundle = new Bundle();
        occasionsFragment.setArguments(bundle);
        return occasionsFragment;
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        if (!CollectionUtils.isEmpty(occasionCollections))
            occasionCollections.clear();
        updateUI();
        getOccasions();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_occasions, container, false);
        ButterKnife.bind(this, view);
        if(listState!=null)
            listState=savedInstanceState.getParcelable("ListState");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (occasionsCollectionAdapter == null)
                    return 1;
                if (occasionsCollectionAdapter.getItemViewType(position) == OccasionsCollectionAdapter.ITEM_TYPE_HEADER)
                    return 2;
                return 1;
            }
        });
        rvOccasions.setLayoutManager(gridLayoutManager);
        getOccasions();
    }

    private GetOccasions getOccasions;

    private void getOccasions() {
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getOccasions = new GetOccasions();
        getOccasions.addPagination();
        getOccasions.runAsync((BaseServiceable.OnApiFinishListener<GetOccasions>) getOccasions -> {
            if (getOccasions.isValidResponse()) {
                rvOccasions.onHide();
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                if (this.getOccasions.isFirstPage())
                    occasionCollections.clear();
                occasionCollections.addAll(getOccasions.getOccasionCollections());
                updateUI();
                rvOccasions.onNoMoreData();
                rvOccasions.onHide();
            } else
                showToast(getOccasions.getErrorMessage());
        });

    }

    private ArrayList<OccasionCollection> occasionCollections = new ArrayList<>();
    private OccasionsCollectionAdapter occasionsCollectionAdapter;
    private void updateUI() {
        if (occasionsCollectionAdapter == null) {
            Parcelable state = rvOccasions.getLayoutManager().onSaveInstanceState();
            occasionsCollectionAdapter = new OccasionsCollectionAdapter(GetActivity(), occasionCollections);
            occasionsCollectionAdapter.setOnItemClickListener(onItemClickListener);
            rvOccasions.setAdapter(occasionsCollectionAdapter);
            rvOccasions.setOnPagingListener((view1, direction) -> {
                if (getOccasions != null) {
                    getOccasions.loadMoreData();
                    rvOccasions.onPaging();
                }
            });
            rvOccasions.getLayoutManager().onRestoreInstanceState(state);
        } else
            occasionsCollectionAdapter.notifyDataSetChanged();

    }

}
