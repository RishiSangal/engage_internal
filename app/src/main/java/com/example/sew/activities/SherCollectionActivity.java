package com.example.sew.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;

import com.example.sew.R;
import com.example.sew.SherViewHolder;
import com.example.sew.adapters.SherCollectionAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetContentTypeTabByType;
import com.example.sew.apis.GetCoupletListWithPaging;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.PagingListView;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.BaseModel;
import com.example.sew.models.BaseSherTag;
import com.example.sew.models.HomeImageTag;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.models.Line;
import com.example.sew.models.OccasionCollection;
import com.example.sew.models.SherCollection;
import com.example.sew.models.SherContent;
import com.example.sew.models.SherTag;
import com.example.sew.models.WordContainer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sew.common.Enums.SHER_COLLECTION_TYPE.OTHER;

public class SherCollectionActivity extends BaseActivity {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;
    String targetId;
    public static final int PAGE_TYPE_BASIC = 1;
    public static final int PAGE_TYPE_CRITIQUE_CONTROLS = 2;
    public static final int PAGE_TYPE_CRITIQUE_ENABLED = 3;
    public int currentPageType = PAGE_TYPE_BASIC;

    private static Intent getInstance(BaseActivity activity, String targetId, Enums.SHER_COLLECTION_TYPE sherCollectionType, BaseModel data) {
        Intent intent = new Intent(activity, SherCollectionActivity.class);
        intent.putExtra(TARGET_ID, targetId);
        intent.putExtra(CONTENT_DATA_OBJ, data.getJsonObject().toString());
        intent.putExtra(SHER_COLLECTION_TYPE, sherCollectionType.toString());
        return intent;
    }

    public static Intent getInstance(BaseActivity activity, SherCollection sherCollection) {
        return getInstance(activity, sherCollection.getSherCollectionId(), Enums.SHER_COLLECTION_TYPE.TOP_20, sherCollection);
    }

    public static Intent getInstance(BaseActivity activity, HomeSherCollection homeSherCollection) {
        return getInstance(activity, homeSherCollection.getId(), homeSherCollection.getSherCollectionType(), homeSherCollection);
    }

    public static Intent getInstance(BaseActivity activity, BaseSherTag sherTag) {
        return getInstance(activity, sherTag.getTagId(), Enums.SHER_COLLECTION_TYPE.TAG, sherTag);
    }

    public static Intent getInstance(BaseActivity activity, OccasionCollection occasionCollection) {
        return getInstance(activity, occasionCollection.getSherCollectionId(), Enums.SHER_COLLECTION_TYPE.OCCASIONS, occasionCollection);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateLanguageSpecificContent();
        updateUI();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }

