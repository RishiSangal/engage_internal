package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentPageModel;

import org.json.JSONObject;

public class GetContentById extends Base {


    public GetContentById() {
        setUrl(MyConstants.getGetContentById());
        setRequestType(REQUEST_TYPE.GET);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
    }

    public GetContentById setContentId(String contentId) {
        addParam("contentId", contentId);
        return this;
    }

    private ContentPageModel contentPageModel;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONObject data = getData();
        if (isValidResponse()) {
            contentPageModel = new ContentPageModel(data);
        }
    }

    public ContentPageModel getContentPageModel() {
        return contentPageModel;
    }


}
