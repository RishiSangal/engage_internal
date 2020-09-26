package com.example.sew.apis;

import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;

public class PostSubmitCritique extends Base {

    public PostSubmitCritique() {
        setUrl(MyConstants.getPostSubmitCritique());
        setRequestType(REQUEST_TYPE.POST);
    }

    public PostSubmitCritique setName(String name) {
        addJson("name", name);
        return this;
    }

    public PostSubmitCritique setEmail(String email) {
        addJson("email", email);
        return this;
    }

    public PostSubmitCritique setContentId(String contentId) {
        addJson("contentId", contentId);
        return this;
    }

    public PostSubmitCritique setContentTitle(String contentTitle) {
        addJson("contentTitle", contentTitle);
        return this;
    }

    private PostSubmitCritique setPageType(String pageType) {
        addJson("pageType", pageType);
        return this;
    }

    public PostSubmitCritique setSubject(String subject) {
        addJson("subject", subject);
        return this;
    }

    public PostSubmitCritique setMessage(String message) {
        addJson("message", message);
        return this;
    }

    public PostSubmitCritique setTypeOfQuery(Enums.CRITIQUE_TYPE critiqueType) {
        if (critiqueType == null)
            throw new IllegalArgumentException("Critique type cannot be null");
        switch (critiqueType) {
            case FEEDBACK:
                addJson("typeOfQuery", "1");
                setContentId("");
                setContentTitle("Feedback Title");
                setPageType("App-Feedback-Android");
                setPageUrl("");
                break;
            case CRITIQUE:
                addJson("typeOfQuery", "2");
                setPageType("App Poem");
                break;
        }
        return this;
    }

    public PostSubmitCritique setPageUrl(String pageUrl) {
        addJson("pageUrl", pageUrl);
        return this;
    }


    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
    }
}
