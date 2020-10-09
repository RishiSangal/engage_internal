package com.example.sew.helpers;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.fragments.RenderContentAudioFragment;
import com.example.sew.models.BaseAudioContent;
import com.example.sew.models.PlayingAudioItem;
import com.example.sew.views.TitleTextView;
import com.google.android.gms.common.util.CollectionUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioPlayerControls implements Runnable,
        SeekBar.OnSeekBarChangeListener {
    public View controlView;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        try {
            if (mp != null) {
                if (mp.isPlaying() && fromUser)
                    mp.seekTo(progress);
            } else {
                seekBar.setProgress(0);
            }
        } catch (Exception e) {
            seekBar.setEnabled(false);
        }
        //  if (activity == null || isPlayerClosed || !isWindowFocused() || activity.isDestroyed() || layAudioPlayerParent.getVisibility() == View.GONE) {
        if (activity == null || isPlayerClosed || !isWindowFocused() || activity.isDestroyed()) {
            closePlayer();
        } else
            updatePlayer(progress, remainTime);
    }

    private boolean isWindowFocused() {
        if (activity == null)
            return false;
        if (activity.hasWindowFocus())
            return true;
        if (!CollectionUtils.isEmpty(activity.getSupportFragmentManager().getFragments())) {
            for (Fragment fragment : activity.getSupportFragmentManager().getFragments())
                if (fragment instanceof RenderContentAudioFragment)
                    return true;
        }
        return false;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void run() {
        if (mp != null) {
            int currentPosition = mp.getCurrentPosition();
            int total = mp.getDuration();

            while (mp != null && currentPosition < total) {
                try {
                    Thread.sleep(1000);
                    currentPosition = mp.getCurrentPosition();
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    return;
                }
                progessSeekBar.setProgress(currentPosition);
            }
        }
    }

    public interface onAudioPlayerStateChanged {
        void onAudioPause();

        void onAudioStart();

        void onAudioWindowClose();

        void onAudioSelected(BaseAudioContent audioContent);
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
    onAudioPlayerStateChanged onAudioPlayerStateChanged;
    private ArrayList<? extends BaseAudioContent> audioContents;
    MediaPlayer mp;

    public AudioPlayerControls(BaseActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity.findViewById(R.id.layAudioPlayerParent));
        init();
    }

    public AudioPlayerControls(BaseActivity activity, View view) {
        this.activity = activity;
        this.controlView = view;
        ButterKnife.bind(this, view);
        init();
    }

    public void setAudioContents(ArrayList<? extends BaseAudioContent> audioContents) {
        this.audioContents = audioContents;
    }

    private void init() {
        progessSeekBar.setOnSeekBarChangeListener(this);
        aviLoading.show();
        layAudioLoading.setOnClickListener(v -> {
        });
    }

    public void setOnAudioPlayerStateChanged(AudioPlayerControls.onAudioPlayerStateChanged onAudioPlayerStateChanged) {
        this.onAudioPlayerStateChanged = onAudioPlayerStateChanged;
    }

    private String audioUrl = null;
    private String length = null;
    private int currentPosition;
    boolean isAudioProessing;

    public void playAudio(int position, int startTime) {
        isPlayerClosed = false;
        if (isAudioProessing) {
            activity.showToast("Please wait");
            return;
        }
        isAudioProessing = true;
        if (mp != null && mp.isPlaying())
            mp.stop();
        if (layAudioPlayerParent != null){
            layAudioPlayerParent.setVisibility(View.VISIBLE);
            layAudioPlayerParent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
        layAudioLoading.setVisibility(View.VISIBLE);
        currentPosition = position;
        Uri myUri = null;
        audioUrl = audioContents.get(position).getAudioUrl();
        if (onAudioPlayerStateChanged != null)
            onAudioPlayerStateChanged.onAudioSelected(audioContents.get(position));
        new PrepareAudioPlayerAsync(audioUrl, this, startTime).execute();
        if (currentPosition == 0)
            poetDetailBkdBtn.setAlpha(0.3f);
        else
            poetDetailBkdBtn.setAlpha(1f);
        if ((currentPosition) == (audioContents.size() - 1))
            poetDetailFwdBtn.setAlpha(0.3f);
        else
            poetDetailFwdBtn.setAlpha(1f);
    }

    public final PlayingAudioItem getCurrentlyPlayingItem() {
        PlayingAudioItem playingAudioItem = new PlayingAudioItem(null);
        playingAudioItem.setAudioContent(audioContents.get(currentPosition));
        playingAudioItem.setCurrentPlayingPosition(currentDuration);
        playingAudioItem.setCurrentPlayingState(currentPlayingState);
        return playingAudioItem;
    }

    public void playAudio(int position) {
        playAudio(position, 0);
    }

    private static class PrepareAudioPlayerAsync extends AsyncTask<Void, Void, Void> {
        String audioUrl;
        AudioPlayerControls audioPlayerControls;
        int startTime = 0;

        PrepareAudioPlayerAsync(String audioUrl, AudioPlayerControls audioPlayerControls) {
            this.audioUrl = audioUrl;
            this.audioPlayerControls = audioPlayerControls;
        }

        PrepareAudioPlayerAsync(String audioUrl, AudioPlayerControls audioPlayerControls, int startTime) {
            this.audioUrl = audioUrl;
            this.audioPlayerControls = audioPlayerControls;
            this.startTime = startTime;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Uri myUri = null;
            if (!TextUtils.isEmpty(audioUrl)) {
                myUri = Uri.parse(audioUrl);
            }
            try {
                audioPlayerControls.createMediaPlayer();
                audioPlayerControls.mp.setDataSource(audioPlayerControls.activity, myUri);
                audioPlayerControls.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                audioPlayerControls.mp.prepare(); //don't use prepareAsync for mp3 playback
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            audioPlayerControls.layAudioLoading.setVisibility(View.GONE);
            audioPlayerControls.mp.start();
            if (startTime > 0)
                audioPlayerControls.mp.seekTo(startTime);
            audioPlayerControls.progessSeekBar.setEnabled(true);
            audioPlayerControls.progessSeekBar.setMax(audioPlayerControls.mp.getDuration());
            new Thread(audioPlayerControls).start();
            audioPlayerControls.updateUI();
            audioPlayerControls.isAudioProessing = false;
        }
    }

    private void createMediaPlayer() {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
        mp = new MediaPlayer();
//        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//            }
//        });
    }

    boolean isPlayerClosed;

    private void closePlayer() {
        stopAudioAndCloseWindow();
        isPlayerClosed = true;
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
                    if (mp != null && mp.isPlaying()) {
                        mp.pause();
                        poetDetailPlayBtn.setImageResource(R.drawable.ic_play);
                    } else {
                        if (mp != null) {
                            poetDetailPlayBtn.setImageResource(R.drawable.ic_pause);
                            mp.start();
                        }
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

    public void stopAudioAndCloseWindow() {
        try {
            if (mp != null && mp.isPlaying()) {
                currentPlayingState = PlayingAudioItem.PLAYING_STATE_PLAYING;
                this.currentDuration = mp.getCurrentPosition();
                if (mp.getDuration() > 0) {
                    mp.stop();
                    mp = null;
                }
                poetDetailPlayBtn.setImageResource(R.drawable.ic_play);
                progessSeekBar.setProgress(0);
            } else
                currentPlayingState = PlayingAudioItem.PLAYING_STATE_PAUSE;
        } catch (Exception ignored) {
        }

    }

    public MediaPlayer getMediaPlayer() {
        return mp;
    }

    private int currentDuration;
    @PlayingAudioItem.PlayingState
    private int currentPlayingState = PlayingAudioItem.PLAYING_STATE_PAUSE;

    private void updatePlayer(int currentDuration, TextView remiainTime) {
        remiainTime.setText(String.format("%s", milliSecondsToTimer((long) currentDuration)));
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
        if (mp != null && mp.isPlaying())
            poetDetailPlayBtn.setImageResource(R.drawable.ic_pause);
        else
            poetDetailPlayBtn.setImageResource(R.drawable.ic_play);
    }

    public final void onPause() {

    }

    public final void onDestroy() {

    }
}
