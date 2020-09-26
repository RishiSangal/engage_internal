package com.example.sew.common;

/**
 * Created by Raman Kumar on 29-07-2017.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sew.R;
import com.wang.avi.AVLoadingIndicatorView;


public class LoadingView extends LinearLayout {

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    AVLoadingIndicatorView avLoadingIndicatorView;

    private void init() {
        View view = inflate(getContext(), R.layout.cell_list_loading, this);
        avLoadingIndicatorView = view.findViewById(R.id.aviLoading);
        startAnim();
    }

    void startAnim() {
        avLoadingIndicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }

}