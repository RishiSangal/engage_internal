package com.example.sew.adapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.AudioContent;
import com.example.sew.models.PoetDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoetAudioAdapter extends BasePoetContentAdapter {
    private ArrayList<AudioContent> audioContents;
    private AudioContent selectedAudioContent;
    private PoetDetail poetDetail;

    public PoetAudioAdapter(BaseActivity activity, ArrayList<AudioContent> audioContents, PoetDetail poetDetail) {
        super(activity, poetDetail);
        this.audioContents = audioContents;
        this.poetDetail = poetDetail;
    }

    @Override
    public int getCount() {
        return audioContents.size() + 1;
    }

    @Override
    public AudioContent getItem(int position) {
        return audioContents.get(position - 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                PoetsProfileViewHolder poetsProfileViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.poet_detailed_header);
                    poetsProfileViewHolder = new PoetsProfileViewHolder(convertView);
                } else
                    poetsProfileViewHolder = (PoetsProfileViewHolder) convertView.getTag();
                convertView.setTag(poetsProfileViewHolder);
                loadDataForPoetHeader(poetsProfileViewHolder);
                break;
            case VIEW_TYPE_CONTENT:
                AudioViewHolder audioViewHolder;
                AudioContent audioContent = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_poet_audio);
                    audioViewHolder = new AudioViewHolder(convertView);
                } else
                    audioViewHolder = (AudioViewHolder) convertView.getTag();
                convertView.setTag(audioViewHolder);
                convertView.setTag(R.id.tag_data, audioContent);
                if (selectedAudioContent != null && selectedAudioContent.getId().contentEquals(audioContent.getId())) {
                    audioViewHolder.txtAudioTitle.setTextColor(getColor(R.color.colorPrimary));
                    audioViewHolder.txtPoetName.setTextColor(getColor(R.color.colorPrimary));
                } else {
                    audioViewHolder.txtAudioTitle.setTextColor(getActivity().getPrimaryTextColor());
                    audioViewHolder.txtPoetName.setTextColor(getActivity().getPrimaryTextColor());
                }
                if(TextUtils.isEmpty(audioContent.getAudioLength()))
                    audioViewHolder.layAudioDuration.setVisibility(View.GONE);
                else
                    audioViewHolder.layAudioDuration.setVisibility(View.VISIBLE);
                audioViewHolder.txtTimeDuration.setText(audioContent.getAudioLength());
                audioViewHolder.poetTemplateFavIcon.setTag(R.id.tag_data, audioContent);
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        audioViewHolder.txtAudioTitle.setText(audioContent.getTitleEng());
                        audioViewHolder.txtPoetName.setText(audioContent.getAudioHeaderTitleEng());
                        break;
                    case HINDI:
                        audioViewHolder.txtAudioTitle.setText(audioContent.getTitleHin());
                        audioViewHolder.txtPoetName.setText(audioContent.getAudioHeaderTitleHin());
                        break;
                    case URDU:
                        audioViewHolder.txtAudioTitle.setText(audioContent.getTitleUr());
                        audioViewHolder.txtPoetName.setText(audioContent.getAudioHeaderTitleUr());
                        break;
                }
                ImageHelper.setImage(audioViewHolder.ivLogo, audioContent.getAudioImageUrl());
                addFavoriteClick(audioViewHolder.poetTemplateFavIcon, audioContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(audioViewHolder.poetTemplateFavIcon, audioContent.getId());
                break;
        }

        return convertView;
    }

    @Override
    String getContentTitle() {
        return MyHelper.getString(R.string.audio).toUpperCase();
    }

    @Override
    String getContentCount() {
        return String.valueOf(poetDetail.getAudioCount());
    }

    @Override
    void contentFilter(View view) {

    }

    public void setSelectedAudioContent(AudioContent audioContent) {
        this.selectedAudioContent = audioContent;
        notifyDataSetChanged();
    }

    static class AudioViewHolder {
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.txtAudioTitle)
        TextView txtAudioTitle;
        @BindView(R.id.txtPoetName)
        TextView txtPoetName;
        @BindView(R.id.poetTemplateFavIcon)
        ImageView poetTemplateFavIcon;
        @BindView(R.id.layAudioDuration)
        LinearLayout layAudioDuration;
        @BindView(R.id.txtTimeDuration)
        TextView txtTimeDuration;
        AudioViewHolder(View view) {
            ButterKnife.bind(this, view);
            poetTemplateFavIcon.setVisibility(View.INVISIBLE);
        }
    }
}
