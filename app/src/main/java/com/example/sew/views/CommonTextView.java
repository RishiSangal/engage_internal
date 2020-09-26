package com.example.sew.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.example.sew.R;
import com.example.sew.MyApplication;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.common.Utils;
import com.example.sew.helpers.MyService;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;


public class CommonTextView extends AppCompatTextView implements ICommonValues {
    boolean isSingleLine,
            isBold,
            isItalic,
            isTextStyleNeeded = true;
    private int original_padding_left,
            original_padding_right,
            original_padding_top,
            original_padding_bottom;
    private float textSizeEn,
            textSizeHi,
            textSizeUr;
    private float textPaddingTopEn, textPaddingTopHi, textPaddingTopUr, textPaddingBottomEn,
            textPaddingBottomHi, textPaddingBottomUr, textPaddingStartEn, textPaddingStartHi,
            textPaddingStartUr, textPaddingEndEn, textPaddingEndHi, textPaddingEndUr,
            textPaddingEn, textPaddingHi, textPaddingUr;
    private int textMarginTopEn, textMarginTopHi, textMarginTopUr,
            textMarginBottomEn, textMarginBottomHi, textMarginBottomUr,
            textMarginStartEn, textMarginStartHi, textMarginStartUr,
            textMarginEndEn, textMarginEndHi, textMarginEndUr;

    enum TEXT_STYLE {NORMAL, BOLD, ITALIC, BOLD_ITALIC}

    public TEXT_STYLE getTextStyle() {
        if (isBold && isItalic)
            return TEXT_STYLE.BOLD_ITALIC;
        else if (isBold)
            return TEXT_STYLE.BOLD;
        else if (isItalic)
            return TEXT_STYLE.ITALIC;
        else
            return TEXT_STYLE.NORMAL;
    }


