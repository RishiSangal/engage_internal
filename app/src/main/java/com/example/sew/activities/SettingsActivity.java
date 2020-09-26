package com.example.sew.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SwitchCompat;

import com.example.sew.R;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetSettings;
import com.example.sew.apis.SetUserSettings;
import com.example.sew.common.ActivityManager;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.ThemeHelper;
import com.example.sew.models.UserSettings;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class SettingsActivity extends BaseActivity {
    @BindView(R.id.frame)
    LinearLayout frame;
    @BindView(R.id.txtAccountTitle)
    TextView txtAccountTitle;
    @BindView(R.id.txtChangePasswordTitle)
    TextView txtChangePasswordTitle;
    @BindView(R.id.txtSetNewPassword)
    TextView txtSetNewPassword;
    @BindView(R.id.layChangePassword)
    LinearLayout layChangePassword;
    @BindView(R.id.txtFavManagementTitle)
    TextView txtFavManagementTitle;
    @BindView(R.id.txtSaveFavOfflineTitle)
    TextView txtSaveFavOfflineTitle;
    @BindView(R.id.txtSaveFavOfflineSubtitle)
    TextView txtSaveFavOfflineSubtitle;
    @BindView(R.id.swSaveFavOnline)
    SwitchCompat swSaveFavOnline;
    @BindView(R.id.txtNotificationTitle)
    TextView txtNotificationTitle;
    @BindView(R.id.txtSherOfTheDayTitle)
    TextView txtSherOfTheDayTitle;
    @BindView(R.id.txtSherOfTheDaySubtitle)
    TextView txtSherOfTheDaySubtitle;
    @BindView(R.id.swSherOfTheDay)
    SwitchCompat swSherOfTheDay;
    @BindView(R.id.txtPreferredLangTitle)
    TextView txtPreferredLangTitle;
    @BindView(R.id.txtPreferredLangEng)
    TextView txtPreferredLangEng;
    @BindView(R.id.chkPreferredLangEng)
    CheckBox chkPreferredLangEng;
    @BindView(R.id.txtPreferredLangHin)
    TextView txtPreferredLangHin;
    @BindView(R.id.chkPreferredLangHin)
    CheckBox chkPreferredLangHin;
    @BindView(R.id.txtPreferredLangUrdu)
    TextView txtPreferredLangUrdu;
    @BindView(R.id.chkPreferredLangUrdu)
    CheckBox chkPreferredLangUrdu;
    @BindView(R.id.txtWordOfTheDayTitle)
    TextView txtWordOfTheDayTitle;
    @BindView(R.id.txtWordOfTheDaySubtitle)
    TextView txtWordOfTheDaySubtitle;
    @BindView(R.id.swWordOfTheDay)
    SwitchCompat swWordOfTheDay;
    @BindView(R.id.txtEventNotificationTitle)
    TextView txtEventNotificationTitle;
    @BindView(R.id.txtEventNotificationSubtitle)
    TextView txtEventNotificationSubtitle;
    @BindView(R.id.swEventNotification)
    SwitchCompat swEventNotification;
    @BindView(R.id.txtGenericNotificationTitle)
    TextView txtGenericNotificationTitle;
    @BindView(R.id.txtGenericNotificationSubtitle)
    TextView txtGenericNotificationSubtitle;
    @BindView(R.id.swGenericNotification)
    SwitchCompat swGenericNotification;
    @BindView(R.id.txtNewsletterTitle)
    TextView txtNewsletterTitle;
    @BindView(R.id.txtNewsletterSubtitle)
    TextView txtNewsletterSubtitle;
    @BindView(R.id.txtNewsletterDescription)
    TextView txtNewsletterDescription;
    @BindView(R.id.swNewsLetter)
    SwitchCompat swNewsLetter;
    @BindView(R.id.txtCurrentVersionHeader)
    TextView txtCurrentVersionHeader;
    @BindView(R.id.txtCurrentVersion)
    TextView txtCurrentVersion;
    @BindView(R.id.swDarkTheme)
    SwitchCompat swDarkTheme;
    @BindView(R.id.txtDarkTheme)
    TextView txtDarkTheme;
    @BindView(R.id.txtGeneral)
    TextView txtGeneral;
    @BindView(R.id.layDarkTheme)
    LinearLayout layDarkTheme;
    @BindView(R.id.txtDefaultTheme)
    TextView txtDefaultTheme;
    @BindView(R.id.txtDisplay)
    TextView txtDisplay;
    @BindView(R.id.txtCurrentTheme)
    TextView txtCurrentTheme;
    @BindView(R.id.layAppUpdate)
    LinearLayout layAppUpdate;
    @BindView(R.id.layGenricNotification)
    LinearLayout layGenricNotification;
    @BindView(R.id.layEventNotification)
    LinearLayout layEventNotification;
    @BindView(R.id.laySaveFavOffline)
    LinearLayout laySaveFavOffline;
    @BindView(R.id.layRekhtaNewletters)
    LinearLayout layRekhtaNewletters;
    @BindView(R.id.viewNewLetter)
    View viewNewLetter;
    @BindView(R.id.viewNotification)
    View viewNotification;
    @BindView(R.id.viewFavorite)
    View viewFavorite;
    int selectedDarkMode = -1;
    AppUpdateManager appUpdateManager;
    public static Intent getInstance(BaseActivity activity) {
        return new Intent(activity, SettingsActivity.class);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateLanguageSpecificContent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        updateLanguageSpecificContent();
        if (!MyService.isUserLogin()) {
            layGenricNotification.setVisibility(View.GONE);
            layEventNotification.setVisibility(View.GONE);
            laySaveFavOffline.setVisibility(View.GONE);
            layRekhtaNewletters.setVisibility(View.GONE);
            txtNotificationTitle.setVisibility(View.GONE);
            txtFavManagementTitle.setVisibility(View.GONE);
            txtSaveFavOfflineSubtitle.setVisibility(View.GONE);
            txtNewsletterTitle.setVisibility(View.GONE);
            viewNewLetter.setVisibility(View.GONE);
            viewFavorite.setVisibility(View.GONE);
            viewNotification.setVisibility(View.GONE);
        } else {
            layGenricNotification.setVisibility(View.VISIBLE);
            layEventNotification.setVisibility(View.VISIBLE);
            laySaveFavOffline.setVisibility(View.VISIBLE);
            layRekhtaNewletters.setVisibility(View.VISIBLE);
            txtNotificationTitle.setVisibility(View.VISIBLE);
            txtFavManagementTitle.setVisibility(View.VISIBLE);
            txtNewsletterTitle.setVisibility(View.VISIBLE);
            txtSaveFavOfflineSubtitle.setVisibility(View.VISIBLE);
            viewNewLetter.setVisibility(View.VISIBLE);
            viewFavorite.setVisibility(View.VISIBLE);
            viewNotification.setVisibility(View.VISIBLE);
            getUserSettings();
        }
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getPackageName(), 0);
            txtCurrentVersion.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        checkForAppUpdate();

    }

    private UserSettings userSettings;

    private void getUserSettings() {
        showDialog();
        new GetSettings()
                .runAsync((BaseServiceable.OnApiFinishListener<GetSettings>) getSettings -> {
                    dismissDialog();
                    if (getSettings.isValidResponse()) {
                        userSettings = getSettings.getUserSettings();
                        updateUI();
                    }
                });
    }

    private void updateUI() {
        if (userSettings != null) {
            swSaveFavOnline.setChecked(userSettings.isSaveFavoriteOffline());
            swSherOfTheDay.setChecked(userSettings.isSherOfTheDaySubscriber());
            chkPreferredLangEng.setChecked(userSettings.isContentSpecificNotificationEn());
            chkPreferredLangHin.setChecked(userSettings.isContentSpecificNotificationHi());
            chkPreferredLangUrdu.setChecked(userSettings.isContentSpecificNotificationUr());
            swWordOfTheDay.setChecked(userSettings.isWordOfTheDaySubscriber());
            swEventNotification.setChecked(userSettings.isEventNotificationSubscriber());
            swGenericNotification.setChecked(userSettings.isGenericNotificationSubscriber());
            swNewsLetter.setChecked(userSettings.isRequestedNewsLetter());
        }
    }

    private void updateLanguageSpecificContent() {
        setHeaderTitle(MyHelper.getString(R.string.setting));
        txtAccountTitle.setText(MyHelper.getString(R.string.account));
        txtSetNewPassword.setText(MyHelper.getString(R.string.hint_password));
        txtChangePasswordTitle.setText(MyHelper.getString(R.string.change_password));
        txtFavManagementTitle.setText(MyHelper.getString(R.string.favorites_mangement));
        txtSaveFavOfflineTitle.setText(MyHelper.getString(R.string.save_favorites_offline));
        txtSaveFavOfflineSubtitle.setText(MyHelper.getString(R.string.save_favorites_offline_subtitle));
        txtNotificationTitle.setText(MyHelper.getString(R.string.notification));
        txtSherOfTheDayTitle.setText(MyHelper.getString(R.string.sher_of_the_day));
        txtSherOfTheDaySubtitle.setText(MyHelper.getString(R.string.remove_images_daily));
        txtPreferredLangTitle.setText(MyHelper.getString(R.string.preferred_lang));
        txtPreferredLangEng.setText(MyHelper.getString(R.string.english));
        txtPreferredLangHin.setText(MyHelper.getString(R.string.hindi));
        txtPreferredLangUrdu.setText(MyHelper.getString(R.string.urdu));
        txtWordOfTheDayTitle.setText(MyHelper.getString(R.string.word_of_the_sday));
        txtWordOfTheDaySubtitle.setText(MyHelper.getString(R.string.receive_meaning_daily));
        txtEventNotificationTitle.setText(MyHelper.getString(R.string.event_notifications));
        txtEventNotificationSubtitle.setText(MyHelper.getString(R.string.updates_about_rekhta));
        txtGenericNotificationTitle.setText(MyHelper.getString(R.string.genric_notifcations));
        txtGenericNotificationSubtitle.setText(MyHelper.getString(R.string.all_other_new_and_happenings));
        txtNewsletterTitle.setText(MyHelper.getString(R.string.newsletter));
        txtNewsletterSubtitle.setText(MyHelper.getString(R.string.receive_newsletter));
        txtNewsletterDescription.setText(MyHelper.getString(R.string.latest_from_rekhta));
        txtDarkTheme.setText(MyHelper.getString(R.string.dark_theme));
        txtGeneral.setText(MyHelper.getString(R.string.general));
        txtDisplay.setText(MyHelper.getString(R.string.display));
        txtCurrentTheme.setText(MyHelper.getString(R.string.current_theme));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (MyService.getDarkTheme() == DARK_MODE_YES)
                txtDefaultTheme.setText(MyHelper.getString(R.string.dark));
            else if (MyService.getDarkTheme() == DARK_MODE_NO)
                txtDefaultTheme.setText(MyHelper.getString(R.string.light));
            else
                txtDefaultTheme.setText(MyHelper.getString(R.string.use_device_theme));
        } else {
            if (MyService.getDarkTheme() == DARK_MODE_YES)
                txtDefaultTheme.setText(MyHelper.getString(R.string.dark));
            else
                txtDefaultTheme.setText(MyHelper.getString(R.string.light));
        }

        txtCurrentVersionHeader.setText(MyHelper.getString(R.string.app_version));
        swSaveFavOnline.setTag(SetUserSettings.SETTING_FAV_OFFLINE);
        swSherOfTheDay.setTag(SetUserSettings.SETTING_SHER_OF_THE_DAY);
        chkPreferredLangEng.setTag(SetUserSettings.SETTING_PREFERRED_LANG_EN);
        chkPreferredLangHin.setTag(SetUserSettings.SETTING_PREFERRED_LANG_HI);
        chkPreferredLangUrdu.setTag(SetUserSettings.SETTING_PREFERRED_LANG_UR);
        swWordOfTheDay.setTag(SetUserSettings.SETTING_WORD_OF_THE_DAY);
        swEventNotification.setTag(SetUserSettings.SETTING_EVENT_NOTIFICATION);
        swGenericNotification.setTag(SetUserSettings.SETTING_GENERIC_NOTIFICATION);
        swNewsLetter.setTag(SetUserSettings.SETTING_NEWSLETTER);
    }
    @OnClick(R.id.layAppUpdate)
    void onAppUpdateNow(){
        checkForAppUpdate();
    }
    @OnCheckedChanged({R.id.swSaveFavOnline, R.id.swSherOfTheDay, R.id.chkPreferredLangEng, R.id.chkPreferredLangHin, R.id.chkPreferredLangUrdu,
            R.id.swWordOfTheDay, R.id.swEventNotification, R.id.swGenericNotification, R.id.swNewsLetter})
    public void onCheckChanged(CompoundButton view, boolean isChecked) {

        switch (view.getId()) {
            case R.id.swSaveFavOnline:
                if (!isChecked && MyService.isOfflineSaveEnable())
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Rekhta")
                            .setMessage("By turning off this setting, you will loose all local content. Do you really want to proceed?")
                            .setPositiveButton("CANCEL", ((dialog, which) -> swSaveFavOnline.setChecked(true)))
                            .setNegativeButton("OK", (dialog, which) -> {
                                MyService.setIsOfflineSaveEnable(false);
                                MyService.removeAllDetailedFavoriteContent();
                                updateSetting((int) view.getTag(), false);
                            })
                            .setCancelable(false)
                            .create().show();
                else {
                    updateSetting((int) view.getTag(), isChecked);
                    MyService.setIsOfflineSaveEnable(isChecked);
                }
                break;
            case R.id.swSherOfTheDay:
            case R.id.chkPreferredLangEng:
            case R.id.chkPreferredLangHin:
            case R.id.chkPreferredLangUrdu:
            case R.id.swWordOfTheDay:
            case R.id.swEventNotification:
            case R.id.swGenericNotification:
            case R.id.swNewsLetter:
                if (!MyService.isUserLogin())
                    showToast("Please Login to change these settings");
                else
                    updateSetting((int) view.getTag(), isChecked);
        }
    }

    private void updateSetting(int setting, boolean value) {
        new SetUserSettings()
                .setSetting(setting, value)
                .runAsync(null);
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkNewAppVersionState();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {

            case REQUEST_SUPPORT_IN_APP_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    // Log.d("Update flow failed! Result code: " + resultCode);
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    unregisterInstallStateUpdListener();
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterInstallStateUpdListener();
        super.onDestroy();
    }
    @OnClick(R.id.layDarkTheme)
    public void onClickDarkTheme() {
        showDarkThemePopup();
    }

    String[] items;
    int checkedItem;

    private void showDarkThemePopup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        alertDialog.setTitle(MyHelper.getString(R.string.dark_theme));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            items = new String[]{MyHelper.getString(R.string.light), MyHelper.getString(R.string.dark), MyHelper.getString(R.string.use_device_theme)};
            checkedItem = 0;
        } else {
            items = new String[]{MyHelper.getString(R.string.light), MyHelper.getString(R.string.dark)};
            checkedItem = 0;
        }

        if (MyService.getDarkTheme() == DARK_MODE_YES) {
            checkedItem = 1;
            selectedDarkMode = DARK_MODE_YES;
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (MyService.getDarkTheme() == DARK_MODE_NO) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            checkedItem = 0;
            selectedDarkMode = DARK_MODE_NO;
        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            checkedItem = 2;
            selectedDarkMode = DARK_MODE_AUTO;
        }
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        txtDefaultTheme.setText(MyHelper.getString(R.string.light));
                        selectedDarkMode = DARK_MODE_NO;
                        break;
                    case 1:
                        txtDefaultTheme.setText(MyHelper.getString(R.string.dark));
                        selectedDarkMode = DARK_MODE_YES;
                        break;
                    case 2:
                        txtDefaultTheme.setText(MyHelper.getString(R.string.use_device_theme));
                        selectedDarkMode = DARK_MODE_AUTO;
                        break;

                }
            }
        });
        alertDialog.setPositiveButton(MyHelper.getString(R.string.okay), (dialog, which) -> {
            if (selectedDarkMode == DARK_MODE_AUTO) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                MyService.setDarkTheme(DARK_MODE_AUTO);
                MyService.setDarkModePref(ThemeHelper.DEFAULT_MODE);
            } else if (selectedDarkMode == DARK_MODE_NO) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                MyService.setDarkTheme(DARK_MODE_NO);
                MyService.setDarkModePref(ThemeHelper.LIGHT_MODE);
            } else if (selectedDarkMode == DARK_MODE_YES) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                MyService.setDarkTheme(DARK_MODE_YES);
                MyService.setDarkModePref(ThemeHelper.DARK_MODE);
            }
            dialog.dismiss();
            ThemeHelper.applyTheme(MyService.getDarkModePref());
//            ActivityManager.getInstance().reCreateCall();
        });
        alertDialog.setNegativeButton(MyHelper.getString(R.string.button_cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
    InstallStateUpdatedListener installStateUpdatedListener;
    private void checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(SettingsActivity.this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                layAppUpdate.setVisibility(View.VISIBLE);
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    // Before starting an update, register a listener for updates.
                    appUpdateManager.registerListener(installStateUpdatedListener);
                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo);
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) ) {
                    // Start an update.
                    startAppUpdateImmediate(appUpdateInfo);
                }
            }
        });
    }
    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQUEST_SUPPORT_IN_APP_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQUEST_SUPPORT_IN_APP_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        Snackbar snackbar =
                Snackbar.make(frame, "Downloaded", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Restart", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.md_red_700));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            //FLEXIBLE:
                            // If the update is downloaded but not installed,
                            // notify the user to complete the update.
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                popupSnackbarForCompleteUpdateAndUnregister();
                            }

                            //IMMEDIATE:
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                startAppUpdateImmediate(appUpdateInfo);
                            }
                        });

    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }




}
