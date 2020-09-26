package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ShayariContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NazmAdapter extends BaseNazmContentAdapter {
    private ArrayList<ShayariContent> poetNazams;

    public NazmAdapter(BaseActivity activity, ArrayList<ShayariContent> poetNazams, String targetId) {
        super(activity, targetId);
        this.poetNazams = poetNazams;
    }

    @Override
    public int getCount() {
        return poetNazams.size() + 1;
    }

    @Override
    public ShayariContent getItem(int position) {
        return poetNazams.get(position - 1);
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
                NazmViewHolder nazmViewHolder;
                ShayariContent shayariContent = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_nazams);
                    nazmViewHolder = new NazmViewHolder(convertView);
                } else
                    nazmViewHolder = (NazmViewHolder) convertView.getTag();
                convertView.setTag(nazmViewHolder);
                convertView.setTag(R.id.tag_data, shayariContent);
                nazmViewHolder.poetaudioLink.setVisibility(shayariContent.isAudioAvailable() ? View.VISIBLE : View.GONE);
                nazmViewHolder.poetyoutubeLink.setVisibility(shayariContent.isVideoAvailable() ? View.VISIBLE : View.GONE);
                addFavoriteClick(nazmViewHolder.offlineFavIcon, shayariContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(nazmViewHolder.offlineFavIcon, shayariContent.getId());
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleEng());
                        nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesEng());
                        nazmViewHolder.txtAuthor.setText(shayariContent.getPoetNameEng());
                        break;
                    case HINDI:
                        nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleHin());
                        nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesHin());
                        nazmViewHolder.txtAuthor.setText(shayariContent.getPoetNameHin());
                        break;
                    case URDU:
                        nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleUr());
                        nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesUr());
                        nazmViewHolder.txtAuthor.setText(shayariContent.getPoetNameUR());
                        break;
                }

                break;
        }

        return convertView;
    }

    static class NazmViewHolder {
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        @BindView(R.id.txtNazamTitle)
        TextView txtNazamTitle;
        @BindView(R.id.txtNazamSubTitle)
        TextView txtNazamSubTitle;
        @BindView(R.id.txtAuthor)
        TextView txtAuthor;
        @BindView(R.id.poetEditorChoiceIcon)
        ImageView poetEditorChoiceIcon;
        @BindView(R.id.poetPopularChoiceIcon)
        ImageView poetPopularChoiceIcon;
        @BindView(R.id.poetaudioLink)
        ImageView poetaudioLink;
        @BindView(R.id.poetyoutubeLink)
        ImageView poetyoutubeLink;

        NazmViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static
    class ViewHolder {
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        @BindView(R.id.txtNazamTitle)
        TextView txtNazamTitle;
        @BindView(R.id.txtNazamSubTitle)
        TextView txtNazamSubTitle;
        @BindView(R.id.txtAuthor)
        TextView txtAuthor;
        @BindView(R.id.poetEditorChoiceIcon)
        ImageView poetEditorChoiceIcon;
        @BindView(R.id.poetPopularChoiceIcon)
        ImageView poetPopularChoiceIcon;
        @BindView(R.id.poetaudioLink)
        ImageView poetaudioLink;
        @BindView(R.id.poetyoutubeLink)
        ImageView poetyoutubeLink;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;
        @BindView(R.id.poetList_layout)
        RelativeLayout poetListLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
