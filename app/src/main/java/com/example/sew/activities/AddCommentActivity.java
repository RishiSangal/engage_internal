package com.example.sew.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.R;
import com.example.sew.adapters.CommentListRecyclerAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetAllCommentsByTargetId;
import com.example.sew.apis.PostAddEditReplyComment;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.Comment;
import com.example.sew.models.ReplyComment;
import com.example.sew.models.User;
import com.example.sew.views.paging_recycler_view.PagingRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;


public class AddCommentActivity extends BaseActivity {

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtCommentHeader)
    TextView txtCommentHeader;
    @BindView(R.id.txtCommentHeaderCount)
    TextView txtCommentHeaderCount;
    @BindView(R.id.txtFilter)
    TextView txtFilter;
    @BindView(R.id.imgUserImage)
    CircleImageView imgUserImage;
    @BindView(R.id.edComment)
    EditText edComment;
    @BindView(R.id.txtCancel)
    TextView txtCancel;
    @BindView(R.id.txtComment)
    TextView txtComment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvComment)
    PagingRecyclerView rvComment;
    @BindView(R.id.layCancelComment)
    LinearLayout layCancelComment;
    @BindView(R.id.layTagName)
    LinearLayout layTagName;
    @BindView(R.id.txtTagName)
    TextView txtTagName;
    @BindView(R.id.viewShadow)
    View viewShadow;
    CommentListRecyclerAdapter commentListRecyclerAdapter;

    boolean isParentReply, isChildReply;
    ReplyComment currReplyComment;
    Comment currComment;
    String commentId;

    String commentHeader = "Remember to keep comments respectful and to follow our Community Guidelines";
    String defaultFilterKey = Enums.FORUM_SORT_FIELDS.POPULARITY.getKey();
    String defaultAscKey = Enums.COMMENT_SORT_LIST.DESCENDING.getKey();
    public final int REPLY_TYPE_DEFAULT = 0;
    public final int REPLY_TYPE_PARENT = 1;
    public final int REPLY_TYPE_CHILD = 2;
    public final int REPLY_TYPE_PARENT_EDIT = 3;
    public final int REPLY_TYPE_CHILD_EDIT = 4;
    public int currentViewType = REPLY_TYPE_DEFAULT;
    private boolean isOpenKeyBoard;
    private View.OnClickListener onParentReplyClickListener = v -> {
        Comment currComment = (Comment) v.getTag(R.id.tag_data);
        edComment.requestFocus();
        openKeyBoard();
        parentReplyUpdate(currComment);
    };

    void parentReplyUpdate(Comment currComment) {
        isParentReply = true;
        currentViewType = REPLY_TYPE_PARENT;
        layTagName.setVisibility(View.GONE);
        edComment.setHint("Reply...");
        commentId = currComment.getId();
    }

    @OnTextChanged(R.id.edComment)
    void onSearchTextChanged() {
        if (MyService.isUserLogin()) {
            String searchText = getEditTextData(edComment);
            if (TextUtils.isEmpty(searchText))
                layCancelComment.setVisibility(View.GONE);
            else
                layCancelComment.setVisibility(View.VISIBLE);
        } else
            startActivity(LoginActivity.getInstance(AddCommentActivity.this));
    }

    @OnClick(R.id.txtComment)
    public void onAddCommentClick() {
        addComment();
    }

    private void addComment() {
        String commentText = getEditTextData(edComment);
        if (MyService.isUserLogin()) {
            if (!TextUtils.isEmpty(commentText)) {
                showDialog();
                PostAddEditReplyComment postAddEditReplyComment = new PostAddEditReplyComment();
                postAddEditReplyComment.setCommentId("");
                if (currentViewType == REPLY_TYPE_PARENT)
                    postAddEditReplyComment.setAddComment(commentText).setParentCommentId(commentId);
                else if (currentViewType == REPLY_TYPE_CHILD) {
                    if (currReplyComment.getCommentByUserName().equalsIgnoreCase(MyService.getUser().getDisplayName()))
                        postAddEditReplyComment.setAddComment(String.format("@You %s", commentText));
                    else
                        postAddEditReplyComment.setAddComment(String.format("@%s %s", currReplyComment.getCommentByUserName(), commentText));
                    postAddEditReplyComment.setParentCommentId(currComment.getId());
                } else if (currentViewType == REPLY_TYPE_PARENT_EDIT) {
                    postAddEditReplyComment.setCommentId(currComment.getId()).setParentCommentId(currComment.getParentCommentId()).setAddComment(edComment.getText().toString());
                } else if (currentViewType == REPLY_TYPE_CHILD_EDIT) {
                    postAddEditReplyComment.setCommentId(currReplyComment.getId()).setParentCommentId(currReplyComment.getParentCommentId()).setAddComment(edComment.getText().toString());
                } else
                    postAddEditReplyComment.setAddComment(commentText).setParentCommentId("");
                postAddEditReplyComment.setTargetId(targetId).setLangauge(String.valueOf(MyService.getSelectedLanguageInt()))
                        .runAsync((BaseServiceable.OnApiFinishListener<PostAddEditReplyComment>) postAddEditReplyComments -> {
                            if (postAddEditReplyComments.isValidResponse()) {
                                isParentReply = false;
                                edComment.setText("");
                                isChildReply = false;
                                currentViewType = REPLY_TYPE_DEFAULT;
                                txtTagName.setVisibility(View.GONE);
                                hideKeyBoard();
                                //  if (isChildReply && currComment.isAllRepliesLoaded()) {
                                if (currentViewType == REPLY_TYPE_CHILD && currComment.isAllRepliesLoaded()) {
                                    getAllCommentsByTargetIdApi(defaultFilterKey, defaultAscKey);
                                    //} else if (!isChildReply)
                                } else if (currentViewType != REPLY_TYPE_CHILD)
                                    getAllCommentsByTargetIdApi(defaultFilterKey, defaultAscKey);
                            } else {
                                dismissDialog();
                                showToast(postAddEditReplyComments.getErrorMessage());
                            }
                        });

            } else {
                BaseActivity.showToast("Please enter comment");
            }
        } else {
            startActivity(LoginActivity.getInstance(getActivity()));
            BaseActivity.showToast("Please login");
        }

    }

    @OnClick(R.id.txtCancel)
    public void onCancelClick() {
        edComment.setText("");
        layTagName.setVisibility(View.GONE);
        isChildReply = false;
        isParentReply = false;
        currentViewType = REPLY_TYPE_DEFAULT;
        edComment.setHint("Add a comment...");
        hideKeyBoard();
    }

    @OnClick(R.id.txtFilter)
    public void onFilterClick() {
        PopupMenu popup = new PopupMenu(getActivity(), txtFilter);
        for (int i = 0; i < filterList.size(); i++) {
            popup.getMenu().add(R.id.menuGroup, R.id.group_detail, i, filterList.get(i));
        }
        popup.setOnMenuItemClickListener(item -> {
            if (item.toString().equalsIgnoreCase(getString(R.string.top_comment))) {
                defaultFilterKey = Enums.FORUM_SORT_FIELDS.POPULARITY.getKey();
                defaultAscKey = Enums.COMMENT_SORT_LIST.DESCENDING.getKey();
                getAllCommentsByTargetIdApi(defaultFilterKey, defaultAscKey);
            } else if ((item.toString().equalsIgnoreCase(getString(R.string.newest_comments)))) {
                defaultFilterKey = Enums.FORUM_SORT_FIELDS.RECENT_COMMENT.getKey();
                defaultAscKey = Enums.COMMENT_SORT_LIST.DESCENDING.getKey();
                getAllCommentsByTargetIdApi(defaultFilterKey, defaultAscKey);
            } else {
                defaultFilterKey = Enums.FORUM_SORT_FIELDS.COMMENT_BY_REKHTA.getKey();
                defaultAscKey = Enums.COMMENT_SORT_LIST.ASCENDING.getKey();
                getAllCommentsByTargetIdApi(defaultFilterKey, defaultAscKey);
            }
            return true;
        });
        popup.show();
    }


    private ArrayList<Comment> commentList = new ArrayList<>();
    String targetId;
    ArrayList<String> filterList;

    class MyClickableSpan extends ClickableSpan {

        String clicked;

        public MyClickableSpan(String string) {
            super();
            clicked = string;
        }

        @Override
        public void onClick(View tv) {
            Toast.makeText(AddCommentActivity.this, clicked, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.dark_blue));
            ds.setUnderlineText(false);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comment_activity);
        ButterKnife.bind(this);
        // setHeaderTitle(MyHelper.getString(R.string.comments));
        targetId = getIntent().getStringExtra(CONTENT_ID);
        isOpenKeyBoard = getIntent().getBooleanExtra(IS_OPEN_KEYBOARD, false);
        if (MyService.isUserLogin()) {
            User user = MyService.getUser();
            if (!TextUtils.isEmpty(user.getImageName()))
                ImageHelper.setImage(imgUserImage, user.getImageName(), Enums.PLACEHOLDER_TYPE.PROFILE);
        }
        filterList = new ArrayList<>();
        filterList.add(getString(R.string.top_comment));
        filterList.add(getString(R.string.newest_comments));
        filterList.add(getString(R.string.replied_by_rekhta));
        SpannableString ss = new SpannableString(commentHeader);
        final String first = "Community Guidelines";
        int firstIndex = commentHeader.indexOf(first);
