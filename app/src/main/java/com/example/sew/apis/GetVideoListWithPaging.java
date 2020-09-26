package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.VideoContent;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetVideoListWithPaging extends Base {


    public GetVideoListWithPaging() {
        setUrl(MyConstants.getVideoListByPoetWithPaging());
        setRequestType(REQUEST_TYPE.POST);
        addParam("keyword", "");
    }

    public GetVideoListWithPaging setPoetId(String poetId) {
        addParam("poetId", poetId);
        return this;
    }

    private int totalCount;
    private ArrayList<VideoContent> videoContents;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            totalCount = getData().optInt("TC");
            JSONArray poetsArray = getData().optJSONArray("V");
            if (poetsArray == null)
                poetsArray = new JSONArray();
            videoContents = new ArrayList<>(poetsArray.length());
            for (int i = 0; i < poetsArray.length(); i++)
                videoContents.add(new VideoContent(poetsArray.optJSONObject(i)));
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ArrayList<VideoContent> getVideoContents() {
        return videoContents;
    }
}
