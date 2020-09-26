package com.example.sew.common;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.fragments.PoetNazmFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentSortNazmPopupWindow extends RelativePopupWindow {
    BaseActivity activity;
    public PoetNazmFragment fragment;
    ViewHolder viewHolder;
    private String contentTitle;
    String sortedBy;
    public ContentSortNazmPopupWindow(BaseActivity context, String contentTitle, PoetNazmFragment fragment,String sortedBy) {
        this.activity = context;
        this.contentTitle = contentTitle;
        this.fragment = fragment;
        this.sortedBy= sortedBy;
        View convertView = LayoutInflater.from(context).inflate(R.layout.content_sort_popup, null);
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
        viewHolder.txtAlphabetic.setText(MyHelper.getString(R.string.alphabetic));
        viewHolder.txtPopularity.setText(MyHelper.getString(R.string.popularity));
        viewHolder.txtRadeef.setText(MyHelper.getString(R.string.radeef));
        viewHolder.txtAlphabetic.setVisibility(View.GONE);
        viewHolder.txtRadeef.setVisibility(View.GONE);
        if(sortedBy==Enums.SORT_CONTENT.POPULARITY.getKey())
            viewHolder.txtPopularity.setTypeface(Typeface.DEFAULT_BOLD);
        if(sortedBy==Enums.SORT_CONTENT.ALPHABETIC.getKey())
            viewHolder.txtAlphabetic.setTypeface(Typeface.DEFAULT_BOLD);
        if(sortedBy==Enums.SORT_CONTENT.RADEEF.getKey())
            viewHolder.txtRadeef.setTypeface(Typeface.DEFAULT_BOLD);
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH || MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {
            viewHolder.txtAlphabetic.setVisibility(View.VISIBLE);
        }

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
        ContentSortNazmPopupWindow contentSortPopupWindow;
        @BindView(R.id.txtPopularity)
        TextView txtPopularity;
        @BindView(R.id.txtAlphabetic)
        TextView txtAlphabetic;
        @BindView(R.id.txtRadeef)
        TextView txtRadeef;

        ViewHolder(View view, ContentSortNazmPopupWindow contentSortPopupWindow) {
            ButterKnife.bind(this, view);
            this.contentSortPopupWindow = contentSortPopupWindow;
        }

        @OnClick(R.id.txtPopularity)
        void onPopularityClick() {
            fragment.sortContent(Enums.SORT_CONTENT.POPULARITY);
            contentSortPopupWindow.dismiss();
        }

        @OnClick(R.id.txtAlphabetic)
        void onAlphabeticallyClick() {
            fragment.sortContent(Enums.SORT_CONTENT.ALPHABETIC);
            contentSortPopupWindow.dismiss();
        }

        @OnClick(R.id.txtRadeef)
        void OnRadeefClick() {
            fragment.sortContent(Enums.SORT_CONTENT.RADEEF);
            contentSortPopupWindow.dismiss();
        }
    }
}
