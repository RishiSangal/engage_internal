package com.example.sew.apis;

import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SearchContent;
import com.example.sew.models.SearchContentAll;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetSearchContentByType extends Base {


    public GetSearchContentByType() {
        setUrl(MyConstants.getSearchContentByType());
        setRequestType(REQUEST_TYPE.POST);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
        setKeyword("");
        addCommonHeaders();
    }

    public GetSearchContentByType setKeyword(String keyword) {
        addParam("keyword", keyword);
        return this;
    }

    public GetSearchContentByType setContentType(Enums.CONTENT_TYPE contentType) {
        String type = "";
        switch (contentType) {
            case GHAZAL:
                type = "3";
                break;
            case NAZM:
                type = "4";
                break;
            case SHER:
                type = "5";
                break;
            case POET:
                type="1";
                break;

        }
        addParam("type", type);
        return this;
    }

    private String errorMessage;
    private ArrayList<SearchContent> searchContents;
    private int totalCount;
    private SearchContentAll searchContentAll;
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONObject data = getData();
        setValidResponse(data != null && data.optString("Message").contentEquals("success"));
        if (isValidResponse()) {
            searchContentAll = new SearchContentAll(data);
            totalCount = MyHelper.convertToInt(getData().optString("ResultsTotal"));
            JSONArray jsonArray = getData().optJSONArray("Results");
            if (jsonArray == null)
                jsonArray = new JSONArray();
            int size = jsonArray.length();
            searchContents = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                searchContents.add(new SearchContent(jsonArray.optJSONObject(i)));
        } else
            errorMessage = MyHelper.getErrorMessage(getData());
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public ArrayList<SearchContent> getSearchContents() {
        return searchContents;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public SearchContentAll getSearchContentAll() {
        return searchContentAll;
    }
}
