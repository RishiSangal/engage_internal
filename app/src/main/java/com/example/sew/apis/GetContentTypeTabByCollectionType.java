package com.example.sew.apis;

import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

public class GetContentTypeTabByCollectionType extends Base {

    public GetContentTypeTabByCollectionType() {
        setUrl(MyConstants.getGetContentTypeTabByCollectionType());
        setRequestType(REQUEST_TYPE.POST);
//        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
    }

    public GetContentTypeTabByCollectionType setCollectionType(Enums.COLLECTION_TYPE collectionType) {
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

    private ArrayList<ContentType> contentTypes;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONArray contentTypeArray = getData().optJSONArray("CT");
        if (contentTypeArray == null)
            contentTypeArray = new JSONArray();
        contentTypes = new ArrayList<>(contentTypeArray.length());
        for (int i = 0; i < contentTypeArray.length(); i++)
            contentTypes.add(new ContentType(contentTypeArray.optJSONObject(i)));
        Collections.sort(contentTypes);
    }

    public ArrayList<ContentType> getContentTypes() {
        return contentTypes;
    }
}
