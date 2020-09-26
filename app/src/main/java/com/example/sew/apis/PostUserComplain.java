package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;

public class PostUserComplain extends Base {
    public PostUserComplain() {
        setUrl(MyConstants.getPostUserComplain());
        setRequestType(REQUEST_TYPE.POST);
        addJson("WS","1");
        addJson("S","android app");
        addJson("L", String.valueOf(MyService.getSelectedLanguageInt()));
    }
    public PostUserComplain setTargetId(String targetId){
        addJson("TI",targetId);
        return this;
    }
    public PostUserComplain setComplainId(String complainId){
        addJson("CI",complainId);
        return this;
    }
    public PostUserComplain setReportTypeId(String typeId){
        addJson("RTI",typeId);
        return this;
    }
    //{"TI":"4978813f-b448-491c-9163-5df64191ff1b","CI":"4ea1d27d-f964-4019-9d5c-5118be056a9d","RTI":"ff27b0c1-c6ff-4911-b784-ca2954dd6cdc","WS":1,"L":1,"S":"ios app"}


    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if(isValidResponse()){

        }
    }
}
