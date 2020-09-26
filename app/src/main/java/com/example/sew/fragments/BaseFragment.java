package com.example.sew.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.LoginActivity;
import com.example.sew.apis.GetFavoriteListWithPaging;
import com.example.sew.apis.PostMarkFavorite;
import com.example.sew.apis.PostRemoveFavorite;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.SLog;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BaseFragment extends Fragment implements ICommonValues {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onLanguageChanged();
            }
        }, BROADCAST_LANGUAGE_CHANGED);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onFavoriteUpdated();
            }
        }, BROADCAST_FAVORITE_UPDATED);
    }

    final View getInflatedView(int layoutResId) {
        return GetActivity().getLayoutInflater().inflate(layoutResId, null);
    }
//    private void setLayoutDirection() {
//        if (getView() == null)
//            return;
//        switch (Utils.getSelectedLanguage()) {
//            case ENGLISH:
//            case HINDI:
//                getView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                break;
//            case URDU:
//                getView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                break;
//        }
//    }

    public BaseActivity GetActivity() {
        return (BaseActivity) getActivity();
    }

    public final void showDialog() {
        GetActivity().showDialog();
    }

    public final void dismissDialog() {
        try {
            GetActivity().dismissDialog();
        } catch (Exception ignored) {
        }
    }

    public final void showToast(String message) {
        GetActivity().showToast(message);
    }

    public final String getEditTextData(EditText editText) {
        if (editText == null || GetActivity() == null)
            return "";
        return GetActivity().getEditTextData(editText);
    }

    ArrayList<BroadcastReceiver> receivers = new ArrayList<>();

    public void registerBroadcastListener(BroadcastReceiver receiver, String... action) {
        if (receiver == null || action == null)
            return;
        receivers.add(receiver);
        for (String currAction : action)
            LocalBroadcastManager.getInstance(GetActivity()).registerReceiver(receiver, new IntentFilter(currAction));
    }

    public void unRegisterBroadCastListner(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(GetActivity()).unregisterReceiver(receiver);
    }

    public void unregisterAllBroadCastListener() {
        if (receivers == null || receivers.size() == 0)
            return;
        for (BroadcastReceiver receiver : receivers)
            unRegisterBroadCastListner(receiver);
    }

    public void hideViewsWhenKeyboardOpen(View... views) {
        this.views = views;
        try {
            KeyboardVisibilityEvent.setEventListener(
                    getActivity(),
                    new KeyboardVisibilityEventListener() {
                        @Override
                        public void onVisibilityChanged(boolean isOpen) {
                            // some code depending on keyboard visiblity status
                            if (views != null && views.length > 0) {
                                for (View currView : views)
                                    if (currView != null)
                                        currView.setVisibility(isOpen ? View.GONE : View.VISIBLE);
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    View[] views;

    public void hideViewsWhenKeyboardOpen(int i, View... views) {
        this.views = views;
        try {
            KeyboardVisibilityEvent.setEventListener(
                    getActivity(),
                    new KeyboardVisibilityEventListener() {
                        @Override
                        public void onVisibilityChanged(boolean isOpen) {
                            // some code depending on keyboard visiblity status
                            if (views != null && views.length > 0) {
                                for (View currView : views)
                                    if (currView != null)
                                        currView.setVisibility(isOpen ? View.GONE : View.VISIBLE);
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    public boolean isDestroyed;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
        unregisterAllBroadCastListener();
    }

    public final void hideKeyBoard() {
        if (GetActivity() != null)
            GetActivity().hideKeyBoard();
    }

    public void onLanguageChanged() {
    }

    public void onFavoriteUpdated() {
    }

    @Override
    public void onResume() {
        super.onResume();
        SLog.i("Current_Fragment:", "(" + getClass().getSimpleName() + ".java:0)");
    }

    public final void addFavoriteClick(ImageView imgFavIcon, String contentId, String favType) {
        addFavoriteClick(imgFavIcon, contentId, null, favType);
    }

    public final void addFavoriteClick(ImageView imgFavIcon, String contentId, String contentTypeId, String favType) {
        if (imgFavIcon == null || TextUtils.isEmpty(contentId))
            return;
        imgFavIcon.setTag(R.id.tag_content_id, contentId);
//        imgFavIcon.setTag(R.id.tag_content_type_id, contentTypeId);
        imgFavIcon.setOnClickListener(v -> {
            if (!MyService.isUserLogin()) {
                showToast("Please login");
                startActivity(LoginActivity.getInstance(getActivity()));
                return;
            }
            String content_id = v.getTag(R.id.tag_content_id).toString();
//            String content_type_id = v.getTag(R.id.tag_content_type_id).toString();
            if (MyService.isFavorite(content_id))
                new PostRemoveFavorite().setFavType(favType)
                        .setContentId(contentId)
                        .runAsync(null);
            else
                new PostMarkFavorite().setFavType(favType)
                        .setContentId(contentId)
                        .runAsync(null);
        });
    }


    public final void updateFavoriteIcon(ImageView imgFavIcon, String contentId) {
        if (imgFavIcon == null || TextUtils.isEmpty(contentId))
            return;
        if (MyService.isFavorite(contentId)) {
            imgFavIcon.setImageResource(R.drawable.ic_favorited);
            imgFavIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        } else {
            imgFavIcon.setImageResource(R.drawable.ic_favorite);
            imgFavIcon.setColorFilter(getAppIconColor(), PorterDuff.Mode.SRC_IN);

        }
    }

    public final void updateFavoriteIcon(ImageView imgFavIcon, String contentId, int color) {
        if (imgFavIcon == null || TextUtils.isEmpty(contentId))
            return;
        if (MyService.isFavorite(contentId)) {
            imgFavIcon.setImageResource(R.drawable.ic_favorited);
            imgFavIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        } else {
            imgFavIcon.setImageResource(R.drawable.ic_favorite);
            imgFavIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);

        }
    }

    public int getPrimaryTextColor() {
        return getColorFromAttribute(R.attr.primaryTextColor);
    }

    public int getAppBackgroundColor() {
        return getColorFromAttribute(R.attr.appBackgroundColor);
    }

    public int getDarkGreyTextColor() {
        return getColorFromAttribute(R.attr.darkGreyTextColor);
    }

    public int getAppIconColor() {
        return getColorFromAttribute(R.attr.appIconColor);
    }

    private int getColorFromAttribute(int attId) {
        if (getActivity() == null)
            return Color.WHITE;
        TypedValue a = new TypedValue();
        getActivity().getTheme().resolveAttribute(attId, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return a.data;
        } else {
            return Color.WHITE;
        }
    }

    public void setSelectableItemForeground(View view) {
        GetActivity().setSelectableItemForeground(view);
    }

    public void setSelectableItemForeground(View... views) {
        GetActivity().setSelectableItemForeground(views);
    }
}
