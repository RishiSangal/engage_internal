package com.example.sew.models.home_view_holders;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.MyConstants;
import com.example.sew.common.Utils;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.ContentType;
import com.example.sew.models.HomeFeatured;
import com.example.sew.models.Para;
import com.example.sew.models.RenderContent;
import com.example.sew.models.WordContainer;
import com.example.sew.views.IconTextView;
import com.example.sew.views.TitleTextViewType6;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.zxing.client.result.VINParsedResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sew.models.HomeFeatured.DAY_TYPE_BIRTHDAY;
import static com.example.sew.models.HomeFeatured.DAY_TYPE_DEATH_ANNIVERSARY;

public class FeaturedPoetViewHolder extends BaseHomeViewHolder {

    @BindView(R.id.txtCollectionTitle)
    TextView txtCollectionTitle;
    @BindView(R.id.poetProfileImageTemplate)
    CircleImageView poetProfileImageTemplate;
    @BindView(R.id.txtFeaturedType)
    TextView txtFeaturedType;
    @BindView(R.id.txtPoetName)
    TextView txtPoetName;
    @BindView(R.id.txtPoetTenure)
    TextView txtPoetTenure;
    @BindView(R.id.txtPoetTenureDivider)
    TextView txtPoetTenureDivider;
    @BindView(R.id.txtPoetBirthPlace)
    TextView txtPoetBirthPlace;
    @BindView(R.id.txtPoetShortDescription)
    TextView txtPoetShortDescription;
    @BindView(R.id.layDivider)
    View layDivider;
    @BindView(R.id.laySher)
    LinearLayout laySher;
    @BindView(R.id.txtSeeFullGhazal)
    TextView txtSeeFullGhazal;
    @BindView(R.id.txtCalendarIcon)
    IconTextView txtCalendarIcon;
    @BindView(R.id.txtLocationIcon)
    IconTextView txtLocationIcon;
    @BindView(R.id.laySeeFullGhazal)
    View laySeeFullGhazal;

    @OnClick(R.id.laySeeFullGhazal)
    void onSeeFullGhazalClick() {
//        ContentType contentType = MyHelper.getContentBySlug(featured.getContentTypeSlug());
        getActivity().startActivity(RenderContentActivity.getInstance(getActivity(), featured.getParentSlug()));
    }

    @OnClick(R.id.txtPoetName)
    void onPoetClick() {
        if (featured != null)
            getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), featured.getPoetId()));
    }

    public static FeaturedPoetViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        FeaturedPoetViewHolder featuredPoetViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_feature_poet, baseActivity);
            featuredPoetViewHolder = new FeaturedPoetViewHolder(convertView, baseActivity);
        } else
            featuredPoetViewHolder = (FeaturedPoetViewHolder) convertView.getTag();
        featuredPoetViewHolder.setConvertView(convertView);
        convertView.setTag(featuredPoetViewHolder);
        return featuredPoetViewHolder;
    }

    private FeaturedPoetViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
    }

    private boolean isLoaded;
    private HomeFeatured featured;

    public BaseHomeViewHolder loadData(final HomeFeatured featured) {
        if (!isLoaded) {
            this.featured = featured;
            isLoaded = true;
            updateUI();
        }
        return this;
    }

    private void updateUI() {
        if (TextUtils.isEmpty(featured.getParentSlug()))
            laySeeFullGhazal.setVisibility(View.GONE);
        else
            laySeeFullGhazal.setVisibility(View.VISIBLE);
        txtCollectionTitle.setText(MyHelper.getString(R.string.featured_poet));
        txtSeeFullGhazal.setText(featured.getAnchorText());
        // txtSeeFullGhazal.setText(MyHelper.getString(R.string.see_full_ghazal));
        if(MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI){
            txtSeeFullGhazal.setTextSize(12);
            txtFeaturedType.setTextSize(12);
        }
        txtPoetName.setText(featured.getEntityName());
        txtPoetTenure.setText(featured.getPoetTenure());
        txtPoetBirthPlace.setText(featured.getDomicile());
        if (TextUtils.isEmpty(featured.getPoetTenure())) {
            txtPoetTenure.setVisibility(View.GONE);
            txtCalendarIcon.setVisibility(View.GONE);
        } else {
            txtPoetTenure.setVisibility(View.VISIBLE);
            txtCalendarIcon.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(featured.getDomicile())) {
            txtPoetBirthPlace.setVisibility(View.GONE);
            txtLocationIcon.setVisibility(View.GONE);
        } else {
            txtPoetBirthPlace.setVisibility(View.VISIBLE);
            txtLocationIcon.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(featured.getDomicile()) || TextUtils.isEmpty(featured.getPoetTenure()))
            txtPoetTenureDivider.setVisibility(View.GONE);
        else
            txtPoetTenureDivider.setVisibility(View.VISIBLE);


        ImageHelper.setImage(poetProfileImageTemplate, featured.getImageUrl());
        if (CollectionUtils.isEmpty(featured.getTitle())) {
            laySher.setVisibility(View.GONE);
            layDivider.setVisibility(View.GONE);
        } else {
            laySher.setVisibility(View.VISIBLE);
            layDivider.setVisibility(View.VISIBLE);
            RenderHelper.RenderContentBuilder.Builder(getActivity())
                    .setTextAlignment(Enums.TEXT_ALIGNMENT.CENTER)
                    .setParas(featured.getTitle())
                    .setLeftRightPadding((int) Utils.pxFromDp(32))
                    .setLayParaContainer(laySher)
                    .setOnWordLongClick(onWordLongClick)
                    .setOnWordClick(onWordClickListener)
                    .Build();
        }
        if(TextUtils.isEmpty(featured.getRemarks())){
            txtPoetShortDescription.setVisibility(View.GONE);
            layDivider.setVisibility(View.GONE);
        }else {
            txtPoetShortDescription.setText(featured.getRemarks());
            layDivider.setVisibility(View.VISIBLE);
        }
        txtFeaturedType.setVisibility(View.VISIBLE);
        switch (featured.getDayType()) {
            case DAY_TYPE_BIRTHDAY:
                txtFeaturedType.setText(String.format("%s: %s", MyHelper.getString(R.string.remembering), MyHelper.getString(R.string.birth__anniversary)));
                break;
            case DAY_TYPE_DEATH_ANNIVERSARY:
                txtFeaturedType.setText(String.format("%s: %s", MyHelper.getString(R.string.remembering), MyHelper.getString(R.string.death_anniversary)));
                break;
            default:
                txtFeaturedType.setVisibility(View.GONE);
                break;
        }
    }
    private View.OnLongClickListener onWordLongClick = v -> {
        Para para = (Para) v.getTag(R.id.tag_para);
        if (para == null)
            return false;
        String shareContentText=MyHelper.getSherContentText(para);
        MyHelper.shareTheText(shareContentText, getActivity());
        MyHelper.copyToClipBoard(shareContentText,getActivity());
        return false;
    };
    private View.OnClickListener onWordClickListener = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
//        MeaningBottomSheetFragment.getInstance(wordContainer.getWord(), wordContainer.getMeaning()).show(getActivity().getSupportFragmentManager(), "MEANING");
        new MeaningBottomPopupWindow(getActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();
    };
}
