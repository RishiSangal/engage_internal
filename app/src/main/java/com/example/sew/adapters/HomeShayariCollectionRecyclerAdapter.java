package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.ProseShayariCollectionActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.HomeShayari;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.models.home_view_holders.SeeMoreViewHolder;
import com.example.sew.views.SquareImageView;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sew.common.Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE;

public class HomeShayariCollectionRecyclerAdapter extends BaseRecyclerAdapter {
    private ArrayList<HomeShayari> shayaris;

    public HomeShayariCollectionRecyclerAdapter(BaseActivity activity, ArrayList<HomeShayari> shayaris) {
        super(activity);
        this.shayaris = shayaris;
    }


    @Override
    public int getItemViewType(int position) {
        return shayaris.size() == position ? ITEM_VIEW_SEE_MORE : ITEM_VIEW_CONTENT;
    }

    public HomeShayari getItem(int position) {
        return shayaris.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_SEE_MORE)
            return new SeeMoreViewHolder(getInflatedView(R.layout.cell_home_prose_see_more), SEE_MORE_SHAYARI);
        else
            return new SherCollectionHolder(getInflatedView(R.layout.cell_home_sher_shayari_prose_collection));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof SherCollectionHolder))
            return;
        HomeShayari shayari = getItem(position);
        SherCollectionHolder poetViewHolder = (SherCollectionHolder) holder;
        poetViewHolder.itemView.setTag(R.id.tag_data, shayari);
        poetViewHolder.txtCollectionTitle.setText(shayari.getName());
        poetViewHolder.txtFavoriteCount.setText(shayari.getFavoriteCount());
        poetViewHolder.txtShareCount.setText(shayari.getShareCount());
        if (MyHelper.convertToInt(shayari.getFavoriteCount()) == 0) {
            poetViewHolder.txtFavoriteCount.setVisibility(View.GONE);
            poetViewHolder.imgFavorite.setVisibility(View.GONE);
        } else {
            poetViewHolder.txtFavoriteCount.setVisibility(View.VISIBLE);
            poetViewHolder.imgFavorite.setVisibility(View.VISIBLE);
        }
        if (MyHelper.convertToInt(shayari.getShareCount()) == 0) {
            poetViewHolder.txtShareCount.setVisibility(View.GONE);
            poetViewHolder.imgShare.setVisibility(View.GONE);
        } else {
            poetViewHolder.txtShareCount.setVisibility(View.VISIBLE);
            poetViewHolder.imgShare.setVisibility(View.VISIBLE);
        }
        ImageHelper.setImage(poetViewHolder.imgCollectionImage, shayari.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_COLLECTION);
    }

    @Override
    public int getItemCount() {
        return shayaris.size() + 1;
    }

    class SherCollectionHolder extends BaseViewHolder {
        @BindView(R.id.imgCollectionImage)
        ImageView imgCollectionImage;
        @BindView(R.id.txtCollectionTitle)
        TitleTextViewType6 txtCollectionTitle;
        @BindView(R.id.imgFavorite)
        ImageView imgFavorite;
        @BindView(R.id.txtFavoriteCount)
        TitleTextViewType6 txtFavoriteCount;
        @BindView(R.id.imgShare)
        ImageView imgShare;
        @BindView(R.id.txtShareCount)
        TitleTextViewType6 txtShareCount;

        @OnClick()
        void onItemClick(View view) {
            HomeShayari homeShayari = (HomeShayari) view.getTag(R.id.tag_data);
            switch (homeShayari.getProseShayariCategory()) {
                case POET:
                    getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), homeShayari.getPoetId(), MyHelper.getContentById(homeShayari.getContentTypeId())));
                    break;
                case COLLECTION:
                    getActivity().startActivity(ProseShayariCollectionActivity.getInstance(getActivity(), homeShayari.getListId(), MyHelper.getContentById(homeShayari.getContentTypeId()),""));
                    break;
            }
        }

        SherCollectionHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
