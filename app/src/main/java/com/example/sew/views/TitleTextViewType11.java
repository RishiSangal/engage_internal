package com.example.sew.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.example.sew.R;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

import androidx.core.content.res.ResourcesCompat;

public class TitleTextViewType11 extends CommonTextView {
    public TitleTextViewType11(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TitleTextViewType11(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleTextViewType11(Context context) {
        super(context);
    }

    @Override
    public Typeface geCustomTypeFace() {
        int fontName = R.font.notoserif_light;
        isTextStyleNeeded = false;
        switch (getTextStyle()) {
            case NORMAL: /*latoxregular,mehr_master,laila_regular_hin*/
//                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
//                   // fontName = R.font.noto_nastaliq_regular_urdu;
//                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
//                   // fontName = R.font.lato_x_regular_eng;
//                    isTextStyleNeeded = false;
//                } else
//                   // fontName = R.font.noto_devanagari_hin;
                break;
            case BOLD: /*latoxheavy,mehr_master,NotoDevanagaribold*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
                    fontName = R.font.lato_heavy_eng;
                    isTextStyleNeeded = false;
                } else
                    fontName = R.font.noto_sans_devanagari_bold_hin;
                break;
            case ITALIC: /*latoxitalic,mehr_master,NotoDevanagari*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {
                    fontName = R.font.noto_devanagari_hin;
                } else {
                    fontName = R.font.lato_x_italic_eng;
                }
                break;

            case BOLD_ITALIC:  /*merriweather_extendedLtIt,lailaregular,mehr_master*/
//                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
//                    fontName = R.font.noto_nastaliq_regular_urdu;
//                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {
//                    fontName = R.font.laila_regular_hin;
//                } else {
//                    fontName = R.font.merriweather_extended_light_italic_eng;
//                }
                break;
        }
        return ResourcesCompat.getFont(getContext(), fontName);
    }
}


