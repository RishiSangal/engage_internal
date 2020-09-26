package com.example.sew.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.R;
import com.example.sew.adapters.YoutubeHandlerListAdapter;
import com.example.sew.helpers.MyService;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONArray;
import org.json.JSONException;

public class YoutubeHandler extends BaseActivity implements YouTubePlayer.OnFullscreenListener {

   // private static final String API_KEY = "AIzaSyAucCMhVmsY5JZRRl2JPsvRdnWeThfOHx8";
    @SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    private static String VIDEO_ID = "M7g3tyqZADY";
    boolean isVideoPlaying = false;

    static JSONArray videoObj;
    static TextView currentAuthorName;
    TextView currentTitle;
    ImageView closeYouTubedownArrow;
    String cActivity = null;
    private static YouTubePlayer youtubePlayer;
    private boolean mAutoRotation;
    private String videoId;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private int fullscreen;
    private String videoTitle;
    private static String mvideoTitle;

    private NestedScrollView scroll;
    private FrameLayout.LayoutParams parameter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_handler);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        scroll = findViewById(R.id.nestedYoutube);
        scroll.setNestedScrollingEnabled(true);
        scroll.setSmoothScrollingEnabled(true);
        scroll.setFillViewport(true);
        mAutoRotation = Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        Intent intent = getIntent();
        cActivity = intent.getExtras().getString("cActivity");
        String dash = "Dashboard";
        assert cActivity != null;
        if (!cActivity.equals(dash)) {
            try {
                videoObj = new JSONArray(intent.getExtras().getString("videoObj"));
                videoTitle = intent.getExtras().getString("videoTitle");
                mvideoTitle = intent.getExtras().getString("videoTitle");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cActivity != null) {
                currentAuthorName = (TextView) findViewById(R.id.currentVideoAuthorName);
                currentTitle = (TextView) findViewById(R.id.currentVideoTitle);
                try {
                    openVideo(videoObj.getJSONObject(0).getString("YI"), 0);
                    currentAuthorName.setText(videoObj.getJSONObject(0).getString("AN"));
                    currentTitle.setText(videoTitle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RecyclerView listView = findViewById(R.id.videoList);
                YoutubeHandlerListAdapter videoAdapter = new YoutubeHandlerListAdapter(YoutubeHandler.this, videoObj, 0);
                listView.setLayoutManager(new LinearLayoutManager(YoutubeHandler.this));
                listView.setAdapter(videoAdapter);

            }
        } else {
            videoId = intent.getExtras().getString("videoId");
            findViewById(R.id.dividerYou).setVisibility(View.GONE);
            findViewById(R.id.authLay).setVisibility(View.GONE);
            findViewById(R.id.videoList).setVisibility(View.GONE);
            findViewById(R.id.videolayout).setBackgroundColor(getResources().getColor(android.R.color.transparent));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            findViewById(R.id.cardPlayYoutube).setLayoutParams(params);
            openVideo(videoId, 0);
        }
        closeYouTubedownArrow = (ImageView) findViewById(R.id.spinnerToCloseImg);
        closeYouTubedownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (youtubePlayer != null) {
                youtubePlayer.setFullscreen(false);
                youtubePlayer.pause();
                youtubePlayer.release();
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void openVideo(final String videoID, final int position) {

                if (youTubePlayerFragment == null) {
                    youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.youtube_layout, youTubePlayerFragment).commit();
                youTubePlayerFragment.initialize(MyService.getYouTubeKey(), new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                        youtubePlayer = player;
                        player.setOnFullscreenListener(YoutubeHandler.this);
                        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        player.setShowFullscreenButton(true);

                        if (!wasRestored) {
                            playVideo(videoID, position);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                        String errorMessage = error.toString();
                        Toast.makeText(YoutubeHandler.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.d("errorMessage:", errorMessage);
                    }

                });

    }

    public static void playVideo(String videoId, int position) {
        if (youtubePlayer != null) {
            youtubePlayer.loadVideo(videoId);
            try {
                if ((videoObj == null)) {
                } else {
                    currentAuthorName.setText(videoObj.getJSONObject(position).getString("AN"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void checkOrientaiotn(Intent intent) {

        startActivityForResult(intent, 100);
    }

    @Override
    public void onFullscreen(boolean fullsize) {
        if (fullsize) {
            setRequestedOrientation(LANDSCAPE_ORIENTATION);
            fullscreen = 1;

        } else {
            setRequestedOrientation(PORTRAIT_ORIENTATION);
            fullscreen = 2;
        }
    }

    @Override
    public void onBackPressed() {
        if (fullscreen == 1) {
            if (this.youtubePlayer != null) {
                super.onBackPressed();
                youtubePlayer.setFullscreen(false);
            }
//        } else
        } else {
            super.onBackPressed();

        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
