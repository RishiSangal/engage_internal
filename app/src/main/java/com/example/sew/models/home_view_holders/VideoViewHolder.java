package com.example.sew.models.home_view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.HomeVideo;
import com.example.sew.views.TitleTextViewType6;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoViewHolder extends BaseHomeViewHolder {

    @BindView(R.id.txtFeaturedVideoTitle)
    TitleTextViewType6 txtFeaturedVideoTitle;
    //    @BindView(R.id.youtube_layout)
//    YouTubePlayerView youtubeLayout;
    @BindView(R.id.imgVideoImage)
    ImageView imgVideoImage;
    @BindView(R.id.layVideoLayout)
    RelativeLayout layVideoLayout;
    @BindView(R.id.txtVideoName)
    TitleTextViewType6 txtVideoName;
    @BindView(R.id.txtVideoSubTitle)
    TitleTextViewType6 txtVideoSubTitle;
    @BindView(R.id.imgShare)
    ImageView imgShare;
    private YouTubePlayer youtubePlayer;

    @OnClick(R.id.imgShare)
    void onShareClick() {
        BaseActivity.shareTheUrl(String.format("https://www.youtube.com/watch?v=%s", video.getYoutube_Id()), getActivity());
    }

    @OnClick(R.id.imgVideoImage)
    void onVideoImageClick() {
        getActivity().showYoutubeDialog(video.getYoutube_Id());
    }

    public static VideoViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        VideoViewHolder videoViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_video, baseActivity);
            videoViewHolder = new VideoViewHolder(convertView, baseActivity);
        } else
            videoViewHolder = (VideoViewHolder) convertView.getTag();
        videoViewHolder.setConvertView(convertView);
        convertView.setTag(videoViewHolder);
        return videoViewHolder;
    }

    private VideoViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
    }

    private boolean isLoaded;
    private HomeVideo video;

    public BaseHomeViewHolder loadData(final HomeVideo video) {
        if (!isLoaded) {
            this.video = video;
            isLoaded = true;
            updateUI();
        }
        return this;
    }

    private void updateUI() {
        txtFeaturedVideoTitle.setText(MyHelper.getString(R.string.feaured_video));
        txtVideoName.setText(video.getVideoTitle().trim());
        txtVideoSubTitle.setText(video.getEntityName());
        if(MyService.getSelectedLanguage()==Enums.LANGUAGE.HINDI){
            txtVideoSubTitle.setTextSize(12);
        }
        ImageHelper.setImage(imgVideoImage, video.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE);
//        getActivity().getLifecycle().addObserver(youtubeLayout);
//        youtubeLayout.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
//                super.onReady(youTubePlayer);
//                VideoViewHolder.this.youtubePlayer = youTubePlayer;
//                youtubePlayer.cueVideo(video.getYoutube_Id(), 0);
//            }
//        });
    }
}
