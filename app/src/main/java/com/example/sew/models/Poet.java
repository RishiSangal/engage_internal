package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.common.MyConstants;

import org.json.JSONObject;

import java.util.Locale;

public class Poet extends BaseModel {
    private String poetId;
    private String nameEng;
    private String nameHin;
    private String nameUr;
    private String P;
    private String poetImage;
    private String HI;
    private String dateFrom;
    private String dateTo;
    private String Le;
    private String Lh;
    private String Lu;
    private String ghazalCount;
    private String nazmCount;
    private String sherCount;
    private String descriptionEng;
    private String descriptionHin;
    private String descriptionUr;
    private boolean isNewPoet;
    private String SPE;
    private String SPH;
    private String SPU;

    public Poet(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        poetId = optString(jsonObject, "I");
        nameEng = optString(jsonObject, "NE");
        nameHin = optString(jsonObject, "NH");
        nameUr = optString(jsonObject, "NU");
        P = optString(jsonObject, "P");//true/false
        poetImage = optString(jsonObject, "SL");
        HI = optString(jsonObject, "HI");//true/false
        dateFrom = optString(jsonObject, "DF");
        dateTo = optString(jsonObject, "DT");
        Le = optString(jsonObject, "Le");
        Lh = optString(jsonObject, "Lh");
        Lu = optString(jsonObject, "Lu");
        ghazalCount = optString(jsonObject, "GC");
        nazmCount = optString(jsonObject, "NC");
        sherCount = optString(jsonObject, "SC");
        descriptionEng = optString(jsonObject, "DE");
        descriptionHin = optString(jsonObject, "DH");
        descriptionUr = optString(jsonObject, "DU");
        isNewPoet = optString(jsonObject, "N").contentEquals("true");
        SPE = optString(jsonObject, "SPE");
        SPH = optString(jsonObject, "SPH");
        SPU = optString(jsonObject, "SPU");
    }

    public String getPoetId() {
        return poetId;
    }

    public String getNameEng() {
        return nameEng;
    }

    public String getNameHin() {
        return nameHin;
    }

    public String getNameUr() {
        return nameUr;
    }

    public String getP() {
        return P;
    }

    public String getPoetImage() {
        if (TextUtils.isEmpty(poetImage))
            return "";
        return String.format(Locale.getDefault(), MyConstants.getPoetImageUrlSmall(), poetImage);
    }

    public String getHI() {
        return HI;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getLe() {
        return Le;
    }

    public String getLh() {
        return Lh;
    }

    public String getLu() {
        return Lu;
    }

    public String getGhazalCount() {
        return ghazalCount;
    }

    public String getNazmCount() {
        return nazmCount;
    }

    public String getSherCount() {
        return sherCount;
    }

    public String getDescriptionEng() {
        return descriptionEng;
    }

    public String getDescriptionHin() {
        return descriptionHin;
    }

    public String getDescriptionUr() {
        return descriptionUr;
    }

    public boolean isNewPoet() {
        return isNewPoet;
    }

    public String getSPE() {
        return SPE;
    }

    public String getSPH() {
        return SPH;
    }

    public String getSPU() {
        return SPU;
    }
}
