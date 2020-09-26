package com.example.sew.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.example.sew.R;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

import androidx.core.content.res.ResourcesCompat;

public class TitleTextViewType3 extends CommonTextView {
    public TitleTextViewType3(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TitleTextViewType3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleTextViewType3(Context context) {
        super(context);
    }

    @Override
    public Typeface geCustomTypeFace() {
        int fontName = R.font.notoserif_light;
        isTextStyleNeeded = true;
        switch (getTextStyle()) {
            case NORMAL:
            case BOLD:
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
                    fontName = R.font.oswald_regular_eng;
                } else
                    fontName = R.font.laila_regular;
                break;
            case ITALIC:
            case BOLD_ITALIC:
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {
                    fontName = R.font.laila_regular;
                } else {
                    fontName = R.font.notoserif_light;
                }
                break;
        }
        return ResourcesCompat.getFont(getContext(), fontName);
    }
}
