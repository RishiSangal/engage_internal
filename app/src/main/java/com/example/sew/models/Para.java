package com.example.sew.models;

import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Para {
    private ArrayList<Line> lines;
    private ArrayList<Line> translations;
    View containerView;

    Para(JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        JSONArray paraArray = jsonObject.optJSONArray("L");
        if (paraArray == null) {
            paraArray = new JSONArray();
        }
        int size = paraArray.length();
        this.lines = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.lines.add(new Line(paraArray.optJSONObject(i)));
        }

        JSONArray translationArray = jsonObject.optJSONArray("T");
        if (translationArray == null)
            translationArray = new JSONArray();
        size = translationArray.length();
        this.translations = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.translations.add(new Line(translationArray.optJSONObject(i)));
        }
    }

    public ArrayList<Line> getLines() {
        return this.lines;
    }

    public ArrayList<Line> getTranslations() {
        return translations;
    }

    public void setContainerView(View containerView) {
        this.containerView = containerView;
    }

    public View getContainerView() {
        return containerView;
    }
}
