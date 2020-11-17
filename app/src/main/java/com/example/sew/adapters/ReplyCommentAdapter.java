package com.example.sew.adapters;


import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.example.sew.R;
import com.example.sew.activities.AddCommentActivity;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.LoginActivity;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetMarkLikeDislike;
import com.example.sew.apis.GetUserComplainType;
import com.example.sew.apis.PostRemoveComment;
import com.example.sew.apis.PostUserComplain;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.Comment;
import com.example.sew.models.ComplainType;
import com.example.sew.models.ReplyComment;
import com.example.sew.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyCommentAdapter extends BaseRecyclerAdapter {
    private ArrayList<ReplyComment> replyComments;
    private ArrayList<ReplyComment> selectedLiked = new ArrayList<ReplyComment>();
    private ArrayList<ReplyComment> selectedDisLiked = new ArrayList<ReplyComment>();
    private Comment currComment;

    public ReplyCommentAdapter(BaseActivity activity, ArrayList<ReplyComment> replyComments, Comment currComments) {
        super(activity);
        this.replyComments = replyComments;
        this.currComment = currComments;
    }

    public ReplyComment getItem(int position) {
        return replyComments.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReplyViewHolder(getInflatedView(R.layout.cell_reply_section, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof ReplyViewHolder))
            return;
        ReplyComment currReply = getItem(position);
        ReplyViewHolder replyViewHolder = (ReplyViewHolder) holder;
        replyViewHolder.imgMore.setTag(R.id.tag_data, currReply);
        replyViewHolder.layLike.setTag(R.id.tag_data, currReply);
        replyViewHolder.layDislike.setTag(R.id.tag_data, currReply);
        replyViewHolder.txtReply.setTag(R.id.tag_data, currReply);
        replyViewHolder.txtName.setText(currReply.getCommentByUserName());
        replyViewHolder.txtComment.setText(currReply.getCommentDescription());
        replyViewHolder.txtLikeCount.setText(currReply.getTotalLike());
        replyViewHolder.txtDislikeCount.setText(currReply.getTotalDisLike());
        replyViewHolder.txtTime.setText(currReply.getCommentDate());

        if (MyService.isUserLogin()) {
            if (currReply.isEditable()) {
                User user = MyService.getUser();
                if (!TextUtils.isEmpty(user.getImageName())) {
                    ImageHelper.setImage(replyViewHolder.imgImage, user.getImageName(), Enums.PLACEHOLDER_TYPE.PROFILE);
                    replyViewHolder.txtFirstCharacter.setVisibility(View.GONE);
                    replyViewHolder.imgImage.setVisibility(View.VISIBLE);
                } else {
                    replyViewHolder.txtFirstCharacter.setVisibility(View.VISIBLE);
                    replyViewHolder.imgImage.setVisibility(View.GONE);
                    replyViewHolder.txtFirstCharacter.setText(String.valueOf(currComment.getCommentByUserName().charAt(0)).toUpperCase());
                }
            } else {
                if (!TextUtils.isEmpty(currReply.getOtheruserImage())) {
                    ImageHelper.setImage(replyViewHolder.imgImage, currReply.getOtheruserImage(), Enums.PLACEHOLDER_TYPE.PROFILE);
                    replyViewHolder.txtFirstCharacter.setVisibility(View.GONE);
                    replyViewHolder.imgImage.setVisibility(View.VISIBLE);
                } else {
                    replyViewHolder.txtFirstCharacter.setVisibility(View.VISIBLE);
                    replyViewHolder.imgImage.setVisibility(View.GONE);
                    replyViewHolder.txtFirstCharacter.setText(String.valueOf(currComment.getCommentByUserName().charAt(0)).toUpperCase());
                }

            }

        } else {
            replyViewHolder.txtFirstCharacter.setVisibility(View.VISIBLE);
            replyViewHolder.imgImage.setVisibility(View.GONE);
            replyViewHolder.txtFirstCharacter.setText(String.valueOf(currComment.getCommentByUserName().charAt(0)).toUpperCase());
        }


//        replyViewHolder.txtTime.setText(MyHelper.getTimeAgo(currReply.getCommentTimeStamp()));
//        ImageHelper.setImage(replyViewHolder.imgImage, currReply.getUserImage());
        if (currReply.getUserFavourite().equalsIgnoreCase("yes")) {
            selectedLiked.add(currReply);
            replyViewHolder.imgLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
        } else {
            replyViewHolder.imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
        }
        if (currReply.getUserFavourite().equalsIgnoreCase("no")) {
            selectedDisLiked.add(currReply);
            replyViewHolder.imgDislike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
        } else
            replyViewHolder.imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);

    }

    @Override
    public int getItemCount() {
        return replyComments.size();
    }

    class ReplyViewHolder extends BaseViewHolder {
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
        @BindView(R.id.imgMore)
        ImageView imgMore;
        @BindView(R.id.layLike)
        LinearLayout layLike;
        @BindView(R.id.layDislike)
        LinearLayout layDislike;
        @BindView(R.id.imgLike)
        ImageView imgLike;
        @BindView(R.id.imgDislike)
        ImageView imgDislike;
        @BindView(R.id.txtFirstCharacter)
        TextView txtFirstCharacter;
        @BindView(R.id.txtReply)
        TextView txtReply;

        ReplyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.layLike, R.id.layDislike, R.id.txtReply, R.id.imgMore})
        public void onViewClicked(View view) {
            ReplyComment currReplyComment = (ReplyComment) view.getTag(R.id.tag_data);
            switch (view.getId()) {
                case R.id.layLike:
                    if (MyService.isUserLogin()) {
                        if (isLikeChecked(currReplyComment)) {
                            imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currReplyComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.REMOVE_STATUS)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
//                                    imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedLiked.remove(currReplyComment);
                                    selectedLiked.clear();
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        } else {
                            imgLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
                            imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currReplyComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.LIKE)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
//                                    imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedLiked.add(currReplyComment);
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
                        imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                        imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                        if (isDisLikeChecked(currReplyComment)) {
                            new GetMarkLikeDislike().setCommentId(currReplyComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.REMOVE_STATUS)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgDislike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
//                                    imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedDisLiked.remove(currReplyComment);
                                    selectedDisLiked.clear();
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        } else {
                            imgDislike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
                            imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currReplyComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.DISLIKE)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgDislike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
//                                    imgLike.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedDisLiked.add(currReplyComment);
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        }
                    } else {
                        getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                        BaseActivity.showToast("Please login");
                    }
                    break;
                case R.id.txtReply:
                    if (!MyService.isUserLogin()) {
                        getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                        BaseActivity.showToast("Please login");
                    } else {
                        if (getActivity() instanceof AddCommentActivity) {
                            ((AddCommentActivity) getActivity()).childReply(true, currReplyComment, currComment);
                        }


                    }
                    break;
                case R.id.imgMore:
                    PopupMenu popup = new PopupMenu(getActivity(), imgMore);
                    ArrayList<String> popupList = new ArrayList<>();
                    if (currReplyComment.isEditable()) {
                        //popupList.add(getString(R.string.report_comment));
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
                                getComplainTypeApiCall(currReplyComment);
                        } else if (item.toString().equalsIgnoreCase(getString(R.string.edit))) {
                            if (!MyService.isUserLogin()) {
                                getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                                BaseActivity.showToast("Please login");
                            } else {
                                if (getActivity() instanceof AddCommentActivity)
                                    ((AddCommentActivity) getActivity()).childEditComment(currReplyComment);

                            }
                        } else if (item.toString().equalsIgnoreCase(getString(R.string.delete))) {
                            if (!MyService.isUserLogin()) {
                                getActivity().startActivity(LoginActivity.getInstance(getActivity()));
                                BaseActivity.showToast("Please login");
                            } else
                                warningPopup(currReplyComment);
                        }
                        return true;

                    });
                    popup.show();
                    break;
            }
        }

        private void warningPopup(ReplyComment currReplyComment) {
            new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom))
                    .setTitle(MyHelper.getString(R.string.rekhta))
                    .setMessage("Do you want to delete?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        getDeleteCommentApiCall(currReplyComment);
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create().show();
        }

        private void getDeleteCommentApiCall(ReplyComment currReplyComment) {
            getActivity().showDialog();
            new PostRemoveComment().setCommentId(currReplyComment.getId()).runAsync((BaseServiceable.OnApiFinishListener<PostRemoveComment>) postRemoveComment -> {
                getActivity().dismissDialog();
                if (postRemoveComment.isValidResponse()) {
                    showToast("Deleted Successfully");
                    replyComments.remove(currReplyComment);
                    if (getActivity() instanceof AddCommentActivity) {
                        ((AddCommentActivity) getActivity()).refreshTotalCommentCount(postRemoveComment.getTotalCommentCount());
                        ((AddCommentActivity) getActivity()).refreshCommentAdapter();
                    }
                    currComment.updateReplyCount(postRemoveComment.getReplyCount());
                    notifyDataSetChanged();
                }
            });
        }


    }

    private void getComplainTypeApiCall(ReplyComment replyComment) {
        new GetUserComplainType().runAsync((BaseServiceable.OnApiFinishListener<GetUserComplainType>) getUserComplainType -> {
            if (getUserComplainType.isValidResponse()) {
                if (getUserComplainType.getComplainTypes().size() > 0)
                    openPopupShowComplainTypeList(getUserComplainType.getComplainTypes(), replyComment);
            } else
                BaseActivity.showToast(getUserComplainType.getErrorMessage());
        });
    }

    private void openPopupShowComplainTypeList(ArrayList<ComplainType> complainTypes, ReplyComment replyComment) {
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
                new PostUserComplain().setComplainId(replyComment.getId()).setTargetId(replyComment.getTargetId()).setReportTypeId(selectTypeId)
                        .runAsync((BaseServiceable.OnApiFinishListener<PostUserComplain>) postUserComplain -> {
                            if (postUserComplain.isValidResponse()) {
                                String message = MyHelper.getString(R.string.success_report_complain_message);
                                openPopupConfrimationMessage(message);
                                dialog.dismiss();
                            } else
                                BaseActivity.showToast(postUserComplain.getErrorMessage());
                        });
            } else
                BaseActivity.showToast("Please select any one");
        });
        dialog.show();
    }

    private void openPopupConfrimationMessage(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.okay), (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean isLikeChecked(ReplyComment replyComment) {
        if (replyComment == null)
            return false;
        return selectedLiked.contains(replyComment);
    }

    private boolean isDisLikeChecked(ReplyComment replyComment) {
        if (replyComment == null)
            return false;
        return selectedDisLiked.contains(replyComment);
    }
}
