package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.RenderContentVideo;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RenderContentVideoAdapter extends BaseMyAdapter {
    private ArrayList<RenderContentVideo> contentVideos;
    private RenderContentVideo selectedContentVideo;
    private String contentTitle;

    final int VIEW_TYPE_HEADER = 0;
    final int VIEW_TYPE_CONTENT = 1;

    public RenderContentVideoAdapter(BaseActivity activity, ArrayList<RenderContentVideo> contentVideos, String contentTitle) {
        super(activity);
        this.contentTitle = contentTitle;
        this.contentVideos = contentVideos;
    }

    @Override
    public int getCount() {
        return contentVideos.size() + 1;
    }

    @Override
    public RenderContentVideo getItem(int position) {
        return contentVideos.get(position - 1);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                HeaderViewHolder headerViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_render_content_video_header);
                    headerViewHolder = new HeaderViewHolder(convertView);
                } else
                    headerViewHolder = (HeaderViewHolder) convertView.getTag();
                convertView.setTag(headerViewHolder);
                headerViewHolder.txtHeaderContentTitle.setText(contentTitle);
                if (selectedContentVideo != null)
                    headerViewHolder.txtPoetName.setText(selectedContentVideo.getAuthorName());
                else
                    headerViewHolder.txtPoetName.setText("");
                break;
            case VIEW_TYPE_CONTENT: {
                AudioViewHolder audioViewHolder;
                RenderContentVideo contentVideo = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_render_content_video);
                    audioViewHolder = new AudioViewHolder(convertView);
                } else
                    audioViewHolder = (AudioViewHolder) convertView.getTag();
                convertView.setTag(audioViewHolder);
                convertView.setTag(R.id.tag_data, contentVideo);
                if (selectedContentVideo != null && selectedContentVideo.getYoutubeId().contentEquals(contentVideo.getYoutubeId())) {
                    audioViewHolder.txtPoetName.setTextColor(getColor(R.color.colorPrimary));
                } else {
                    audioViewHolder.txtPoetName.setTextColor(MyHelper.getPrimaryTextColor(getActivity()));
                }
                audioViewHolder.txtPerformedBy.setText(MyHelper.getString(R.string.performed_by));
                audioViewHolder.txtPoetName.setText(contentVideo.getAuthorName());
                ImageHelper.setImage(audioViewHolder.ivLogo, contentVideo.getImageUrl());
            }
            break;
        }

        return convertView;
    }

    public void setRenderContentVideo(RenderContentVideo contentVideo) {
        this.selectedContentVideo = contentVideo;
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

    static
    class HeaderViewHolder {
        @BindView(R.id.txtPoetName)
        TitleTextView txtPoetName;
        @BindView(R.id.txtHeaderContentTitle)
        TitleTextViewType2 txtHeaderContentTitle;
        @BindView(R.id.imgFavorite)
        ImageView imgFavorite;
        @BindView(R.id.imgShare)
        ImageView imgShare;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
