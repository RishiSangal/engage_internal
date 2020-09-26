package com.example.sew.models.home_view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetsActivity;
import com.example.sew.activities.ProseShayariActivity;
import com.example.sew.activities.ShayariActivity;
import com.example.sew.activities.ShayariImagesActivity;
import com.example.sew.adapters.BaseViewHolder;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeeMoreViewHolder extends BaseViewHolder implements ICommonValues {
    @BindView(R.id.imgSeeMore)
    ImageView imgSeeMore;
    private int seeMoreItemType;

    @OnClick()
    void onItemClick(View view) {
        if (!(view.getContext() instanceof BaseActivity))
            return;
        BaseActivity baseActivity = (BaseActivity) view.getContext();
        switch (seeMoreItemType) {
            case SEE_MORE_PROSE:
                baseActivity.startActivity(ProseShayariActivity.getInstance(baseActivity, Enums.COLLECTION_TYPE.PROSE));
                break;
            case SEE_MORE_POETS:
                baseActivity.startActivity(PoetsActivity.getInstance(baseActivity, PoetsActivity.TAB_FAMOUS));
                break;
            case SEE_MORE_SHER:
                baseActivity.startActivity(ShayariActivity.getInstance(baseActivity));
                break;
            case SEE_MORE_SHAYARI:
                baseActivity.startActivity(ProseShayariActivity.getInstance(baseActivity, Enums.COLLECTION_TYPE.SHAYARI));
                break;
            case SEE_MORE_IMAGE:
                baseActivity.startActivity(ShayariImagesActivity.getInstance(baseActivity));
                break;
        }

    }

    public SeeMoreViewHolder(View view, int seeMoreItemType) {
        super(view);
        this.seeMoreItemType = seeMoreItemType;
        ButterKnife.bind(this, view);
        if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH)
            imgSeeMore.setImageResource(R.drawable.see_more_en);
        else if(MyService.getSelectedLanguage()== Enums.LANGUAGE.HINDI)
            imgSeeMore.setImageResource(R.drawable.see_more_hi);
        else if(MyService.getSelectedLanguage()== Enums.LANGUAGE.URDU)
            imgSeeMore.setImageResource(R.drawable.see_more_ur);
        //imgSeeMore.setText(MyHelper.getString(R.string.see_more));
    }
}