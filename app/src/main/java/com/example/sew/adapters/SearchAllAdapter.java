package com.example.sew.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SearchContent;
import com.example.sew.models.SearchContentAll;
import com.example.sew.models.SearchDictionary;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAllAdapter extends BaseMyAdapter {

    private ArrayList<Object> allData;
    private final int VIEW_TYPE_HEADER_TITLE = 0;
    private final int VIEW_TYPE_SECTION_TITLE = 1;
    private final int VIEW_TYPE_POETS = 2;
    private final int VIEW_TYPE_DICTIONARY = 3;
    private final int VIEW_TYPE_CONTENT = 4;

    public SearchAllAdapter(BaseActivity activity, ArrayList<Object> allData) {
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
        if (position == 0)
            return VIEW_TYPE_HEADER_TITLE;
        else if (getItem(position) instanceof String)
            return VIEW_TYPE_SECTION_TITLE;
        else if (getItem(position) instanceof ArrayList<?>)
            return VIEW_TYPE_POETS;
        else if (getItem(position) instanceof SearchDictionary)
            return VIEW_TYPE_DICTIONARY;
        else if (getItem(position) instanceof SearchContent)
            return VIEW_TYPE_CONTENT;
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "RestrictedApi"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER_TITLE:
                SearchHeaderTitleViewHolder searchHeaderTitleViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_search_header_title);
                    searchHeaderTitleViewHolder = new SearchHeaderTitleViewHolder(convertView);
                    convertView.setTag(searchHeaderTitleViewHolder);
                    if (String.valueOf(getItem(position)).startsWith(MyHelper.getString(R.string.showing_result_for))) {
                        String[] separated = String.valueOf(getItem(position)).split("\"");
                        searchHeaderTitleViewHolder.txtTitle.setText(MyHelper.getString(R.string.showing_result_for));
                        searchHeaderTitleViewHolder.txtSearchWord.setText(" \"" + separated[1] + "\"");
                    }
                }
                break;
            case VIEW_TYPE_SECTION_TITLE:
                TitleViewHolder titleViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_search_content_title);
                    titleViewHolder = new TitleViewHolder(convertView);
                } else
                    titleViewHolder = (TitleViewHolder) convertView.getTag();
                convertView.setTag(titleViewHolder);
                if (TextUtils.isEmpty(String.valueOf(getItem(position)))) {
                    titleViewHolder.layContentTitle.setVisibility(View.GONE);
                } else {
                    titleViewHolder.layContentTitle.setVisibility(View.VISIBLE);
                    titleViewHolder.txtTitle.setText(String.valueOf(getItem(position)));
                }
                break;
            case VIEW_TYPE_POETS:
                PoetsViewHolder poetsViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_search_poets);
                    poetsViewHolder = new PoetsViewHolder(convertView);
                } else
                    poetsViewHolder = (PoetsViewHolder) convertView.getTag();
                convertView.setTag(poetsViewHolder);
                SearchPoetsAdapter searchPoetsAdapter = new SearchPoetsAdapter(getActivity(), (ArrayList<SearchContentAll.SearchPoet>) getItem(position));
                searchPoetsAdapter.setOnPoetClickListener(onPoetClickListener);
                poetsViewHolder.rvPoets.setAdapter(searchPoetsAdapter);
                break;
            case VIEW_TYPE_DICTIONARY: {
                DictionaryViewHolder dictionaryViewHolder;
                SearchDictionary dictionary = (SearchDictionary) getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_search_dictionary);
                    dictionaryViewHolder = new DictionaryViewHolder(convertView);
                } else
                    dictionaryViewHolder = (DictionaryViewHolder) convertView.getTag();
                convertView.setTag(dictionaryViewHolder);
                dictionaryViewHolder.txtWord.setText(dictionary.getEnglish().trim());
                dictionaryViewHolder.txtHindi.setText(dictionary.getHindi().trim());
                dictionaryViewHolder.txtUrdu.setText(dictionary.getUrdu().trim());
                String meaning_1 = "", meaning_2 = "", meaning_3 = "";
                String mainWord = "", secondaryWord1 = "", secondaryWord2 = "";
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        meaning_1 = dictionary.getMeaning1_En();
                        meaning_2 = dictionary.getMeaning2_En();
                        meaning_3 = dictionary.getMeaning3_En();
                        dictionaryViewHolder.txtWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                        dictionaryViewHolder.txtHindi.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                        dictionaryViewHolder.txtUrdu.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                        mainWord = dictionary.getEnglish();
                        secondaryWord1 = dictionary.getHindi();
                        secondaryWord2 = dictionary.getUrdu();
                        break;
                    case HINDI:
                        meaning_1 = dictionary.getMeaning1_Hi();
                        meaning_2 = dictionary.getMeaning2_Hi();
                        meaning_3 = dictionary.getMeaning3_Hi();
                        dictionaryViewHolder.txtWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                        dictionaryViewHolder.txtHindi.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                        dictionaryViewHolder.txtUrdu.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                        mainWord = dictionary.getHindi();
                        secondaryWord1 = dictionary.getEnglish();
                        secondaryWord2 = dictionary.getUrdu();
                        break;
                    case URDU:
                        meaning_1 = dictionary.getMeaning1_Ur();
                        meaning_2 = dictionary.getMeaning2_Ur();
                        meaning_3 = dictionary.getMeaning3_Ur();
                        dictionaryViewHolder.txtWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                        dictionaryViewHolder.txtHindi.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_devanagari_hin));
                        dictionaryViewHolder.txtUrdu.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                        mainWord = dictionary.getUrdu();
                        secondaryWord1 = dictionary.getHindi();
                        secondaryWord2 = dictionary.getEnglish();
                        break;
                }
                dictionaryViewHolder.txtEngMeaning1.setVisibility(View.GONE);
                dictionaryViewHolder.txtEngMeaning2.setVisibility(View.GONE);
                dictionaryViewHolder.txtEngMeaning3.setVisibility(View.GONE);
                dictionaryViewHolder.txtWord.setText(mainWord);
                dictionaryViewHolder.txtHindi.setText(secondaryWord1);
                dictionaryViewHolder.txtUrdu.setText(secondaryWord2);
