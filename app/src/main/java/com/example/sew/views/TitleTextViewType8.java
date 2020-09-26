package com.example.sew.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;

import com.example.sew.R;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

public class TitleTextViewType8 extends CommonTextView {
    public TitleTextViewType8(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TitleTextViewType8(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleTextViewType8(Context context) {
        super(context);
    }

    @Override
    public Typeface geCustomTypeFace() {
        int fontName = R.font.notoserif_light;
        isTextStyleNeeded = false;
        switch (getTextStyle()) {
            case NORMAL:/*latoxregular,lailaregular,NotoNastaliqUrdu;*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
                    fontName = R.font.lato_x_regular_eng;
                    isTextStyleNeeded = false;
                } else
                    fontName = R.font.laila_regular_hin;
                break;
            case BOLD:/*latoxbold,lailaregular,NotoNastaliqUrdu*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
                    fontName = R.font.lato_x_bold_eng;
                    isTextStyleNeeded = false;
                } else
                    fontName = R.font.laila_regular_hin;
                break;
            case ITALIC:
            case BOLD_ITALIC:
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {
                    fontName = R.font.laila_regular_hin;
                } else {
                    fontName = R.font.merriweather_extended_light_italic_eng;
                }
                break;
        }
        return ResourcesCompat.getFont(getContext(), fontName);
    }
}
