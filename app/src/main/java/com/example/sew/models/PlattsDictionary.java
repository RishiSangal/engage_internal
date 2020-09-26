package com.example.sew.models;

import org.json.JSONObject;

public class PlattsDictionary extends BaseModel {
    private String Id;
    private String Urdu;
    private String Hindi;
    private String English;
    private String EnTr;
    private String Html;
    private String Origin;

    public PlattsDictionary(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        Id = optString(jsonObject, "Id");
        Urdu = optString(jsonObject, "Urdu");
        Hindi = optString(jsonObject, "Hindi");
        English = optString(jsonObject, "English");
        EnTr = optString(jsonObject, "EnTr");
        Html = optString(jsonObject, "Html");
        Origin = optString(jsonObject, "Origin");
    }

    public String getId() {
        return Id;
    }

    public String getUrdu() {
        return Urdu;
    }

    public String getHindi() {
        return Hindi;
    }

    public String getEnglish() {
        return English;
    }

    public String getEnTr() {
        return EnTr;
    }

    public String getHtml() {
        return Html;
    }

    public String getOrigin() {
        return Origin;
    }
}