    Enums.SHER_COLLECTION_TYPE sherCollectionType;
    SherCollection sherCollection;
    OccasionCollection occasionCollection;
    HomeSherCollection homeSherCollection;
    BaseSherTag sherTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sher_collection);
        ButterKnife.bind(this);
        targetId = getIntent().getStringExtra(TARGET_ID);
        sherCollectionType = Enum.valueOf(Enums.SHER_COLLECTION_TYPE.class, getIntent().getStringExtra(SHER_COLLECTION_TYPE));

        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(CONTENT_DATA_OBJ));
            switch (sherCollectionType) {
                case TOP_20:
                    if (jsonObject.has("T"))
                        homeSherCollection = new HomeSherCollection(jsonObject);
                    else
                        sherCollection = new SherCollection(jsonObject);
                    break;
                case OCCASIONS:
                    if (jsonObject.has("T"))
                        homeSherCollection = new HomeSherCollection(jsonObject);
                    else
                        occasionCollection = new OccasionCollection(jsonObject);
                    break;
                case TAG:
                    if (jsonObject.has("TI"))
                        sherTag = new HomeImageTag(jsonObject);
                    else
                        sherTag = new SherTag(jsonObject);
                    break;
                case OTHER:
                    homeSherCollection = new HomeSherCollection(jsonObject);
                    break;
            }
        } catch (Exception e) {
            showToast(e.getLocalizedMessage());
            finish();
            return;
        }
        lstPoetContent.setPagingableListener(() -> {
            if (getCoupletListWithPaging != null) {
                getCoupletListWithPaging.loadMoreData();
                lstPoetContent.setIsLoading(true);
            }
        });
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        updateLanguageSpecificContent();
        getSherContent();
    }
    private GetCoupletListWithPaging getCoupletListWithPaging;

    private void getSherContent() {
        getCoupletListWithPaging = new GetCoupletListWithPaging();
        getCoupletListWithPaging
                .setTargetId(sherCollectionType == OTHER ? "" : targetId)
                .setContentTypeId(sherCollectionType == OTHER ? targetId : MyConstants.SHER_ID)
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetCoupletListWithPaging>) coupletListWithPaging1 -> {
                    if (coupletListWithPaging1.isValidResponse()) {
                        lstPoetContent.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getCoupletListWithPaging.isFirstPage())
                            sherContents.clear();
                        sherContents.addAll(getCoupletListWithPaging.getSherContents());
                        if (getCoupletListWithPaging.getSherContents() == null || getCoupletListWithPaging.getSherContents().size() == 0 || sherContents.size() == getCoupletListWithPaging.getTotalCount())
                            lstPoetContent.setHasMoreItems(false);
                        else
                            lstPoetContent.setHasMoreItems(true);
                        if (sherTag != null)
                            sherTag.setTagName(getCoupletListWithPaging.getName());
                        if (homeSherCollection != null)
                            homeSherCollection.setName(getCoupletListWithPaging.getName());
                        updateUI();
                    } else
                        showToast(coupletListWithPaging1.getErrorMessage());
                });

    }

    private ArrayList<SherContent> sherContents = new ArrayList<>();
    private SherCollectionAdapter sherCollectionAdapter;
    private View.OnClickListener onWordClickListener = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
        new MeaningBottomPopupWindow(getActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();

    };
    private View.OnClickListener onTagClickListener = v -> {
        SherTag sherTag = (SherTag) v.getTag(R.id.tag_data);
        startActivity(getInstance(getActivity(), sherTag));
        finish();
    };
    private View.OnClickListener onGhazalClickListener = v -> {
        SherContent sherContent = (SherContent) v.getTag(R.id.tag_data);
        startActivity(RenderContentActivity.getInstance(getActivity(), sherContent.getGhazalID()));
    };

    private void updateLanguageSpecificContent() {
        switch (sherCollectionType) {
            case TOP_20:
            case TAG:
                setHeaderTitle(MyHelper.getString(R.string.sher));
                if (sherTag != null && getCoupletListWithPaging != null)
                    sherTag.setTagName(getCoupletListWithPaging.getName());
                else if (homeSherCollection != null && getCoupletListWithPaging != null)
                    homeSherCollection.setName(getCoupletListWithPaging.getName());
                break;
            case OCCASIONS:
                if (occasionCollection != null)
                    setHeaderTitle(occasionCollection.getTitle());
                else
                    setHeaderTitle(homeSherCollection.getName());
                break;
            case OTHER:
                if (homeSherCollection != null && getCoupletListWithPaging != null)
                    homeSherCollection.setName(getCoupletListWithPaging.getName());
                break;
        }
    }

    private void updateUI() {
        if (sherCollectionAdapter == null) {
            sherCollectionAdapter = new SherCollectionAdapter(getActivity(), sherContents, getCoupletListWithPaging.getTotalCount());
            sherCollectionAdapter.setCollectionType(sherCollectionType);
            sherCollectionAdapter.setContent(sherTag);
            sherCollectionAdapter.setContent(sherCollection);
            sherCollectionAdapter.setContent(homeSherCollection);
            sherCollectionAdapter.setContent(occasionCollection);
//            sherCollectionAdapter.setOnWordClickListener(onWordClickListener);
            sherCollectionAdapter.setOnTagClick(onTagClickListener);
            sherCollectionAdapter.setOnGhazalClickListener(onGhazalClickListener);
            lstPoetContent.setAdapter(sherCollectionAdapter);
        } else
            sherCollectionAdapter.notifyDataSetChanged();
    }

}
