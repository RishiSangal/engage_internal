package com.example.sew.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.ProseShayariCollectionFragmentAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetContentTypeTabByCollectionType;
import com.example.sew.common.Enums;
import com.example.sew.models.ContentType;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProseShayariActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static Intent getInstance(Activity activity, Enums.COLLECTION_TYPE collectionType) {
        Intent intent = new Intent(activity, ProseShayariActivity.class);
        intent.putExtra(COLLECTION_TYPE, collectionType.toString());
        return intent;
    }

    public static Intent getInstance(Activity activity, Enums.COLLECTION_TYPE collectionType, ContentType contentType) {
        Intent intent = new Intent(activity, ProseShayariActivity.class);
        intent.putExtra(COLLECTION_TYPE, collectionType.toString());
        if (contentType != null)
            intent.putExtra(INITIAL_TAB, String.valueOf(contentType.getJsonObject()));
        return intent;
    }

    Enums.COLLECTION_TYPE collectionType;
    ArrayList<ContentType> contentTypes;
    ContentType selectedContentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prose_shayari);
        ButterKnife.bind(this);
        collectionType = Enums.COLLECTION_TYPE.valueOf(getIntent().getStringExtra(COLLECTION_TYPE));
        if (getIntent().hasExtra(INITIAL_TAB)) {
            try {
                selectedContentType = new ContentType(new JSONObject(getIntent().getStringExtra(INITIAL_TAB)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getCollectionTabs();
    }

    private void getCollectionTabs() {
        //showDialog();
        new GetContentTypeTabByCollectionType()
                .setCollectionType(collectionType)
                .runAsync((BaseServiceable.OnApiFinishListener<GetContentTypeTabByCollectionType>) getContentTypeTabByCollectionType -> {
                   // dismissDialog();
                    if (getContentTypeTabByCollectionType.isValidResponse()) {
                        contentTypes = getContentTypeTabByCollectionType.getContentTypes();
                        updateUI();
                    } else {
                        showToast(getContentTypeTabByCollectionType.getErrorMessage());
                        finish();
                    }
                });
    }


    private void updateUI() {
        viewpager.setAdapter(new ProseShayariCollectionFragmentAdapter(getSupportFragmentManager(), this,
                collectionType, contentTypes));
        viewpager.setOffscreenPageLimit(Math.min(contentTypes.size(), 5));
        tabLayout.setViewPager(viewpager);
        if (selectedContentType != null && containsDefaultContentType()) {
            viewpager.setCurrentItem(getContentTypePosition());
            selectedContentType = null;
        }
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        lazyRefreshTabPositioning(tabLayout, viewpager);
    }

    private boolean containsDefaultContentType() {
        if (selectedContentType == null)
            return false;
        for (ContentType contentType : contentTypes) {
            if (contentType.getContentId().contentEquals(selectedContentType.getContentId()))
                return true;
        }
        return false;
    }

    private int getContentTypePosition() {
        if (selectedContentType == null)
            return 0;
        int index = 0;
        for (ContentType contentType : contentTypes) {
            if (contentType.getContentId().contentEquals(selectedContentType.getContentId()))
                return index;
            ++index;
        }
        return index;
    }
}
