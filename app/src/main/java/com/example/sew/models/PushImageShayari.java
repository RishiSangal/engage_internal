package com.example.sew.models;

import org.json.JSONObject;

public class PushImageShayari extends BaseModel {
    JSONObject jsonObject;
    private String imageId;//
    private String contentId;//
    private String pictureSlug;//
    private String imageUrl;//

    public PushImageShayari(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        imageId = optString(jsonObject, "I");
        contentId = optString(jsonObject, "CI");
        pictureSlug = optString(jsonObject, "PS");
        imageUrl = optString(jsonObject, "IF");
    }



    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getImageId() {
        return imageId;
    }

    public String getContentId() {
        return contentId;
    }

    public String getPictureSlug() {
        return pictureSlug;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
