package com.example.sew.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.activities.ShayariImageDetailActivity;
import com.example.sew.adapters.ShayariImageAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetShayariImageWithSearch;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ShayariImage;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import androidx.core.view.ViewCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class ShayariImageFragment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstShayariImage)
    PagingListView lstShayariImage;
    @BindView(R.id.edSearch)
    EditText edSearch;
    @BindView(R.id.laySearch)
    View laySearch;
    @BindView(R.id.txtNoData)
    TextView txtNoData;
    @BindView(R.id.txtNoSavedImage)
    TextView txtNoSavedImage;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtDescription)
    TextView txtDescription;

    @OnItemClick(R.id.lstShayariImage)
    void onShayariClick(View convertView) {
        ShayariImage shayariImage = (ShayariImage) convertView.getTag(R.id.tag_data);
        startActivity(ShayariImageDetailActivity.getInstance(GetActivity(), shayariImages, shayariImage));
//        startActivity(PoetDetailActivity.getInstance(getActivity(), poet.getPoetId()));
    }

    @OnEditorAction(R.id.edSearch)
    void onSearchKeyboardAction(int actionId) {
        hideKeyBoard();
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            showDialog();
            getPoetLists(getEditTextData(edSearch));
        }
    }

    @OnTextChanged(R.id.edSearch)
    void onSearchTextChanged() {
        String searchText = getEditTextData(edSearch);
        if (TextUtils.isEmpty(searchText))
            getPoetLists(searchText);
    }

    public static ShayariImageFragment getInstance(boolean locallySaved) {
        ShayariImageFragment poetsFragment = new ShayariImageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(LOCALLY_SAVED, locallySaved);
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shayari_image, container, false);
        if (getArguments() != null)
            locallySaved = getArguments().getBoolean(LOCALLY_SAVED, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private ShayariImageAdapter shayariImageAdapter;
    private ArrayList<ShayariImage> shayariImages = new ArrayList<>();
    private boolean locallySaved;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstShayariImage.setPagingableListener(() -> {
            if (getShayariImageWithSearch != null) {
                getShayariImageWithSearch.loadMoreData();
                lstShayariImage.setIsLoading(true);
            }
        });
        getPoetLists(null);
        laySearch.setVisibility(locallySaved ? View.GONE : View.VISIBLE);
        updateLanguageSpecificUi();
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (locallySaved)
                    getPoetLists(null);
            }
        }, BROADCAST_SAVED_IMAGE_SHAYARI_UPDATED);
        ViewCompat.setNestedScrollingEnabled(lstShayariImage, true);
    }

    private GetShayariImageWithSearch getShayariImageWithSearch;

    private void getPoetLists(final String keyword) {
        if (locallySaved) {
            shimmerViewContainer.stopShimmer();
            shimmerViewContainer.setVisibility(View.GONE);
            lstShayariImage.setIsLoading(false);
            lstShayariImage.setHasMoreItems(false);
            shayariImages.clear();
            shayariImages.addAll(MyService.getAllSavedImageShayari());
            if(shayariImages.size()==0){
                txtNoSavedImage.setVisibility(View.VISIBLE);
                txtNoSavedImage.setText(MyHelper.getString(R.string.saved_image_will_be_show_here));
                lstShayariImage.setVisibility(View.GONE);
            }else{
                txtNoSavedImage.setVisibility(View.GONE);
                lstShayariImage.setVisibility(View.VISIBLE);
            }
            updateUI();
        } else {
            shimmerViewContainer.startShimmer();
            shimmerViewContainer.setVisibility(View.VISIBLE);
            lstShayariImage.setIsLoading(true);
            getShayariImageWithSearch = new GetShayariImageWithSearch();
            getShayariImageWithSearch.setKeyword(TextUtils.isEmpty(keyword) ? "" : keyword)
                    .addPagination()
                    .runAsync((BaseServiceable.OnApiFinishListener<GetShayariImageWithSearch>) getShayariImageWithSearch -> {
                        dismissDialog();
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getShayariImageWithSearch.isValidResponse()) {
                            lstShayariImage.setIsLoading(false);
                            if (getShayariImageWithSearch.isFirstPage())
                                shayariImages.clear();
                            shayariImages.addAll(getShayariImageWithSearch.getShayariImages());
                            if (CollectionUtils.isEmpty(getShayariImageWithSearch.getShayariImages()) || shayariImages.size() == getShayariImageWithSearch.getTotalCount())
                                lstShayariImage.setHasMoreItems(false);
                            else
                                lstShayariImage.setHasMoreItems(true);
                            if (!TextUtils.isEmpty(keyword) && CollectionUtils.isEmpty(shayariImages)) {
                                txtNoData.setVisibility(View.VISIBLE);
                                lstShayariImage.setVisibility(View.GONE);
                            } else {
                                txtNoData.setVisibility(View.GONE);
                                lstShayariImage.setVisibility(View.VISIBLE);
                            }
                            updateUI();
                        } else
                            showToast(getShayariImageWithSearch.getErrorMessage());
                    });
        }

    }

    private void updateUI() {
        if (shayariImageAdapter == null) {
            shayariImageAdapter = new ShayariImageAdapter(GetActivity(), shayariImages);
            if (getShayariImageWithSearch != null) {
                txtDescription.setText(getShayariImageWithSearch.getDescription());
                txtTitle.setText(MyHelper.getString(R.string.shayari_gallery_capson));
            } else {
                txtTitle.setText(MyHelper.getString(R.string.saved_images).toUpperCase());
                txtDescription.setText("");
            }
            lstShayariImage.setAdapter(shayariImageAdapter);
        } else {
            if (getShayariImageWithSearch != null) {
                txtDescription.setText(getShayariImageWithSearch.getDescription());
                txtTitle.setText(MyHelper.getString(R.string.shayari_gallery_capson));
            } else {
                txtDescription.setText("");
                txtTitle.setText(MyHelper.getString(R.string.saved_images).toUpperCase());
            }
            shayariImageAdapter.notifyDataSetChanged();
        }
        updateLanguageSpecificUi();
    }

    private void updateLanguageSpecificUi() {
        edSearch.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        edSearch.setHint(MyHelper.getString(R.string.search_images));
        txtNoData.setText(MyHelper.getString(R.string.no_records_found));
        txtNoSavedImage.setText(MyHelper.getString(R.string.saved_image_will_be_show_here));
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        if (!TextUtils.isEmpty(getEditTextData(edSearch)))
            edSearch.setText("");
        else {
            getPoetLists("");
            if (!locallySaved) {
                shimmerViewContainer.startShimmer();
                shimmerViewContainer.setVisibility(View.VISIBLE);
            }
        }
        updateUI();
    }
    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }
}
