package com.example.sew.apis;

import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.AppCollection;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetCollectionListByCollectionType extends Base {

    public GetCollectionListByCollectionType() {
        setUrl(MyConstants.getGetCollectionListByCollectionType());
        setRequestType(REQUEST_TYPE.POST);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
        addParam("keyword", "");
    }

    public GetCollectionListByCollectionType setContentTypeId(String contentTypeId) {
        addParam("contentTypeId", contentTypeId);
        return this;
    }

    public GetCollectionListByCollectionType setCollectionType(Enums.COLLECTION_TYPE collectionType) {
        switch (collectionType) {
            case PROSE:
                addParam("collectionType", "2");
                break;
            case SHAYARI:
                addParam("collectionType", "1");
                break;
        }
        return this;
    }

    private ArrayList<AppCollection> appCollections;
    private String description;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        description = getData().optString("DE");
        JSONArray contentListArray = getData().optJSONArray("CL");
        if (contentListArray == null)
            contentListArray = new JSONArray();
        appCollections = new ArrayList<>(contentListArray.length());
        for (int i = 0; i < contentListArray.length(); i++)
            appCollections.add(new AppCollection(contentListArray.optJSONObject(i)));
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<AppCollection> getAppCollections() {
        return appCollections;
    }
}
