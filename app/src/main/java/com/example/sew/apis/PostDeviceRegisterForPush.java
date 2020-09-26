package com.example.sew.apis;

import android.provider.Settings;

import com.example.sew.MyApplication;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;

public class PostDeviceRegisterForPush extends Base {
    public PostDeviceRegisterForPush() {
        setUrl(MyConstants.getPostPushRegistration());
        setRequestType(REQUEST_TYPE.POST);
        addCommonHeaders();
        addParam("fcmToken",MyService.getFcmToken());
        addParam("deviceId", Settings.Secure.getString(MyApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        addParam("deviceType","android");
    }

    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
    }
}
