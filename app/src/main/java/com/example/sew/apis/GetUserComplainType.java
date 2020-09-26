package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.Comment;
import com.example.sew.models.ComplainType;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetUserComplainType extends Base{
    public GetUserComplainType() {
        setUrl(MyConstants.getGetComplainTypes());
        setRequestType(REQUEST_TYPE.GET);
    }
    private ArrayList<ComplainType> complainTypes;
    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if(isValidResponse()){
            JSONArray complainTypeArray = getData().optJSONArray("CT");
            if (complainTypeArray == null)
                complainTypeArray = new JSONArray();
            complainTypes = new ArrayList<>(complainTypeArray.length());
            for (int i = 0; i < complainTypeArray.length(); i++)
                complainTypes.add(new ComplainType(complainTypeArray.optJSONObject(i)));
        }
    }

    public ArrayList<ComplainType> getComplainTypes() {
        return complainTypes;
    }
}
