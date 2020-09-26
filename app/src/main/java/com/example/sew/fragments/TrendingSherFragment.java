package com.example.sew.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.binaryfork.spanny.Spanny;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.SherCollectionActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.adapters.SherCollectionAdapter;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.Utils;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.HomeImageTag;
import com.example.sew.models.HomeTodayTop;
import com.example.sew.models.Para;
import com.example.sew.models.SherTag;
import com.example.sew.models.WordContainer;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.util.CollectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrendingSherFragment extends BaseFragment {

    @BindView(R.id.laySher)
    LinearLayout laySher;
    @BindView(R.id.sher_poet_name)
    TextView sherPoetName;
    @BindView(R.id.flexTags)
    FlexboxLayout flexTags;
    private HomeTodayTop todayTop;

    @OnClick(R.id.sher_poet_name)
    public void onViewClicked(View view) {
        HomeTodayTop todayTop = null;
        if (view.getTag(R.id.tag_data) instanceof HomeTodayTop)
            todayTop = (HomeTodayTop) view.getTag(R.id.tag_data);
        if (todayTop != null)
            GetActivity().startActivity(PoetDetailActivity.getInstance(GetActivity(), todayTop.getPoetId()));
    }

    public static TrendingSherFragment getInstance(HomeTodayTop todayTop) {
        TrendingSherFragment poetsFragment = new TrendingSherFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HOME_TODAY_TOP, todayTop.getJsonObject().toString());
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_sher, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private View.OnLongClickListener onWordLongClick = v -> {
        Para para = (Para) v.getTag(R.id.tag_para);
        if (para == null)
            return false;
        String shareContentText = MyHelper.getSherContentText(para);
        MyHelper.shareTheText(shareContentText, GetActivity());
        MyHelper.copyToClipBoard(shareContentText, GetActivity());
        return false;
    };
    private View.OnClickListener onWordClickListener = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
        new MeaningBottomPopupWindow(GetActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();

    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            todayTop = getArguments() != null ? new HomeTodayTop(new JSONObject(getArguments().getString(HOME_TODAY_TOP, "{}"))) : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (todayTop == null)
            return;
        RenderHelper.RenderContentBuilder.Builder(GetActivity())
                .setTextAlignment(Enums.TEXT_ALIGNMENT.CENTER)
                .setParas(todayTop.getRenderText())
                .setLayParaContainer(laySher)
                .setLeftRightPadding((int) Utils.pxFromDp(28))
                .setOnWordLongClick(onWordLongClick)
                .setOnWordClick(onWordClickListener)
                .Build();
        sherPoetName.setText(todayTop.getPoetName());
        sherPoetName.setTag(R.id.tag_data, todayTop);

        for (HomeImageTag tag : todayTop.getHomeTags()) {
            View convertView = getInflatedView(GetActivity());
            TextView txtTagName = convertView.findViewById(R.id.txtTagName);
            txtTagName.setText(String.format("#%s", tag.getTagName()));
            convertView.setTag(tag);
            convertView.setOnClickListener(v -> {
                HomeImageTag tag1 = (HomeImageTag) v.getTag();
                GetActivity().startActivity(SherTagOccasionActivity.getInstance(GetActivity(), tag1));
               // GetActivity().startActivity(SherCollectionActivity.getInstance(GetActivity(), tag1));
            });
            flexTags.addView(convertView);
        }

        flexTags.removeAllViews();
        flexTags.setVisibility(CollectionUtils.isEmpty(todayTop.getHomeTags()) ? View.GONE : View.VISIBLE);
        if (!CollectionUtils.isEmpty(todayTop.getHomeTags()))
            switch (todayTop.getHomeTags().size()) {
                case 1:
                    flexTags.addView(getFlexTagView(todayTop.getHomeTags().get(0)));
                    break;
                case 2:
                    flexTags.addView(getFlexTagView(todayTop.getHomeTags().get(0)));
                    flexTags.addView(getFlexTagView(todayTop.getHomeTags().get(1)));
                    break;
                default:
                    flexTags.addView(getFlexTagView(todayTop.getHomeTags().get(0)));
                    flexTags.addView(getFlexTagMoreView(todayTop.getHomeTags().subList(1, todayTop.getHomeTags().size())));
                    break;
            }
    }

    private View getFlexTagView(HomeImageTag tag) {
        View convertView = getInflatedView(GetActivity());
        TextView txtTagName = convertView.findViewById(R.id.txtTagName);
        txtTagName.setText(String.format("#%s", tag.getTagName()));
        convertView.setTag(tag);
        convertView.setOnClickListener(v -> {
            HomeImageTag tag1 = (HomeImageTag) v.getTag();
            GetActivity().startActivity(SherTagOccasionActivity.getInstance(GetActivity(), tag1));
        });
        return convertView;
    }

    private View getFlexTagMoreView(final List<HomeImageTag> moreImageTags) {
        View convertView = getInflatedView(GetActivity());
        TextView txtTagName = convertView.findViewById(R.id.txtTagName);
        txtTagName.setText(String.format(Locale.getDefault(), "+%d", moreImageTags.size()));
        convertView.setOnClickListener(v -> {
            Context wrapper = new ContextThemeWrapper(GetActivity(), R.style.PopupMenu);
            PopupMenu popup = new PopupMenu(wrapper, convertView);
//            PopupMenu popup = new PopupMenu(GetActivity(), convertView);
            for (int i = 0; i < moreImageTags.size(); i++) {
                HomeImageTag sherTag = moreImageTags.get(i);
                popup.getMenu().add(R.id.menuGroup, R.id.group_detail, i, sherTag.getTagName());
            }
            popup.setOnMenuItemClickListener(item -> {
                GetActivity().startActivity(SherTagOccasionActivity.getInstance(GetActivity(), moreImageTags.get(item.getOrder())));
                return true;
            });
            popup.show();
        });
        return convertView;
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
    }

    private View getInflatedView(BaseActivity baseActivity) {
        View convertView = baseActivity.getLayoutInflater().inflate(R.layout.cell_home_tag, null);
        convertView.setTag(R.id.tag_language, MyService.getSelectedLanguage());
        return convertView;
    }
}
