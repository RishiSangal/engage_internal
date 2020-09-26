package com.example.sew.models;

import org.json.JSONObject;

public class PreviousNextContent extends BaseModel {
    private String id;
    private String poetName;
    private String contentTitle;
    private String previousTitle;
    private String nextTitle;
    private boolean isNext;

    public PreviousNextContent(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.id = optString(jsonObject, "I");
        this.poetName = optString(jsonObject, "PN");
        this.contentTitle = optString(jsonObject, "CT");
        this.previousTitle = optString(jsonObject, "PT");
        this.nextTitle = optString(jsonObject, "NT");
        isNext = optString(jsonObject, "IN").equalsIgnoreCase("true");
    }

    public String getId() {
        return id;
    }

    public String getPoetName() {
        return poetName;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public boolean isNext() {
        return isNext;
    }

    public String getPreviousTitle() {
        return previousTitle;
    }

    public String getNextTitle() {
        return nextTitle;
    }
}
