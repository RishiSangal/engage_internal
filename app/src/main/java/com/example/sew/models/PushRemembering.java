package com.example.sew.models;

import org.json.JSONObject;

public class PushRemembering extends BaseModel {
    JSONObject jsonObject;

    private String dayType;//
    private String poetId;//
    private String poetName;//
    private String poetSlug;//
    private String poetUrl;//
    private String imageUrl;//
    private String entityYearRange;//

    public PushRemembering(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        dayType = optString(jsonObject, "DayType");
        poetId = optString(jsonObject, "PoetId");
        poetName = optString(jsonObject, "PoetName");
        poetSlug = optString(jsonObject, "PoetSlug");
        poetUrl = optString(jsonObject, "PoetUrl");
        imageUrl = optString(jsonObject, "ImageUrl");
        entityYearRange = optString(jsonObject, "EntityYearRange");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getDayType() {
        return dayType;
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

    public String getPoetUrl() {
        return poetUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getEntityYearRange() {
        return entityYearRange;
    }
}
