package com.example.sew.models;

import com.example.sew.common.MyConstants;

import org.json.JSONObject;

import java.util.Locale;

public class WordMeaning extends BaseModel {
    private String id;
    private String englishWord;
    private String hindiWord;
    private String urduWord;
    private String meaning1Eng;
    private String meaning2Eng;
    private String meaning3Eng;
    private String meaning1Hin;
    private String meaning2Hin;
    private String meaning3Hin;
    private String meaning1Ur;
    private String meaning2Ur;
    private String meaning3Ur;
    private boolean isAudioShow;
    public WordMeaning(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        isAudioShow=jsonObject.optBoolean("A");
        id = optString(jsonObject, "I");
        englishWord = optString(jsonObject, "E");
        hindiWord = optString(jsonObject, "H");
        urduWord = optString(jsonObject, "U");
        meaning1Eng = optString(jsonObject, "M1E");
        meaning2Eng = optString(jsonObject, "M2E");
        meaning3Eng = optString(jsonObject, "M3E");
        meaning1Hin = optString(jsonObject, "M1H");
        meaning2Hin = optString(jsonObject, "M2H");
        meaning3Hin = optString(jsonObject, "M3H");
        meaning1Ur = optString(jsonObject, "M1U");
        meaning2Ur = optString(jsonObject, "M2U");
        meaning3Ur = optString(jsonObject, "M3U");
    }

    public String getId() {
        return id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getHindiWord() {
        return hindiWord;
    }

    public String getUrduWord() {
        return urduWord;
    }

    public String getMeaning1Eng() {
        return meaning1Eng;
    }

    public String getMeaning2Eng() {
        return meaning2Eng;
    }

    public String getMeaning3Eng() {
        return meaning3Eng;
    }

    public String getMeaning1Hin() {
        return meaning1Hin;
    }

    public String getMeaning2Hin() {
        return meaning2Hin;
    }

    public String getMeaning3Hin() {
        return meaning3Hin;
    }

    public String getMeaning1Ur() {
        return meaning1Ur;
    }

    public String getMeaning2Ur() {
        return meaning2Ur;
    }

    public String getMeaning3Ur() {
        return meaning3Ur;
    }

    public String getAudioUrl() {
        return String.format(Locale.getDefault(), MyConstants.getDictionaryAudioUrl(), getId());
    }

    public boolean isAudioShow() {
        return isAudioShow;
    }
}
