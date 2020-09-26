package com.example.sew.models;

import androidx.annotation.IntDef;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class PlayingAudioItem extends BaseModel {
    private int currentPlayingPosition;
    private BaseAudioContent audioContent;
    public static final int PLAYING_STATE_PLAYING = 1;
    public static final int PLAYING_STATE_PAUSE = 0;
    JSONObject jsonObject;

    @IntDef({PLAYING_STATE_PLAYING, PLAYING_STATE_PAUSE})
    public @interface PlayingState {
    }

    @PlayingState
    private int currentPlayingState;

    public PlayingAudioItem(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        currentPlayingPosition = MyHelper.convertToInt(optString(jsonObject, "CPP"));
        currentPlayingState = MyHelper.convertToInt(optString(jsonObject, "CPS"));
        audioContent = new AudioContent(jsonObject.optJSONObject("AC"));
    }

    public int getCurrentPlayingPosition() {
        return currentPlayingPosition;
    }

    public void setCurrentPlayingPosition(int currentPlayingPosition) {
        this.currentPlayingPosition = currentPlayingPosition;
        updateKey(getJsonObject(), "CPP", currentPlayingPosition);
    }

    public BaseAudioContent getAudioContent() {
        return audioContent;
    }

    public void setAudioContent(BaseAudioContent audioContent) {
        this.audioContent = audioContent;
        updateKey(getJsonObject(), "AC", audioContent.getJsonObject());
    }

    public int getCurrentPlayingState() {
        return currentPlayingState;
    }

    public void setCurrentPlayingState(@PlayingState int currentPlayingState) {
        this.currentPlayingState = currentPlayingState;
        updateKey(getJsonObject(), "CPS", currentPlayingState);
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
