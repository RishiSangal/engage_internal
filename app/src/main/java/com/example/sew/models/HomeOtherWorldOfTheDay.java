package com.example.sew.models;

import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class HomeOtherWorldOfTheDay extends BaseModel {
    JSONObject jsonObject;
    private String word_En;
    private String word_Hi;
    private String word_Ur;
    private String meaning;
    private String dictionaryId;

    public HomeOtherWorldOfTheDay(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        word_En = optString(jsonObject, "WE");
        word_Hi = optString(jsonObject, "WH");
        word_Ur = optString(jsonObject, "WU");
        meaning = optString(jsonObject, "WM");
        dictionaryId = optString(jsonObject, "DI");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getWord_En() {
        return word_En;
    }

    public String getWord_Hi() {
        return word_Hi;
    }

    public String getWord_Ur() {
        return word_Ur;
    }

    public String getMeaning() {
        return meaning;
    }
public String getWord(){
    switch (MyService.getSelectedLanguage()){
        case ENGLISH:
            return getWord_En();
        case HINDI:
            return getWord_Hi();
        case URDU:
            return getWord_Ur();
    }
    return getWord_En();
}
    public String getDictionaryId() {
        return dictionaryId;
    }
}
