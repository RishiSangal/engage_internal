package com.example.sew.apis;

import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SherContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetCoupletListWithPaging extends Base {


    public GetCoupletListWithPaging() {
        setUrl(MyConstants.getCoupletListWithPaging());
        setRequestType(REQUEST_TYPE.POST);
        setTargetId("");
        setPoetId("");
        addParam("keyword", "");
    }

    public GetCoupletListWithPaging setPoetId(String poetId) {
        addParam("poetId", poetId);
        return this;
    }

    public GetCoupletListWithPaging setTargetId(String targetId) {
        addParam("targetId", targetId);
        return this;
    }

    public GetCoupletListWithPaging setContentTypeId(String contentTypeId) {
        addParam("contentTypeId", contentTypeId);
        return this;
    }
    public GetCoupletListWithPaging setSortBy(String sort_content){
        addParam("sortBy",sort_content);
        return this;
    }

    private int totalCount;
    private ArrayList<SherContent> sherContents;
    private String nameHi = "", nameEn = "", nameUr = "";

    /*
    "SI": [
          {
            "I": "ebd62f8a-f4e8-4b65-a1c2-24e48422fddc",
            "NE": "New 20Top",
            "NH": "New 20Top",
            "NU": "New 20Top",
            "S": "new-20top"
          }
        ]
     */
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            totalCount = getData().optInt("TC");
            JSONArray poetsArray = getData().optJSONArray("CD");
            if (poetsArray == null)
                poetsArray = new JSONArray();
            sherContents = new ArrayList<>(poetsArray.length());
            for (int i = 0; i < poetsArray.length(); i++)
                sherContents.add(new SherContent(poetsArray.optJSONObject(i)));


            JSONArray jsonArray = getData().optJSONArray("SI");
            if (jsonArray == null)
                jsonArray = new JSONArray();
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                nameEn = jsonObject.optString("NE");
                nameHi = jsonObject.optString("NH");
                nameUr = jsonObject.optString("NU");
            }

        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ArrayList<SherContent> getSherContents() {
        return sherContents;
    }

    public String getName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return nameEn;
            case HINDI:
                return nameHi;
            case URDU:
                return nameUr;
        }
        return "";
    }
}
