package com.example.sew.models;

import android.text.TextUtils;

import org.json.JSONObject;

public class User extends BaseModel {
    private String displayName;
    private String imageName;
    private String haveImage;
    private String id;
    private String selectedHeaderImage;
    private String token;
    private String bannerImageName;
    public User(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        displayName = optString(jsonObject, "DisplayName");
        imageName = optString(jsonObject, "ImageName");
        haveImage = optString(jsonObject, "HaveImage");
        id = optString(jsonObject, "Id");
        selectedHeaderImage = optString(jsonObject, "SelectedHeaderImage");
        bannerImageName= optString(jsonObject,"BannerImageName");
        JSONObject tokenObject = jsonObject.optJSONObject("TokenDetails");
        if (tokenObject == null)
            tokenObject = new JSONObject();
        token = tokenObject.optString("access_token");
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageName() {
        return imageName;
//        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("http"))
//            return imageName;
//        else
//            return String.format(MyConstants.getUserProfileImageUrl(), imageName);
    }

    public String getHaveImage() {
        return haveImage;
    }

    public boolean isUserImageExist() {
        return !TextUtils.isEmpty(imageName.trim());
    }

    public String getId() {
        return id;
    }

    public String getSelectedHeaderImage() {
        return selectedHeaderImage;
    }

    public String getToken() {
        return token;
    }

    public String getBannerImageName() {
        return bannerImageName;
    }
}
