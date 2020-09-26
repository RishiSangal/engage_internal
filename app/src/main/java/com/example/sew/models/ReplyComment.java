package com.example.sew.models;

import org.json.JSONObject;

public class ReplyComment extends BaseModel{
    JSONObject jsonObject;
    private String totalLike,totalDisLike,userFavourite,commentDate,commentBy,
            commentByUserName,id, targetId,commentDescription,parentCommentId,source;
    private int language;
    private boolean isEditable;
    public ReplyComment(JSONObject jsonObject) {
        if(jsonObject==null)
            jsonObject= new JSONObject();
        this.jsonObject= jsonObject;
        totalLike= jsonObject.optString("TL");
        totalDisLike= jsonObject.optString("TD");
        userFavourite = jsonObject.optString("LM");
        commentDate = jsonObject.optString("CDT");
        commentBy = jsonObject.optString("UI");
        commentByUserName = jsonObject.optString("UN");
        isEditable= jsonObject.optBoolean("IE");
        id= jsonObject.optString("CI");
        targetId = jsonObject.optString("TI");
        commentDescription = jsonObject.optString("CD");
        parentCommentId= jsonObject.optString("PCI");
        language = jsonObject.optInt("L");
        source = jsonObject.optString("S");
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

    public int getLanguage() {
        return language;
    }

    public boolean isEditable() {
        return isEditable;
    }
}
