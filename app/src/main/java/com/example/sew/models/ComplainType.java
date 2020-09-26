package com.example.sew.models;

import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class ComplainType extends BaseModel {
    JSONObject jsonObject;
    private String typeId, nameEn, nameHi, nameUr;

    public ComplainType(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        typeId = optString(jsonObject, "RTI");
        nameEn = optString(jsonObject, "NE");
        nameHi = optString(jsonObject, "NH");
        nameUr = optString(jsonObject, "NU");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameHi() {
        return nameHi;
    }

    public String getNameUr() {
        return nameUr;
    }
    public String getName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getNameEn();
            case HINDI:
                return getNameHi();
            case URDU:
                return getNameUr();
        }
        return getNameEn();
    }

}
