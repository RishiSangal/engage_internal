package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.RenderContentAudio;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class RenderContentAudioAdapter extends BaseMyAdapter {
    private ArrayList<RenderContentAudio> audioContents;
    private RenderContentAudio selectedAudioContent;

    public RenderContentAudioAdapter(BaseActivity activity, ArrayList<RenderContentAudio> audioContents) {
        super(activity);
        this.audioContents = audioContents;
    }

    @Override
    public int getCount() {
        return audioContents.size();
    }

    @Override
    public RenderContentAudio getItem(int position) {
        return audioContents.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;

        AudioViewHolder audioViewHolder;
        RenderContentAudio audioContent = getItem(position);
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_render_content_audio);
            audioViewHolder = new AudioViewHolder(convertView);
        } else
            audioViewHolder = (AudioViewHolder) convertView.getTag();
        convertView.setTag(audioViewHolder);
        convertView.setTag(R.id.tag_data, audioContent);
        if (selectedAudioContent != null && selectedAudioContent.getId().contentEquals(audioContent.getId())) {
            audioViewHolder.txtPoetName.setTextColor(getColor(R.color.colorPrimary));
        } else {
            audioViewHolder.txtPoetName.setTextColor(getActivity().getPrimaryTextColor());
        }
        audioViewHolder.txtPerformedBy.setText(MyHelper.getString(R.string.performed_by));
        audioViewHolder.txtPoetName.setText(audioContent.getAuthorName());
        ImageHelper.setImage(audioViewHolder.ivLogo, audioContent.getImageUrl());
        return convertView;
    }

    public void setSelectedAudioContent(RenderContentAudio audioContent) {
        this.selectedAudioContent = audioContent;
        notifyDataSetChanged();
    }

    static class AudioViewHolder {
        @BindView(R.id.iv_logo)
        CircleImageView ivLogo;
        @BindView(R.id.txtPerformedBy)
        TextView txtPerformedBy;
        @BindView(R.id.txtPoetName)
        TextView txtPoetName;

        AudioViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
