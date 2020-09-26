package com.example.sew.adapters;

import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ShayariContent;


/**
 * Created by raman.kumar on 1/3/17.
 */

public abstract class BaseMyAdapter extends BaseAdapter implements ICommonValues {
    private BaseActivity activity;

    public BaseActivity getActivity() {
        return this.activity;
    }

    BaseMyAdapter(BaseActivity activity) {
        this.activity = activity;
    }

    public String getString(int resId) {
        return getActivity().getString(resId);
    }

    public int getColor(int colorId) {
        return getActivity().getResources().getColor(colorId);
    }

    final View getInflatedView(int layoutResId) {
        View convertView = getActivity().getLayoutInflater().inflate(layoutResId, null);
        convertView.setTag(R.id.tag_language, MyService.getSelectedLanguage());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    boolean isLayoutDirectionMismatched(View convertView) {
        return convertView == null || convertView.getTag(R.id.tag_language) != MyService.getSelectedLanguage();
//        if (convertView == null)
//            return false;
//        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
//            return convertView.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR;
//        else
//            return convertView.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;

    }

    final void updateFavoriteIcon(ImageView imgFavIcon, String contentId) {
        if (getActivity() == null || imgFavIcon == null || TextUtils.isEmpty(contentId))
            return;
        getActivity().updateFavoriteIcon(imgFavIcon, contentId);
    }

    final void addFavoriteClick(ImageView imgFavIcon, String contentId, String contentTypeId, String favType) {
        if (getActivity() == null || imgFavIcon == null || TextUtils.isEmpty(contentId))
            return;
        getActivity().addFavoriteClick(imgFavIcon, contentId, contentTypeId, favType, null);
    }

    final void addFavoriteClick(ImageView imgFavIcon, String contentId, String favType) {
        addFavoriteClick(imgFavIcon, contentId, null, favType);
    }
}

