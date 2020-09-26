package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.AudioContent;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetAudioListWithPaging extends Base {


    public GetAudioListWithPaging() {
        setUrl(MyConstants.getAudioListByPoetWithPaging());
        setRequestType(REQUEST_TYPE.POST);
        addParam("keyword", "");
    }

    public GetAudioListWithPaging setPoetId(String poetId) {
        addParam("poetId", poetId);
        return this;
    }

    private int totalCount;
    private ArrayList<AudioContent> audioContents;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            totalCount = getData().optInt("TC");
            JSONArray poetsArray = getData().optJSONArray("A");
            if (poetsArray == null)
                poetsArray = new JSONArray();
            audioContents = new ArrayList<>(poetsArray.length());
            for (int i = 0; i < poetsArray.length(); i++)
                audioContents.add(new AudioContent(poetsArray.optJSONObject(i)));
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ArrayList<AudioContent> getAudioContents() {
        return audioContents;
    }
}
