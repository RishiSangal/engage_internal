package com.example.sew.adapters;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> implements ICommonValues {
    public final int ITEM_VIEW_CONTENT = 101;
    public final int ITEM_VIEW_SEE_MORE = 999;
    private BaseActivity activity;

    public BaseActivity getActivity() {
        return this.activity;
    }

    public BaseRecyclerAdapter(BaseActivity activity) {
        this.activity = activity;
    }

    public String getString(int resId) {
        return getActivity().getString(resId);
    }

    public int getColor(int colorId) {
        return getActivity().getResources().getColor(colorId);
    }

    public final View getInflatedView(int layoutResId) {
        return getInflatedView(layoutResId, null);
    }

    public final View getInflatedView(int layoutResId, ViewGroup parent) {
        return getActivity().getLayoutInflater().inflate(layoutResId, parent, false);
    }

    final void updateFavoriteIcon(ImageView imgFavIcon, String contentId) {
        if (imgFavIcon == null || TextUtils.isEmpty(contentId))
            return;
        if (MyService.isFavorite(contentId)) {
            imgFavIcon.setImageResource(R.drawable.ic_favorited);
            imgFavIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        } else {
            imgFavIcon.setImageResource(R.drawable.ic_favorite);
            imgFavIcon.setColorFilter(MyHelper.getAppIconColor(getActivity()), PorterDuff.Mode.SRC_IN);

        }
    }

    final void addFavoriteClick(ImageView imgFavIcon, String contentId, String favType) {
        if (getActivity() == null || imgFavIcon == null || TextUtils.isEmpty(contentId))
            return;
        getActivity().addFavoriteClick(imgFavIcon, contentId, favType, null);
    }

    final void addFavoriteClick(ImageView imgFavIcon, String contentId, String favType, View.OnClickListener onClickListener) {
        if (getActivity() == null || imgFavIcon == null || TextUtils.isEmpty(contentId))
            return;
        getActivity().addFavoriteClick(imgFavIcon, contentId, favType, onClickListener);
    }
    public static Toast toast;
    @SuppressLint("ShowToast")
    public static void showToast(final String message) {
        if (TextUtils.isEmpty(message))
            return;
        new Handler(Looper.getMainLooper()).post(() -> {
            if (toast == null) {
                toast = Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT);
            }
            toast.setText(message);
            toast.show();
        });
    }
}
