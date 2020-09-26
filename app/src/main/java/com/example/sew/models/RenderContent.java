package com.example.sew.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RenderContent {
    ArrayList<Para> paras;

    public RenderContent(JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        JSONArray paraArray = jsonObject.optJSONArray("P");
        if (paraArray == null) {
            paraArray = new JSONArray();
        }
        int size = paraArray.length();
        this.paras = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.paras.add(new Para(paraArray.optJSONObject(i)));
        }
    }

    public ArrayList<Para> getParas() {
        return this.paras;
    }
}
