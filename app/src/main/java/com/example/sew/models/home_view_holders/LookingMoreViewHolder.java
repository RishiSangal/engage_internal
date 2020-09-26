package com.example.sew.models.home_view_holders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.HomeLookingMore;
import com.example.sew.views.TitleTextViewType11;
import com.example.sew.views.TitleTextViewType6;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LookingMoreViewHolder extends BaseHomeViewHolder {


    @BindView(R.id.txtLookingMoreTitle)
    TitleTextViewType11 txtLookingMoreTitle;
    @BindView(R.id.txtLookingMoreSubTitle)
    TitleTextViewType6 txtLookingMoreSubTitle;
    @BindView(R.id.layLookingMoreTitleHeader)
    LinearLayout layLookingMoreTitleHeader;
    @BindView(R.id.offlineFavIcon)
    ImageView offlineFavIcon;
    @BindView(R.id.txtNazamTitle)
    TitleTextViewType6 txtNazamTitle;
    @BindView(R.id.txtNazamSubTitle)
    TitleTextViewType6 txtNazamSubTitle;
    @BindView(R.id.txtAuthor)
    TitleTextViewType6 txtAuthor;
    @BindView(R.id.poetEditorChoiceIcon)
    ImageView poetEditorChoiceIcon;
    @BindView(R.id.poetPopularChoiceIcon)
    ImageView poetPopularChoiceIcon;
    @BindView(R.id.poetaudioLink)
    ImageView poetaudioLink;
    @BindView(R.id.poetyoutubeLink)
    ImageView poetyoutubeLink;
    @BindView(R.id.poetList_layout)
    LinearLayout poetListLayout;
    View convertView;

    public static LookingMoreViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        LookingMoreViewHolder videoViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_looking_more, baseActivity);
            videoViewHolder = new LookingMoreViewHolder(convertView, baseActivity);
        } else
            videoViewHolder = (LookingMoreViewHolder) convertView.getTag();
        videoViewHolder.setConvertView(convertView);
        convertView.setTag(videoViewHolder);
        videoViewHolder.convertView = convertView;
        return videoViewHolder;
    }

    private LookingMoreViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
        getActivity().registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUI();
            }
        }, BROADCAST_FAVORITE_UPDATED);
    }

    private HomeLookingMore lookingMore;
    private boolean showTitleHeader;

    public BaseHomeViewHolder loadData(HomeLookingMore lookingMore, boolean showTitleHeader) {
        this.lookingMore = lookingMore;
        this.showTitleHeader = showTitleHeader;
        convertView.setTag(R.id.tag_data, lookingMore);
        updateUI();
        return this;
    }

    private void updateUI() {
        txtLookingMoreTitle.setText(MyHelper.getString(R.string.looking_for_more));
        txtLookingMoreSubTitle.setText(MyHelper.getString(R.string.few_things_to_read));
        layLookingMoreTitleHeader.setVisibility(showTitleHeader ? View.VISIBLE : View.GONE);
        offlineFavIcon.setTag(R.id.tag_data, lookingMore);
        poetaudioLink.setVisibility(MyHelper.convertToInt(lookingMore.getAudioCount()) > 0 ? View.VISIBLE : View.GONE);
        poetyoutubeLink.setVisibility(MyHelper.convertToInt(lookingMore.getVideoCount()) > 0 ? View.VISIBLE : View.GONE);
        txtNazamSubTitle.setVisibility(TextUtils.isEmpty(lookingMore.getSubTitle().trim()) ? View.GONE : View.VISIBLE);
        getActivity().addFavoriteClick(offlineFavIcon, lookingMore.getId(), Enums.FAV_TYPES.CONTENT.getKey());
        getActivity().updateFavoriteIcon(offlineFavIcon, lookingMore.getId());
        txtNazamTitle.setText(lookingMore.getTitle());
        txtNazamSubTitle.setText(lookingMore.getSubTitle());
        txtAuthor.setText(lookingMore.getPoetName());
        if(MyService.getSelectedLanguage()==Enums.LANGUAGE.HINDI)
            txtAuthor.setTextSize(12);


    }
}
