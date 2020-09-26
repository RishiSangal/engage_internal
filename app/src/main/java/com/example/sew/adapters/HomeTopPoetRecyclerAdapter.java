package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.HomeTopPoet;
import com.example.sew.models.home_view_holders.SeeMoreViewHolder;
import com.example.sew.views.TitleTextViewType5;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeTopPoetRecyclerAdapter extends BaseRecyclerAdapter {
    private ArrayList<HomeTopPoet> topPoets;

    public HomeTopPoetRecyclerAdapter(BaseActivity activity, ArrayList<HomeTopPoet> topPoets) {
        super(activity);
        this.topPoets = topPoets;
    }


    public HomeTopPoet getItem(int position) {
        return topPoets.get(position);
    }

    @Override
    public int getItemViewType(int position) {
//        return topPoets.size() == position ? ITEM_VIEW_SEE_MORE : ITEM_VIEW_CONTENT;
        return ITEM_VIEW_CONTENT;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_SEE_MORE)
            return new SeeMoreViewHolder(getInflatedView(R.layout.cell_home_poets_see_more), SEE_MORE_POETS);
        else
            return new PoetViewHolder(getInflatedView(MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? R.layout.cell_home_poet_item_urdu : R.layout.cell_home_poet_item));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof PoetViewHolder))
            return;
        HomeTopPoet topPoet = getItem(position);
        PoetViewHolder poetViewHolder = (PoetViewHolder) holder;
        poetViewHolder.itemView.setTag(R.id.tag_data, topPoet);
        poetViewHolder.txtPoetName.setText(topPoet.getName());
        poetViewHolder.txtPoetTenure.setText(topPoet.getPoetTenure());
        ImageHelper.setImage(poetViewHolder.poetProfileImageTemplate, topPoet.getImageURL());
    }

    @Override
    public int getItemCount() {
        return topPoets.size();
    }

    class PoetViewHolder extends BaseViewHolder {
        @BindView(R.id.poetProfileImageTemplate)
        CircleImageView poetProfileImageTemplate;
        @BindView(R.id.txtPoetName)
        TextView txtPoetName;
        @BindView(R.id.txtPoetTenure)
        TextView txtPoetTenure;

        @OnClick()
        void onItemClick(View view) {
            HomeTopPoet topPoet = (HomeTopPoet) view.getTag(R.id.tag_data);
            getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), topPoet.getEntityId()));
        }

        PoetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
