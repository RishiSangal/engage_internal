package com.example.sew.adapters;

import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.binaryfork.spanny.Spanny;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.PlattsDictionary;
import com.example.sew.models.SearchDictionary;
import com.example.sew.views.TitleTextViewType4;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DictionaryAdapter extends BaseMyAdapter {

    private ArrayList<Object> allData;
    private final int VIEW_TYPE_HEADER_TITLE = 0;
    private final int VIEW_TYPE_SECTION_TITLE = 1;
    private final int VIEW_TYPE_DICTIONARY = 2;
    private final int VIEW_TYPE_PLATTS = 3;

    public DictionaryAdapter(BaseActivity activity, ArrayList<Object> allData) {
        super(activity);
        this.allData = allData;
    }

    @Override
    public int getCount() {
        return allData.size();
    }

    @Override
    public Object getItem(int position) {
        return allData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && getItem(position) instanceof String)
            return VIEW_TYPE_HEADER_TITLE;
        else if (getItem(position) instanceof String)
            return VIEW_TYPE_SECTION_TITLE;
        else if (getItem(position) instanceof SearchDictionary)
            return VIEW_TYPE_DICTIONARY;
        else if (getItem(position) instanceof PlattsDictionary)
            return VIEW_TYPE_PLATTS;
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER_TITLE: {
                HeaderTitleViewHolder headerTitleViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_search_dictionary_header_title);
                    headerTitleViewHolder = new HeaderTitleViewHolder(convertView);
                    convertView.setTag(headerTitleViewHolder);
                } else
                    headerTitleViewHolder = (HeaderTitleViewHolder) convertView.getTag();

                headerTitleViewHolder.txtTitle.setText(new Spanny(MyHelper.getString(R.string.showing_result_for_1))
                        .append(" ")
                        .append(String.valueOf(getItem(position)), boldSpan(), new ForegroundColorSpan(getClickableTextColor())));
                headerTitleViewHolder.txtDictionaryMatchesTitle.setText(MyHelper.getString(R.string.dict_matches));
            }
            break;
            case VIEW_TYPE_SECTION_TITLE: {
                TitleViewHolder titleViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_search_content_title);
                    titleViewHolder = new TitleViewHolder(convertView);
                } else
                    titleViewHolder = (TitleViewHolder) convertView.getTag();
                convertView.setTag(titleViewHolder);
                titleViewHolder.txtTitle.setText(String.valueOf(getItem(position)));
            }
            break;
            case VIEW_TYPE_DICTIONARY: {
                DictionaryViewHolder dictionaryViewHolder;
                SearchDictionary dictionary = (SearchDictionary) getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_dictionary);
                    dictionaryViewHolder = new DictionaryViewHolder(convertView);
                } else
                    dictionaryViewHolder = (DictionaryViewHolder) convertView.getTag();
                convertView.setTag(dictionaryViewHolder);
                String mainWord = "", secondaryWord1 = "", secondaryWord2 = "";
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        dictionaryViewHolder.txtMainWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                        dictionaryViewHolder.txtSecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                        dictionaryViewHolder.txtSecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                        mainWord = dictionary.getEnglish();
                        secondaryWord1 = dictionary.getHindi();
                        secondaryWord2 = dictionary.getUrdu();
                        break;
                    case HINDI:
                        dictionaryViewHolder.txtMainWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                        dictionaryViewHolder.txtSecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                        dictionaryViewHolder.txtSecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                        mainWord = dictionary.getHindi();
                        secondaryWord1 = dictionary.getEnglish();
                        secondaryWord2 = dictionary.getUrdu();
                        break;
                    case URDU:
                        dictionaryViewHolder.txtMainWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                        dictionaryViewHolder.txtSecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                        dictionaryViewHolder.txtSecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                        mainWord = dictionary.getUrdu();
                        secondaryWord1 = dictionary.getHindi();
                        secondaryWord2 = dictionary.getEnglish();
                        break;
                }
                dictionaryViewHolder.txtMainWord.setText(mainWord);
                dictionaryViewHolder.txtSecondaryText1.setText(secondaryWord1);
                dictionaryViewHolder.txtSecondaryText2.setText(secondaryWord2);

                String meaning_1 = dictionary.getMeaning1();
                String meaning_2 = dictionary.getMeaning2();
                String meaning_3 = dictionary.getMeaning3();
                dictionaryViewHolder.txtMeaning1.setVisibility(View.GONE);
                dictionaryViewHolder.txtMeaning2.setVisibility(View.GONE);
                dictionaryViewHolder.txtMeaning3.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(meaning_1)) {
                    dictionaryViewHolder.txtMeaning1.setVisibility(View.VISIBLE);
                    dictionaryViewHolder.txtMeaning1.setText(meaning_1);
                }
                if (!TextUtils.isEmpty(meaning_2)) {
                    dictionaryViewHolder.txtMeaning2.setVisibility(View.VISIBLE);
                    dictionaryViewHolder.txtMeaning2.setText(meaning_2);
                }
                if (!TextUtils.isEmpty(meaning_3)) {
                    dictionaryViewHolder.txtMeaning3.setVisibility(View.VISIBLE);
                    dictionaryViewHolder.txtMeaning3.setText(meaning_3);
                }
                getActivity().addFavoriteClick(dictionaryViewHolder.imgWordFavorite, dictionary.getId(), Enums.FAV_TYPES.WORD.getKey());
                getActivity().updateFavoriteIcon(dictionaryViewHolder.imgWordFavorite, dictionary.getId());
                dictionaryViewHolder.imgWordAudio.setTag(dictionary.getAudioUrl());
                dictionaryViewHolder.imgWordAudio.setVisibility(dictionary.isHaveAudio()? View.VISIBLE : View.GONE);
                break;
            }
        }
        return convertView;
    }

    private StyleSpan boldSpan() {
        return new StyleSpan(android.graphics.Typeface.BOLD);
    }

    private int getClickableTextColor() {
        return ContextCompat.getColor(getActivity(), R.color.blue);
    }

    class DictionaryViewHolder {
        @BindView(R.id.txtMainWord)
        TitleTextViewType6 txtMainWord;
        @BindView(R.id.imgWordFavorite)
        ImageView imgWordFavorite;
        @BindView(R.id.imgWordAudio)
        ImageView imgWordAudio;
        @BindView(R.id.txtSecondaryText1)
        TextView txtSecondaryText1;
        @BindView(R.id.txtSecondaryText2)
        TextView txtSecondaryText2;
        @BindView(R.id.txtMeaning1)
        TitleTextViewType6 txtMeaning1;
        @BindView(R.id.txtMeaning2)
        TitleTextViewType6 txtMeaning2;
        @BindView(R.id.txtMeaning3)
        TitleTextViewType6 txtMeaning3;
        @OnClick(R.id.imgWordAudio)
        void onAudioIconClick(View view) {
            MyHelper.playAudio(view.getTag().toString(), getActivity(),imgWordAudio);
        }
        DictionaryViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }



    class TitleViewHolder {
        @BindView(R.id.txtTitle)
        TextView txtTitle;

        TitleViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class HeaderTitleViewHolder {
        @BindView(R.id.txtTitle)
        TitleTextViewType6 txtTitle;
        @BindView(R.id.txtDictionaryMatchesTitle)
        TitleTextViewType6 txtDictionaryMatchesTitle;

        HeaderTitleViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
