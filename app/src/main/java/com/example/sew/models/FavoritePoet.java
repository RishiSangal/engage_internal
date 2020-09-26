package com.example.sew.models;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class FavoritePoet extends BaseOtherFavModel {
    private String id;
    private String poetNameEnglish;
    private String poetNameHindi;
    private String poetNameUrdu;
    private String seoSlug;
    private boolean haveImage;
    private String imageUrl;
    private String entityYearRange;
    private String shortUrlEnglish;
    private String shortUrlHindi;
    private String shortUrlUrdu;
    JSONObject jsonObject;

    public FavoritePoet(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        poetNameEnglish = optString(jsonObject, "NE");
        poetNameHindi = optString(jsonObject, "NH");
        poetNameUrdu = optString(jsonObject, "NU");
        seoSlug = optString(jsonObject, "S");
        haveImage = jsonObject.optBoolean("HI", false);
        imageUrl = optString(jsonObject, "IU");
        entityYearRange = optString(jsonObject, "EY");
        shortUrlEnglish = optString(jsonObject, "UE");
        shortUrlHindi = optString(jsonObject, "UH");
        shortUrlUrdu = optString(jsonObject, "UU");
    }


    public String getId() {
        return id;
    }

    @Override
    public String getContentTypeId() {
        return MyConstants.FAV_POET_CONTENT_TYPE_ID;
    }

    public String getPoetNameEnglish() {
        return poetNameEnglish;
    }

    public String getPoetNameHindi() {
        return poetNameHindi;
    }

    public String getPoetNameUrdu() {
        return poetNameUrdu;
    }

    public String getSeoSlug() {
        return seoSlug;
    }

    public boolean isHaveImage() {
        return haveImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getEntityYearRange() {
        return entityYearRange;
    }

    public String getShortUrlEnglish() {
        return shortUrlEnglish;
    }

    public String getShortUrlHindi() {
        return shortUrlHindi;
    }

    public String getShortUrlUrdu() {
        return shortUrlUrdu;
    }


    public String getName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getPoetNameEnglish();
            case HINDI:
                return getPoetNameHindi();
            case URDU:
                return getPoetNameUrdu();
        }
        return getPoetNameEnglish();
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !(o instanceof FavoritePoet))
            return false;
        FavoritePoet favoritePoet = (FavoritePoet) o;
        return getId().equalsIgnoreCase(favoritePoet.getId());
    }
    @Override
    public int hashCode() {
        return getId().toUpperCase().hashCode();
    }
}
