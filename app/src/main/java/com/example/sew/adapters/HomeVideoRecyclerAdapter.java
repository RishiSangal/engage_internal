package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.models.HomeVideo;
import com.example.sew.views.SquareImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeVideoRecyclerAdapter extends BaseRecyclerAdapter {

    private ArrayList<HomeVideo> videos;

    public HomeVideoRecyclerAdapter(BaseActivity activity, ArrayList<HomeVideo> videos) {
        super(activity);
        this.videos = videos;
    }


    public HomeVideo getItem(int position) {
        return videos.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(getInflatedView(R.layout.cell_home_more_video_item));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof VideoViewHolder))
            return;
        HomeVideo video = getItem(position);
        VideoViewHolder poetViewHolder = (VideoViewHolder) holder;
        poetViewHolder.itemView.setTag(R.id.tag_data, video);
        poetViewHolder.txtVideoTitle.setText(video.getVideoTitle());
        ImageHelper.setImage(poetViewHolder.imgVideoImage, video.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends BaseViewHolder {
        @BindView(R.id.imgVideoImage)
        ImageView imgVideoImage;
        @BindView(R.id.txtVideoTitle)
        TextView txtVideoTitle;

        @OnClick()
        void onItemClick(View view) {
            HomeVideo video = (HomeVideo) view.getTag(R.id.tag_data);
            getActivity().showYoutubeDialog(video.getYoutube_Id());
//            HomeTopPoet topPoet = (HomeTopPoet) view.getTag(R.id.tag_data);
//            getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), topPoet.getEntityId()));
        }

        VideoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
