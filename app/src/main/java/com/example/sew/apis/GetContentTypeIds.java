package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;
import com.example.sew.models.SearchContentAll;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetContentTypeIds extends Base {


    public GetContentTypeIds() {
        //https://world-staging.rekhta.org/api/v5/shayari/GetContentTypeList
        setUrl(MyConstants.getGetContentTypeId());
//        setUrl("http://world.rekhta.org/api/v4/shayari/GetContentTypeList");
        setRequestType(REQUEST_TYPE.POST);
        addParam("lastFetchDate", "");
    }


    private String errorMessage;
    private SearchContentAll searchContentAll;
    private ArrayList<ContentType> allContentTypeList;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            JSONArray contentTypeArray = getData().optJSONArray("R");
            if (contentTypeArray == null)
                contentTypeArray = new JSONArray();
            allContentTypeList = new ArrayList<>(contentTypeArray.length());
            for (int i = 0; i < contentTypeArray.length(); i++) {
                allContentTypeList.add(new ContentType(contentTypeArray.optJSONObject(i)));
                MyService.saveContentType(new ContentType(contentTypeArray.optJSONObject(i)));
            }
            ArrayList<ContentType> customContentTypes = MyHelper.getContentTypes();
            for(ContentType currContentType:customContentTypes){
                allContentTypeList.add(currContentType);
                MyService.saveContentType(currContentType);
            }
//        setValidResponse(data != null && data.optString("Message").contentEquals("success"));
//        if (isValidResponse()) {
//            searchContentAll = new SearchContentAll(data);
//        } else
//            errorMessage = AppErrorMessage.an_error_ocurre;
        }
    }

    public ArrayList<ContentType> getAllContentTypeList() {
        return allContentTypeList;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public SearchContentAll getSearchContent() {
        return searchContentAll;
    }
}
