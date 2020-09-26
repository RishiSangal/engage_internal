package com.example.sew.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.example.sew.R;

public class IconTextView extends AppCompatTextView {

    private Context context;

    public IconTextView(Context context) {
        super(context);
        this.context = context;
        createView();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView();
    }

    private void createView() {
        setGravity(Gravity.CENTER);
        setTypeface(ResourcesCompat.getFont(getContext(), R.font.r_icons));
    }
}
