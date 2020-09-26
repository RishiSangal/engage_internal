package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.OccasionCollection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetOccasions extends Base {


    public GetOccasions() {
        setUrl(MyConstants.getGetOccasions());
        setRequestType(REQUEST_TYPE.POST);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
    }

    private ArrayList<OccasionCollection> occasionCollections;
    private String description = "";

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            JSONObject jsonObject = getData().optJSONObject("R");
            if (jsonObject == null)
                jsonObject = new JSONObject();
            description = jsonObject.optString("DE");
            JSONArray occasionsArray = getData().optJSONArray("OL");
            if (occasionsArray == null)
                occasionsArray = new JSONArray();
            occasionCollections = new ArrayList<>(occasionsArray.length());
            for (int i = 0; i < occasionsArray.length(); i++)
                occasionCollections.add(new OccasionCollection(occasionsArray.optJSONObject(i)));
        }
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<OccasionCollection> getOccasionCollections() {
        return occasionCollections;
    }
}
