package com.example.sew.helpers;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sew.MyApplication;
import com.example.sew.activities.BaseActivity;
import com.example.sew.models.BaseAudioContent;
import com.example.sew.models.PlayingAudioItem;
import com.example.sew.models.RenderContentAudio;

import java.util.ArrayList;

public class RenderActivityAudioPlayer implements Runnable {

    private boolean isWindowFocused() {
      /*  if (activity == null)
            return false;
        if (activity.hasWindowFocus())
            return true;
        if (!CollectionUtils.isEmpty(activity.getSupportFragmentManager().getFragments())) {
            for (Fragment fragment : activity.getSupportFragmentManager().getFragments())
                if (fragment instanceof RenderContentAudioFragment)
                    return true;
        }*/
        return false;
    }

    @Override
    public void run() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int total = mediaPlayer.getDuration();
            while (mediaPlayer != null && currentPosition < total) {
                try {
                    Thread.sleep(1000);
                    currentPosition = mediaPlayer.getCurrentPosition();
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    return;
                }
                for (onAudioPlayerStateChanged onAudioPlayerStateChanged : onAudioPlayerStateChangedListener)
                    onAudioPlayerStateChanged.onAudioProgressChanged(currentPosition, total);
            }
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public interface onAudioPlayerStateChanged {
        void onAudioPause();

        void onAudioStart();

        void onAudioSelected(BaseAudioContent audioContent);

        void onAudioProgressChanged(int currentProgress, int totalProgress);
    }


    private ArrayList<onAudioPlayerStateChanged> onAudioPlayerStateChangedListener = new ArrayList<>();
    private ArrayList<RenderContentAudio> audioContents;
    //    private MediaPlayer mp;
    private static RenderActivityAudioPlayer renderActivityAudioPlayer;

    public static RenderActivityAudioPlayer createNewInstance(ArrayList<RenderContentAudio> audioContents) {
        return renderActivityAudioPlayer = new RenderActivityAudioPlayer(audioContents);
    }

    public static RenderActivityAudioPlayer getInstance() {
        return renderActivityAudioPlayer;
    }

    private RenderActivityAudioPlayer(ArrayList<RenderContentAudio> audioContents) {
        this.audioContents = audioContents;
        init();
    }

    private void init() {
        createMediaPlayer();
    }

    public void addOnAudioPlayerStateChanged(RenderActivityAudioPlayer.onAudioPlayerStateChanged onAudioPlayerStateChanged) {
        if (!this.onAudioPlayerStateChangedListener.contains(onAudioPlayerStateChanged))
            this.onAudioPlayerStateChangedListener.add(onAudioPlayerStateChanged);
    }

    private String length = null;
    private int currentPosition;
    private boolean isAudioProcessing;
    private boolean isPause = false;

    public void playAudio(int position) {
        playAudio(position, 0);
        isPause = false;
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void stopMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            MyService.createNewRenderContentMediaPlayer();
            isPause = false;
        }
    }

    public void pauseMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
            for (onAudioPlayerStateChanged onAudioPlayerStateChanged : onAudioPlayerStateChangedListener)
                onAudioPlayerStateChanged.onAudioPause();
        }
    }

    public void resumeMediaPlayer() {
        if (mediaPlayer != null && isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    public void seekTo(int msec) throws IllegalStateException {
        mediaPlayer.seekTo(msec);
    }

    public void playAudio(int position, int startTime) {
        isPlayerClosed = false;
        if (isAudioProcessing) {
            BaseActivity.showToast("Please wait");
            return;
        }
        isAudioProcessing = true;
        currentPosition = position;
        String audioUrl = audioContents.get(position).getAudioUrl();
        for (onAudioPlayerStateChanged onAudioPlayerStateChanged : onAudioPlayerStateChangedListener)
            onAudioPlayerStateChanged.onAudioSelected(audioContents.get(position));
        new PrepareAudioPlayerAsync(audioUrl, this, startTime).execute();
    }

//    public final PlayingAudioItem getCurrentlyPlayingItem() {
//        PlayingAudioItem playingAudioItem = new PlayingAudioItem(null);
//        playingAudioItem.setAudioContent(audioContents.get(currentPosition));
//        playingAudioItem.setCurrentPlayingPosition(currentDuration);
//        playingAudioItem.setCurrentPlayingState(currentPlayingState);
//        return playingAudioItem;
//    }

    private static class PrepareAudioPlayerAsync extends AsyncTask<Void, Void, Void> {
        String audioUrl;
        RenderActivityAudioPlayer audioPlayer;
        int startTime = 0;

        PrepareAudioPlayerAsync(String audioUrl, RenderActivityAudioPlayer audioPlayer) {
            this.audioUrl = audioUrl;
            this.audioPlayer = audioPlayer;
        }

        PrepareAudioPlayerAsync(String audioUrl, RenderActivityAudioPlayer audioPlayer, int startTime) {
            this.audioUrl = audioUrl;
            this.audioPlayer = audioPlayer;
            this.startTime = startTime;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Uri myUri = null;
            if (!TextUtils.isEmpty(audioUrl)) {
                myUri = Uri.parse(audioUrl);
            }
            try {
                if (audioPlayer.mediaPlayer.isPlaying() || audioPlayer.isPause) {
//                    audioPlayer.mediaPlayer.pause();
                    audioPlayer.mediaPlayer.seekTo(0);
                    audioPlayer.mediaPlayer.reset();
                    audioPlayer.mediaPlayer.stop();
                    audioPlayer.mediaPlayer.release();
                }
                audioPlayer.createMediaPlayer();
                audioPlayer.mediaPlayer.setDataSource(MyApplication.getContext(), myUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    audioPlayer.mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build());
                } else {
                    audioPlayer.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
//                audioPlayerControls.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                audioPlayer.mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            audioPlayer.mediaPlayer.start();
            if (startTime > 0)
                audioPlayer.mediaPlayer.seekTo(startTime);
            new Thread(audioPlayer).start();
            for (onAudioPlayerStateChanged onAudioPlayerStateChanged : audioPlayer.onAudioPlayerStateChangedListener)
                onAudioPlayerStateChanged.onAudioStart();
            audioPlayer.isAudioProcessing = false;
        }
    }

    private MediaPlayer mediaPlayer;

    private void createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    boolean isPlayerClosed;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
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

    public final void onPause() {

    }

    public final void onDestroy() {

    }
}
