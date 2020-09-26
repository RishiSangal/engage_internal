package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.WordMeaning;

public class GetWordMeaning extends Base {

    public GetWordMeaning() {
        setUrl(MyConstants.getGetWorldMeaning());
        setRequestType(REQUEST_TYPE.POST);
    }

    public GetWordMeaning setWord(String word) {
        addParam("word", word);
        return this;
    }

    private WordMeaning wordMeaning;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            wordMeaning = new WordMeaning(getData());
        }
    }

    public WordMeaning getWordMeaning() {
        return wordMeaning;
    }
}
