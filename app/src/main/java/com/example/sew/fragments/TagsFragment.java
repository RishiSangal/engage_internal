package com.example.sew.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;

import com.example.sew.R;
import com.example.sew.activities.SherCollectionActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.adapters.TagsAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetTagLists;
import com.example.sew.common.Enums;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.SherTag;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class TagsFragment extends BaseFragment {
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.edSearch)
    EditText edSearch;
    @BindView(R.id.lstTags)
    PagingListView lstTags;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtDescription)
    TextView txtDescription;
    @BindView(R.id.txtNoData)
    TextView txtNoData;
    private View.OnClickListener onTagClick = v -> {
        SherTag sherTag = (SherTag) v.getTag(R.id.tag_data);
        startActivity(SherTagOccasionActivity.getInstance(GetActivity(), sherTag));
    };

    @OnEditorAction(R.id.edSearch)
    void onSearchKeyboardAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyBoard();
            if (!TextUtils.isEmpty(getEditTextData(edSearch))) {

            }
        }
    }

    @OnTextChanged(R.id.edSearch)
    void onSearchTextChanged() {
        String searchText = getEditTextData(edSearch);
        if (TextUtils.isEmpty(searchText))
            updateUI(false);
        else
            filterTags(searchText, true);
    }

    public static TagsFragment getInstance() {
        TagsFragment poetsFragment = new TagsFragment();
        Bundle bundle = new Bundle();
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private TagsAdapter tagsAdapter;
    private ArrayList<SherTag> popularTags = new ArrayList<>();
    private ArrayList<String> tagsSections;
    private HashMap<String, ArrayList<SherTag>> allTags;
    private ArrayList<String> tagsSectionsFiltered = new ArrayList<>();
    private HashMap<String, ArrayList<SherTag>> allTagsFiltered = new HashMap<>();
    private Enums.SHER_COLLECTION_TYPE collectionType;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateLanguageSpecificValues();
        getTagsList();
        txtTitle.setText(MyHelper.getString(R.string.shayariByTags));
        txtDescription.setText(MyHelper.getString(R.string.shayariByTags_description));
        ViewCompat.setNestedScrollingEnabled(lstTags, true);
    }

    private void getTagsList() {
        shimmerViewContainer.startShimmer();
        txtNoData.setVisibility(View.GONE);
        edSearch.setEnabled(false);
        shimmerViewContainer.setVisibility(View.VISIBLE);
        edSearch.setEnabled(false);
        new GetTagLists()
                .runAsync((BaseServiceable.OnApiFinishListener<GetTagLists>) getSherTags -> {
                    dismissDialog();
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
                    edSearch.setEnabled(true);
                    if (getSherTags.isValidResponse()) {
                        popularTags.clear();
                        allTags = getSherTags.getAllTags();
                        popularTags.addAll(getSherTags.getTopTags());
                        tagsSections = getSherTags.getTagsSections();
                        filterTags("", false);
                    } else
                        showToast(getSherTags.getErrorMessage());
                });
    }

    private void updateUI(boolean isSearch) {
        if (allTagsFiltered.size() == 0) {
            lstTags.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(MyHelper.getString(R.string.no_records_found));
        } else {
            lstTags.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
            if (isSearch) {
                if (TextUtils.isEmpty(getEditTextData(edSearch))) {
                    tagsSectionsFiltered.clear();
                    allTagsFiltered.clear();
                    tagsSectionsFiltered.addAll(tagsSections);
                    allTagsFiltered.putAll(allTags);
                }
                tagsAdapter = new TagsAdapter(GetActivity(), null, allTagsFiltered, tagsSectionsFiltered, isSearch);
                tagsAdapter.setOnTagClickListener(onTagClick);
                lstTags.setAdapter(tagsAdapter);
            } else {
                if (TextUtils.isEmpty(getEditTextData(edSearch))) {
                    tagsSectionsFiltered.clear();
                    allTagsFiltered.clear();
                    tagsSectionsFiltered.addAll(tagsSections);
                    allTagsFiltered.putAll(allTags);
                }
                tagsAdapter = new TagsAdapter(GetActivity(), popularTags, allTagsFiltered, tagsSectionsFiltered, isSearch);
                tagsAdapter.setOnTagClickListener(onTagClick);
                lstTags.setAdapter(tagsAdapter);
            }
            lstTags.setHasMoreItems(false);
        }
    }

    private void updateLanguageSpecificValues() {
        edSearch.setHint(MyHelper.getString(R.string.searchwithintags));
        txtTitle.setText(MyHelper.getString(R.string.shayariByTags));
        txtDescription.setText(MyHelper.getString(R.string.shayariByTags_description));
        edSearch.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
    }

    String currentKeyword = "";

    private void filterTags(String keyword, boolean isSearch) {
        if (CollectionUtils.isEmpty(tagsSections) ||
                allTags == null ||
                allTags.size() == 1
        )
            return;
        tagsSectionsFiltered.clear();
        allTagsFiltered.clear();
        if (TextUtils.isEmpty(keyword)) {
            tagsSectionsFiltered.addAll(tagsSections);
            allTagsFiltered.putAll(allTags);
        } else {
            for (String tagSection : tagsSections) {
                ArrayList<SherTag> sherTags = allTags.get(tagSection);
                for (SherTag sherTag : sherTags) {
                    if (sherTag.getNameEng().toUpperCase().contains(keyword.toUpperCase()) ||
                            sherTag.getNameHin().toUpperCase().contains(keyword.toUpperCase()) ||
                            sherTag.getNameUr().toUpperCase().contains(keyword.toUpperCase())) {
                        if (!tagsSectionsFiltered.contains(tagSection))
                            tagsSectionsFiltered.add(tagSection);
                        ArrayList<SherTag> currentSectionTags = allTagsFiltered.get(tagSection);
                        if (currentSectionTags == null)
                            currentSectionTags = new ArrayList<>();
                        currentSectionTags.add(sherTag);
                        allTagsFiltered.put(tagSection, currentSectionTags);
                    }
                }
            }
        }
        updateUI(isSearch);
    }

    private void clearAllData() {
        if (!CollectionUtils.isEmpty(popularTags))
            popularTags.clear();
        if (!CollectionUtils.isEmpty(tagsSections))
            tagsSections.clear();
        if (!CollectionUtils.isEmpty(tagsSectionsFiltered))
            tagsSectionsFiltered.clear();
        if (allTags != null && allTags.size() > 0)
            allTags.clear();
        if (allTagsFiltered != null && allTagsFiltered.size() > 0)
            allTagsFiltered.clear();
        edSearch.setText("");
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        clearAllData();
        updateUI(false);
        getTagsList();
        updateLanguageSpecificValues();
    }


}
