package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.SherCollection;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetT20Shers extends Base {


    public GetT20Shers() {
        setUrl(MyConstants.getGetT20Sher());
        setRequestType(REQUEST_TYPE.POST);
    }

    private ArrayList<SherCollection> sherCollections;
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            JSONArray poetsArray = getData().optJSONArray("R");
            if (poetsArray == null)
                poetsArray = new JSONArray();
            sherCollections = new ArrayList<>(poetsArray.length());
            for (int i = 0; i < poetsArray.length(); i++)
                sherCollections.add(new SherCollection(poetsArray.optJSONObject(i)));
        }
    }

    public ArrayList<SherCollection> getSherCollections() {
        return sherCollections;
    }
}
