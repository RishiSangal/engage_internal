package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;

import org.json.JSONObject;

import java.util.Locale;

public class HomeTopPoet extends BaseModel {
    JSONObject jsonObject;
    private String entityId;
    private String name;
    private String sEO_Slug;
    private String haveImage;
    private String fromDate;
    private String toDate;
    private String imageURL;

    public HomeTopPoet(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        entityId = optString(jsonObject, "PI");
        name = optString(jsonObject, "PN");
        sEO_Slug = optString(jsonObject, "PS");
        haveImage = optString(jsonObject, "HI");
        fromDate = optString(jsonObject, "FD");
        toDate = optString(jsonObject, "TD");
        imageURL= optString(jsonObject,"IU");
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getName() {
        return name;
    }

    public String getsEO_Slug() {
        return sEO_Slug;
    }

    public String getHaveImage() {
        return haveImage;
    }

    public String getPoetTenure() {
        if(TextUtils.isEmpty(getToDate())){
            return String.format("%s ", getFromDate());
        }else
            return String.format("%s - %s", getFromDate(), getToDate());
    }
    public String getPoetImageUrl(){
        if (TextUtils.isEmpty(getEntityId()))
            return "";
        return String.format(Locale.getDefault(), MyConstants.getPoetImageUrlSmall(), getEntityId());
    }

    public String getImageURL() {
        return imageURL;
    }
}
