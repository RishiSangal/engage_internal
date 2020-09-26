package com.example.sew.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.example.sew.R;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

import androidx.core.content.res.ResourcesCompat;

public class TitleTextViewType10 extends CommonTextView {
    public TitleTextViewType10(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TitleTextViewType10(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleTextViewType10(Context context) {
        super(context);
    }

    @Override
    public Typeface geCustomTypeFace() {
        int fontName = R.font.notoserif_light;
        isTextStyleNeeded = false;
        switch (getTextStyle()) {
            case NORMAL: /*latoxregular,NotoNastaliqUrdu,laila_regular_hin*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
                    fontName = R.font.lato_x_regular_eng;
                    isTextStyleNeeded = false;
                } else
                    fontName = R.font.noto_devanagari_hin;
                break;
            case BOLD: /*latoxbold,NotoNastaliqUrdu,NotoDevanagari*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
                    fontName = R.font.lato_x_bold_eng;
                    isTextStyleNeeded = false;
                } else
                    fontName = R.font.noto_sans_devanagari_bold_hin;
                break;
            case ITALIC: /*latoxitalic,NotoNastaliqUrdu,NotoDevanagari*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.noto_nastaliq_regular_urdu;
                else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {
                    fontName = R.font.noto_devanagari_hin;
                } else {
                    fontName = R.font.lato_x_italic_eng;
                }
                break;

            case BOLD_ITALIC:  /*merriweather_extendedLtIt,lailaregular,NotoNastaliqUrdu*/
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    fontName = R.font.notonastaliqurdu_regular;
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
