package com.example.sew.apis;
import com.example.sew.common.MyConstants;


public class PostAddEditReplyComment extends Base {
    public PostAddEditReplyComment() {
        setUrl(MyConstants.getPostUserComments());
        setRequestType(REQUEST_TYPE.POST);
        addJson("S","android app");

    }

    public PostAddEditReplyComment setAddComment(String comment){
        addJson("CD",comment);
        return this;
    }
    public PostAddEditReplyComment setTargetId(String targetId){
        addJson("TI",targetId);
        return this;
    }
    public PostAddEditReplyComment setCommentId(String commentId){
        addJson("CI",commentId);
        return this;
    }
    public PostAddEditReplyComment setParentCommentId(String ParentCommentId){
        addJson("PCI",ParentCommentId);
        return this;
    }
    public PostAddEditReplyComment setLangauge(String langauge){
        addJson("L",langauge);
        return this;
    }
    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);

    }
}
