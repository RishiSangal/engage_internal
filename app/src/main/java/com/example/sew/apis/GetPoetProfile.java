package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.PoetCompleteProfile;

public class GetPoetProfile extends Base {


    public GetPoetProfile() {
        setUrl(MyConstants.getPoetCompleteProfile());
        setRequestType(REQUEST_TYPE.POST);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
    }

    public GetPoetProfile setPoetId(String poetId) {
        addParam("poetId", poetId);
        return this;
    }


    private PoetCompleteProfile poetCompleteProfile;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            poetCompleteProfile = new PoetCompleteProfile(getData());
        }
    }

    public PoetCompleteProfile getPoetCompleteProfile() {
        return poetCompleteProfile;
    }
}
