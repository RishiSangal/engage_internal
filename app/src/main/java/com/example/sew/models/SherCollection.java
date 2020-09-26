package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class SherCollection extends BaseModel {

    private String sherCollectionId;
    private String imageUrl;
    private String titleEng;
    private String titleHin;
    private String titleUrdu;
    private String top20TextEng;
    private String top20TextHindi;
    private String top20TextUrdu;
    private JSONObject jsonObject;
    private String shareURLEng;
    private String shareURLHindi;
    private String shareURLUrdu;
    private String poetId;

    public SherCollection(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        sherCollectionId = optString(jsonObject, "I");
        imageUrl = optString(jsonObject, "Sl");
        titleEng = optString(jsonObject, "Ne");
        titleHin = optString(jsonObject, "Nh");
        titleUrdu = optString(jsonObject, "Nu");
        top20TextEng = optString(jsonObject, "Te");
        top20TextHindi = optString(jsonObject, "Th");
        top20TextUrdu = optString(jsonObject, "Tu");
        shareURLEng= optString(jsonObject,"UE");
        shareURLHindi= optString(jsonObject,"UH");
        shareURLUrdu= optString(jsonObject,"UU");
        poetId= optString(jsonObject,"PI");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getSherCollectionId() {
        return sherCollectionId;
    }

    public String getImageUrl() {
        return String.format(MyService.getMediaURL()+"/images/Cms/T20/%s_Medium.png", imageUrl);
    }



    public String getTitleEng() {
        return titleEng;
    }

    public String getTitle() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getTitleEng();
            case HINDI:
                return getTitleHin();
            case URDU:
                return getTitleUrdu();
        }
        return titleEng;
    }
    public String getShareUrl() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getShareURLEng();
            case HINDI:
                return getShareURLHindi();
            case URDU:
                return getShareURLUrdu();
        }
        return shareURLEng;
    }

    public String getTitleHin() {
        return titleHin;
    }

    public String getTitleUrdu() {
        return titleUrdu;
    }

    public String getTop20TextEng() {
        return top20TextEng;
    }

    public String getTop20TextHindi() {
        return top20TextHindi;
    }

    public String getTop20TextUrdu() {
        return top20TextUrdu;
    }

    public String getShareURLEng() {
        return shareURLEng;
    }

    public String getShareURLHindi() {
        return shareURLHindi;
    }

    public String getShareURLUrdu() {
        return shareURLUrdu;
    }

    public String getPoetId() {
        return poetId;
    }
}
