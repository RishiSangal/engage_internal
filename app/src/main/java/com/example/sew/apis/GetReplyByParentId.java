package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.Comment;
import com.example.sew.models.ReplyComment;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetReplyByParentId extends Base {
    public GetReplyByParentId() {
        setUrl(MyConstants.getGetReplyByParentId());
        setRequestType(REQUEST_TYPE.GET);
    }
    public GetReplyByParentId setParentCommentId(String parentCommentId){
        addParam("PCI",parentCommentId);
        return this;
    }
    public GetReplyByParentId setSortBy(String sortBy){
        addParam("sortBy",sortBy);
        return this;
    }
    public GetReplyByParentId setIsAsc(String isAsc){
        addParam("isAsc",isAsc);
        return this;
    }
private ArrayList<ReplyComment> replyComments;
    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if(isValidResponse()){
            JSONArray replyArray = getData().optJSONArray("R");
            if (replyArray == null)
                replyArray = new JSONArray();
            replyComments = new ArrayList<>(replyArray.length());
            for (int i = 0; i < replyArray.length(); i++)
                replyComments.add(new ReplyComment(replyArray.optJSONObject(i)));
        }

    }

    public ArrayList<ReplyComment> getReplyComments() {
        return replyComments;
    }
}
