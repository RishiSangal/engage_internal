package com.example.sew.apis;

import android.text.TextUtils;
import android.util.Log;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ShayariContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetContentListWithPaging extends Base {


    public GetContentListWithPaging() {
        setUrl(MyConstants.getContentListPaging());
        setRequestType(REQUEST_TYPE.POST);
        setTargetId("");
        setKeyword("");
        setPoetId("");
        setContentTypeId("");
        addCommonHeaders();
    }

    public GetContentListWithPaging setPoetId(String poetId) {
        addParam("poetId", poetId);
        return this;
    }

    public GetContentListWithPaging setContentTypeId(String contentTypeId) {
        addParam("contentTypeId", contentTypeId);
        return this;
    }

    public GetContentListWithPaging setTargetId(String targetId) {
        addParam("targetId", targetId);
        return this;
    }

    public GetContentListWithPaging setKeyword(String keyword) {
        addParam("keyword", keyword);
        return this;
    }

    public GetContentListWithPaging setSortBy(String sortContent) {
        addParam("sortBy", sortContent);
        return this;
    }


    private int totalCount;
    private ArrayList<ShayariContent> shayariContents;
    private String nameEn, nameHi, nameUr;
    private String descriptionEn, descriptionHi, descriptionUr, slug;
    private String favCount, shareUrlEnglish, shareUrlHin, shareUrlUrdu;
    JSONArray jsonArray1;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            totalCount = getData().optInt("TC");
            favCount = getData().optString("FC");
            shareUrlEnglish = getData().optString("UE");
            shareUrlHin = getData().optString("UH");
            shareUrlUrdu = getData().optString("UU");
            JSONArray poetsArray = getData().optJSONArray("CS");
            if (poetsArray == null)
                poetsArray = new JSONArray();
            shayariContents = new ArrayList<>(poetsArray.length());
            for (int i = 0; i < poetsArray.length(); i++)
                shayariContents.add(new ShayariContent(poetsArray.optJSONObject(i)));

            JSONArray jsonArray = getData().optJSONArray("SI");
            if (jsonArray == null)
                jsonArray = new JSONArray();
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                nameEn = jsonObject.optString("NE");
                nameHi = jsonObject.optString("NH");
                nameUr = jsonObject.optString("NU");
                descriptionEn = jsonObject.optString("DE");
                descriptionHi = jsonObject.optString("DH");
                descriptionUr = jsonObject.optString("DU");
                slug = jsonObject.optString("S");
            }
            jsonArray1 = new JSONArray();
            for (int i = 0; i < shayariContents.size(); i++) {
                try {
                    jsonArray1.put(new JSONObject().put("target_id", shayariContents.get(i).getId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("", "");
        }
    }

    private String getNameEn() {
        return TextUtils.isEmpty(nameEn) ? "" : nameEn;
    }

    private String getNameHi() {
        return TextUtils.isEmpty(nameHi) ? "" : nameHi;
    }

    private String getNameUr() {
        return TextUtils.isEmpty(nameUr) ? "" : nameUr;
    }

    public String getName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getNameEn();
            case HINDI:
                return getNameHi();
            case URDU:
                return getNameUr();
        }
        return "";
    }

    public String getDescription() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return descriptionEn;
            case HINDI:
                return descriptionHi;
            case URDU:
                return descriptionUr;
        }
        return "";
    }

    public String getShareUrl() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getShareUrlEnglish();
            case HINDI:
                return getShareUrlHin();
            case URDU:
                return getShareUrlUrdu();
        }
        return getShareUrlEnglish();
    }

    public String getShareUrlEnglish() {
        return shareUrlEnglish;
    }

    public String getShareUrlHin() {
        return shareUrlHin;
    }

    public String getShareUrlUrdu() {
        return shareUrlUrdu;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getSlug() {
        return slug;
    }

    public ArrayList<ShayariContent> getShayariContents() {
        return shayariContents;
    }

    public String getFavCount() {
        return favCount;
    }
}
