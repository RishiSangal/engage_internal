package com.example.sew.models;

import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class ShayariContent extends BaseModel {
    private String id;
    private String T;
    private String PI;
    private String poetNameEng;//PE
    private String poetNameHin;//PH
    private String poetNameUR;//PU
    private String P;
    private String titleEng;//TE;
    private String titleHin;//TH;
    private String titleUr;//TU;
    private String seriesEng;//SE;
    private String seriesHin;//SH;
    private String seriesUr;//SU;
    private String R;
    private String S;
    private String SI;
    private String N;
    private boolean isEditorChoice;//EC;
    private boolean isPopularChoice;//PC;
    private boolean isAudioAvailable;//AU;
    private boolean isVideoAvailable;//VI;
    private String AC;
    private String VC;
    private String HE;
    private String HH;
    private String HU;
    private String linkEng;//UE;
    private String linkHin;//UH;
    private String linkUr;//UU;
    private String FC;
    private String SC;
    private JSONObject jsonObject;

    public ShayariContent(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        T = optString(jsonObject, "T");
        PI = optString(jsonObject, "PI");
        poetNameEng = optString(jsonObject, "PE");
        poetNameHin = optString(jsonObject, "PH");
        poetNameUR = optString(jsonObject, "PU");
        P = optString(jsonObject, "P");
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
        isAudioAvailable = optString(jsonObject, "AU").contentEquals("true");
        isVideoAvailable = optString(jsonObject, "VI").contentEquals("true");
        AC = optString(jsonObject, "AC");
        VC = optString(jsonObject, "VC");
        HE = optString(jsonObject, "HE");
        HH = optString(jsonObject, "HH");
        HU = optString(jsonObject, "HU");
        linkEng = optString(jsonObject, "UE");
        linkHin = optString(jsonObject, "UH");
        linkUr = optString(jsonObject, "UU");
        FC = optString(jsonObject, "FC");
        SC = optString(jsonObject, "SC");
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

    public String getPoetNameHin() {
        return poetNameHin;
    }

    public String getPoetNameUR() {
        return poetNameUR;
    }

    public String getP() {
        return P;
    }

    public String getTitle() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getTitleEng();
            case HINDI:
                return getTitleHin();
            case URDU:
                return getTitleUr();
        }
        return "";
    }

    public String getSeries() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getSeriesEng();
            case HINDI:
                return getSeriesHin();
            case URDU:
                return getSeriesUr();
        }
        return "";
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
        return "";
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

    public boolean isAudioAvailable() {
        return isAudioAvailable;
    }

    public boolean isVideoAvailable() {
        return isVideoAvailable;
    }

    public String getAC() {
        return AC;
    }

    public String getVC() {
        return VC;
    }

    public String getHE() {
        return HE;
    }

    public String getHH() {
        return HH;
    }

    public String getHU() {
        return HU;
    }

    public String getLinkEng() {
        return linkEng;
    }

    public String getLinkHin() {
        return linkHin;
    }

    public String getLinkUr() {
        return linkUr;
    }

    public String getFC() {
        return FC;
    }

    public String getSC() {
        return SC;
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
