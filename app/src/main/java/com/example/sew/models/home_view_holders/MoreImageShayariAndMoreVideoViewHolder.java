package com.example.sew.models.home_view_holders;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.adapters.HomeShayariImageRecyclerAdapter;
import com.example.sew.adapters.HomeVideoRecyclerAdapter;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.HomeShayariImage;
import com.example.sew.models.HomeVideo;
import com.example.sew.models.ShayariImage;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreImageShayariAndMoreVideoViewHolder extends BaseHomeViewHolder {

    @BindView(R.id.txtCollectionTitle)
    TitleTextViewType6 txtCollectionTitle;
    @BindView(R.id.rvCollection)
    RecyclerView rvCollection;

    private MoreImageShayariAndMoreVideoViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
        rvCollection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    public static MoreImageShayariAndMoreVideoViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        MoreImageShayariAndMoreVideoViewHolder moreImageShayariAndMoreVideoViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_more_image_shayri, baseActivity);
            moreImageShayariAndMoreVideoViewHolder = new MoreImageShayariAndMoreVideoViewHolder(convertView, baseActivity);
        } else
            moreImageShayariAndMoreVideoViewHolder = (MoreImageShayariAndMoreVideoViewHolder) convertView.getTag();
        moreImageShayariAndMoreVideoViewHolder.setConvertView(convertView);
        convertView.setTag(moreImageShayariAndMoreVideoViewHolder);
        return moreImageShayariAndMoreVideoViewHolder;
    }

    ArrayList<ShayariImage> commonShayariImages = new ArrayList<>();

    public BaseHomeViewHolder loadMoreImageShayari(ArrayList<HomeShayariImage> shayariImages) {
        txtCollectionTitle.setText(MyHelper.getString(R.string.more_image_shayeri));
        if (rvCollection.getAdapter() == null) {
            for (int i = 0; i < shayariImages.size(); i++) {
                ShayariImage shayariImage = new ShayariImage(shayariImages.get(i).getJsonObject());
                commonShayariImages.add(shayariImage);
            }
            rvCollection.setAdapter(new HomeShayariImageRecyclerAdapter(getActivity(), commonShayariImages, shayariImages));
        }
        return this;
    }

    public BaseHomeViewHolder loadMoreVideo(ArrayList<HomeVideo> videos) {
        txtCollectionTitle.setText(MyHelper.getString(R.string.more_video));
        if (rvCollection.getAdapter() == null)
            rvCollection.setAdapter(new HomeVideoRecyclerAdapter(getActivity(), videos));
        return this;

    }
}
