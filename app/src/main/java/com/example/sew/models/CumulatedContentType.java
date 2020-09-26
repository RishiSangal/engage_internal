package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;

import org.json.JSONObject;

import static com.example.sew.common.MyConstants.GHAZAL_ID;
import static com.example.sew.common.MyConstants.NAZM_ID;

public class CumulatedContentType extends BaseModel {
    JSONObject jsonObject;
    private String typeId;
    private String contentType;
    private String typeSlug;
    private int totalContent;
    private int seq;
    private int listingType;


    public CumulatedContentType(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        typeId = optString(jsonObject, "TypeId");
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
        } else if (getListingType() == 2)
            return Enums.LIST_RENDERING_FORMAT.SHER;
        else
            return Enums.LIST_RENDERING_FORMAT.IMAGE_SHAYRI;
        // return Enums.LIST_RENDERING_FORMAT.NAZM;
    }
}
