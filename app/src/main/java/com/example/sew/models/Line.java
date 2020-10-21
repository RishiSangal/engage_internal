package com.example.sew.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Line {
    private ArrayList<WordContainer> wordContainers;
    private String fullText;

    public Line(JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        JSONArray paraArray = jsonObject.optJSONArray("W");
        if (paraArray == null) {
            paraArray = new JSONArray();
        }
        int size = paraArray.length();
        this.wordContainers = new ArrayList<>(size);
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < size; i++) {
            WordContainer wordContainer = new WordContainer(paraArray.optJSONObject(i));
            this.wordContainers.add(wordContainer);
            if (i != 0)
                stringBuffer.append(" ");
            stringBuffer.append(wordContainer.getWord());
        }
        fullText = stringBuffer.toString();
    }

    public String getFullText() {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < wordContainers.size(); i++) {
            WordContainer wordContainer = wordContainers.get(i);
            if (i != 0)
                stringBuffer.append(" ");
            stringBuffer.append(wordContainer.getWord());
        }
        fullText = stringBuffer.toString();
        return fullText;
    }

    public ArrayList<WordContainer> getWordContainers() {
        return this.wordContainers;
    }

    public Line cloneThis() {
        Line line = new Line(null);
        line.wordContainers = new ArrayList<>(this.wordContainers);
        line.fullText = this.fullText;
        return line;
    }
}
