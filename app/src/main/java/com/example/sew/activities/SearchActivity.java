package com.example.sew.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.adapters.CustomListAdapter;
import com.example.sew.adapters.ExplorePoetRecyclerAdapter;
import com.example.sew.adapters.ExploreShayariRecyclerAdapter;
import com.example.sew.adapters.SearchFragmentAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetExploreCollection;
import com.example.sew.common.Enums;
import com.example.sew.fragments.DictionaryFragment;
import com.example.sew.fragments.SearchAllFragment;
import com.example.sew.fragments.SearchGhazalNazmSherFragment;
import com.example.sew.fragments.SearchPoetsFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.HomeImageTag;
import com.example.sew.models.HomeProseCollection;
import com.example.sew.models.HomeTopPoet;
import com.example.sew.views.CustomAutoCompleteTextView;
import com.example.sew.views.TitleTextViewType12;
import com.example.sew.views.TitleTextViewType6;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.util.CollectionUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

import static com.example.sew.MyApplication.getContext;

public class SearchActivity extends BaseHomeActivity {
    @BindView(R.id.tabLayout)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.edSearch)
    AutoCompleteTextView searchEditBox;
    @BindView(R.id.clear_btn)
    ImageView clearBtn;
    @BindView(R.id.mic_btn)
    ImageView micBtn;
    @BindView(R.id.search_lay)
    RelativeLayout searchLay;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.laySearchContent)
    View laySearchContent;
    @BindView(R.id.laySearchContentTab)
    View laySearchContentTab;
    String searchedText;
    boolean shouldShowDictionary;
    @BindView(R.id.txtExplorePoetryTitle)
    TextView txtExplorePoetryTitle;
    @BindView(R.id.rvExplorePoetry)
    RecyclerView rvExplorePoetry;
    @BindView(R.id.layExplorePoetry)
    LinearLayout layExplorePoetry;
    @BindView(R.id.txtTopPoetsTitle)
    TextView txtTopPoetsTitle;
    @BindView(R.id.rvTopPoets)
    RecyclerView rvTopPoets;
    @BindView(R.id.layTopPoets)
    LinearLayout layTopPoets;
    @BindView(R.id.txtExploreTagsTitle)
    TextView txtExploreTagsTitle;
    @BindView(R.id.flexTags)
    FlexboxLayout flexTags;
    @BindView(R.id.layExploreTags)
    LinearLayout layExploreTags;
    @BindView(R.id.searchActivity_mainView)
    RelativeLayout searchActivityMainView;
    @BindView(R.id.txtExploreMoreTags)
    TextView txtExploreMoreTags;
    public final int VIEW_TYPE_EXPLORE = 0;
    public final int VIEW_TYPE_SEARCH = 1;
    public int currentViewType = VIEW_TYPE_EXPLORE;
    @BindView(R.id.layExplore)
    NestedScrollView layExplore;

    boolean isClick=false;

    @OnFocusChange(R.id.edSearch)
    void onFocusChanged(boolean focused) {
        if (focused) {
            showSuggestions();
        }
    }

    @OnEditorAction(R.id.edSearch)
    void onSearchKeyboardAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            loadDictionarySearch(getActivity().getEditTextData(edSearch));
            currentViewType = VIEW_TYPE_SEARCH;
            searchEditBox.dismissDropDown();
            hideKeyBoard();
            updateView();
        }
    }

    public void showSuggestions() {
        searchEditBox.setAdapter(new CustomListAdapter(getActivity(), R.layout.cell_word_suggestion_history, MyService.getSearchKeywordHistory()));

        String content = getActivity().getEditTextData(searchEditBox);
        searchEditBox.setText(String.format("%s ", content));
        searchEditBox.setText(content);
    }

    @OnTextChanged(R.id.edSearch)
    void onSearchTextChanged() {
        String searchText = getEditTextData(searchEditBox);
        if (TextUtils.isEmpty(searchText)) {
            currentViewType = VIEW_TYPE_EXPLORE;
            clearBtn.setVisibility(View.GONE);
            isClick=true;
            updateView();
        } else {
            clearBtn.setVisibility(View.VISIBLE);
        }
    }


    private void updateView() {
        searchEditBox.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        layExplore.setVisibility(View.GONE);
        laySearchContent.setVisibility(View.GONE);
        laySearchContentTab.setVisibility(View.GONE);
//        txtTitle.setVisibility(View.VISIBLE);
        if (currentViewType == VIEW_TYPE_EXPLORE) {
            layExplore.setVisibility(View.VISIBLE);
//            txtTitle.setVisibility(View.VISIBLE);
//            if(isClick)
//                txtTitle.setVisibility(View.GONE);
//            else
//                txtTitle.setVisibility(View.VISIBLE);
        }
        else {
//            txtTitle.setVisibility(View.GONE);
            laySearchContent.setVisibility(View.VISIBLE);
            laySearchContentTab.setVisibility(View.VISIBLE);
            viewpager.setOffscreenPageLimit(5);
            viewpager.setAdapter(new SearchFragmentAdapter(getSupportFragmentManager(), this, getEditTextData(searchEditBox)));
            tabLayout.setViewPager(viewpager);
            if (shouldShowDictionary) {
                shouldShowDictionary = false;
                viewpager.setCurrentItem(2);
            }
        }
    }


