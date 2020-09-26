package com.example.sew.models.home_view_holders;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.MyService;

public class BaseHomeViewHolder implements ICommonValues {
   public BaseActivity activity;
    View convertView;

    final static View getInflatedView(int layoutResId, BaseActivity baseActivity) {
        View convertView = baseActivity.getLayoutInflater().inflate(layoutResId, null);
        convertView.setTag(R.id.tag_language, MyService.getSelectedLanguage());
        return convertView;
    }

    @NonNull
    public View getConvertView() {
        return convertView;
    }

    public void setConvertView(View convertView) {
        this.convertView = convertView;
    }

    public BaseActivity getActivity() {
        return activity;
    }

    public BaseHomeViewHolder(BaseActivity baseActivity) {
        this.activity = baseActivity;
    }
}
