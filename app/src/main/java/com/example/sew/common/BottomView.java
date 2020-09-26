package com.example.sew.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.example.sew.R;

public class BottomView extends LinearLayout {

    public BottomView(Context context) {
        super(context);
        init();
    }

    public BottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    LinearLayout bottomView;
    private void init() {
        View view = inflate(getContext(), R.layout.cell_list_bottom_view, this);
        bottomView= view.findViewById(R.id.bottom_view);

    }



}