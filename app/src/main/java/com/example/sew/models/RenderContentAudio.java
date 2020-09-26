package com.example.sew.models;

import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class RenderContentAudio extends BaseAudioContent {
    private String id;
    private String authorName;
    private String audioUrl;
    private String imageUrl;
    private String audioSlug;
    private JSONObject jsonObject;

    public RenderContentAudio(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        authorName = optString(jsonObject, "AN");
        imageUrl = optString(jsonObject, "IU");
        audioUrl = optString(jsonObject, "AU");
        audioSlug = optString(jsonObject, "ASS");
    }

    @Override
    public String getAudioUrl() {
        return String.format(MyService.getMediaURL()+"/Images/SiteImages/Audio/%s.mp3", getId());

    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public String getAudioTitle() {
        return authorName;
    }

    public String getId() {
        return id;
    }

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
