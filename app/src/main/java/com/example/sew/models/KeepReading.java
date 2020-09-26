package com.example.sew.models;

import org.json.JSONObject;

import java.util.ArrayList;

public class KeepReading extends BaseSherModel {
    private String id;
    private String contentTitle,
            contentSlug,
            contentType,
            totalFavorite;
    private ArrayList<Para> renderContent;
    JSONObject jsonObject;

    public KeepReading(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        this.id = optString(jsonObject, "CI");
        this.contentTitle = optString(jsonObject, "KRT");
        contentSlug = optString(jsonObject, "CS");
        contentType = optString(jsonObject, "CT");
        renderContent = getPara(optString(jsonObject, "RT"));
        totalFavorite = optString(jsonObject, "TF");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getId() {
        return id;
    }


    public String getContentTitle() {
        return contentTitle;
    }


    public String getContentSlug() {
        return contentSlug;
    }

    public String getContentType() {
        return contentType;
    }

    public ArrayList<Para> getRenderContent() {
        return renderContent;
    }

    public String getTotalFavorite() {
        return totalFavorite;
    }
}
