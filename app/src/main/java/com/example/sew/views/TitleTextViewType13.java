package com.example.sew.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;

import com.example.sew.R;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

public class TitleTextViewType13 extends CommonTextView {
    public TitleTextViewType13(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TitleTextViewType13(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleTextViewType13(Context context) {
        super(context);
    }

    @Override
    public Typeface geCustomTypeFace() {
        int fontName = R.font.notoserif_light;
        isTextStyleNeeded = false;
        switch (getTextStyle()) {
            case NORMAL: /*latoxregular,NotoNastaliqUrdu,NotoDevanagari*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.lato_x_regular_eng;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
                    fontName = R.font.lato_x_regular_eng;
                    isTextStyleNeeded = false;
                } else
                    fontName = R.font.noto_devanagari_hin;
                break;
            case BOLD: /*latoxbold,NotoNastaliqUrdu,NotoDevanagari*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.lato_x_regular_eng;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
                    fontName = R.font.lato_x_bold_eng;
                    isTextStyleNeeded = false;
                } else
                    fontName = R.font.noto_devanagari_hin;
                break;
            case ITALIC:/*merriweather_extendedLtIt,lailaregular,NotoNastaliqUrdu*/
            case BOLD_ITALIC:
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {
                    fontName = R.font.laila_regular_hin;
                } else {
                    fontName = R.font.laila_regular_hin;
                }
                break;
        }
        return ResourcesCompat.getFont(getContext(), fontName);
    }
}
