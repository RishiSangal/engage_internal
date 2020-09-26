package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;

import org.json.JSONObject;

public class HomeProseCollection extends BaseModel implements Comparable<HomeProseCollection> {
    JSONObject jsonObject;
    private String id;
    private String name;
    private String sEO_Slug;
    private String imageUrl;
    private String contentTypeId;
    private String contentTypeName;
    private String contentTypeSlug,
            favoriteCount,
            shareCount,
            category,
            poetId;
    private int sequence;

    public HomeProseCollection(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        name = optString(jsonObject, "N");
        sEO_Slug = optString(jsonObject, "S");
        imageUrl = optString(jsonObject, "IU");
        contentTypeId = optString(jsonObject, "TI");
        contentTypeName = optString(jsonObject, "TN");
        contentTypeSlug = optString(jsonObject, "TS");
        favoriteCount = optString(jsonObject, "FC");
        shareCount = optString(jsonObject, "SC");
        if (jsonObject.has("T"))
            category = optString(jsonObject, "T");
        else
            category = optString(jsonObject, "CC");
        poetId = optString(jsonObject, "PI");
        sequence = MyHelper.convertToInt(optString(jsonObject, "CS"));
    }


    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getsEO_Slug() {
        return sEO_Slug;
    }

    public String getImageUrl() {
        return imageUrl;
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

    private int getSequence() {
        return sequence;
    }

    @Override
    public int compareTo(HomeProseCollection o) {
        return getSequence() - o.getSequence();
    }
}
