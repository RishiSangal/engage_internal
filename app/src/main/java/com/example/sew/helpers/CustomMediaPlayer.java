package com.example.sew.helpers;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;

import com.example.sew.models.BaseAudioContent;
import com.example.sew.models.RenderContentAudio;

import java.io.IOException;
import java.util.ArrayList;

public class CustomMediaPlayer {
    private MediaPlayer mediaPlayer;
    private static CustomMediaPlayer customMediaPlayer;

    private int currentPosition;

    public interface OnMusicStateChangeListener {
        void onAudioPause();

        void onAudioStart();

        void onAudioSelected(RenderContentAudio song);

        void onAudioProgressChanged(int currentProgress, int totalProgress);

        void onAudioPlayerDestroy();
    }

    private synchronized ArrayList<OnMusicStateChangeListener> getmOnMusicStateChangeListeners() {
        return mOnMusicStateChangeListeners;
    }

    private volatile ArrayList<OnMusicStateChangeListener> mOnMusicStateChangeListeners;
    private ArrayList<RenderContentAudio> allSongsList;

    private CustomMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mOnMusicStateChangeListeners = new ArrayList<>();
        allSongsList = new ArrayList<>();
        currentPosition = -1;
    }

    public static CustomMediaPlayer getInstance() {
        if (customMediaPlayer == null) {
            synchronized (CustomMediaPlayer.class) {
                if (customMediaPlayer == null) {
                    customMediaPlayer = new CustomMediaPlayer();
                }
            }
        }
        return customMediaPlayer;
    }

    public static void createNewInstance() {
        customMediaPlayer = null;
        getInstance();
    }

    public synchronized void removeOnMusicStateChangeListener(OnMusicStateChangeListener onMusicStateChangeListener) {
        mOnMusicStateChangeListeners.remove(onMusicStateChangeListener);
    }

    synchronized void addOnMusicStateChangeListener(OnMusicStateChangeListener onMusicStateChangeListener) {
        if (!mOnMusicStateChangeListeners.contains(onMusicStateChangeListener)) {
            mOnMusicStateChangeListeners.add(onMusicStateChangeListener);
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    private void setCurrentPosition(int position) {
        currentPosition = position % allSongsList.size();
    }

    void playMedia(int position) {
        setCurrentPosition(position);
        for (OnMusicStateChangeListener currState : mOnMusicStateChangeListeners) {
            currState.onAudioSelected(allSongsList.get(position % allSongsList.size()));
        }
    }

    void setMedia(ArrayList<RenderContentAudio> songs) {
        allSongsList.clear();
        allSongsList.addAll(songs);
    }

    public ArrayList<RenderContentAudio> getMedia() {
        return allSongsList;
    }

    private static Handler songHandler = null;

    void startNewSong(BaseAudioContent song) {
        if (songHandler != null) {
            songHandler.removeCallbacksAndMessages(null);
        }

        songHandler = new Handler();
        songHandler.postDelayed(getSongRunnable(song), 100);
        mediaPlayer.stop();
        mediaPlayer.reset();

    }

    boolean isMediaPlayerDestroyed = false;

    void destroyMediaPlayer() {
        isMediaPlayerDestroyed = true;
        if (mediaPlayer != null && (mediaPlayer.isPlaying() || isPause)) {
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
                createNewInstance();
                for (OnMusicStateChangeListener currState : mOnMusicStateChangeListeners) {
                    currState.onAudioPlayerDestroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean isPlaying() {
        try {
            return mediaPlayer != null && mediaPlayer.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    boolean isPause() {
        try {
            return mediaPlayer != null && !mediaPlayer.isPlaying() && isPause;
        } catch (Exception e) {
            return false;
        }
    }

    void playSong() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            for (OnMusicStateChangeListener currState : mOnMusicStateChangeListeners) {
                currState.onAudioStart();
            }
            new Thread(() -> {
                try {
                    while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        if (isMediaPlayerDestroyed) {
                            destroyMediaPlayer();
                            return;
                        }
                        isPause = false;
                        for (OnMusicStateChangeListener currState : mOnMusicStateChangeListeners) {
                            currState.onAudioProgressChanged(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                        }
                        Thread.sleep(1000);
                    }
                } catch (Exception ignored) {
                }
            }).start();
        }
    }

    boolean isPause = false;

    void pauseSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
            for (OnMusicStateChangeListener currState : mOnMusicStateChangeListeners) {
                currState.onAudioPause();
            }
        }
    }

    void seekTo(int msec) throws IllegalStateException {
        mediaPlayer.seekTo(msec);
    }

    public int getCurrentProgress() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getMaxProgress() {
        return mediaPlayer.getDuration();
    }

    private Runnable getSongRunnable(final BaseAudioContent currentSong) {
        return () -> {
            try {
                mediaPlayer.setDataSource(currentSong.getAudioUrl());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build());
                } else {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                    if (!isMediaPlayerDestroyed)
                        playSong();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        };
    }


}
