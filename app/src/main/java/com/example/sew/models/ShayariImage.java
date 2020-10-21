package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.common.MyConstants;
import com.example.sew.common.Utils;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONException;
import org.json.JSONObject;

public class ShayariImage extends BaseOtherFavModel {
    private String id;
    private String poetId;
    private String poetNameEN;
    private String poetNameHI;
    private String poetNameUR;
    private String coupletId;
    private boolean isSher;
    private String dateCreated;
    private int languageCode;
    private String shortUrlIndex;
    private String shortUrl;
    private int favoriteCount;
    private int shareCount;
    private JSONObject jsonObject;
    private String imageLocalSavedPath;
    private String slug;
    private String shareUrlEN;
    private String shareUrlHI;
    private String shareUrlUR;
    private String FD;

    /*
    "UE":"http://rek.ht/a/07ps",
    "UH":"http://rek.ht/a/07ps/2",
    "UU":"http://rek.ht/a/07ps/3",
    "FC":0,
    "SC":0
     */
    public ShayariImage(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        poetId = optString(jsonObject, "PI");
        if (jsonObject.has("N"))
            poetNameEN = poetNameHI = poetNameUR = optString(jsonObject, "N");
        else {
            poetNameEN = optString(jsonObject, "NE");
            poetNameHI = optString(jsonObject, "NH");
            poetNameUR = optString(jsonObject, "NU");
        }
        coupletId = optString(jsonObject, "CI");
        isSher = optString(jsonObject, "IS").equalsIgnoreCase("true");
        dateCreated = optString(jsonObject, "D");
        languageCode = MyHelper.convertToInt(optString(jsonObject, "L"));
        shortUrlIndex = optString(jsonObject, "SI");
        shortUrl = optString(jsonObject, "SU");

        slug = optString(jsonObject, "S");
        if (TextUtils.isEmpty(slug))
            slug = optString(jsonObject, "PS");
        favoriteCount = MyHelper.convertToInt(optString(jsonObject, "FC"));
        shareCount = MyHelper.convertToInt(optString(jsonObject, "SC"));
        imageLocalSavedPath = jsonObject.optString("imageLocalSavedPath");
        shareUrlEN = optString(jsonObject, "UE");
        shareUrlHI = optString(jsonObject, "UH");
        shareUrlUR = optString(jsonObject, "UU");
        FD = optString(jsonObject, "FD");

    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getImageUrl() {
        return String.format(MyConstants.getShayariImageUrl(), getSlug());
    }

    public String getId() {
        return id;
    }

    @Override
    public String getContentTypeId() {
        return MyConstants.FAV_IMAGE_SHAYARI_CONTENT_TYPE_ID;
    }

    public String getPoetId() {
        return poetId;
    }

    public String getPoetName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return poetNameEN;
            case HINDI:
                return poetNameHI;
            case URDU:
                return poetNameUR;
        }
        return poetNameEN;
    }

    public String getShareUrl() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return shareUrlEN;
            case HINDI:
                return shareUrlHI;
            case URDU:
                return shareUrlUR;
        }
        return shareUrlEN;
    }

    public String getCoupletId() {
        return coupletId;
    }

    public boolean isSher() {
        return isSher;
    }

    public String getDateCreated() {
        if (FD.isEmpty())
            return Utils.getCurrentFM();
        else
            return FD;
    }

    public int getLanguageCode() {
        return languageCode;
    }

    public String getShortUrlIndex() {
        return shortUrlIndex;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getSlug() {
        return slug;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setLocalPath(String imageLocalSavedPath) {
        this.imageLocalSavedPath = imageLocalSavedPath;
        updateKey(jsonObject, "imageLocalSavedPath", imageLocalSavedPath);
    }

    public String getImageLocalSavedPath() {
        return imageLocalSavedPath;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !(o instanceof ShayariImage))
            return false;
        ShayariImage shayariImage = (ShayariImage) o;
        return getId().equalsIgnoreCase(shayariImage.getId());
    }

    @Override
    public int hashCode() {
        return getId().toUpperCase().hashCode();
    }

    public void updatePushShayariImage(PushNotification pushNotification){
        try {
            jsonObject.put("I",pushNotification.getImageShayari().getImageId());
            jsonObject.put("CI",pushNotification.getImageShayari().getContentId());
            jsonObject.put("PS",pushNotification.getImageShayari().getPictureSlug());
            jsonObject.put("IF",pushNotification.getImageShayari().getImageUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
