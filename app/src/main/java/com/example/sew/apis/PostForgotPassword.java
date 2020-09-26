package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

public class PostForgotPassword extends Base {


    public PostForgotPassword() {
        setUrl(MyConstants.getForgotPassword());
        setRequestType(REQUEST_TYPE.POST);
        addParam("reToken", MyService.getUniqueId());
        addHeader("TempToken", MyService.getUniqueId());
    }

    public PostForgotPassword setEmail(String email) {
        addJson("Email", email);
        return this;
    }

    private String errorMessage = "";

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (!isValidResponse()) {
            errorMessage = MyHelper.parseResponse(response).optString("Me");
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
