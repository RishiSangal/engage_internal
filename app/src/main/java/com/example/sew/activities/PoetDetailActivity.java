package com.example.sew.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.PoetDetailFragmentAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetPoetProfile;
import com.example.sew.common.CollapsingImageLayout;
import com.example.sew.common.Enums;
import com.example.sew.helpers.AudioPlayerControls;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;
import com.example.sew.models.PoetCompleteProfile;
import com.google.android.material.appbar.AppBarLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PoetDetailActivity extends BaseActivity {
    //    implements PoetAudioInterFace
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.poetProfileImageTemplate)
    CircleImageView poetProfileImageTemplate;
    @BindView(R.id.poetNameTemplateTV)
    TextView poetNameTemplateTV;
    @BindView(R.id.poetTenureTemplateTV)
    TextView poetTenureTemplateTV;
    @BindView(R.id.poetTenureTemplateDivider)
    TextView poetTenureTemplateDivider;
    @BindView(R.id.poetBirthPlaceTemplate)
    TextView poetBirthPlaceTemplate;
    @BindView(R.id.poetDescriptionTemplate)
    TextView poetDescriptionTemplate;
    @BindView(R.id.imgFavorite)
    ImageView imgFavorite;
    @BindView(R.id.imgShareUrl)
    ImageView imgShareUrl;
    @BindView(R.id.poetHeader)
    LinearLayout poetHeader;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.CollapseImageLayout)
    CollapsingImageLayout CollapseImageLayout;
    @BindView(R.id.searchActivity_mainView)
    RelativeLayout searchActivity_mainView;
    public AudioPlayerControls audioPlayerControls;

    public static Intent getInstance(Activity activity, String poetId) {
        Intent intent = new Intent(activity, PoetDetailActivity.class);
        intent.putExtra(POET_ID, poetId);
        return intent;
    }

    public static Intent getInstance(Activity activity, String poetId, ContentType contentType) {
        Intent intent = new Intent(activity, PoetDetailActivity.class);
        intent.putExtra(POET_ID, poetId);
        if (contentType != null)
            intent.putExtra(INITIAL_TAB, String.valueOf(contentType.getJsonObject()));
        return intent;
    }

    String poetId;
    PoetCompleteProfile poetCompleteProfile;
    ContentType selectedContentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poet_detail);
        ButterKnife.bind(this);
        poetId = getIntent().getStringExtra(POET_ID);
        if (getIntent().hasExtra(INITIAL_TAB)) {
            try {
                selectedContentType = new ContentType(new JSONObject(getIntent().getStringExtra(INITIAL_TAB)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        audioPlayerControls = new AudioPlayerControls(PoetDetailActivity.this, searchActivity_mainView);
//        audioPlayerControls.setOnAudioPlayerStateChanged(this);
        getPoetDetail();
    }

    private void getPoetDetail() {
        showDialog();
        new GetPoetProfile()
                .setPoetId(poetId)
                .runAsync((BaseServiceable.OnApiFinishListener<GetPoetProfile>) getPoetProfile -> {
                    dismissDialog();
                    if (getPoetProfile.isValidResponse()) {
                        poetCompleteProfile = getPoetProfile.getPoetCompleteProfile();
                        updateUI();
                    } else {
                        showToast(getPoetProfile.getErrorMessage());
                        finish();
                    }
                });
    }

    ArrayList<ContentType> poetProfilesTabs = new ArrayList<>();

    private void updateUI() {
        poetProfilesTabs.clear();
        boolean isEnglishAvailable = poetCompleteProfile.getPoetDetail().isEngDescriptionAvailable();
        boolean isHindiAvailable = poetCompleteProfile.getPoetDetail().isEngDescriptionAvailable();
        boolean isUrduAvailable = poetCompleteProfile.getPoetDetail().isEngDescriptionAvailable();

        poetNameTemplateTV.setText(poetCompleteProfile.getPoetDetail().getPoetName().toUpperCase());
        poetBirthPlaceTemplate.setText(poetCompleteProfile.getPoetDetail().getDomicile());
        poetDescriptionTemplate.setText(poetCompleteProfile.getPoetDetail().getShortDescription());
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
            poetDescriptionTemplate.setTextSize(16);
        }
        ImageHelper.setImage(poetProfileImageTemplate, poetCompleteProfile.getPoetDetail().getPoetImage());
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
            String[] split = poetCompleteProfile.getPoetDetail().getPoetTenure().split(" ");
            String result = "";
            for (int i = split.length - 1; i >= 0; i--) {
                result += (split[i] + " ");
            }
            poetTenureTemplateTV.setText(result.trim());
        } else
            poetTenureTemplateTV.setText(poetCompleteProfile.getPoetDetail().getPoetTenure());
        if (TextUtils.isEmpty(poetCompleteProfile.getPoetDetail().getDomicile())) {
            poetTenureTemplateDivider.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(poetCompleteProfile.getPoetDetail().getPoetTenure())) {
            poetTenureTemplateDivider.setVisibility(View.GONE);
        }
        addFavoriteClick(imgFavorite, poetCompleteProfile.getPoetDetail().getPoetId(), Enums.FAV_TYPES.ENTITY.getKey());
        updateFavoriteIcon(imgFavorite, poetCompleteProfile.getPoetDetail().getPoetId());

        if (isEnglishAvailable || isHindiAvailable || isUrduAvailable)
            poetProfilesTabs.add(MyHelper.getDummyContentTypeProfile());
        ArrayList<ContentType> saveContentType = MyService.getAllContentType();
        for (int i = 0; i < poetCompleteProfile.getPoetDetail().getContentTypes().size(); i++) {
            ContentType localContentType = poetCompleteProfile.getPoetDetail().getContentTypes().get(i);
            for (int j = 0; j < saveContentType.size(); j++) {
                ContentType contentTypeSave = saveContentType.get(j);
                if (contentTypeSave.getContentId().equalsIgnoreCase(localContentType.getContentId())) {
                    poetProfilesTabs.add(contentTypeSave);
                }
            }

        }

        // poetProfilesTabs.addAll(poetCompleteProfile.getPoetDetail().getContentTypes());
        if (MyHelper.convertToInt(poetCompleteProfile.getPoetDetail().getImageShayriCount()) > 0)
            poetProfilesTabs.add(MyHelper.getDummyContentTypeImageShayari());
        if (MyHelper.convertToInt(poetCompleteProfile.getPoetDetail().getAudioCount()) > 0)
            poetProfilesTabs.add(MyHelper.getDummyContentTypeAudio());
        if (MyHelper.convertToInt(poetCompleteProfile.getPoetDetail().getVideoCount()) > 0)
            poetProfilesTabs.add(MyHelper.getDummyContentTypeVideo());
        viewpager.setAdapter(new PoetDetailFragmentAdapter(getSupportFragmentManager(), this, poetCompleteProfile.getPoetDetail(), poetProfilesTabs));
        viewpager.setOffscreenPageLimit(Math.min(poetProfilesTabs.size(), 5));
        tabLayout.setViewPager(viewpager);
        if (selectedContentType != null && containsDefaultContentType()) {
            viewpager.setCurrentItem(getContentTypePosition());
            selectedContentType = null;
        } else if (selectedContentType != null && selectedContentType.getName().equalsIgnoreCase(MyHelper.getDummyContentTypeProfile().getName()))
            viewpager.setCurrentItem(Math.min(0, poetProfilesTabs.size()));
        else {
            if (poetProfilesTabs.size() > 0)
                if (poetProfilesTabs.get(0).getName().equalsIgnoreCase(MyHelper.getDummyContentTypeProfile().getName()))
                    viewpager.setCurrentItem(Math.min(1, poetProfilesTabs.size()));
                else
                    viewpager.setCurrentItem(Math.min(0, poetProfilesTabs.size()));
        }
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (poetProfilesTabs.get(position).getName().equalsIgnoreCase(MyHelper.getDummyContentTypeProfile().getName())) {
                    poetHeader.setVisibility(View.GONE);
                    CollapseImageLayout.getLayoutParams().height = 0;
                    //  CollapseImageLayout.setVisibility(View.GONE);
                } else {
                    poetHeader.setVisibility(View.VISIBLE);
                    CollapseImageLayout.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    CollapseImageLayout.requestLayout();
                    // CollapseImageLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (poetProfilesTabs.get(position).getName().equalsIgnoreCase(MyHelper.getDummyContentTypeProfile().getName())) {
                    poetHeader.setVisibility(View.GONE);
                    CollapseImageLayout.getLayoutParams().height = 0;
                } else {
                    poetHeader.setVisibility(View.VISIBLE);
                    CollapseImageLayout.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    CollapseImageLayout.requestLayout();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        lazyRefreshTabPositioning(tabLayout, viewpager);
        poetNameTemplateTV.setText(poetCompleteProfile.getPoetDetail().getPoetName().toUpperCase());
        poetBirthPlaceTemplate.setText(poetCompleteProfile.getPoetDetail().getDomicile());
        poetDescriptionTemplate.setText(poetCompleteProfile.getPoetDetail().getShortDescription());
    }

    @OnClick(R.id.imgShareUrl)
    public void onImgShareUrlClick() {
        if (poetCompleteProfile != null)
            MyHelper.shareContent(poetCompleteProfile.getPoetDetail().getPoetName() + "\n" + poetCompleteProfile.getPoetDetail().getShortUrl());
    }

    private boolean containsDefaultContentType() {
        if (selectedContentType == null)
            return false;
        for (ContentType contentType : poetCompleteProfile.getPoetDetail().getContentTypes()) {
            if (contentType.getContentId().contentEquals(selectedContentType.getContentId()))
                return true;
        }
        return false;
    }

    private int getContentTypePosition() {
        if (selectedContentType == null)
            return 0;
        int index = 0;
        for (ContentType contentType : poetProfilesTabs) {
            if (contentType.getContentId().contentEquals(selectedContentType.getContentId()))
                return index;
            ++index;
        }
        return index;
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        if (poetCompleteProfile != null)
            updateFavoriteIcon(imgFavorite, poetCompleteProfile.getPoetDetail().getPoetId());
    }

//    private ArrayList<AudioContent> audioContents = new ArrayList<>();

//    @Override
//    public void onPoetAudioPlay(int i, AudioContent audioContent) {
//        audioPlayerControls.playAudio(audioContents.indexOf(audioContent));
//    }

//    @Override
//    public void onAudioPause() {
//
//    }
//
//    @Override
//    public void onAudioStart() {
//
//    }
//
//    @Override
//    public void onAudioWindowClose() {
//
//    }
//
//    @Override
//    public void onAudioSelected(BaseAudioContent audioContent) {
//        if (poetAudioAdapter != null && audioContent instanceof AudioContent)
//            poetAudioAdapter.setSelectedAudioContent((AudioContent) audioContent);
//    }
}
