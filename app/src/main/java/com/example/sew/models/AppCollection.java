package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class AppCollection extends BaseModel {
    JSONObject jsonObject;
    private String id;
    private String name;
    private String sequence;
    private String category;
    private String sEO_Slug;
    private String poetId;
    private String contentTypeId;
    private String contentTypeName;
    private String contentTypeSlug;
    private String imageUrl;
    private String favoriteCount;
    private String shareCount;
    private String shareURLEng;
    private String shareURLHindi;
    private String shareURLUrdu;
    public AppCollection(JSONObject jsonObject) {
        if (jsonObject == null)

            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        name = optString(jsonObject, "CN");
        sequence = optString(jsonObject, "CS");
        category = optString(jsonObject, "CC");
        sEO_Slug = optString(jsonObject, "S");
        poetId = optString(jsonObject, "PI");
        contentTypeId = optString(jsonObject, "TI");
        contentTypeName = optString(jsonObject, "TN");
        contentTypeSlug = optString(jsonObject, "TS");
        imageUrl = optString(jsonObject, "IU");
        favoriteCount = optString(jsonObject, "FC");
        shareCount = optString(jsonObject, "SC");
        shareURLEng= optString(jsonObject,"UE");
        shareURLHindi= optString(jsonObject,"UH");
        shareURLUrdu= optString(jsonObject,"UU");
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

    public String getSequence() {
        return sequence;
    }

    public String getCategory() {
        return category;
    }

    public String getsEO_Slug() {
        return sEO_Slug;
    }

    public String getPoetId() {
        return poetId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public String getShareCount() {
        return shareCount;
    }

//    public String getShareUrl() {
//        return String.format("https://www.rekhta.org/collections/%s%s", getsEO_Slug(),
//                MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH ? "" : MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI ? "?lang=hi" : "?lang=ur");
//    }

    public Enums.PROSE_SHAYARI_CATEGORY getProseShayariCategory() {
        if ("poet".equalsIgnoreCase(getCategory()))
            return Enums.PROSE_SHAYARI_CATEGORY.POET;
        return Enums.PROSE_SHAYARI_CATEGORY.COLLECTION;
    }
    public String getShareUrl() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getShareURLEng();
            case HINDI:
                return getShareURLHindi();
            case URDU:
                return getShareURLUrdu();
        }
        return shareURLEng;
    }
    public String getShareURLEng() {
        return shareURLEng;
    }

    public String getShareURLHindi() {
        return shareURLHindi;
    }

    public String getShareURLUrdu() {
        return shareURLUrdu;
    }
}
