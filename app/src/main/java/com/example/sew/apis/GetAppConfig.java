package com.example.sew.apis;

import android.text.TextUtils;

import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SearchContentAll;

import org.json.JSONObject;

public class GetAppConfig extends Base {


    public GetAppConfig() {
        setUrl(MyConstants.GET_APP_CONFIG);
        setRequestType(REQUEST_TYPE.GET);
        addParam("serverType", String.valueOf(MyService.getAppServer()));
    }


    private String errorMessage;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONObject data = getData();
        if (isValidResponse()) {
            String baseUrl = data.optString("WU");
            String cdnUrl = data.optString("CU");
            String mediaUrl = data.optString("MU");
            if (!TextUtils.isEmpty(baseUrl.trim()) && baseUrl.startsWith("http"))
                MyService.setBaseURL(baseUrl);
            if (!TextUtils.isEmpty(cdnUrl.trim()) && cdnUrl.startsWith("http"))
                MyService.setCdnURL(cdnUrl);
            if (!TextUtils.isEmpty(mediaUrl.trim()) && mediaUrl.startsWith("http"))
                MyService.setMediaURL(mediaUrl);
            MyService.setLastConfigUpdateTime(System.currentTimeMillis());
        } else
            errorMessage = AppErrorMessage.an_error_ocurre;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
