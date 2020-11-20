package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

import static com.example.sew.common.MyConstants.DOHA_ID;
import static com.example.sew.common.MyConstants.NAZM_ID;
import static com.example.sew.common.MyConstants.SHER_ID;

public class CumulatedContentType extends BaseModel {
    JSONObject jsonObject;
    private String typeId;
    private String contentType;
    private String typeSlug;
    private int totalContent;
    private int seq;
    private int listingType;

    private String contentNameEn, contentNameHi, contentNameUr;

    public CumulatedContentType(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        typeId = optString(jsonObject, "TypeId");
        contentNameEn = optString(jsonObject, "Name_En");
        contentNameHi = optString(jsonObject, "Name_Hi");
        contentNameUr = optString(jsonObject, "Name_Ur");
        contentType = optString(jsonObject, "ContentType");
        typeSlug = optString(jsonObject, "TypeSlug");
        totalContent = MyHelper.convertToInt(optString(jsonObject, "TotalContent"));
        seq = MyHelper.convertToInt(optString(jsonObject, "Seq"));
        listingType = MyHelper.convertToInt(optString(jsonObject, "ListingType"));
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getContentType() {
        return contentType;
    }

    public String getTypeSlug() {
        return typeSlug;
    }

    public int getTotalContent() {
        return totalContent;
    }

    public int getSeq() {
        return seq;
    }

    public int getListingType() {
        return listingType;
    }


    public Enums.LIST_RENDERING_FORMAT getListRenderingFormat() {
        if (getListingType() == 1) {
            if (NAZM_ID.contentEquals(getTypeId()))
                return Enums.LIST_RENDERING_FORMAT.NAZM;
            else
                return Enums.LIST_RENDERING_FORMAT.GHAZAL;
        } else if (getListingType() == 2) {
            if (SHER_ID.contentEquals(getTypeId()) || DOHA_ID.contentEquals(getTypeId()))
                return Enums.LIST_RENDERING_FORMAT.SHER;
            else
                return Enums.LIST_RENDERING_FORMAT.QUOTE;

        }
//        else if (getListingType() == 2)
//            return Enums.LIST_RENDERING_FORMAT.SHER;
        else
            return Enums.LIST_RENDERING_FORMAT.IMAGE_SHAYRI;
        // return Enums.LIST_RENDERING_FORMAT.NAZM;
    }

    public String getContentNameEn() {
        return contentNameEn;
    }

    public String getContentNameHi() {
        return contentNameHi;
    }

    public String getContentNameUr() {
        return contentNameUr;
    }

    public String getContentTypeName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getContentNameEn();
            case HINDI:
                return getContentNameHi();
            case URDU:
                return getContentNameUr();
        }
        return contentNameEn;
    }
}
