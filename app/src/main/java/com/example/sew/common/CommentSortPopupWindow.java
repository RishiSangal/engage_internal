package com.example.sew.common;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.AddCommentActivity;
import com.example.sew.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentSortPopupWindow extends RelativePopupWindow {
    BaseActivity activity;

    ViewHolder viewHolder;
    String filterOption, ascDesOrder;
    AddCommentActivity currentActivity;

    public CommentSortPopupWindow(BaseActivity context, String filterOptions, String ascDesOrders, AddCommentActivity addCommentActivity) {
        this.activity = context;
        this.filterOption = filterOptions;
        this.ascDesOrder = ascDesOrders;
        this.currentActivity = addCommentActivity;
        View convertView = LayoutInflater.from(context).inflate(R.layout.comment_sort_popup, null);
        viewHolder = new ViewHolder(convertView, this);
        setContentView(convertView);
        setWidth(getDesiredWidth(context));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        // setSelectableItemForeground(viewHolder.txtMenuLanguageEn, viewHolder.txtMenuLanguageHin,viewHolder.txtMenuLanguageUr);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(40);
        }
        viewHolder.txtTopComments.setText("Top Comments");
        viewHolder.txtNewestComments.setText("Newest Comments");
        viewHolder.txtRepliedByRekhta.setText("Replied by Rekhta");
        if (filterOption == Enums.FORUM_SORT_FIELDS.POPULARITY.getKey())
        viewHolder.txtTopComments.setTextColor(activity.getResources().getColor(R.color.dark_blue));
        if (filterOption == Enums.FORUM_SORT_FIELDS.RECENT_COMMENT.getKey())
        viewHolder.txtNewestComments.setTextColor(activity.getResources().getColor(R.color.dark_blue));
        if (filterOption == Enums.FORUM_SORT_FIELDS.COMMENT_BY_REKHTA.getKey())
            viewHolder.txtRepliedByRekhta.setTextColor(activity.getResources().getColor(R.color.dark_blue));
    }

    public BaseActivity getActivity() {
        return activity;
    }

    private int getDesiredWidth(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width / 2;
    }

    class ViewHolder {
        CommentSortPopupWindow commentSortPopupWindow;
        @BindView(R.id.txtTopComments)
        TextView txtTopComments;
        @BindView(R.id.txtNewestComments)
        TextView txtNewestComments;
        @BindView(R.id.txtRepliedByRekhta)
        TextView txtRepliedByRekhta;

        ViewHolder(View view, CommentSortPopupWindow commentSortPopupWindow) {
            ButterKnife.bind(this, view);
            this.commentSortPopupWindow = commentSortPopupWindow;
        }

        @OnClick(R.id.txtTopComments)
        void onTopCommentsClick() {
            currentActivity.sortComment(Enums.FORUM_SORT_FIELDS.POPULARITY, Enums.COMMENT_SORT_LIST.DESCENDING);
            commentSortPopupWindow.dismiss();
        }

        @OnClick(R.id.txtNewestComments)
        void onNewestCommentsClick() {
            currentActivity.sortComment(Enums.FORUM_SORT_FIELDS.RECENT_COMMENT, Enums.COMMENT_SORT_LIST.DESCENDING);
            commentSortPopupWindow.dismiss();
        }

        @OnClick(R.id.txtRepliedByRekhta)
        void OnRepliedByRekhtaClick() {
            currentActivity.sortComment(Enums.FORUM_SORT_FIELDS.COMMENT_BY_REKHTA, Enums.COMMENT_SORT_LIST.DESCENDING);
            commentSortPopupWindow.dismiss();
        }
    }
}


