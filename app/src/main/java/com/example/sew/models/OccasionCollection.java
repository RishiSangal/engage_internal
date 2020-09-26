package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class OccasionCollection extends BaseModel {

    private String sherCollectionId;
    private String imageUrl;
    private String title;
    private String description;
    private String favoriteCount;
    private String shareCount;
    private boolean haveImage;
    private String shareURLEng;
    private String shareURLHindi;
    private String shareURLUrdu;
    JSONObject jsonObject;

    public OccasionCollection(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        sherCollectionId = optString(jsonObject, "I");
        imageUrl = optString(jsonObject, "Sl");
        title = optString(jsonObject, "ON");
        description = optString(jsonObject, "OD");
        favoriteCount = optString(jsonObject, "FC");
        shareCount = optString(jsonObject, "SC");
        haveImage = optString(jsonObject, "HI").contentEquals("true");
        shareURLEng= optString(jsonObject,"UE");
        shareURLHindi= optString(jsonObject,"UH");
        shareURLUrdu= optString(jsonObject,"UU");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getImageUrl() {
        return String.format(MyConstants.getCollectionImageUrl(), imageUrl);
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
    public String getSherCollectionId() {
        return sherCollectionId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public String getShareCount() {
        return shareCount;
    }

    public boolean isHaveImage() {
        return haveImage;
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
