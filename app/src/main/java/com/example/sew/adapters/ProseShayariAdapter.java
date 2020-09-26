package com.example.sew.adapters;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;
import com.example.sew.models.ShayariContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProseShayariAdapter extends BaseMyAdapter {
    private ArrayList<ShayariContent> poetNazams;
    final int VIEW_TYPE_HEADER = 0;
    final int VIEW_TYPE_CONTENT_GHAZAL_NAZM = 1;
    private String title, description;
    private ContentType contentType;

    public ProseShayariAdapter(BaseActivity activity, ArrayList<ShayariContent> poetNazams, ContentType contentType) {
        super(activity);
        this.poetNazams = poetNazams;
        this.contentType = contentType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getCount() {
        return poetNazams.size() + 1;
    }

    @Override
    public ShayariContent getItem(int position) {
        return poetNazams.get(position - 1);
    }

    @SuppressLint("WrongConstant")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                CommonViewHolder commonViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_prose_shayari_collection_content_haeder);
                    commonViewHolder = new CommonViewHolder(convertView);
                } else
                    commonViewHolder = (CommonViewHolder) convertView.getTag();
                convertView.setTag(commonViewHolder);
                commonViewHolder.txtTitle.setText(title);
                commonViewHolder.txtDescription.setText(description);
                if (TextUtils.isEmpty(title))
                    commonViewHolder.txtTitle.setVisibility(View.GONE);
                else
                    commonViewHolder.txtTitle.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(description))
                    commonViewHolder.txtDescription.setVisibility(View.GONE);
                else
                    commonViewHolder.txtDescription.setVisibility(View.VISIBLE);
                break;
            case VIEW_TYPE_CONTENT_GHAZAL_NAZM: {
                NazmAdapter.NazmViewHolder nazmViewHolder;
                ShayariContent shayariContent = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_nazams);
                    nazmViewHolder = new NazmAdapter.NazmViewHolder(convertView);
                } else
                    nazmViewHolder = (NazmAdapter.NazmViewHolder) convertView.getTag();
                convertView.setTag(nazmViewHolder);
                convertView.setTag(R.id.tag_data, shayariContent);
                nazmViewHolder.offlineFavIcon.setTag(R.id.tag_data, shayariContent);
                addFavoriteClick(nazmViewHolder.offlineFavIcon, shayariContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(nazmViewHolder.offlineFavIcon, shayariContent.getId());
                nazmViewHolder.poetaudioLink.setVisibility(shayariContent.isAudioAvailable() ? View.VISIBLE : View.GONE);
                nazmViewHolder.poetyoutubeLink.setVisibility(shayariContent.isVideoAvailable() ? View.VISIBLE : View.GONE);
                if (contentType.getContentId().equalsIgnoreCase(MyConstants.NAZM_ID))
                    nazmViewHolder.txtNazamSubTitle.setVisibility(View.VISIBLE);
                else
                    nazmViewHolder.txtNazamSubTitle.setVisibility(View.GONE);
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
                        break;
                    case URDU:
                        nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitleUr());
                        nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeriesUr());
                        break;
                }
                //nazmViewHolder.txtNazamTitle.setText(shayariContent.getTitle());
                //  nazmViewHolder.txtNazamSubTitle.setText(shayariContent.getSeries());
                nazmViewHolder.txtAuthor.setText(shayariContent.getPoetName());
                // nazmViewHolder.txtNazamTitle.setVisibility(TextUtils.isEmpty(shayariContent.getTitle()) ? View.GONE : View.VISIBLE);
                // nazmViewHolder.txtNazamSubTitle.setVisibility(TextUtils.isEmpty(shayariContent.getSeries()) ? View.GONE : View.VISIBLE);
                nazmViewHolder.txtAuthor.setVisibility(TextUtils.isEmpty(shayariContent.getPoetName()) ? View.GONE : View.VISIBLE);
                break;
            }

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

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT_GHAZAL_NAZM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class CommonViewHolder {
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtDescription)
        TextView txtDescription;

        CommonViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
