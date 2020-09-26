package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class HomeSherCollection extends BaseModel {
    JSONObject jsonObject;
    private String id;
    private String name;
    private String sEO_Slug;
    private String imageUrl;
    private String type,
            favoriteCount,
            shareCount;

    public HomeSherCollection(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        name = optString(jsonObject, "N");
        sEO_Slug = optString(jsonObject, "S");
        imageUrl = optString(jsonObject, "IU");
        favoriteCount = optString(jsonObject, "FC");
        shareCount = optString(jsonObject, "SC");
        type = optString(jsonObject, "T");
    }

    public void setId(String id) {
        this.id = id;
        updateKey(jsonObject, "I", id);
    }

    public void setName(String name) {
        this.name = name;
        updateKey(jsonObject, "N", name);
    }

    public void setsEO_Slug(String sEO_Slug) {
        this.sEO_Slug = sEO_Slug;
        updateKey(jsonObject, "S", sEO_Slug);
    }

    public void setType(String type) {
        this.type = type;
        updateKey(jsonObject, "T", type);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOccasionImageUrl() {
        return String.format(MyService.getMediaURL() + "/images/Cms/CollectionSeries/Occasions/%s_Medium.png", getId());
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

    public String getType() {
        return type;
    }

    public Enums.SHER_COLLECTION_TYPE getSherCollectionType() {
        if ("t20".equalsIgnoreCase(getType()))
            return Enums.SHER_COLLECTION_TYPE.TOP_20;
        else if ("occasion".equalsIgnoreCase(getType()))
            return Enums.SHER_COLLECTION_TYPE.OCCASIONS;
        else
            return Enums.SHER_COLLECTION_TYPE.OTHER;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public String getShareCount() {
        return shareCount;
    }
}
