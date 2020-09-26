package com.example.sew.models;

import org.json.JSONObject;

public class PoetRelatedEntity extends BaseModel {

    private boolean isDirectRelation;
    private String entityId;
    private String entityName;
    private String entityRelation;
    private String seoSlug;
    private String entityUrl;
    private String entityImageUrl;

    private JSONObject jsonObject;

    public PoetRelatedEntity(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        entityId = optString(jsonObject, "ERI");
        entityName = optString(jsonObject, "EN");
        entityRelation = optString(jsonObject, "ER");
        isDirectRelation = optString(jsonObject, "DR").contentEquals("true");
        seoSlug = optString(jsonObject, "SS");
        entityUrl = optString(jsonObject, "EU");
        entityImageUrl = optString(jsonObject, "IU");
    }

    public boolean isDirectRelation() {
        return isDirectRelation;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityRelation() {
        return entityRelation;
    }

    public String getSeoSlug() {
        return seoSlug;
    }

    public String getEntityUrl() {
        return entityUrl;
    }

    public String getEntityImageUrl() {
        return entityImageUrl;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
