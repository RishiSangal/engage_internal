package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.models.ShayariImage;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class FavoriteShayariImageAdapter extends BaseRecyclerAdapter {
    private ArrayList<ShayariImage> shayariImages, selectedShayariImages;

    public FavoriteShayariImageAdapter(BaseActivity activity, ArrayList<ShayariImage> shayariImages, ArrayList<ShayariImage> selectedShayariImages) {
        super(activity);
        this.shayariImages = shayariImages;
        this.selectedShayariImages = selectedShayariImages;
    }

    @Override
    public int getItemCount() {
        return shayariImages.size();
    }

    public ShayariImage getItem(int position) {
        return shayariImages.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShayariImageViewHolder(getInflatedView(R.layout.cell_favorite_shayari_image, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof ShayariImageViewHolder))
            return;
        ShayariImageViewHolder shayariImageViewHolder = (ShayariImageViewHolder) holder;
        ShayariImage shayariImage = getItem(position);
        shayariImageViewHolder.itemView.setTag(R.id.tag_data, shayariImage);
        ImageHelper.setImage(shayariImageViewHolder.imgShayariImage, shayariImage.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE);
        if (!CollectionUtils.isEmpty(selectedShayariImages) && selectedShayariImages.contains(shayariImage))
            shayariImageViewHolder.viewSelection.setVisibility(View.VISIBLE);
        else
            shayariImageViewHolder.viewSelection.setVisibility(View.GONE);
    }


    private View.OnClickListener onItemClickListener;
    private View.OnLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(View.OnLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    class ShayariImageViewHolder extends BaseViewHolder {
        @BindView(R.id.imgShayariImage)
        ImageView imgShayariImage;
        @BindView((R.id.viewSelection))
        View viewSelection;

        @OnClick()
        void onItemClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onClick(view);
        }

        @OnLongClick()
        boolean onItemLongClick(View view) {
            if (onItemLongClickListener != null)
                return onItemLongClickListener.onLongClick(view);
            return false;
        }

        ShayariImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
