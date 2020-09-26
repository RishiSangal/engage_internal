package com.example.sew.views.paging_recycler_view;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by duansishu on 2018/12/31.
 */
public abstract class AbsPagingViewHolder extends RecyclerView.ViewHolder {

    protected int direction;

    public AbsPagingViewHolder(View itemView, int direction) {
        super(itemView);
        this.direction = direction;
    }

    abstract void onHide();

    abstract void onPaging();

    abstract void onLoading();

    abstract void onNoMoreData();

    abstract void onError();

}
