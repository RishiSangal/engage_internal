package com.example.sew.models;

import com.example.sew.helpers.MyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SherContent extends BaseSherModel {

    private final String renderingFormat;
    private final String parentSlug;
    private final String parentTypeSlug;
    private String id;
    private String T;
    private String PI;
    private String poetNameEng;
    private String poetNameHin;
    private String poetNameUR;
    private String ghazalID;
    private String titleEng;
    private String titleHin;
    private String titleUr;
    private String seriesEng;
    private String seriesHin;
    private String seriesUr;
    private String R;
    private String S;
    private String SI;
    private String N;
    private boolean isEditorChoice;
    private boolean isPopularChoice;
    private ArrayList<Para> renderTextEng;
    private ArrayList<Para> renderTextHin;
    private ArrayList<Para> renderTextUR;
    private String A;

    private String linkEng;
    private String linkHin;
    private String linkUr;
    private ArrayList<SherTag> sherTags;


    private boolean shouldShowTranslation;

    public SherContent(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        id = optString(jsonObject, "I");
        T = optString(jsonObject, "T");
        PI = optString(jsonObject, "PI");
        poetNameEng = optString(jsonObject, "PE");
        poetNameHin = optString(jsonObject, "PH");
        poetNameUR = optString(jsonObject, "PU");

        renderTextEng = getPara(optString(jsonObject, "RE"));
        renderTextHin = getPara(optString(jsonObject, "RH"));
        renderTextUR = getPara(optString(jsonObject, "RU"));

        A = optString(jsonObject, "A");

        renderingFormat = optString(jsonObject, "RF");
        parentSlug = optString(jsonObject, "PS");
        parentTypeSlug = optString(jsonObject, "PTS");

        ghazalID = optString(jsonObject, "P");
        titleEng = optString(jsonObject, "TE");
        titleHin = optString(jsonObject, "TH");
        titleUr = optString(jsonObject, "TU");
        seriesEng = optString(jsonObject, "SE");
        seriesHin = optString(jsonObject, "SH");
        seriesUr = optString(jsonObject, "SU");
        R = optString(jsonObject, "R");
        S = optString(jsonObject, "S");
        SI = optString(jsonObject, "SI");
        N = optString(jsonObject, "N");
        isEditorChoice = optString(jsonObject, "EC").contentEquals("true");
        isPopularChoice = optString(jsonObject, "PC").contentEquals("true");
        linkEng = optString(jsonObject, "UE");
        linkHin = optString(jsonObject, "UH");
        linkUr = optString(jsonObject, "UU");
        JSONArray tagsArray = jsonObject.optJSONArray("TS");
        if (tagsArray == null)
            tagsArray = new JSONArray();
        int size = tagsArray.length();
        sherTags = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            sherTags.add(new SherTag(tagsArray.optJSONObject(i)));
    }

    public boolean isShouldShowTranslation() {
        return shouldShowTranslation;
    }

    public void setShouldShowTranslation(boolean shouldShowTranslation) {
        this.shouldShowTranslation = shouldShowTranslation;
    }

    public ArrayList<SherTag> getSherTags() {
        return sherTags;
    }

    public String getId() {
        return id;
    }

    public String getT() {
        return T;
    }

    public String getPI() {
        return PI;
    }

    public String getPoetNameEng() {
        return poetNameEng;
    }

    public String getPoetName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getPoetNameEng();
            case HINDI:
                return getPoetNameHin();
            case URDU:
                return getPoetNameUR();
        }
        return getPoetNameEng();
    }

    public String getPoetNameHin() {
        return poetNameHin;
    }

    public String getPoetNameUR() {
        return poetNameUR;
    }

    public String getGhazalID() {
        return ghazalID;
    }

    public String getTitleEng() {
        return titleEng;
    }

    public String getTitleHin() {
        return titleHin;
    }

    public String getTitleUr() {
        return titleUr;
    }

    public String getSeriesEng() {
        return seriesEng;
    }

    public String getSeriesHin() {
        return seriesHin;
    }

    public String getSeriesUr() {
        return seriesUr;
    }

    public String getR() {
        return R;
    }

    public String getS() {
        return S;
    }

    public String getSI() {
        return SI;
    }

    public String getN() {
        return N;
    }

    public boolean isEditorChoice() {
        return isEditorChoice;
    }

    public boolean isPopularChoice() {
        return isPopularChoice;
    }
    public ArrayList<Para> getRenderText() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getRenderTextEng();
            case HINDI:
                return getRenderTextHin();
            case URDU:
                return getRenderTextUR();
        }
        return getRenderTextEng();
    }
    public ArrayList<Para> getRenderTextEng() {
        return renderTextEng;
    }

    public ArrayList<Para> getRenderTextHin() {
        return renderTextHin;
    }

    public ArrayList<Para> getRenderTextUR() {
        return renderTextUR;
    }

    public String getA() {
        return A;
    }

    public String getLinkEng() {
        return linkEng;
    }

    public String getLink() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getLinkEng();
            case HINDI:
                return getLinkHin();
            case URDU:
                return getLinkUr();
        }
        return getLinkEng();
    }

    public String getLinkHin() {
        return linkHin;
    }

    public String getLinkUr() {
        return linkUr;
    }
}
