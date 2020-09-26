package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.UserSettings;

import org.json.JSONObject;

public class GetSettings extends Base {


    public GetSettings() {
        setUrl(MyConstants.getGetSettings());
        setRequestType(REQUEST_TYPE.POST);
        addCommonHeaders();
    }

    private UserSettings userSettings;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            userSettings = new UserSettings(getData());
            MyService.setIsOfflineSaveEnable(userSettings.isSaveFavoriteOffline());
        }
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }
}