    public CommonTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeStyleParameters(attrs);
        initialize();
    }

    public CommonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeStyleParameters(attrs);
        initialize();
    }

    public CommonTextView(Context context) {
        super(context);
        initialize();
    }

    private void initializeStyleParameters(AttributeSet attrs) {

        initializeTextSize(attrs);
        initializeMargins(attrs);
        initializePadding(attrs);
    }

    private void initializeTextSize(AttributeSet attrs) {
        TypedArray t = MyApplication.getContext().obtainStyledAttributes(attrs, R.styleable.CommonTextView, 0, 0);
        isSingleLine = t.getBoolean(R.styleable.CommonTextView_android_singleLine, false);
        int textStyle = t.getInt(R.styleable.CommonTextView_android_textStyle, 0);
        textSizeEn = t.getDimension(R.styleable.CommonTextView_textSizeEn, 0);
        textSizeHi = t.getDimension(R.styleable.CommonTextView_textSizeHi, 0);
        textSizeUr = t.getDimension(R.styleable.CommonTextView_textSizeUr, 0);
        textSizeEn = textSizeEn == 0 ? getTextSize() : textSizeEn;
        textSizeHi = textSizeHi == 0 ? getTextSize() : textSizeHi;
        textSizeUr = textSizeUr == 0 ? getTextSize() : textSizeUr;

        if (getTypeface() != null) {
            isBold = (textStyle & Typeface.BOLD) != 0;
            isItalic = (textStyle & Typeface.ITALIC) != 0;
        }
        t.recycle();
    }

    private void initializePadding(AttributeSet attrs) {
        TypedArray paddingTypedArray = MyApplication.getContext().obtainStyledAttributes(attrs, R.styleable.CommonTextView, 0, 0);
        int padding = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_padding, 0);
        int paddingEn = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingEn, padding);
        int paddingHi = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingHi, padding);
        int paddingUr = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingUr, padding);

        int dp_1 = Math.round(Utils.pxFromDp(1));
        original_padding_left = isItalic ? Math.max(dp_1, getPaddingLeft()) : getPaddingLeft();
        original_padding_right = isItalic ? Math.max(dp_1, getPaddingRight()) : getPaddingRight();
        original_padding_top = getPaddingTop();
        original_padding_bottom = getPaddingBottom();

        int defaultPaddingStartEn = getPaddingStart(paddingTypedArray, paddingEn);
        int defaultPaddingStartHi = getPaddingStart(paddingTypedArray, paddingHi);
        int defaultPaddingStartUr = getPaddingStart(paddingTypedArray, paddingUr);

        int defaultPaddingEndEn = getPaddingEnd(paddingTypedArray, paddingEn);
        int defaultPaddingEndHi = getPaddingEnd(paddingTypedArray, paddingHi);
        int defaultPaddingEndUr = getPaddingEnd(paddingTypedArray, paddingUr);

        int defaultPaddingTopEn = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingTop, paddingEn);
        int defaultPaddingTopHi = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingTop, paddingHi);
        int defaultPaddingTopUr = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingTop, paddingUr);

        int defaultPaddingBottomEn = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingBottom, paddingEn);
        int defaultPaddingBottomHi = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingBottom, paddingHi);
        int defaultPaddingBottomUr = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingBottom, paddingUr);


        textPaddingTopEn = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingTopEn, defaultPaddingTopEn);
        textPaddingTopHi = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingTopHi, defaultPaddingTopHi);
        textPaddingTopUr = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingTopUr, defaultPaddingTopUr);

        textPaddingBottomEn = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingBottomEn, defaultPaddingBottomEn);
        textPaddingBottomHi = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingBottomHi, defaultPaddingBottomHi);
        textPaddingBottomUr = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingBottomUr, defaultPaddingBottomUr);

        textPaddingStartEn = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingStartEn, defaultPaddingStartEn);
        textPaddingStartHi = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingStartHi, defaultPaddingStartHi);
        textPaddingStartUr = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingStartUr, defaultPaddingStartUr);

        textPaddingEndEn = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingEndEn, defaultPaddingEndEn);
        textPaddingEndHi = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingEndHi, defaultPaddingEndHi);
        textPaddingEndUr = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_paddingEndUr, defaultPaddingEndUr);

        paddingTypedArray.recycle();
    }

    private int getPaddingStart(TypedArray paddingTypedArray, int defaultPadding) {
        int paddingLeft = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingLeft, defaultPadding);
        int paddingStart = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingStart, defaultPadding);
        return Math.max(paddingLeft, paddingStart);
    }

    private int getPaddingEnd(TypedArray paddingTypedArray, int defaultPadding) {
        int paddingRight = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingRight, defaultPadding);
        int paddingEnd = paddingTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_paddingEnd, defaultPadding);
        return Math.max(paddingRight, paddingEnd);
    }

    private void initializeMargins(AttributeSet attrs) {
        TypedArray marginTypedArray = MyApplication.getContext().obtainStyledAttributes(attrs, R.styleable.CommonTextView, 0, 0);
        int margin = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_margin, 0);
        int marginEn = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginEn, margin);
        int marginHi = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginHi, margin);
        int marginUr = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginUr, margin);

        int defaultMarginStartEn = getMarginStart(marginTypedArray, marginEn);
        int defaultMarginStartHi = getMarginStart(marginTypedArray, marginHi);
        int defaultMarginStartUr = getMarginStart(marginTypedArray, marginUr);

        int defaultMarginEndEn = getMarginEnd(marginTypedArray, marginEn);
        int defaultMarginEndHi = getMarginEnd(marginTypedArray, marginHi);
        int defaultMarginEndUr = getMarginEnd(marginTypedArray, marginUr);

        int defaultMarginTopEn = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginTop, marginEn);
        int defaultMarginTopHi = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginTop, marginHi);
        int defaultMarginTopUr = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginTop, marginUr);

        int defaultMarginBottomEn = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginBottom, marginEn);
        int defaultMarginBottomHi = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginBottom, marginHi);
        int defaultMarginBottomUr = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginBottom, marginUr);


        textMarginTopEn = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginTopEn, defaultMarginTopEn);
        textMarginTopHi = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginTopHi, defaultMarginTopHi);
        textMarginTopUr = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginTopUr, defaultMarginTopUr);

        textMarginBottomEn = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginBottomEn, defaultMarginBottomEn);
        textMarginBottomHi = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginBottomHi, defaultMarginBottomHi);
        textMarginBottomUr = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginBottomUr, defaultMarginBottomUr);

        textMarginStartEn = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginStartEn, defaultMarginStartEn);
        textMarginStartHi = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginStartHi, defaultMarginStartHi);
        textMarginStartUr = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginStartUr, defaultMarginStartUr);

        textMarginEndEn = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginEndEn, defaultMarginEndEn);
        textMarginEndHi = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginEndHi, defaultMarginEndHi);
        textMarginEndUr = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_layout_marginEndUr, defaultMarginEndUr);

        marginTypedArray.recycle();
    }

    private int getMarginStart(TypedArray marginTypedArray, int defaultMargin) {
        int marginLeft = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginLeft, defaultMargin);
        int marginStart = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginStart, defaultMargin);
        return Math.max(marginLeft, marginStart);
    }

    private int getMarginEnd(TypedArray marginTypedArray, int defaultMargin) {
        int marginRight = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginRight, defaultMargin);
        int marginEnd = marginTypedArray.getDimensionPixelSize(R.styleable.CommonTextView_android_layout_marginEnd, defaultMargin);
        return Math.max(marginRight, marginEnd);
    }

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode())
            return;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initialize();
            }
        };
        BaseActivity.registerBroadcastListener(broadcastReceiver, MyApplication.getContext(), BROADCAST_LANGUAGE_CHANGED);
        log("Attaching");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isInEditMode())
            return;
        BaseActivity.unRegisterBroadCastListener(broadcastReceiver, MyApplication.getContext());
        log("De-Attaching");
    }

    private void initialize() {
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
            float font_size_dp = Utils.dpFromPx(getTextSize());
            int padding_vertical = Math.round(Utils.pxFromDp(Math.round(font_size_dp / 4f) + 1));
            int padding_horizontal = Math.round(Utils.pxFromDp(Math.round(font_size_dp / 4f)));
//            super.setPadding(Math.max(padding_horizontal, original_padding_left),
//                    Math.max(padding_vertical, original_padding_top),
//                    Math.max(padding_horizontal, original_padding_right),
//                    Math.max(padding_vertical, original_padding_bottom));
            super.setPadding(Math.max(padding_horizontal, original_padding_left),
                    original_padding_top,
                    Math.max(padding_horizontal, original_padding_right),
                    original_padding_bottom);
        } else {
            super.setPadding(original_padding_left,
                    original_padding_top,
                    original_padding_right,
                    original_padding_bottom);
        }
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeEn);
                super.setPadding((int) textPaddingStartEn, (int) textPaddingTopEn, (int) textPaddingEndEn, (int) textPaddingBottomEn);
                break;
            case HINDI:
                super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeHi);
                super.setPadding((int) textPaddingStartHi, (int) textPaddingTopHi, (int) textPaddingEndHi, (int) textPaddingBottomHi);
                break;
            case URDU:
                super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeUr);
                super.setPadding((int) textPaddingEndUr, (int) textPaddingTopUr, (int) textPaddingStartUr, (int) textPaddingBottomUr);
                break;
        }
        setFont();
        initializeMargins();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        initializeMargins();
    }

    private void initializeMargins() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                Utils.setMargins(this, textMarginStartEn, textMarginTopEn, textMarginEndEn, textMarginBottomEn);
                break;
            case HINDI:
                Utils.setMargins(this, textMarginStartHi, textMarginTopHi, textMarginEndHi, textMarginBottomHi);
                break;
            case URDU:
                Utils.setMargins(this, textMarginEndUr, textMarginTopUr, textMarginStartUr, textMarginBottomUr);
                break;
        }
    }

    private void setFont() {
        int textStyle = Typeface.NORMAL;
        switch (getTextStyle()) {
            case BOLD:
                textStyle = Typeface.BOLD;
                break;
            case ITALIC:
                textStyle = Typeface.ITALIC;
                break;
            case BOLD_ITALIC:
                textStyle = Typeface.BOLD_ITALIC;
                break;
        }
        Typeface typeface = geCustomTypeFace();
        if (isTextStyleNeeded())
            super.setTypeface(typeface, textStyle);
        else
            super.setTypeface(typeface);
    }


    private Bitmap _bitmap;
    private NonClippableCanvas _canvas;

    @Override
    protected void onSizeChanged(final int width, final int height,
                                 final int oldwidth, final int oldheight) {
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU && !isSingleLine) {
            if ((width != oldwidth || height != oldheight) && width > 0 && height > 0) {
                _bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                _canvas = new NonClippableCanvas(_bitmap);
            }
        }
        super.onSizeChanged(width, height, oldwidth, oldheight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU && !isSingleLine) {
            if (_canvas != null) {
                _canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                super.onDraw(_canvas);
            } else
                super.onDraw(canvas);

            if (_bitmap != null)
                canvas.drawBitmap(_bitmap, 0, -0, null);
        } else {
            super.onDraw(canvas);
        }
    }

    public class NonClippableCanvas extends Canvas {

        NonClippableCanvas(@NonNull Bitmap bitmap) {
            super(bitmap);
        }

        @Override
        public boolean clipRect(float left, float top, float right, float bottom) {
            return true;
        }
    }


    public Typeface geCustomTypeFace() {
        return Typeface.DEFAULT;
    }

    @Override
    public void setTextSize(float size) {
        textSizeEn = textSizeHi = textSizeUr = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getResources().getDisplayMetrics());
        initialize();
    }

    @Override
    public void setTextSize(int unit, float size) {
        textSizeEn = textSizeHi = textSizeUr = TypedValue.applyDimension(unit, size, getResources().getDisplayMetrics());
        initialize();
    }

    public void setTextSizeEn(float textSizeEn) {
        this.textSizeEn = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeEn, getResources().getDisplayMetrics());
        initialize();
    }

    public void setTextSizeHi(float textSizeHi) {
        this.textSizeHi = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeHi, getResources().getDisplayMetrics());
        initialize();
    }

    public void setTextSizeUr(float textSizeUr) {
        this.textSizeUr = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeUr, getResources().getDisplayMetrics());
        initialize();
    }

    public Resources getResources() {
        Context c = getContext();
        Resources r;
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        return r;
    }


    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        int dp_1 = Math.round(Utils.pxFromDp(1));
//        this.original_padding_left = isItalic ? Math.max(dp_1, left) : left;
//        this.original_padding_right = isItalic ? Math.max(dp_1, right) : right;
//        this.original_padding_bottom = bottom;
//        this.original_padding_top = top;
        textPaddingStartEn = textPaddingStartHi = textPaddingStartUr = left;
        textPaddingEndEn = textPaddingEndHi = textPaddingEndUr = right;
        textPaddingTopEn = textPaddingTopHi = textPaddingTopUr = top;
        textPaddingBottomEn = textPaddingBottomHi = textPaddingBottomUr = bottom;
        initialize();
    }

    public boolean isTextStyleNeeded() {
        return isTextStyleNeeded;
    }

    public void setTextStyleNeeded(boolean textStyleNeeded) {
        isTextStyleNeeded = textStyleNeeded;
    }

    private void log(Object data) {
        Log.i("IMRKJ", data.toString());
    }

}
