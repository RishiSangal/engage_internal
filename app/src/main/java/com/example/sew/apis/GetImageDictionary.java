package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.PlattsDictionary;

import java.util.ArrayList;

public class GetImageDictionary extends Base {

    public GetImageDictionary() {
        setUrl(MyConstants.getGetImageDictionaryMeaning());
        setRequestType(REQUEST_TYPE.POST);
    }

    public GetImageDictionary setKeyword(String keyword) {
        addParam("keyword", keyword);
        return this;
    }

    private ArrayList<PlattsDictionary> dictionaries;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            int size = getDataArray().length();
            dictionaries = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                dictionaries.add(new PlattsDictionary(getDataArray().optJSONObject(i)));
        }
    }

    public ArrayList<PlattsDictionary> getDictionaries() {
        return dictionaries;
    }
}
