package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.Utils;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.FavContentPageModel;
import com.example.sew.models.Para;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteContentRecyclerAdapter extends BaseRecyclerAdapter {
    private ArrayList<FavContentPageModel> favContentPageModels;
    private View.OnClickListener onItemClickListener;

    public FavoriteContentRecyclerAdapter(BaseActivity activity, ArrayList<FavContentPageModel> favContentPageModels) {
        super(activity);
        this.favContentPageModels = favContentPageModels;
    }


    public FavContentPageModel getItem(int position) {
        return favContentPageModels.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GhazalViewHolder(getInflatedView(R.layout.cell_ghazals));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof GhazalViewHolder))
            return;
        FavContentPageModel favContentPageModel = getItem(position);
        GhazalViewHolder ghazalViewHolder = (GhazalViewHolder) holder;
        ghazalViewHolder.itemView.setTag(R.id.tag_data, favContentPageModel);
        ghazalViewHolder.offlineFavIcon.setTag(R.id.tag_data, favContentPageModel);
        ghazalViewHolder.poetaudioLink.setVisibility(View.GONE);
        ghazalViewHolder.poetyoutubeLink.setVisibility(View.GONE);
      //  ghazalViewHolder.ghazalTitle.setText(favContentPageModel.getTitle());
        if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH||MyService.getSelectedLanguage()== Enums.LANGUAGE.HINDI){
            ghazalViewHolder.ghazalTitle.setTextDirection(View.TEXT_DIRECTION_LTR);
            ghazalViewHolder.ghazalTitleSecond.setTextDirection(View.TEXT_DIRECTION_LTR);
        }
        ghazalViewHolder.ghazalPoet.setText(favContentPageModel.getPoetName().toUpperCase());
        addFavoriteClick(ghazalViewHolder.offlineFavIcon, favContentPageModel.getId(),Enums.FAV_TYPES.CONTENT.getKey());
        updateFavoriteIcon(ghazalViewHolder.offlineFavIcon, favContentPageModel.getId());
        ghazalViewHolder.offlineFavIcon.setVisibility(MyApplication.getInstance().isBrowsingOffline() ? View.GONE : View.VISIBLE);
        //ghazalViewHolder.offlineFavIcon.setVisibility(MyApplication.getInstance().isBrowsingOffline() ? View.GONE : View.VISIBLE);
        ghazalViewHolder.ghazalTitleSecond.setVisibility(View.VISIBLE);
        String bodyData = favContentPageModel.getTitle().replace("<br/>", "\n");
        //ghazalViewHolder.ghazalTitle.setText(bodyData);
        if(favContentPageModel.getBody().contains("\r\n")){
            String[] separated = favContentPageModel.getBody().split("\r\n");
            String firstLine = separated[0];
            String lastLine = separated[1];
            //  ghazalViewHolder.ghazalTitle.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
            //  ghazalViewHolder.ghazalTitleSecond.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
            ghazalViewHolder.ghazalTitle.setText(firstLine);
            ghazalViewHolder.ghazalTitleSecond.setText(lastLine);
            ghazalViewHolder.ghazalTitle.setMaxLines(1);
            ghazalViewHolder.ghazalTitleSecond.setMaxLines(1);
            ghazalViewHolder.ghazalTitleSecond.setVisibility(View.VISIBLE);
        }else{
            //  ghazalViewHolder.ghazalTitle.setAutoSizeTextTypeUniformWithConfiguration(1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
            ghazalViewHolder.ghazalTitle.setText(favContentPageModel.getBody());
            ghazalViewHolder.ghazalTitle.setMaxLines(1);
            ghazalViewHolder.ghazalTitleSecond.setVisibility(View.GONE);
        }
    }
    View.OnClickListener onWordClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return favContentPageModels.size();
    }

    class GhazalViewHolder extends BaseViewHolder {
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        @BindView(R.id.ghazalTitle)
        TextView ghazalTitle;
        @BindView(R.id.ghazalPoet)
        TextView ghazalPoet;
        @BindView(R.id.poetEditorChoiceIcon)
        ImageView poetEditorChoiceIcon;
        @BindView(R.id.poetPopularChoiceIcon)
        ImageView poetPopularChoiceIcon;
        @BindView(R.id.poetaudioLink)
        ImageView poetaudioLink;
        @BindView(R.id.poetyoutubeLink)
        ImageView poetyoutubeLink;
        @BindView(R.id.ghazalTitleSecond)
        TextView ghazalTitleSecond;

        @OnClick()
        void onItemClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onClick(view);
        }

        GhazalViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
