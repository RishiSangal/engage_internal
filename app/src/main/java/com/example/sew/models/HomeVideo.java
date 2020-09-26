package com.example.sew.models;

import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class HomeVideo extends BaseModel {
    JSONObject jsonObject;
    private String youtube_Id;
    private String entityName;
    private String entityUrl;
    private String poetId;
    private String videoTitle;
    private String imageUrl;
    private String shortUrl_En;
    private String shortUrl_Hi;
    private String shortUrl_Ur;

    public HomeVideo(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        youtube_Id = optString(jsonObject, "YI");
        entityName = optString(jsonObject, "PN");
        entityUrl = optString(jsonObject, "PU");
        poetId = optString(jsonObject, "PI");
        videoTitle = optString(jsonObject, "VT");
        imageUrl = optString(jsonObject, "IU");
        shortUrl_En = optString(jsonObject, "SE");
        shortUrl_Hi = optString(jsonObject, "SH");
        shortUrl_Ur = optString(jsonObject, "SU");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getYoutube_Id() {
        return youtube_Id;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityUrl() {
        return entityUrl;
    }

    public String getPoetId() {
        return poetId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getImageUrl() {
        return String.format("%s%s", imageUrl.startsWith("http") ? "" : MyService.getMediaURL(), this.imageUrl);
    }

    public String getShortUrl_En() {
        return shortUrl_En;
    }

    public String getShortUrl_Hi() {
        return shortUrl_Hi;
    }

    public String getShortUrl_Ur() {
        return shortUrl_Ur;
    }
}
