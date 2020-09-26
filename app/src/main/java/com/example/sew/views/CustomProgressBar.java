package com.example.sew.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.sew.R;

public final class CustomProgressBar {

    private Dialog dialog;

    public Dialog show(Context context) {
        return show(context, null);
    }

    public Dialog show(Context context, CharSequence title) {
        return show(context, title, false);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable) {
        return show(context, title, cancelable, null);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable,
                       Dialog.OnCancelListener cancelListener) {
        LayoutInflater inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflator.inflate(R.layout.progress_bar, null);
        dialog = new Dialog(context, R.style.NewDialog);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        if (dialog.getWindow() != null)
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        return dialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

}
