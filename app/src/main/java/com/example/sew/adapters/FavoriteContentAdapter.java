package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.models.FavContentPageModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteContentAdapter extends BaseMyAdapter {
    private ArrayList<FavContentPageModel> favContentPageModels;

    public FavoriteContentAdapter(BaseActivity activity, ArrayList<FavContentPageModel> favContentPageModels) {
        super(activity);
        this.favContentPageModels = favContentPageModels;
    }

    @Override
    public int getCount() {
        return favContentPageModels.size();
    }

    @Override
    public FavContentPageModel getItem(int position) {
        return favContentPageModels.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        GhazalViewHolder ghazalViewHolder;
        FavContentPageModel favContentPageModel = getItem(position);
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_ghazals);
            ghazalViewHolder = new GhazalViewHolder(convertView);
        } else
            ghazalViewHolder = (GhazalViewHolder) convertView.getTag();
        convertView.setTag(ghazalViewHolder);
        convertView.setTag(R.id.tag_data, favContentPageModel);
        ghazalViewHolder.offlineFavIcon.setTag(R.id.tag_data, favContentPageModel);
        ghazalViewHolder.poetaudioLink.setVisibility(View.GONE);
        ghazalViewHolder.poetyoutubeLink.setVisibility(View.GONE);
        ghazalViewHolder.ghazalTitle.setText(favContentPageModel.getTitle());
        ghazalViewHolder.ghazalPoet.setText(favContentPageModel.getPoetName().toUpperCase());
        addFavoriteClick(ghazalViewHolder.offlineFavIcon, favContentPageModel.getId(), Enums.FAV_TYPES.CONTENT.getKey());
        updateFavoriteIcon(ghazalViewHolder.offlineFavIcon, favContentPageModel.getId());
        return convertView;
    }

    static class GhazalViewHolder {
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        @BindView(R.id.ghazalTitle)
        TextView ghazalTitle;
        @BindView(R.id.ghazalPoet)
        TextView ghazalPoet;
        @BindView(R.id.poetEditorChoiceIcon)
        ImageView poetEditorChoiceIcon;
        @BindView(R.id.poetPopularChoiceIcon)
        ImageView poetPopularChoiceIcon;
        @BindView(R.id.poetaudioLink)
        ImageView poetaudioLink;
        @BindView(R.id.poetyoutubeLink)
        ImageView poetyoutubeLink;

        GhazalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
