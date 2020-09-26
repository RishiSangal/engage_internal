package com.example.sew.models;

import org.json.JSONObject;

public class PushEvent extends BaseModel {
    JSONObject jsonObject;

    private String eventTitle;//
    private String eventDescription;//
    private String isEvent;//
    private String targetUrl;//
    private String imageUrl;//

    public PushEvent(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        eventTitle = optString(jsonObject, "ET");
        eventDescription = optString(jsonObject, "ED");
        isEvent = optString(jsonObject, "IE");
        targetUrl = optString(jsonObject, "TU");
        imageUrl = optString(jsonObject, "IU");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getIsEvent() {
        return isEvent;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
