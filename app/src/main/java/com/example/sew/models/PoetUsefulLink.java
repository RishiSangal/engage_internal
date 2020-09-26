package com.example.sew.models;

import org.json.JSONObject;

public class PoetUsefulLink extends BaseModel {

    private String urlType;
    private String url;
    private String displayText;

    private JSONObject jsonObject;

    public PoetUsefulLink(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        urlType = optString(jsonObject, "UT");
        url = optString(jsonObject, "UU");
        displayText = optString(jsonObject, "DT");
    }

    public String getUrlType() {
        return urlType;
    }

    public String getUrl() {
        return url;
    }

    public String getDisplayText() {
        return displayText;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
