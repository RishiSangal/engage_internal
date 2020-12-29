package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.Comment;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetAllCommentsByTargetId extends Base {
    public GetAllCommentsByTargetId() {
        setUrl(MyConstants.getGetAllCommentsByTargetId());
        setRequestType(REQUEST_TYPE.GET);
    }

    public GetAllCommentsByTargetId setTargetId(String targetId) {
        addParam("TI", targetId);
        return this;
    }

    public GetAllCommentsByTargetId setSortBy(String sortFields) {
        addParam("sortBy", sortFields);
        return this;
    }

    public GetAllCommentsByTargetId setIsAsc(String sortList) {
        addParam("isAsc", sortList);
        return this;
    }

    private String targetId, contentType, userStatus, totalCommentsCount,communityGuildlines;
    private ArrayList<Comment> comment;

    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            targetId = getData().optString("TI");
            contentType = getData().optString("CT");
            userStatus = getData().optString("US");
            totalCommentsCount = getData().optString("TCC");
            communityGuildlines= getData().optString("CG");
            JSONArray commentArray = getData().optJSONArray("C");
            if (commentArray == null)
                commentArray = new JSONArray();
            comment = new ArrayList<>(commentArray.length());for (int i = 0; i < commentArray.length(); i++)
                comment.add(new Comment(commentArray.optJSONObject(i)));
           // BaseActivity.sendBroadCast(ICommonValues.BROADCAST_RENDER_CONTENT_COMMENT_UPDATE);
        }

    }

    public String getContentType() {
        return contentType;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public String getTotalCommentsCount() {
        return totalCommentsCount;
    }

    public String getCommunityGuildlines() {
        return communityGuildlines;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }
}
