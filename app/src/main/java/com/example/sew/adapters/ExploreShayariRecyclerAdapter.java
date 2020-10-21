package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.ProseShayariCollectionActivity;
import com.example.sew.activities.SherCollectionActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.ContentType;
import com.example.sew.models.HomeProseCollection;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExploreShayariRecyclerAdapter extends BaseRecyclerAdapter {
    private ArrayList<HomeProseCollection> homeProseCollections;

    public ExploreShayariRecyclerAdapter(BaseActivity activity, ArrayList<HomeProseCollection> homeProseCollections) {
        super(activity);
        this.homeProseCollections = homeProseCollections;
    }


    public HomeProseCollection getItem(int position) {
        return homeProseCollections.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShayariViewHolder(getInflatedView(R.layout.cell_explore_shayari));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof ShayariViewHolder))
            return;
        HomeProseCollection topPoet = getItem(position);
        ShayariViewHolder shayariViewHolder = (ShayariViewHolder) holder;
        shayariViewHolder.itemView.setTag(R.id.tag_data, topPoet);
        shayariViewHolder.txtShayariName.setText(topPoet.getContentTypeName().toUpperCase());
        shayariViewHolder.layTag.setBackgroundColor(MyHelper.getTagColorForSearch(position));
    }

    @Override
    public int getItemCount() {
        return homeProseCollections.size();
    }

    class ShayariViewHolder extends BaseViewHolder {
        @BindView(R.id.txtShayariName)
        TitleTextViewType6 txtShayariName;
        @BindView(R.id.layTag)
        LinearLayout layTag;

        @OnClick()
        void onItemClick(View view) {
            HomeProseCollection proseCollection = (HomeProseCollection) view.getTag(R.id.tag_data);
            switch (proseCollection.getProseShayariCategory()) {
                case POET:
                    getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), proseCollection.getPoetId(), MyHelper.getContentById(proseCollection.getContentTypeId())));
                    break;
                case COLLECTION:
                    ContentType contentType = MyHelper.getContentById(proseCollection.getContentTypeId());
                    if (contentType.getListRenderingFormat() == Enums.LIST_RENDERING_FORMAT.SHER||contentType.getListRenderingFormat() == Enums.LIST_RENDERING_FORMAT.QUOTE) {
                        HomeSherCollection homeOccasionCollection = MyHelper.getDummyDefaultSherCollection(proseCollection.getContentTypeId(), proseCollection.getName());
                        getActivity().startActivity(SherTagOccasionActivity.getInstance(getActivity(), homeOccasionCollection));
                    } else
                        getActivity().startActivity(ProseShayariCollectionActivity.getInstance(getActivity(), proseCollection.getId(), MyHelper.getContentById(proseCollection.getContentTypeId()),proseCollection.getsEO_Slug()));
                    break;
            }
        }

        ShayariViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
