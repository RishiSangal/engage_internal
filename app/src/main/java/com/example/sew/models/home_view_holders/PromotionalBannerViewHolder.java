package com.example.sew.models.home_view_holders;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.models.HomePromotionalBanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PromotionalBannerViewHolder extends BaseHomeViewHolder {
    private HomePromotionalBanner promotionalBanner;
    @BindView(R.id.imgPromotionImage)
    ImageView imgPromotionImage;

    @OnClick(R.id.imgPromotionImage)
    void onImageShayariClick() {
        if (promotionalBanner == null)
            return;
        try {
            String url = promotionalBanner.getTargetUrl();
            Intent in = new Intent(Intent.ACTION_VIEW);
            in.setData(Uri.parse(url));
            getActivity().startActivity(in);
        } catch (Exception ignored) {
        }
    }

    public static PromotionalBannerViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        PromotionalBannerViewHolder imageShayariViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_promotional_banner, baseActivity);
            imageShayariViewHolder = new PromotionalBannerViewHolder(convertView, baseActivity);
        } else
            imageShayariViewHolder = (PromotionalBannerViewHolder) convertView.getTag();
        convertView.setTag(imageShayariViewHolder);
        imageShayariViewHolder.setConvertView(convertView);
        return imageShayariViewHolder;
    }

    private PromotionalBannerViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
    }

    public BaseHomeViewHolder loadData(HomePromotionalBanner promotionalBanner) {
        this.promotionalBanner = promotionalBanner;
        ImageHelper.setImage(imgPromotionImage, promotionalBanner.getImgaUrl(), Enums.PLACEHOLDER_TYPE.PROMOTIONAL_BANNER);
        return this;
    }
}
