package com.example.sew.adapters;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.SherTag;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType3;
import com.example.sew.views.TitleTextViewType6;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TagsAdapter extends BaseMyAdapter {
    private ArrayList<SherTag> popularTags;
    private HashMap<String, ArrayList<SherTag>> allTags;
    private ArrayList<String> tagsSections;
    private final int VIEW_TYPE_TRENDING = 0;
    private final int VIEW_TYPE_ALL = 1;
    private boolean isSearch;
    public TagsAdapter(BaseActivity activity, ArrayList<SherTag> popularTags, HashMap<String, ArrayList<SherTag>> allTags, ArrayList<String> tagsSections,boolean isSearch) {
        super(activity);
        this.popularTags = popularTags;
        this.allTags = allTags;
        this.tagsSections = tagsSections;
        this.isSearch= isSearch;
    }

    @Override
    public int getCount() {
            return tagsSections.size() + (CollectionUtils.isEmpty(popularTags) ? 0 : 1);
    }

    @Override
    public ArrayList<SherTag> getItem(int position) {
            int actualPosition = CollectionUtils.isEmpty(popularTags) ? position : position - 1;
            return allTags.get(tagsSections.get(actualPosition));

    }

    @Override
    public int getItemViewType(int position) {
            return (position == 0 && !CollectionUtils.isEmpty(popularTags)) ? VIEW_TYPE_TRENDING : VIEW_TYPE_ALL;
    }

    @Override
    public int getViewTypeCount() {
            return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_TRENDING:
                TrendingViewHolder trendingViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_trending_tags);
                    trendingViewHolder = new TrendingViewHolder(convertView);
                } else
                    trendingViewHolder = (TrendingViewHolder) convertView.getTag();
                convertView.setTag(trendingViewHolder);
                trendingViewHolder.txtAllTagsTitle.setText(MyHelper.getString(R.string.allTags).toUpperCase());
                trendingViewHolder.txtTrendingTagsTitle.setText(MyHelper.getString(R.string.trendingTags).toUpperCase());
                TrendingTagsAdapter trendingTagsAdapter = new TrendingTagsAdapter(getActivity(), popularTags);
                trendingTagsAdapter.setOnTagClickListener(onTagClick);
                trendingViewHolder.rvTrendingTags.setAdapter(trendingTagsAdapter);
                break;
            case VIEW_TYPE_ALL:
                ArrayList<SherTag> sherTags = getItem(position);
                TagsViewHolder tagsViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_tags_list);
                    tagsViewHolder = new TagsViewHolder(convertView);
                } else
                    tagsViewHolder = (TagsViewHolder) convertView.getTag();
                convertView.setTag(tagsViewHolder);
                tagsViewHolder.txtSectionTitle.setText(tagsSections.get(CollectionUtils.isEmpty(popularTags) ? position : position - 1));
                tagsViewHolder.txtSectionTitle.setBackgroundColor(CollectionUtils.isEmpty(sherTags) ? Color.BLACK : sherTags.get(0).getTagColor());
                tagsViewHolder.flexTags.removeAllViews();
                for (int i = 0; i < sherTags.size(); i++) {
                    View cellTag = getInflatedView(R.layout.cell_tag);
                    TagCellViewHolder tagCellViewHolder = new TagCellViewHolder(cellTag);
                    tagCellViewHolder.txtSherCount.setText(sherTags.get(i).getTotalSher());
                    tagCellViewHolder.txtTagName.setText(sherTags.get(i).getTagName());
                    tagCellViewHolder.viewTagColor.setBackgroundColor(sherTags.get(i).getTagColor());
                    tagsViewHolder.flexTags.addView(cellTag);
                    cellTag.setTag(R.id.tag_data, sherTags.get(i));
                    cellTag.setOnClickListener(onTagClick);
                    FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) cellTag.getLayoutParams();
                    lp.setFlexBasisPercent(0.5f);
                    cellTag.setLayoutParams(lp);
                }
                break;
        }
        return convertView;
    }

    private View.OnClickListener onTagClick;

    public void setOnTagClickListener(View.OnClickListener onTagClick) {
        this.onTagClick = onTagClick;
    }

    static class TagsViewHolder {
        @BindView(R.id.rvTags)
        RecyclerView rvTags;
        @BindView(R.id.flexTags)
        FlexboxLayout flexTags;
        @BindView(R.id.txtSectionTitle)
        TextView txtSectionTitle;

        TagsViewHolder(View view) {
            ButterKnife.bind(this, view);
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(MyApplication.getContext());
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
            rvTags.setLayoutManager(flexboxLayoutManager);
        }
    }

    static class TagCellViewHolder {
        @BindView(R.id.viewTagColor)
        View viewTagColor;
        @BindView(R.id.txtTagName)
        TextView txtTagName;
        @BindView(R.id.txtSherCount)
        TextView txtSherCount;

        TagCellViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class TrendingViewHolder {
        @BindView(R.id.txtTrendingTagsTitle)
        TitleTextViewType6 txtTrendingTagsTitle;
        @BindView(R.id.rvTrendingTags)
        RecyclerView rvTrendingTags;
        @BindView(R.id.txtAllTagsTitle)
        TitleTextViewType6 txtAllTagsTitle;
        @BindView(R.id.linear)
        LinearLayout linear;
        TrendingViewHolder(View view) {
            ButterKnife.bind(this, view);
            rvTrendingTags.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    public class TrendingTagsAdapter extends BaseRecyclerAdapter {
        private ArrayList<SherTag> sherTags;
        private View.OnClickListener onTagClickListener;

        public void setOnTagClickListener(View.OnClickListener onTagClickListener) {
            this.onTagClickListener = onTagClickListener;
        }

        TrendingTagsAdapter(BaseActivity activity, ArrayList<SherTag> sherTags) {
            super(activity);
            this.sherTags = sherTags;
        }

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TrendingTagViewHolder(getInflatedView(R.layout.cell_item_trending_tag));
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            if (!(holder instanceof TrendingTagViewHolder))
                return;
            TrendingTagViewHolder trendingTagViewHolder = (TrendingTagViewHolder) holder;
            SherTag sherTag = sherTags.get(position);
            holder.itemView.setTag(R.id.tag_data, sherTag);
            trendingTagViewHolder.txtTagName.setText(sherTag.getTagName());
            trendingTagViewHolder.layTagContainer.setBackgroundColor(sherTag.getTagColor());
        }

        @Override
        public int getItemCount() {
            return sherTags.size();
        }

        class TrendingTagViewHolder extends BaseViewHolder {
            @BindView(R.id.txtTagName)
            TextView txtTagName;
            @BindView(R.id.layTagContainer)
            LinearLayout layTagContainer;

            @OnClick()
            void onItemClick(View v) {
                if (onTagClickListener != null)
                    onTagClickListener.onClick(v);
            }

            TrendingTagViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
