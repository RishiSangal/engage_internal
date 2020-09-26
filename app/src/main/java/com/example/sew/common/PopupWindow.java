package com.example.sew.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.FavoriteActivity;
import com.example.sew.activities.LoginActivity;
import com.example.sew.activities.SettingsActivity;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.ServiceManager;
import com.example.sew.helpers.ThemeHelper;
import com.example.sew.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sew.common.ICommonValues.DARK_MODE_AUTO;
import static com.example.sew.common.ICommonValues.DARK_MODE_NO;
import static com.example.sew.common.ICommonValues.DARK_MODE_YES;


public class PopupWindow extends RelativePopupWindow {
    BaseActivity activity;
    ViewHolder viewHolder;

    public PopupWindow(BaseActivity context) {
        this.activity = context;
        View convertView = LayoutInflater.from(context).inflate(R.layout.menu_popup, null);
        viewHolder = new ViewHolder(convertView, this);
        setContentView(convertView);
        setWidth(getDesiredWidth(context));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
       // setSelectableItemForeground(viewHolder.txtMenuLanguageEn, viewHolder.txtMenuLanguageHin,viewHolder.txtMenuLanguageUr);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(40);
        }
        if (!MyService.isUserLogin()) {
            viewHolder.txtUserName.setText(MyHelper.getString(R.string.guest));
            viewHolder.txtMenuMyFavoriteCount.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.txtMenuMyFavoriteCount.setVisibility(View.VISIBLE);
            User user = MyService.getUser();
            viewHolder.txtUserName.setText(user.getDisplayName());
            if (user.isUserImageExist())
                ImageHelper.setImage(viewHolder.imgProfile, user.getImageName(), Enums.PLACEHOLDER_TYPE.PROFILE);

        }
        viewHolder.txtDarkTheme.setText(MyHelper.getString(R.string.dark_theme));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (MyService.getDarkTheme() == DARK_MODE_YES)
                viewHolder.txtDefaultTheme.setText(MyHelper.getString(R.string.dark));
            else if (MyService.getDarkTheme() == DARK_MODE_NO)
                viewHolder.txtDefaultTheme.setText(MyHelper.getString(R.string.light));
            else
                viewHolder.txtDefaultTheme.setText(MyHelper.getString(R.string.use_device_theme));
        } else {
            if (MyService.getDarkTheme() == DARK_MODE_YES)
                viewHolder.txtDefaultTheme.setText(MyHelper.getString(R.string.dark));
            else
                viewHolder.txtDefaultTheme.setText(MyHelper.getString(R.string.light));
        }

    }

    public BaseActivity getActivity() {
        return activity;
    }

    private int getDesiredWidth(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width / 2 + 150;
    }

    static class ViewHolder {
        PopupWindow popupWindow;
        @BindView(R.id.imgProfile)
        CircleImageView imgProfile;
        @BindView(R.id.txtUserName)
        TextView txtUserName;
        @BindView(R.id.txtMenuLanguageEn)
        TextView txtMenuLanguageEn;
        @BindView(R.id.txtMenuLanguageHin)
        TextView txtMenuLanguageHin;
        @BindView(R.id.txtMenuLanguageUr)
        TextView txtMenuLanguageUr;
        @BindView(R.id.txtMenuMyFavorite)
        TextView txtMenuMyFavorite;
        @BindView(R.id.txtMenuSettings)
        TextView txtMenuSettings;
        @BindView(R.id.layUser)
        LinearLayout layUser;
        @BindView(R.id.txtMenuMyFavoriteCount)
        TextView txtMenuMyFavoriteCount;
        @BindView(R.id.layDarkTheme)
        LinearLayout layDarkTheme;
        @BindView(R.id.txtDefaultTheme)
        TextView txtDefaultTheme;
        @BindView(R.id.txtDarkTheme)
        TextView txtDarkTheme;

        @OnClick(R.id.txtMenuLanguageEn)
        void onMenuLanguageEnClicked() {
            changeLanguageIfNecessary(Enums.LANGUAGE.ENGLISH);
        }

        @OnClick(R.id.txtMenuLanguageHin)
        void onMenuLanguageHinClicked() {
            changeLanguageIfNecessary(Enums.LANGUAGE.HINDI);
        }

        @SuppressLint("WrongConstant")
        @OnClick(R.id.txtMenuLanguageUr)
        void onMenuLanguageUrClicked() {
            changeLanguageIfNecessary(Enums.LANGUAGE.URDU);
        }

        private void changeLanguageIfNecessary(Enums.LANGUAGE newLanguage) {
            if (MyService.getSelectedLanguage() != newLanguage) {
                MyService.setSelectedLanguage(newLanguage);
                popupWindow.dismiss();
            }
        }

        @OnClick(R.id.txtMenuMyFavorite)
        void onMenuMyFavoriteClicked() {
            if (new ServiceManager(popupWindow.getActivity()).isNetworkAvailable()) {
                if (!MyService.isUserLogin()) {
                    popupWindow.getActivity().startActivity(LoginActivity.getInstance(popupWindow.getActivity()));
                    BaseActivity.showToast("Please login");
                } else
                    popupWindow.getActivity().startActivity(FavoriteActivity.getInstance(popupWindow.getActivity()));
                popupWindow.dismiss();
            } else
                BaseActivity.showToast(AppErrorMessage.please_check_your_connection_and_try_again);
        }

        @OnClick(R.id.txtMenuSettings)
        void onMenuSettingsClicked() {
            if (new ServiceManager(popupWindow.getActivity()).isNetworkAvailable()) {
                popupWindow.getActivity().startActivity(SettingsActivity.getInstance(popupWindow.getActivity()));
                popupWindow.dismiss();
            } else
                BaseActivity.showToast(AppErrorMessage.please_check_your_connection_and_try_again);

        }

        @OnClick(R.id.layDarkTheme)
        public void onClickDarkTheme() {
            showDarkThemePopup();
        }

        String[] items;
        int checkedItem;
        int selectedDarkMode = -1;

        private void showDarkThemePopup() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(popupWindow.getActivity(), R.style.AlertDialogCustom));
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
                popupWindow.dismiss();
                ThemeHelper.applyTheme(MyService.getDarkModePref());
//                ActivityManager.getInstance().reCreateCall();
            });
            alertDialog.setNegativeButton(MyHelper.getString(R.string.button_cancel), (dialog, which) -> dialog.dismiss());
            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }

        ViewHolder(View view, PopupWindow popupWindow) {
            ButterKnife.bind(this, view);
            this.popupWindow = popupWindow;
            txtMenuLanguageEn.setBackgroundResource(R.drawable.rounded_button_language_unselected);
            txtMenuLanguageUr.setBackgroundResource(R.drawable.rounded_button_language_unselected);
            txtMenuLanguageHin.setBackgroundResource(R.drawable.rounded_button_language_unselected);
            txtMenuLanguageEn.setTextColor(popupWindow.activity.getPrimaryTextColor());
            txtMenuLanguageUr.setTextColor(popupWindow.activity.getPrimaryTextColor());
            txtMenuLanguageHin.setTextColor(popupWindow.activity.getPrimaryTextColor());
            switch (MyService.getSelectedLanguage()) {
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
            int totalFavoriteCount = MyService.getTotalFavoriteCount();
            txtMenuMyFavorite.setText(MyHelper.getString(R.string.myfavorites));
            txtMenuMyFavoriteCount.setText(totalFavoriteCount == 0 ? "" : String.valueOf(totalFavoriteCount));
            txtMenuSettings.setText(MyHelper.getString(R.string.setting));
        }
    }
}
