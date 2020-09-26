package com.example.sew.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.adapters.NazmAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetContentListWithPaging;
import com.example.sew.common.PagingListView;
import com.example.sew.models.ShayariContent;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import static com.example.sew.common.MyConstants.NAZM_ID;

public class NazmsFragment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstNazams)
    PagingListView lstNazams;

    @OnItemClick(R.id.lstNazams)
    void onNazmClick(View convertView) {
        ShayariContent shayariContent = (ShayariContent) convertView.getTag(R.id.tag_data);
        if (shayariContent != null)
            startActivity(RenderContentActivity.getInstance(getActivity(), shayariContent.getId()));
    }

    public static NazmsFragment getInstance(String targetId) {
        NazmsFragment poetsFragment = new NazmsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TARGET_ID, targetId);
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nazams, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private String targetId;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        targetId = getArguments() != null ? getArguments().getString(TARGET_ID, "") : null;
        lstNazams.setPagingableListener(() -> {
            if (getContentListWithPaging != null) {
                getContentListWithPaging.loadMoreData();
                lstNazams.setIsLoading(true);
            }
        });
        getNazamsList();
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
    }

    private GetContentListWithPaging getContentListWithPaging;

    private void getNazamsList() {

        lstNazams.setIsLoading(true);
        getContentListWithPaging = new GetContentListWithPaging();
        getContentListWithPaging.setTargetId(targetId)
                .setContentTypeId(NAZM_ID)
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetContentListWithPaging>) contentListWithPaging -> {
                    dismissDialog();
                    if (contentListWithPaging.isValidResponse()) {
                        lstNazams.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getContentListWithPaging.isFirstPage())
                            nazams.clear();
                        nazams.addAll(getContentListWithPaging.getShayariContents());
                        if (getContentListWithPaging.getShayariContents() == null || getContentListWithPaging.getShayariContents().size() == 0 || nazams.size() == getContentListWithPaging.getTotalCount())
                            lstNazams.setHasMoreItems(false);
                        else
                            lstNazams.setHasMoreItems(true);
                        updateUI();
                    } else
                        showToast(contentListWithPaging.getErrorMessage());
                });
    }

    private ArrayList<ShayariContent> nazams = new ArrayList<>();
    private NazmAdapter nazmAdapter;

    private void updateUI() {
        if (nazmAdapter == null) {
            Parcelable state = lstNazams.onSaveInstanceState();
            nazmAdapter = new NazmAdapter(GetActivity(), nazams, targetId);
            lstNazams.setAdapter(nazmAdapter);
            lstNazams.onRestoreInstanceState(state);
        } else
            nazmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        nazmAdapter = null;
        updateUI();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }
}
