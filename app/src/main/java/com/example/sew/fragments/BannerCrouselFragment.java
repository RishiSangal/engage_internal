package com.example.sew.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.ProseShayariActivity;
import com.example.sew.activities.ShayariActivity;
import com.example.sew.activities.SherCollectionActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.HomeBannerCollection;
import com.example.sew.models.HomeImageTag;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.models.SherTag;
import com.example.sew.views.TitleTextViewType7;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BannerCrouselFragment extends BaseFragment {
    private HomeBannerCollection bannerCollection;
    @BindView(R.id.imgBannerImage)
    ImageView imgBannerImage;

    @BindView(R.id.txtHeaderTitle)
    TextView txtHeaderTitle;
    @BindView(R.id.txtHeaderSubTitle)
    TextView txtHeaderSubTitle;

    @OnClick(R.id.imgBannerImage)
    void onBannerClick() {
        switch (bannerCollection.getBannerType()) {
            case HomeBannerCollection.BANNERTYPE_T20:
                HomeSherCollection homeSherCollection = MyHelper.getDummyT20SherCollection(bannerCollection.getTargetId(), bannerCollection.getBannerName());
                startActivity(SherCollectionActivity.getInstance(GetActivity(), homeSherCollection));
               // startActivity(SherTagOccasionActivity.getInstance(GetActivity(), homeSherCollection));
                break;
            case HomeBannerCollection.BANNERTYPE_ENTITY:
                startActivity(PoetDetailActivity.getInstance(getActivity(), bannerCollection.getTargetId()));
                break;
            case HomeBannerCollection.BANNERTYPE_TAGS:
                GetActivity().startActivity(SherTagOccasionActivity.getInstance(GetActivity(), HomeImageTag.getInstance(bannerCollection.getTargetId())));
                break;
            case HomeBannerCollection.BANNERTYPE_OCCASION:
                HomeSherCollection homeOccasionCollection = MyHelper.getDummyOccasionCollection(bannerCollection.getTargetId(), bannerCollection.getBannerName());
                startActivity(SherTagOccasionActivity.getInstance(GetActivity(), homeOccasionCollection));
                break;
            case HomeBannerCollection.BANNERTYPE_SHAYARI:
                startActivity(ProseShayariActivity.getInstance(getActivity(), Enums.COLLECTION_TYPE.SHAYARI));
                break;
            case HomeBannerCollection.BANNERTYPE_PROSE:
                startActivity(ProseShayariActivity.getInstance(getActivity(), Enums.COLLECTION_TYPE.PROSE));
                break;
            case HomeBannerCollection.BANNERTYPE_IMAGESHAYARI:
                break;

        }
    }

    public static BannerCrouselFragment getInstance(HomeBannerCollection bannerCollection) {
        BannerCrouselFragment bannerCrouselFragment = new BannerCrouselFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BANNER_CROUSEL_OBJ, bannerCollection.getJsonObject().toString());
        bannerCrouselFragment.setArguments(bundle);
        return bannerCrouselFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cell_home_banner_crousel_item, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(BANNER_CROUSEL_OBJ)) {
            try {
                bannerCollection = new HomeBannerCollection(new JSONObject(getArguments().getString(BANNER_CROUSEL_OBJ, "{}")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ImageHelper.setImage(imgBannerImage, bannerCollection.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE);
        txtHeaderTitle.setVisibility(TextUtils.isEmpty(bannerCollection.getBannerName()) ? View.GONE : View.VISIBLE);
        txtHeaderSubTitle.setVisibility(TextUtils.isEmpty(bannerCollection.getByLine()) ? View.INVISIBLE : View.VISIBLE);
        txtHeaderSubTitle.setText(bannerCollection.getByLine());
        txtHeaderTitle.setText(bannerCollection.getBannerName());
    }

}
