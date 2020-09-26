package com.example.sew.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.widget.AutoSizeableTextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SearchContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.example.sew.common.MyConstants.GHAZAL_ID;
import static com.example.sew.common.MyConstants.NAZM_ID;
import static com.example.sew.common.MyConstants.SHER_ID;

public class SearchGhazalNazmSherAdapter extends BaseMyAdapter {

    private ArrayList<SearchContent> searchContents;
    private final int VIEW_TYPE_GHAZAL = 0;
    private final int VIEW_TYPE_NAZM = 1;
    private final int VIEW_TYPE_SHER = 2;

    public SearchGhazalNazmSherAdapter(BaseActivity activity, ArrayList<SearchContent> searchContents) {
        super(activity);
        this.searchContents = searchContents;
    }

    @Override
    public int getCount() {
        return searchContents.size();
    }

    @Override
    public SearchContent getItem(int position) {
        return searchContents.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (GHAZAL_ID.equalsIgnoreCase(getItem(position).getTypeId()))
            return VIEW_TYPE_GHAZAL;
        else if (NAZM_ID.equalsIgnoreCase(getItem(position).getTypeId()))
            return VIEW_TYPE_NAZM;
        else if (SHER_ID.equalsIgnoreCase(getItem(position).getTypeId()))
            return VIEW_TYPE_SHER;
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        SearchContent searchContent = getItem(position);
        switch (getItemViewType(position)) {
            case VIEW_TYPE_GHAZAL: {
                GhazalViewHolder ghazalViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_ghazals);
                    ghazalViewHolder = new GhazalViewHolder(convertView);
                } else
                    ghazalViewHolder = (GhazalViewHolder) convertView.getTag();
                convertView.setTag(ghazalViewHolder);
                convertView.setTag(R.id.tag_data, searchContent);
                addFavoriteClick(ghazalViewHolder.offlineFavIcon, searchContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(ghazalViewHolder.offlineFavIcon, searchContent.getId());
                ghazalViewHolder.poetaudioLink.setVisibility(searchContent.getAudioCountInt() > 0 ? View.VISIBLE : View.GONE);
                ghazalViewHolder.poetyoutubeLink.setVisibility(searchContent.getVideoCountInt() > 0 ? View.VISIBLE : View.GONE);
                ghazalViewHolder.poetPopularChoiceIcon.setVisibility(searchContent.isPopularChoice() ? View.VISIBLE : View.GONE);
                ghazalViewHolder.poetEditorChoiceIcon.setVisibility(searchContent.isEditorChoice() ? View.VISIBLE : View.GONE);
                ghazalViewHolder.ghazalTitleSecond.setVisibility(View.VISIBLE);
                String bodyData = searchContent.getBody().replace("<br/>", "\n");
                //ghazalViewHolder.ghazalTitle.setText(bodyData);
                if (bodyData.contains("\r\n")) {
                    String[] separated = bodyData.split("\r\n");
                    String firstLine = separated[0];
                    String lastLine = separated[1];
                    ghazalViewHolder.ghazalTitle.setText(firstLine);
                    ghazalViewHolder.ghazalTitleSecond.setText(lastLine);
                    ghazalViewHolder.ghazalTitle.setMaxLines(1);
                    ghazalViewHolder.ghazalTitleSecond.setMaxLines(1);
                    ghazalViewHolder.ghazalTitleSecond.setVisibility(View.VISIBLE);
                } else {
                    ghazalViewHolder.ghazalTitle.setText(bodyData);
                    ghazalViewHolder.ghazalTitle.setMaxLines(1);
                    ghazalViewHolder.ghazalTitleSecond.setVisibility(View.GONE);
                }

                ghazalViewHolder.ghazalPoet.setText(searchContent.getPoetName().toUpperCase());
            }
            break;
            case VIEW_TYPE_NAZM: {
                NazmViewHolder nazmViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_nazams);
                    nazmViewHolder = new NazmViewHolder(convertView);
                } else
                    nazmViewHolder = (NazmViewHolder) convertView.getTag();
                convertView.setTag(nazmViewHolder);
                convertView.setTag(R.id.tag_data, searchContent);

