package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

import java.util.Locale;

public class PoetDetailContentHeader extends BaseModel {
    private String poetId;
    private String nameEng;
    private String nameHin;
    private String nameUr;
    private String shortDescriptionEng;
    private String shortDescriptionHin;
    private String shortDescriptionUr;
    private String yearOfBirth;
    private String yearOfDeath;
    private String seoSlug;
    private String poetImage;
    private String domicileEng;
    private String domicileHin;
    private String domicileUr;
    private String shortUrlEng;
    private String shortUrlHin;
    private String shortUrlUrdu;
    private JSONObject jsonObject;

    PoetDetailContentHeader(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;

        poetId = optString(jsonObject, "I");
        nameEng = optString(jsonObject, "NE");
        nameHin = optString(jsonObject, "NH");
        nameUr = optString(jsonObject, "NU");
        shortDescriptionEng = optString(jsonObject, "SPE");
        shortDescriptionHin = optString(jsonObject, "SPH");
        shortDescriptionUr = optString(jsonObject, "SPU");
        poetImage = getPoetId();
        yearOfBirth = optString(jsonObject, "DOB");
        yearOfDeath = optString(jsonObject, "DOD");
        seoSlug = optString(jsonObject, "S");
        domicileEng = optString(jsonObject, "DE");
        domicileHin = optString(jsonObject, "DH");
        domicileUr = optString(jsonObject, "DU");
        shortUrlEng = optString(jsonObject, "UE");
        shortUrlHin = optString(jsonObject, "UH");
        shortUrlUrdu = optString(jsonObject, "UU");
    }

    public String getPoetImage() {
        if (TextUtils.isEmpty(poetImage))
            return "";
        return String.format(Locale.getDefault(), MyConstants.getPoetImageUrlSmall(), poetImage);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getPoetTenure() {
        StringBuilder builder = new StringBuilder();
        builder.append(getYearOfBirth());
        if (!TextUtils.isEmpty(getYearOfDeath().trim())) {
            builder.append(" - ");
            builder.append(getYearOfDeath().trim());
        }
        return builder.toString();
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

    public String getShortDescriptionEng() {
        return shortDescriptionEng;
    }

    public String getShortDescriptionHin() {
        return shortDescriptionHin;
    }

    public String getShortDescriptionUr() {
        return shortDescriptionUr;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public String getYearOfDeath() {
        return yearOfDeath;
    }

    public String getSeoSlug() {
        return seoSlug;
    }

    public String getDomicileEng() {
        return domicileEng;
    }

    public String getDomicileHin() {
        return domicileHin;
    }

    public String getDomicileUr() {
        return domicileUr;
    }

    public String getDomicile() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getDomicileEng();
            case HINDI:
                return getDomicileHin();
            case URDU:
                return getDomicileUr();
        }
        return getDomicileEng();
    }

    public String getName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getNameEng();
            case HINDI:
                return getNameHin();
            case URDU:
                return getNameUr();
        }
        return getNameEng();
    }

    String getShortDescription() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getShortDescriptionEng();
            case HINDI:
                return getShortDescriptionHin();
            case URDU:
                return getShortDescriptionUr();
        }
        return getShortDescriptionEng();
    }
    public String getShortUrlEng() {
        return shortUrlEng;
    }

    public String getShortUrlHin() {
        return shortUrlHin;
    }

    public String getShortUrlUrdu() {
        return shortUrlUrdu;
    }

    public String getShortUrl(){
        switch (MyService.getSelectedLanguage()){
            case ENGLISH:
                return getShortUrlEng();
            case HINDI:
                return  getShortUrlHin();
            case URDU:
                return getShortUrlUrdu();
        }
        return getShortUrlEng();

    }
}
