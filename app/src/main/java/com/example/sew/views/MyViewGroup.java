package com.example.sew.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.view.MotionEventCompat;

import com.example.sew.activities.RenderContentActivity;

public class MyViewGroup extends LinearLayout {

    private int mTouchSlop;
    private boolean mIsScrolling;
    private float mDownX;
    private boolean mShown;

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyViewGroup(Context context) {
        super(context);
        init();
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            if (!mIsScrolling&&action == MotionEvent.ACTION_UP)
                Toast.makeText(getContext(), "This is a click event", Toast.LENGTH_SHORT).show();
            mIsScrolling = false;
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mShown = false;
                mDownX = ev.getRawX();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mIsScrolling) {
                    return true;
                }

                final int xDiff = calculateDistanceX(ev);
                if (xDiff > mTouchSlop) {
                    mIsScrolling = true;
                    return true;
                }
                break;
        }

        return false;
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (!mShown) {
                    mShown = true;
                    Toast.makeText(getContext(), "Moving intercepted in the parent", Toast.LENGTH_SHORT).show();
                }
                //((RenderContentActivity) getContext()).zoomLayout.onTouchEvent(event);
                return true;
        }

        return true;
    }

    private int calculateDistanceX(MotionEvent ev) {
        return (int) (ev.getRawX() - mDownX);
    }
}
