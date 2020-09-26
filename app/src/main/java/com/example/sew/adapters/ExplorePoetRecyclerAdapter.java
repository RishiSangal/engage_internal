package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.models.HomeTopPoet;
import com.example.sew.views.SquareImageView;
import com.example.sew.views.TitleTextView;
import com.example.sew.views.TitleTextViewType6;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExplorePoetRecyclerAdapter extends BaseRecyclerAdapter {
    private ArrayList<HomeTopPoet> topPoets;

    public ExplorePoetRecyclerAdapter(BaseActivity activity, ArrayList<HomeTopPoet> topPoets) {
        super(activity);
        this.topPoets = topPoets;
    }


    public HomeTopPoet getItem(int position) {
        return topPoets.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoetViewHolder(getInflatedView(R.layout.cell_explore_top_poet));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!(holder instanceof PoetViewHolder))
            return;
        HomeTopPoet topPoet = getItem(position);
        PoetViewHolder poetViewHolder = (PoetViewHolder) holder;
        poetViewHolder.itemView.setTag(R.id.tag_data, topPoet);
        poetViewHolder.txtCollectionTitle.setText(topPoet.getName());
        poetViewHolder.txtPoetTenure.setText(topPoet.getPoetTenure());
        ImageHelper.setImage(poetViewHolder.imgCollectionImage, topPoet.getImageURL());
    }

    @Override
    public int getItemCount() {
        return topPoets.size();
    }

    class PoetViewHolder extends BaseViewHolder {
        @BindView(R.id.imgCollectionImage)
        CircleImageView imgCollectionImage;
        @BindView(R.id.txtCollectionTitle)
        TitleTextViewType6 txtCollectionTitle;
        @BindView(R.id.txtPoetTenure)
        TitleTextViewType6 txtPoetTenure;


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
