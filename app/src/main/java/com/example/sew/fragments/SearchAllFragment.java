package com.example.sew.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.example.sew.R;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.adapters.SearchAllAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetSearchAll;
import com.example.sew.apis.PostSearchOnnLoadDemand;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.SearchContent;
import com.example.sew.models.SearchContentAll;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class SearchAllFragment extends BaseSearchFragment {
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstSearchResult)
    PagingListView lstSearchResult;
    @BindView(R.id.txtNoData)
    TextView txtNoData;
    public static BaseSearchFragment getInstance(String searchedText) {
        return getInstance(new SearchAllFragment(), searchedText);
    }

    @OnItemClick(R.id.lstSearchResult)
    void onItemClicked(View convertView) {
        if (convertView.getTag(R.id.tag_data) instanceof SearchContentAll.SearchPoet)
            startActivity(PoetDetailActivity.getInstance(getActivity(), ((SearchContentAll.SearchPoet) convertView.getTag(R.id.tag_data)).getEntityId()));
        else if (convertView.getTag(R.id.tag_data) instanceof SearchContent) {
            SearchContent content = (SearchContent) convertView.getTag(R.id.tag_data);
            startActivity(RenderContentActivity.getInstance(getActivity(), content.getId()));
        }
    }

    private View.OnClickListener onPoetClickListener = this::onItemClicked;

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        if (!CollectionUtils.isEmpty(allData)) {
            allData.clear();
            updateUI();
            getPoetAudios(searchKeyword);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private PostSearchOnnLoadDemand postSearchOnnLoadDemand;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
        lstSearchResult.setPagingableListener(() -> {
            if (postSearchOnnLoadDemand == null) {
                postSearchOnnLoadDemand = new PostSearchOnnLoadDemand();
                postSearchOnnLoadDemand.setKeyword(searchKeyword).addPagination()
                        .runAsync((BaseServiceable.OnApiFinishListener<PostSearchOnnLoadDemand>) postSearchOnnLoadDemand -> {
                            if (postSearchOnnLoadDemand.isValidResponse()) {
                                if (searchContentAll.getSearchContents().size() > 0) {
                                    searchContentAll = postSearchOnnLoadDemand.getSearchContentAll();
                                    allData.addAll(searchContentAll.getSearchContents());
                                    lstSearchResult.setIsLoading(false);
                                    lstSearchResult.setHasMoreItems(true);
                                    generateDataOnDemand();
                                    updateUI();
                                }else {
                                    lstSearchResult.setHasMoreItems(false);
//                                    txtNoData.setVisibility(View.VISIBLE);
//                                    lstSearchResult.setVisibility(View.GONE);
//                                    txtNoData.setText(MyHelper.getString(R.string.no_records_found));
                                }
                            } else
                                showToast(postSearchOnnLoadDemand.getErrorMessage());

                        });
            } else {
                postSearchOnnLoadDemand.loadMoreData();
                lstSearchResult.setIsLoading(true);
                searchContentAll = postSearchOnnLoadDemand.getSearchContentAll();
                allData.addAll(searchContentAll.getSearchContents());
                generateDataOnDemand();
                updateUI();
            }

        });


        if (!TextUtils.isEmpty(getSearchText()))
            getPoetAudios(getSearchText());
        ViewCompat.setNestedScrollingEnabled(lstSearchResult, true);
    }

    private void generateDataOnDemand() {
        if (searchContentAll == null)
            return;
//        allData.add("");
        if (!CollectionUtils.isEmpty(searchContentAll.getPoets())) {
            //allData.add("");
            allData.add(searchContentAll.getPoets());
        }
        if (!CollectionUtils.isEmpty(searchContentAll.getSearchContents())) {
            // allData.add("");
            for (int i = 0; i < Math.min(5, searchContentAll.getSearchContents().size()); i++)
                allData.add(searchContentAll.getSearchContents().get(i));
        }
        if (!CollectionUtils.isEmpty(searchContentAll.getDictionary())) {
            // allData.add("");
            allData.addAll(searchContentAll.getDictionary());
        }
        if (!CollectionUtils.isEmpty(searchContentAll.getSearchContents()) && searchContentAll.getSearchContents().size() > 5) {
            //allData.add("");
            for (int i = 5; i < searchContentAll.getSearchContents().size(); i++)
                allData.add(searchContentAll.getSearchContents().get(i));
        }
    }

    private GetSearchAll getSearchAll;

    private void getPoetAudios(String keyWord) {
        this.searchKeyword = keyWord;
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getSearchAll = new GetSearchAll();
        getSearchAll.setKeyword(keyWord)
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetSearchAll>) getAudioListWithPaging -> {
                    if (getAudioListWithPaging.isValidResponse()) {
                        lstSearchResult.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        searchContentAll = getSearchAll.getSearchContent();
                        //  lstSearchResult.setHasMoreItems(true);
                        if (searchContentAll== null || searchContentAll.getSearchContents().size() == 0) {
                            lstSearchResult.setHasMoreItems(false);
                            if(searchContentAll.getSearchContents().size() == 0) {
                                txtNoData.setVisibility(View.VISIBLE);
                                lstSearchResult.setVisibility(View.GONE);
                                txtNoData.setText(MyHelper.getString(R.string.no_records_found));
                            }else{
                                txtNoData.setVisibility(View.GONE);
                                lstSearchResult.setVisibility(View.VISIBLE);
                            }
                        }else {
                            lstSearchResult.setHasMoreItems(true);
                            txtNoData.setVisibility(View.GONE);
                            lstSearchResult.setVisibility(View.VISIBLE);
                        }
                        generateData();
                        updateUI();
                    } else
                        showToast(getAudioListWithPaging.getErrorMessage());
                });
    }

    private SearchContentAll searchContentAll;
    private SearchAllAdapter searchAllAdapter;
    private ArrayList<Object> allData = new ArrayList<>();

    private void updateUI() {
        if (searchAllAdapter == null) {
            searchAllAdapter = new SearchAllAdapter(GetActivity(), allData);
            searchAllAdapter.setOnPoetClickListener(onPoetClickListener);
            lstSearchResult.setAdapter(searchAllAdapter);
        } else
            searchAllAdapter.notifyDataSetChanged();
    }

    private void generateData() {
        if (searchContentAll == null)
            return;
        allData.clear();
        allData.add(String.format("%s \"%s\"", MyHelper.getString(R.string.showing_result_for), searchKeyword));
        if (!CollectionUtils.isEmpty(searchContentAll.getPoets())) {
            allData.add(MyHelper.getString(R.string.more_matches));
            allData.add(searchContentAll.getPoets());
        }
        if (!CollectionUtils.isEmpty(searchContentAll.getSearchContents())) {
            allData.add(MyHelper.getString(R.string.top_5_matches));
            for (int i = 0; i < Math.min(5, searchContentAll.getSearchContents().size()); i++)
                allData.add(searchContentAll.getSearchContents().get(i));
        }
        if (!CollectionUtils.isEmpty(searchContentAll.getDictionary())) {
            allData.add(MyHelper.getString(R.string.dict_matches));
            allData.addAll(searchContentAll.getDictionary());
        }
        if (!CollectionUtils.isEmpty(searchContentAll.getSearchContents()) && searchContentAll.getSearchContents().size() > 5) {
            allData.add(MyHelper.getString(R.string.total_matches));
            for (int i = 5; i < searchContentAll.getSearchContents().size(); i++)
                allData.add(searchContentAll.getSearchContents().get(i));
        }
    }

    private String searchKeyword;

    @Override
    void onSearchTextChanged(String newText) {
        allData.clear();
        updateUI();
        getPoetAudios(newText);
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }
}
