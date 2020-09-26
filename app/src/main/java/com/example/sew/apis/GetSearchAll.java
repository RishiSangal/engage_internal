package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SearchContentAll;

import org.json.JSONObject;

public class GetSearchAll extends Base {


    public GetSearchAll() {
        setUrl(MyConstants.getSearchAll());
        setRequestType(REQUEST_TYPE.POST);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
        addCommonHeaders();
        setKeyword("");
    }

    private String searchedKeyword;

    public GetSearchAll setKeyword(String keyword) {
        this.searchedKeyword = keyword;
        addParam("keyword", keyword);
        return this;
    }


    private String errorMessage;
    private SearchContentAll searchContentAll;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONObject data = getData();
        setValidResponse(data != null && data.optString("Message").contentEquals("success"));
        if (isValidResponse()) {
            searchContentAll = new SearchContentAll(data);
            MyService.addSearchKeywordHistory(searchedKeyword);
        } else
            errorMessage = MyHelper.getErrorMessage(getData());
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public SearchContentAll getSearchContent() {
        return searchContentAll;
    }
}
