package com.example.sew.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.adapters.PoetsAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetPoetLists;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.Poet;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class PoetsFragment extends BaseFragment {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoets)
    PagingListView lstPoets;
    @BindView(R.id.edSearch)
    EditText edSearch;
    @BindView(R.id.laySearch)
    LinearLayout laySearch;
    @BindView(R.id.txtNoData)
    TextView txtNoData;

    @OnItemClick(R.id.lstPoets)
    void onPoetClick(View convertView) {
        Poet poet = (Poet) convertView.getTag(R.id.tag_data);
        if(poet!=null && !TextUtils.isEmpty(poet.getPoetId()))
            startActivity(PoetDetailActivity.getInstance(getActivity(), poet.getPoetId()));
    }

    @OnEditorAction(R.id.edSearch)
    void onSearchKeyboardAction(int actionId) {
        hideKeyBoard();
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            showDialog();
            getPoetLists(getEditTextData(edSearch));
        }
    }

    @OnTextChanged(R.id.edSearch)
    void onSearchTextChanged() {
        String searchText = getEditTextData(edSearch);
        if (TextUtils.isEmpty(searchText))
            getPoetLists(searchText);
    }

    public static PoetsFragment getInstance(String targetId, boolean shouldShowSearch) {
        PoetsFragment poetsFragment = new PoetsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TARGET_ID, targetId);
        bundle.putBoolean(SHOULD_SHOW_SEARCH_BAR, shouldShowSearch);
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poets, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private String targetId;
    private PoetsAdapter poetsAdapter;
    private ArrayList<Poet> poets = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        targetId = getArguments() != null ? getArguments().getString(TARGET_ID, "") : null;
        boolean shouldShowSearchBar = getArguments() != null && getArguments().getBoolean(SHOULD_SHOW_SEARCH_BAR, false);
        lstPoets.setPagingableListener(() -> {
            if (getPoetLists != null) {
                getPoetLists.loadMoreData();
                lstPoets.setIsLoading(true);
            }
        });
        getPoetLists(null);
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        laySearch.setVisibility(shouldShowSearchBar ? View.VISIBLE : View.GONE);
        edSearch.setHint(MyHelper.getString(R.string.search_poets_name));
    }

    private GetPoetLists getPoetLists;

    private void getPoetLists(String keyword) {

        lstPoets.setIsLoading(true);
        getPoetLists = new GetPoetLists();
        getPoetLists.setKeyword(TextUtils.isEmpty(keyword) ? "" : keyword)
                .setTargetId(targetId)
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetPoetLists>) getPoetLists -> {
                    dismissDialog();
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
                    if (getPoetLists.isValidResponse()) {
                        lstPoets.setIsLoading(false);
                        if (getPoetLists.isFirstPage())
                            poets.clear();
                        poets.addAll(getPoetLists.getPoets());
                        if (getPoetLists.getPoets() == null || getPoetLists.getPoets().size() == 0 || poets.size() == getPoetLists.getTotalCount()) {
                            lstPoets.setHasMoreItems(false);
                            if(getPoetLists.getPoets().size() == 0) {
                                //noRecordFoundPopup();
                                txtNoData.setVisibility(View.VISIBLE);
                                lstPoets.setVisibility(View.GONE);
                                txtNoData.setText(MyHelper.getString(R.string.no_records_found));
                            }else{
                                txtNoData.setVisibility(View.GONE);
                                lstPoets.setVisibility(View.VISIBLE);
                            }
                        }else {
                            lstPoets.setHasMoreItems(true);
                            txtNoData.setVisibility(View.GONE);
                            lstPoets.setVisibility(View.VISIBLE);
                        }
                        updateUI();
                    } else
                        showToast(getPoetLists.getErrorMessage());
                });
    }


    private void updateUI() {
        if (poetsAdapter == null) {
            poetsAdapter = new PoetsAdapter(GetActivity(), poets);
            lstPoets.setAdapter(poetsAdapter);
        } else
            poetsAdapter.notifyDataSetChanged();
        edSearch.setHint(MyHelper.getString(R.string.search_poets_name));
        edSearch.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }
}
