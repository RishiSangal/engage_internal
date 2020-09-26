package com.example.sew.activities;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.ServiceManager;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class BaseHomeActivity extends BaseActivity {

    @BindView(R.id.imgTabHome)
    TextView imgTabHome;
    @BindView(R.id.txtTabHome)
    TextView txtTabHome;
    @BindView(R.id.imgTabSearch)
    TextView imgTabSearch;
    @BindView(R.id.txtTabSearch)
    TextView txtTabSearch;
    @BindView(R.id.imgTabFavorite)
    TextView imgTabFavorite;
    @BindView(R.id.txtTabFavorite)
    TextView txtTabFavorite;
    @BindView(R.id.imgTabMore)
    TextView imgTabMore;
    @BindView(R.id.txtTabMore)
    TextView txtTabMore;
    @BindView(R.id.layTabHome)
    LinearLayout layTabHome;
    @BindView(R.id.layTabSearch)
    LinearLayout layTabSearch;
    @BindView(R.id.layTabFavorite)
    LinearLayout layTabFavorite;
    @BindView(R.id.layTabMore)
    LinearLayout layTabMore;

    public final void initBottomNavigation(Enums.BOTTOM_TYPE bottomType) {
        updateBottomText();
        imgTabHome.setText(getString(R.string.rico_home));
        imgTabFavorite.setText(getString(R.string.rico_not_favorited));
        switch (bottomType) {
            case HOME_1:
                imgTabHome.setText(getString(R.string.rico_home_filled));
                imgTabHome.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
                txtTabHome.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
                break;
            case HOME_2:
                imgTabSearch.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
                txtTabSearch.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
                break;
            case HOME_3:
                imgTabFavorite.setText(getString(R.string.rico_favorited));
                imgTabFavorite.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
                txtTabFavorite.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
                break;
            case HOME_4:
                imgTabMore.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
                txtTabMore.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
                break;
        }
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateBottomText();
    }

    private void updateBottomText() {
        txtTabHome.setText(MyHelper.getString(R.string.home));
        txtTabFavorite.setText(MyHelper.getString(R.string.my_collections));
        txtTabSearch.setText(MyHelper.getString(R.string.search));
        txtTabMore.setText(MyHelper.getString(R.string.home_more));
    }

    @OnClick({R.id.layTabHome, R.id.layTabSearch, R.id.layTabFavorite, R.id.layTabMore})
    public void onBottomViewClicked(View view) {
        // if (MyApplication.getInstance().isBrowsingOffline()) {
        if (!new ServiceManager(getActivity()).isNetworkAvailable()){
            showToast(AppErrorMessage.please_check_your_connection_and_try_again);
            return;
        }else{
            MyApplication.getInstance().setBrowsingOffline(false);
        }
        switch (view.getId()) {
            case R.id.layTabHome:
                if (!(getActivity() instanceof HomeActivity)) {
                    startActivity(HomeActivity.getInstance(getActivity(),false));
                    finish();
                }
                break;
            case R.id.layTabSearch:
                if (!(getActivity() instanceof SearchActivity)) {
                    startActivity(SearchActivity.getInstance(getActivity(), ""));
                    finish();
                }
                break;
            case R.id.layTabFavorite:
                if (!MyService.isUserLogin()) {
                    startActivity(LoginActivity.getInstance(getActivity()));
                    BaseActivity.showToast("Please login");
                } else
                    startActivity(FavoriteActivity.getInstance(getActivity()));
                break;
            case R.id.layTabMore:
                if (!(getActivity() instanceof MoreActivity)) {
                    startActivity(MoreActivity.getInstance(getActivity()));
                    finish();
                }
                break;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (!(getActivity() instanceof HomeActivity)) {
            startActivity(HomeActivity.getInstance(getActivity(),false));
            finish();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            showToast("Please click BACK again to exit");
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }

    }
}
