package com.example.sew.models;

import org.json.JSONObject;

import java.util.ArrayList;

public class PushNotification extends BaseSherModel {
    JSONObject jsonObject;
    private ArrayList<Para> title;
    private HomeWordOfTheDay wordOfTheDay;
    private SherOfTheDay sherOfTheDay;
    private PushImageShayari imageShayari;
    private PushRemembering remembering;
    private PushEvent event;
    private PushGeneral general;

    public PushNotification(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        wordOfTheDay = new HomeWordOfTheDay(jsonObject.optJSONObject("WordOfTheDay"));
        sherOfTheDay = new SherOfTheDay(jsonObject.optJSONObject("SherOfTheDay"));
        imageShayari = new PushImageShayari(jsonObject.optJSONObject("ImgShayari"));
        remembering = new PushRemembering(jsonObject.optJSONObject("Remembering"));
        event = new PushEvent(jsonObject.optJSONObject("Event"));
        general = new PushGeneral(jsonObject.optJSONObject("General"));
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public ArrayList<Para> getTitle() {
        return title;
    }

    public HomeWordOfTheDay getWordOfTheDay() {
        return wordOfTheDay;
    }

    public SherOfTheDay getSherOfTheDay() {
        return sherOfTheDay;
    }

    public PushImageShayari getImageShayari() {
        return imageShayari;
    }

    public PushRemembering getRemembering() {
        return remembering;
    }

    public PushEvent getEvent() {
        return event;
    }

    public PushGeneral getGeneral() {
        return general;
    }
}
