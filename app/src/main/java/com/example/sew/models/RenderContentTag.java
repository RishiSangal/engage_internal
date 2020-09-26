package com.example.sew.models;

import com.example.sew.helpers.MyHelper;

import org.json.JSONObject;

public class RenderContentTag extends BaseSherTag {
    JSONObject jsonObject;
    private String tagId;
    private String tagName;
    private String tagSlug;

    public RenderContentTag(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        tagId = optString(jsonObject, "TI");
        if (jsonObject.has("TN"))
            tagName = optString(jsonObject, "TN");
        else
            tagName = optString(jsonObject, "N");
        tagSlug = optString(jsonObject, "TS");

    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public String getTagId() {
        return tagId;
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    public String getTagSlug() {
        return tagSlug;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
