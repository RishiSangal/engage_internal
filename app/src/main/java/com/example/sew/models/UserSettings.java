package com.example.sew.models;

import org.json.JSONObject;

public class UserSettings extends BaseSherModel {
    JSONObject jsonObject;
    private boolean isContentSpecificNotificationEn;
    private boolean isContentSpecificNotificationHi;
    private boolean isContentSpecificNotificationUr;
    private boolean isEventNotificationSubscriber;
    private boolean isSaveFavoriteOffline;
    private boolean isSherOfTheDaySubscriber;
    private boolean isWordOfTheDaySubscriber;
    private boolean isGenericNotificationSubscriber;
    private boolean isRequestedNewsLetter;

    public UserSettings(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        isContentSpecificNotificationEn = jsonObject.optBoolean("IsContentSpecificNotificationEn");
        isContentSpecificNotificationHi = jsonObject.optBoolean("IsContentSpecificNotificationHi");
        isContentSpecificNotificationUr = jsonObject.optBoolean("IsContentSpecificNotificationUr");
        isEventNotificationSubscriber = jsonObject.optBoolean("IsEventNotificationSubscriber");
        isSaveFavoriteOffline = jsonObject.optBoolean("IsSaveFavoriteOffline");
        isSherOfTheDaySubscriber = jsonObject.optBoolean("IsSherOfTheDaySubscriber");
        isWordOfTheDaySubscriber = jsonObject.optBoolean("IsWordOfTheDaySubscriber");
        isGenericNotificationSubscriber = jsonObject.optBoolean("IsGenericNotificationSubscriber");
        isRequestedNewsLetter = jsonObject.optBoolean("IsRequestedNewsLetter");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public boolean isContentSpecificNotificationEn() {
        return isContentSpecificNotificationEn;
    }

    public boolean isContentSpecificNotificationHi() {
        return isContentSpecificNotificationHi;
    }

    public boolean isContentSpecificNotificationUr() {
        return isContentSpecificNotificationUr;
    }

    public boolean isEventNotificationSubscriber() {
        return isEventNotificationSubscriber;
    }

    public boolean isSaveFavoriteOffline() {
        return isSaveFavoriteOffline;
    }

    public boolean isSherOfTheDaySubscriber() {
        return isSherOfTheDaySubscriber;
    }

    public boolean isWordOfTheDaySubscriber() {
        return isWordOfTheDaySubscriber;
    }

    public boolean isGenericNotificationSubscriber() {
        return isGenericNotificationSubscriber;
    }

    public boolean isRequestedNewsLetter() {
        return isRequestedNewsLetter;
    }
}
