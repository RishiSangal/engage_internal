package com.example.sew.adapters;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.PoetDetail;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


abstract class BasePoetContentAdapter extends BaseMyAdapter {
    abstract String getContentTitle();

    abstract String getContentCount();

    abstract void contentFilter(View view);

    final int VIEW_TYPE_HEADER = 0;
    final int VIEW_TYPE_CONTENT = 1;
    private PoetDetail poetDetail;

    BasePoetContentAdapter(BaseActivity activity, PoetDetail poetDetail) {
        super(activity);
        this.poetDetail = poetDetail;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    void loadDataForPoetHeader(PoetsProfileViewHolder viewHolder) {
        if (viewHolder == null)
            return;
        viewHolder.poetTypeOfScreenTemplate.setText(getContentTitle());
        viewHolder.poetCountTemplate.setText(getContentCount());
        viewHolder.poetNameTemplateTV.setText(poetDetail.getPoetName());
        viewHolder.poetBirthPlaceTemplate.setText(poetDetail.getDomicile());
        if (TextUtils.isEmpty(poetDetail.getShortDescription()))
            setMargins(viewHolder.view, 16, 0, 0, 32);
        //viewHolder.view.
        viewHolder.poetDescriptionTemplate.setText(poetDetail.getShortDescription());
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
            viewHolder.poetDescriptionTemplate.setTextSize(16);
            viewHolder.poetTypeOfScreenTemplate.setTextSize(18);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.poetTypeOfScreenTemplate.setLetterSpacing(MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? 0 : 0.2f);
        }
        ImageHelper.setImage(viewHolder.poetProfileImageTemplate, poetDetail.getPoetImage());
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
            String[] split = poetDetail.getPoetTenure().split(" ");
            String result = "";
            for (int i = split.length - 1; i >= 0; i--) {
                result += (split[i] + " ");
            }
            viewHolder.poetTenureTemplateTV.setText(result.trim());
        } else
            viewHolder.poetTenureTemplateTV.setText(poetDetail.getPoetTenure());
        if (TextUtils.isEmpty(poetDetail.getDomicile())) {
            viewHolder.poetTenureTemplateDivider.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(poetDetail.getDomicile()) && TextUtils.isEmpty(poetDetail.getPoetName())) {
            viewHolder.lin.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(poetDetail.getPoetTenure())) {
            viewHolder.poetTenureTemplateDivider.setVisibility(View.GONE);
        }
        addFavoriteClick(viewHolder.imgFavorite, poetDetail.getPoetId(), Enums.FAV_TYPES.ENTITY.getKey());
        updateFavoriteIcon(viewHolder.imgFavorite, poetDetail.getPoetId());
        viewHolder.imgShareUrl.setTag(poetDetail);
        if(getContentTitle().equalsIgnoreCase(MyHelper.getString(R.string.audio)))
            viewHolder.txtFilter.setVisibility(View.INVISIBLE);
        if(getContentTitle().equalsIgnoreCase(MyHelper.getString(R.string.video)))
            viewHolder.txtFilter.setVisibility(View.INVISIBLE);
        if(getContentTitle().equalsIgnoreCase(MyHelper.getString(R.string.image_shayari)))
            viewHolder.txtFilter.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

     class PoetsProfileViewHolder {
        @BindView(R.id.poetProfileImageTemplate)
        CircleImageView poetProfileImageTemplate;
        @BindView(R.id.poetNameTemplateTV)
        TextView poetNameTemplateTV;
        @BindView(R.id.poetTenureTemplateTV)
        TextView poetTenureTemplateTV;
        @BindView(R.id.poetTenureTemplateDivider)
        TextView poetTenureTemplateDivider;
        @BindView(R.id.poetBirthPlaceTemplate)
        TextView poetBirthPlaceTemplate;
        @BindView(R.id.poetDescriptionTemplate)
        TextView poetDescriptionTemplate;
        @BindView(R.id.poetTypeOfScreenTemplate)
        TextView poetTypeOfScreenTemplate;
        @BindView(R.id.poetCountTemplate)
        TextView poetCountTemplate;
        @BindView(R.id.lin)
        LinearLayout lin;
        @BindView(R.id.view)
        View view;
        @BindView(R.id.imgFavorite)
        ImageView imgFavorite;
        @BindView(R.id.imgShareUrl)
        ImageView imgShareUrl;

        @OnClick(R.id.imgShareUrl)
        void onShareUrlClick(View view) {
            PoetDetail poetDetail = (PoetDetail) view.getTag();
            MyHelper.shareContent(poetDetail.getPoetName() + "\n" + poetDetail.getShortUrl());
        }

        @BindView(R.id.txtFilter)
        TextView txtFilter;
        @OnClick(R.id.txtFilter)
        void onFilterClick() {
            contentFilter(txtFilter);
        }

        PoetsProfileViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}

