package com.example.sew.adapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;
import com.example.sew.models.CumulatedContentType;
import com.example.sew.models.ShayariContent;
import com.example.sew.models.SherContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TagGhazalAdapter extends BaseMyAdapter {
    private ArrayList<ShayariContent> shayariContents;

    public TagGhazalAdapter(BaseActivity activity, ArrayList<ShayariContent> tagGhazals) {
        super(activity);
        this.shayariContents = tagGhazals;
    }

    @Override
    public int getCount() {
        return shayariContents.size();
    }

    @Override
    public ShayariContent getItem(int position) {
        return shayariContents.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GhazalViewHolder ghazalViewHolder;
        ShayariContent shayariContent = getItem(position);
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_tag_ghazals);
            ghazalViewHolder = new GhazalViewHolder(convertView);
        } else
            ghazalViewHolder = (GhazalViewHolder) convertView.getTag();
        convertView.setTag(ghazalViewHolder);
        convertView.setTag(R.id.tag_data, shayariContent);
        ghazalViewHolder.txtPoetName.setText(shayariContent.getPoetName());
        ghazalViewHolder.offlineFavIcon.setTag(R.id.tag_data, shayariContent);
        ghazalViewHolder.txtPoetName.setTag(R.id.tag_data, shayariContent);
        ghazalViewHolder.poetaudioLink.setVisibility(shayariContent.isAudioAvailable() ? View.VISIBLE : View.GONE);
        ghazalViewHolder.poetyoutubeLink.setVisibility(shayariContent.isVideoAvailable() ? View.VISIBLE : View.GONE);
        ghazalViewHolder.poetPopularChoiceIcon.setVisibility(shayariContent.isPopularChoice() ? View.VISIBLE : View.GONE);
        ghazalViewHolder.poetEditorChoiceIcon.setVisibility(shayariContent.isEditorChoice() ? View.VISIBLE : View.GONE);
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                if (TextUtils.isEmpty(shayariContent.getTitleEng().trim()))
                    ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleUr());
                else
                    ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleEng());

                break;
            case HINDI:
                if (TextUtils.isEmpty(shayariContent.getTitleHin().trim()))
                    ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleUr());
                else
                    ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleHin());
                break;
            case URDU:
                ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleUr());
                break;
        }
        addFavoriteClick(ghazalViewHolder.offlineFavIcon, shayariContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
        updateFavoriteIcon(ghazalViewHolder.offlineFavIcon, shayariContent.getId());
        return convertView;
    }

    class GhazalViewHolder {
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        @BindView(R.id.ghazalTitle)
        TextView ghazalTitle;
        @BindView(R.id.poetEditorChoiceIcon)
        ImageView poetEditorChoiceIcon;
        @BindView(R.id.poetPopularChoiceIcon)
        ImageView poetPopularChoiceIcon;
        @BindView(R.id.poetaudioLink)
        ImageView poetaudioLink;
        @BindView(R.id.poetyoutubeLink)
        ImageView poetyoutubeLink;
        @BindView(R.id.txtPoetName)
        TextView txtPoetName;
        @OnClick(R.id.txtPoetName)
        void onPoetNameClick(View v){
            ShayariContent shayariContent = (ShayariContent) v.getTag(R.id.tag_data);
            getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), shayariContent.getPI()));
        }

        GhazalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