//    private void loadResult() {
//        searchedText = getEditTextData(searchEditBox);
//        showResult();
//    }

    private void showResult() {
        Intent intent = new Intent();
        intent.putExtra(SEARCH_TEXT, searchedText);
        sendBroadCast(BROADCAST_SEARCH_TEXT_CHANGED, intent);
    }

    public static Intent getInstance(BaseActivity activity, String query) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra(SEARCH_QUERY, query);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchEditBox.setDropDownBackgroundResource(R.drawable.background_with_shadow);
        initBottomNavigation(Enums.BOTTOM_TYPE.HOME_2);
        updateView();
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                isOpen -> {
                    if (isOpen && searchEditBox.isFocused()) {
                        showSuggestions();
                        searchEditBox.setCursorVisible(true);
                        searchEditBox.setSelection(getEditTextData(searchEditBox).length());
                    }
                });
        searchEditBox.setOnItemClickListener((parent, view, position, id) -> {
            currentViewType = VIEW_TYPE_SEARCH;
            hideKeyBoard();
            updateView();
        });
//        searchEditBox.requestFocusFromTouch();
//        searchEditBox.setOnTouchListener((v, event) -> {
//            searchEditBox.setCursorVisible(true);
//            return false;
//        });
//        searchEditBox.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                searchEditBox.setCursorVisible(true);
//            }
//        });
        String initialSearch = getIntent().getStringExtra(SEARCH_QUERY);
        rvExplorePoetry.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTopPoets.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        if (!TextUtils.isEmpty(initialSearch)) {
            new Handler().postDelayed(() -> {
                hideKeyBoard();
                searchEditBox.setText(initialSearch);
                shouldShowDictionary = true;
                currentViewType = VIEW_TYPE_SEARCH;
                updateView();
            }, 500);
        }

        Drawable drawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_searchtab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            searchEditBox.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
        }

        getExploreContent();
        updateLanguageSpecificContent();
    }

    @Override
    public void hideKeyBoard() {
        super.hideKeyBoard();
        searchEditBox.setCursorVisible(false);
    }

    private ArrayList<HomeTopPoet> topPoets;
    private ArrayList<HomeProseCollection> explorePoetry;
    private ArrayList<HomeImageTag> tags;

    private void getExploreContent() {
        showDialog();
        new GetExploreCollection()
                .setCommonParams()
                .runAsync((BaseServiceable.OnApiFinishListener<GetExploreCollection>) getExploreCollection -> {
                    dismissDialog();
                    if (getExploreCollection.isValidResponse()) {
                        topPoets = getExploreCollection.getTopPoets();
                        explorePoetry = getExploreCollection.getExplorePoetry();
                        tags = getExploreCollection.getTags();
                        updateUI();
                    } else {
                        showToast(getExploreCollection.getErrorMessage());
                        finish();
                    }
                });
    }

    private void updateUI() {
        flexTags.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            View cellTag = getInflatedView(R.layout.cell_tag);
            TagCellViewHolder tagCellViewHolder = new TagCellViewHolder(cellTag);
            tagCellViewHolder.txtSherCount.setText("");
            tagCellViewHolder.txtTagName.setText(tags.get(i).getTagName());
            tagCellViewHolder.viewTagColor.setBackgroundColor(MyHelper.getTagColor(i));
            flexTags.addView(cellTag);
            cellTag.setTag(R.id.tag_data, tags.get(i));
            cellTag.setOnClickListener(onTagClick);
            FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) cellTag.getLayoutParams();
            lp.setFlexBasisPercent(0.5f);
            cellTag.setLayoutParams(lp);
        }
        rvTopPoets.setAdapter(new ExplorePoetRecyclerAdapter(getActivity(), topPoets));
        rvExplorePoetry.setAdapter(new ExploreShayariRecyclerAdapter(getActivity(), explorePoetry));

        layTopPoets.setVisibility(CollectionUtils.isEmpty(topPoets)?View.GONE:View.VISIBLE);
        layExplorePoetry.setVisibility(CollectionUtils.isEmpty(explorePoetry)?View.GONE:View.VISIBLE);
        layExploreTags.setVisibility(CollectionUtils.isEmpty(tags)?View.GONE:View.VISIBLE);
    }

    private View.OnClickListener onTagClick = v -> {
        HomeImageTag sherTag = (HomeImageTag) v.getTag(R.id.tag_data);
        startActivity(SherTagOccasionActivity.getInstance(SearchActivity.this, sherTag));
    };

    static class TagCellViewHolder {
        @BindView(R.id.viewTagColor)
        View viewTagColor;
        @BindView(R.id.txtTagName)
        TitleTextViewType6 txtTagName;
        @BindView(R.id.txtSherCount)
        TextView txtSherCount;

        TagCellViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @OnClick({R.id.clear_btn, R.id.mic_btn, R.id.txtExploreMoreTags})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_btn:
                searchEditBox.clearFocus();
                hideKeyBoard();
                searchEditBox.setText("");
                txtTitle.setVisibility(View.VISIBLE);
//                searchEditBox.setCursorVisible(false);
                break;
            case R.id.mic_btn:
                startVoiceRecognitionActivity();
                break;
            case R.id.txtExploreMoreTags:
                startActivity(ShayariActivity.getInstance(getActivity(), Enums.SHER_COLLECTION_TYPE.TAG));
                break;
        }
    }

    private void startVoiceRecognitionActivity() {
        try{
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition");
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch(ActivityNotFoundException e) {
            String appPackageName = "org.Rekhta";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchEditBox.setText(matches.get(0));
            searchedText = matches.get(0);
            clearBtn.setVisibility(View.VISIBLE);
            searchEditBox.setCursorVisible(false);
            currentViewType = VIEW_TYPE_SEARCH;
            updateView();
//            showResult();
        }
    }

    private void updateLanguageSpecificContent() {
        txtTitle.setText(MyHelper.getString(R.string.search));
        searchEditBox.setHint(MyHelper.getString(R.string.search));
        txtExplorePoetryTitle.setText(MyHelper.getString(R.string.explore_poetry));
        txtExploreTagsTitle.setText(MyHelper.getString(R.string.explore_tags));
        txtTopPoetsTitle.setText(MyHelper.getString(R.string.top_searched_poets));
        txtExploreMoreTags.setText(MyHelper.getString(R.string.see_more_tags));
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        lazyRefreshTabPositioning(tabLayout, viewpager);
        getExploreContent();
        updateLanguageSpecificContent();
    }
    //Saved images will be shown here
    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
    }
}