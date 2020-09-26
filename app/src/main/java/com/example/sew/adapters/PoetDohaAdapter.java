package com.example.sew.adapters;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.PopupMenu;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.Utils;
import com.example.sew.fragments.PoetDohaFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.Para;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.SherContent;
import com.example.sew.views.TitleTextViewType2;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoetDohaAdapter extends BasePoetContentAdapter {
    private ArrayList<SherContent> sherContents;
    private PoetDetail poetDetail;
    private int totalContentCount;
    private PoetDohaFragment fragment;
    public PoetDohaAdapter(BaseActivity activity, ArrayList<SherContent> sherContents, PoetDetail poetDetail, PoetDohaFragment fragment) {
        super(activity, poetDetail);
        this.sherContents = sherContents;
        this.poetDetail = poetDetail;
        this.fragment= fragment;
    }

    public void setTotalContentCount(int totalContentCount) {
        this.totalContentCount = totalContentCount;
    }

    @Override
    public int getCount() {
        return sherContents.size() + 1;
    }

    @Override
    public SherContent getItem(int position) {
        return sherContents.get(position - 1);
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
            case VIEW_TYPE_HEADER:
                PoetsProfileViewHolder poetsProfileViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.poet_detailed_header);
                    poetsProfileViewHolder = new PoetsProfileViewHolder(convertView);
                } else
                    poetsProfileViewHolder = (PoetsProfileViewHolder) convertView.getTag();
                convertView.setTag(poetsProfileViewHolder);
                loadDataForPoetHeader(poetsProfileViewHolder);
                break;
            case VIEW_TYPE_CONTENT:
                SherViewHolder sherViewHolder;
                SherContent sherContent = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_poet_doha);
                    sherViewHolder = new SherViewHolder(convertView);
                } else
                    sherViewHolder = (SherViewHolder) convertView.getTag();
                convertView.setTag(sherViewHolder);
                convertView.setTag(R.id.tag_data, sherContent);
                sherViewHolder.sherHeartIcon.setTag(R.id.tag_data, sherContent);

                if (CollectionUtils.isEmpty(sherContent.getRenderText())) {
                    sherViewHolder.laySher.removeAllViews();
                } else {
                    RenderHelper.RenderContentBuilder.Builder(getActivity())
                            .setLayParaContainer(sherViewHolder.laySher)
                            .setParas(sherContent.getRenderText().get(0))
                            .setLeftRightPadding((int) Utils.pxFromDp(32))
                            .setOnWordLongClick(onWordLongClick)
                            .setOnWordClick(onWordClickListener)
                            .Build();
                }
                break;
        }

        return convertView;
    }


    private View.OnLongClickListener onWordLongClick = v -> {
        Para para = (Para) v.getTag(R.id.tag_para);
        if (para == null)
            return false;
        String shareContentText=MyHelper.getSherContentText(para);
        MyHelper.shareTheText(shareContentText, getActivity());
        MyHelper.copyToClipBoard(shareContentText,getActivity());
        return false;
    };
    private View.OnClickListener onWordClickListener;

    public void setOnWordClickListener(View.OnClickListener onWordClickListener) {
        this.onWordClickListener = onWordClickListener;
    }


    @Override
    String getContentTitle() {
        return MyHelper.getString(R.string.dohe).toUpperCase();
    }

    @Override
    String getContentCount() {
        return String.valueOf(totalContentCount);
    }
    private ArrayList<String> sortContent;
    @Override
    void contentFilter(View view) {
        sortContent = new ArrayList<>();
        sortContent.add(MyHelper.getString(R.string.popularity));
        if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH||MyService.getSelectedLanguage()== Enums.LANGUAGE.HINDI)
            sortContent.add(MyHelper.getString(R.string.alphabetic));
        //  sortContent.add(MyHelper.getString(R.string.radeef));
        PopupMenu popup = new PopupMenu(getActivity(), view);
        for (int i = 0; i < sortContent.size(); i++) {
            popup.getMenu().add(R.id.menuGroup, R.id.group_detail, i, sortContent.get(i));
        }
        popup.setOnMenuItemClickListener(item -> {
            if (item.toString().equalsIgnoreCase(MyHelper.getString(R.string.popularity))) {
                fragment.sortContent(Enums.SORT_CONTENT.POPULARITY);
            } else if ((item.toString().equalsIgnoreCase(MyHelper.getString(R.string.alphabetic)))) {
                fragment.sortContent(Enums.SORT_CONTENT.ALPHABETIC);
            } else {
                fragment.sortContent(Enums.SORT_CONTENT.RADEEF);
            }
            return true;
        });
        popup.show();
    }


    class SherViewHolder {
        @BindView(R.id.laySher)
        LinearLayout laySher;
        @BindView(R.id.sher_poet_name)
        TitleTextViewType2 sherPoetName;
        @BindView(R.id.sher_heartIcon)
        ImageView sherHeartIcon;
        @BindView(R.id.sher_shareIcon)
        ImageView sherShareIcon;
        @BindView(R.id.poetList_layout)
        LinearLayout poetListLayout;

        @OnClick({R.id.sher_shareIcon})
        public void onViewClicked(View view) {
            SherContent sherContent = null;
            if (view.getTag(R.id.tag_data) != null && view.getTag(R.id.tag_data) instanceof SherContent)
                sherContent = (SherContent) view.getTag(R.id.tag_data);
            if (sherContent == null)
                return;
            if (view.getId() == R.id.sher_shareIcon) {
                StringBuilder stringBuilder = new StringBuilder();
                String sherContentText = MyHelper.getSherContentText(sherContent.getRenderText());
                if (TextUtils.isEmpty(sherContentText))
                    getActivity().showToast("Sher not found");
                else
                    stringBuilder.append(sherContentText);
                stringBuilder.append("\n\n");
                stringBuilder.append(sherContent.getPoetName());
                stringBuilder.append("\n");
                stringBuilder.append(sherContent.getLink());
                BaseActivity.shareTheUrl(stringBuilder.toString(), getActivity());
            }
        }

        SherViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static
    class ViewHolder {
        @BindView(R.id.laySher)
        LinearLayout laySher;
        @BindView(R.id.sher_poet_name)
        TitleTextViewType2 sherPoetName;
        @BindView(R.id.sher_heartIcon)
        ImageView sherHeartIcon;
        @BindView(R.id.sher_shareIcon)
        ImageView sherShareIcon;
        @BindView(R.id.poetList_layout)
        LinearLayout poetListLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
