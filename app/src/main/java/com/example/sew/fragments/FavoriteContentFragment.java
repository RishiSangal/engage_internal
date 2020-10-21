package com.example.sew.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.R;
import com.example.sew.activities.FavoriteActivity;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.adapters.FavoriteContentRecyclerAdapter;
import com.example.sew.models.ContentType;
import com.example.sew.models.FavContentPageModel;
import com.google.android.gms.common.util.CollectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteContentFragment extends BaseFragment {

    @BindView(R.id.lstFavContent)
    RecyclerView lstFavContent;
    private ArrayList<FavContentPageModel> favContentPageModels = new ArrayList<>();

    //    @OnItemClick(R.id.lstFavContent)
    private void onGhazalClick(View convertView) {
        FavContentPageModel favContentPageModel = (FavContentPageModel) convertView.getTag(R.id.tag_data);
        if (favContentPageModel != null)
            startActivity(RenderContentActivity.getInstance(getActivity(), favContentPageModel.getId()));
    }

    public static FavoriteContentFragment getInstance(ContentType contentType) {
        FavoriteContentFragment poetsFragment = new FavoriteContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_TYPE_OBJ, contentType.getJsonObject().toString());
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_content, container, false);
        ButterKnife.bind(this, view);
        lstFavContent.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        lstFavContent.setNestedScrollingEnabled(true);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        lstFavContent.setHasMoreItems(false);
        updateUI();
    }

    private FavoriteContentRecyclerAdapter favoriteContentAdapter;

    private void updateUI() {
        if (GetActivity() instanceof FavoriteActivity) {
            favContentPageModels.clear();
            ArrayList<FavContentPageModel> data = ((FavoriteActivity) GetActivity()).getFavContents(getContentType());
            if (!CollectionUtils.isEmpty(data))
                favContentPageModels.addAll(data);
        }
        sortFavouriteList(favContentPageModels);
//        Collections.reverse(favContentPageModels);
        if (favoriteContentAdapter == null) {
            favoriteContentAdapter = new FavoriteContentRecyclerAdapter(GetActivity(), favContentPageModels);
            favoriteContentAdapter.setOnItemClickListener(this::onGhazalClick);
            lstFavContent.setAdapter(favoriteContentAdapter);
        } else
            favoriteContentAdapter.notifyDataSetChanged();
    }

    private void sortFavouriteList(ArrayList<FavContentPageModel> favContentPageModels) {
        try {
            Collections.sort(favContentPageModels, new FavouriteComparator());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public class FavouriteComparator implements Comparator<FavContentPageModel> {
        @Override
        public int compare(FavContentPageModel favContentPageModel, FavContentPageModel t1) {
            try {
                SimpleDateFormat sdf_billdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                long f1 = sdf_billdate.parse(t1.getFM()).getTime();
                long f2 = sdf_billdate.parse(favContentPageModel.getFM()).getTime();
                if (f1 > f2) return 1;
                else if (f1 < f2) return -1;
                return 0;
            } catch (Exception e) {
                // TODO: handle exception
                return 0;
            }
        }
    }
    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        favoriteContentAdapter = null;
        updateUI();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        lazyRefreshContent();
    }

    private ContentType contentType;

    public ContentType getContentType() {
        if (contentType != null)
            return contentType;
        try {
            if (getArguments() != null) {
                return contentType = new ContentType(new JSONObject(getArguments().getString(CONTENT_TYPE_OBJ, "{}")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentType = new ContentType(null);
    }

    public final void lazyRefreshContent() {
        if (lstFavContent == null || favoriteContentAdapter == null)
            return;
        new Handler().postDelayed(this::updateUI, 300);
    }
}
