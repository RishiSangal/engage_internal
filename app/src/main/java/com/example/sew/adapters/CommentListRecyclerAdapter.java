package com.example.sew.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.R;
import com.example.sew.activities.AddCommentActivity;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.LoginActivity;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetMarkLikeDislike;
import com.example.sew.apis.GetReplyByParentId;
import com.example.sew.apis.GetUserComplainType;
import com.example.sew.apis.PostRemoveComment;
import com.example.sew.apis.PostUserComplain;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.SLog;
import com.example.sew.models.Comment;
import com.example.sew.models.ComplainType;
import com.example.sew.models.User;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListRecyclerAdapter extends BaseRecyclerAdapter {

    private ArrayList<Comment> commentList;
    private ArrayList<Comment> selectedLiked = new ArrayList<Comment>();
    private ArrayList<Comment> selectedDisLiked = new ArrayList<Comment>();
    private ArrayList<Comment> selectedShowReplies = new ArrayList<Comment>();
    private String sortOrder, sortFilter;

    public CommentListRecyclerAdapter(BaseActivity activity, ArrayList<Comment> commentList, String sortOrder, String sortFilter) {
        super(activity);
        this.commentList = commentList;
        this.sortOrder = sortOrder;
        this.sortFilter = sortFilter;
    }

    public Comment getItem(int position) {
        return commentList.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(getInflatedView(R.layout.cell_comment_section, parent));
    }

    boolean isLiked, isDisliked;

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof CommentViewHolder))
            return;
        Comment currComment = getItem(position);
        CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
        commentViewHolder.imgMore.setTag(R.id.tag_data, currComment);
        commentViewHolder.layLike.setTag(R.id.tag_data, currComment);
        commentViewHolder.layDislike.setTag(R.id.tag_data, currComment);
        commentViewHolder.layComment.setTag(R.id.tag_data, currComment);
        commentViewHolder.txtShowReplies.setTag(R.id.tag_data, currComment);
        commentViewHolder.txtLoadMoreReplies.setTag(R.id.tag_data, currComment);
        commentViewHolder.txtName.setText(currComment.getCommentByUserName());
        commentViewHolder.txtComment.setText(currComment.getCommentDescription());
        commentViewHolder.txtLikeCount.setText(currComment.getTotalLike());
        commentViewHolder.txtDislikeCount.setText(currComment.getTotalDisLike());
        commentViewHolder.txtCommentCount.setText(String.valueOf(currComment.getReplyCount()));
        if (MyService.isUserLogin()) {
            if (currComment.isEditable()) {
                User user = MyService.getUser();
                if (!TextUtils.isEmpty(user.getImageName())) {
                    ImageHelper.setImage(commentViewHolder.imgImage, user.getImageName(), Enums.PLACEHOLDER_TYPE.PROFILE);
                    commentViewHolder.txtFirstCharacterName.setVisibility(View.GONE);
                    commentViewHolder.imgImage.setVisibility(View.VISIBLE);
                } else {
                    commentViewHolder.txtFirstCharacterName.setVisibility(View.VISIBLE);
                    commentViewHolder.imgImage.setVisibility(View.GONE);
                    commentViewHolder.txtFirstCharacterName.setText(String.valueOf(currComment.getCommentByUserName().charAt(0)).toUpperCase());
                }
            } else {
                if (!TextUtils.isEmpty(currComment.getOtherUserImage())) {
                    ImageHelper.setImage(commentViewHolder.imgImage, currComment.getOtherUserImage(), Enums.PLACEHOLDER_TYPE.PROFILE);
                    commentViewHolder.txtFirstCharacterName.setVisibility(View.GONE);
                    commentViewHolder.imgImage.setVisibility(View.VISIBLE);
                } else {
                    commentViewHolder.txtFirstCharacterName.setVisibility(View.VISIBLE);
                    commentViewHolder.imgImage.setVisibility(View.GONE);
                    commentViewHolder.txtFirstCharacterName.setText(String.valueOf(currComment.getCommentByUserName().charAt(0)).toUpperCase());
                }
            }

        } else {
            commentViewHolder.txtFirstCharacterName.setVisibility(View.VISIBLE);
            commentViewHolder.imgImage.setVisibility(View.GONE);
            commentViewHolder.txtFirstCharacterName.setText(String.valueOf(currComment.getCommentByUserName().charAt(0)).toUpperCase());
        }

        //commentViewHolder.txtTime.setText(MyHelper.getTimeAgo(currComment.getCommentTimeStamp()));
        commentViewHolder.txtTime.setText(currComment.getCommentDate());
