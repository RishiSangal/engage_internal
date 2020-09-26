package com.example.sew.fragments;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sew.R;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.SherCollectionActivity;
import com.example.sew.adapters.ProseShayariCollectionAdapter;
import com.example.sew.adapters.T20CollectionAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetCoupletListWithPaging;
import com.example.sew.apis.GetT20Shers;
import com.example.sew.apis.GetWordMeaning;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.SherCollection;
import com.example.sew.models.WordContainer;
import com.example.sew.views.paging_recycler_view.PagingRecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class T20SherFragment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.rvSherCollection)
    PagingRecyclerView rvSherCollection;
    private View.OnClickListener onItemClickListener = v -> {
        SherCollection sherCollection = (SherCollection) v.getTag(R.id.tag_data);
//        startActivity(PoetDetailActivity.getInstance(GetActivity(), sherCollection.getPoetId()));
        startActivity(SherCollectionActivity.getInstance(GetActivity(), sherCollection));
    };

    public static T20SherFragment getInstance() {
        T20SherFragment t20SherFragment = new T20SherFragment();
        Bundle bundle = new Bundle();
        t20SherFragment.setArguments(bundle);
        return t20SherFragment;
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (t20CollectionAdapter == null)
                    return 1;
                if (t20CollectionAdapter.getItemViewType(position) == T20CollectionAdapter.ITEM_TYPE_HEADER)
                    return 2;
                return 1;
            }
        });

        rvSherCollection.setLayoutManager(gridLayoutManager);
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getSherContent();
    }

    private GetT20Shers getT20Shers;

    private void getSherContent() {
        getT20Shers = new GetT20Shers();
        getT20Shers.runAsync((BaseServiceable.OnApiFinishListener<GetT20Shers>) getT20Shers -> {
            if (getT20Shers.isValidResponse()) {
                rvSherCollection.onHide();
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                if (this.getT20Shers.isFirstPage())
                    sherCollections.clear();
                sherCollections.addAll(getT20Shers.getSherCollections());
//                if (getT20Shers.getSherContents() == null || getT20Shers.getSherContents().size() == 0 || sherCollections.size() == getT20Shers.getTotalCount())
//                    lstSherCollection.setHasMoreItems(false);
//                else
//                    lstSherCollection.setHasMoreItems(true);
                updateUI();
                rvSherCollection.onNoMoreData();
                rvSherCollection.onHide();
            } else
                showToast(getT20Shers.getErrorMessage());
        });

    }

    private ArrayList<SherCollection> sherCollections = new ArrayList<>();
    private T20CollectionAdapter t20CollectionAdapter;

    private void updateUI() {
        if (t20CollectionAdapter == null) {
            Parcelable state= rvSherCollection.getLayoutManager().onSaveInstanceState();
            t20CollectionAdapter = new T20CollectionAdapter(GetActivity(), sherCollections);
            t20CollectionAdapter.setOnItemClickListener(onItemClickListener);
            rvSherCollection.setAdapter(t20CollectionAdapter);
            rvSherCollection.setOnPagingListener((view1, direction) -> {
                if (getT20Shers != null) {
                    getT20Shers.loadMoreData();
                    rvSherCollection.onPaging();
                }
            });
            rvSherCollection.getLayoutManager().onRestoreInstanceState(state);
        } else
            t20CollectionAdapter.notifyDataSetChanged();

    }
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (rvSherCollection != null) {
//            outState.putParcelable("SCROLL_POSITION", rvSherCollection.getLayoutManager().onSaveInstanceState());
//        }
//    }

}
