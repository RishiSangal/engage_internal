package com.example.sew.models;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeWordOfTheDay extends BaseSherModel {
    JSONObject jsonObject;
    private String renderingFormat;
    private String word_En;
    private String word_Hi;
    private String word_Ur;
    private String meaning;
    private ArrayList<Para> title;
    private String poetId;
    private String poetName;
    private String poetSlug;
    private String parentTitle;
    private String parentTitleSlug;
    private String parentSlug;
    private String dictionaryId;
    private String haveAudio;
    private String audioMp3File;
    private String audioOggFile;

    public HomeWordOfTheDay(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        renderingFormat = optString(jsonObject, "RF");
        word_En = optString(jsonObject, "WE");
        word_Hi = optString(jsonObject, "WH");
        word_Ur = optString(jsonObject, "WU");
        meaning = optString(jsonObject, "WM");
        title = getPara(optString(jsonObject, "T"));
        poetId = optString(jsonObject, "PI");
        poetName = optString(jsonObject, "PN");
        poetSlug = optString(jsonObject, "PS");
        parentTitle = optString(jsonObject, "PT");
        parentTitleSlug = optString(jsonObject, "PTS");
        parentSlug = optString(jsonObject, "CPS");
        dictionaryId = optString(jsonObject, "DI");
        haveAudio = optString(jsonObject, "HA");
        audioMp3File = optString(jsonObject, "AMF");
        audioOggFile = optString(jsonObject, "AOF");
    }

    public String getParentTitleSlug() {
        return parentTitleSlug;
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getRenderingFormat() {
        return renderingFormat;
    }

    public String getWord_En() {
        return word_En;
    }

    public String getWord_Hi() {
        return word_Hi;
    }

    public String getWord_Ur() {
        return word_Ur;
    }

    public String getMeaning() {
        return meaning;
    }

    public ArrayList<Para> getTitle() {
        return title;
    }

    public String getPoetId() {
        return poetId;
    }

    public String getPoetName() {
        return poetName;
    }

    public String getPoetSlug() {
        return poetSlug;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public String getParentSlug() {
        return parentSlug;
    }

    public String getDictionaryId() {
        return dictionaryId;
    }

    public String getHaveAudio() {
        return haveAudio;
    }

    public String getAudioMp3File() {
        return audioMp3File;
    }

    public String getAudioOggFile() {
        return audioOggFile;
    }
}
