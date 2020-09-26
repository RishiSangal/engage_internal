package com.example.sew.models;

import org.json.JSONObject;

public class HomePromotionalBanner extends BaseModel {
    JSONObject jsonObject;
    private String id;
    private String bannerId;
    private String bannerName;
    private String targetUrl;
    private String seq;
    private String mediaType;
    private String bannerType;
    private String imageType;
    private String isExternal;
    private String fromDate;
    private String toDate;
    private String imgaUrl;
    private String isHomeBannerShow;

    public HomePromotionalBanner(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        bannerId = optString(jsonObject, "BI");
        bannerName = optString(jsonObject, "BN");
        targetUrl = optString(jsonObject, "TU");
        seq = optString(jsonObject, "S");
        mediaType = optString(jsonObject, "MT");
        bannerType = optString(jsonObject, "BT");
        imageType = optString(jsonObject, "IT");
        isExternal = optString(jsonObject, "IE");
        fromDate = optString(jsonObject, "FD");
        toDate = optString(jsonObject, "TD");
        isHomeBannerShow = optString(jsonObject, "IH");
        imgaUrl = optString(jsonObject, "IU");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public String getBannerId() {
        return bannerId;
    }

    public String getBannerName() {
        return bannerName;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getSeq() {
        return seq;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getBannerType() {
        return bannerType;
    }

    public String getImageType() {
        return imageType;
    }

    public String getIsExternal() {
        return isExternal;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getIsHomeBannerShow() {
        return isHomeBannerShow;
    }

    public String getImgaUrl() {
        return imgaUrl;
    }
}
