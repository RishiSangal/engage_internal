package com.example.sew.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.example.sew.R;
import com.example.sew.MyApplication;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import static com.example.sew.common.ICommonValues.BROADCAST_LANGUAGE_CHANGED;

public class CommonImageView extends AppCompatImageView {
    public CommonImageView(Context context) {
        super(context);
    }

    public CommonImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeStyleParameters(attrs);
    }

    public CommonImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeStyleParameters(attrs);
    }

    boolean rotateForRtl;

    private void initializeStyleParameters(AttributeSet attrs) {
        TypedArray t = MyApplication.getContext().obtainStyledAttributes(attrs, R.styleable.CommonImageView, 0, 0);
        rotateForRtl = t.getBoolean(R.styleable.CommonImageView_rotateForRtl, false);
        t.recycle();
        doRotationIfNecessary();
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
                doRotationIfNecessary();
            }
        };
        BaseActivity.registerBroadcastListener(broadcastReceiver, MyApplication.getContext(), BROADCAST_LANGUAGE_CHANGED);
    }

    private void doRotationIfNecessary() {
        if (rotateForRtl)
            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                setRotation(180);
            else
                setRotation(0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BaseActivity.unRegisterBroadCastListener(broadcastReceiver, MyApplication.getContext());
    }

}
