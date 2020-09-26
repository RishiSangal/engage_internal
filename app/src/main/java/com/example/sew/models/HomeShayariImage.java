package com.example.sew.models;

import com.example.sew.common.MyConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeShayariImage extends BaseModel {
    JSONObject jsonObject;
    private String id;
    private String poetId;
    private String name;
    private String coupletId;
    private String isSher;
    private String tag1;
    private String sEO_Slug;
    private String shortUrlIndex;
    private String shortUrl_En;
    private String shortUrl_Hi;
    private String shortUrl_Ur;
    private ArrayList<HomeImageTag> homeImageTags;

    public HomeShayariImage(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        poetId = optString(jsonObject, "PI");
        if (jsonObject.has("PN"))
            name = optString(jsonObject, "PN");
        else
            name = optString(jsonObject, "N");
        coupletId = optString(jsonObject, "CI");
        isSher = optString(jsonObject, "IS");
        tag1 = optString(jsonObject, "TG");
        sEO_Slug = optString(jsonObject, "PS");
        shortUrlIndex = optString(jsonObject, "SI");
        shortUrl_En = optString(jsonObject, "SE");
        shortUrl_Hi = optString(jsonObject, "SH");
        shortUrl_Ur = optString(jsonObject, "SU");
        JSONArray jsonArray = jsonObject.optJSONArray("ITG");
        if (jsonArray == null)
            jsonArray = new JSONArray();
        int size = jsonArray.length();
        homeImageTags = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            homeImageTags.add(new HomeImageTag(jsonArray.optJSONObject(i)));
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public String getPoetId() {
        return poetId;
    }

    public String getName() {
        return name;
    }

    public String getCoupletId() {
        return coupletId;
    }

    public String getIsSher() {
        return isSher;
    }

    public String getTag1() {
        return tag1;
    }

    public String getsEO_Slug() {
        return sEO_Slug;
    }

    public String getShortUrlIndex() {
        return shortUrlIndex;
    }

    public String getShortUrl_En() {
        return shortUrl_En;
    }

    public String getShortUrl_Hi() {
        return shortUrl_Hi;
    }

    public String getShortUrl_Ur() {
        return shortUrl_Ur;
    }

    public String getImageUrl() {
        return String.format(MyConstants.getShayariImageUrl(), getsEO_Slug());
    }

    public ArrayList<HomeImageTag> getHomeImageTags() {
        return homeImageTags;
    }
}
