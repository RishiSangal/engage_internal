package com.example.sew.models;

import org.json.JSONObject;

public class Blogs extends BaseModel {
    JSONObject jsonObject;
    private String id;
    private String blogEnglishName;
    private String blogHindiName;
    private String blogUrduName;
    private String blogUrl;
    private String imageUrl;
    private boolean IE;

    public Blogs(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        id = optString(jsonObject, "I");
        blogEnglishName = optString(jsonObject, "BE");
        blogHindiName = optString(jsonObject, "BH");
        blogUrduName = optString(jsonObject, "BU");
        blogUrl = optString(jsonObject, "TU");
        imageUrl = optString(jsonObject, "IU");
        IE = jsonObject.optBoolean("IE");

    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public String getBlogEnglishName() {
        return blogEnglishName;
    }

    public String getBlogHindiName() {
        return blogHindiName;
    }

    public String getBlogUrduName() {
        return blogUrduName;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isIE() {
        return IE;
    }
}
