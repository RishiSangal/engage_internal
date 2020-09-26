package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ShayariImageDetail;

import org.json.JSONArray;

public class GetShayariImageDetail extends Base {


    public GetShayariImageDetail() {
        setUrl(MyConstants.getGetShayariImageDetail());
        setRequestType(REQUEST_TYPE.POST);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
    }

    public GetShayariImageDetail setShayariImgId(String shayariImgId) {
        addParam("shayariImgId", shayariImgId);
        return this;
    }
    public GetShayariImageDetail setTargetIdSlug(String targetIdSlug) {
        addParam("targetIdSlug", targetIdSlug);
        return this;
    }
    private ShayariImageDetail shayariImageDetail;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            JSONArray imageArray = getData().optJSONArray("SI");
            if (imageArray == null)
                imageArray = new JSONArray();
            if (imageArray.length() > 0)
                shayariImageDetail = new ShayariImageDetail(imageArray.optJSONObject(0));
        }
    }

    public ShayariImageDetail getShayariImageDetail() {
        return shayariImageDetail;
    }
}
