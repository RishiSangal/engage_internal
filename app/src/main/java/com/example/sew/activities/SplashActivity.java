package com.example.sew.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sew.BuildConfig;
import com.example.sew.R;
import com.example.sew.MyApplication;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetAllFavoriteListWithPagingV5;
import com.example.sew.apis.GetAppConfig;
import com.example.sew.apis.GetAppInfo;
import com.example.sew.apis.GetFavoriteListWithPaging;
import com.example.sew.apis.GetHomePageCollection;
import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.ServiceManager;
import com.example.sew.models.AppVersion;
import com.example.sew.models.PushNotification;
import com.example.sew.models.RenderContent;
import com.example.sew.models.ShayariImage;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.splashRlogo)
    ImageView splashRlogo;
    @BindView(R.id.imgWelcomeText)
    ImageView imgWelcomeText;
    @BindView(R.id.text_language)
    TextView textLanguage;
    @BindView(R.id.language_content)
    TextView languageContent;
    @BindView(R.id.txtMenuLanguageEn)
    TextView txtMenuLanguageEn;
    @BindView(R.id.txtMenuLanguageHin)
    TextView txtMenuLanguageHin;
    @BindView(R.id.txtMenuLanguageUr)
    TextView txtMenuLanguageUr;
    @BindView(R.id.btnContinue)
    Button btnContinue;
    @BindView(R.id.layLanguageSelection)
    LinearLayout layLanguageSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        MyService.setUniqueIdAndParamsIfNecessary();
        setSelectableItemForeground(txtMenuLanguageEn, txtMenuLanguageHin, txtMenuLanguageUr);
        layLanguageSelection.setVisibility(View.GONE);
        imgWelcomeText.setVisibility(View.GONE);
        if (BuildConfig.APP_SERVER != MyService.getAppServer()) {
            MyHelper.resetConfig();
            loadAppConfig();
        } else if (MyService.isLastConfigUpdateTimeExpired())
            loadAppConfig();
        else if (!MyHelper.isConfigLoaded())
            loadAppConfig();
        else
            proceedToNextStep();
    }

    private void loadAppConfig() {
        showDialog();
        new GetAppConfig()
                .runAsync((BaseServiceable.OnApiFinishListener<GetAppConfig>) baseServiceable -> {
                    dismissDialog();
                    if (MyHelper.isConfigLoaded())
                        proceedToNextStep();
                    else {
                        showToast("Error occurred while loading configuration, please try again after some time");
                        finish();
                    }
                });
    }

    private void proceedToNextStep() {
        MyConstants.init();
        if (getIntent().hasExtra(PUSH_NOTIFICATION_OBJ)) {
            PushNotification pushNotification = null;
            try {
                pushNotification = new PushNotification(new JSONObject(getIntent().getStringExtra(PUSH_NOTIFICATION_OBJ)));
            } catch (JSONException e) {
                pushNotification = new PushNotification(null);
            }
            startActivity(HomeActivity.getInstance(getActivity(), pushNotification));
            finish();
            // getPoetInfoAndLoadSpecificPage(pushNotification);

        } else {
//            checkForVersionUpdate();
            showHome();
            if (MyService.isUserLogin())
                new GetAllFavoriteListWithPagingV5()
                        .setFavType(Enums.FAV_TYPES.IMAGE_SHAYRI)
                        .addPagination()
                        .runAsync(null);
        }
        generateKeyHash();
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
        finish();
    }

    public static Intent getInstance(Context context, PushNotification pushNotification) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(PUSH_NOTIFICATION_OBJ, pushNotification.getJsonObject().toString());
        return intent;
    }

    private void showHomeOrLanguageSelection() {
        if (!new ServiceManager(getActivity()).isNetworkAvailable()) {
            startActivity(ServerErrorActivity.getInstance(getActivity()));
        } else if (MyService.isUserLogin()) {
            MyService.setShouldShowLanguagePopupSplash(false);
            MyApplication.getInstance().setBrowsingOffline(false);
            if (MyService.getSelectedLastLanguage() == Enums.LANGUAGE.ENGLISH) {
                newLanguage = Enums.LANGUAGE.ENGLISH;
                MyService.setSelectedLanguage(Enums.LANGUAGE.ENGLISH);
            } else if (MyService.getSelectedLastLanguage() == Enums.LANGUAGE.HINDI) {
                newLanguage = Enums.LANGUAGE.HINDI;
                MyService.setSelectedLanguage(Enums.LANGUAGE.HINDI);
            } else if (MyService.getSelectedLastLanguage() == Enums.LANGUAGE.URDU) {
                newLanguage = Enums.LANGUAGE.URDU;
                MyService.setSelectedLanguage(Enums.LANGUAGE.URDU);
            }
            startActivity(HomeActivity.getInstance(getActivity(), false));
        } else {
            MyApplication.getInstance().setBrowsingOffline(false);
            if (MyService.shouldShowLanguagePopupSplash()) {
                layLanguageSelection.setVisibility(View.VISIBLE);
                imgWelcomeText.setVisibility(View.VISIBLE);
                Animation animLangSelection = AnimationUtils.loadAnimation(this, R.anim.bottom_to_center);
                layLanguageSelection.startAnimation(animLangSelection);
                Animation fadeOut = new AlphaAnimation(0, 1);
                fadeOut.setInterpolator(new AccelerateInterpolator());
                fadeOut.setDuration(800);
                imgWelcomeText.startAnimation(fadeOut);
                onMenuLanguageEnClicked();
            } else {
                if (MyService.getSelectedLastLanguage() == Enums.LANGUAGE.ENGLISH) {
                    newLanguage = Enums.LANGUAGE.ENGLISH;
                    MyService.setSelectedLanguage(Enums.LANGUAGE.ENGLISH);
                    startActivity(LoginActivity.getInstance(getActivity()));
                    finish();
                } else if (MyService.getSelectedLastLanguage() == Enums.LANGUAGE.HINDI) {
                    newLanguage = Enums.LANGUAGE.HINDI;
                    MyService.setSelectedLanguage(Enums.LANGUAGE.HINDI);
                    startActivity(LoginActivity.getInstance(getActivity()));
                    finish();
                } else if (MyService.getSelectedLastLanguage() == Enums.LANGUAGE.URDU) {
                    newLanguage = Enums.LANGUAGE.URDU;
                    MyService.setSelectedLanguage(Enums.LANGUAGE.URDU);
                    startActivity(LoginActivity.getInstance(getActivity()));
                    finish();
                }
            }

        }
    }

    @OnClick(R.id.btnContinue)
    public void onContinueClicked() {
        MyService.setSelectedLanguage(newLanguage);
        startActivity(LoginActivity.getInstance(getActivity()));
        finish();
    }

    @OnClick(R.id.txtMenuLanguageEn)
    public void onMenuLanguageEnClicked() {
        changeLanguageIfNecessary(Enums.LANGUAGE.ENGLISH);
    }

    @OnClick(R.id.txtMenuLanguageHin)
    public void onMenuLanguageHinClicked() {
        changeLanguageIfNecessary(Enums.LANGUAGE.HINDI);
    }

    @OnClick(R.id.txtMenuLanguageUr)
    public void onMenuLanguageUrClicked() {
        changeLanguageIfNecessary(Enums.LANGUAGE.URDU);
    }

    Enums.LANGUAGE newLanguage;

    private void changeLanguageIfNecessary(Enums.LANGUAGE newLanguage) {
        if (this.newLanguage != newLanguage) {
            this.newLanguage = newLanguage;
            updateUI();
        }
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
    }

    private void updateUI() {
        txtMenuLanguageEn.setBackgroundResource(R.drawable.rounded_button_language_unselected);
        txtMenuLanguageUr.setBackgroundResource(R.drawable.rounded_button_language_unselected);
        txtMenuLanguageHin.setBackgroundResource(R.drawable.rounded_button_language_unselected);

        txtMenuLanguageEn.setTextColor(getPrimaryTextColor());
        txtMenuLanguageUr.setTextColor(getPrimaryTextColor());
        txtMenuLanguageHin.setTextColor(getPrimaryTextColor());
        if (newLanguage == null)
            newLanguage = MyService.getSelectedLanguage();
        switch (newLanguage) {
            case ENGLISH:
                txtMenuLanguageEn.setBackgroundResource(R.drawable.rounded_button_language_selected);
                txtMenuLanguageEn.setTextColor(ContextCompat.getColor(MyApplication.getContext(), R.color.white));
                break;
            case HINDI:
                txtMenuLanguageHin.setBackgroundResource(R.drawable.rounded_button_language_selected);
                txtMenuLanguageHin.setTextColor(ContextCompat.getColor(MyApplication.getContext(), R.color.white));
                break;
            case URDU:
                txtMenuLanguageUr.setBackgroundResource(R.drawable.rounded_button_language_selected);
                txtMenuLanguageUr.setTextColor(ContextCompat.getColor(MyApplication.getContext(), R.color.white));
                break;
        }
    }

    public void generateKeyHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.rekhta", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    void checkForVersionUpdate() {
        new GetAppInfo()
                .runAsync((BaseServiceable.OnApiFinishListener<GetAppInfo>) getAppInfo -> {
                    if (getAppInfo.isValidResponse()) {
                        AppVersion appVersion = getAppInfo.getAppVersion();
                        if (appVersion != null) {
                            boolean shouldShowUpdatePopup = (!TextUtils.isEmpty(appVersion.getCurrentVersion())) && !BuildConfig.VERSION_NAME.equalsIgnoreCase(appVersion.getCurrentVersion());
                            if (shouldShowUpdatePopup) {
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
                                    builder.setPositiveButton("Not Now", (dialog, which) -> showHome());
                                if (appVersion.getUpdateType() == AppVersion.FORCE)
                                    builder.setPositiveButton("Cancel", (dialog, which) -> finish());
                                builder.create().show();
                            } else {
                                showHome();
                            }
                        }
                    } else {
                        showHome();
                    }
                });
    }

    private void showHome() {
        int SPLASH_DISPLAY_LENGTH = 1600;//1600
        new Handler().postDelayed(SplashActivity.this::showHomeOrLanguageSelection, SPLASH_DISPLAY_LENGTH);
    }
}
