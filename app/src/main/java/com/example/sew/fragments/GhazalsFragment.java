package com.example.sew.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.adapters.GhazalAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetContentListWithPaging;
import com.example.sew.common.PagingListView;
import com.example.sew.models.ShayariContent;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import static com.example.sew.common.MyConstants.GHAZAL_ID;

public class GhazalsFragment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstGhazals)
    PagingListView lstGhazals;
    @BindView(R.id.viewEndLine)
    View viewEndLine;
    @OnItemClick(R.id.lstGhazals)
    void onGhazalClick(View convertView, AdapterView<?> adapterView,int position) {
        ShayariContent shayariContent = (ShayariContent) convertView.getTag(R.id.tag_data);
        if (shayariContent != null)
            startActivity(RenderContentActivity.getInstance(getActivity(), shayariContent.getId()));
    }

    public static GhazalsFragment getInstance(String targetId) {
        GhazalsFragment poetsFragment = new GhazalsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TARGET_ID, targetId);
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }
    private AbsListView.OnScrollListener onScrollListener;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ghazals, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private String targetId;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        targetId = getArguments() != null ? getArguments().getString(TARGET_ID, "") : null;
        lstGhazals.setPagingableListener(() -> {
            if (getContentListWithPaging != null) {
                getContentListWithPaging.loadMoreData();
                lstGhazals.setIsLoading(true);
            }
        });
        getGhazalsList();
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
    }

    private GetContentListWithPaging getContentListWithPaging;

    private void getGhazalsList() {

        lstGhazals.setIsLoading(true);
        getContentListWithPaging = new GetContentListWithPaging();
        getContentListWithPaging.setTargetId(targetId)
                .setContentTypeId(GHAZAL_ID)
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetContentListWithPaging>) contentListWithPaging -> {
                    dismissDialog();
                    lstGhazals.setIsLoading(false);
                    if (contentListWithPaging.isValidResponse()) {
                        lstGhazals.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getContentListWithPaging.isFirstPage())
                            ghazals.clear();
                        ghazals.addAll(getContentListWithPaging.getShayariContents());
                        if (getContentListWithPaging.getShayariContents() == null || getContentListWithPaging.getShayariContents().size() == 0 || ghazals.size() == getContentListWithPaging.getTotalCount()) {
                            lstGhazals.setHasMoreItems(false);
                        } else {
                            lstGhazals.setHasMoreItems(true);
                        }
                        updateUI();
                    } else
                        showToast(contentListWithPaging.getErrorMessage());
                });
    }

    private ArrayList<ShayariContent> ghazals = new ArrayList<>();
    private GhazalAdapter ghazalAdapter;

    private void updateUI() {
        if (ghazalAdapter == null) {
            Parcelable state = lstGhazals.onSaveInstanceState();
            ghazalAdapter = new GhazalAdapter(GetActivity(), ghazals, targetId);
            lstGhazals.setAdapter(ghazalAdapter);
            lstGhazals.onRestoreInstanceState(state);
        } else
            ghazalAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        ghazalAdapter = null;
        updateUI();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }
}
