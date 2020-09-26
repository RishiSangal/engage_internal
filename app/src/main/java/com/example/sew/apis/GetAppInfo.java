package com.example.sew.apis;

import android.provider.Settings;

import com.example.sew.MyApplication;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.AppVersion;

public class GetAppInfo extends Base {
    public GetAppInfo() {
        setUrl(MyConstants.getGetAppInfo());
        setRequestType(REQUEST_TYPE.POST);
        addCommonHeaders();
        addParam("deviceType", "android");
    }

    private AppVersion appVersion;

    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse())
            appVersion = new AppVersion(getData());
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }
}
