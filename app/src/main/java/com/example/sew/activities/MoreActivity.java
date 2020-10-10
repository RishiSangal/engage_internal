package com.example.sew.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.alexvasilkov.gestures.commons.circle.CircleImageView;
import com.example.sew.R;
import com.example.sew.apis.PostLogout;
import com.example.sew.common.ActivityManager;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.User;
import com.example.sew.views.TitleTextViewType6;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreActivity extends BaseHomeActivity {

    @BindView(R.id.nav_moreTV)
    TextView navMoreTV;
    @BindView(R.id.txtImageShayariTitle)
    TextView txtImageShayariTitle;
    @BindView(R.id.txtDictionaryTitle)
    TextView txtDictionaryTitle;
    @BindView(R.id.txtSettingsTitle)
    TextView txtSettingsTitle;
    @BindView(R.id.laySettings)
    LinearLayout laySettings;
    @BindView(R.id.txtAboutUsTitle)
    TextView txtAboutUsTitle;
    @BindView(R.id.txtFeedbackTitle)
    TextView txtFeedbackTitle;

    @BindView(R.id.txtLogoutTitle)
    TextView txtLogoutTitle;
    //   @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    ImageView imgBannerImage;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtLanguageSelectTitle)
    TextView txtLanguageSelectTitle;
    @BindView(R.id.btnEnglishLanguage)
    Button btnEnglishLanguage;
    @BindView(R.id.btnHindiLanguage)
    Button btnHindiLanguage;
    @BindView(R.id.btnUrduLanguage)
    Button btnUrduLanguage;
    @BindView(R.id.txtProseTitle)
    TextView txtProseTitle;
    @BindView(R.id.txtShayariTitle)
    TextView txtShayariTitle;
    @BindView(R.id.txtPoetsTitle)
    TextView txtPoetsTitle;
    @BindView(R.id.txtSherTitle)
    TextView txtSherTitle;
    @BindView(R.id.txtGhazalTitle)
    TextView txtGhazalTitle;
    @BindView(R.id.txtNazmTitle)
    TextView txtNazmTitle;
    @BindView(R.id.txtFavoriteTitle)
    TextView txtFavoriteTitle;
    @BindView(R.id.txtHomeTitle)
    TextView txtHomeTitle;
    @BindView(R.id.txtLogin)
    TextView txtLogin;
    @BindView(R.id.nav_ContributeTV)
    TextView navContributeTV;
    @BindView(R.id.layDonateNow)
    LinearLayout layDonateNow;
    @BindView(R.id.layDonateViaPaytm)
    LinearLayout layDonateViaPaytm;
    @BindView(R.id.txtPoetryTitle)
    TextView txtPoetryTitle;
    @BindView(R.id.txtDonateOnline)
    TitleTextViewType6 txtDonateOnline;
    @BindView(R.id.txtDonateViaPaytm)
    TitleTextViewType6 txtDonateViaPaytm;
    @BindView(R.id.txtMoreMyFavoriteCount)
    TextView txtMoreMyFavoriteCount;
    @BindView(R.id.layNotification)
    LinearLayout layNotification;
    @BindView(R.id.txtNotificationTitle)
    TextView txtNotificationTitle;
    @BindView(R.id.txtNotificationCount)
    TextView txtNotificationCount;
    @BindView(R.id.layWholeDonation)
    LinearLayout layWholeDonation;
    View top_more;

    public static Intent getInstance(Activity activity) {
        return new Intent(activity, MoreActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        top_more = findViewById(R.id.top_more);
        imgProfile = top_more.findViewById(R.id.imgProfile);
        imgBannerImage = top_more.findViewById(R.id.imgBannerImage);
        // setSelectableItemForeground(layDonateViaPaytm,layDonateNow);
        initBottomNavigation(Enums.BOTTOM_TYPE.HOME_4);
        if (MyService.isUserLogin()) {
            User user = MyService.getUser();
            if (!TextUtils.isEmpty(user.getBannerImageName()))
                ImageHelper.setImage(imgBannerImage, user.getBannerImageName(), Enums.PLACEHOLDER_TYPE.USER_INFO_BANNER);
        } else
            imgBannerImage.setImageResource(R.drawable.home);
        updateUI();
        syncFavoriteIfNecessary();
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
    }

    private void updateUI() {
//        laySettings.setVisibility(MyService.isUserLogin() ? View.VISIBLE : View.GONE);
        txtImageShayariTitle.setText(MyHelper.getString(R.string.shayari_gallery));
        txtDictionaryTitle.setText(MyHelper.getString(R.string.dictionary));
        txtSettingsTitle.setText(MyHelper.getString(R.string.setting));
        txtAboutUsTitle.setText(MyHelper.getString(R.string.about));
        txtFeedbackTitle.setText(MyHelper.getString(R.string.feedback));
        navMoreTV.setText(MyHelper.getString(R.string.more));
        txtPoetryTitle.setText(MyHelper.getString(R.string.poetry));
        navContributeTV.setText(MyHelper.getString(R.string.contribute));
        txtProseTitle.setText(MyHelper.getString(R.string.prose));
        txtShayariTitle.setText(MyHelper.getString(R.string.shayari));
        txtLanguageSelectTitle.setText(MyHelper.getString(R.string.language_quick_switch));
        txtPoetsTitle.setText(MyHelper.getString(R.string.poets));
        txtSherTitle.setText(MyHelper.getString(R.string.sher));
        txtGhazalTitle.setText(MyHelper.getString(R.string.ghazals));
        txtNazmTitle.setText(MyHelper.getString(R.string.nazms));
        txtFavoriteTitle.setText(MyHelper.getString(R.string.myfavorites));
        txtHomeTitle.setText(MyHelper.getString(R.string.home));
        navContributeTV.setText(MyHelper.getString(R.string.contribute));
        txtDonateOnline.setText(MyHelper.getString(R.string.donate_now));
        txtDonateViaPaytm.setText(MyHelper.getString(R.string.donate_via_paytm));
        int totalFavoriteCount = MyService.getTotalFavoriteCount();
        layWholeDonation.setVisibility(View.GONE);
        txtMoreMyFavoriteCount.setText(totalFavoriteCount == 0 ? "" : String.valueOf(totalFavoriteCount) + " " + MyHelper.getString(R.string.myfavorites));
        if (MyService.isUserLogin()) {
            txtLogoutTitle.setText(MyHelper.getString(R.string.logout));
            txtLogin.setText(getString(R.string.rico_logout));
            //imgLogout.setImageResource(R.drawable.ic_logout);
            User user = MyService.getUser();
            txtUserName.setText(user.getDisplayName());
            txtMoreMyFavoriteCount.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user.getImageName()))
                ImageHelper.setImage(imgProfile, user.getImageName(), Enums.PLACEHOLDER_TYPE.PROFILE);
        } else {
            txtLogoutTitle.setText(MyHelper.getString(R.string.login));
            txtLogin.setText(getString(R.string.rico_logout));
            // imgLogout.setImageResource(R.drawable.ic_login);
            txtUserName.setText(MyService.getUser().getDisplayName());
            txtUserName.setText(MyHelper.getString(R.string.guest));
            txtMoreMyFavoriteCount.setVisibility(View.INVISIBLE);
        }
        btnEnglishLanguage.setBackgroundResource(R.drawable.languagebtn_white_border);
        btnEnglishLanguage.setTextColor(Color.WHITE);
        btnHindiLanguage.setBackgroundResource(R.drawable.languagebtn_white_border);
        btnHindiLanguage.setTextColor(Color.WHITE);
        btnUrduLanguage.setBackgroundResource(R.drawable.languagebtn_white_border);
        btnUrduLanguage.setTextColor(Color.WHITE);
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                btnEnglishLanguage.setBackgroundResource(R.drawable.languagebtn_white_selected);
                btnEnglishLanguage.setTextColor(Color.BLACK);
                break;
            case HINDI:
                btnHindiLanguage.setBackgroundResource(R.drawable.languagebtn_white_selected);
                btnHindiLanguage.setTextColor(Color.BLACK);
                break;
            case URDU:
                btnUrduLanguage.setBackgroundResource(R.drawable.languagebtn_white_selected);
                btnUrduLanguage.setTextColor(Color.BLACK);
                break;
        }
    }

    @SuppressLint("ResourceType")
    @OnClick({R.id.layFavorite, R.id.layHome, R.id.layPoets, R.id.laySher, R.id.layGhazal, R.id.layNazm, R.id.layProse, R.id.layShayari, R.id.layImageShayari, R.id.layDictionary, R.id.laySettings, R.id.layAboutUs, R.id.layFeedback,
            R.id.layLogout, R.id.btnEnglishLanguage, R.id.btnHindiLanguage, R.id.btnUrduLanguage, R.id.layDonateNow, R.id.layDonateViaPaytm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layImageShayari:
                startActivity(ShayariImagesActivity.getInstance(getActivity()));
                break;
            case R.id.layDictionary:
                startActivity(DictionaryActivity.getInstance(getActivity()));
                break;
            case R.id.laySettings:
                startActivity(SettingsActivity.getInstance(getActivity()));
                break;
            case R.id.layAboutUs:
                startActivity(AboutUsActivity.getInstance(getActivity()));
                break;
            case R.id.layFeedback:
                startActivity(FeedbackActivity.getInstance(getActivity()));
                break;
            case R.id.layLogout:
                if (MyService.isUserLogin()) {
                    new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                            // new AlertDialog.Builder(getActivity())
                            .setTitle(MyHelper.getString(R.string.rekhta))
                            .setMessage(MyHelper.getString(R.string.logout_warning))
                            .setPositiveButton(MyHelper.getString(R.string.yes), (dialog, which) -> {
                                new PostLogout().runAsync(null);
                                MyService.logoutUser();
                                startActivity(new Intent(getActivity(), SplashActivity.class));
                                ActivityManager.getInstance().clearStack();
                                dialog.dismiss();
                            })
                            .setNegativeButton(MyHelper.getString(R.string.no), (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .create().show();

                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    ActivityManager.getInstance().clearStack();
                    finish();
                }

                break;
            case R.id.btnEnglishLanguage:
                MyService.setSelectedLanguage(Enums.LANGUAGE.ENGLISH);
                break;
            case R.id.btnHindiLanguage:
                MyService.setSelectedLanguage(Enums.LANGUAGE.HINDI);
                break;
            case R.id.btnUrduLanguage:
                MyService.setSelectedLanguage(Enums.LANGUAGE.URDU);
                break;
            case R.id.layProse:
                startActivity(ProseShayariActivity.getInstance(getActivity(), Enums.COLLECTION_TYPE.PROSE));
                break;
            case R.id.layShayari:
                startActivity(ProseShayariActivity.getInstance(getActivity(), Enums.COLLECTION_TYPE.SHAYARI));
                break;
            case R.id.layPoets:
                startActivity(PoetsActivity.getInstance(getActivity()));
                break;
            case R.id.laySher:
                startActivity(ShayariActivity.getInstance(getActivity()));
                break;
            case R.id.layGhazal:
                startActivity(GhazalsActivity.getInstance(getActivity()));
                break;
            case R.id.layNazm:
                startActivity(NazmActivity.getInstance(getActivity()));
                break;
            case R.id.layFavorite:
                if (!MyService.isUserLogin()) {
                    startActivity(LoginActivity.getInstance(getActivity()));
                    BaseActivity.showToast("Please login");
                } else
                    startActivity(FavoriteActivity.getInstance(getActivity()));
                break;
            case R.id.layHome:
                startActivity(HomeActivity.getInstance(getActivity(), false));
                break;
            case R.id.layDonateNow:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getActivity().getString(R.string.donate_rekhta_url)));
                getActivity().startActivity(browserIntent);
                break;
            case R.id.layDonateViaPaytm:
                Intent browserIntentPaytm = new Intent(Intent.ACTION_VIEW, Uri.parse(getActivity().getString(R.string.donate_rekhta_via_paytm)));
                getActivity().startActivity(browserIntentPaytm);
                break;
        }
    }

}
