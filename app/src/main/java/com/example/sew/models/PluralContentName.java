package com.example.sew.models;

import org.json.JSONObject;

public class PluralContentName extends BaseModel {
    JSONObject jsonObject;
    private String pluralName;
    private String contentSlug;
    public PluralContentName(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        this.pluralName = optString(jsonObject, "PN");
        this.contentSlug = optString(jsonObject, "CS");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getContentSlug() {
        return contentSlug;
    }

    public String getPluralName() {
        return pluralName;
    }
}
