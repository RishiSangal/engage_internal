package com.example.sew.models;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class AudioContent extends BaseAudioContent {
    private String audioContentId;
    private String contentScript;
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
    private JSONObject jsonObject;

    public AudioContent(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");//
        PI = optString(jsonObject, "PI");//
        poetNameEng = optString(jsonObject, "NE");//
        poetNameHin = optString(jsonObject, "NH");//
        poetNameUR = optString(jsonObject, "NU");//
        poetStach = optString(jsonObject, "PS");//
        titleEng = optString(jsonObject, "TE");//
        titleHin = optString(jsonObject, "TH");//
        titleUr = optString(jsonObject, "TU");//
        audioContentId = optString(jsonObject, "CI");
        contentScript = optString(jsonObject, "CS");
        audioId = optString(jsonObject, "AI");
        audioHeaderTitleEng = optString(jsonObject, "AE");
        audioHeaderTitleHin = optString(jsonObject, "AH");
        audioHeaderTitleUr = optString(jsonObject, "AU");
        AS = optString(jsonObject, "AS");
        TI = optString(jsonObject, "TI");
        PSN = optString(jsonObject, "PSN");
        ASN = optString(jsonObject, "ASN");
        HA = optString(jsonObject, "HA").contentEquals("true");
        HP = optString(jsonObject, "HP").contentEquals("true");
        audioLength = optString(jsonObject, "AD");
    }

    public String getAudioUrl() {
        return String.format(MyService.getMediaURL()+"/Images/SiteImages/Audio/%s.mp3", getId());

    }

    @Override
    public String getAudioTitle() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getTitleEng();
            case HINDI:
                return getTitleHin();
            case URDU:
                return getTitleUr();
        }
        return getTitleEng();
    }

    public String getAudioContentId() {
        return audioContentId;
    }

    public String getContentScript() {
        return contentScript;
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

    public String getAudioImageUrl() {
        return String.format(MyConstants.getPoetImageUrlSmall(), AS);
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

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
