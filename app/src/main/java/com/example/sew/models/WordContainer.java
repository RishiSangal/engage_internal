package com.example.sew.models;

import org.json.JSONObject;

public class WordContainer extends BaseModel{
    private String Meaning;
    private String SimpleWord;
    private String Word;

    public WordContainer(JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        this.Meaning = optString(jsonObject,"M");
        this.Word = optString(jsonObject,"W");
        this.SimpleWord = optString(jsonObject,"S") + " ";
    }

    public String getMeaning() {
        return this.Meaning;
    }

    public String getSimpleWord() {
        return this.SimpleWord;
    }

    public String getWord() {
        return this.Word;
    }
}