                addFavoriteClick(nazmViewHolder.offlineFavIcon, searchContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(nazmViewHolder.offlineFavIcon, searchContent.getId());
                nazmViewHolder.poetaudioLink.setVisibility(searchContent.getAudioCountInt() > 0 ? View.VISIBLE : View.GONE);
                nazmViewHolder.poetyoutubeLink.setVisibility(searchContent.getVideoCountInt() > 0 ? View.VISIBLE : View.GONE);
                nazmViewHolder.poetPopularChoiceIcon.setVisibility(searchContent.isPopularChoice() ? View.VISIBLE : View.GONE);
                nazmViewHolder.poetEditorChoiceIcon.setVisibility(searchContent.isEditorChoice() ? View.VISIBLE : View.GONE);
                String bodyData = searchContent.getBody().replace("<br/>", "\n");
                nazmViewHolder.txtNazamTitle.setText(searchContent.getTitle());
                if (bodyData.contains("\r\n")) {
                    String[] separated = bodyData.split("\r\n");
                    String firstLine = separated[0];
                    String lastLine = separated[1];
                    nazmViewHolder.txtNazamSubTitle.setText(firstLine);
                    nazmViewHolder.txtNazamSubTitleSecond.setText(lastLine);
                    nazmViewHolder.txtNazamSubTitleSecond.setVisibility(View.VISIBLE);
                } else {
                    nazmViewHolder.txtNazamSubTitle.setText(bodyData);
                    nazmViewHolder.txtNazamSubTitleSecond.setVisibility(View.GONE);
                }
                nazmViewHolder.txtAuthor.setText(searchContent.getPoetName());
                nazmViewHolder.txtNazamSubTitleSecond.setVisibility(View.VISIBLE);

                if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH||MyService.getSelectedLanguage()== Enums.LANGUAGE.HINDI){
                    nazmViewHolder.offlineFavIcon.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }else
                    nazmViewHolder.offlineFavIcon.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                break;
            }
            case VIEW_TYPE_SHER: {
                SherViewHolder sherViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_search_shers);
                    sherViewHolder = new SherViewHolder(convertView);
                } else
                    sherViewHolder = (SherViewHolder) convertView.getTag();
                convertView.setTag(sherViewHolder);
                convertView.setTag(R.id.tag_data, searchContent);
                sherViewHolder.sherClipbordIcon.setTag(R.id.tag_data, searchContent);
                sherViewHolder.sherShareIcon.setTag(R.id.tag_data, searchContent);
                sherViewHolder.sherPoetName.setTag(R.id.tag_data, searchContent);

                addFavoriteClick(sherViewHolder.sherHeartIcon, searchContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(sherViewHolder.sherHeartIcon, searchContent.getId());

                String bodyData = searchContent.getBody().replace("<br/>", "\n");
                if (bodyData.contains("\r\n")) {
                    String[] separated = bodyData.split("\r\n");
                    String firstLine = separated[0];
                    String lastLine = separated[1];

                    if (Build.VERSION.SDK_INT >= 27) {
                        sherViewHolder.txtSher.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                        sherViewHolder.translatedSecondtextsher.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                    } else if (sherViewHolder.txtSher instanceof AutoSizeableTextView) {
                        ((AutoSizeableTextView) sherViewHolder.txtSher).setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                        ((AutoSizeableTextView) sherViewHolder.translatedSecondtextsher).setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                    }

//                    sherViewHolder.txtSher.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
//                    sherViewHolder.translatedSecondtextsher.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                    sherViewHolder.txtSher.setText(firstLine);
                    sherViewHolder.translatedSecondtextsher.setText(lastLine);
                    sherViewHolder.translatedSecondtextsher.setVisibility(View.VISIBLE);
                } else {
                    if (Build.VERSION.SDK_INT >= 27) {
                        sherViewHolder.txtSher.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                    } else if (sherViewHolder.txtSher instanceof AutoSizeableTextView) {
                        ((AutoSizeableTextView) sherViewHolder.txtSher).setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                    }

                    //   sherViewHolder.txtSher.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                    sherViewHolder.txtSher.setText(bodyData);
                    sherViewHolder.translatedSecondtextsher.setVisibility(View.GONE);
                }

                // sherViewHolder.txtSher.setText(bodyData);
                sherViewHolder.sherPoetName.setText(searchContent.getPoetName());
                break;
            }
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
        @BindView(R.id.ghazalTitleSecond)
        TextView ghazalTitleSecond;

        GhazalViewHolder(View view) {
            ButterKnife.bind(this, view);
            ghazalTitle.setMaxLines(2);
            ghazalTitle.setEllipsize(TextUtils.TruncateAt.END);
        }
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
        @BindView(R.id.txtNazamSubTitleSecond)
        TextView txtNazamSubTitleSecond;

        NazmViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class SherViewHolder {
        @BindView(R.id.translatedtextsher)
        TextView txtSher;
        @BindView(R.id.sher_poet_name)
        TextView sherPoetName;
        @BindView(R.id.sher_heartIcon)
        ImageView sherHeartIcon;
        @BindView(R.id.sher_shareIcon)
        ImageView sherShareIcon;
        @BindView(R.id.sher_clipbordIcon)
        ImageView sherClipbordIcon;
        @BindView(R.id.poetList_layout)
        LinearLayout poetListLayout;
        @BindView(R.id.translatedSecondtextsher)
        TextView translatedSecondtextsher;

        @OnClick({R.id.sher_poet_name, R.id.sher_shareIcon, R.id.sher_clipbordIcon})
        public void onViewClicked(View view) {
            SearchContent searchContent = null;
            if (view.getTag(R.id.tag_data) != null && view.getTag(R.id.tag_data) instanceof SearchContent)
                searchContent = (SearchContent) view.getTag(R.id.tag_data);
            if (searchContent == null)
                return;
            switch (view.getId()) {
                case R.id.sher_poet_name:
                    getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), searchContent.getPoetId()));
                    break;
                case R.id.sher_shareIcon: {
                    String bodyData = searchContent.getBody().replace("<br/>", "\n");
                    String stringBuilder = String.format("%s\n\n%s\n%s", bodyData, searchContent.getPoetName(), searchContent.getContentUrl());
                    BaseActivity.shareTheUrl(stringBuilder, getActivity());
                }
                break;
                case R.id.sher_clipbordIcon:
                    String bodyData = searchContent.getBody().replace("<br/>", "\n");
                    copyToClipBoard(bodyData);
                    break;
            }
        }

        SherViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public void copyToClipBoard(String sherContentText) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Sher", sherContentText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), AppErrorMessage.poetsher_adapter_copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }


}
