package com.example.sew.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.SherTagOccassionFragmentAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetContentTypeTabByType;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.models.BaseModel;
import com.example.sew.models.BaseSherTag;
import com.example.sew.models.ContentTypeTab;
import com.example.sew.models.HomeImageTag;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.models.OccasionCollection;
import com.example.sew.models.SherCollection;
import com.example.sew.models.SherTag;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SherTagOccasionActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtSubTitle)
    TextView txtSubTitle;
    @BindView(R.id.imgBanner)
    ImageView imgBanner;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.layTitle)
    LinearLayout layTitle;
    String targetId, tagTitle;

    Enums.SHER_COLLECTION_TYPE sherCollectionType;
    SherCollection sherCollection;
    OccasionCollection occasionCollection;
    HomeSherCollection homeSherCollection;
    BaseSherTag sherTag;

    private static Intent getInstance(BaseActivity activity, String targetId, Enums.SHER_COLLECTION_TYPE sherCollectionType, BaseModel data, String title) {
        Intent intent = new Intent(activity, SherTagOccasionActivity.class);
        intent.putExtra(TARGET_ID, targetId);
        intent.putExtra(CONTENT_DATA_OBJ, data.getJsonObject().toString());
        intent.putExtra(SHER_COLLECTION_TYPE, sherCollectionType.toString());
        intent.putExtra(TAG_TITLE, title);
        return intent;
    }

    public static Intent getInstance(BaseActivity activity, SherCollection sherCollection) {
        return getInstance(activity, sherCollection.getSherCollectionId(), Enums.SHER_COLLECTION_TYPE.TOP_20, sherCollection, sherCollection.getTitle());
    }

    public static Intent getInstance(BaseActivity activity, HomeSherCollection homeSherCollection) {
        return getInstance(activity, homeSherCollection.getId(), homeSherCollection.getSherCollectionType(), homeSherCollection, homeSherCollection.getName());
    }

    public static Intent getInstance(BaseActivity activity, BaseSherTag sherTag) {
        return getInstance(activity, sherTag.getTagId(), Enums.SHER_COLLECTION_TYPE.TAG, sherTag, sherTag.getTagName());
    }

    public static Intent getInstance(BaseActivity activity, OccasionCollection occasionCollection) {
        return getInstance(activity, occasionCollection.getSherCollectionId(), Enums.SHER_COLLECTION_TYPE.OCCASIONS, occasionCollection, occasionCollection.getTitle());
    }

    ContentTypeTab contentTypeTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sher_tag_occassion);
        ButterKnife.bind(this);
        targetId = getIntent().getStringExtra(TARGET_ID);
        tagTitle = getIntent().getStringExtra(TAG_TITLE);
        sherCollectionType = Enum.valueOf(Enums.SHER_COLLECTION_TYPE.class, getIntent().getStringExtra(SHER_COLLECTION_TYPE));
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(CONTENT_DATA_OBJ));
            switch (sherCollectionType) {
                case TOP_20:
                    if (jsonObject.has("T"))
                        homeSherCollection = new HomeSherCollection(jsonObject);
                    else
                        sherCollection = new SherCollection(jsonObject);
                    break;
                case OCCASIONS:
                    if (jsonObject.has("T"))
                        homeSherCollection = new HomeSherCollection(jsonObject);
                    else
                        occasionCollection = new OccasionCollection(jsonObject);
                    break;
                case TAG:
                    if (jsonObject.has("TI"))
                        sherTag = new HomeImageTag(jsonObject);
                    else
                        sherTag = new SherTag(jsonObject);
                    break;
                case OTHER:
                    homeSherCollection = new HomeSherCollection(jsonObject);
                    break;
            }
        } catch (Exception e) {
            showToast(e.getLocalizedMessage());
            finish();
            return;
        }
        getContentTypeTab();
    }

    private void getContentTypeTab() {
        showDialog();
        GetContentTypeTabByType getContentTypeTabByType = new GetContentTypeTabByType();
        if (sherCollectionType == Enums.SHER_COLLECTION_TYPE.TAG)
            getContentTypeTabByType.setTargetType("1");
        else if (sherCollectionType == Enums.SHER_COLLECTION_TYPE.OCCASIONS)
            getContentTypeTabByType.setTargetType("2");
        else if (sherCollectionType == Enums.SHER_COLLECTION_TYPE.TOP_20)
            getContentTypeTabByType.setTargetType("3");
        getContentTypeTabByType.setTargetIdSlug(targetId).
                runAsync((BaseServiceable.OnApiFinishListener<GetContentTypeTabByType>) getCoupletListWithPaging -> {
                    dismissDialog();
                    if (getCoupletListWithPaging.isValidResponse()) {
                        contentTypeTab = getCoupletListWithPaging.getContentTypeTab();
                        updateUI();
                    } else {
                        showToast(getCoupletListWithPaging.getErrorMessage());
                    }
                });
    }

    private void updateUI() {
        updateLanguageSpecificContent();
        viewPager.setAdapter(new SherTagOccassionFragmentAdapter(getSupportFragmentManager(), this, sherCollectionType, contentTypeTab, contentTypeTab.getCumulatedContentType()));
        viewPager.setOffscreenPageLimit(Math.min(contentTypeTab.getCumulatedContentType().size(), 5));
        tabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
//        if (selectedCumulatedContentType != null && containsDefaultContentType()) {
//            viewpager.setCurrentItem(getContentTypePosition());
//            cumulatedContentTypes = null;
//        }
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        lazyRefreshTabPositioning(tabLayout, viewPager);
       // getContentTypeTab();
        updateLanguageSpecificContent();
    }

    private void updateLanguageSpecificContent() {
        if (contentTypeTab.isHaveBannerImage()) {
            imgBanner.setVisibility(View.VISIBLE);
            layTitle.setVisibility(View.GONE);
            ImageHelper.setImage(imgBanner, contentTypeTab.getImageFile(), null, Enums.PLACEHOLDER_TYPE.PROMOTIONAL_BANNER);
        } else {
            imgBanner.setVisibility(View.GONE);
            layTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(contentTypeTab.getTitleName().toUpperCase());
        }
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        //updateUI();
    }


}
