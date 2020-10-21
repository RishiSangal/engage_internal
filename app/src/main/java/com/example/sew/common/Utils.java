package com.example.sew.common;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.example.sew.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static float dpFromPx(final float px) {
        return px / MyApplication.getContext().getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final float dp) {
        return dp * MyApplication.getContext().getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
//            p.setLayoutDirection(MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
            if (p.getMarginStart() != left || p.topMargin != top || p.getMarginEnd() != right || p.bottomMargin != bottom) {
                p.setMargins(left, top, right, bottom);
                view.requestLayout();
            }
        }
    }

    public static void setMarginStart(View view, int start, int end) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMarginStart(start);
            view.requestLayout();
        }
    }

    public static String getCurrentFM() {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return formatter1.format(calendar.getTime());
    }
}
