package com.example.sew.apis;

import com.example.sew.common.MyConstants;

public class GetAllNotification extends Base {
    public GetAllNotification() {
        setUrl("");
        setRequestType(REQUEST_TYPE.GET);
    }

    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if(isValidResponse()){

        }
    }
}
