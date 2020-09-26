package com.example.sew.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.example.sew.R;


public class UrduTextView extends AppCompatTextView {

    private final Paint mPaint = new Paint();
    Typeface typeface;

    private final Rect mBounds = new Rect();

    public UrduTextView(Context context) {
        super(context);
        init();
    }

    public UrduTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UrduTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        typeface = ResourcesCompat.getFont(getContext(), R.font.noto_nastaliq_regular_urdu);
        mPaint.setTypeface(typeface);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        final String text = calculateTextParams();
        final int left = mBounds.left;
        final int bottom = mBounds.bottom;
        mBounds.offset(-mBounds.left, -mBounds.top);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getCurrentTextColor());
        canvas.drawText(text, -left, mBounds.bottom - bottom, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateTextParams();
//        calculateAdditionalPadding();
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            int height = measureHeight(getText().toString());
            height += getPaddingTop() + getPaddingBottom();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        setMeasuredDimension(mBounds.width() + 1, heightMeasureSpec);
    }

    private int measureHeight(String text) {
        float textSize = getTextSize();
        TextPaint paint = new TextPaint();
        paint.setTypeface(typeface);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.measureText(text);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    private String calculateTextParams() {
        final String text = getText().toString();
        final int textLength = text.length();
        mPaint.setTextSize(getTextSize());
        mPaint.getTextBounds(text, 0, textLength, mBounds);
        if (textLength == 0) {
            mBounds.right = mBounds.left;
        }
        return text;
    }

//    private void calculateAdditionalPadding() {
//        float textSize = getTextSize();
//        TextView textView = new TextView(getContext());
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//        textView.setLines(1);
//        textView.setTypeface(typeface);
//        textView.measure(0, 0);
//        int measuredHeight = textView.getMeasuredHeight();
//        if (measuredHeight - textSize > 0) {
//            mAdditionalPadding = (int) (measuredHeight - textSize);
//            Log.v("NoPaddingTextView", "onMeasure: height=" + measuredHeight + " textSize=" + textSize + " mAdditionalPadding=" + mAdditionalPadding);
//        }
//    }
}