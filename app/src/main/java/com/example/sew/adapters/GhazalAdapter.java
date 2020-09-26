package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ShayariContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GhazalAdapter extends BaseGhazalContentAdapter {
    private ArrayList<ShayariContent> poetGhazals;
    String targetId;

    public GhazalAdapter(BaseActivity activity, ArrayList<ShayariContent> poetGhazals, String targetId) {
        super(activity, targetId);
        this.poetGhazals = poetGhazals;
        this.targetId = targetId;
    }

    @Override
    public int getCount() {
        return poetGhazals.size() + 1;
    }

    @Override
    public ShayariContent getItem(int position) {
        return poetGhazals.get(position - 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                GhazalHeaderViewHolder ghazalHeaderViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_ghazal_haeder);
                    ghazalHeaderViewHolder = new GhazalHeaderViewHolder(convertView);
                } else
                    ghazalHeaderViewHolder = (GhazalHeaderViewHolder) convertView.getTag();
                convertView.setTag(ghazalHeaderViewHolder);
                loadDataForGhazalHeader(ghazalHeaderViewHolder);
                break;
            case VIEW_TYPE_CONTENT:
                GhazalViewHolder ghazalViewHolder;
                ShayariContent shayariContent = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_ghazals);
                    ghazalViewHolder = new GhazalViewHolder(convertView);
                } else
                    ghazalViewHolder = (GhazalViewHolder) convertView.getTag();
                convertView.setTag(ghazalViewHolder);
                convertView.setTag(R.id.tag_data, shayariContent);
                ghazalViewHolder.poetaudioLink.setVisibility(shayariContent.isAudioAvailable() ? View.VISIBLE : View.GONE);
                ghazalViewHolder.poetyoutubeLink.setVisibility(shayariContent.isVideoAvailable() ? View.VISIBLE : View.GONE);

                addFavoriteClick(ghazalViewHolder.offlineFavIcon, shayariContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(ghazalViewHolder.offlineFavIcon, shayariContent.getId());
//                ghazalViewHolder.poetPopularChoiceIcon.setVisibility(shayariContent.isPopularChoice() ? View.VISIBLE : View.GONE);
//                ghazalViewHolder.poetEditorChoiceIcon.setVisibility(shayariContent.isEditorChoice() ? View.VISIBLE : View.GONE);
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleEng());
                        ghazalViewHolder.ghazalPoet.setText(shayariContent.getPoetNameEng().toUpperCase());
                        break;
                    case HINDI:
                        ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleHin());
                        ghazalViewHolder.ghazalPoet.setText(shayariContent.getPoetNameHin().toUpperCase());
                        break;
                    case URDU:
                        ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleUr());
                        ghazalViewHolder.ghazalPoet.setText(shayariContent.getPoetNameUR().toUpperCase());
                        break;
                }
                break;
        }

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
