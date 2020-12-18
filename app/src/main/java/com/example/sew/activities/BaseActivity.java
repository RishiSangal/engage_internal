package com.example.sew.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetAllFavoriteListWithPagingV5;
import com.example.sew.apis.GetAllfavoriteId;
import com.example.sew.apis.GetFavoriteListWithPaging;
import com.example.sew.apis.PostMarkFavorite;
import com.example.sew.apis.PostRemoveFavorite;
import com.example.sew.common.ActivityManager;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.common.MyConstants;
import com.example.sew.common.PopupWindow;
import com.example.sew.common.RelativePopupWindow;
import com.example.sew.fragments.VideoFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.SLog;
import com.example.sew.views.CustomProgressBar;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.Vector;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

public abstract class BaseActivity extends AppCompatActivity implements ICommonValues {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Token", FirebaseInstanceId.getInstance().getToken("146085015376", "FCM"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Token", e.getLocalizedMessage());
                }
            }
        }).start();
        if (getActivity() instanceof HomeActivity ||
                getActivity() instanceof PoetryActivity ||
                getActivity() instanceof MoreActivity ||
                getActivity() instanceof FavoriteActivity ||
                getActivity() instanceof SearchActivity)
            ActivityManager.getInstance().clearStack();
        ActivityManager.getInstance().onCreate(this);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setLayoutDirection();
                onLanguageChanged();
            }
        }, BROADCAST_LANGUAGE_CHANGED);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onFavoriteUpdated();
            }
        }, BROADCAST_FAVORITE_UPDATED);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                if (!new ServiceManager(getActivity()).isNetworkAvailable() && !(getActivity() instanceof ServerErrorActivity))
