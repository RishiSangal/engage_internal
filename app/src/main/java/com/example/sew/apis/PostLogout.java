package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;

public class PostLogout extends Base {
    public PostLogout() {
        setUrl(MyConstants.getLogoutV5Url());
        setRequestType(BaseServiceable.REQUEST_TYPE.POST);
        addHeader("TempToken", MyService.getUniqueId());
        addHeader("Authorization", MyService.getAuthToken());
        addParam("reToken", MyService.getUniqueId());
        addParam("deviceId", MyService.getDeviceId());
        addParam("fcmToken", MyService.getFcmToken());
    }

    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);

    }
}
