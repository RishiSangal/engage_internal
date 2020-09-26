package com.example.sew.apis;

import android.util.Log;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SherTag;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetTagLists extends Base {


    public GetTagLists() {
        setUrl(MyConstants.getGetTagsList());
        setRequestType(REQUEST_TYPE.POST);
        setLang(String.valueOf(MyService.getSelectedLanguageInt()));
    }

    public GetTagLists setLang(String lang) {
        addParam("lang", lang);
        return this;
    }

    private ArrayList<SherTag> topTags = new ArrayList<>();
    private ArrayList<String> tagsSections = new ArrayList<>();
    private HashMap<String, ArrayList<SherTag>> allTags = new HashMap<>();

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            JSONArray topTagsArray = getData().optJSONArray("TT");
            if (topTagsArray == null)
                topTagsArray = new JSONArray();
            topTags = new ArrayList<>(topTagsArray.length());
            for (int i = 0; i < topTagsArray.length(); i++) {
                SherTag sherTag = new SherTag(topTagsArray.optJSONObject(i));
                sherTag.setTagColor(MyHelper.getTagColor(i));
                topTags.add(sherTag);
            }
            JSONArray tagsListArray = getData().optJSONArray("TL");
            if (tagsListArray == null)
                tagsListArray = new JSONArray();
            allTags = new HashMap<>(tagsListArray.length());
            tagsSections = new ArrayList<>(tagsListArray.length());
            for (int i = 0; i < tagsListArray.length(); i++) {
                JSONObject tagsObject = tagsListArray.optJSONObject(i);
                if (tagsObject == null)
                    tagsObject = new JSONObject();
                String sectionName = tagsObject.optString("TG");
                JSONArray sectionTagsArray = tagsObject.optJSONArray("TS");
                tagsSections.add(sectionName);
                if (sectionTagsArray == null)
                    sectionTagsArray = new JSONArray();
                ArrayList<SherTag> sectionTags = new ArrayList<>(sectionTagsArray.length());
                for (int j = 0; j < sectionTagsArray.length(); j++) {
                    SherTag sherTag = new SherTag(sectionTagsArray.optJSONObject(j));
                    sherTag.setTagColor(MyHelper.getTagColor(i));
                    sectionTags.add(sherTag);
                }
                allTags.put(sectionName, sectionTags);
            }
        }
        Log.d("sdf", "sdf");
    }

    public ArrayList<SherTag> getTopTags() {
        return topTags;
    }

    public HashMap<String, ArrayList<SherTag>> getAllTags() {
        return allTags;
    }

    public ArrayList<String> getTagsSections() {
        return tagsSections;
    }
}
