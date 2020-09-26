package com.example.sew.apis;

import com.example.sew.BuildConfig;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;

public class PostUserAppInfo extends Base {
    public PostUserAppInfo() {
        setUrl(MyConstants.getPostUserAppInfo());
        setRequestType(REQUEST_TYPE.POST);
        addCommonHeaders();
        addParam("deviceType", "android");
        addParam("appVersion", BuildConfig.VERSION_NAME);
    }

    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse())
            MyService.setLastAppVersion(BuildConfig.VERSION_NAME);
    }
}
