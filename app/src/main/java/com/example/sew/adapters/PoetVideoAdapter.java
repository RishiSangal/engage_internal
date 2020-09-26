package com.example.sew.adapters;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.YoutubeHandler;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.VideoContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoetVideoAdapter extends BasePoetContentAdapter {
    private ArrayList<VideoContent> videoContents;
    private PoetDetail poetDetail;

    public PoetVideoAdapter(BaseActivity activity, ArrayList<VideoContent> videoContents, PoetDetail poetDetail) {
        super(activity, poetDetail);
        this.videoContents = videoContents;
        this.poetDetail = poetDetail;
    }


    @Override
    public int getCount() {
        return videoContents.size() + 1;
    }

    @Override
    public VideoContent getItem(int position) {
        return videoContents.get(position - 1);
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
                VideoViewHolder videoViewHolder;
                VideoContent videoContent = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_poet_video);
                    videoViewHolder = new VideoViewHolder(convertView);
                } else
                    videoViewHolder = (VideoViewHolder) convertView.getTag();
                convertView.setTag(videoViewHolder);
                convertView.setTag(R.id.tag_data, videoContent);
                if(!TextUtils.isEmpty(videoContent.getTotalCount())){
                    videoViewHolder.totalViewer.setVisibility(View.VISIBLE);
                    videoViewHolder.imgViewer.setVisibility(View.VISIBLE);
                }else{
                    videoViewHolder.totalViewer.setVisibility(View.GONE);
                    videoViewHolder.imgViewer.setVisibility(View.GONE);
                }
                videoViewHolder.totalViewer.setText(videoContent.getTotalCount());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoContent videoContent = (VideoContent) v.getTag(R.id.tag_data);
                        getActivity().startActivity(new Intent(getActivity(), YoutubeHandler.class).putExtra("videoId", videoContent.getYoutubeId()).putExtra("cActivity", "Dashboard"));
                    }
                });
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        videoViewHolder.tvDiscrpition.setText(videoContent.getTitleEng());
                        videoViewHolder.txtPoetName.setText(videoContent.getAudioHeaderTitleEng());
                        break;
                    case HINDI:
                        videoViewHolder.tvDiscrpition.setText(videoContent.getTitleHin());
                        videoViewHolder.txtPoetName.setText(videoContent.getAudioHeaderTitleHin());
                        break;
                    case URDU:
                        videoViewHolder.tvDiscrpition.setText( videoContent.getTitleUr());
                        videoViewHolder.txtPoetName.setText(videoContent.getAudioHeaderTitleUr());
                        break;
                }
                ImageHelper.setImage(videoViewHolder.youtubeThumbnail, String.format("http://img.youtube.com/vi/%s/0.jpg", videoContent.getYoutubeId()), Enums.PLACEHOLDER_TYPE.SHAYARI_COLLECTION);
                break;
        }

        return convertView;
    }

    @Override
    String getContentTitle() {
        return MyHelper.getString(R.string.video).toUpperCase();
    }

    @Override
    String getContentCount() {
        return String.valueOf(poetDetail.getVideoCount());
    }

    @Override
    void contentFilter(View view) {

    }

    static class VideoViewHolder {
        @BindView(R.id.youtube_thumbnail)
        ImageView youtubeThumbnail;
        @BindView(R.id.btnYoutube_playerlk)
        ImageView btnYoutubePlayerlk;
        @BindView(R.id.tv_discrpition)
        TextView tvDiscrpition;
        @BindView(R.id.total_viewer)
        TextView totalViewer;
        @BindView(R.id.layViewCount)
        LinearLayout layViewCount;
        @BindView(R.id.imgViewer)
        ImageView imgViewer;
        @BindView(R.id.txtPoetName)
        TextView txtPoetName;

        VideoViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
