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
import com.example.sew.models.ShayariContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TagNazmAdapter extends BaseMyAdapter {

    private ArrayList<ShayariContent> shayariContents;

    public TagNazmAdapter(BaseActivity activity, ArrayList<ShayariContent> tagNazms) {
        super(activity);
        this.shayariContents = tagNazms;
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
        NazmViewHolder nazmViewHolder;
        ShayariContent shayariContent = getItem(position);
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_tag_nazams);
            nazmViewHolder = new NazmViewHolder(convertView);
        } else
            nazmViewHolder = (NazmViewHolder) convertView.getTag();
        convertView.setTag(nazmViewHolder);
        convertView.setTag(R.id.tag_data, shayariContent);
        nazmViewHolder.offlineFavIcon.setTag(R.id.tag_data, shayariContent);
        nazmViewHolder.txtAuthor.setTag(R.id.tag_data, shayariContent);
        nazmViewHolder.poetNazmaudioLink.setVisibility(shayariContent.isAudioAvailable() ? View.VISIBLE : View.GONE);
        nazmViewHolder.poetNazmyoutubeLink.setVisibility(shayariContent.isVideoAvailable() ? View.VISIBLE : View.GONE);
        nazmViewHolder.poetNazmpopularchoice.setVisibility(shayariContent.isPopularChoice() ? View.VISIBLE : View.GONE);
        nazmViewHolder.poetNazmeditorchoice.setVisibility(shayariContent.isEditorChoice() ? View.VISIBLE : View.GONE);
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                if (TextUtils.isEmpty(shayariContent.getTitleEng().trim())) {
                    nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleUr());
                    nazmViewHolder.txtNazamTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
                } else {
                    nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleEng());
                    nazmViewHolder.txtNazamTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
                }

                if (TextUtils.isEmpty(shayariContent.getSeriesEng().trim())) {
                    nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesUr());
                    nazmViewHolder.txtNazamSubTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
                } else {
                    nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesEng());
                    nazmViewHolder.txtNazamSubTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
                }
                //  nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleEng());
                // nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesEng());
                nazmViewHolder.txtAuthor.setText(shayariContent.getPoetNameEng());
                nazmViewHolder.txtNazamTitle.setTextSize(14);
                nazmViewHolder.txtNazamSubTitle.setTextSize(14);
                nazmViewHolder.txtAuthor.setTextSize(10);
                break;
            case HINDI:
                if (TextUtils.isEmpty(shayariContent.getTitleHin().trim())) {
                    nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleUr());
                    nazmViewHolder.txtNazamTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
                } else {
                    nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleHin());
                    nazmViewHolder.txtNazamTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
                }
                if (TextUtils.isEmpty(shayariContent.getSeriesHin().trim())) {
                    nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesUr());
                    nazmViewHolder.txtNazamSubTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
                } else {
                    nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesHin());
                    nazmViewHolder.txtNazamSubTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
                }
                //nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleHin());
                //nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesHin());
                nazmViewHolder.txtAuthor.setText(shayariContent.getPoetNameHin());
                nazmViewHolder.txtNazamTitle.setTextSize(14);
                nazmViewHolder.txtNazamSubTitle.setTextSize(14);
                nazmViewHolder.txtAuthor.setTextSize(10);
                break;
            case URDU:
                nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleUr());
                nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesUr());
                nazmViewHolder.txtAuthor.setText(shayariContent.getPoetNameUR());
                nazmViewHolder.txtNazamTitle.setTextSize(18);
                nazmViewHolder.txtNazamSubTitle.setTextSize(16);
                nazmViewHolder.txtAuthor.setTextSize(14);
                break;
        }
        nazmViewHolder.txtAuthor.setVisibility(View.VISIBLE);
        addFavoriteClick(nazmViewHolder.offlineFavIcon, shayariContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
        updateFavoriteIcon(nazmViewHolder.offlineFavIcon, shayariContent.getId());

        return convertView;
    }

     class NazmViewHolder {
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        @BindView(R.id.txtNazamTitle)
        TextView txtNazamTitle;
        @BindView(R.id.txtNazamSubTitle)
        TextView txtNazamSubTitle;
        @BindView(R.id.poetNazmeditorchoice)
        ImageView poetNazmeditorchoice;
        @BindView(R.id.poetNazmpopularchoice)
        ImageView poetNazmpopularchoice;
        @BindView(R.id.poetNazmaudioLink)
        ImageView poetNazmaudioLink;
        @BindView(R.id.poetNazmyoutubeLink)
        ImageView poetNazmyoutubeLink;
        @BindView(R.id.txtAuthor)
        TextView txtAuthor;

        @OnClick(R.id.txtAuthor)
        void onPoetNameClick(View view) {
            ShayariContent shayariContent= (ShayariContent)view.getTag(R.id.tag_data);
            getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), shayariContent.getPI()));
        }

        NazmViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
