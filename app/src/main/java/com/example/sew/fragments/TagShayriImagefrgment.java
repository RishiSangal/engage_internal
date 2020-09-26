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
import androidx.core.view.ViewCompat;

import com.example.sew.R;
import com.example.sew.activities.ShayariImageDetailActivity;
import com.example.sew.adapters.ShayariImageAdapter;
import com.example.sew.adapters.TagShayriImageAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetShayariImageWithSearch;
import com.example.sew.common.Enums;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentTypeTab;
import com.example.sew.models.CumulatedContentType;
import com.example.sew.models.ShayariImage;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class TagShayriImagefrgment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstShayariImage)
    PagingListView lstShayariImage;

    @BindView(R.id.txtNoData)
    TextView txtNoData;
    private CumulatedContentType contentType;
    private Enums.SHER_COLLECTION_TYPE sherCollectionType;
    private ContentTypeTab contentTypeTab;
    @OnItemClick(R.id.lstShayariImage)
    void onShayariClick(View convertView) {
        ShayariImage shayariImage = (ShayariImage) convertView.getTag(R.id.tag_data);
        startActivity(ShayariImageDetailActivity.getInstance(GetActivity(), shayariImages, shayariImage));
    }



    public static TagShayriImagefrgment getInstance(CumulatedContentType contentType, Enums.SHER_COLLECTION_TYPE collectionTYpe, ContentTypeTab contentTypeTab) {
        TagShayriImagefrgment tagShayriImagefrgment = new TagShayriImagefrgment();
        Bundle bundle = new Bundle();
        bundle.putString(CUMULATED_CONTENT_TYPE_OBJ, contentType.getJsonObject().toString());
        bundle.putString(SHER_COLLECTION_TYPE,collectionTYpe.toString());
        bundle.putString(CONTENT_TYPE_TAB, contentTypeTab.getJsonObject().toString());
        tagShayriImagefrgment.setArguments(bundle);
        return tagShayriImagefrgment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_shayari_image, container, false);
        ButterKnife.bind(this, view);
        try {
            contentType= new CumulatedContentType(new JSONObject(getArguments().getString(CUMULATED_CONTENT_TYPE_OBJ)));
            sherCollectionType = Enum.valueOf(Enums.SHER_COLLECTION_TYPE.class, getArguments().getString(SHER_COLLECTION_TYPE));
            contentTypeTab = new ContentTypeTab((new JSONObject(getArguments().getString(CONTENT_TYPE_TAB))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private TagShayriImageAdapter tagShayriImageAdapter;
    private ArrayList<ShayariImage> shayariImages = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstShayariImage.setPagingableListener(() -> {
            if (getShayariImageWithSearch != null) {
                getShayariImageWithSearch.loadMoreData();
                lstShayariImage.setIsLoading(true);
            }
        });
        getShayriImage();
        updateLanguageSpecificUi();
        ViewCompat.setNestedScrollingEnabled(lstShayariImage, true);
    }

    private GetShayariImageWithSearch getShayariImageWithSearch;

    private void getShayriImage() {

        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        lstShayariImage.setIsLoading(true);
        getShayariImageWithSearch = new GetShayariImageWithSearch();
        getShayariImageWithSearch.setTargetIdSlug(contentTypeTab.getTargetId());
        if(sherCollectionType== Enums.SHER_COLLECTION_TYPE.TAG)
            getShayariImageWithSearch.setTargetType("1");
        else if(sherCollectionType== Enums.SHER_COLLECTION_TYPE.OCCASIONS)
            getShayariImageWithSearch.setTargetType("2");
        else
            getShayariImageWithSearch.setTargetType("0");
        getShayariImageWithSearch.addPagination()
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
                        if (CollectionUtils.isEmpty(shayariImages)) {
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

    private void updateUI() {
        if (tagShayriImageAdapter == null) {
            tagShayriImageAdapter = new TagShayriImageAdapter(GetActivity(), shayariImages);
            lstShayariImage.setAdapter(tagShayriImageAdapter);
        } else
            tagShayriImageAdapter.notifyDataSetChanged();

        updateLanguageSpecificUi();
    }

    private void updateLanguageSpecificUi() {
        txtNoData.setText(MyHelper.getString(R.string.no_records_found));
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
    }
    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }
}
