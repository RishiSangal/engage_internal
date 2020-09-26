package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SearchContentAll;

import org.json.JSONObject;

public class PostSearchOnnLoadDemand extends Base {
    public PostSearchOnnLoadDemand() {
        setUrl(MyConstants.getPostSearchOnloadDemand());
        setRequestType(REQUEST_TYPE.POST);
        addCommonHeaders();
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
    }
    public PostSearchOnnLoadDemand setKeyword(String keyword){
        addParam("keyword",keyword);
        return this;
    }
    private SearchContentAll searchContentAll;
    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONObject data = getData();
        setValidResponse(data != null && data.optString("Message").contentEquals("success"));
        if (isValidResponse()) {
            searchContentAll = new SearchContentAll(data);
        }
    }

    public SearchContentAll getSearchContentAll() {
        return searchContentAll;
    }
}
