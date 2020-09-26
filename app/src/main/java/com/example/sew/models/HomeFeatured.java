package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFeatured extends BaseSherModel {
    JSONObject jsonObject;
    private int dayType;
    private String entityUrl;
    private String entityName;
    private String poetId;
    private String imageUrl;
    private ArrayList<Para> title;
    private String fromDate;
    private String toDate;
    private String remarks;
    private String destinationUrl;
    private String anchorText;
    private String renderingFormat;
    public static final int DAY_TYPE_NONE = 0;
    public static final int DAY_TYPE_BIRTHDAY = 1;
    public static final int DAY_TYPE_DEATH_ANNIVERSARY = 2;
    private String domicile;
    private String parentSlug;
    private String contentTypeSlug;

    public HomeFeatured(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        dayType = MyHelper.convertToInt(optString(jsonObject, "DT"));
        entityUrl = optString(jsonObject, "PU");
        entityName = optString(jsonObject, "PN");
        poetId = optString(jsonObject, "PI");
        imageUrl = optString(jsonObject, "IU");
        title = getPara(optString(jsonObject, "T"));
        fromDate = optString(jsonObject, "FD");
        toDate = optString(jsonObject, "TD");
        domicile = optString(jsonObject, "DP");
        remarks = optString(jsonObject, "R");
        destinationUrl = optString(jsonObject, "DI");
        anchorText = optString(jsonObject, "AT");
        renderingFormat = optString(jsonObject, "RF");
        parentSlug=optString(jsonObject,"CPS");
        contentTypeSlug=optString(jsonObject,"TS");
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public int getDayType() {
        return dayType;
    }

    public String getEntityUrl() {
        return entityUrl;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getPoetId() {
        return poetId;
    }

    public String getImageUrl() {
        return String.format("%s%s", imageUrl.startsWith("http") ? "" : MyService.getMediaURL(), this.imageUrl);

    }

    public ArrayList<Para> getTitle() {
        return title;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public String getAnchorText() {
        return anchorText;
    }

    public String getRenderingFormat() {
        return renderingFormat;
    }

    public String getPoetTenure() {
        StringBuilder builder = new StringBuilder();
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
            case HINDI:
                builder.append(getFromDate());
                if (!TextUtils.isEmpty(getToDate().trim())) {
                    builder.append(" - ");
                    builder.append(getToDate().trim());
                }
                break;
            case URDU:
                builder.append(getToDate());
                if (!TextUtils.isEmpty(getFromDate().trim())) {
                    builder.append(" - ");
                    builder.append(getFromDate().trim());
                }
                break;
        }

        return builder.toString();
    }

    public String getParentSlug() {
        return parentSlug;
    }

    public String getContentTypeSlug() {
        return contentTypeSlug;
    }

    public String getDomicile() {
        return domicile;
    }
}
