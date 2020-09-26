package com.example.sew.apis;

import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.MyConstants;
import com.example.sew.models.SearchContentAll;

import org.json.JSONObject;

public class GetContentTypeIds extends Base {


    public GetContentTypeIds() {
        //https://world-staging.rekhta.org/api/v5/shayari/GetContentTypeList
        setUrl(MyConstants.getGetContentTypeId());
//        setUrl("http://world.rekhta.org/api/v4/shayari/GetContentTypeList");
        setRequestType(REQUEST_TYPE.POST);
        addParam("lastFetchDate", "13-10-1988");
    }


    private String errorMessage;
    private SearchContentAll searchContentAll;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONObject data = getData();
        setValidResponse(data != null && data.optString("Message").contentEquals("success"));
        if (isValidResponse()) {
            searchContentAll = new SearchContentAll(data);
        } else
            errorMessage = AppErrorMessage.an_error_ocurre;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public SearchContentAll getSearchContent() {
        return searchContentAll;
    }
}
