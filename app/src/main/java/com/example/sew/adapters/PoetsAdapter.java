package com.example.sew.adapters;

import android.text.TextUtils;
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
import com.example.sew.helpers.MyService;
import com.example.sew.models.Poet;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PoetsAdapter extends BaseMyAdapter {
    private ArrayList<Poet> poets;

    public PoetsAdapter(BaseActivity activity, ArrayList<Poet> poets) {
        super(activity);
        this.poets = poets;
    }

    @Override
    public int getCount() {
        return poets.size();
    }

    @Override
    public Poet getItem(int position) {
        return poets.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        PoetsViewHolder poetsViewHolder;
        Poet poet = getItem(position);
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_poets);
            poetsViewHolder = new PoetsViewHolder(convertView);
        } else
            poetsViewHolder = (PoetsViewHolder) convertView.getTag();
        convertView.setTag(poetsViewHolder);
        convertView.setTag(R.id.tag_data, poet);
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                poetsViewHolder.txtPoetName.setText(poet.getNameEng());
                if (TextUtils.isEmpty(poet.getSPE()))
                    poetsViewHolder.txtPoetDescription.setVisibility(View.GONE);
                else {
                    poetsViewHolder.txtPoetDescription.setVisibility(View.VISIBLE);
                    poetsViewHolder.txtPoetDescription.setText(poet.getSPE());
                }
                break;
            case HINDI:
                poetsViewHolder.txtPoetName.setText(poet.getNameHin());
                if (TextUtils.isEmpty(poet.getSPH()))
                    poetsViewHolder.txtPoetDescription.setVisibility(View.GONE);
                else {
                    poetsViewHolder.txtPoetDescription.setVisibility(View.VISIBLE);
                    poetsViewHolder.txtPoetDescription.setText(poet.getSPH());
                }
                break;
            case URDU:
                poetsViewHolder.txtPoetName.setText(poet.getNameUr());
                if (TextUtils.isEmpty(poet.getSPU()))
                    poetsViewHolder.txtPoetDescription.setVisibility(View.GONE);
                else {
                    poetsViewHolder.txtPoetDescription.setVisibility(View.VISIBLE);
                    poetsViewHolder.txtPoetDescription.setText(poet.getSPU());
                }
                break;
        }
        int ghazalCount = MyHelper.convertToInt(poet.getGhazalCount());
        int nazmCount = MyHelper.convertToInt(poet.getNazmCount());
        int sherCount = MyHelper.convertToInt(poet.getSherCount());
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
        if (sherCount == 0)
            poetsViewHolder.txtSherCount.setVisibility(View.GONE);
        else {
            poetsViewHolder.txtSherCount.setVisibility(View.VISIBLE);
            poetsViewHolder.txtSherCount.setText(String.format(Locale.getDefault(), content, getString(R.string.dot), sherCount, sher));
        }
        poetsViewHolder.txtpoetIsNew.setVisibility(poet.isNewPoet() ? View.VISIBLE : View.GONE);
        ImageHelper.setImage(poetsViewHolder.imgPoetImage, poet.getPoetImage());
        addFavoriteClick(poetsViewHolder.offlineFavIcon, poet.getPoetId(), Enums.FAV_TYPES.ENTITY.getKey());
        updateFavoriteIcon(poetsViewHolder.offlineFavIcon, poet.getPoetId());
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
        }
    }
}
