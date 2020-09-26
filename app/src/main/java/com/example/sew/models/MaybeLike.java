package com.example.sew.models;

import com.example.sew.helpers.MyService;

import org.json.JSONObject;

import java.util.ArrayList;

public class MaybeLike extends BaseSherModel {
    private String id;
    private String contentTitle,
            poetName,
            poetSlug,
            contentSlug,
            imageUrl,
            typeSlug,
            renderingFormat,
            totalFavorite;
    private ArrayList<Para> renderContent;
    JSONObject jsonObject;

    public MaybeLike(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        this.id = optString(jsonObject, "CI");
        this.poetName = optString(jsonObject, "PN");
        this.contentTitle = optString(jsonObject, "CT");
        poetName = optString(jsonObject, "PN");
        poetSlug = optString(jsonObject, "PS");
        contentSlug = optString(jsonObject, "CS");
        imageUrl = optString(jsonObject, "IU");
        typeSlug = optString(jsonObject, "TS");
        renderingFormat = optString(jsonObject, "RF");
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

    public String getPoetName() {
        return poetName;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public String getPoetSlug() {
        return poetSlug;
    }

    public String getContentSlug() {
        return contentSlug;
    }

    public String getImageUrl() {
        return String.format("%s%s", imageUrl.startsWith("http") ? "" : MyService.getMediaURL(), this.imageUrl);
    }

    public String getTypeSlug() {
        return typeSlug;
    }

    public String getRenderingFormat() {
        return renderingFormat;
    }

    public ArrayList<Para> getRenderContent() {
        return renderContent;
    }

    public String getTotalFavorite() {
        return totalFavorite;
    }
}
