package com.example.sew.models;

import org.json.JSONObject;

public class HomeLookingMore extends BaseModel {
    JSONObject jsonObject;
    private String id;
    private String title;
    private String subTitle;
    private String sEO_Slug;
    private String poetId;
    private String poetName;
    private String poetSlug;
    private String contentTypeId;
    private String contentTypeName;
    private String contentTypeSlug;
    private String audioCount;
    private String videoCount;
    private String favoriteCount;

    public HomeLookingMore(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        title = optString(jsonObject, "T");
        subTitle = optString(jsonObject, "ST");
        sEO_Slug = optString(jsonObject, "S");
        poetId = optString(jsonObject, "PI");
        poetName = optString(jsonObject, "PN");
        poetSlug = optString(jsonObject, "PS");
        contentTypeId = optString(jsonObject, "TI");
        contentTypeName = optString(jsonObject, "TN");
        contentTypeSlug = optString(jsonObject, "TS");
        audioCount = optString(jsonObject, "AC");
        videoCount = optString(jsonObject, "VC");
        favoriteCount = optString(jsonObject, "FC");

    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getsEO_Slug() {
        return sEO_Slug;
    }

    public String getPoetId() {
        return poetId;
    }

    public String getPoetName() {
        return poetName;
    }

    public String getPoetSlug() {
        return poetSlug;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public String getContentTypeName() {
        return contentTypeName;
    }

    public String getContentTypeSlug() {
        return contentTypeSlug;
    }

    public String getAudioCount() {
        return audioCount;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }
}
