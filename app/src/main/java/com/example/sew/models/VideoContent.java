package com.example.sew.models;

import org.json.JSONObject;

public class VideoContent extends BaseModel {

    private String audioContentId;
    private String audioId;
    private String audioHeaderTitleEng;
    private String audioHeaderTitleHin;
    private String audioHeaderTitleUr;
    private String AS;
    private String TI;
    private String PSN;
    private String ASN;
    private boolean HA;
    private boolean HP;
    private String audioLength;
    private String id;
    private String PI;
    private String poetNameEng;
    private String poetNameHin;
    private String poetNameUR;
    private String poetStach;
    private String titleEng;
    private String titleHin;
    private String titleUr;

    private String youtubeId;
    private String descriptionEng;
    private String descriptionHin;
    private String descriptionUR;
    private String contentScript;
    private String totalCount;

    public VideoContent(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        id = optString(jsonObject, "I");//
        PI = optString(jsonObject, "PI");//
        poetNameEng = optString(jsonObject, "NE");//
        poetNameHin = optString(jsonObject, "NH");//
        poetNameUR = optString(jsonObject, "NU");//
        poetStach = optString(jsonObject, "PS");//
        titleEng = optString(jsonObject, "TE");//
        titleHin = optString(jsonObject, "TH");//
        titleUr = optString(jsonObject, "TU");//
        audioContentId = optString(jsonObject, "CI");//
        audioId = optString(jsonObject, "AI");//
        audioHeaderTitleEng = optString(jsonObject, "AE");//
        audioHeaderTitleHin = optString(jsonObject, "AH");//
        audioHeaderTitleUr = optString(jsonObject, "AU");//
        AS = optString(jsonObject, "AS");//
        TI = optString(jsonObject, "TI");//
        PSN = optString(jsonObject, "PSN");//
        ASN = optString(jsonObject, "ASN");//
        HA = optString(jsonObject, "HA").contentEquals("true");//
        HP = optString(jsonObject, "HP").contentEquals("true");//
        audioLength = optString(jsonObject, "AD");


        youtubeId = optString(jsonObject, "YI");//
        descriptionEng = optString(jsonObject, "DE");//
        descriptionHin = optString(jsonObject, "DH");//
        descriptionUR = optString(jsonObject, "DU");//
        contentScript = optString(jsonObject, "CS");//
        totalCount = optString(jsonObject, "VC");//
    }

    public String getAudioContentId() {
        return audioContentId;
    }

    public String getAudioId() {
        return audioId;
    }

    public String getAudioHeaderTitleEng() {
        return audioHeaderTitleEng;
    }

    public String getAudioHeaderTitleHin() {
        return audioHeaderTitleHin;
    }

    public String getAudioHeaderTitleUr() {
        return audioHeaderTitleUr;
    }

    public String getAS() {
        return AS;
    }

    public String getTI() {
        return TI;
    }

    public String getPSN() {
        return PSN;
    }

    public String getASN() {
        return ASN;
    }

    public boolean isHA() {
        return HA;
    }

    public boolean isHP() {
        return HP;
    }

    public String getAudioLength() {
        return audioLength;
    }

    public String getId() {
        return id;
    }

    public String getPI() {
        return PI;
    }

    public String getPoetNameEng() {
        return poetNameEng;
    }

    public String getPoetNameHin() {
        return poetNameHin;
    }

    public String getPoetNameUR() {
        return poetNameUR;
    }

    public String getPoetStach() {
        return poetStach;
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

    public String getYoutubeId() {
        return youtubeId;
    }

    public String getDescriptionEng() {
        return descriptionEng;
    }

    public String getDescriptionHin() {
        return descriptionHin;
    }

    public String getDescriptionUR() {
        return descriptionUR;
    }

    public String getContentScript() {
        return contentScript;
    }

    public String getTotalCount() {
        return totalCount;
    }
}
