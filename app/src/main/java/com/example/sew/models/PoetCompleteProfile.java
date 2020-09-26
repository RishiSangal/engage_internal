package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PoetCompleteProfile extends BaseModel {
    private PoetDetailContentHeader poetDetailContentHeader;
    private PoetDetail poetDetail;
    private PoetSignatureSher poetSignatureSher;
    private ArrayList<PoetRelatedEntity> poetRelatedEntities;
    private ArrayList<PoetUsefulLink> poetUsefulLinks;
    private int ghazalCount, nazmCount, sherCount, geetCount, rubaaiCount, qitaCount, doheCount;
    private JSONObject jsonObject;
    private ArrayList<ContentType> contentTypes;

    public PoetCompleteProfile(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        poetDetail = new PoetDetail(jsonObject.optJSONObject("EP"));
        poetSignatureSher = new PoetSignatureSher(jsonObject.optJSONObject("SS"));
        poetDetailContentHeader = new PoetDetailContentHeader(jsonObject.optJSONObject("CH"));
        poetDetail.setContentHeader(poetDetailContentHeader);
        JSONArray jsonArray = jsonObject.optJSONArray("PR");
        if (jsonArray == null)
            jsonArray = new JSONArray();
        int size = jsonArray.length();
        poetRelatedEntities = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            poetRelatedEntities.add(new PoetRelatedEntity(jsonArray.optJSONObject(i)));
        jsonArray = jsonObject.optJSONArray("UL");
        if (jsonArray == null)
            jsonArray = new JSONArray();
        size = jsonArray.length();
        poetUsefulLinks = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            poetUsefulLinks.add(new PoetUsefulLink(jsonArray.optJSONObject(i)));

        JSONArray countArray = jsonObject.optJSONArray("CS");
        if (countArray == null)
            countArray = new JSONArray();
        contentTypes = new ArrayList<>(countArray.length());
        for (int i = 0; i < countArray.length(); i++) {//geetCount,rubaaiCount,qitaCount,doheCount
            String id = countArray.optJSONObject(i).optString("I");
            ContentType contentType = MyHelper.getContentById(id);
            if (!TextUtils.isEmpty(contentType.getContentId()))
                contentTypes.add(contentType);
            if (MyConstants.GHAZAL_ID.equalsIgnoreCase(id)) {
                ghazalCount = countArray.optJSONObject(i).optInt("C");
            } else if (MyConstants.GHAZAL_ID_1.equalsIgnoreCase(id)) {
                ghazalCount = countArray.optJSONObject(i).optInt("C");
            } else if (MyConstants.SHER_ID.equalsIgnoreCase(id)) {
                sherCount = countArray.optJSONObject(i).optInt("C");
            } else if (MyConstants.NAZM_ID.equalsIgnoreCase(id)) {
                nazmCount = countArray.optJSONObject(i).optInt("C");
            } else if (MyConstants.GEET_ID.equalsIgnoreCase(id)) {
                geetCount = countArray.optJSONObject(i).optInt("C");
            } else if (MyConstants.RUBAAI_ID.equalsIgnoreCase(id)) {
                rubaaiCount = countArray.optJSONObject(i).optInt("C");
            } else if (MyConstants.QITA_ID.equalsIgnoreCase(id)) {
                qitaCount = countArray.optJSONObject(i).optInt("C");
            } else if (MyConstants.DOHA_ID.equalsIgnoreCase(id)) {
                doheCount = countArray.optJSONObject(i).optInt("C");
            }
        }
        poetDetail.setCountArray(countArray);
        poetDetail.setGhazalCount(ghazalCount);
        poetDetail.setNazmCount(nazmCount);
        poetDetail.setSherCount(sherCount);
    }

    public PoetDetail getPoetDetail() {
        return poetDetail;
    }

    public PoetSignatureSher getPoetSignatureSher() {
        return poetSignatureSher;
    }

    public ArrayList<PoetRelatedEntity> getPoetRelatedEntities() {
        return poetRelatedEntities;
    }

    public ArrayList<PoetUsefulLink> getPoetUsefulLinks() {
        return poetUsefulLinks;
    }

    public int getGhazalCount() {
        return ghazalCount;
    }

    public int getNazmCount() {
        return nazmCount;
    }

    public int getSherCount() {
        return sherCount;
    }

    public int getGeetCount() {
        return geetCount;
    }

    public int getRubaaiCount() {
        return rubaaiCount;
    }

    public int getQitaCount() {
        return qitaCount;
    }

    public int getDoheCount() {
        return doheCount;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public PoetDetailContentHeader getPoetDetailContentHeader() {
        return poetDetailContentHeader;
    }

    public ArrayList<ContentType> getContentTypes() {
        return contentTypes;
    }
}
