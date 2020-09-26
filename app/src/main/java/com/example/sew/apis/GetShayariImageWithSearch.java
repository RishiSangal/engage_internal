package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ShayariImage;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetShayariImageWithSearch extends Base {


    public GetShayariImageWithSearch() {
        setUrl(MyConstants.getGetShayariImageWithSearch());
        setRequestType(REQUEST_TYPE.POST);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
    }

    public GetShayariImageWithSearch setKeyword(String keyword) {
        addParam("keyword", keyword);
        return this;
    }
    public GetShayariImageWithSearch setTargetIdSlug(String targetIdSlug) {
        addParam("targetIdSlug", targetIdSlug);
        return this;
    }
    public GetShayariImageWithSearch setTargetType(String targetType) {
        addParam("targetType", targetType);
        return this;
    }
  //  Generic = 0, Tag = 1, Occasion = 2, Entity = 3,

    private String description;
    private int totalCount;
    private ArrayList<ShayariImage> shayariImages;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            description = getData().optString("D");
            totalCount = getData().optInt("TC");
            JSONArray imageArray = getData().optJSONArray("P");
            if (imageArray == null)
                imageArray = new JSONArray();
            shayariImages = new ArrayList<>(imageArray.length());
            for (int i = 0; i < imageArray.length(); i++)
                shayariImages.add(new ShayariImage(imageArray.optJSONObject(i)));
        }
    }

    public String getDescription() {
        return description;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ArrayList<ShayariImage> getShayariImages() {
        return shayariImages;
    }
}