//                   finish();
            }
        }, BROADCAST_NETWORK_CHANGED);

        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!CollectionUtils.isEmpty(ActivityManager.getInstance().activities)) {
                    ArrayList<Activity> allRecentActivity = ActivityManager.getInstance().activities;
                    int size = allRecentActivity.size() - 1;
                    allRecentActivity.get(size).recreate();
                }


            }
        }, BROADCAST_INTERNET_RESTORE);

        setLayoutDirection();
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        logScreenOpen();
    }


    private void setLayoutDirection() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
            case HINDI:
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                break;
            case URDU:
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                break;
        }
    }

    public static void sendBroadCast(String broadcastAction, Intent in) {
        in.setAction(broadcastAction);
        sendBroadCast(in);
    }

    public static void sendBroadCast(Intent in) {
        LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(in);
    }

    public static void sendBroadCast(String action) {
        Intent in = new Intent();
        in.setAction(action);
        sendBroadCast(in);
    }

    ArrayList<BroadcastReceiver> receivers = new ArrayList<>();

    public void registerBroadcastListener(BroadcastReceiver receiver, String... action) {
        if (receiver == null || action == null)
            return;
        receivers.add(receiver);
        for (String currAction : action)
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(currAction));
    }

    public static void registerBroadcastListener(BroadcastReceiver receiver, Context context, String... action) {
        if (receiver == null || action == null)
            return;
        for (String currAction : action)
            LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(currAction));
    }

    public static void unRegisterBroadCastListener(BroadcastReceiver receiver, Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public void unRegisterBroadCastListner(BroadcastReceiver receiver) {
//        if (receiver != null && receivers != null && receivers.contains(receiver))
//            receivers.remove(receiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    public void unregisterAllBroadCastListener() {
        if (receivers == null || receivers.size() == 0)
            return;
        for (BroadcastReceiver receiver : receivers)
            unRegisterBroadCastListner(receiver);
    }

    @Override
    protected void onDestroy() {
        unregisterAllBroadCastListener();
        ActivityManager.getInstance().onDestroy(this);
        dismissDialog();
        super.onDestroy();
    }

    public final BaseActivity getActivity() {
        return this;
    }


//    private void initializeHeaderType1() {
//    }

    public final void showPopup(View view) {
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
            new PopupWindow(getActivity()).showOnAnchor(view, RelativePopupWindow.VerticalPosition.ALIGN_TOP, RelativePopupWindow.HorizontalPosition.LEFT, true);
        else
            new PopupWindow(getActivity()).showOnAnchor(view, RelativePopupWindow.VerticalPosition.ALIGN_TOP, RelativePopupWindow.HorizontalPosition.RIGHT, true);
    }

    @Nullable
    @BindView(R.id.txtHeaderTitle)
    TextView txtHeaderTitle;
    @Nullable
    @BindView(R.id.imgHeaderMenu)
    ImageView imgHeaderMenu;

    @Nullable
    @BindView(R.id.imgBack)
    public ImageView imgBack;

    @Optional
    @OnClick(R.id.imgHeaderMenu)
    void onHeaderMenuClick() {
        showPopup(imgHeaderMenu);
    }

    @Optional
    @OnClick(R.id.imgHeaderSearch)
    void onSearchClick() {
        startActivity(SearchActivity.getInstance(getActivity(), null));
    }

    @Optional
    @OnClick(R.id.imgHeaderLogo)
    void onLogoClick() {
        startActivity(HomeActivity.getInstance(getActivity(), false));
    }

    @Optional
    @OnClick(R.id.imgBack)
    void onBackClicked() {
        if (imgBack != null && imgBack.getVisibility() == View.VISIBLE)
            onBackPressed();
    }

    public final void setHeaderTitle(String headerTitle) {
        if (txtHeaderTitle != null)
            txtHeaderTitle.setText(headerTitle);
    }

    public final void lazyRefreshTabPositioning(SmartTabLayout tabLayout, ViewPager viewpager) {
        if (tabLayout == null || viewpager == null)
            return;
        new Handler().postDelayed(() -> {
            int currentItem = viewpager.getCurrentItem();
            if (viewpager.getChildCount() > 1) {
                if ((viewpager.getChildCount() - 1) > currentItem)
                    viewpager.setCurrentItem(currentItem + 1);
                else
                    viewpager.setCurrentItem(currentItem - 1);
            }
            viewpager.setCurrentItem(currentItem);
            tabLayout.setViewPager(viewpager);
        }, 300);
    }

    public static Toast toast;

    public final String getEditTextData(EditText editText) {
        if (editText == null)
            return "";
        return editText.getText().toString().trim();
    }

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

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        this.overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        ActivityManager.getInstance().onResume(this);
        SLog.i("Current_Activity:", "(" + getClass().getSimpleName() + ".java:0)");
        if (!(this instanceof SplashActivity))
            MyConstants.initIfRequired();
        super.onResume();
    }

    @Override
    protected void onPause() {
        ActivityManager.getInstance().onPause(this);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        ActivityManager.getInstance().onNewIntent(this, intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onLanguageChanged() {
    }

    public void onFavoriteUpdated() {
    }

    Vector<CustomProgressBar> progress = new Vector<>();

    public void showDialog(String message, boolean canCancelled) {
        if (progress.size() > 0)
            return;
        try {
            CustomProgressBar progressBar = new CustomProgressBar();
            progressBar.show(this);
//            ProgressDialog dialog = new ProgressDialog(this);
//            dialog.setMessage(message);
//            dialog.setCancelable(false);
//            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            dialog.show();
            progress.add(progressBar);
        } catch (Exception ignored) {

        }
    }

    public void dismissDialog() {
        try {
            if (progress != null)
                for (CustomProgressBar prog : progress)
                    prog.getDialog().dismiss();
            progress.clear();
        } catch (Exception ignored) {
        }
    }

    public void showDialog() {
        showDialog("Loading please wait", true);
    }

    public final void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void openKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    public final void addFavoriteClick(ImageView imgFavIcon, String contentId, String favType, View.OnClickListener onClickListener) {
        addFavoriteClick(imgFavIcon, contentId, null, favType, onClickListener);
    }

    public final void addFavoriteClick(ImageView imgFavIcon, String contentId, String favType) {
        addFavoriteClick(imgFavIcon, contentId, null, favType, null);
    }

    public final void addFavoriteClick(ImageView imgFavIcon, String contentId, String contentTypeId,
                                       String favType, View.OnClickListener onClickListener) {
        if (imgFavIcon == null || TextUtils.isEmpty(contentId))
            return;
        imgFavIcon.setTag(R.id.tag_content_id, contentId);
//        imgFavIcon.setTag(R.id.tag_content_type_id, contentTypeId);
        imgFavIcon.setOnClickListener(v -> {
            if (onClickListener != null)
                onClickListener.onClick(v);
            if (!MyService.isUserLogin()) {
                showToast("Please login");
                startActivity(LoginActivity.getInstance(getActivity()));
                return;
            }
            String content_id = v.getTag(R.id.tag_content_id).toString();
//            String content_type_id = v.getTag(R.id.tag_content_type_id).toString();
            if (MyService.isFavorite(content_id))
                new PostRemoveFavorite()
                        .setContentId(contentId).setFavType(favType)
                        .runAsync(null);
            else
                new PostMarkFavorite()
                        .setContentId(contentId).setFavType(favType)
                        .runAsync(null);
        });
    }

  /*  public void getAllFavoriteIdApiCall(){
        new GetAllfavoriteId().runAsync((BaseServiceable.OnApiFinishListener<GetAllfavoriteId>) getAllfavoriteId ->{
            if(getAllfavoriteId.isValidResponse()){
                ArrayList<String> favIds= getAllfavoriteId.getAllFavoriteIds();


            }
        });
    }*/

    public final void syncFavoriteIfNecessary() {
        if (MyService.isUserLogin()) {

            new GetFavoriteListWithPaging().addPagination().runAsync(null);
            new GetAllFavoriteListWithPagingV5().setFavType(Enums.FAV_TYPES.IMAGE_SHAYRI).addPagination().runAsync(null);
            new GetAllFavoriteListWithPagingV5().setFavType(Enums.FAV_TYPES.ENTITY).addPagination().runAsync(null);
            new GetAllFavoriteListWithPagingV5().setFavType(Enums.FAV_TYPES.WORD).addPagination().runAsync(null);
            new GetAllFavoriteListWithPagingV5().setFavType(Enums.FAV_TYPES.OCCASION).addPagination().runAsync(null);
            new GetAllFavoriteListWithPagingV5().setFavType(Enums.FAV_TYPES.T20).addPagination().runAsync(null);
            new GetAllFavoriteListWithPagingV5().setFavType(Enums.FAV_TYPES.PROSE_COLLECTION).addPagination().runAsync(null);
            new GetAllFavoriteListWithPagingV5().setFavType(Enums.FAV_TYPES.SHAYARI_COLLECTION).addPagination().runAsync(null);

        }
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

    public static void shareTheUrl(String text, BaseActivity baseActivity) {
        MyHelper.shareTheText(text, baseActivity);
    }


    final View getInflatedView(int layoutResId) {
        View convertView = getActivity().getLayoutInflater().inflate(layoutResId, null);
        convertView.setTag(R.id.tag_language, MyService.getSelectedLanguage());
        return convertView;
    }

    public final void showYoutubeDialog(String videoId) {
        FragmentManager fm = getSupportFragmentManager();
        VideoFragment videoFragment = VideoFragment.getInstance(videoId);
        videoFragment.show(fm, "fragment_video");
    }

    public final int getLanguageSpecificGravity() {
        return MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? Gravity.END : Gravity.START;
    }

    private void logScreenOpen() {
        try {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            //        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "123");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getClass().getSimpleName());
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
            String formattedName = getFormattedScreenName(getClass().getSimpleName());
            SLog.d("FormattedName", formattedName);
            mFirebaseAnalytics.setCurrentScreen(this, getClass().getSimpleName(), formattedName);
        } catch (Exception ignored) {
        }
    }

    private String getFormattedScreenName(String name) {
        if (TextUtils.isEmpty(name))
            return name;
        name = name.replace("Activity", "Screen");
        String[] r = name.split("(?=\\p{Upper})");
        return TextUtils.join(" ", r);
    }

    public int getPrimaryTextColor() {
        return getColorFromAttribute(R.attr.primaryTextColor);
    }

    public int getAppBackgroundColor() {
        return getColorFromAttribute(R.attr.appBackgroundColor);
    }

    public int getPopupBackgroundColor() {
        return getColorFromAttribute(R.attr.popupBackgroundColor);
    }

    public int getDarkGreyTextColor() {
        return getColorFromAttribute(R.attr.darkGreyTextColor);
    }

    public int getAppDividerColor() {
        return getColorFromAttribute(R.attr.appDividerColor);
    }

    public int getAppIconColor() {
        return getColorFromAttribute(R.attr.appIconColor);
    }

    public int getColorFromAttribute(int attId) {
        TypedValue a = new TypedValue();
        getTheme().resolveAttribute(attId, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return a.data;
        } else {
            return Color.WHITE;
        }
    }

    public void setSelectableItemForeground(View view) {
        try {
            if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TypedValue outValue = new TypedValue();
                getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                view.setBackgroundResource(outValue.resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setSelectableItemForeground(View... views) {
        if (views == null || views.length == 0)
            return;
        for (View view : views) setSelectableItemForeground(view);
    }
}
