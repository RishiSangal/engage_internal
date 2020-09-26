package com.example.sew.models;

import com.example.sew.common.Enums;

import org.json.JSONObject;

public class HomeShayari extends BaseModel {
    JSONObject jsonObject;
    private String listId;
    private String name;
    private String contentTypeId;
    private String imageUrl,
            favoriteCount,
            shareCount,
            category,
            poetId;

    public HomeShayari(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        listId = optString(jsonObject, "LI");
        name = optString(jsonObject, "N");
        contentTypeId = optString(jsonObject, "TI");
        imageUrl = optString(jsonObject, "IU");
        favoriteCount = optString(jsonObject, "FC");
        shareCount = optString(jsonObject, "SC");
        category = optString(jsonObject, "T");
        poetId = optString(jsonObject, "PI");

    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getPoetId() {
        return poetId;
    }

    public String getCategory() {
        return category;
    }

    public Enums.PROSE_SHAYARI_CATEGORY getProseShayariCategory() {
        if ("poet".equalsIgnoreCase(getCategory()))
            return Enums.PROSE_SHAYARI_CATEGORY.POET;
        return Enums.PROSE_SHAYARI_CATEGORY.COLLECTION;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public String getShareCount() {
        return shareCount;
    }
}