//                dictionaryViewHolder.txtHindi.setText(dictionary.getHindi().trim());
//                dictionaryViewHolder.txtUrdu.setText(dictionary.getUrdu().trim());

                if (!TextUtils.isEmpty(meaning_1)) {
                    dictionaryViewHolder.txtEngMeaning1.setVisibility(View.VISIBLE);
                    dictionaryViewHolder.txtEngMeaning1.setText(meaning_1);
                }
                if (!TextUtils.isEmpty(meaning_2)) {
                    dictionaryViewHolder.txtEngMeaning2.setVisibility(View.VISIBLE);
                    dictionaryViewHolder.txtEngMeaning2.setText(meaning_2);
                }
                if (!TextUtils.isEmpty(meaning_3)) {
                    dictionaryViewHolder.txtEngMeaning3.setVisibility(View.VISIBLE);
                    dictionaryViewHolder.txtEngMeaning3.setText(meaning_3);
                }
                getActivity().addFavoriteClick(dictionaryViewHolder.imgWordFavorite, dictionary.getId(), Enums.FAV_TYPES.WORD.getKey());
                getActivity().updateFavoriteIcon(dictionaryViewHolder.imgWordFavorite, dictionary.getId());
                dictionaryViewHolder.imgWordAudio.setTag(dictionary.getAudioUrl());
                dictionaryViewHolder.imgWordAudio.setVisibility(dictionary.isHaveAudio() ? View.VISIBLE : View.GONE);
                break;
            }
            case VIEW_TYPE_CONTENT: {
                ContentViewHolder contentViewHolder;
                SearchContent content = (SearchContent) getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_search_content);
                    contentViewHolder = new ContentViewHolder(convertView);
                } else
                    contentViewHolder = (ContentViewHolder) convertView.getTag();
                convertView.setTag(contentViewHolder);
                convertView.setTag(R.id.tag_data, content);
                contentViewHolder.txtContentAuthor.setText(content.getPoetName());
                String bodyData = content.getBody().replace("<br/>", "\n");
                String body = content.getBody().substring(0, content.getBody().lastIndexOf("\n") + 1) + "";
                if (content.getContentTypeId().getListRenderingFormat() == Enums.LIST_RENDERING_FORMAT.SHER || content.getContentTypeId().getListRenderingFormat() == Enums.LIST_RENDERING_FORMAT.QUOTE) {
                    contentViewHolder.txtSher.setText(content.getBody());
                    contentViewHolder.txtContentBodySecond.setVisibility(View.GONE);
                    contentViewHolder.txtContentBody.setVisibility(View.GONE);
                    contentViewHolder.txtContentNazmBody.setVisibility(View.GONE);
                    contentViewHolder.txtContentNazmBodySecond.setVisibility(View.GONE);
                } else if (content.getContentTypeId().getListRenderingFormat() == Enums.LIST_RENDERING_FORMAT.GHAZAL) {
                    contentViewHolder.txtContentBodySecond.setVisibility(View.VISIBLE);
                    contentViewHolder.txtContentBody.setVisibility(View.VISIBLE);
                    contentViewHolder.txtContentNazmBody.setVisibility(View.GONE);
                    contentViewHolder.txtContentNazmBodySecond.setVisibility(View.GONE);
                    if (bodyData.contains("\r\n")) {
                        String[] separated = bodyData.split("\r\n");
                        String firstLine = separated[0];
                        String lastLine = separated[1];
                        // contentViewHolder.txtContentBody.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                        //contentViewHolder.txtContentBodySecond.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                        contentViewHolder.txtContentBody.setText(firstLine);
                        contentViewHolder.txtContentBodySecond.setText(lastLine);
                        contentViewHolder.txtContentBody.setMaxLines(1);
                        contentViewHolder.txtContentBodySecond.setMaxLines(1);
                        contentViewHolder.txtContentBodySecond.setVisibility(View.VISIBLE);
                    } else {
                        //contentViewHolder.txtContentBody.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                        contentViewHolder.txtContentBody.setText(bodyData);
                        contentViewHolder.txtContentBody.setMaxLines(1);
                        contentViewHolder.txtContentBodySecond.setVisibility(View.GONE);
                    }
                } else {
                    contentViewHolder.txtContentBodySecond.setVisibility(View.GONE);
                    contentViewHolder.txtContentBody.setVisibility(View.GONE);
                    contentViewHolder.txtContentNazmBody.setVisibility(View.VISIBLE);
                    contentViewHolder.txtContentNazmBodySecond.setVisibility(View.VISIBLE);
                    if (bodyData.contains("\r\n")) {
                        String[] separated = bodyData.split("\r\n");
                        String firstLine = separated[0];
                        String lastLine = separated[1];
                        //contentViewHolder.txtContentNazmBody.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                        //contentViewHolder.txtContentNazmBodySecond.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                        contentViewHolder.txtContentNazmBody.setText(firstLine);
                        contentViewHolder.txtContentNazmBodySecond.setText(lastLine);
                        contentViewHolder.txtContentNazmBody.setMaxLines(1);
                        contentViewHolder.txtContentNazmBodySecond.setMaxLines(1);
                        contentViewHolder.txtContentNazmBodySecond.setVisibility(View.VISIBLE);
                    } else {
                        //contentViewHolder.txtContentNazmBody.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                        contentViewHolder.txtContentNazmBody.setText(bodyData);
                        contentViewHolder.txtContentNazmBody.setMaxLines(1);
                        contentViewHolder.txtContentNazmBodySecond.setVisibility(View.GONE);
                    }
                }
                contentViewHolder.txtNazmTitle.setText(content.getTitle());
                contentViewHolder.txtNazmTitle.setVisibility(View.GONE);
                contentViewHolder.searchaudioLink.setVisibility(content.getAudioCountInt() > 0 ? View.VISIBLE : View.GONE);
                contentViewHolder.searchyoutubeLink.setVisibility(content.getAudioCountInt() > 0 ? View.VISIBLE : View.GONE);
                contentViewHolder.searchPopularChoiceIcon.setVisibility(content.isPopularChoice() ? View.VISIBLE : View.GONE);
                contentViewHolder.searchEditorChoiceIcon.setVisibility(content.isEditorChoice() ? View.VISIBLE : View.GONE);
                addFavoriteClick(contentViewHolder.offlineFavIcon, content.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(contentViewHolder.offlineFavIcon, content.getId());
                contentViewHolder.txtContentType.setText(content.getType());
               contentViewHolder.txtContentType.setTextColor(content.getContentTitleColor());
//                for (int i = 0; i < MyService.getAllContentType().size(); i++) {
//                    if (MyService.getAllContentType().get(i).getContentId().equalsIgnoreCase(content.getTypeId())) {
//                        int contentTypeColor = MyHelper.getTagColor(i);
//                        contentViewHolder.txtContentType.setTextColor(contentTypeColor);
//                    }
//                }
                if (content.getTypeId().equalsIgnoreCase(MyConstants.GHAZAL_ID)) {
                    contentViewHolder.topLayout.setVisibility(View.GONE);
                    contentViewHolder.linearLayout.setVisibility(View.VISIBLE);
                    contentViewHolder.txtSher.setVisibility(View.GONE);
                  //  contentViewHolder.txtContentType.setTextColor(getColor(R.color.md_red_A800));
                } else if (content.getTypeId().equalsIgnoreCase(MyConstants.NAZM_ID)) {
                    contentViewHolder.txtNazmTitle.setVisibility(View.VISIBLE);
                    contentViewHolder.txtSher.setVisibility(View.GONE);
                   // contentViewHolder.txtContentType.setTextColor(getColor(R.color.md_blue_A800));
                } else if (content.getTypeId().equalsIgnoreCase(MyConstants.SHER_ID)) {
                    contentViewHolder.topLayout.setVisibility(View.GONE);
                    contentViewHolder.linearLayout.setVisibility(View.GONE);
                    contentViewHolder.txtSher.setVisibility(View.VISIBLE);
                   // contentViewHolder.txtContentType.setTextColor(getColor(R.color.md_green_A800));
                } else {
                    contentViewHolder.topLayout.setVisibility(View.GONE);
                    contentViewHolder.linearLayout.setVisibility(View.VISIBLE);
                    contentViewHolder.txtSher.setVisibility(View.GONE);
                   // contentViewHolder.txtContentType.setTextColor(getColor(R.color.md_yellow_800));
                }


//                switch (content.getContentTypeId().getListRenderingFormat()) {
//                    case GHAZAL:
//                        contentViewHolder.topLayout.setVisibility(View.GONE);
//                        contentViewHolder.linearLayout.setVisibility(View.VISIBLE);
//                        contentViewHolder.txtSher.setVisibility(View.GONE);
//                       // contentViewHolder.txtContentType.setText(MyHelper.getString(R.string.ghazal).toUpperCase());
//                        contentViewHolder.txtContentType.setTextColor(getColor(R.color.md_red_A800));
//                        break;
//                    case NAZM:
//                        contentViewHolder.txtNazmTitle.setVisibility(View.VISIBLE);
//                        contentViewHolder.txtSher.setVisibility(View.GONE);
//                        //contentViewHolder.txtContentType.setText(MyHelper.getString(R.string.nazm).toUpperCase());
//                        contentViewHolder.txtContentType.setTextColor(getColor(R.color.md_blue_A800));
//                        break;
//                    case SHER:
//                    case QUOTE:
//                        //contentViewHolder.txtContentType.setText(MyHelper.getString(R.string.sher).toUpperCase());
//                        contentViewHolder.txtContentType.setTextColor(getColor(R.color.md_green_A800));
//                        contentViewHolder.topLayout.setVisibility(View.GONE);
//                        contentViewHolder.linearLayout.setVisibility(View.GONE);
//                        contentViewHolder.txtSher.setVisibility(View.VISIBLE);
//                        break;
//
//                }
                break;
            }
        }

        return convertView;
    }

    private View.OnClickListener onPoetClickListener;


    public void setOnPoetClickListener(View.OnClickListener onPoetClickListener) {
        this.onPoetClickListener = onPoetClickListener;
    }

    class DictionaryViewHolder {
        @BindView(R.id.txtWord)
        TitleTextViewType6 txtWord;
        @BindView(R.id.txtHindi)
        TextView txtHindi;
        @BindView(R.id.txtUrdu)
        TextView txtUrdu;
        @BindView(R.id.txtEngMeaning1)
        TitleTextViewType6 txtEngMeaning1;
        @BindView(R.id.txtEngMeaning2)
        TitleTextViewType6 txtEngMeaning2;
        @BindView(R.id.txtEngMeaning3)
        TitleTextViewType6 txtEngMeaning3;
        @BindView(R.id.imgWordFavorite)
        ImageView imgWordFavorite;
        @BindView(R.id.imgWordAudio)
        ImageView imgWordAudio;

        @OnClick(R.id.imgWordAudio)
        void onAudioIconClick(View view) {
            MyHelper.playAudio(view.getTag().toString(), getActivity(), imgWordAudio);
        }

        DictionaryViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class TitleViewHolder {
        @BindView(R.id.txtTitle)
        TitleTextViewType6 txtTitle;
        @BindView(R.id.layContentTitle)
        LinearLayout layContentTitle;

        TitleViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class SearchHeaderTitleViewHolder {
        @BindView(R.id.txtTitle)
        TitleTextViewType6 txtTitle;
        @BindView(R.id.txtSearchWord)
        TextView txtSearchWord;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        SearchHeaderTitleViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class PoetsViewHolder {
        @BindView(R.id.rvPoets)
        RecyclerView rvPoets;

        PoetsViewHolder(View view) {
            ButterKnife.bind(this, view);
            rvPoets.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    class ContentViewHolder {
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        @BindView(R.id.txtNazmTitle)
        TitleTextViewType6 txtNazmTitle;
        @BindView(R.id.topLayout)
        LinearLayout topLayout;
        @BindView(R.id.txtContentBody)
        TitleTextViewType6 txtContentBody;
        @BindView(R.id.shareIcon)
        ImageView shareIcon;
        @BindView(R.id.txtContentAuthor)
        TitleTextViewType6 txtContentAuthor;
        @BindView(R.id.searchEditorChoiceIcon)
        ImageView searchEditorChoiceIcon;
        @BindView(R.id.searchPopularChoiceIcon)
        ImageView searchPopularChoiceIcon;
        @BindView(R.id.searchaudioLink)
        ImageView searchaudioLink;
        @BindView(R.id.searchyoutubeLink)
        ImageView searchyoutubeLink;
        @BindView(R.id.imagecontainer)
        LinearLayout imagecontainer;
        @BindView(R.id.txtContentType)
        TitleTextViewType6 txtContentType;
        @BindView(R.id.txtSher)
        TextView txtSher;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;
        @BindView(R.id.txtContentBodySecond)
        TextView txtContentBodySecond;
        @BindView(R.id.txtContentNazmBody)
        TextView txtContentNazmBody;
        @BindView(R.id.txtContentNazmBodySecond)
        TextView txtContentNazmBodySecond;

        ContentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public class SearchPoetsAdapter extends BaseRecyclerAdapter {
        private ArrayList<SearchContentAll.SearchPoet> poets;
        private View.OnClickListener onPoetClickListener;

        public void setOnPoetClickListener(View.OnClickListener onPoetClickListener) {
            this.onPoetClickListener = onPoetClickListener;
        }

        SearchPoetsAdapter(BaseActivity activity, ArrayList<SearchContentAll.SearchPoet> poets) {
            super(activity);
            this.poets = poets;
        }

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PoetViewHolder(getInflatedView(R.layout.cell_search_poet));
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            if (!(holder instanceof PoetViewHolder))
                return;
            PoetViewHolder poetViewHolder = (PoetViewHolder) holder;
            SearchContentAll.SearchPoet poet = poets.get(position);
            holder.itemView.setTag(R.id.tag_data, poet);
            poetViewHolder.txtPoetName.setText(poet.getName());
            poetViewHolder.txtPoetTenure.setText(poet.getPoetTenure());
            ImageHelper.setImage(poetViewHolder.imgCollectionImage, poet.getImageUrl());
        }

        @Override
        public int getItemCount() {
            return poets.size();
        }

        class PoetViewHolder extends BaseViewHolder {
            @BindView(R.id.imgCollectionImage)
            CircleImageView imgCollectionImage;
            @BindView(R.id.txtPoetName)
            TitleTextViewType6 txtPoetName;
            @BindView(R.id.txtPoetTenure)
            TitleTextViewType6 txtPoetTenure;

            @OnClick
            void onItemClick(View view) {
                if (onPoetClickListener != null)
                    onPoetClickListener.onClick(view);
            }

            PoetViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

}
