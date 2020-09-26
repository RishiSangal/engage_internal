package com.example.sew.models;

import com.example.sew.adapters.BaseViewHolder;

import org.json.JSONObject;

public class RenderContentVideo extends BaseVideoContent {
    private String youtubeId;
    private String authorName;
    private String audioUrl;
    private String imageUrl;
    private String audioSlug;
    private JSONObject jsonObject;

    public RenderContentVideo(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        youtubeId = optString(jsonObject, "YI");
        authorName = optString(jsonObject, "AN");
        imageUrl = optString(jsonObject, "IU");
        audioUrl = optString(jsonObject, "AU");
        audioSlug = optString(jsonObject, "ASS");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getAudioTitle() {
        return authorName;
    }

    @Override
    public String getYoutubeUrl() {
        return null;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    @Override
    public String getAuthorName() {
        return authorName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAudioSlug() {
        return audioSlug;
    }
}
