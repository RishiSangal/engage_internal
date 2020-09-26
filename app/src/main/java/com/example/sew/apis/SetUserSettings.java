package com.example.sew.apis;

import com.example.sew.common.MyConstants;

public class SetUserSettings extends Base {

    public static final int SETTING_PREFERRED_LANG_EN = 1,
            SETTING_PREFERRED_LANG_HI = 2,
            SETTING_PREFERRED_LANG_UR = 3,
            SETTING_EVENT_NOTIFICATION = 4,
            SETTING_FAV_OFFLINE = 5,
            SETTING_SHER_OF_THE_DAY = 6,
            SETTING_WORD_OF_THE_DAY = 7,
            SETTING_GENERIC_NOTIFICATION = 8,
            SETTING_NEWSLETTER = 0;

    public SetUserSettings() {
        setUrl(MyConstants.getSetUserSetting());
        setRequestType(REQUEST_TYPE.POST);
        addCommonHeaders();
    }

    public SetUserSettings setSetting(int setting, boolean value) {
        addParam("setting", String.valueOf(setting));
        addParam("value", String.valueOf(value));
        return this;
    }

//    private UserSettings userSettings;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);

    }

//    public UserSettings getUserSettings() {
//        return userSettings;
//    }
}
