package com.example.sew.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.adapters.DictionaryRecyclerAdapter;
import com.example.sew.adapters.FavoritePoetAdapter;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.ServiceManager;
import com.example.sew.models.FavContentPageModel;
import com.example.sew.models.FavoriteDictionary;
import com.example.sew.models.FavoritePoet;
import com.example.sew.models.PoetDetail;
import com.example.sew.views.paging_recycler_view.PagingRecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritePoetFragment extends BaseFragment {
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoets)
    PagingRecyclerView lstPoets;

    public static Intent getInstance(BaseActivity activity) {
        return new Intent(activity, FavoritePoetFragment.class);
    }
    private void onPoetClick(View convertView) {
        if (new ServiceManager(getActivity()).isNetworkAvailable()) {
            FavoritePoet favoritePoet = (FavoritePoet) convertView.getTag(R.id.tag_data);
            if (favoritePoet != null)
                startActivity(PoetDetailActivity.getInstance(getActivity(), favoritePoet.getId()));
        }
    }
    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
        if (!CollectionUtils.isEmpty(favoritePoets)) {
            favoritePoets.clear();
            updateUI();
            getPoetResult();
        }
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        if (favoritePoetAdapter != null)
            favoritePoetAdapter.notifyDataSetChanged();
    }

    public static FavoritePoetFragment getInstance() {
        FavoritePoetFragment favoritePoetFragment = new FavoritePoetFragment();
        Bundle bundle = new Bundle();
        favoritePoetFragment.setArguments(bundle);
        return favoritePoetFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_poet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
        lstPoets.setNestedScrollingEnabled(true);
        getPoetResult();
    }

    private void getPoetResult() {
        getFavoritePoet();
    }

    private ArrayList<FavoritePoet> favoritePoets = new ArrayList<>();

    private void getFavoritePoet() {
        favoritePoets.clear();
        favoritePoets.addAll(MyService.getAllFavoritePoet());
        Collections.reverse(favoritePoets);
        updateUI();
        lstPoets.onNoMoreData();
        lstPoets.onHide();
    }


    private FavoritePoetAdapter favoritePoetAdapter;
    private void updateUI() {
        dismissDialog();
        lstPoets.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (favoritePoetAdapter == null) {
            favoritePoetAdapter = new FavoritePoetAdapter(GetActivity(), favoritePoets);
            favoritePoetAdapter.setOnItemClickListener(this::onPoetClick);
            lstPoets.setAdapter(favoritePoetAdapter);
        } else
            favoritePoetAdapter.notifyDataSetChanged();



    }

}
