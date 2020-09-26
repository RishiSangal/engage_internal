package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;

import org.json.JSONObject;

public class SearchContent extends BaseModel {

    private String Body;
    private String Id;
    private String TypeId;
    private String ContentSlug;
    private String PoetId;
    private String PoetSlug;
    private String PoetName;
    private String ImageUrl;
    private String ContentUrl;
    private String AudioCount;
    private String VideoCount;
    private boolean EditorChoice;
    private boolean PopularChoice;
    private String ShortUrlIndex;
    private String Title;

    public SearchContent(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        Body = optString(jsonObject, "Body");
        Id = optString(jsonObject, "Id");
        TypeId = optString(jsonObject, "TypeId");
        ContentSlug = optString(jsonObject, "ContentSlug");
        PoetId = optString(jsonObject, "PoetId");
        PoetSlug = optString(jsonObject, "PoetSlug");
        PoetName = optString(jsonObject, "PoetName");
        ImageUrl = optString(jsonObject, "ImageUrl");
        ContentUrl = optString(jsonObject, "ContentUrl");
        AudioCount = optString(jsonObject, "AudioCount");
        VideoCount = optString(jsonObject, "VideoCount");
        EditorChoice = optString(jsonObject, "EditorChoice").contentEquals("true");
        PopularChoice = optString(jsonObject, "PopularChoice").contentEquals("true");
        ShortUrlIndex = optString(jsonObject, "ShortUrlIndex");
        Title = optString(jsonObject, "Title");
    }

    public ContentType getContentTypeId() {
        return MyHelper.getContentById(getTypeId());
    }

    public String getBody() {
        return Body;
    }

    public String getId() {
        return Id;
    }

    public String getTypeId() {
        return TypeId;
    }

    public String getContentSlug() {
        return ContentSlug;
    }

    public String getPoetId() {
        return PoetId;
    }

    public String getPoetSlug() {
        return PoetSlug;
    }

    public String getPoetName() {
        return PoetName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getContentUrl() {
        return ContentUrl;
    }

    public String getAudioCount() {
        return AudioCount;
    }

    public int getAudioCountInt() {
        return MyHelper.convertToInt(getAudioCount());
    }

    public String getVideoCount() {
        return VideoCount;
    }

    public int getVideoCountInt() {
        return MyHelper.convertToInt(getVideoCount());
    }

    public boolean isEditorChoice() {
        return EditorChoice;
    }

    public boolean isPopularChoice() {
        return PopularChoice;
    }

    public String getShortUrlIndex() {
        return ShortUrlIndex;
    }

    public String getTitle() {
        return Title;
    }
}