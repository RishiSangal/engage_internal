package com.example.sew.models;

import androidx.annotation.IntDef;

import org.json.JSONObject;

public class AppVersion extends BaseModel {
    private String deviceType;
    private String currentVersion;
    @UpdateType
    private int updateType;
    private String popupText;
    private String releaseDescription;
    private String publishDate;

    public static final int FORCE = 1, SOFT = 2;

    @IntDef({FORCE, SOFT})
    private @interface UpdateType{}

    public AppVersion(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.deviceType = optString(jsonObject, "DT");
        this.currentVersion = optString(jsonObject, "CV");
        this.updateType = optString(jsonObject, "UT").contentEquals("1") ? FORCE : SOFT;
        this.popupText = optString(jsonObject, "PT");
        this.releaseDescription = optString(jsonObject, "RD");
        publishDate = optString(jsonObject, "PD");
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public @UpdateType
    int getUpdateType() {
        return updateType;
    }

    public String getPopupText() {
        return popupText;
    }

    public String getReleaseDescription() {
        return releaseDescription;
    }

    public String getPublishDate() {
        return publishDate;
    }

}
