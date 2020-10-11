package com.example.sew.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.FavoriteFragmentAdapter;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.ServiceManager;
import com.example.sew.models.ContentType;
import com.example.sew.models.FavContentPageModel;
import com.example.sew.models.FavoriteDictionary;
import com.example.sew.models.FavoritePoet;
import com.example.sew.models.ShayariImage;
import com.example.sew.models.User;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteActivity extends BaseHomeActivity {
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userImage)
    CircleImageView userImage;
    @BindView(R.id.txtFavTabMyFavoriteCount)
    TextView txtFavTabMyFavoriteCount;
    @BindView(R.id.imgBannerImage)
    ImageView imgBannerImage;

    public static Intent getInstance(Activity activity) {
        return new Intent(activity, FavoriteActivity.class);
    }

    boolean requireUpdate = false;

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        requireUpdate = true;
    }

    TreeMap<ContentType, ArrayList<FavContentPageModel>> allFavoriteSections = new TreeMap<>();
    ArrayList<ShayariImage> allSavedImageShayari = new ArrayList<>();
    ArrayList<FavoriteDictionary> allFavoriteDictionary = new ArrayList<>();
    ArrayList<FavoritePoet> allFavoritePoet = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_myconnectiins_tab);
        ButterKnife.bind(this);
        initBottomNavigation(Enums.BOTTOM_TYPE.HOME_3);
        getFavorites();
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUI();
            }
        }, BROADCAST_FAVORITE_UPDATED);
//        updateUI();
        if (MyService.isUserLogin()) {
            User user = MyService.getUser();
            userName.setText(user.getDisplayName());
            if (!TextUtils.isEmpty(user.getImageName()))
                ImageHelper.setImage(userImage, user.getImageName(), Enums.PLACEHOLDER_TYPE.PROFILE);
            if (!TextUtils.isEmpty(user.getBannerImageName()))
                ImageHelper.setImage(imgBannerImage, user.getBannerImageName(), Enums.PLACEHOLDER_TYPE.USER_INFO_BANNER);
        } else {
            userName.setText(MyHelper.getString(R.string.guest));
            imgBannerImage.setImageResource(R.drawable.home);
        }
        showFavCount();
        resetAndUpdateFavorite();
    }

    public void showFavCount() {
        int totalFavoriteCount = MyService.getTotalFavoriteCount();
        txtFavTabMyFavoriteCount.setText(totalFavoriteCount == 0 ? "" : String.valueOf(totalFavoriteCount) + " " + MyHelper.getString(R.string.myfavorites));
    }

    private void getFavorites() {
        syncFavoriteIfNecessary();
    }

    FavoriteFragmentAdapter favoriteFragmentAdapter;

    private void updateUI() {
        allFavoriteSections.clear();
        allSavedImageShayari.clear();
        allFavoriteDictionary.clear();
        allFavoritePoet.clear();
        if (new ServiceManager(getActivity()).isNetworkAvailable())
            allSavedImageShayari.addAll(MyService.MyFavService.getShayariImages());
        allFavoriteSections.putAll(MyService.MyFavService.getAllFavoriteSections());
        allFavoriteDictionary.addAll(MyService.MyFavService.getFavoriteDictionary());
        allFavoritePoet.addAll(MyService.MyFavService.getFavoritePoet());
        if (favoriteFragmentAdapter == null) {
            favoriteFragmentAdapter = new FavoriteFragmentAdapter(getSupportFragmentManager(), getActivity(),
                    allFavoriteSections, allSavedImageShayari, allFavoriteDictionary,allFavoritePoet);
            viewpager.setAdapter(favoriteFragmentAdapter);
        } else {
            favoriteFragmentAdapter.notifyDataSetChanged();
        }
        tabLayout.setViewPager(viewpager);
    }

    public ArrayList<FavContentPageModel> getFavContents(ContentType contentType) {
        return allFavoriteSections.get(contentType);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        lazyRefreshTabPositioning(tabLayout, viewpager);
        showFavCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requireUpdate) {
            requireUpdate = false;
            resetAndUpdateFavorite();
        }
    }

    private void resetAndUpdateFavorite() {
        new ResetFavoriteAsync().execute();
    }

    private class ResetFavoriteAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            showDialog();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MyService.MyFavService.resetAllFavorites();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissDialog();
            updateUI();
        }
    }
}
