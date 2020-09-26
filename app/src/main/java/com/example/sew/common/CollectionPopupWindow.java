package com.example.sew.common;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionPopupWindow extends RelativePopupWindow {
    public CollectionPopupWindow(Activity context) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.menu_collections, null);
        new ViewHolder(convertView, this);
        setContentView(convertView);
//        setWidth(getDesiredWidth(context));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(40);
        }
    }

    public int getDesiredWidth(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width / 2 + 150;
    }

    static class ViewHolder {
        private final RelativePopupWindow popupWindow;
        @BindView(R.id.imgFavorite)
        ImageView imgFavorite;
        @BindView(R.id.imgShare)
        ImageView imgShare;
        @BindView(R.id.popUp)
        LinearLayout popUp;

        @OnClick(R.id.imgShare)
        public void onShareClicked() {

        }

        @OnClick(R.id.imgFavorite)
        public void onFavoriteClicked() {
        }

        ViewHolder(View view, RelativePopupWindow popupWindow) {
            ButterKnife.bind(this, view);
            this.popupWindow = popupWindow;
        }
    }


}
