package com.example.sew.models;


import androidx.annotation.NonNull;

import com.example.sew.adapters.ReplyCommentAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Comment extends BaseModel {
    JSONObject jsonObject;

    private String totalLike, totalDisLike, userFavourite, commentDate, commentBy,
            commentByUserName, id, targetId, commentDescription, parentCommentId, source;
    private int replyCount, language;
    private ArrayList<ReplyComment> replyComment;
    private boolean isEditable;

    public Comment(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        totalLike = jsonObject.optString("TL");
        totalDisLike = jsonObject.optString("TD");
        userFavourite = jsonObject.optString("LM");
        commentDate = jsonObject.optString("CDT");
        replyCount = jsonObject.optInt("TR");
        commentBy = jsonObject.optString("UI");
        commentByUserName = jsonObject.optString("UN");
        isEditable= jsonObject.optBoolean("IE");
        id = jsonObject.optString("CI");
        targetId = jsonObject.optString("TI");
        commentDescription = jsonObject.optString("CD");
        parentCommentId = jsonObject.optString("PCI");
        language = jsonObject.optInt("L");
        source = jsonObject.optString("S");


        // commentTimeStamp= jsonObject.optLong("comment_timestamp");


        JSONArray replyJson = jsonObject.optJSONArray("C");
        if (replyJson == null)
            replyJson = new JSONArray();
        int size = replyJson.length();
        replyComment = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            replyComment.add(new ReplyComment(replyJson.optJSONObject(i)));
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public String getTotalDisLike() {
        return totalDisLike;
    }

    public String getUserFavourite() {
        return userFavourite;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public String getCommentByUserName() {
        return commentByUserName;
    }

    public String getId() {
        return id;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public String getSource() {
        return source;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public int getLanguage() {
        return language;
    }

    public boolean isEditable() {
        return isEditable;
    }

    @NonNull
    public ArrayList<ReplyComment> getReplyComment() {
        if (replyComment == null)
            replyComment = new ArrayList<>(replyCount);
        return replyComment;
    }

    /***  data loading keys  ***/
    boolean isDataLoading = false;
    boolean isRepliesShowing = false;
    int currentPageIndex = 1;
    ReplyCommentAdapter replyCommentAdapter;

    public boolean isDataLoading() {
        return isDataLoading;
    }

    public void setDataLoading(boolean dataLoading) {
        isDataLoading = dataLoading;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public ReplyCommentAdapter getReplyCommentAdapter() {
        return replyCommentAdapter;
    }

    public void setReplyCommentAdapter(ReplyCommentAdapter replyCommentAdapter) {
        this.replyCommentAdapter = replyCommentAdapter;
    }

    public boolean isAllRepliesLoaded() {
        return replyCount <= getReplyComment().size();
    }

    public boolean isRepliesShowing() {
        return isRepliesShowing;
    }

    public void setRepliesShowing(boolean repliesShowing) {
        isRepliesShowing = repliesShowing;
    }
    public void updateReplyCount(int replyCount) {
        this.replyCount = replyCount;
        updateKey(jsonObject, "TR", replyCount);
    }
}
