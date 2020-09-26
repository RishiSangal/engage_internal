package com.example.sew.models;

import com.example.sew.helpers.MyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SherOfTheDay extends BaseSherModel {
    JSONObject jsonObject;
    private String contentId;//
    private String slug;//
    private String title;//
    private String poetName;//
    private ArrayList<Para> renderText;//

    public SherOfTheDay(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        contentId = optString(jsonObject, "ContentId");
        slug = optString(jsonObject, "Slug");
        title = optString(jsonObject, "Title");
        renderText = getPara(optString(jsonObject, "Render"));
        poetName = optString(jsonObject, "PoetName");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getContentId() {
        return contentId;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getPoetName() {
        return poetName;
    }

    public ArrayList<Para> getRenderText() {
        return renderText;
    }
}
