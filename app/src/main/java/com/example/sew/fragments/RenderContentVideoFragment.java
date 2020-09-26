package com.example.sew.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.adapters.RenderContentVideoAdapter;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.MyService;
import com.example.sew.models.RenderContentVideo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class RenderContentVideoFragment extends BottomSheetDialogFragment implements ICommonValues {


    private static final String API_KEY = "AIzaSyAucCMhVmsY5JZRRl2JPsvRdnWeThfOHx8";

    @OnClick(R.id.spinnerToCloseImg)
    void onCloseClick() {
        dismiss();
    }

    @BindView(R.id.youtube_layout)
    YouTubePlayerView youTubePlayerView;
    @BindView(R.id.lstVideoContent)
    ListView lstVideoContent;
    private YouTubePlayer youtubePlayer;

    @OnItemClick(R.id.lstVideoContent)
    void onItemClicked(View convertView) {
        if (convertView.getTag(R.id.tag_data) instanceof RenderContentVideo) {
            RenderContentVideo audioContent = (RenderContentVideo) convertView.getTag(R.id.tag_data);
            playVideo(contentVideos.indexOf(audioContent));
        }
    }

    public static RenderContentVideoFragment getInstance(ArrayList<RenderContentVideo> contentVideos, String conententTitle) {
        RenderContentVideoFragment fragment = new RenderContentVideoFragment();
        JSONArray jsonArray = new JSONArray();
        for (RenderContentVideo contentVideo : contentVideos)
            jsonArray.put(contentVideo.getJsonObject());
        Bundle args = new Bundle();
        args.putString(VIDEO_CONTENT_ARRAY, jsonArray.toString());
        args.putString(CONTENT_TITLE, conententTitle);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<RenderContentVideo> contentVideos = new ArrayList<>();
    private String contentTitle = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_render_content_video, container, false);
        ButterKnife.bind(this, convertView);
        return convertView;
    }

    private RenderContentVideoAdapter renderContentVideoAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(VIDEO_CONTENT_ARRAY)) {
            try {
                contentTitle = getArguments().getString(CONTENT_TITLE, "");
                contentVideos.clear();
                JSONArray jsonArray = new JSONArray(getArguments().getString(VIDEO_CONTENT_ARRAY, "[]"));
                for (int i = 0; i < jsonArray.length(); i++)
                    contentVideos.add(new RenderContentVideo(jsonArray.optJSONObject(i % jsonArray.length())));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }
        }
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                RenderContentVideoFragment.this.youtubePlayer = youTubePlayer;
                if (videoPosition != -1) {
                    playVideo(videoPosition);
                    videoPosition = -1;
                }
            }
        });
        setLayoutDirection(view);
        lstVideoContent.setAdapter(renderContentVideoAdapter = new RenderContentVideoAdapter((BaseActivity) getActivity(), contentVideos, contentTitle));
        setCancelable(false);
        playVideo(0);
    }

    private void setLayoutDirection(View rootView) {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
            case HINDI:
                rootView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                break;
            case URDU:
                rootView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                break;
        }
    }

    private int videoPosition = -1;

    private void playVideo(final int position) {
        if (youtubePlayer != null) {
            youtubePlayer.loadVideo(contentVideos.get(position).getYoutubeId(), 0);
            renderContentVideoAdapter.setRenderContentVideo(contentVideos.get(position));
        } else {
            videoPosition = position;
            Toast.makeText(GetActivity(), "Please wait", Toast.LENGTH_SHORT).show();
        }

    }


//    @Override
//    public void onFullscreen(boolean fullSize) {
//        if (fullSize) {
//            GetActivity().setRequestedOrientation(LANDSCAPE_ORIENTATION);
//            fullscreen = 1;
//        } else {
//            GetActivity().setRequestedOrientation(PORTRAIT_ORIENTATION);
//            fullscreen = 2;
//        }
//    }

    public BaseActivity GetActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
}
