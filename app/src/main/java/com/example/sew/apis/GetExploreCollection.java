package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.HomeImageTag;
import com.example.sew.models.HomeProseCollection;
import com.example.sew.models.HomeTopPoet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetExploreCollection extends Base {


    public GetExploreCollection() {
        setUrl(MyConstants.getGetExploreCollection());
        setRequestType(REQUEST_TYPE.POST);
    }

    public GetExploreCollection setCommonParams() {
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
        return this;
    }

    private ArrayList<HomeTopPoet> topPoets;
    private ArrayList<HomeProseCollection> explorePoetry;
    private ArrayList<HomeImageTag> tags;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONObject data = getData();
        if (isValidResponse()) {
            JSONArray array;
            int size = 0;

            array = getArray("ExplorePoetry");
            size = array.length();
            explorePoetry = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                explorePoetry.add(new HomeProseCollection(array.optJSONObject(i)));


            array = getArray("TopPoets");
            size = array.length();
            topPoets = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                topPoets.add(new HomeTopPoet(array.optJSONObject(i)));

            array = getArray("ExploreTags");
            size = array.length();
            tags = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                tags.add(new HomeImageTag(array.optJSONObject(i)));
//            ==========================================================

        }
    }

    private JSONArray getArray(String key) {
        JSONArray array = getData().optJSONArray(key);
        if (array == null)
            array = new JSONArray();
        return array;
    }

    public ArrayList<HomeTopPoet> getTopPoets() {
        return topPoets;
    }

    public ArrayList<HomeProseCollection> getExplorePoetry() {
        return explorePoetry;
    }

    public ArrayList<HomeImageTag> getTags() {
        return tags;
    }
}
