package com.example.sew.models;

import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class HomeBannerCollection extends BaseModel {
    private String id;
    private String bannerId;
    private String bannerNameEn;
    private String bannerNameHi;
    private String bannerNameUr;
    private String labelTextEn;
    private String labelTextHi;
    private String labelTextUr;
    private String byLineEn;
    private String byLineHi;
    private String byLineUr;
    private String fromDate;
    private String toDate;
    private String targetId;
    private String targetUrl;
    private String childId;
    private int imageType;
    private String seq;
    private int bannerType;
    private String tabIndex;
    private String mergeToPrev;
    private String labelBackgroundColor;
    private String labelColor;
    private String poetNameEn;
    private String poetNameHi;
    private String poetNameUr;
    private String imageURL;
    private String contentTypeId;
    JSONObject jsonObject;
    public static final int IMAGETYPE_NONE = 0;
    public static final int IMAGETYPE_SMALL = 1;
    public static final int IMAGETYPE_MEDIUM = 2;
    public static final int IMAGETYPE_LARGE = 3;
    //Tags = 7, Occasion = 8, Shayari = 9, Prose = 10, ImageShayari = 11, Collection = 12
    public static final int BANNERTYPE_ENTITY = 1;
    public static final int BANNERTYPE_CONTENT = 2;
    public static final int BANNERTYPE_T20 = 3;
    public static final int BANNERTYPE_HEADER = 4;
    public static final int BANNERTYPE_TARGET_URL = 5;
    public static final int BANNERTYPE_PROMOTIONAL = 6;
    public static final int BANNERTYPE_TAGS = 7;
    public static final int BANNERTYPE_OCCASION = 8;
    public static final int BANNERTYPE_SHAYARI = 9;
    public static final int BANNERTYPE_PROSE = 10;
    public static final int BANNERTYPE_IMAGESHAYARI = 11;

    public static final int MEDIATYPE_IMAGE = 1;
    public static final int MEDIATYPE_VIDEO = 2;

    public HomeBannerCollection(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        bannerId = optString(jsonObject, "BI");
        bannerNameUr = bannerNameHi = bannerNameEn = optString(jsonObject, "BE");
        /*bannerNameHi = optString(jsonObject, "BH");
        bannerNameUr = optString(jsonObject, "BU");*/
        //labelTextEn = optString(jsonObject, "LE");
        // labelTextHi = optString(jsonObject, "LH");
        // labelTextUr = optString(jsonObject, "LU");
        byLineUr = byLineHi = byLineEn = optString(jsonObject, "BL");
//        byLineHi = optString(jsonObject, "BLH");
//        byLineUr = optString(jsonObject, "BLU");
        //fromDate = optString(jsonObject, "FD");
        // toDate = optString(jsonObject, "TD");
        targetId = optString(jsonObject, "TI");
        //targetUrl = optString(jsonObject, "TU");
        childId = optString(jsonObject, "CI");
        imageType = MyHelper.convertToInt(optString(jsonObject, "IT"));
        seq = optString(jsonObject, "S");
        bannerType = MyHelper.convertToInt(optString(jsonObject, "BT"));
        // tabIndex = optString(jsonObject, "TIN");
        // mergeToPrev = optString(jsonObject, "MP");
        // labelBackgroundColor = optString(jsonObject, "LB");
        // labelColor = optString(jsonObject, "LC");
        //  poetNameEn = optString(jsonObject, "PNE");
        // poetNameHi = optString(jsonObject, "PNH");
        // poetNameUr = optString(jsonObject, "PNU");
        imageURL = optString(jsonObject, "IU");
        contentTypeId = optString(jsonObject, "CT");
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
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getBannerNameEn();
            case HINDI:
                return getBannerNameHi();
            case URDU:
                return getBannerNameUr();
        }
        return getBannerNameEn();
    }

    public String getBannerNameEn() {
        return bannerNameEn;
    }

    public String getBannerNameHi() {
        return bannerNameHi;
    }

    public String getBannerNameUr() {
        return bannerNameUr;
    }

    public String getLabelTextEn() {
        return labelTextEn;
    }

    public String getLabelTextHi() {
        return labelTextHi;
    }

    public String getLabelTextUr() {
        return labelTextUr;
    }

    public String getByLine() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getByLineEn();
            case HINDI:
                return getByLineHi();
            case URDU:
                return getByLineUr();
        }
        return getByLineEn();
    }

    public String getByLineEn() {
        return byLineEn;
    }

    public String getByLineHi() {
        return byLineHi;
    }

    public String getByLineUr() {
        return byLineUr;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getChildId() {
        return childId;
    }

    public int getImageType() {
        return imageType;
    }

    public String getSeq() {
        return seq;
    }

    public int getBannerType() {
        return bannerType;
    }

    public String getTabIndex() {
        return tabIndex;
    }

    public String getMergeToPrev() {
        return mergeToPrev;
    }

    public String getLabelBackgroundColor() {
        return labelBackgroundColor;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public String getPoetNameEn() {
        return poetNameEn;
    }

    public String getPoetNameHi() {
        return poetNameHi;
    }

    public String getPoetNameUr() {
        return poetNameUr;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }
}
