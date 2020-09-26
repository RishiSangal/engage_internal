package com.example.sew.adapters;

import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.binaryfork.spanny.Spanny;
import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.ServiceManager;
import com.example.sew.models.FavoriteDictionary;
import com.example.sew.models.PlattsDictionary;
import com.example.sew.models.SearchDictionary;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DictionaryRecyclerAdapter extends BaseRecyclerAdapter {

    private ArrayList<Object> allData;
    private final int VIEW_TYPE_HEADER_TITLE = 0;
    private final int VIEW_TYPE_SECTION_TITLE = 1;
    private final int VIEW_TYPE_DICTIONARY = 2;
    private final int VIEW_TYPE_PLATTS = 3;

    public DictionaryRecyclerAdapter(BaseActivity activity, ArrayList<Object> allData) {
        super(activity);
        this.allData = allData;
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public Object getItem(int position) {
        return allData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && getItem(position) instanceof String)
            return VIEW_TYPE_HEADER_TITLE;
        else if (getItem(position) instanceof String)
            return VIEW_TYPE_SECTION_TITLE;
        else if (getItem(position) instanceof SearchDictionary || getItem(position) instanceof FavoriteDictionary)
            return VIEW_TYPE_DICTIONARY;
        else if (getItem(position) instanceof PlattsDictionary)
            return VIEW_TYPE_PLATTS;
        return super.getItemViewType(position);
    }

    public int getViewTypeCount() {
        return 4;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER_TITLE:
                return new HeaderTitleViewHolder(getInflatedView(R.layout.cell_search_dictionary_header_title, parent));
            case VIEW_TYPE_SECTION_TITLE:
                return new TitleViewHolder(getInflatedView(R.layout.cell_search_content_title, parent));
            case VIEW_TYPE_DICTIONARY:
                return new DictionaryViewHolder(getInflatedView(R.layout.cell_dictionary, parent));

        }
        return new TitleViewHolder(getInflatedView(R.layout.cell_search_content_title, parent));

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER_TITLE: {
                if (!(holder instanceof HeaderTitleViewHolder))
                    return;
                HeaderTitleViewHolder headerTitleViewHolder = (HeaderTitleViewHolder) holder;
                headerTitleViewHolder.txtTitle.setText(new Spanny(MyHelper.getString(R.string.showing_result_for_1))
                        .append(" ")
                        .append(String.valueOf(getItem(position)), boldSpan(), new ForegroundColorSpan(getClickableTextColor())));
                headerTitleViewHolder.txtDictionaryMatchesTitle.setText(MyHelper.getString(R.string.dict_matches));
            }
            break;
            case VIEW_TYPE_SECTION_TITLE: {
                if (!(holder instanceof TitleViewHolder))
                    return;
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                titleViewHolder.txtTitle.setText(String.valueOf(getItem(position)));
            }
            break;
            case VIEW_TYPE_DICTIONARY: {
                if (!(holder instanceof DictionaryViewHolder))
                    return;
                DictionaryViewHolder dictionaryViewHolder = (DictionaryViewHolder) holder;
                SearchDictionary dictionary = null;
                FavoriteDictionary favoriteDictionary = null;
                if (getItem(position) instanceof SearchDictionary)
                    dictionary = (SearchDictionary) getItem(position);
                if (getItem(position) instanceof FavoriteDictionary)
                    favoriteDictionary = (FavoriteDictionary) getItem(position);
                String mainWord = "", secondaryWord1 = "", secondaryWord2 = "";
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        dictionaryViewHolder.txtMainWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                        dictionaryViewHolder.txtSecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                        dictionaryViewHolder.txtSecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                        mainWord = dictionary != null ? dictionary.getEnglish() : favoriteDictionary.getEnglish();
                        secondaryWord1 = dictionary != null ? dictionary.getHindi() : favoriteDictionary.getHindi();
                        secondaryWord2 = dictionary != null ? dictionary.getUrdu() : favoriteDictionary.getUrdu();
                        break;
                    case HINDI:
                        dictionaryViewHolder.txtMainWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                        dictionaryViewHolder.txtSecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                        dictionaryViewHolder.txtSecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                        mainWord = dictionary != null ? dictionary.getHindi() : favoriteDictionary.getHindi();
                        secondaryWord1 = dictionary != null ? dictionary.getEnglish() : favoriteDictionary.getEnglish();
                        secondaryWord2 = dictionary != null ? dictionary.getUrdu() : favoriteDictionary.getUrdu();
                        break;
                    case URDU:
                        dictionaryViewHolder.txtMainWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                        dictionaryViewHolder.txtSecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                        dictionaryViewHolder.txtSecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                        mainWord = dictionary != null ? dictionary.getUrdu() : favoriteDictionary.getUrdu();
                        secondaryWord1 = dictionary != null ? dictionary.getHindi() : favoriteDictionary.getHindi();
                        secondaryWord2 = dictionary != null ? dictionary.getEnglish() : favoriteDictionary.getEnglish();
                        break;
                }
                dictionaryViewHolder.txtMainWord.setText(mainWord);
                dictionaryViewHolder.txtSecondaryText1.setText(secondaryWord1);
                dictionaryViewHolder.txtSecondaryText2.setText(secondaryWord2);

                String meaning_1 = dictionary != null ? dictionary.getMeaning1() : favoriteDictionary.getMeaning1();
                String meaning_2 = dictionary != null ? dictionary.getMeaning2() : "";
                String meaning_3 = dictionary != null ? dictionary.getMeaning3() : "";
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
                getActivity().addFavoriteClick(dictionaryViewHolder.imgWordFavorite, dictionary != null ? dictionary.getId() : favoriteDictionary.getId(), Enums.FAV_TYPES.WORD.getKey());
                getActivity().updateFavoriteIcon(dictionaryViewHolder.imgWordFavorite, dictionary != null ? dictionary.getId() : favoriteDictionary.getId());
                dictionaryViewHolder.imgWordAudio.setTag(dictionary != null ? dictionary.getAudioUrl() : favoriteDictionary.getAudioUrl());
                if (getItem(position) instanceof SearchDictionary) {
                    assert dictionary != null;
                    dictionaryViewHolder.imgWordAudio.setVisibility(dictionary.isHaveAudio()? View.VISIBLE : View.GONE);
                }
                if (getItem(position) instanceof FavoriteDictionary) {
                    assert favoriteDictionary != null;
                    dictionaryViewHolder.imgWordAudio.setVisibility(favoriteDictionary.isHaveAudio()? View.VISIBLE : View.GONE);
                }
                dictionaryViewHolder.imgWordFavorite.setVisibility(MyApplication.getInstance().isBrowsingOffline() ? View.GONE : View.VISIBLE);
            }
            break;
        }
    }

    private StyleSpan boldSpan() {
        return new StyleSpan(android.graphics.Typeface.BOLD);
    }

    private int getClickableTextColor() {
        return ContextCompat.getColor(getActivity(), R.color.blue);
    }

    class DictionaryViewHolder extends BaseViewHolder {
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
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    class TitleViewHolder extends BaseViewHolder {
        @BindView(R.id.txtTitle)
        TextView txtTitle;

        TitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class HeaderTitleViewHolder extends BaseViewHolder {
        @BindView(R.id.txtTitle)
        TitleTextViewType6 txtTitle;
        @BindView(R.id.txtDictionaryMatchesTitle)
        TitleTextViewType6 txtDictionaryMatchesTitle;

        HeaderTitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
