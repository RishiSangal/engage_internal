package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.common.MyConstants;
import com.example.sew.common.Utils;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

import java.util.Locale;

public class FavoriteDictionary extends BaseOtherFavModel {
    private String Id;
    private String Urdu;
    private String Hindi;
    private String English;
    private String FD;
    private String Meaning1_En;
    private String Meaning2_En;
    private String Meaning3_En;
    private String Meaning1_Hi;
    private String Meaning2_Hi;
    private String Meaning3_Hi;
    private String Meaning1_Ur;
    private String Meaning2_Ur;
    private String Meaning3_Ur;
    private boolean isHaveAudio;
    JSONObject jsonObject;
    /*
    "I":"3d13dd2e-8f55-41df-adbb-c4f3c3f860ed",
"WE":" bhulaa ",
"WH":"भुला",
"WU":"بھلا",
"ME":"forgot",
"MH":null,
"MU":null,
"HA":null,
"AMF":"",
"AOF":""
     */
    public FavoriteDictionary(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject= jsonObject;
        Id = optString(jsonObject, "I");
        Urdu = optString(jsonObject, "WU");
        Hindi = optString(jsonObject, "WH");
        English = optString(jsonObject, "WE");
        FD = optString(jsonObject, "FD");
        Meaning1_En = optString(jsonObject, "ME");
        Meaning2_En = optString(jsonObject, "ME");
        Meaning3_En = optString(jsonObject, "ME");
        Meaning1_Hi = optString(jsonObject, "MH");
        Meaning2_Hi = optString(jsonObject, "MH");
        Meaning3_Hi = optString(jsonObject, "MH");
        Meaning1_Ur = optString(jsonObject, "MU");
        Meaning2_Ur = optString(jsonObject, "MU");
        Meaning3_Ur = optString(jsonObject, "MU");
        isHaveAudio = jsonObject.optBoolean("HA",false);
    }

    public String getFD() {
        if (FD.isEmpty())
            return Utils.getCurrentFM();
        else
            return FD;
    }

    public String getId() {
        return Id;
    }

    @Override
    public String getContentTypeId() {
        return MyConstants.FAV_WORD_CONTENT_TYPE_ID;
    }

    public String getAudioUrl() {
        return String.format(Locale.getDefault(), MyConstants.getDictionaryAudioUrl(), getId());
    }

    public String getUrdu() {
        return Urdu;
    }

    public String getHindi() {
        return Hindi;
    }

    public String getEnglish() {
        return English;
    }

    public String getMeaning1_En() {
        return Meaning1_En;
    }

    public String getMeaning1() {
        String meaning = "";
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                meaning = getMeaning1_En();
                break;
            case HINDI:
                meaning = getMeaning1_Hi();
                break;
            case URDU:
                meaning = getMeaning1_Ur();
                break;
        }
        return TextUtils.isEmpty(meaning) ? Meaning1_En : meaning;
    }

    public String getMeaning2() {
        String meaning = "";
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                meaning = getMeaning2_En();
                break;
            case HINDI:
                meaning = getMeaning2_Hi();
                break;
            case URDU:
                meaning = getMeaning2_Ur();
                break;
        }
        return TextUtils.isEmpty(meaning) ? Meaning2_En : meaning;
    }

    public String getMeaning3() {
        String meaning = "";
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                meaning = getMeaning3_En();
                break;
            case HINDI:
                meaning = getMeaning3_Hi();
                break;
            case URDU:
                meaning = getMeaning3_Ur();
                break;
        }
        return TextUtils.isEmpty(meaning) ? Meaning3_En : meaning;
    }

    public String getMeaning2_En() {
        return Meaning2_En;
    }

    public String getMeaning3_En() {
        return Meaning3_En;
    }

    public String getMeaning1_Hi() {
        return Meaning1_Hi;
    }

    public String getMeaning2_Hi() {
        return Meaning2_Hi;
    }

    public String getMeaning3_Hi() {
        return Meaning3_Hi;
    }

    public String getMeaning1_Ur() {
        return Meaning1_Ur;
    }

    public String getMeaning2_Ur() {
        return Meaning2_Ur;
    }

    public String getMeaning3_Ur() {
        return Meaning3_Ur;
    }

    public boolean isHaveAudio() {
        return isHaveAudio;
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !(o instanceof FavoriteDictionary))
            return false;
        FavoriteDictionary favoriteDictionary = (FavoriteDictionary) o;
        return getId().equalsIgnoreCase(favoriteDictionary.getId());
    }

    @Override
    public int hashCode() {
        return getId().toUpperCase().hashCode();
    }
}
