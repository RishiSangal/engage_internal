package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SearchDictionary;

import java.util.ArrayList;

public class GetRekhtaDictionary extends Base {

    public GetRekhtaDictionary() {
        setUrl(MyConstants.getGetDictionaryMeaning());
        setRequestType(REQUEST_TYPE.POST);
    }

    private String searchedKeyword;

    public GetRekhtaDictionary setKeyword(String keyword) {
        this.searchedKeyword = keyword;
        addParam("keyword", keyword);
        return this;
    }

    private ArrayList<SearchDictionary> dictionaries;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            MyService.addSearchKeywordHistory(searchedKeyword);
            int size = getDataArray().length();
            dictionaries = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                dictionaries.add(new SearchDictionary(getDataArray().optJSONObject(i)));
        }
    }

    public ArrayList<SearchDictionary> getDictionaries() {
        return dictionaries;
    }
}