//        ImageHelper.setImage(commentViewHolder.imgImage, currComment.getUserImage());

        if (currComment.getUserFavourite().equalsIgnoreCase("yes")) {
            selectedLiked.add(currComment);
            commentViewHolder.imgLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
        } else {
            selectedLiked.clear();
            commentViewHolder.imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
        }
        if (currComment.getUserFavourite().equalsIgnoreCase("no")) {
            selectedDisLiked.add(currComment);
            commentViewHolder.imgDislike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
        } else {
            selectedDisLiked.clear();
            commentViewHolder.imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
        }

        if (currComment.isDataLoading()) {
            commentViewHolder.prgReplyLoading.setVisibility(View.VISIBLE);
            commentViewHolder.txtShowReplies.setVisibility(View.GONE);
        } else {
            commentViewHolder.prgReplyLoading.setVisibility(View.GONE);
            commentViewHolder.txtShowReplies.setVisibility(currComment.getReplyCount() == 0 ? View.GONE : View.VISIBLE);
        }
        if (currComment.isRepliesShowing()) {
            commentViewHolder.txtShowReplies.setText(MyHelper.getString(R.string.hide_replies));
            showReplies(commentViewHolder, currComment);
        } else {
            commentViewHolder.txtShowReplies.setText(MyHelper.getString(R.string.show_replies));
            hideReplies(commentViewHolder, currComment);
        }
        if (currComment.getReplyCount() == 0)
            commentViewHolder.txtCommentCount.setVisibility(View.GONE);
        else {
            commentViewHolder.txtCommentCount.setVisibility(View.VISIBLE);
        }
        commentViewHolder.prgMoreReplyLoading.setVisibility(View.GONE);
        if (currComment.isAllRepliesLoaded())
            commentViewHolder.txtLoadMoreReplies.setVisibility(View.GONE);
    }

    private View.OnClickListener onParentReplyClickListener;

    public void setOnParentReplyClickListener(View.OnClickListener onItemClickListener) {
        this.onParentReplyClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    class CommentViewHolder extends BaseViewHolder {
        @BindView(R.id.txtLoadMoreReplies)
        TextView txtLoadMoreReplies;
        @BindView(R.id.prgMoreReplyLoading)
        ProgressBar prgMoreReplyLoading;
        @BindView(R.id.prgReplyLoading)
        ProgressBar prgReplyLoading;
        @BindView(R.id.imgImage)
        CircleImageView imgImage;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtTime)
        TextView txtTime;
        @BindView(R.id.txtComment)
        TextView txtComment;
        @BindView(R.id.txtLikeCount)
        TextView txtLikeCount;
        @BindView(R.id.txtDislikeCount)
        TextView txtDislikeCount;
        @BindView(R.id.txtCommentCount)
        TextView txtCommentCount;
        @BindView(R.id.imgMore)
        ImageView imgMore;
        @BindView(R.id.txtShowReplies)
        TextView txtShowReplies;
        @BindView(R.id.rvReplyComment)
        RecyclerView rvReplyComment;
        @BindView(R.id.layLike)
        LinearLayout layLike;
        @BindView(R.id.layDislike)
        LinearLayout layDislike;
        @BindView(R.id.layComment)
        LinearLayout layComment;
        @BindView(R.id.imgLike)
        ImageView imgLike;
        @BindView(R.id.imgDislike)
        ImageView imgDislike;
        @BindView(R.id.txtFirstCharacterName)
        TextView txtFirstCharacterName;
        @BindView(R.id.layReplySection)
        LinearLayout layReplySection;

        CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        @OnClick({R.id.layLike, R.id.layDislike, R.id.layComment, R.id.imgMore, R.id.txtShowReplies, R.id.txtLoadMoreReplies})
        public void onViewClicked(View view) {
            Comment currComment = (Comment) view.getTag(R.id.tag_data);
            switch (view.getId()) {
                case R.id.layLike:
                    if (MyService.isUserLogin()) {
                        if (isLikeChecked(currComment)) {
                            imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.REMOVE_STATUS)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
//                                    imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedLiked.remove(currComment);
                                    selectedLiked.clear();
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        } else {
                            imgLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
                            imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.LIKE)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
//                                    imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedLiked.add(currComment);
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        }
                    } else {
                        getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                        BaseActivity.showToast("Please login");
                    }
                    break;
                case R.id.layDislike:
                    if (MyService.isUserLogin()) {
                        if (isDisLikeChecked(currComment)) {
                            imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.REMOVE_STATUS)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
//                                    imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedDisLiked.remove(currComment);
                                    selectedDisLiked.clear();
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        } else {
                            imgDislike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
                            imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.DISLIKE)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgDislike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
//                                    imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedDisLiked.add(currComment);
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        }
                    } else {
                        getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                        BaseActivity.showToast("Please login");
                    }
                    break;
                case R.id.layComment:
                    if (MyService.isUserLogin()) {
                        if (getActivity() instanceof AddCommentActivity)
                            if (onParentReplyClickListener != null)
                                onParentReplyClickListener.onClick(view);

                    } else {
                        getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                        BaseActivity.showToast("Please login");
                    }
                    break;
                case R.id.imgMore:
                    PopupMenu popup = new PopupMenu(getActivity(), imgMore, Gravity.TOP);
                    ArrayList<String> popupList = new ArrayList<>();
                    if (currComment.isEditable()) {
                        popupList.add(getString(R.string.edit));
                        popupList.add(getString(R.string.delete));
                    } else {
                        popupList.add(getString(R.string.report_comment));
                    }
                    for (int i = 0; i < popupList.size(); i++) {
                        popup.getMenu().add(R.id.menuGroup, R.id.group_detail, 0, popupList.get(i));
                    }
                    popup.setOnMenuItemClickListener(item -> {
                        if (item.toString().equalsIgnoreCase(getString(R.string.report_comment))) {
                            if (!MyService.isUserLogin()) {
                                getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                                BaseActivity.showToast("Please login");
                            } else
                                getComplainTypeApiCall(currComment);
                        } else if (item.toString().equalsIgnoreCase(getString(R.string.edit))) {
                            if (!MyService.isUserLogin()) {
                                getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                                BaseActivity.showToast("Please login");
                            } else {
                                ((AddCommentActivity) getActivity()).parentEditComment(currComment);

                            }
                        } else if (item.toString().equalsIgnoreCase(getString(R.string.delete))) {
                            if (!MyService.isUserLogin()) {
                                getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                                BaseActivity.showToast("Please login");
                            } else
                                warningPopup(currComment);
                        }
                        return true;
                    });

                    popup.show();
                    break;
                case R.id.txtShowReplies:
                    if (currComment.isRepliesShowing()) {
                        currComment.setRepliesShowing(false);
                        notifyDataSetChanged();
                    } else {
                        if (CollectionUtils.isEmpty(currComment.getReplyComment())) {
                            currComment.setDataLoading(true);
                            getReplyByParentIdApiCall(this, currComment);
                            prgReplyLoading.setVisibility(View.VISIBLE);
                            txtShowReplies.setVisibility(View.GONE);
                        } else {
                            currComment.setRepliesShowing(true);
                            notifyDataSetChanged();
                        }
                    }

