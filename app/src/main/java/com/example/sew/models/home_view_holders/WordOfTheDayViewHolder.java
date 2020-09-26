package com.example.sew.models.home_view_holders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.binaryfork.spanny.Spanny;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.activities.SearchActivity;
import com.example.sew.adapters.CustomListAdapter;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.Utils;
import com.example.sew.fragments.MeaningBottomSheetFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.ContentType;
import com.example.sew.models.HomeOtherWorldOfTheDay;
import com.example.sew.models.HomeWordOfTheDay;
import com.example.sew.models.Para;
import com.example.sew.models.WordContainer;
import com.google.android.flexbox.FlexboxLayout;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;

public class WordOfTheDayViewHolder extends BaseHomeViewHolder {
    @BindView(R.id.txtWordOfTheDayTitle)
    TextView txtWordOfTheDayTitle;
    @BindView(R.id.txtWordOfTheDayMainText)
    TextView txtWordOfTheDayMainText;
    @BindView(R.id.txtWordOfTheSecondaryText1)
    TextView txtWordOfTheDaySecondaryText1;
    @BindView(R.id.txtWordOfTheSecondaryText2)
    TextView txtWordOfTheDaySecondaryText2;
    @BindView(R.id.txtWordMeaning)
    TextView txtWordMeaning;
    @BindView(R.id.laySher)
    LinearLayout laySher;
    @BindView(R.id.txtDictionaryTitle)
    TextView txtDictionaryTitle;
    @BindView(R.id.edSearch)
    AutoCompleteTextView edSearch;
    @BindView(R.id.laySearch)
    LinearLayout laySearch;
    @BindView(R.id.txtOtherWordsTitle)
    TextView txtOtherWordsTitle;
    @BindView(R.id.flexOtherWords)
    FlexboxLayout flexOtherWords;
    @BindView(R.id.imgWordAudio)
    ImageView imgWordAudio;
    @BindView(R.id.imgWordFavorite)
    ImageView imgWordFavorite;
    @BindView(R.id.poetList_layout)
    LinearLayout poetListLayout;
    @BindView(R.id.txtFromAuthor)
    TextView txtFromAuthor;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (imgWordFavorite != null && wordOfTheDay != null && getActivity() != null)
                getActivity().updateFavoriteIcon(imgWordFavorite, wordOfTheDay.getDictionaryId());
        }
    };

    @OnFocusChange(R.id.edSearch)
    void onFocusChanged(boolean focused) {
        if (focused) {
            showSuggestions();
        }
    }

    @OnEditorAction(R.id.edSearch)
    void onSearchKeyboardAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            loadDictionarySearch(getActivity().getEditTextData(edSearch));
        }
    }

    public void showSuggestions() {
        edSearch.setDropDownBackgroundResource(R.drawable.background_with_shadow);
        edSearch.setAdapter(new CustomListAdapter(getActivity(), R.layout.cell_word_suggestion_history, MyService.getSearchKeywordHistory()));
        String content = getActivity().getEditTextData(edSearch);
        edSearch.setText(String.format("%s ", content));
        edSearch.setText(content);
    }

    private void loadDictionarySearch(String query) {
        getActivity().hideKeyBoard();
        getActivity().startActivity(SearchActivity.getInstance(getActivity(), query));
    }

    public static WordOfTheDayViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        WordOfTheDayViewHolder wordOfTheDayViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_word_of_the_day, baseActivity);
            wordOfTheDayViewHolder = new WordOfTheDayViewHolder(convertView, baseActivity);
        } else
            wordOfTheDayViewHolder = (WordOfTheDayViewHolder) convertView.getTag();
        wordOfTheDayViewHolder.setConvertView(convertView);
        convertView.setTag(wordOfTheDayViewHolder);
        return wordOfTheDayViewHolder;
    }

    private WordOfTheDayViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                isOpen -> {
                    if (isOpen && edSearch.isFocused())
                        showSuggestions();
                });
        edSearch.setOnItemClickListener((parent, view, position, id) -> {
            loadDictionarySearch(view.getTag().toString());
        });
        getActivity().registerBroadcastListener(broadcastReceiver, BROADCAST_FAVORITE_UPDATED);
    }

    private boolean isDataLoaded;
    private ArrayList<HomeOtherWorldOfTheDay> otherWorldOfTheDays;
    private HomeWordOfTheDay wordOfTheDay;

    public BaseHomeViewHolder loadData(ArrayList<HomeOtherWorldOfTheDay> otherWorldOfTheDays, HomeWordOfTheDay wordOfTheDay) {
        this.otherWorldOfTheDays = otherWorldOfTheDays;
        this.wordOfTheDay = wordOfTheDay;
        if (!isDataLoaded) {
            isDataLoaded = true;
            updateUI();
        }
        return this;
    }

    private void updateUI() {
        if (!TextUtils.isEmpty(wordOfTheDay.getParentTitleSlug())) {
            txtFromAuthor.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(wordOfTheDay.getPoetName())) {
                txtFromAuthor.setMovementMethod(LinkMovementMethod.getInstance());
                ContentType contentType = MyHelper.getContentBySlug(wordOfTheDay.getParentTitleSlug());
                Spanny spanny = new Spanny();
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        spanny.append("from the ")
                                .append(String.format("%s ", contentType.getName()))
                                .append(String.format("\"%s\"", wordOfTheDay.getParentTitle()), new ForegroundColorSpan(getClickableTextColor()), new CustomClickableSpan(wordOfTheDay.getParentSlug()))
                                .append(String.format(" by %s", wordOfTheDay.getPoetName()));
                        break;
                    case HINDI:
                        spanny.append(String.format("\"%s\"", wordOfTheDay.getParentTitle()), new ForegroundColorSpan(getClickableTextColor()), new CustomClickableSpan(wordOfTheDay.getParentSlug()))
                                .append(String.format(" %s की ", wordOfTheDay.getPoetName()))
                                .append(String.format("%s से", contentType.getName()));
                        break;
                    case URDU:
                        spanny.append(String.format("\"%s\"", wordOfTheDay.getParentTitle()), new ForegroundColorSpan(getClickableTextColor()), new CustomClickableSpan(wordOfTheDay.getParentSlug()))
                                .append(String.format("%s ", wordOfTheDay.getPoetName()))
                                .append(String.format(" کی %s سے ", contentType.getName()));
                        break;
                }
                txtFromAuthor.setText(spanny);
            }else{
                txtFromAuthor.setMovementMethod(LinkMovementMethod.getInstance());
                ContentType contentType = MyHelper.getContentBySlug(wordOfTheDay.getParentTitleSlug());
                Spanny spanny = new Spanny();
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        spanny.append("from the ")
                                .append(String.format("%s ", contentType.getName()))
                                .append(String.format("\"%s\"", wordOfTheDay.getParentTitle()), new ForegroundColorSpan(getClickableTextColor()), new CustomClickableSpan(wordOfTheDay.getParentSlug()));
                        break;
                    case HINDI:
                        spanny.append(String.format("\"%s\"", wordOfTheDay.getParentTitle()), new ForegroundColorSpan(getClickableTextColor()), new CustomClickableSpan(wordOfTheDay.getParentSlug()))
                                .append(String.format("%s से", contentType.getName()));
                        break;
                    case URDU:
                        spanny.append(String.format("\"%s\"", wordOfTheDay.getParentTitle()), new ForegroundColorSpan(getClickableTextColor()), new CustomClickableSpan(wordOfTheDay.getParentSlug()))
                                .append(String.format("%s ", wordOfTheDay.getPoetName()));
                        break;
                }
                txtFromAuthor.setText(spanny);
            }
        } else{
            txtFromAuthor.setVisibility(View.VISIBLE);
            txtFromAuthor.setMovementMethod(LinkMovementMethod.getInstance());
           // ContentType contentType = MyHelper.getContentBySlug(wordOfTheDay.getParentTitleSlug());
            Spanny spanny = new Spanny();
            switch (MyService.getSelectedLanguage()) {
                case ENGLISH:
                    spanny.append(String.format(" by %s", wordOfTheDay.getPoetName()));
                    break;
                case HINDI:
                    spanny.append(String.format(" %s ", wordOfTheDay.getPoetName()));
                    break;
                case URDU:
                    spanny.append(String.format("%s ", wordOfTheDay.getPoetName()));
                    break;
            }
            txtFromAuthor.setText(spanny);
            if(TextUtils.isEmpty(wordOfTheDay.getPoetName())) {
                txtFromAuthor.setVisibility(View.GONE);
            }
        }
           // txtFromAuthor.setVisibility(View.GONE);
        txtOtherWordsTitle.setText(MyHelper.getString(R.string.other_popular_words));
        txtWordOfTheDayTitle.setText(MyHelper.getString(R.string.word_of_the_sday));
        txtDictionaryTitle.setText(MyHelper.getString(R.string.dictionary));
        String mainWord = "",
                secondaryWord1 = "",
                secondayrWord2 = "";
        edSearch.setHint(MyHelper.getString(R.string.search_for_more_words));
        imgWordAudio.setVisibility(TextUtils.isEmpty(wordOfTheDay.getAudioMp3File()) ? View.GONE : View.VISIBLE);
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                txtWordOfTheDayMainText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                txtWordOfTheDaySecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_sans_devanagari_regular_hin));
                txtWordOfTheDaySecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                mainWord = wordOfTheDay.getWord_En();
                secondaryWord1 = wordOfTheDay.getWord_Hi();
                secondayrWord2 = wordOfTheDay.getWord_Ur();
                break;
            case HINDI:
                txtWordOfTheDayMainText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_sans_devanagari_regular_hin));
                txtWordOfTheDaySecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                txtWordOfTheDaySecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                mainWord = wordOfTheDay.getWord_Hi();
                secondaryWord1 = wordOfTheDay.getWord_En();
                secondayrWord2 = wordOfTheDay.getWord_Ur();
                break;
            case URDU:
                txtWordOfTheDayMainText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                txtWordOfTheDaySecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_sans_devanagari_regular_hin));
                txtWordOfTheDaySecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                mainWord = wordOfTheDay.getWord_Ur();
                secondaryWord1 = wordOfTheDay.getWord_Hi();
                secondayrWord2 = wordOfTheDay.getWord_En();
                break;
        }

        getActivity().addFavoriteClick(imgWordFavorite, wordOfTheDay.getDictionaryId(), Enums.FAV_TYPES.WORD.getKey());
        getActivity().updateFavoriteIcon(imgWordFavorite, wordOfTheDay.getDictionaryId());
        txtWordOfTheDayMainText.setText(mainWord);
        txtWordOfTheDaySecondaryText1.setText(secondaryWord1);
        txtWordOfTheDaySecondaryText2.setText(secondayrWord2);
        txtWordMeaning.setText(wordOfTheDay.getMeaning());
        RenderHelper.RenderContentBuilder.Builder(getActivity())
                .setTextAlignment(Enums.TEXT_ALIGNMENT.CENTER)
                .setParas(wordOfTheDay.getTitle())
                .setLayParaContainer(laySher)
                .setLeftRightPadding((int) Utils.pxFromDp(28))
                .setOnWordLongClick(onWordLongClick)
                .setOnWordClick(onWordClickListener)
                .Build();
        flexOtherWords.removeAllViews();
        for (HomeOtherWorldOfTheDay otherWorldOfTheDay : otherWorldOfTheDays) {
            View view = getInflatedView(R.layout.cell_home_tag, getActivity());
            TextView txtTagName = view.findViewById(R.id.txtTagName);
            txtTagName.setText(otherWorldOfTheDay.getWord());
            view.setTag(otherWorldOfTheDay.getWord());
            view.setOnClickListener(v -> loadDictionarySearch(v.getTag().toString()));
            flexOtherWords.addView(view);
        }
    }

    private View.OnLongClickListener onWordLongClick = v -> {
        Para para = (Para) v.getTag(R.id.tag_para);
        if (para == null)
            return false;
        String shareContentText = MyHelper.getSherContentText(para);
        MyHelper.shareTheText(shareContentText, getActivity());
        MyHelper.copyToClipBoard(shareContentText, getActivity());
        return false;
    };
    private View.OnClickListener onWordClickListener = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
//        MeaningBottomSheetFragment.getInstance(wordContainer.getWord(), wordContainer.getMeaning()).show(getActivity().getSupportFragmentManager(), "MEANING");
        new MeaningBottomPopupWindow(getActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();
    };


    @OnClick({R.id.imgWordAudio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgWordAudio:
                MyHelper.playAudio(wordOfTheDay.getAudioMp3File(), getActivity(),imgWordAudio);
                break;
        }
    }

    private int getClickableTextColor() {
        return ContextCompat.getColor(getActivity(), R.color.dark_blue);
    }

    public class CustomClickableSpan extends ClickableSpan {
        String contentID;

        CustomClickableSpan(String contentID) {
            this.contentID = contentID;
        }

        @Override
        public void onClick(@NonNull View widget) {
//            startActivity(PoetDetailActivity.getInstance(GetActivity(), contentID));

            getActivity().startActivity(RenderContentActivity.getInstance(getActivity(), contentID));
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.linkColor = getClickableTextColor();
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
