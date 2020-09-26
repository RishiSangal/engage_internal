package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.WordOfTheDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GetWordOfTheDay extends Base {


    public GetWordOfTheDay() {
        setUrl(MyConstants.getGetWordOfTheDay());
        setRequestType(REQUEST_TYPE.GET);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c.getTime());
        addParam("displayDate", formattedDate);
    }


    private WordOfTheDay wordOfTheDay;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            wordOfTheDay = new WordOfTheDay(getData());
        }
    }

    public WordOfTheDay getWordOfTheDay() {
        return wordOfTheDay;
    }
}
