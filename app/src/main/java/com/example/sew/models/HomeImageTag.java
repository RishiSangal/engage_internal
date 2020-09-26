package com.example.sew.models;

import com.example.sew.helpers.MyHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeImageTag extends BaseSherTag {
    JSONObject jsonObject;
    private String tagId;
    private String tagName;
    private String tagSlug;
    private String coupletId;
    private int count;

    public HomeImageTag(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        tagId = optString(jsonObject, "TI");
        if (jsonObject.has("TN"))
            tagName = optString(jsonObject, "TN");
        else
            tagName = optString(jsonObject, "N");
        tagSlug = optString(jsonObject, "TS");
        coupletId = optString(jsonObject, "CI");
        count = MyHelper.convertToInt(optString(jsonObject, "TC"));

    }

    public static HomeImageTag getInstance(String tagId) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("TI", tagId);
            jsonObject.put("N", "");
            jsonObject.put("TS", "");
            jsonObject.put("CI", "");
            jsonObject.put("TC", "");
        } catch (JSONException ignored) {
        }
        return new HomeImageTag(jsonObject);
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

    public String getCoupletId() {
        return coupletId;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
