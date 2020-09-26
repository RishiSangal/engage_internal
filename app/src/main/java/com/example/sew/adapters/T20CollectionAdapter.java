package com.example.sew.adapters;

import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.SherCollection;
import com.example.sew.views.IconTextView;
import com.example.sew.views.SquareImageView;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sew.common.Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE;

public class T20CollectionAdapter extends BaseRecyclerAdapter {
    private ArrayList<SherCollection> collections;
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;

    public T20CollectionAdapter(BaseActivity activity, ArrayList<SherCollection> collections) {
        super(activity);
        this.collections = collections;
    }

    @Override
    public int getItemCount() {
        return collections.size() + 1;
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
                return new ProseShayariCollectionAdapter.CommonViewHolder(getInflatedView(R.layout.cell_common_header));
            case ITEM_TYPE_CONTENT:
                return new CollectionViewHolder(getInflatedView(R.layout.cell_item_sher_collection));
        }
        return new CollectionViewHolder(getInflatedView(R.layout.cell_item_sher_collection));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_TYPE_HEADER: {
                if (!(holder instanceof ProseShayariCollectionAdapter.CommonViewHolder))
                    return;
                ProseShayariCollectionAdapter.CommonViewHolder commonViewHolder = (ProseShayariCollectionAdapter.CommonViewHolder) holder;
                commonViewHolder.txtTitle.setText(MyHelper.getString(R.string.t20_series_header_title));
                commonViewHolder.txtDescription.setText(MyHelper.getString(R.string.t20_series_header_sub_title));
            }
            break;
            case ITEM_TYPE_CONTENT:
                if (!(holder instanceof CollectionViewHolder))
                    return;
                CollectionViewHolder collectionViewHolder = (CollectionViewHolder) holder;
                SherCollection sherCollection = collections.get(position - 1);
                collectionViewHolder.itemView.setTag(R.id.tag_data, sherCollection);
                ImageHelper.setImage(collectionViewHolder.imgCollectionImage, sherCollection.getImageUrl(), SHAYARI_IMAGE);
                switch (MyService.getSelectedLanguage()) {
                    case ENGLISH:
                        collectionViewHolder.txtTitle.setText(sherCollection.getTitleEng());
                        break;
                    case HINDI:
                        collectionViewHolder.txtTitle.setText(sherCollection.getTitleHin());
                        break;
                    case URDU:
                        collectionViewHolder.txtTitle.setText(sherCollection.getTitleUrdu());
                        break;
                }
                collectionViewHolder.txtMore.setOnClickListener(view -> {
                    PopupWindow popup = new PopupWindow(getActivity());
                    View layout = getActivity().getLayoutInflater().inflate(R.layout.menu_collections, null);
                    layout.findViewById(R.id.imgFavorite).setTag(R.id.tag_data, sherCollection);
                    layout.findViewById(R.id.imgShare).setTag(R.id.tag_data, sherCollection);
                    addFavoriteClick(layout.findViewById(R.id.imgFavorite), sherCollection.getSherCollectionId(), Enums.FAV_TYPES.T20.getKey(), v -> popup.dismiss());
                    updateFavoriteIcon(layout.findViewById(R.id.imgFavorite), sherCollection.getSherCollectionId());
//            layout.findViewById(R.id.imgFavorite).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SherCollection sherCollection = (SherCollection) v.getTag(R.id.tag_data);
//                }
//            });
                    layout.findViewById(R.id.imgShare).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SherCollection sherCollection = (SherCollection) v.getTag(R.id.tag_data);
                            MyHelper.shareContent(String.format("%s\n%s", sherCollection.getTitle(), sherCollection.getShareUrl()));
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
}
