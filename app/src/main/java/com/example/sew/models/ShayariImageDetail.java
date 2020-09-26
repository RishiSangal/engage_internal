package com.example.sew.models;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShayariImageDetail extends BaseModel {
    private String id;
    private String contentId;
    private String title;
    private String content;
    private String seoSlug;
    private String poetId;
    private String poetName;
    private String poetUrl;
    private String poetSlug;
    private String contentSlug;
    private String typeSlug;
    private String parentSlug;
    private String parentTypeSlug;
    private String shortUrlIndex;
    private int shareCount;
    private int favoriteCount;
    private boolean isNew;
    private String haveDifferentTitle;
    private String renderingFormat;
    private String previousId;
    private String nextId;
    private JSONObject jsonObject;
    private ArrayList<SherTag> sherTags;
    private ArrayList<Para> paras;
    private String imageLocalSavedPath;

    public ShayariImageDetail(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        id = optString(jsonObject, "I");
        contentId = optString(jsonObject, "CI");
        title = optString(jsonObject, "TI");
        content = optString(jsonObject, "C");
        seoSlug = optString(jsonObject, "IS");
        poetId = optString(jsonObject, "EI");
        poetName = optString(jsonObject, "EN");
        poetUrl = optString(jsonObject, "EU");
        poetSlug = optString(jsonObject, "ES");
        contentSlug = optString(jsonObject, "CS");
        typeSlug = optString(jsonObject, "TS");
        parentSlug = optString(jsonObject, "PS");
        parentTypeSlug = optString(jsonObject, "PT");
        shortUrlIndex = optString(jsonObject, "SI");
        isNew = optString(jsonObject, "IN").equalsIgnoreCase("true");
        haveDifferentTitle = optString(jsonObject, "DT");
        renderingFormat = optString(jsonObject, "RF");
        previousId = optString(jsonObject, "PI");
        nextId = optString(jsonObject, "NI");
        favoriteCount = MyHelper.convertToInt(optString(jsonObject, "FC"));
        shareCount = MyHelper.convertToInt(optString(jsonObject, "SC"));
        JSONArray jsonArray = jsonObject.optJSONArray("TA");
        if (jsonArray == null)
            jsonArray = new JSONArray();
        int size = jsonArray.length();
        sherTags = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            JSONObject sherTagObj = jsonArray.optJSONObject(i);
            if (sherTagObj == null)
                sherTagObj = new JSONObject();
//            try {
//                sherTagObj.put("I", sherTagObj.optString("CoupletId"));
//                sherTagObj.put("N", sherTagObj.optString("TagName"));
//                sherTagObj.put("S", sherTagObj.optString("TagSlug"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            sherTags.add(new SherTag(sherTagObj));
        }
        JSONObject paraObject;
        try {
            paraObject = new JSONObject(jsonObject.optString("C"));
        } catch (JSONException e) {
            e.printStackTrace();
            paraObject = new JSONObject();
        }
        JSONArray paraArray = paraObject.optJSONArray("P");
        if (paraArray == null) {
            paraArray = new JSONArray();
        }
        size = paraArray.length();
        this.paras = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.paras.add(new Para(paraArray.optJSONObject(i)));
        }
        imageLocalSavedPath = jsonObject.optString("imageLocalSavedPath");
    }

    public ArrayList<Para> getParas() {
        return paras;
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getImageUrl() {
        return String.format(MyConstants.getShayariImageUrl(), getSeoSlug());
    }

    public String getId() {
        return id;
    }

    public String getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSeoSlug() {
        return seoSlug;
    }

    public String getPoetId() {
        return poetId;
    }

    public String getPoetName() {
        return poetName;
    }

    public String getPoetUrl() {
        return poetUrl;
    }

    public String getPoetSlug() {
        return poetSlug;
    }

    public String getContentSlug() {
        return contentSlug;
    }

    public String getTypeSlug() {
        return typeSlug;
    }

    public String getParentSlug() {
        return parentSlug;
    }

    public String getParentTypeSlug() {
        return parentTypeSlug;
    }

    public String getShortUrlIndex() {
        return shortUrlIndex;
    }

    public int getShareCount() {
        return shareCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public boolean isNew() {
        return isNew;
    }

    public String getHaveDifferentTitle() {
        return haveDifferentTitle;
    }

    public String getRenderingFormat() {
        return renderingFormat;
    }

    public String getPreviousId() {
        return previousId;
    }

    public String getNextId() {
        return nextId;
    }

    public ArrayList<SherTag> getSherTags() {
        return sherTags;
    }

    public void setLocalPath(String imageLocalSavedPath) {
        this.imageLocalSavedPath = imageLocalSavedPath;
        updateKey(jsonObject, "imageLocalSavedPath", imageLocalSavedPath);
    }

    public String getImageLocalSavedPath() {
        return imageLocalSavedPath;
    }
}
