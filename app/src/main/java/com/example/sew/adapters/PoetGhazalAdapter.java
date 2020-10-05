package com.example.sew.adapters;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.ContentSortPopupWindow;
import com.example.sew.common.Enums;
import com.example.sew.common.RelativePopupWindow;
import com.example.sew.fragments.BasePoetGhazalFragment;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.ShayariContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PoetGhazalAdapter extends BasePoetContentAdapter {
    private ArrayList<ShayariContent> poetGhazals;
    private PoetDetail poetDetail;
    private int totalContentCount;
    private ContentType contentType;
    private BasePoetGhazalFragment fragment;
    private String sortedBy;
    public PoetGhazalAdapter(BaseActivity activity, ArrayList<ShayariContent> poetGhazals, PoetDetail poetDetail, ContentType contentType,
                             BasePoetGhazalFragment fragment, String sortedBy) {
        super(activity, poetDetail);
        this.poetGhazals = poetGhazals;
        this.poetDetail = poetDetail;
        this.contentType = contentType;
        this.fragment = fragment;
        this.sortedBy= sortedBy;
    }

    public void setTotalContentCount(int totalContentCount) {
        this.totalContentCount = totalContentCount;
    }

    @Override
    public int getCount() {
        return poetGhazals.size() + 1;
    }

    @Override
    public ShayariContent getItem(int position) {
        return poetGhazals.get(position - 1);
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
                if(poetGhazals.size()>1)
                    poetsProfileViewHolder.txtFilter.setVisibility(View.VISIBLE);
                else
                    poetsProfileViewHolder.txtFilter.setVisibility(View.GONE);
                loadDataForPoetHeader(poetsProfileViewHolder);
                break;
            case VIEW_TYPE_CONTENT:
                GhazalViewHolder ghazalViewHolder;
                ShayariContent shayariContent = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_poet_ghazals);
                    ghazalViewHolder = new GhazalViewHolder(convertView);
                } else
                    ghazalViewHolder = (GhazalViewHolder) convertView.getTag();
                convertView.setTag(ghazalViewHolder);
                convertView.setTag(R.id.tag_data, shayariContent);
                ghazalViewHolder.offlineFavIcon.setTag(R.id.tag_data, shayariContent);
                ghazalViewHolder.poetaudioLink.setVisibility(shayariContent.isAudioAvailable() ? View.VISIBLE : View.GONE);
                ghazalViewHolder.poetyoutubeLink.setVisibility(shayariContent.isVideoAvailable() ? View.VISIBLE : View.GONE);
                ghazalViewHolder.poetPopularChoiceIcon.setVisibility(shayariContent.isPopularChoice() ? View.VISIBLE : View.GONE);
                ghazalViewHolder.poetEditorChoiceIcon.setVisibility(shayariContent.isEditorChoice() ? View.VISIBLE : View.GONE);
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        if (TextUtils.isEmpty(shayariContent.getTitleEng().trim()))
                            ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleUr());
                        else
                            ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleEng());

                        break;
                    case HINDI:
                        if (TextUtils.isEmpty(shayariContent.getTitleHin().trim()))
                            ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleUr());
                        else
                            ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleHin());
                        break;
                    case URDU:
                        ghazalViewHolder.ghazalTitle.setText(shayariContent.getTitleUr());
                        break;
                }
                addFavoriteClick(ghazalViewHolder.offlineFavIcon, shayariContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(ghazalViewHolder.offlineFavIcon, shayariContent.getId());
                break;
        }
//listingTypek base pr
        return convertView;
    }

    @Override
    String getContentTitle() {
        return contentType.getName().toUpperCase();
    }

    @Override
    String getContentCount() {
        return String.valueOf(totalContentCount);
    }

    ArrayList<String> sortContent;
    @Override
    void contentFilter(View view) {
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
            new ContentSortPopupWindow(getActivity(),getContentTitle(),fragment,sortedBy).showOnAnchor(view, RelativePopupWindow.VerticalPosition.ALIGN_BOTTOM, RelativePopupWindow.HorizontalPosition.ALIGN_LEFT, false); // Creation of popup
        else
            new ContentSortPopupWindow(getActivity(),getContentTitle(),fragment,sortedBy).showOnAnchor(view, RelativePopupWindow.VerticalPosition.ALIGN_BOTTOM, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, false); // Creation of popup

//form_popup is the template to the popup

////...
//        view.findViewById(R.id.txtPopularity);
//        view.findViewById(R.id.txtAlphabetic);
//        view.findViewById(R.id.txtRadeef);


//        sortContent = new ArrayList<>();
//        sortContent.add(MyHelper.getString(R.string.popularity));
//        if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH||MyService.getSelectedLanguage()== Enums.LANGUAGE.HINDI)
//            sortContent.add(MyHelper.getString(R.string.alphabetic));
//        if (getContentTitle().equalsIgnoreCase(MyHelper.getString(R.string.ghazal)) | getContentTitle().equalsIgnoreCase(MyHelper.getString(R.string.unpublished_ghazal)))
//            sortContent.add(MyHelper.getString(R.string.radeef));
//        PopupMenu popup = new PopupMenu(getActivity(), view);
//        for (int i = 0; i < sortContent.size(); i++) {
//            popup.getMenu().add(R.id.menuGroup, R.id.group_detail, i, sortContent.get(i));
//        }
//        popup.setOnMenuItemClickListener(item -> {
//            if (item.toString().equalsIgnoreCase(MyHelper.getString(R.string.popularity))) {
//                fragment.sortContent(Enums.SORT_CONTENT.POPULARITY);
//            } else if ((item.toString().equalsIgnoreCase(MyHelper.getString(R.string.alphabetic)))) {
//                fragment.sortContent(Enums.SORT_CONTENT.ALPHABETIC);
//            } else {
//                fragment.sortContent(Enums.SORT_CONTENT.RADEEF);
//            }
//            return true;
//        });
//        popup.show();
    }

    class GhazalViewHolder {
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        @BindView(R.id.ghazalTitle)
        TextView ghazalTitle;
        @BindView(R.id.poetEditorChoiceIcon)
        ImageView poetEditorChoiceIcon;
        @BindView(R.id.poetPopularChoiceIcon)
        ImageView poetPopularChoiceIcon;
        @BindView(R.id.poetaudioLink)
        ImageView poetaudioLink;
        @BindView(R.id.poetyoutubeLink)
        ImageView poetyoutubeLink;

        GhazalViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
