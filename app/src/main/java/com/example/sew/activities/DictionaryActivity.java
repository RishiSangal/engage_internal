package com.example.sew.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.binaryfork.spanny.Spanny;
import com.example.sew.R;
import com.example.sew.adapters.DictionaryAdapter;
import com.example.sew.adapters.PoetVideoAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetPlattsDictionary;
import com.example.sew.apis.GetRekhtaDictionary;
import com.example.sew.apis.GetWordOfTheDay;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.Enums;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;
import com.example.sew.models.PlattsDictionary;
import com.example.sew.models.SearchDictionary;
import com.example.sew.models.WordOfTheDay;
import com.example.sew.models.home_view_holders.WordOfTheDayViewHolder;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType7;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class DictionaryActivity extends BaseActivity {

    @BindView(R.id.edSearch)
    EditText edSearch;
    @BindView(R.id.laySearch)
    LinearLayout laySearch;
    @BindView(R.id.lstDictionary)
    PagingListView lstDictionary;
    @BindView(R.id.txtWordOfTheDayMainText)
    TitleTextViewType7 txtWordOfTheDayMainText;
    @BindView(R.id.txtWordOfTheDayHindiText)
    TextView txtWordOfTheDayHindiText;
    @BindView(R.id.txtWordOfTheDayUrduText)
    TextView txtWordOfTheDayUrduText;
    @BindView(R.id.txtWordMeaning)
    TextView txtWordMeaning;
    @BindView(R.id.txtContentUsingWord)
    TextView txtContentUsingWord;
    @BindView(R.id.txtPoetName)
    TextView txtPoetName;
    @BindView(R.id.txtWordOfTheDayHeading)
    TextView txtWordOfTheDayHeading;
    @BindView(R.id.txtMeaningHeading)
    TextView txtMeaningHeading;
    @BindView(R.id.worldOfDayHeader)
    LinearLayout worldOfDayHeader;
    @BindView(R.id.layWorldOfDayBox)
    LinearLayout layWorldOfDayBox;
    @BindView(R.id.txtUrduDictionary)
    TextView txtUrduDictionary;
    @BindView(R.id.txtHeaderDictionary)
    TextView txtHeaderDictionary;
    @BindView(R.id.txtNoData)
    TextView txtNoData;
    @BindView(R.id.txtContentUsingWordHinUrdu)
    TextView txtContentUsingWordHinUrdu;

    @OnEditorAction(R.id.edSearch)
    void onSearchKeyboardAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            if (TextUtils.isEmpty(getEditTextData(edSearch)))
                showToast(AppErrorMessage.please_enter_text_to_search);

            else {
                hideKeyBoard();
                showDialog();
                getDictionaryResult(getEditTextData(edSearch));
            }
        }
    }

    @OnTextChanged(R.id.edSearch)
    void onSearchTextChanged() {
        String searchText = getEditTextData(edSearch);
        if (TextUtils.isEmpty(searchText)) {
            getWordOfTheDay();
            worldOfDayHeader.setVisibility(View.VISIBLE);
            layWorldOfDayBox.setVisibility(View.VISIBLE);
            lstDictionary.setVisibility(View.GONE);
            txtNoData.setVisibility(View.GONE);
        }
    }

    public static Intent getInstance(BaseActivity activity) {
        return new Intent(activity, DictionaryActivity.class);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
        updateLanguageSpecificUi();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        if (dictionaryAdapter != null)
            dictionaryAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        ButterKnife.bind(this);
        updateUI();
        getWordOfTheDay();
        lstDictionary.setIsLoading(false);
        lstDictionary.setHasMoreItems(false);

    }


    @OnClick(R.id.txtPoetName)
    public void onPoetClicked() {
//        if (wordOfTheDay != null && !TextUtils.isEmpty(wordOfTheDay.getPoetId()))
//            startActivity(PoetDetailActivity.getInstance(getActivity(), wordOfTheDay.getPoetId()));
    }

    private void getDictionaryResult(String keyword) {
        this.searchKeyword = keyword;
        worldOfDayHeader.setVisibility(View.GONE);
        layWorldOfDayBox.setVisibility(View.GONE);
        lstDictionary.setVisibility(View.VISIBLE);
        showDialog();
        getRekhtaDictionary(keyword);
        getPalletsDictionary(keyword);
    }

    boolean isRekhtaDictionaryLoaded;
    boolean isPalletDictionaryLoaded;
    String searchKeyword;
    private ArrayList<SearchDictionary> searchDictionaries;
    private ArrayList<PlattsDictionary> plattsDictionaries;
    private ArrayList<Object> allData = new ArrayList<>();

    private void getRekhtaDictionary(String keyword) {
        isRekhtaDictionaryLoaded = false;
        new GetRekhtaDictionary()
                .setKeyword(keyword)
                .runAsync((BaseServiceable.OnApiFinishListener<GetRekhtaDictionary>) getRekhtaDictionary -> {
                    isRekhtaDictionaryLoaded = true;
                    if (getRekhtaDictionary.isValidResponse()) {
                        if (getRekhtaDictionary.getDictionaries() == null || getRekhtaDictionary.getDictionaries().size() == 0) {
                            txtNoData.setVisibility(View.VISIBLE);
                            lstDictionary.setVisibility(View.GONE);
                            updateLanguageSpecificUi();
                            //  noRecordsFoundPopup();
                        } else {
                            txtNoData.setVisibility(View.GONE);
                            lstDictionary.setVisibility(View.VISIBLE);
                            searchDictionaries = getRekhtaDictionary.getDictionaries();
                        }
                        updateUI();

                    } else
                        showToast(getRekhtaDictionary.getErrorMessage());
                });
    }

    private void updateLanguageSpecificUi() {
        txtNoData.setText(MyHelper.getString(R.string.no_records_found));
    }

    private void getPalletsDictionary(String keyword) {
        isPalletDictionaryLoaded = false;
        new GetPlattsDictionary()
                .setKeyword(keyword)
                .runAsync((BaseServiceable.OnApiFinishListener<GetPlattsDictionary>) getPlattsDictionary -> {
                    isPalletDictionaryLoaded = true;
                    if (getPlattsDictionary.isValidResponse()) {
                        plattsDictionaries = getPlattsDictionary.getDictionaries();
                        updateUI();
                    } else
                        showToast(getPlattsDictionary.getErrorMessage());

                });

    }

    private WordOfTheDay wordOfTheDay;

    private void getWordOfTheDay() {
        showDialog();
        new GetWordOfTheDay().runAsync((BaseServiceable.OnApiFinishListener<GetWordOfTheDay>) getWordOfTheDay -> {
            dismissDialog();
            if (getWordOfTheDay.isValidResponse()) {
                wordOfTheDay = getWordOfTheDay.getWordOfTheDay();
                updateUI();
            } else {
                showToast(getWordOfTheDay.getErrorMessage());
            }
        });
    }

    DictionaryAdapter dictionaryAdapter;

    private void updateUI() {
        setHeaderTitle(MyHelper.getString(R.string.dictionary));
        if (wordOfTheDay != null) {
            String mainWord = "", secondaryWord1 = "", secondayrWord2 = "";
            switch (MyService.getSelectedLanguage()) {
                case ENGLISH:
                    txtWordOfTheDayMainText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                    txtWordOfTheDayHindiText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_sans_devanagari_regular_hin));
                    txtWordOfTheDayUrduText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                    mainWord = wordOfTheDay.getEnglishWord();
                    secondaryWord1 = wordOfTheDay.getHindiWord();
                    secondayrWord2 = wordOfTheDay.getUrduWord();
                    break;
                case HINDI:
                    txtWordOfTheDayMainText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_sans_devanagari_regular_hin));
                    txtWordOfTheDayHindiText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                    txtWordOfTheDayUrduText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                    mainWord = wordOfTheDay.getHindiWord();
                    secondaryWord1 = wordOfTheDay.getEnglishWord();
                    secondayrWord2 = wordOfTheDay.getUrduWord();
                    break;
                case URDU:
                    txtWordOfTheDayMainText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                    txtWordOfTheDayHindiText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_sans_devanagari_regular_hin));
                    txtWordOfTheDayUrduText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.lato_x_bold_italic_eng));
                    mainWord = wordOfTheDay.getUrduWord();
                    secondaryWord1 = wordOfTheDay.getHindiWord();
                    secondayrWord2 = wordOfTheDay.getEnglishWord();
                    break;
            }

            txtWordOfTheDayMainText.setText(mainWord);
            txtWordOfTheDayHindiText.setText(secondaryWord1);
            txtWordOfTheDayUrduText.setText(secondayrWord2);
            txtWordMeaning.setText(wordOfTheDay.getMeaning());

            if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH){
                txtContentUsingWord.setVisibility(View.VISIBLE);
                txtContentUsingWord.setText(wordOfTheDay.getContent());
                txtContentUsingWordHinUrdu.setVisibility(View.GONE);
            }else{
                txtContentUsingWordHinUrdu.setText(wordOfTheDay.getContent());
                txtContentUsingWordHinUrdu.setVisibility(View.VISIBLE);
                txtContentUsingWord.setVisibility(View.GONE);
            }




            if (!TextUtils.isEmpty(wordOfTheDay.getParentTitleSlug())) {
                txtPoetName.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(wordOfTheDay.getPoetName())) {
                    txtPoetName.setMovementMethod(LinkMovementMethod.getInstance());
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
                    txtPoetName.setText(spanny);
                } else {
                    txtPoetName.setMovementMethod(LinkMovementMethod.getInstance());
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
                    txtPoetName.setText(spanny);
                }
            } else {
                txtPoetName.setVisibility(View.VISIBLE);
                txtPoetName.setMovementMethod(LinkMovementMethod.getInstance());
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
                txtPoetName.setText(spanny);
                if (TextUtils.isEmpty(wordOfTheDay.getPoetName())) {
                    txtPoetName.setVisibility(View.GONE);
                }
            }


           // txtPoetName.setText(wordOfTheDay.getPoetName());
            txtWordOfTheDayHeading.setText(MyHelper.getString(R.string.word_of_the_day));
            txtMeaningHeading.setText(MyHelper.getString(R.string.meaning));
            txtUrduDictionary.setText(MyHelper.getString(R.string.urdu_dictionary));
            edSearch.setHint(MyHelper.getString(R.string.urdu_dictionary_search_header));
            edSearch.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            txtHeaderDictionary.setText(MyHelper.getString(R.string.dictionary_header));
        }
        if (isPalletDictionaryLoaded && isRekhtaDictionaryLoaded) {
            dismissDialog();
//            allData.add(String.format("%s \"%s\"", "Showing results for ", searchKeyword));
            allData.clear();
            if (!CollectionUtils.isEmpty(searchDictionaries)) {
                allData.add(searchKeyword);
                allData.addAll(searchDictionaries);
            }
//            if (!CollectionUtils.isEmpty(plattsDictionaries)) {
//                allData.add("PLATTS DICTIONARY");
//                allData.addAll(plattsDictionaries);
//            }

            if (dictionaryAdapter == null) {
                Parcelable state = lstDictionary.onSaveInstanceState();
                dictionaryAdapter = new DictionaryAdapter(getActivity(), allData);
                lstDictionary.setAdapter(dictionaryAdapter);
                lstDictionary.onRestoreInstanceState(state);
            } else
                dictionaryAdapter.notifyDataSetChanged();

        }
    }

    public void noRecordsFoundPopup() {
        new AlertDialog.Builder(getActivity())
                .setTitle(MyHelper.getString(R.string.rekhta))
                .setMessage(MyHelper.getString(R.string.no_records_found))
                .setPositiveButton(MyHelper.getString(R.string.okay), null)
                .create().show();
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

    private int getClickableTextColor() {
        return ContextCompat.getColor(getActivity(), R.color.dark_blue);
    }
}