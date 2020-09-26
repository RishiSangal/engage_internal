package com.example.sew.views.paging_recycler_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.sew.R;

/**
 * Created by dss886 on 16/7/25.
 */
public class PagingRecyclerView extends RecyclerView {

    public static final int HEAD = 1;
    public static final int FOOT = 2;

    private PagingAdapterDecorator mAdapter;
    private PagingScrollListener mInnerScrollListener;
    private OnScrollListener mScrollListener;

    public PagingRecyclerView(Context context) {
        super(context);
    }

    public PagingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        boolean supportLayoutManager = layout instanceof LinearLayoutManager
                || layout instanceof StaggeredGridLayoutManager;
        if (supportLayoutManager) {
            addOnScrollListener(mInnerScrollListener = new PagingScrollListener(mAdapter));
        } else {
            Log.w("PagingRecyclerView", "You are using a custom LayoutManager and OnScrollListener cannot be set automatically, you need to implement and add it by yourself.");
        }
        super.setLayoutManager(layout);
    }

    @Override
    public void addOnScrollListener(@NonNull OnScrollListener listener) {
        super.addOnScrollListener(listener);
        mScrollListener = listener;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        //noinspection unchecked
        this.mAdapter = new PagingAdapterDecorator(getContext(), this, adapter);
        if (getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager grid = (GridLayoutManager) getLayoutManager();
            int spanCount = grid.getSpanCount();
            GridLayoutManager.SpanSizeLookup lookup = grid.getSpanSizeLookup();
            grid.setSpanSizeLookup(new PagingSpanSizeLookup(mAdapter, spanCount, lookup));
        }
        if (mInnerScrollListener != null) {
            mInnerScrollListener.setAdapter(mAdapter);
        }
        super.setAdapter(mAdapter);
    }

    public void setOnPagingListener(OnPagingListener listener) {
        if (mAdapter == null) {
            throw new IllegalArgumentException("Please set adapter before setting OnPagingListener!");
        }
        mAdapter.setOnPagingListener(listener);
    }

    @Override
    public void scrollToPosition(int position) {
        if (mAdapter.isHeader(0)) {
            position++;
        }
        super.scrollToPosition(position);
    }

    public void setPageEnable(boolean header, boolean footer) {
        mAdapter.setPageEnable(header, footer);
        mScrollListener.onScrollStateChanged(this, getScrollState());
    }

    /**
     * Override this method to custom your own paging item
     */
    protected AbsPagingViewHolder createPagingViewHolder(LayoutInflater inflater, ViewGroup parent, int direction, OnPagingListener pagingListener) {
        View view = inflater.inflate(R.layout.paging_recycler_view_default_item, parent, false);
        return new DefaultPagingViewHolder(view, direction, this, pagingListener);
    }

    public void onPaging() {
        if (mAdapter != null)
            mAdapter.onPaging(FOOT);
    }

    public void onNoMoreData() {
        if (mAdapter != null)
            mAdapter.onNoMoreData(FOOT);
    }

    public void onHide() {
        if (mAdapter != null)
            mAdapter.onHide(FOOT);
    }

    public void onLoading() {
        if (mAdapter != null)
            mAdapter.onLoading(FOOT);
    }

    public void onPaging(int direction) {
        mAdapter.onPaging(direction);
    }

    public void onFailure(int direction) {
        mAdapter.onFailure(direction);
    }

    public void onNoMoreData(int direction) {
        mAdapter.onNoMoreData(direction);
    }

    public void onHide(int direction) {
        mAdapter.onHide(direction);
    }

    public void onLoading(int direction) {
        mAdapter.onLoading(direction);
    }

    public int getFixedPosition(int position) {
        return mAdapter.getFixedPosition(position);
    }

    public interface OnPagingListener {
        void onPaging(PagingRecyclerView view, int direction);
    }

}
