package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.User;

public class PostLogin extends Base {

    public PostLogin() {
        setUrl(MyConstants.getLoginUrl());
        setRequestType(REQUEST_TYPE.POST);
        addParam("reToken", MyService.getUniqueId());
        addJson("DeviceParams", MyService.getDeviceParams());
        addJson("Language", 1);
        addJson("RememberMe", "true");
    }

    private String email;

    public PostLogin setEmail(String email) {
        this.email = email;
        addJson("Email", email);
        return this;
    }

    public PostLogin setPassword(String password) {
        addJson("Password", password);
        return this;
    }

    private User user;

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
