package com.example.sew.apis;

import com.example.sew.common.MyConstants;


public class GetYouTubeKey  extends Base{
    public GetYouTubeKey() {
        setUrl(MyConstants.getGetYoutubeKey());
        addCommonHeaders();
        setRequestType(REQUEST_TYPE.GET);
    }
    private String youTubeKey;
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            youTubeKey= getData().optString("key");
        }
    }

    public String getYouTubeKey() {
        return youTubeKey;
    }
}
