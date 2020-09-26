package com.example.sew.models.home_view_holders;

import android.view.View;
import android.widget.LinearLayout;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomViewHolder extends BaseHomeViewHolder {
    @BindView(R.id.bottomLine)
    View bottomLine;
    @BindView(R.id.bottom_view)
    LinearLayout bottomView;

    public static BottomViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        BottomViewHolder bottomViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_list_bottom_view, baseActivity);
            bottomViewHolder = new BottomViewHolder(convertView, baseActivity);
        } else
            bottomViewHolder = (BottomViewHolder) convertView.getTag();
        bottomViewHolder.setConvertView(convertView);
        convertView.setTag(bottomViewHolder);
        return bottomViewHolder;
    }

    private BottomViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
    }
}
