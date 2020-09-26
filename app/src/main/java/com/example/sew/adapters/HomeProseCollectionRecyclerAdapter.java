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
import com.example.sew.models.HomeProseCollection;
import com.example.sew.models.home_view_holders.SeeMoreViewHolder;
import com.example.sew.views.SquareImageView;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sew.common.Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE;

public class HomeProseCollectionRecyclerAdapter extends BaseRecyclerAdapter {
    private ArrayList<HomeProseCollection> proseCollections;


    public HomeProseCollectionRecyclerAdapter(BaseActivity activity, ArrayList<HomeProseCollection> proseCollections) {
        super(activity);
        this.proseCollections = proseCollections;
    }

    @Override
    public int getItemViewType(int position) {
        return proseCollections.size() == position ? ITEM_VIEW_SEE_MORE : ITEM_VIEW_CONTENT;
    }

    public HomeProseCollection getItem(int position) {
        return proseCollections.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_SEE_MORE)
            return new SeeMoreViewHolder(getInflatedView(R.layout.cell_home_prose_see_more), SEE_MORE_PROSE);
        else
            return new ProseCollectionHolder(getInflatedView(R.layout.cell_home_sher_shayari_prose_collection));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof ProseCollectionHolder))
            return;
        HomeProseCollection proseCollection = getItem(position);
        ProseCollectionHolder proseCollectionHolder = (ProseCollectionHolder) holder;
        proseCollectionHolder.itemView.setTag(R.id.tag_data, proseCollection);
        proseCollectionHolder.txtCollectionTitle.setText(proseCollection.getName());
        proseCollectionHolder.txtFavoriteCount.setText(proseCollection.getFavoriteCount());
        proseCollectionHolder.txtShareCount.setText(proseCollection.getShareCount());
        if (MyHelper.convertToInt(proseCollection.getFavoriteCount()) == 0) {
            proseCollectionHolder.txtFavoriteCount.setVisibility(View.GONE);
            proseCollectionHolder.imgFavorite.setVisibility(View.GONE);
        } else {
            proseCollectionHolder.txtFavoriteCount.setVisibility(View.VISIBLE);
            proseCollectionHolder.imgFavorite.setVisibility(View.VISIBLE);
        }
        if (MyHelper.convertToInt(proseCollection.getShareCount()) == 0) {
            proseCollectionHolder.txtShareCount.setVisibility(View.GONE);
            proseCollectionHolder.imgShare.setVisibility(View.GONE);
        } else {
            proseCollectionHolder.txtShareCount.setVisibility(View.VISIBLE);
            proseCollectionHolder.imgShare.setVisibility(View.VISIBLE);
        }
        ImageHelper.setImage(proseCollectionHolder.imgCollectionImage, proseCollection.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_COLLECTION);
    }

    @Override
    public int getItemCount() {
        return proseCollections.size() + 1;
    }

    class ProseCollectionHolder extends BaseViewHolder {
        @BindView(R.id.imgCollectionImage)
        SquareImageView imgCollectionImage;
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
            HomeProseCollection proseCollection = (HomeProseCollection) view.getTag(R.id.tag_data);
            switch (proseCollection.getProseShayariCategory()) {
                case POET:
                    getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), proseCollection.getPoetId(), MyHelper.getContentById(proseCollection.getContentTypeId())));
                    break;
                case COLLECTION:
                    getActivity().startActivity(ProseShayariCollectionActivity.getInstance(getActivity(), proseCollection.getId(), MyHelper.getContentById(proseCollection.getContentTypeId()),proseCollection.getsEO_Slug()));
                    break;
            }
        }

        ProseCollectionHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
