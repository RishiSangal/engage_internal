package com.example.sew.apis;

import com.example.sew.common.MyConstants;

public class PostRemoveComment extends Base {
    public PostRemoveComment() {
        setUrl(MyConstants.getGetRemoveComment());
        setRequestType(REQUEST_TYPE.GET);
    }
    public PostRemoveComment setCommentId(String commentId){
        addParam("CI",commentId);
        return this;
    }
    private  String totalCommentCount,userStatus;
    private int replyCount;
    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if(isValidResponse()){
            userStatus= getData().optString("US");
            totalCommentCount= getData().optString("TCC");
            replyCount= getData().optInt("TR");
        }
    }

    public String getTotalCommentCount() {
        return totalCommentCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public String getUserStatus() {
        return userStatus;
    }
}
