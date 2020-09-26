package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.User;

public class PostSocialLogin extends Base {


    private User user;

    public PostSocialLogin() {
        setUrl(MyConstants.getSocialLogin());
        setRequestType(REQUEST_TYPE.POST);
        addParam("reToken", MyService.getUniqueId());
        addHeader("TempToken", MyService.getUniqueId());
        addJson("DeviceParams", MyService.getDeviceParams());
    }

    private String email;

    public PostSocialLogin setEmail(String email) {
        this.email = email;
        addJson("Email", email);
        return this;
    }

    public PostSocialLogin setExternalAccessToken(String externalAccessToken) {
        addJson("ExternalAccessToken", externalAccessToken);
        return this;
    }

    public PostSocialLogin setUserName(String userName) {
        addJson("UserName", userName);
        return this;
    }

    public PostSocialLogin setProvider(String provider) {
        addJson("Provider", provider);
        return this;
    }

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            user = new User(getData());
            MyService.setUser(user);
            MyService.setEmail(email);
        }
    }

    public User getUser() {
        return user;
    }
}
