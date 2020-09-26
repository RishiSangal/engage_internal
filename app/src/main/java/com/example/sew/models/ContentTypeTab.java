package com.example.sew.models;

import com.example.sew.helpers.MyHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentTypeTab extends BaseModel {

    JSONObject jsonObject;
    private String targetId;
    private String targetName;
    private String targetSlug;
    private boolean isHaveBannerImage;
    private int targetType;
    private String imageFile;
    private ArrayList<CumulatedContentType> cumulatedContentType;
    public ContentTypeTab(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        targetId = optString(jsonObject, "TargetId");
        targetName = optString(jsonObject, "TargetName");
        targetSlug = optString(jsonObject, "TargetSlug");
        isHaveBannerImage = optString(jsonObject, "HaveBannerImage").equalsIgnoreCase("true");
        targetType = MyHelper.convertToInt(optString(jsonObject, "TargetType"));
        imageFile= optString(jsonObject,"ImageFile");
        JSONArray cumulatedContentTypeArray = jsonObject.optJSONArray("TagList");
        if (cumulatedContentTypeArray == null)
            cumulatedContentTypeArray = new JSONArray();
        cumulatedContentType = new ArrayList<>(cumulatedContentTypeArray.length());
        for (int i = 0; i < cumulatedContentTypeArray.length(); i++)
            cumulatedContentType.add(new CumulatedContentType(cumulatedContentTypeArray.optJSONObject(i)));
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getTargetSlug() {
        return targetSlug;
    }

    public boolean isHaveBannerImage() {
        return isHaveBannerImage;
    }

    public int getTargetType() {
        return targetType;
    }

    public String getImageFile() {
        return imageFile;
    }

    public ArrayList<CumulatedContentType> getCumulatedContentType() {
        return cumulatedContentType;
    }
}
