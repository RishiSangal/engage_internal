package com.example.sew.apis;

import com.example.sew.common.MyConstants;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetAllfavoriteId extends Base {

    public GetAllfavoriteId() {
        setUrl(MyConstants.getGetAllFavoriteId());
        setRequestType(REQUEST_TYPE.GET);
        addCommonHeaders();
    }

    ArrayList<String> allFavoriteIds = new ArrayList<>();
    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            JSONArray jsonArray = getData().optJSONArray("R");
            for (int i = 0; i < jsonArray.length(); i++) {
                allFavoriteIds.add(jsonArray.optString(i));
            }

        }
    }

    public ArrayList<String> getAllFavoriteIds() {
        return allFavoriteIds;
    }
}
