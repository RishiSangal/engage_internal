package com.example.sew.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.MyService;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VideoFragment extends DialogFragment implements ICommonValues {

    @OnClick(R.id.spinnerToCloseImg)
    void onCloseClick() {
        dismiss();
    }

    @BindView(R.id.youtube_layout)
    YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youtubePlayer;

    public static VideoFragment getInstance(String youtubeVideoId) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(YOUTUBE_VIDEO_ID, youtubeVideoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_youtube_video, container, false);
        ButterKnife.bind(this, convertView);
        return convertView;
    }

    String youtubeId;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(YOUTUBE_VIDEO_ID)) {
            youtubeId = getArguments().getString(YOUTUBE_VIDEO_ID, "");
        }
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                VideoFragment.this.youtubePlayer=youTubePlayer;
                playVideo();
            }
        });
        setLayoutDirection(view);
        setCancelable(false);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
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


    private void playVideo() {
        if (youtubePlayer != null) {
            youtubePlayer.loadVideo(youtubeId, 0);
        }

    }

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
