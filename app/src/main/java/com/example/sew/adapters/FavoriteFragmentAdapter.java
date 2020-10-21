package com.example.sew.adapters;


import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sew.activities.BaseActivity;
import com.example.sew.common.MyConstants;
import com.example.sew.fragments.FavoriteContentFragment;
import com.example.sew.fragments.FavoriteDictionaryFragment;
import com.example.sew.fragments.FavoritePoetFragment;
import com.example.sew.fragments.FavoriteShayariImageFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;
import com.example.sew.models.FavContentPageModel;
import com.example.sew.models.FavoriteDictionary;
import com.example.sew.models.FavoritePoet;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.ShayariImage;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;

import eu.inloop.pager.UpdatableFragmentPagerAdapter;

public class FavoriteFragmentAdapter extends UpdatableFragmentPagerAdapter {
    private BaseActivity activity;
    private TreeMap<ContentType, ArrayList<FavContentPageModel>> allFavoriteSections;
    private ArrayList<ShayariImage> allFavoriteShayariImage;
    private ArrayList<FavoriteDictionary> allFavoriteDictionary;
    private ArrayList<FavoritePoet> allFavoritePoet;

    public FavoriteFragmentAdapter(FragmentManager fm, BaseActivity activity, TreeMap<ContentType, ArrayList<FavContentPageModel>> allFavoriteSections,
                                   ArrayList<ShayariImage> allFavoriteShayariImage, ArrayList<FavoriteDictionary> allFavoriteDictionary, ArrayList<FavoritePoet> allFavoritePoet) {
        super(fm);
        this.activity = activity;
        this.allFavoriteSections = allFavoriteSections;
        this.allFavoriteShayariImage = allFavoriteShayariImage;
        this.allFavoriteDictionary = allFavoriteDictionary;
        this.allFavoritePoet = allFavoritePoet;
        initMapKeys();
    }

    private boolean isShowImageShayariPage() {
        return !CollectionUtils.isEmpty(allFavoriteShayariImage);
    }

    public boolean isShowFavoriteWordPage() {
        return !CollectionUtils.isEmpty(allFavoriteDictionary);
    }

    public boolean isShowFavoritePoetPage() {
        return !CollectionUtils.isEmpty(allFavoritePoet);
    }

    private ContentType[] mapKeys;

    @Override
    public void notifyDataSetChanged() {
        initMapKeys();
        super.notifyDataSetChanged();
    }

    private void initMapKeys() {
        int totalCount = allFavoriteSections.size();
        if (isShowImageShayariPage())
            totalCount += 1;
        if (isShowFavoriteWordPage())
            totalCount += 1;
        if (isShowFavoritePoetPage())
            totalCount += 1;
        mapKeys = new ContentType[totalCount];
        int pos = 0;
        for (ContentType key : allFavoriteSections.keySet()) {
            mapKeys[pos++] = key;
        }
        if (isShowImageShayariPage())
            mapKeys[pos++] = MyHelper.getContentById(MyConstants.FAV_IMAGE_SHAYARI_CONTENT_TYPE_ID);
        if (isShowFavoriteWordPage())
            mapKeys[pos++] = MyHelper.getContentById(MyConstants.FAV_WORD_CONTENT_TYPE_ID);
        if (isShowFavoritePoetPage())
            mapKeys[pos] = MyHelper.getContentById(MyConstants.FAV_POET_CONTENT_TYPE_ID);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (allFavoriteSections.size() <= position) {
            if (mapKeys[position].getContentId().contentEquals(MyConstants.FAV_IMAGE_SHAYARI_CONTENT_TYPE_ID))
                return FavoriteShayariImageFragment.getInstance();
            else if (mapKeys[position].getContentId().contentEquals(MyConstants.FAV_WORD_CONTENT_TYPE_ID))
                return FavoriteDictionaryFragment.getInstance();
            else
                return FavoritePoetFragment.getInstance();
        } else {
            return FavoriteContentFragment.getInstance(mapKeys[position]);
        }

    }

    @Override
    public int getItemPosition(Object object) {
//        FavoriteContentFragment fragment = (FavoriteContentFragment) object;
//        return isContentTypeExists(fragment.getContentType()) ? POSITION_UNCHANGED : POSITION_NONE;
        return POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        if (mapKeys.length >= (position + 1))
            return mapKeys[position].hashCode();
        return super.getItemId(position);
    }

    private boolean isContentTypeExists(ContentType contentType) {
        for (ContentType contentType1 : mapKeys) {
            if (contentType1.getContentId().contentEquals(contentType.getContentId()))
                return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        int totalCount = allFavoriteSections.size();
        if (isShowImageShayariPage())
            totalCount += 1;
        if (isShowFavoriteWordPage())
            totalCount += 1;
        if (isShowFavoritePoetPage())
            totalCount += 1;
        return totalCount;
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        int size;
        if (allFavoriteSections.size() <= position) {
            if (mapKeys[position].getContentId().contentEquals(MyConstants.FAV_IMAGE_SHAYARI_CONTENT_TYPE_ID))
                size = allFavoriteShayariImage.size();
            else if (mapKeys[position].getContentId().contentEquals(MyConstants.FAV_POET_CONTENT_TYPE_ID))
                size = allFavoritePoet.size();
            else
                size = allFavoriteDictionary.size();
        } else {
            ArrayList<FavContentPageModel> data = allFavoriteSections.get(mapKeys[position]);
            size = CollectionUtils.isEmpty(data) ? 0 : data.size();
        }
        SpannableString spannable = new SpannableString(String.format(Locale.getDefault(), "%s %d", mapKeys[position].getName(), size));
        increaseFontSizeForPath(spannable, " " + size, 0.6f);
        return spannable;
    }

    private void increaseFontSizeForPath(Spannable spannable, String path, float multiplier) {
        int startIndexOfPath = spannable.toString().indexOf(path);
        spannable.setSpan(new RelativeSizeSpan(multiplier), startIndexOfPath,
                startIndexOfPath + path.length(), 0);
    }

}
