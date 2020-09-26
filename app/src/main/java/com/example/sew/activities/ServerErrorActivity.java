package com.example.sew.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.ServiceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServerErrorActivity extends BaseActivity {


    @BindView(R.id.networ_error_msg)
    LinearLayout networErrorMsg;
    @BindView(R.id.retryText)
    TextView retryText;
    @BindView(R.id.txtOr)
    TextView txtOr;
    @BindView(R.id.txtOfflineBrowsingUnavailable)
    TextView txtOfflineBrowsingUnavailable;
    @BindView(R.id.browseOffTxt)
    TextView browseOffTxt;
    @BindView(R.id.txtKnowWhy)
    TextView txtKnowWhy;

    public static Intent getInstance(BaseActivity activity) {
        return new Intent(activity, ServerErrorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_error);
        ButterKnife.bind(this);
        retryText.setOnClickListener(view -> {
            if (new ServiceManager(getActivity()).isNetworkAvailable()) {
                MyApplication.getInstance().setBrowsingOffline(false);
                // startActivity(HomeActivity.getInstance(getActivity(),false));
                new Handler().postDelayed(this::finish, 1000);
                sendBroadCast(BROADCAST_INTERNET_RESTORE);
            } else showToast(AppErrorMessage.please_check_your_connection_and_try_again);
        });

        browseOffTxt.setOnClickListener(view -> {
            Intent intent1 = new Intent(getActivity(), FavoriteActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
        });
        txtKnowWhy.setOnClickListener(view -> showToast("Login to access my favorite"));
        browseOffTxt.setVisibility(View.GONE);
        txtOr.setVisibility(View.GONE);
        txtKnowWhy.setVisibility(View.GONE);
        txtOfflineBrowsingUnavailable.setVisibility(View.GONE);
        if (!MyService.isUserLogin()) {
            txtKnowWhy.setVisibility(View.VISIBLE);
            txtOfflineBrowsingUnavailable.setVisibility(View.VISIBLE);
        } else {
            browseOffTxt.setVisibility(View.VISIBLE);
            txtOr.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
