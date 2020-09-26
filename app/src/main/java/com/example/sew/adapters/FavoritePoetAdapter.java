package com.example.sew.adapters;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.FavContentPageModel;
import com.example.sew.models.FavoritePoet;
import com.example.sew.models.PoetDetail;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritePoetAdapter extends BaseRecyclerAdapter {
    private ArrayList<FavoritePoet> poetDetails;
    private View.OnClickListener onItemClickListener;

    public FavoritePoetAdapter(BaseActivity activity, ArrayList<FavoritePoet> poetDetails) {
        super(activity);
        this.poetDetails = poetDetails;
    }


    public FavoritePoet getItem(int position) {
        return poetDetails.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoetViewHolder(getInflatedView(R.layout.cell_fav_poet));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof PoetViewHolder))
            return;
        FavoritePoet favoritePoet = getItem(position);
        PoetViewHolder poetViewHolder = (PoetViewHolder) holder;
        poetViewHolder.itemView.setTag(R.id.tag_data, favoritePoet);
        poetViewHolder.offlineFavIcon.setTag(R.id.tag_data, favoritePoet);
        if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH||MyService.getSelectedLanguage()== Enums.LANGUAGE.HINDI){
            poetViewHolder.txtPoetName.setTextDirection(View.TEXT_DIRECTION_LTR);
        }
        poetViewHolder.txtPoetName.setText(favoritePoet.getName());
        poetViewHolder.txtDob.setText(favoritePoet.getEntityYearRange());
        addFavoriteClick(poetViewHolder.offlineFavIcon, favoritePoet.getId(),Enums.FAV_TYPES.ENTITY.getKey());
        updateFavoriteIcon(poetViewHolder.offlineFavIcon, favoritePoet.getId());
        poetViewHolder.offlineFavIcon.setVisibility(MyApplication.getInstance().isBrowsingOffline() ? View.GONE : View.VISIBLE);
        ImageHelper.setImage(poetViewHolder.imgPoetImage, favoritePoet.getImageUrl());
        if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH||MyService.getSelectedLanguage()== Enums.LANGUAGE.HINDI){
            poetViewHolder.txtPoetName.setTextSize(14);
        }else
            poetViewHolder.txtPoetName.setTextSize(16);

    }
    View.OnClickListener onWordClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return poetDetails.size();
    }

    class PoetViewHolder extends BaseViewHolder {
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        @BindView(R.id.txtPoetName)
        TextView txtPoetName;
        @BindView(R.id.txtDob)
        TextView txtDob;
        @BindView(R.id.imgPoetImage)
        CircleImageView imgPoetImage;


        @OnClick()
        void onItemClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onClick(view);
        }

        PoetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public static String reverseIt(String source) {
        int i, len = source.length();
        StringBuilder dest = new StringBuilder(len);
        for (i = (len - 1); i >= 0; i--){
            dest.append(source.charAt(i));
        }
        return dest.toString();
    }
}
