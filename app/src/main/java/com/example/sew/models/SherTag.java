package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class SherTag extends BaseSherTag {
    private String tagId;
    private String nameEng;
    private String nameHin;
    private String nameUr;
    private String slang;
    private String totalSher;
    private String SE;
    private String SH;
    private String SU;
    private int tagColor;
    JSONObject jsonObject;

    public SherTag(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        if (jsonObject.has("I"))
            tagId = optString(jsonObject, "I");
        else
            tagId = optString(jsonObject, "TI");
        if (jsonObject.has("TN"))
            nameEng = nameHin = nameUr = optString(jsonObject, "TN");
        else if (jsonObject.has("N"))
            nameEng = nameHin = nameUr = optString(jsonObject, "N");
        else {
            nameEng = optString(jsonObject, "NE");
            nameHin = optString(jsonObject, "NH");
            nameUr = optString(jsonObject, "NU");
        }
        if (jsonObject.has("TS"))
            slang = optString(jsonObject, "TS");
        else
            slang = optString(jsonObject, "S");
        totalSher = optString(jsonObject, "T");
        SE = optString(jsonObject, "SE");
        SH = optString(jsonObject, "SH");
        SU = optString(jsonObject, "SU");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public String getTagId() {
        return tagId;
    }

    public String getNameEng() {
        return nameEng;
    }

    @Override
    public String getTagName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getNameEng();
            case HINDI:
                return getNameHin();
            case URDU:
                return getNameUr();
        }
        return nameEng;
    }

    public String getNameHin() {
        return nameHin;
    }

    public String getNameUr() {
        return nameUr;
    }

    public String getSlang() {
        return slang;
    }

    public String getTotalSher() {
        return totalSher;
    }

    public String getSE() {
        return SE;
    }

    public String getSH() {
        return SH;
    }

    public String getSU() {
        return SU;
    }

    public void setTagColor(int tagColor) {
        this.tagColor = tagColor;
    }

    public int getTagColor() {
        return tagColor;
    }

    public void setTagName(String tagName) {
        this.nameEng = tagName;
        this.nameHin = tagName;
        this.nameUr = tagName;
    }

}
