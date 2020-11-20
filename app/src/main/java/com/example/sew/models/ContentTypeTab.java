package com.example.sew.models;

import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentTypeTab extends BaseModel {

    JSONObject jsonObject;
    private String targetId;
    private String targetName;
    private String TargetNameEn,TargetNameHi, TargetNameUr;
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
        TargetNameEn=  optString(jsonObject,"TargetNameEn");
        TargetNameHi=  optString(jsonObject,"TargetNameHi");
        TargetNameUr=  optString(jsonObject,"TargetNameUr");
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

    public String getTargetNameEn() {
        return TargetNameEn;
    }

    public String getTargetNameHi() {
        return TargetNameHi;
    }

    public String getTargetNameUr() {
        return TargetNameUr;
    }

    public String getTitleName(){
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getTargetNameEn();
            case HINDI:
                return getTargetNameHi();
            case URDU:
                return getTargetNameUr();
        }
        return getTargetNameEn();
    }
}
