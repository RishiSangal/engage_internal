package com.example.sew.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.sew.BuildConfig;
import com.example.sew.R;
import com.example.sew.adapters.HomePageAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetAppInfo;
import com.example.sew.apis.GetContentTypeIds;
import com.example.sew.apis.GetHomePageCollection;
import com.example.sew.apis.GetSettings;
import com.example.sew.apis.GetYouTubeKey;
import com.example.sew.apis.PostUserAppInfo;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.ThemeHelper;
import com.example.sew.models.AppVersion;
import com.example.sew.models.HomeLookingMore;
import com.example.sew.models.PushNotification;
import com.example.sew.models.ShayariImage;
import com.google.android.gms.common.util.CollectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class HomeActivity extends BaseHomeActivity {
    @BindView(R.id.lstHomeCollection)
    ListView lstHomeCollection;
    @BindView(R.id.layDarkModeAlert)
    LinearLayout layDarkModeAlert;
    @BindView(R.id.txtTurnOn)
    TextView txtTurnOn;
    @BindView(R.id.txtDissmiss)
    TextView txtDissmiss;
    boolean isScrolledWordOfDay;
    int scrolledPosition = -1;
    String youTubeKey = "";
    Animation slideUpAnimation, slideDownAnimation;

    @OnItemClick(R.id.lstHomeCollection)
    void onListItemClick(View convertView) {
        if (convertView.getTag(R.id.tag_data) instanceof HomeLookingMore) {
            HomeLookingMore lookingMore = (HomeLookingMore) convertView.getTag(R.id.tag_data);
            startActivity(RenderContentActivity.getInstance(getActivity(), lookingMore.getId()));
        }
    }

    public static Intent getInstance(Activity activity, boolean isScrolledWordOfDay) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra(IS_SCROLLED_WORD_OF_DAY, isScrolledWordOfDay);
        return intent;
    }

    public static Intent getInstance(Activity activity, PushNotification pushNotification) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra(PUSH_NOTIFICATION_OBJ, pushNotification.getJsonObject().toString());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
        if (MyService.isFirstTimeOpenApp()) {
            layDarkModeAlert.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layDarkModeAlert.setVisibility(View.VISIBLE);
                    layDarkModeAlert.startAnimation(slideUpAnimation);
                    MyService.setFirstTimeOpenApp(false);
                }
            }, 5000);

        } else
            layDarkModeAlert.setVisibility(View.GONE);
        if (getIntent().hasExtra(PUSH_NOTIFICATION_OBJ)) {
            PushNotification pushNotification = null;
            try {
                pushNotification = new PushNotification(new JSONObject(getIntent().getStringExtra(PUSH_NOTIFICATION_OBJ)));
            } catch (JSONException e) {
                pushNotification = new PushNotification(null);
            }
            getPoetInfoAndLoadSpecificPage(pushNotification);
        }
        isScrolledWordOfDay = getIntent().getBooleanExtra(IS_SCROLLED_WORD_OF_DAY, false);
        initBottomNavigation(Enums.BOTTOM_TYPE.HOME_1);
        if (TextUtils.isEmpty(MyService.getYouTubeKey()))
            getYouTubeKeyApiCall();
        getContentTypeId();
        if(MyService.isUserLogin())
            getAllFavoriteIdApiCall();
        getHomePageCollection = new GetHomePageCollection();
        getHomePageCollections();
        if (MyService.isUserLogin())
            new GetSettings().runAsync(null);
        if (!BuildConfig.VERSION_NAME.equalsIgnoreCase(MyService.getLastAppVersion()))
            new PostUserAppInfo().runAsync(null);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_list_bottom_view, null, false);
        lstHomeCollection.addFooterView(footerView);

        checkForVersionUpdate();
    }

    private void getYouTubeKeyApiCall() {
        new GetYouTubeKey().runAsync((BaseServiceable.OnApiFinishListener<GetYouTubeKey>) getYouTubeKey -> {
            if (getYouTubeKey.isValidResponse()) {
                MyService.setYouTubeKey(getYouTubeKey.getYouTubeKey());
            } else
                showToast(getYouTubeKey.getYouTubeKey());
        });

    }

    GetHomePageCollection getHomePageCollection;
    private void getContentTypeId(){
        new GetContentTypeIds().runAsync(null);
    }

    private void getHomePageCollections() {
        showDialog();
        if (getHomePageCollection == null)
            getHomePageCollection = new GetHomePageCollection();
        getHomePageCollection.setCommonParams();
        getHomePageCollection.runAsync((BaseServiceable.OnApiFinishListener<GetHomePageCollection>) getHomePageCollection -> {
            dismissDialog();
            if (getHomePageCollection.isValidResponse())
                updateUI();
            else
                showToast(getHomePageCollection.getErrorMessage());
        });
    }

    @OnClick(R.id.txtDissmiss)
    void onClickDarkModeAlert() {
        MyService.setFirstTimeOpenApp(false);
        layDarkModeAlert.setVisibility(View.GONE);
    }

    @OnClick(R.id.txtTurnOn)
    void onClickTurnOn() {
        MyService.setFirstTimeOpenApp(false);
        MyService.setDarkModePref(ThemeHelper.DARK_MODE);
        ThemeHelper.applyTheme(MyService.getDarkModePref());
        MyService.setDarkTheme(DARK_MODE_YES);
        layDarkModeAlert.setVisibility(View.GONE);
//        ActivityManager.getInstance().reCreateCall();
    }

    private void getPoetInfoAndLoadSpecificPage(PushNotification pushNotification) {
        if (!TextUtils.isEmpty(pushNotification.getImageShayari().getContentId())) {
            ShayariImage shayariImage = new ShayariImage(pushNotification.getImageShayari().getJsonObject());
            shayariImage.updatePushShayariImage(pushNotification);
            startActivity(ShayariImageDetailActivity.getInstance(getActivity(), shayariImage));
        } else if (!TextUtils.isEmpty(pushNotification.getRemembering().getPoetId())) {
            startActivity(PoetDetailActivity.getInstance(getActivity(), pushNotification.getRemembering().getPoetId()));
        } else if (!TextUtils.isEmpty(pushNotification.getSherOfTheDay().getContentId())) {
            startActivity(RenderContentActivity.getInstance(getActivity(), pushNotification.getSherOfTheDay().getContentId()));
        } else if (!TextUtils.isEmpty(pushNotification.getWordOfTheDay().getDictionaryId())) {
            startActivity(HomeActivity.getInstance(getActivity(), true));
        } else if (!TextUtils.isEmpty(pushNotification.getEvent().getImageUrl())) {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(pushNotification.getEvent().getTargetUrl())));
        } else if (!TextUtils.isEmpty(pushNotification.getGeneral().getTitle())) {
            if (pushNotification.getGeneral().isExternal())
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(pushNotification.getGeneral().getTargetUrl())));
            else
                startActivity(HomeActivity.getInstance(getActivity(), false));
        }
    }

    private ArrayList<Object> data = new ArrayList<>();

    private void updateUI() {
        if (getHomePageCollection == null)
            return;
        data.clear();
        ArrayList<Integer> viewTypes = new ArrayList<>();
        viewTypes.clear();
        if (!CollectionUtils.isEmpty(getHomePageCollection.getBannerCollections()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_CAROUSELS);
        if (!CollectionUtils.isEmpty(getHomePageCollection.getPromotionalBanners()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_PROMOTION);
        if (!CollectionUtils.isEmpty(getHomePageCollection.getTodayTops()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_TODAYS_TOP);
        if (!CollectionUtils.isEmpty(getHomePageCollection.getTopPoets()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_TOP_POETS);
        if (getHomePageCollection.getWordOfTheDay() != null && !TextUtils.isEmpty(getHomePageCollection.getWordOfTheDay().getWord_En())) {
            viewTypes.add(HomePageAdapter.VIEW_TYPE_WORD_OF_THE_DAY);
            scrolledPosition = viewTypes.size() - 1;
        }
        if (!CollectionUtils.isEmpty(getHomePageCollection.getSherCollections()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_SHER_COLLECTION);
        if (!CollectionUtils.isEmpty(getHomePageCollection.getFeatureds()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_FEATURED);
        if (getHomePageCollection.getDidYouKnow() != null && !TextUtils.isEmpty(getHomePageCollection.getDidYouKnow().getId()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_DID_YOU_KNOW);
        if (!CollectionUtils.isEmpty(getHomePageCollection.getShayaris()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_SHAYARI_COLLECTION);
        if (!CollectionUtils.isEmpty(getHomePageCollection.getShayariImages()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_IMG_SHAYARI);
        if (getHomePageCollection.getShayariImages().size() > 1)
            viewTypes.add(HomePageAdapter.VIEW_TYPE_OTHER_IMG_SHAYARI);
        if (!TextUtils.isEmpty(getHomePageCollection.getVideo().getYoutube_Id()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_VIDEO);
        if (!CollectionUtils.isEmpty(getHomePageCollection.getVideos()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_MORE_VIDEOS);
        if(getHomePageCollection.getBlogs() != null && !TextUtils.isEmpty(getHomePageCollection.getBlogs().getId()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_BLOGS);
        if (!CollectionUtils.isEmpty(getHomePageCollection.getProseCollections()))
            viewTypes.add(HomePageAdapter.VIEW_TYPE_PROSE_COLLECTION);
        // viewTypes.add(HomePageAdapter.VIEW_TYPE_SUPPORT_REKHTA);
        data.addAll(viewTypes);
        data.addAll(getHomePageCollection.getLookingMores());
        lstHomeCollection.setAdapter(new HomePageAdapter(getActivity(), getHomePageCollection, data));
        if (isScrolledWordOfDay)
            lstHomeCollection.setSelection(scrolledPosition);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        getHomePageCollections();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkForVersionUpdate();
    }

    void checkForVersionUpdate() {
        new GetAppInfo()
                .runAsync((BaseServiceable.OnApiFinishListener<GetAppInfo>) getAppInfo -> {
                    if (getAppInfo.isValidResponse()) {
                        AppVersion appVersion = getAppInfo.getAppVersion();
                        if (appVersion != null) {
                            boolean shouldShowUpdatePopup = (!TextUtils.isEmpty(appVersion.getCurrentVersion())) && !BuildConfig.VERSION_NAME.equalsIgnoreCase(appVersion.getCurrentVersion());
                            if (shouldShowUpdatePopup &&
                                    (MyService.isLastUpdateDialogShowTimeExpired() ||
                                            !appVersion.getCurrentVersion().equalsIgnoreCase(MyService.getLastSkippedVersion()))) {
                                MyService.setLastUpdateDialogShowTime(System.currentTimeMillis());
                                if (!HomeActivity.this.isFinishing()) {
                                    try {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                                .setTitle(appVersion.getPopupText())
                                                .setMessage(appVersion.getReleaseDescription())
                                                .setCancelable(false)
                                                .setNegativeButton("Update", (dialog, which) -> {
                                                            final String appPackageName = getPackageName();
                                                            try {
                                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("market://details?id=%s", appPackageName))));
                                                            } catch (android.content.ActivityNotFoundException unsafe) {
                                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://play.google.com/store/apps/details?id=%s", appPackageName))));
                                                            }
                                                            finish();
                                                        }
                                                );
                                        if (appVersion.getUpdateType() == AppVersion.SOFT)
                                            builder.setPositiveButton("Not Now", (dialog, which) -> MyService.setLastSkippedVersion(appVersion.getCurrentVersion()));
                                        if (appVersion.getUpdateType() == AppVersion.FORCE)
                                            builder.setPositiveButton("Cancel", (dialog, which) -> finish());
                                        builder.create().show();
                                    } catch (WindowManager.BadTokenException e) {
                                        Log.e("WindowManagerBad ", e.toString());
                                    }
                                }
                            }
                        }
                    }
                });
    }
}
