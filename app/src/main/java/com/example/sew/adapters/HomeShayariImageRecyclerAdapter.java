package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.ShayariImageDetailActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.models.HomeShayariImage;
import com.example.sew.models.ShayariImage;
import com.example.sew.models.home_view_holders.SeeMoreViewHolder;
import com.example.sew.views.SquareImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeShayariImageRecyclerAdapter extends BaseRecyclerAdapter {

    private ArrayList<HomeShayariImage> shayariImages;

    public HomeShayariImageRecyclerAdapter(BaseActivity activity, ArrayList<HomeShayariImage> shayariImages) {
        super(activity);
        this.shayariImages = shayariImages;
    }


    public HomeShayariImage getItem(int position) {
        return shayariImages.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return shayariImages.size() == position ? ITEM_VIEW_SEE_MORE : ITEM_VIEW_CONTENT;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_SEE_MORE)
            return new SeeMoreViewHolder(getInflatedView(R.layout.cell_home_image_see_more), SEE_MORE_IMAGE);
        else
            return new ShayariImageViewHolder(getInflatedView(R.layout.cell_home_more_image_shayari_item));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof ShayariImageViewHolder))
            return;
        HomeShayariImage homeShayariImage = getItem(position);
        ShayariImageViewHolder poetViewHolder = (ShayariImageViewHolder) holder;
        poetViewHolder.itemView.setTag(R.id.tag_data, homeShayariImage);
        ImageHelper.setImage(poetViewHolder.imgShayariImage, homeShayariImage.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE);
    }

    @Override
    public int getItemCount() {
        return shayariImages.size() + 1;
    }

    class ShayariImageViewHolder extends BaseViewHolder {
        @BindView(R.id.imgShayariImage)
        ImageView imgShayariImage;
        @BindView(R.id.txtMoreImageShayari)
        TextView txtMoreImageShayari;

        @OnClick()
        void onItemClick(View view) {
            HomeShayariImage homeShayariImage = (HomeShayariImage) view.getTag(R.id.tag_data);
            ShayariImage shayariImage = new ShayariImage(homeShayariImage.getJsonObject());
            getActivity().startActivity(ShayariImageDetailActivity.getInstance(getActivity(), shayariImage));
//            HomeTopPoet topPoet = (HomeTopPoet) view.getTag(R.id.tag_data);
//            getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), topPoet.getEntityId()));
        }

        ShayariImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
