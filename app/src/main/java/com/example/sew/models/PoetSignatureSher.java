package com.example.sew.models;

import org.json.JSONObject;

import java.util.ArrayList;

public class PoetSignatureSher extends BaseSherModel {

    private final String renderingFormat;
    private final String parentSlug;
    private final String parentTypeSlug;
    private final ArrayList<Para> renderText;
    private String id;
    private String title;
    private String subTitle;
    private String seoSlug;

    public PoetSignatureSher(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        id = optString(jsonObject, "I");//
        title = optString(jsonObject, "T");//
        subTitle = optString(jsonObject, "ST");
        renderingFormat = optString(jsonObject, "RF");
        parentSlug = optString(jsonObject, "PS");
        parentTypeSlug = optString(jsonObject, "PTS");
        seoSlug = optString(jsonObject, "S");
        renderText = getPara(optString(jsonObject, "CR"));
    }

    public String getRenderingFormat() {
        return renderingFormat;
    }

    public String getParentSlug() {
        return parentSlug;
    }

    public String getParentTypeSlug() {
        return parentTypeSlug;
    }

    public ArrayList<Para> getRenderText() {
        return renderText;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getSeoSlug() {
        return seoSlug;
    }
}
