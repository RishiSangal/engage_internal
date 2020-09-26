package com.example.sew.models;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseModel {
    public String optString(JSONObject jsonObject, String key) {
        if (jsonObject == null || TextUtils.isEmpty(key))
            return "";
        String content = jsonObject.optString(key);
        if (TextUtils.isEmpty(content) || content.contentEquals("null"))
            return "";
        return content;
    }

    public JSONObject getJsonObject() {
        return new JSONObject();
    }

    void updateKey(JSONObject jsonObject, String key, Object value) {
        try {
            if (jsonObject != null)
                jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
