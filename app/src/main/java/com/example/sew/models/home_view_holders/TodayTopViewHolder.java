package com.example.sew.models.home_view_holders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.fragments.TrendingSherFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.HomeTodayTop;
import com.example.sew.models.Para;
import com.example.sew.models.WordContainer;
import com.example.sew.views.CommonImageView;
import com.example.sew.views.TitleTextViewType6;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodayTopViewHolder extends BaseHomeViewHolder {
    public static TodayTopViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        TodayTopViewHolder todayTopViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_trending_sher, baseActivity);
            todayTopViewHolder = new TodayTopViewHolder(convertView, baseActivity);
        } else
            todayTopViewHolder = (TodayTopViewHolder) convertView.getTag();
        convertView.setTag(todayTopViewHolder);
        todayTopViewHolder.setConvertView(convertView);
        return todayTopViewHolder;
    }

    private TodayTopViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
        getActivity().registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showContent();
            }
        }, BROADCAST_FAVORITE_UPDATED);
        pagerTrendingSher.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                showContent();
            }
        });
    }

    @BindView(R.id.pagerTrendingSher)
    ViewPager pagerTrendingSher;
    @BindView(R.id.txtTrendingSherTitle)
    TitleTextViewType6 txtTrendingSherTitle;
    @BindView(R.id.laySher)
    LinearLayout laySher;
    @BindView(R.id.sher_poet_name)
    TitleTextViewType6 sherPoetName;
    @BindView(R.id.flexTags)
    FlexboxLayout flexTags;
    @BindView(R.id.sher_ghazalIcon)
    ImageView sherGhazalIcon;
    @BindView(R.id.sher_shareIcon)
    ImageView sherShareIcon;
    @BindView(R.id.sher_heartIcon)
    ImageView sherHeartIcon;
    @BindView(R.id.imgPrevious)
    CommonImageView imgPrevious;
    @BindView(R.id.txtNextTitle)
    TextView txtNextTitle;
    @BindView(R.id.imgNext)
    CommonImageView imgNext;
    @BindView(R.id.poetList_layout)
    LinearLayout poetListLayout;
    @BindView(R.id.layNext)
    View layNext;
    private int currentIndex = -1;
    ArrayList<HomeTodayTop> todayTops;

    public BaseHomeViewHolder loadData(ArrayList<HomeTodayTop> todayTops) {
        this.todayTops = todayTops;
        if (pagerTrendingSher.getAdapter() == null) {
            pagerTrendingSher.setAdapter(new TrendingSherPagerAdapter(getActivity().getSupportFragmentManager(), todayTops));
            new Handler()
                    .postDelayed(() -> {
                        pagerTrendingSher.setCurrentItem((currentIndex + 1) % todayTops.size());
                        pagerTrendingSher.setCurrentItem(currentIndex % todayTops.size());
                    }, 500);
        }

        if (currentIndex == -1) {
            currentIndex = (todayTops.size() * TrendingSherPagerAdapter.LOOPS_COUNT) / 2;
            showContent();
        }
        return this;
    }

    private void showContent() {
        if (currentIndex < 0 || CollectionUtils.isEmpty(todayTops))
            return;
        txtTrendingSherTitle.setText(MyHelper.getString(R.string.trending_sher));
        HomeTodayTop todayTop = todayTops.get(currentIndex % todayTops.size());
        txtNextTitle.setText(todayTop.getNextTitle());

//        RenderHelper.RenderContentBuilder.Builder(getActivity())
//                .setTextAlignment(Enums.TEXT_ALIGNMENT.CENTER)
//                .setParas(todayTop.getRenderText())
//                .setLayParaContainer(laySher)
//                .setLeftRightPadding((int) Utils.pxFromDp(28))
//                .setOnWordLongClick(onWordLongClick)
//                .setOnWordClick(onWordClickListener)
//                .Build();
//        sherPoetName.setText(todayTop.getPoetName());
//        sherPoetName.setTag(R.id.tag_data, todayTop);
//        flexTags.removeAllViews();
        sherGhazalIcon.setTag(R.id.tag_data, todayTop);
        sherShareIcon.setTag(R.id.tag_data, todayTop);
        sherHeartIcon.setTag(R.id.tag_data, todayTop);
        sherGhazalIcon.setVisibility(TextUtils.isEmpty(todayTop.getParentSlug()) ? View.GONE : View.VISIBLE);
        getActivity().addFavoriteClick(sherHeartIcon, todayTop.getContentId(), Enums.FAV_TYPES.CONTENT.getKey());
        getActivity().updateFavoriteIcon(sherHeartIcon, todayTop.getContentId());
//        flexTags.setVisibility(CollectionUtils.isEmpty(todayTop.getHomeTags()) ? View.GONE : View.VISIBLE);
//        for (HomeImageTag tag : todayTop.getHomeTags()) {
//            View view = getInflatedView(R.layout.cell_home_tag, getActivity());
//            TextView txtTagName = view.findViewById(R.id.txtTagName);
//            txtTagName.setText(String.format("#%s", tag.getTagName()));
//            view.setTag(tag);
//            view.setOnClickListener(v -> {
//                HomeImageTag tag1 = (HomeImageTag) v.getTag();
//                getActivity().startActivity(SherCollectionActivity.getInstance(getActivity(), tag1));
//            });
//            flexTags.addView(view);
//        }
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
        new MeaningBottomPopupWindow(getActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();

    };

    @OnClick({R.id.sher_ghazalIcon, R.id.sher_shareIcon, R.id.sher_heartIcon, R.id.imgPrevious, R.id.layNext, R.id.sher_poet_name})
    public void onViewClicked(View view) {
        HomeTodayTop todayTop = null;
        if (view.getTag(R.id.tag_data) instanceof HomeTodayTop)
            todayTop = (HomeTodayTop) view.getTag(R.id.tag_data);
        switch (view.getId()) {
            case R.id.sher_shareIcon:

                if (todayTop == null)
                    return;
                StringBuilder stringBuilder = new StringBuilder();
                String sherContentText = MyHelper.getSherContentText(todayTop.getRenderText());
                if (TextUtils.isEmpty(sherContentText))
                    getActivity().showToast("Sher not found");
                else
                    stringBuilder.append(sherContentText);
                stringBuilder.append("\n\n");
                stringBuilder.append(todayTop.getPoetName());
                stringBuilder.append("\n");
                stringBuilder.append(todayTop.getUrl());
                BaseActivity.shareTheUrl(stringBuilder.toString(), getActivity());
                break;
            case R.id.imgPrevious:
                if (currentIndex == 0)
                    currentIndex = todayTops.size() - 1;
                else
                    --currentIndex;
                pagerTrendingSher.setCurrentItem(currentIndex);
                showContent();
                break;
            case R.id.layNext:
                ++currentIndex;
                pagerTrendingSher.setCurrentItem(currentIndex);
                showContent();
                break;
            case R.id.sher_poet_name:
                getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), todayTop.getPoetId()));
                break;
            case R.id.sher_ghazalIcon:
                getActivity().startActivity(RenderContentActivity.getInstance(getActivity(), todayTop.getParentSlug()));
                break;
        }
    }

    class TrendingSherPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<HomeTodayTop> todayTops;
        public static final int LOOPS_COUNT = 1000;

        public TrendingSherPagerAdapter(@NonNull FragmentManager fm, ArrayList<HomeTodayTop> todayTops) {
            super(fm);
            this.todayTops = todayTops;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return TrendingSherFragment.getInstance(todayTops.get(position % todayTops.size()));
        }


        @Override
        public int getCount() {
            if (!CollectionUtils.isEmpty(todayTops)) {
                return todayTops.size() * LOOPS_COUNT; // simulate infinite by big number of products
            } else {
                return 0;
            }
        }
    }
}
