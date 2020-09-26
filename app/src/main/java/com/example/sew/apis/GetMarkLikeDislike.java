package com.example.sew.apis;

import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;

public class GetMarkLikeDislike extends Base {
    public GetMarkLikeDislike() {
        setUrl(MyConstants.getGetMarkLikeDislike());
        setRequestType(REQUEST_TYPE.GET);
    }

    public GetMarkLikeDislike setCommentId(String commentId) {
        addParam("CI", commentId);
        return this;
    }

    public GetMarkLikeDislike setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE markStatus) {
        addParam("markStatus", markStatus.getKey());
        return this;
    }

    public GetMarkLikeDislike setLangauge(String langauge) {
        addParam("lang", langauge);
        return this;
    }

    private String commentId, totalLike, totalDisLike, userFavourite;

    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            commentId = getData().optString("CI");
            totalLike = getData().optString("TL");
            totalDisLike = getData().optString("TD");
            userFavourite = getData().optString("LM");
        }

    }

    public String getTotalDisLike() {
        return totalDisLike;
    }

    public String getUserFavourite() {
        return userFavourite;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getTotalLike() {
        return totalLike;
    }
}
