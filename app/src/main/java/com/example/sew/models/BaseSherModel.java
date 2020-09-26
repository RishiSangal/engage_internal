package com.example.sew.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseSherModel extends BaseModel{

    ArrayList<Para> getPara(String paraText) {
        JSONObject renderContent = null;
        ArrayList<Para> paras;
        try {
            renderContent = new JSONObject(paraText);
            JSONArray paraArray = renderContent.optJSONArray("P");
            int size = 0;
            if (paraArray == null)
                paraArray = new JSONArray();
            size = paraArray.length();
            paras = new ArrayList<>();
            for (int i = 0; i < size; i++)
                paras.add(new Para(paraArray.optJSONObject(i)));
            return paras;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
