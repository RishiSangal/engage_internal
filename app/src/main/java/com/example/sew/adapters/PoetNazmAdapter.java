package com.example.sew.adapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.fragments.PoetNazmFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.ShayariContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoetNazmAdapter extends BasePoetContentAdapter {
    private ArrayList<ShayariContent> poetGhazals;
    private PoetDetail poetDetail;
    private ContentType contentType;
    private int totalContentCount;
    private PoetNazmFragment fragment;

    public PoetNazmAdapter(BaseActivity activity, ArrayList<ShayariContent> poetGhazals, PoetDetail poetDetail, ContentType contentType, PoetNazmFragment poetNazmFragment) {
        super(activity, poetDetail);
        this.poetGhazals = poetGhazals;
        this.poetDetail = poetDetail;
        this.contentType = contentType;
        this.fragment = poetNazmFragment;
    }

    public void setTotalContentCount(int totalContentCount) {
        this.totalContentCount = totalContentCount;
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
                PoetsProfileViewHolder poetsProfileViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.poet_detailed_header);
                    poetsProfileViewHolder = new PoetsProfileViewHolder(convertView);
                } else
                    poetsProfileViewHolder = (PoetsProfileViewHolder) convertView.getTag();
                convertView.setTag(poetsProfileViewHolder);
                loadDataForPoetHeader(poetsProfileViewHolder);
                break;
            case VIEW_TYPE_CONTENT:
                NazmViewHolder nazmViewHolder;
                ShayariContent shayariContent = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_poet_nazams);
                    nazmViewHolder = new NazmViewHolder(convertView);
                } else
                    nazmViewHolder = (NazmViewHolder) convertView.getTag();
                convertView.setTag(nazmViewHolder);
                convertView.setTag(R.id.tag_data, shayariContent);
                nazmViewHolder.offlineFavIcon.setTag(R.id.tag_data, shayariContent);
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
                nazmViewHolder.txtAuthor.setVisibility(View.GONE);
                addFavoriteClick(nazmViewHolder.offlineFavIcon, shayariContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(nazmViewHolder.offlineFavIcon, shayariContent.getId());
                break;
        }

        return convertView;
    }

    @Override
    String getContentTitle() {
        return contentType.getName().toUpperCase();
    }

    @Override
    String getContentCount() {
        return String.valueOf(totalContentCount);
    }

    ArrayList<String> sortContent;

    @Override
    public void contentFilter(View view) {
        sortContent = new ArrayList<>();
        sortContent.add(MyHelper.getString(R.string.popularity));
        if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH||MyService.getSelectedLanguage()== Enums.LANGUAGE.HINDI)
            sortContent.add(MyHelper.getString(R.string.alphabetic));
        // sortContent.add(MyHelper.getString(R.string.radeef));
        PopupMenu popup = new PopupMenu(getActivity(), view);
        for (int i = 0; i < sortContent.size(); i++) {
            popup.getMenu().add(R.id.menuGroup, R.id.group_detail, i, sortContent.get(i));
        }
        popup.setOnMenuItemClickListener(item -> {
            if (item.toString().equalsIgnoreCase(MyHelper.getString(R.string.popularity))) {
                fragment.sortContent(Enums.SORT_CONTENT.POPULARITY);
            } else if ((item.toString().equalsIgnoreCase(MyHelper.getString(R.string.alphabetic)))) {
                fragment.sortContent(Enums.SORT_CONTENT.ALPHABETIC);
            } else {
                fragment.sortContent(Enums.SORT_CONTENT.RADEEF);
            }
            return true;
        });
        popup.show();
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
        @BindView(R.id.poetNazmeditorchoice)
        ImageView poetNazmeditorchoice;
        @BindView(R.id.poetNazmpopularchoice)
        ImageView poetNazmpopularchoice;
        @BindView(R.id.poetNazmaudioLink)
        ImageView poetNazmaudioLink;
        @BindView(R.id.poetNazmyoutubeLink)
        ImageView poetNazmyoutubeLink;

        NazmViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