//                    if (isShowRepliesChecked(currComment)) {
//                        layReplySection.setVisibility(View.GONE);
//                        txtShowReplies.setVisibility(View.VISIBLE);
//                        selectedShowReplies.remove(currComment);
//                        txtShowReplies.setText(MyHelper.getString(R.string.show_replies));
//                    } else {
//                        layReplySection.setVisibility(View.VISIBLE);
//                        txtShowReplies.setVisibility(View.VISIBLE);
//                        selectedShowReplies.add(currComment);
//                        txtShowReplies.setText(MyHelper.getString(R.string.hide_replies));
//                    }

                    break;
                case R.id.txtLoadMoreReplies:
                    prgMoreReplyLoading.setVisibility(View.VISIBLE);
                    txtLoadMoreReplies.setVisibility(View.GONE);
                    getReplyByParentIdApiCall(this, currComment);
//                    if (isShowRepliesChecked(currComment)) {
//                        layReplySection.setVisibility(View.GONE);
//                        txtShowReplies.setVisibility(View.VISIBLE);
//                        selectedShowReplies.remove(currComment);
//                        txtShowReplies.setText(MyHelper.getString(R.string.show_replies));
//                    } else {
//                        layReplySection.setVisibility(View.VISIBLE);
//                        txtShowReplies.setVisibility(View.VISIBLE);
//                        selectedShowReplies.add(currComment);
//                        txtShowReplies.setText(MyHelper.getString(R.string.hide_replies));
//                    }

                    break;
            }
        }
    }

    private void warningPopup(Comment currComment) {
        new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom))
                .setTitle(MyHelper.getString(R.string.rekhta))
                .setMessage("Do you want to delete?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    getDeleteCommentApiCall(currComment);
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create().show();


    }

    private void getDeleteCommentApiCall(Comment currComment) {
        getActivity().showDialog();
        new PostRemoveComment().setCommentId(currComment.getId()).runAsync((BaseServiceable.OnApiFinishListener<PostRemoveComment>) postRemoveComment -> {
            getActivity().dismissDialog();
            if (postRemoveComment.isValidResponse()) {
                showToast("Deleted Successfully");
                commentList.remove(currComment);
                if (getActivity() instanceof AddCommentActivity) {
                    ((AddCommentActivity) getActivity()).refreshTotalCommentCount(postRemoveComment.getTotalCommentCount());
                    ((AddCommentActivity) getActivity()).refreshInputComment();
                }

                notifyDataSetChanged();
            }
        });
    }


    private void getReplyByParentIdApiCall(final CommentViewHolder viewHolder, final Comment currComment) {
        new GetReplyByParentId().setParentCommentId(currComment.getId()).setSortBy(sortFilter).setIsAsc(sortOrder)
                .setPageCount(currComment.getCurrentPageIndex()).runAsync((BaseServiceable.OnApiFinishListener<GetReplyByParentId>) getReplyByParentId -> {
            currComment.setDataLoading(false);
            if (getReplyByParentId.isValidResponse()) {
                if (getReplyByParentId.getReplyComments().size() > 0) {
                    currComment.setCurrentPageIndex(currComment.getCurrentPageIndex() + 1);
                    currComment.setRepliesShowing(true);
                    currComment.getReplyComment().addAll(getReplyByParentId.getReplyComments());

                    viewHolder.prgMoreReplyLoading.setVisibility(View.GONE);
                    viewHolder.txtLoadMoreReplies.setVisibility(View.VISIBLE);
                    if (currComment.getReplyCommentAdapter() != null)
                        currComment.getReplyCommentAdapter().notifyDataSetChanged();
                    else
                        notifyDataSetChanged();
//                    showReplies(viewHolder, currComment);
                    if (currComment.isAllRepliesLoaded())
                        viewHolder.txtLoadMoreReplies.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showReplies(final CommentViewHolder viewHolder, final Comment currComment) {
        currComment.setRepliesShowing(true);
        viewHolder.layReplySection.setVisibility(View.VISIBLE);
        viewHolder.rvReplyComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        currComment.setReplyCommentAdapter(new ReplyCommentAdapter(getActivity(), currComment.getReplyComment(), currComment));
        viewHolder.rvReplyComment.setAdapter(currComment.getReplyCommentAdapter());
    }

    private void hideReplies(final CommentViewHolder viewHolder, final Comment currComment) {
        currComment.setRepliesShowing(false);
        viewHolder.layReplySection.setVisibility(View.GONE);
    }

    private void getComplainTypeApiCall(Comment comment) {
        getActivity().showDialog();
        new GetUserComplainType().runAsync((BaseServiceable.OnApiFinishListener<GetUserComplainType>) getUserComplainType -> {
            getActivity().dismissDialog();
            if (getUserComplainType.isValidResponse()) {
                if (getUserComplainType.getComplainTypes().size() > 0)
                    openPopupShowComplainTypeList(getUserComplainType.getComplainTypes(), comment);
            } else
                BaseActivity.showToast(getUserComplainType.getErrorMessage());
        });
    }

    private void openPopupShowComplainTypeList(ArrayList<ComplainType> complainTypes, Comment comment) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_popup_complain_type);
        ListView lstComplainType = (ListView) dialog.findViewById(R.id.lstComplainType);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtSubmit = (TextView) dialog.findViewById(R.id.txtSubmit);
        ComplainTypeAdapter adapter = new ComplainTypeAdapter(getActivity(), complainTypes);
        lstComplainType.setAdapter(adapter);
        txtCancel.setOnClickListener(view -> dialog.dismiss());
        txtSubmit.setOnClickListener(view -> {
            if (adapter.getSelectedTypeIds().size() > 0) {
                String selectTypeId = adapter.getSelectedTypeIds().get(0);
                getActivity().showDialog();
                long currTime = System.currentTimeMillis();
                new PostUserComplain().setComplainId(comment.getId()).setTargetId(comment.getTargetId()).setReportTypeId(selectTypeId)
                        .runAsync((BaseServiceable.OnApiFinishListener<PostUserComplain>) postUserComplain -> {
                            SLog.d("Time_Difference", String.valueOf(System.currentTimeMillis() - currTime));
                            getActivity().dismissDialog();
                            if (postUserComplain.isValidResponse()) {
                                String message = MyHelper.getString(R.string.success_report_complain_message);
                                openPopupConformationMessage(message);
                                dialog.dismiss();
                            } else
                                BaseActivity.showToast(postUserComplain.getErrorMessage());
                        });
            } else
                BaseActivity.showToast("Please select any one");
        });
        dialog.show();
    }

    private void openPopupConformationMessage(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.okay), (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean isLikeChecked(Comment comment) {
        if (comment == null)
            return false;
        return selectedLiked.contains(comment);
    }

    private boolean isDisLikeChecked(Comment comment) {
        if (comment == null)
            return false;
        return selectedDisLiked.contains(comment);
    }

    private boolean isShowRepliesChecked(Comment comment) {
        if (comment == null)
            return false;
        return selectedShowReplies.contains(comment);
    }

}

