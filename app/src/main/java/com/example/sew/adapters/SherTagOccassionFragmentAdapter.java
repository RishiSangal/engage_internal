package com.example.sew.adapters;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.fragments.TagGhazalFragment;
import com.example.sew.fragments.TagNazmFragment;
import com.example.sew.fragments.TagShayriImagefrgment;
import com.example.sew.fragments.TagSherFragment;
import com.example.sew.models.ContentTypeTab;
import com.example.sew.models.CumulatedContentType;

import java.util.ArrayList;
import java.util.Locale;

public class SherTagOccassionFragmentAdapter extends FragmentStatePagerAdapter {

    private BaseActivity activity;
    private Enums.SHER_COLLECTION_TYPE sherCollectionType;
    private ArrayList<CumulatedContentType> cumulatedContentTypes;
    ContentTypeTab contentTypeTab;

    public SherTagOccassionFragmentAdapter(FragmentManager fm, BaseActivity activity, Enums.SHER_COLLECTION_TYPE sherCollectionTypes,
                                           ContentTypeTab contentTypeTab, ArrayList<CumulatedContentType> cumulatedContentTypes) {
        super(fm);
        this.activity = activity;
        this.sherCollectionType = sherCollectionTypes;
        this.cumulatedContentTypes = cumulatedContentTypes;
        this.contentTypeTab = contentTypeTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (cumulatedContentTypes.get(position).getListRenderingFormat()) {
            case GHAZAL:
                return TagGhazalFragment.getInstance(cumulatedContentTypes.get(position), contentTypeTab);
            case NAZM:
                return TagNazmFragment.getInstance(cumulatedContentTypes.get(position), contentTypeTab);
            case SHER:
            case QUOTE:
                return TagSherFragment.getInstance(cumulatedContentTypes.get(position), contentTypeTab);
            case IMAGE_SHAYRI:
                return TagShayriImagefrgment.getInstance(cumulatedContentTypes.get(position), sherCollectionType, contentTypeTab);
        }
        return TagGhazalFragment.getInstance(cumulatedContentTypes.get(position), contentTypeTab);
    }

    @Override
    public int getCount() {
        return cumulatedContentTypes.size();
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        int size = cumulatedContentTypes.get(position).getTotalContent();
        SpannableString spannable = new SpannableString(String.format(Locale.getDefault(), "%s %d", cumulatedContentTypes.get(position).getContentTypeName().toUpperCase(), size));
        increaseFontSizeForPath(spannable, " " + size, 0.6f);
        return spannable;
    }

    private void increaseFontSizeForPath(Spannable spannable, String path, float multiplier) {
        int startIndexOfPath = spannable.toString().indexOf(path);
        spannable.setSpan(new RelativeSizeSpan(multiplier), startIndexOfPath,
                startIndexOfPath + path.length(), 0);
    }
}
