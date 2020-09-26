package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.CountSummary;

public class GetCountingSummaryByTargetid extends Base {
    public GetCountingSummaryByTargetid() {
        setUrl(MyConstants.getGetCountingSummarybyTargetid());
        setRequestType(REQUEST_TYPE.GET);
        addCommonHeaders();
    }

    public GetCountingSummaryByTargetid setTargetId(String targetId) {
        addParam("targetId", targetId);
        return this;
    }


    private CountSummary countSummary;

    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            countSummary = new CountSummary(getData());
        }
    }

    public CountSummary getCountSummary() {
        return countSummary;
    }
}
