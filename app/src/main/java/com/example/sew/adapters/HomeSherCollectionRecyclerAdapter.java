package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.ShayariActivity;
import com.example.sew.activities.SherCollectionActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.models.HomeTopPoet;
import com.example.sew.models.home_view_holders.SeeMoreViewHolder;
import com.example.sew.views.SquareImageView;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sew.common.Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE;

public class HomeSherCollectionRecyclerAdapter extends BaseRecyclerAdapter {
    private ArrayList<HomeSherCollection> sherCollections;

    public HomeSherCollectionRecyclerAdapter(BaseActivity activity, ArrayList<HomeSherCollection> sherCollections) {
        super(activity);
        this.sherCollections = sherCollections;
    }


    public HomeSherCollection getItem(int position) {
        return sherCollections.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return sherCollections.size() == position ? ITEM_VIEW_SEE_MORE : ITEM_VIEW_CONTENT;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_SEE_MORE)
            return new SeeMoreViewHolder(getInflatedView(R.layout.cell_home_prose_see_more), SEE_MORE_SHER);
        else
            return new SherCollectionHolder(getInflatedView(R.layout.cell_home_sher_shayari_prose_collection));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof SherCollectionHolder))
            return;
        HomeSherCollection sherCollection = getItem(position);
        SherCollectionHolder poetViewHolder = (SherCollectionHolder) holder;
        poetViewHolder.itemView.setTag(R.id.tag_data, sherCollection);
        poetViewHolder.txtCollectionTitle.setText(sherCollection.getName());
        poetViewHolder.txtFavoriteCount.setText(sherCollection.getFavoriteCount());
        poetViewHolder.txtShareCount.setText(sherCollection.getShareCount());
        if (MyHelper.convertToInt(sherCollection.getFavoriteCount()) == 0) {
            poetViewHolder.txtFavoriteCount.setVisibility(View.GONE);
            poetViewHolder.imgFavorite.setVisibility(View.GONE);
        } else {
            poetViewHolder.txtFavoriteCount.setVisibility(View.VISIBLE);
            poetViewHolder.imgFavorite.setVisibility(View.VISIBLE);
        }
        if (MyHelper.convertToInt(sherCollection.getShareCount()) == 0) {
            poetViewHolder.txtShareCount.setVisibility(View.GONE);
            poetViewHolder.imgShare.setVisibility(View.GONE);
        } else {
            poetViewHolder.txtShareCount.setVisibility(View.VISIBLE);
            poetViewHolder.imgShare.setVisibility(View.VISIBLE);
        }
        ImageHelper.setImage(poetViewHolder.imgCollectionImage, sherCollection.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_COLLECTION);
    }

    @Override
    public int getItemCount() {
        return sherCollections.size() + 1;
    }

    class SherCollectionHolder extends BaseViewHolder {
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
            HomeSherCollection sherCollection = (HomeSherCollection) view.getTag(R.id.tag_data);
            if(sherCollection.getSherCollectionType()== Enums.SHER_COLLECTION_TYPE.TOP_20)
                getActivity().startActivity(SherCollectionActivity.getInstance(getActivity(), sherCollection));
            else
                getActivity().startActivity(SherTagOccasionActivity.getInstance(getActivity(), sherCollection));
        }

        SherCollectionHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
