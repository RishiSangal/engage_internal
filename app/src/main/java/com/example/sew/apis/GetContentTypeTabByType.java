package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;
import com.example.sew.models.ContentTypeTab;
import com.example.sew.models.CumulatedContentType;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetContentTypeTabByType extends Base {
    public GetContentTypeTabByType() {
        setUrl(MyConstants.getGetContentTypeTabbyType());
        setRequestType(REQUEST_TYPE.GET);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
    }
    public GetContentTypeTabByType setTargetIdSlug(String targetIdSlug){
        addParam("targetIdSlug",targetIdSlug);
        return this;
    }
    public GetContentTypeTabByType setTargetType(String targetType){
        addParam("targetType",targetType);
        return this;
    }

    private ContentTypeTab contentTypeTab;
    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if(isValidResponse()){
            contentTypeTab= new ContentTypeTab(getData());
        }
    }

    public ContentTypeTab getContentTypeTab() {
        return contentTypeTab;
    }
}
