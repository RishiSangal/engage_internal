package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.Poet;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetPoetLists extends Base {


    public GetPoetLists() {
        setUrl(MyConstants.getPoetListWithPaging());
        setRequestType(REQUEST_TYPE.POST);
        addParam("lastFetchDate", "");
    }

    public GetPoetLists setTargetId(String targetId) {
        addParam("targetId", targetId);
        return this;
    }

    public GetPoetLists setKeyword(String keyword) {
        addParam("keyword", keyword);
        return this;
    }

//    public GetPoetLists setPageIndex(String pageIndex) {
//        addParam("pageIndex", pageIndex);
//        return this;
//    }

    private int totalCount;
    private ArrayList<Poet> poets = new ArrayList<>();

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            totalCount = getData().optInt("TC");
            JSONArray poetsArray = getData().optJSONArray("P");
            if (poetsArray == null)
                poetsArray = new JSONArray();
            poets = new ArrayList<>(poetsArray.length());
            for (int i = 0; i < poetsArray.length(); i++)
                poets.add(new Poet(poetsArray.optJSONObject(i)));
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ArrayList<Poet> getPoets() {
        return poets;
    }
}
