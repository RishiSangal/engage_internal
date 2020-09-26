package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.User;

public class PostSignup extends Base {


    private User user;

    public PostSignup() {
        setUrl(MyConstants.getSIGNUP());
        setRequestType(REQUEST_TYPE.POST);
        addHeader("TempToken", MyService.getUniqueId());
        addJson("IsRequestedNewsLetter", "true");
        addJson("DeviceParams", MyService.getDeviceParams());
        addJson("IsSuccess", "true");
    }

    private String email;

    public PostSignup setEmail(String email) {
        this.email = email;
        addJson("Email", email);
        return this;
    }

    public PostSignup setPassword(String password) {
        addJson("Password", password);
        addJson("ConfirmPassword", password);
        return this;
    }

    public PostSignup setDisplayName(String displayName) {
        addJson("DisplayName", displayName);
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
