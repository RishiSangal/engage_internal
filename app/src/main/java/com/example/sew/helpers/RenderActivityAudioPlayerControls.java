package com.example.sew.helpers;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.models.BaseAudioContent;
import com.example.sew.models.PlayingAudioItem;
import com.example.sew.models.RenderContentAudio;
import com.example.sew.views.TitleTextView;
import com.google.android.gms.common.util.CollectionUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RenderActivityAudioPlayerControls implements SeekBar.OnSeekBarChangeListener {
    public View controlView;
    private final String TAG = "IMRKJ_AUDIO";

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try {
            if (audioPlayer.isPlaying()) {
                if (fromUser)
                    audioPlayer.seekTo(progress);
            }
        } catch (Exception e) {
            seekBar.setEnabled(false);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface onAudioPlayerStateChanged {
        void onAudioPause();

        void onAudioStart();

        void onAudioWindowClose();

        void onAudioSelected(RenderContentAudio audioContent);
    }

    @BindView(R.id.audioTitle)
    TitleTextView audioTitle;
    @BindView(R.id.progessSeekBar)
    SeekBar progessSeekBar;
    @Nullable
    @BindView(R.id.remiainTime)
    TitleTextView remainTime;
    @Nullable
    @BindView(R.id.totalTime)
    TitleTextView totalTime;
    @Nullable
    @BindView(R.id.layAudioPlayerParent)
    CardView layAudioPlayerParent;
    @Nullable
    @BindView(R.id.poetDetailPlayBtn)
    ImageView poetDetailPlayBtn;
    @Nullable
    @BindView(R.id.poetDetailFwdBtn)
    ImageView poetDetailFwdBtn;
    @Nullable
    @BindView(R.id.poetDetailBkdBtn)
    ImageView poetDetailBkdBtn;
    @Nullable
    @BindView(R.id.layAudioLoading)
    View layAudioLoading;
    @BindView(R.id.aviLoading)
    AVLoadingIndicatorView aviLoading;
    private BaseActivity activity;
    private CustomMediaPlayer audioPlayer;
    private onAudioPlayerStateChanged onAudioPlayerStateChanged;
    private ArrayList<RenderContentAudio> audioContents = new ArrayList<>();
//    private MediaPlayer mp;

    public RenderActivityAudioPlayerControls(BaseActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity.findViewById(R.id.layAudioPlayerParent));
        init();
    }

    public RenderActivityAudioPlayerControls(BaseActivity activity, View view, ArrayList<RenderContentAudio> contentAudios) {
        this.activity = activity;
        this.controlView = view;
        this.audioContents.clear();
        this.audioContents.addAll(contentAudios);
        ButterKnife.bind(this, view);
        init();
    }

    private void enableAudioControls(boolean enable) {
        if (poetDetailBkdBtn != null)
            poetDetailBkdBtn.setEnabled(enable);
        if (poetDetailFwdBtn != null)
            poetDetailFwdBtn.setEnabled(enable);
        if (poetDetailPlayBtn != null)
            poetDetailPlayBtn.setEnabled(enable);
        if (progessSeekBar != null)
            progessSeekBar.setEnabled(enable);
    }

    private void init() {
        enableAudioControls(false);
        progessSeekBar.setOnSeekBarChangeListener(this);
        aviLoading.show();
        audioPlayer = CustomMediaPlayer.getInstance();
      //  if (audioPlayer.isPlaying() || audioPlayer.isPause()) {
            enableAudioControls(true);
            if (layAudioLoading != null)
                layAudioLoading.setVisibility(View.GONE);
      //  }
        audioPlayer.setMedia(audioContents);
        audioPlayer.addOnMusicStateChangeListener(new CustomMediaPlayer.OnMusicStateChangeListener() {
            @Override
            public void onAudioPlayerDestroy() {
                closePlayer();
            }

            @Override
            public void onAudioSelected(RenderContentAudio audioContent) {
                Log.d(TAG, "onAudioSelected: " + audioContent);
                if (onAudioPlayerStateChanged != null)
                    onAudioPlayerStateChanged.onAudioSelected(audioContent);
                audioPlayer.startNewSong(audioContent);
            }

            @Override
            public void onAudioPause() {
                Log.d(TAG, "onAudioPause: ");
                if (poetDetailPlayBtn != null)
                    poetDetailPlayBtn.setImageResource(R.drawable.ic_play);
                if (onAudioPlayerStateChanged != null)
                    onAudioPlayerStateChanged.onAudioPause();
            }

            @Override
            public void onAudioStart() {
                Log.d(TAG, "onAudioStart: ");
                isAudioProcessing = false;
                if (poetDetailPlayBtn != null)
                    poetDetailPlayBtn.setImageResource(R.drawable.ic_pause);
                if (layAudioLoading != null)
                    layAudioLoading.setVisibility(View.GONE);
                if (onAudioPlayerStateChanged != null)
                    onAudioPlayerStateChanged.onAudioStart();
            }

            @Override
            public void onAudioProgressChanged(int currentProgress, int totalProgress) {
                Log.d(TAG, "onAudioProgressChanged: currentProgress: " + currentProgress + ", totalProgress: " + totalProgress);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enableAudioControls(true);
                        if (layAudioLoading != null)
                            layAudioLoading.setVisibility(View.GONE);
                        if (poetDetailPlayBtn != null)
                            poetDetailPlayBtn.setImageResource(R.drawable.ic_pause);
                        if (onAudioPlayerStateChanged != null)
                            onAudioPlayerStateChanged.onAudioStart();
                        progessSeekBar.setEnabled(true);
                        progessSeekBar.setMax(totalProgress);
                        updatePlayer(currentProgress, remainTime);
                        updatePlayerControls();
                    }
                });
            }
        });
        layAudioLoading.setOnClickListener(v -> {
        });
    }

    public void setOnAudioPlayerStateChanged(RenderActivityAudioPlayerControls.onAudioPlayerStateChanged onAudioPlayerStateChanged) {
        this.onAudioPlayerStateChanged = onAudioPlayerStateChanged;
    }

    private String length = null;
    private int currentPosition;
    private boolean isAudioProcessing;

    public void playAudio(int position, int startTime) {
        isPlayerClosed = false;
        if (isAudioProcessing) {
            BaseActivity.showToast("Please wait");
            return;
        }
        isAudioProcessing = true;
        if (layAudioPlayerParent != null)
            layAudioPlayerParent.setVisibility(View.VISIBLE);
        layAudioLoading.setVisibility(View.VISIBLE);
        currentPosition = position;
        audioPlayer.playMedia(position);
        updatePlayerControls();
    }

    public void playAudio(int position) {
        playAudio(position, 0);
    }

    private void updatePlayerControls() {
        if (currentPosition == 0)
            poetDetailBkdBtn.setAlpha(0.3f);
        else
            poetDetailBkdBtn.setAlpha(1f);
        if ((currentPosition) == (audioContents.size() - 1))
            poetDetailFwdBtn.setAlpha(0.3f);
        else
            poetDetailFwdBtn.setAlpha(1f);
    }

    boolean isPlayerClosed;

    private void closePlayer() {
//        pauseMediaPlayer();
//        isPlayerClosed = true;
        if (layAudioPlayerParent != null)
            layAudioPlayerParent.setVisibility(View.GONE);
        if (onAudioPlayerStateChanged != null)
            onAudioPlayerStateChanged.onAudioWindowClose();
    }

    @OnClick({R.id.closePlayer, R.id.poetDetailPlayBtn, R.id.poetDetailFwdBtn, R.id.poetDetailBkdBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closePlayer:
                closePlayer();
                break;
            case R.id.poetDetailPlayBtn:
                try {
                    if (audioPlayer != null && audioPlayer.isPlaying()) {
                        audioPlayer.pauseSong();
                        poetDetailPlayBtn.setImageResource(R.drawable.ic_play);
                    } else {
                        poetDetailPlayBtn.setImageResource(R.drawable.ic_pause);
                        audioPlayer.playSong();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.poetDetailFwdBtn:
                if (currentPosition < audioContents.size() - 1) {
                    playAudio(currentPosition + 1);
                } else {
                    activity.showToast(AppErrorMessage.poetaudio_fragment_no_next_audio_available);
                }
                break;
            case R.id.poetDetailBkdBtn:
                if (currentPosition > 0) {
                    playAudio(currentPosition - 1);
                } else {
                    activity.showToast(AppErrorMessage.poetaudio_fragment_no_previous_audio_available);
                }
                break;
        }
    }

    public void pauseMediaPlayer() {
        audioPlayer.pauseSong();
    }

    public void stopAudioAndCloseWindow() {
        if (progessSeekBar != null) {
            try {
                progessSeekBar.setProgress(0);
            } catch (Exception ignored) {

            }
        }
        audioPlayer.destroyMediaPlayer();

//        try {
//            if (mp != null && mp.isPlaying()) {
//                currentPlayingState = PlayingAudioItem.PLAYING_STATE_PLAYING;
//                this.currentDuration = mp.getCurrentPosition();
//                if (mp.getDuration() > 0) {
//                    mp.stop();
//                    mp = null;
//                }
//                poetDetailPlayBtn.setImageResource(R.drawable.ic_play);
//                progessSeekBar.setProgress(0);
//            } else
//                currentPlayingState = PlayingAudioItem.PLAYING_STATE_PAUSE;
//        } catch (Exception ignored) {
//        }

    }


    private void updatePlayer(int currentDuration, TextView remiainTime) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                remiainTime.setText(String.format("%s", milliSecondsToTimer((long) currentDuration)));
                progessSeekBar.setEnabled(true);
                progessSeekBar.setProgress(currentDuration);
            }
        });

    }

    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        finalTimerString = finalTimerString + minutes + ":" + secondsString;
        return finalTimerString;
    }

    public void updateUI() {
        if (CollectionUtils.isEmpty(audioContents))
            return;
        audioTitle.setText(audioContents.get(currentPosition).getAudioTitle());
        totalTime.setText(length);
        if (audioPlayer.isPlaying())
            poetDetailPlayBtn.setImageResource(R.drawable.ic_pause);
        else
            poetDetailPlayBtn.setImageResource(R.drawable.ic_play);
    }

    public final void onPause() {

    }

    public final void onDestroy() {

    }
}