//        ClickableSpan firstWordClick = new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
//            }
//        };
        ss.setSpan(new MyClickableSpan(commentHeader), firstIndex, firstIndex + first.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtTitle.setLinksClickable(true);
        // txtTitle.setHighlightColor(getResources().getColor(R.color.dark_blue));
        // txtTitle.setMovementMethod(LinkMovementMethod.getInstance());
        txtTitle.setText(ss, TextView.BufferType.SPANNABLE);

        if (isOpenKeyBoard) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } else
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getAllCommentsByTargetIdApi(defaultFilterKey, defaultAscKey);
        rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                viewShadow.setVisibility(View.GONE);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                viewShadow.setVisibility(View.VISIBLE);
            }

        });
    }

    GetAllCommentsByTargetId getAllCommentsByTargetId;

    private void getAllCommentsByTargetIdApi(String forumSortField, String sortAsc) {
        showDialog();
        getAllCommentsByTargetId = new GetAllCommentsByTargetId();
        getAllCommentsByTargetId.setTargetId(targetId).setSortBy(forumSortField)
                .setIsAsc(sortAsc).addPagination().runAsync((BaseServiceable.OnApiFinishListener<GetAllCommentsByTargetId>) getAllCommentsByTargetIds -> {
            if (getAllCommentsByTargetIds.isValidResponse()) {
                dismissDialog();
                targetId = getAllCommentsByTargetIds.getTargetId();
                BaseActivity.sendBroadCast(ICommonValues.BROADCAST_RENDER_CONTENT_COMMENT_UPDATE);
                if (getAllCommentsByTargetIds.isFirstPage())
                    commentList.clear();
                commentList.addAll(getAllCommentsByTargetIds.getComment());
                updateUI();
                rvComment.onNoMoreData();
                rvComment.onHide();
                txtCommentHeaderCount.setText(getAllCommentsByTargetIds.getTotalCommentsCount());

            } else {
                finish();
                showToast(getAllCommentsByTargetIds.getErrorMessage());
            }
        });
    }

    private void updateUI() {
        rvComment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (commentListRecyclerAdapter == null) {
            commentListRecyclerAdapter = new CommentListRecyclerAdapter(getActivity(), commentList);
            commentListRecyclerAdapter.setOnParentReplyClickListener(onParentReplyClickListener);
            rvComment.setAdapter(commentListRecyclerAdapter);
            rvComment.setOnPagingListener((view1, direction) -> {
                if (getAllCommentsByTargetId != null) {
                    getAllCommentsByTargetId.loadMoreData();
                    rvComment.onPaging();
                }
            });
        } else
            commentListRecyclerAdapter.notifyDataSetChanged();

    }

    public static Intent getInstance(Activity activity, String targetId, boolean isOpenKeyBoard) {
        Intent intent = new Intent(activity, AddCommentActivity.class);
        intent.putExtra(CONTENT_ID, targetId);
        intent.putExtra(IS_OPEN_KEYBOARD, isOpenKeyBoard);
        return intent;
    }

    public void refreshTotalCommentCount(String totalCommentCount) {
        txtCommentHeaderCount.setText(totalCommentCount);
    }

    public void refreshInputComment() {
        isParentReply = false;
        edComment.setText("");
        isChildReply = false;
    }

    public void refreshCommentAdapter() {
        commentListRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
    }


    public void childReply(boolean isChildReply, ReplyComment replyComment, Comment currComment) {
        this.isChildReply = isChildReply;
        currentViewType = REPLY_TYPE_CHILD;
        this.currReplyComment = replyComment;
        this.currComment = currComment;
        layTagName.setVisibility(View.VISIBLE);
        txtTagName.setVisibility(View.VISIBLE);
        if (currReplyComment.getCommentByUserName().equalsIgnoreCase(MyService.getUser().getDisplayName()))
            txtTagName.setText("@You");
        else
            txtTagName.setText(String.format("@%s", currReplyComment.getCommentByUserName()));
        layCancelComment.setVisibility(View.VISIBLE);
        edComment.setHint("Reply...");
        edComment.requestFocus();
        openKeyBoard();
    }

    public void parentEditComment(Comment currComment) {
        this.currComment = currComment;
        layCancelComment.setVisibility(View.VISIBLE);
        edComment.requestFocus();
        currentViewType = REPLY_TYPE_PARENT_EDIT;
        edComment.setText(currComment.getCommentDescription());
        edComment.setSelection(currComment.getCommentDescription().length());
        openKeyBoard();
    }

    public void childEditComment(ReplyComment replyComment) {
        this.currReplyComment = replyComment;
        layCancelComment.setVisibility(View.VISIBLE);
        edComment.requestFocus();
        currentViewType = REPLY_TYPE_CHILD_EDIT;
        edComment.setText(replyComment.getCommentDescription());
        edComment.setSelection(replyComment.getCommentDescription().length());
        openKeyBoard();
    }

}
