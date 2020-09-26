package com.example.sew.models;

import org.json.JSONObject;

public class PushGeneral extends BaseModel {
    JSONObject jsonObject;
    /*
      "Title": "test",
            "Content": "message"
     */
    private String title;//
    private String content;//
    private String targetUrl;
    private boolean IsExternal;
    private String imageURL;

    public PushGeneral(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        title = optString(jsonObject, "Title");
        content = optString(jsonObject, "Content");
        targetUrl= optString(jsonObject,"TargetUrl");
        IsExternal= jsonObject.optBoolean("IsExternal");
        imageURL= optString(jsonObject,"ImageUrl");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public boolean isExternal() {
        return IsExternal;
    }

    public String getImageURL() {
        return imageURL;
    }
}
