package com.example.sew.models;

import org.json.JSONObject;

public class HomeT20Series extends BaseModel {
    JSONObject jsonObject;
    private String id;
    private String name_En;
    private String name_Hi;
    private String name_Ur;
    private String description_En;
    private String description_Hi;
    private String description_Ur;
    private String launchDate;
    private String isPoetBased;
    private String listId;
    private String sEO_Slug;
    private String title_En;
    private String title_Hi;
    private String title_Ur;
    private String poetId;
    private String mediumImageId;

    public HomeT20Series(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        name_En = optString(jsonObject, "NE");
        name_Hi = optString(jsonObject, "NH");
        name_Ur = optString(jsonObject, "NU");
        description_En = optString(jsonObject, "DE");
        description_Hi = optString(jsonObject, "DH");
        description_Ur = optString(jsonObject, "DU");
        launchDate = optString(jsonObject, "LD");
        isPoetBased = optString(jsonObject, "IP");
        listId = optString(jsonObject, "LI");
        sEO_Slug = optString(jsonObject, "S");
        title_En = optString(jsonObject, "TE");
        title_Hi = optString(jsonObject, "TH");
        title_Ur = optString(jsonObject, "TU");
        poetId = optString(jsonObject, "PI");
        mediumImageId = optString(jsonObject, "MI");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public String getName_En() {
        return name_En;
    }

    public String getName_Hi() {
        return name_Hi;
    }

    public String getName_Ur() {
        return name_Ur;
    }

    public String getDescription_En() {
        return description_En;
    }

    public String getDescription_Hi() {
        return description_Hi;
    }

    public String getDescription_Ur() {
        return description_Ur;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public String getIsPoetBased() {
        return isPoetBased;
    }

    public String getListId() {
        return listId;
    }

    public String getsEO_Slug() {
        return sEO_Slug;
    }

    public String getTitle_En() {
        return title_En;
    }

    public String getTitle_Hi() {
        return title_Hi;
    }

    public String getTitle_Ur() {
        return title_Ur;
    }

    public String getPoetId() {
        return poetId;
    }

    public String getMediumImageId() {
        return mediumImageId;
    }
}
