package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.SearchContentAll;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchPoetsAdapter extends BaseMyAdapter {
    private ArrayList<SearchContentAll.SearchPoet> poets;

    public SearchPoetsAdapter(BaseActivity activity, ArrayList<SearchContentAll.SearchPoet> poets) {
        super(activity);
        this.poets = poets;
    }

    @Override
    public int getCount() {
        return poets.size();
    }

    @Override
    public SearchContentAll.SearchPoet getItem(int position) {
        return poets.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        PoetsViewHolder poetsViewHolder;
        SearchContentAll.SearchPoet poet = getItem(position);
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_poets);
            poetsViewHolder = new PoetsViewHolder(convertView);
        } else
            poetsViewHolder = (PoetsViewHolder) convertView.getTag();
        convertView.setTag(poetsViewHolder);
        convertView.setTag(R.id.tag_data, poet);
        poetsViewHolder.txtPoetName.setText(poet.getName());
        int ghazalCount = MyHelper.convertToInt(poet.getGazalCount());
        int nazmCount = MyHelper.convertToInt(poet.getNazmCount());
        String ghazal = MyHelper.getString(R.string.ghazal);
        String nazm = MyHelper.getString(R.string.nazm);
        String sher = MyHelper.getString(R.string.sher);
        String content = "%1$s  %2$d %3$s";
        if (ghazalCount == 0)
            poetsViewHolder.txtGhazalCount.setVisibility(View.GONE);
        else {
            poetsViewHolder.txtGhazalCount.setVisibility(View.VISIBLE);
            poetsViewHolder.txtGhazalCount.setText(String.format(Locale.getDefault(), content, getString(R.string.dot), ghazalCount, ghazal));
        }
        if (nazmCount == 0)
            poetsViewHolder.txtNazmCount.setVisibility(View.GONE);
        else {
            poetsViewHolder.txtNazmCount.setVisibility(View.VISIBLE);
            poetsViewHolder.txtNazmCount.setText(String.format(Locale.getDefault(), content, getString(R.string.dot), nazmCount, nazm));
        }
        ImageHelper.setImage(poetsViewHolder.imgPoetImage, poet.getImageUrl());
        addFavoriteClick(poetsViewHolder.offlineFavIcon, poet.getEntityId(), Enums.FAV_TYPES.ENTITY.getKey());
        updateFavoriteIcon(poetsViewHolder.offlineFavIcon, poet.getEntityId());
        return convertView;
    }

    static class PoetsViewHolder {
        @BindView(R.id.imgPoetImage)
        CircleImageView imgPoetImage;
        @BindView(R.id.txtPoetName)
        TextView txtPoetName;
        @BindView(R.id.txtpoetIsNew)
        TextView txtpoetIsNew;
        @BindView(R.id.txtPoetDescription)
        TextView txtPoetDescription;
        @BindView(R.id.txtGhazalCount)
        TextView txtGhazalCount;
        @BindView(R.id.txtNazmCount)
        TextView txtNazmCount;
        @BindView(R.id.txtSherCount)
        TextView txtSherCount;
        @BindView(R.id.viewHo)
        LinearLayout viewHo;
        @BindView(R.id.poetTemplate)
        RelativeLayout poetTemplate;
        @BindView(R.id.offlineFavIcon)
        ImageView offlineFavIcon;
        PoetsViewHolder(View view) {
            ButterKnife.bind(this, view);
            txtpoetIsNew.setVisibility(View.GONE);
            txtSherCount.setVisibility(View.GONE);
            txtPoetDescription.setVisibility(View.GONE);
        }
    }
}
