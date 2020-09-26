package com.example.sew.models;

import com.example.sew.helpers.MyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeTodayTop extends BaseSherModel {
    JSONObject jsonObject;
    private String contentId;
    private String slug;
    private String shortUrlIndex;
    private String translation;
    private String alignment;
    private String renderingFormat;
    private String typeSlug;
    private String translatorName;
    private String poetId;
    private String poetName;
    private String poetSlug;
    private String poetDPSlug;
    private String parentSlug;
    private String parentTypeSlug,
            urlEnglish,
            urlHindi,
            urlUrdu;
    private String previousTitle;
    private String nextTitle;
    private ArrayList<HomeImageTag> homeTags;
    private ArrayList<Para> renderText;

    public HomeTodayTop(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        contentId = optString(jsonObject, "CI");
        slug = optString(jsonObject, "S");
        shortUrlIndex = optString(jsonObject, "SI");
        renderText = getPara(optString(jsonObject, "T"));
        translation = optString(jsonObject, "TR");
        alignment = optString(jsonObject, "RA");
        renderingFormat = optString(jsonObject, "RF");
        typeSlug = optString(jsonObject, "TS");
        translatorName = optString(jsonObject, "TN");
        poetId = optString(jsonObject, "PI");
        poetName = optString(jsonObject, "PN");
        poetSlug = optString(jsonObject, "PS");
        poetDPSlug = optString(jsonObject, "PDS");
        parentSlug = optString(jsonObject, "CPS");
        parentTypeSlug = optString(jsonObject, "PTS");
        urlEnglish = optString(jsonObject, "UE");
        urlHindi = optString(jsonObject, "UH");
        urlUrdu = optString(jsonObject, "UU");
        this.previousTitle = optString(jsonObject, "PT");
        this.nextTitle = optString(jsonObject, "NT");
        JSONArray jsonArray = jsonObject.optJSONArray("TG");
        if (jsonArray == null)
            jsonArray = new JSONArray();
        int size = jsonArray.length();
        homeTags = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            homeTags.add(new HomeImageTag(jsonArray.optJSONObject(i)));
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getContentId() {
        return contentId;
    }

    public String getSlug() {
        return slug;
    }

    public String getShortUrlIndex() {
        return shortUrlIndex;
    }

    public String getTranslation() {
        return translation;
    }

    public String getAlignment() {
        return alignment;
    }

    public String getRenderingFormat() {
        return renderingFormat;
    }

    public String getTypeSlug() {
        return typeSlug;
    }

    public String getTranslatorName() {
        return translatorName;
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

    public String getPoetDPSlug() {
        return poetDPSlug;
    }

    public String getParentSlug() {
        return parentSlug;
    }

    public String getParentTypeSlug() {
        return parentTypeSlug;
    }

    public ArrayList<HomeImageTag> getHomeTags() {
        return homeTags;
    }

    public ArrayList<Para> getRenderText() {
        return renderText;
    }

    public String getUrlEnglish() {
        return urlEnglish;
    }

    public String getUrlHindi() {
        return urlHindi;
    }

    public String getUrlUrdu() {
        return urlUrdu;
    }

    public String getUrl() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getUrlEnglish();
            case HINDI:
                return getUrlHindi();
            case URDU:
                return getUrlUrdu();
        }
        return urlUrdu;
    }

    public String getPreviousTitle() {
        return previousTitle;
    }

    public String getNextTitle() {
        return nextTitle;
    }
}
