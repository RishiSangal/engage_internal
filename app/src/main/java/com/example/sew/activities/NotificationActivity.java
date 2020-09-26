package com.example.sew.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.adapters.NotificationAdapter;
import com.example.sew.common.PagingListView;
import com.facebook.shimmer.ShimmerFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends BaseActivity {
    @BindView(R.id.view)
    View view;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstNotification)
    PagingListView lstNotification;
    public NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        getAllNotification();

    }

    private void getAllNotification() {
        notificationAdapter = new NotificationAdapter(this);
        lstNotification.setAdapter(notificationAdapter);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
    }
}
