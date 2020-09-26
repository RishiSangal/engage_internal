package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class WordOfTheDay extends BaseModel {

    private String id;
    private String englishWord;
    private String hindiWord;
    private String urduWord;
    private String meaningEng;
    private String meaningHin;
    private String meaningUr;
    private String poetId;
    private String poetNameEng;
    private String poetNameHin;
    private String poetNameUrdu;
    private String contentId;
    private String contentSlug;
    private String contentType;
    private String contentEng;
    private String contentHin;
    private String contentUrdu;
    private String seoSlug;
    private String parentSlug;
    private String parentTitleSlug;
    private  String parentTitleEng;
    private String parentTitleHin;
    private String parentTitleUrdu;

    public WordOfTheDay(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        id = optString(jsonObject, "I");
        englishWord = optString(jsonObject, "WE");
        hindiWord = optString(jsonObject, "WH");
        urduWord = optString(jsonObject, "WU");
        meaningEng = optString(jsonObject, "ME");
        meaningHin = optString(jsonObject, "MH");
        meaningUr = optString(jsonObject, "MU");
        poetId = optString(jsonObject, "PI");
        poetNameEng = optString(jsonObject, "NE");
        poetNameHin = optString(jsonObject, "NH");
        poetNameUrdu = optString(jsonObject, "NU");
        contentId = optString(jsonObject, "CI");
        contentSlug = optString(jsonObject, "CS");
        contentType = optString(jsonObject, "CT");
        contentEng = optString(jsonObject, "CE");
        contentHin = optString(jsonObject, "CH");
        contentUrdu = optString(jsonObject, "CU");
        seoSlug = optString(jsonObject, "S");
        parentSlug= optString(jsonObject,"PS");
        parentTitleSlug= optString(jsonObject,"PTS");
        parentTitleEng= optString(jsonObject,"PTE");
        parentTitleHin= optString(jsonObject,"PTH");
        parentTitleUrdu= optString(jsonObject,"PTU");
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

    public String getMeaningEng() {
        return meaningEng;
    }

    public String getMeaningHin() {
        return meaningHin;
    }

    public String getMeaningUr() {
        return meaningUr;
    }

    public String getMeaning() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getMeaningEng();
            case HINDI:
                return getMeaningHin();
            case URDU:
                return getMeaningUr();
        }
        return getMeaningEng();
    }

    public String getParentTitle(){
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getParentTitleEng();
            case HINDI:
                return getParentTitleHin();
            case URDU:
                return getParentTitleUrdu();
        }
        return getParentTitleEng();
    }
    public String getPoetId() {
        return poetId;
    }

    public String getPoetNameEng() {
        return poetNameEng;
    }

    public String getPoetNameHin() {
        return poetNameHin;
    }

    public String getPoetNameUrdu() {
        return poetNameUrdu;
    }

    public String getPoetName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getPoetNameEng();
            case HINDI:
                return getPoetNameHin();
            case URDU:
                return getPoetNameUrdu();
        }
        return getPoetNameEng();
    }

    public String getContentId() {
        return contentId;
    }

    public String getContentSlug() {
        return contentSlug;
    }

    public ContentType getContentType() {
        return MyHelper.getContentById(contentType);
    }

    public String getContentEng() {
        return contentEng;
    }

    public String getContentHin() {
        return contentHin;
    }

    public String getContentUrdu() {
        return contentUrdu;
    }

    public String getContent() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getContentEng();
            case HINDI:
                return getContentHin();
            case URDU:
                return getContentUrdu();
        }
        return getContentEng();
    }

    public String getSeoSlug() {
        return seoSlug;
    }

    public String getParentSlug() {
        return parentSlug;
    }

    public String getParentTitleSlug() {
        return parentTitleSlug;
    }

    public String getParentTitleEng() {
        return parentTitleEng;
    }

    public String getParentTitleHin() {
        return parentTitleHin;
    }

    public String getParentTitleUrdu() {
        return parentTitleUrdu;
    }
}
