package com.example.sew.models.home_view_holders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.adapters.HomeProseCollectionRecyclerAdapter;
import com.example.sew.adapters.HomeShayariCollectionRecyclerAdapter;
import com.example.sew.adapters.HomeShayariImageRecyclerAdapter;
import com.example.sew.adapters.HomeSherCollectionRecyclerAdapter;
import com.example.sew.adapters.HomeTopPoetRecyclerAdapter;
import com.example.sew.adapters.HomeVideoRecyclerAdapter;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.HomeProseCollection;
import com.example.sew.models.HomeShayari;
import com.example.sew.models.HomeShayariImage;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.models.HomeTopPoet;
import com.example.sew.models.HomeVideo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionTemplateViewHolder extends BaseHomeViewHolder {
    @BindView(R.id.txtCollectionTitle)
    TextView txtCollectionTitle;
    @BindView(R.id.rvCollection)
    RecyclerView rvCollection;

    public static CollectionTemplateViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        CollectionTemplateViewHolder collectionTemplateViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_collections_template, baseActivity);
            collectionTemplateViewHolder = new CollectionTemplateViewHolder(convertView, baseActivity);
        } else
            collectionTemplateViewHolder = (CollectionTemplateViewHolder) convertView.getTag();
        convertView.setTag(collectionTemplateViewHolder);
        collectionTemplateViewHolder.setConvertView(convertView);
        return collectionTemplateViewHolder;
    }

    private CollectionTemplateViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
        rvCollection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    public BaseHomeViewHolder loadTopPoets(ArrayList<HomeTopPoet> poets) {
        txtCollectionTitle.setText(MyHelper.getString(R.string.most_read_poets));
        if (rvCollection.getAdapter() == null)
            rvCollection.setAdapter(new HomeTopPoetRecyclerAdapter(getActivity(), poets));
        return this;
    }

    public BaseHomeViewHolder loadShayri(ArrayList<HomeShayari> shayaris) {
        txtCollectionTitle.setText(MyHelper.getString(R.string.shayari_collection));
        if (rvCollection.getAdapter() == null)
            rvCollection.setAdapter(new HomeShayariCollectionRecyclerAdapter(getActivity(), shayaris));
        return this;
    }

    public BaseHomeViewHolder loadProseCollection(ArrayList<HomeProseCollection> proseCollections) {
        txtCollectionTitle.setText(MyHelper.getString(R.string.prose_collections));
        if (rvCollection.getAdapter() == null)
            rvCollection.setAdapter(new HomeProseCollectionRecyclerAdapter(getActivity(), proseCollections));
        return this;

    }

    public BaseHomeViewHolder loadSherCollection(ArrayList<HomeSherCollection> sherCollections) {
        txtCollectionTitle.setText(MyHelper.getString(R.string.sher_collections));
        if (rvCollection.getAdapter() == null)
            rvCollection.setAdapter(new HomeSherCollectionRecyclerAdapter(getActivity(), sherCollections));
        return this;

    }

    public BaseHomeViewHolder loadMoreImageShayari(ArrayList<HomeShayariImage> shayariImages) {
        txtCollectionTitle.setText(MyHelper.getString(R.string.more_image_shayeri));
        if (rvCollection.getAdapter() == null)
            rvCollection.setAdapter(new HomeShayariImageRecyclerAdapter(getActivity(), shayariImages));
        return this;
    }

    public BaseHomeViewHolder loadMoreVideo(ArrayList<HomeVideo> videos) {
        txtCollectionTitle.setText(MyHelper.getString(R.string.more_video));
        if (rvCollection.getAdapter() == null)
            rvCollection.setAdapter(new HomeVideoRecyclerAdapter(getActivity(), videos));
        return this;

    }
}
