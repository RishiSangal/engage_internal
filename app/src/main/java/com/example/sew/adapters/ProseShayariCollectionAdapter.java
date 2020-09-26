package com.example.sew.adapters;

import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.AppCollection;
import com.example.sew.models.ContentType;
import com.example.sew.views.IconTextView;
import com.example.sew.views.SquareImageView;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType5;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProseShayariCollectionAdapter extends BaseRecyclerAdapter {
    private ArrayList<AppCollection> collections;
    private ContentType contentType;
    private String description;
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    private Enums.COLLECTION_TYPE collectionType;

    public ProseShayariCollectionAdapter(BaseActivity activity, ArrayList<AppCollection> collections, ContentType contentType, String description) {
        super(activity);
        this.collections = collections;
        this.contentType = contentType;
        this.description = description;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_HEADER : ITEM_TYPE_CONTENT;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                return new CommonViewHolder(getInflatedView(R.layout.cell_common_header));
            case ITEM_TYPE_CONTENT:
                return new CollectionViewHolder(getInflatedView(R.layout.cell_item_sher_collection));
        }
        return new CollectionViewHolder(getInflatedView(R.layout.cell_item_sher_collection));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE_HEADER: {
                if (!(holder instanceof CommonViewHolder))
                    return;
                CommonViewHolder commonViewHolder = (CommonViewHolder) holder;
                if(TextUtils.isEmpty(description))
                    commonViewHolder.txtDescription.setVisibility(View.GONE);
                else {
                    commonViewHolder.txtDescription.setVisibility(View.VISIBLE);
                    commonViewHolder.txtDescription.setText(description);
                }
                commonViewHolder.txtTitle.setText(contentType.getName());
            }
            break;
            case ITEM_TYPE_CONTENT:
                if (!(holder instanceof CollectionViewHolder))
                    return;
                CollectionViewHolder collectionViewHolder = (CollectionViewHolder) holder;
                AppCollection appCollection = collections.get(position - 1);
                collectionViewHolder.itemView.setTag(R.id.tag_data, appCollection);
                ImageHelper.setImage(collectionViewHolder.imgCollectionImage, appCollection.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_COLLECTION);
                collectionViewHolder.txtTitle.setText(appCollection.getName());
                collectionViewHolder.txtMore.setOnClickListener(view -> {
                    PopupWindow popup = new PopupWindow(getActivity());
                    View layout = getActivity().getLayoutInflater().inflate(R.layout.menu_collections, null);
                    layout.findViewById(R.id.imgFavorite).setTag(R.id.tag_data, appCollection);
                    layout.findViewById(R.id.imgShare).setTag(R.id.tag_data, appCollection);
                    addFavoriteClick(layout.findViewById(R.id.imgFavorite), appCollection.getId(), collectionType == Enums.COLLECTION_TYPE.PROSE ? Enums.FAV_TYPES.PROSE_COLLECTION.getKey() : Enums.FAV_TYPES.SHAYARI_COLLECTION.getKey(), v -> popup.dismiss());
                    updateFavoriteIcon(layout.findViewById(R.id.imgFavorite), appCollection.getId());
                    layout.findViewById(R.id.imgShare).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppCollection appCollection = (AppCollection) v.getTag(R.id.tag_data);
                            MyHelper.shareContent(String.format("%s\n%s", appCollection.getName(), appCollection.getShareUrl()));
/*
Muntakhab Shakeel Badayuni https://www.rekhta.org/collections/shakeel-badayuni-shayari
 */
                        }
                    });
                    popup.setContentView(layout);
                    // Set content width and height
                    popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                    popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                    // Closes the popup window when touch outside of it - when looses focus
                    popup.setOutsideTouchable(true);
                    popup.setFocusable(true);
                    popup.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.popup_background));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        popup.setElevation(40);
                    }
                    int[] values = new int[2];
                    view.getLocationInWindow(values);
                    int positionOfIcon = values[1];
                    System.out.println("Position Y:" + positionOfIcon);
                    DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                    int height = (displayMetrics.heightPixels * 2) / 3;
                    System.out.println("Height:" + height);
                    if (positionOfIcon > height) {
                        popup.showAsDropDown(view, 0, -view.getHeight() * 3);
                    } else {
                        popup.showAsDropDown(view);
                    }
                });
                break;
        }


    }

    private View.OnClickListener onItemClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return collections.size() + 1;
    }

    public void setCollectionType(Enums.COLLECTION_TYPE collectionType) {
        this.collectionType = collectionType;
    }

    class CollectionViewHolder extends BaseViewHolder {
        @BindView(R.id.imgCollectionImage)
        SquareImageView imgCollectionImage;
        @BindView(R.id.txtTitle)
        TitleTextViewType6 txtTitle;
        @BindView(R.id.txtMore)
        IconTextView txtMore;

        @OnClick()
        void onItemClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onClick(view);
        }

        CollectionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class CommonViewHolder extends BaseViewHolder {
        @BindView(R.id.txtTitle)
        TitleTextViewType5 txtTitle;
        @BindView(R.id.txtDescription)
        TextView txtDescription;

        CommonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
