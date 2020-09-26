package com.example.sew.models;


import org.json.JSONObject;

public class DidYouKnow extends BaseModel {
    JSONObject jsonObject;
    private String id;
    private String title;
    private String htmlContentRender;
    private int tt;
    private String typeName;
    private String hi;
    private String ti;
    private String targetSlug;
    private String th;
    private String background;

    public DidYouKnow(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        id = optString(jsonObject, "I");
        title = optString(jsonObject, "T");
        htmlContentRender = optString(jsonObject, "R");
        tt = jsonObject.optInt("TT");
        typeName = optString(jsonObject, "TN");
        hi = optString(jsonObject, "HI");
        ti = optString(jsonObject, "TI");
        targetSlug = optString(jsonObject, "TS");
        th = optString(jsonObject, "TH");
        background = optString(jsonObject, "BG");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getHtmlContentRender() {
        return htmlContentRender;
    }

    public int getTt() {
        return tt;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getHi() {
        return hi;
    }

    public String getTi() {
        return ti;
    }

    public String getTargetSlug() {
        return targetSlug;
    }

    public String getTh() {
        return th;
    }

    public String getBackground() {
        return background;
    }
}
