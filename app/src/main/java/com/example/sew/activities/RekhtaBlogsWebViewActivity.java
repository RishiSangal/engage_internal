package com.example.sew.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.example.sew.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RekhtaBlogsWebViewActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    private String targetUrl;
    public static String staticTargeturl = "https://blog.rekhta.org/?ref=app";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekhta_blogs_webview);
        ButterKnife.bind(this);
        targetUrl = getIntent().getStringExtra(TARGET_URL);
        showDialog();
        loadWebView();
    }

    public static Intent getInstance(Activity activity, String targetUrl) {
        Intent intent = new Intent(activity, RekhtaBlogsWebViewActivity.class);
        intent.putExtra(TARGET_URL, targetUrl);
        return intent;
    }

    public void loadWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new CustomClient());
        webView.getSettings().setBuiltInZoomControls(true);
        if(!TextUtils.isEmpty(targetUrl))
            webView.loadUrl(targetUrl);
        else
            webView.loadUrl(staticTargeturl);
    }

    public class CustomClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissDialog();
        }
    }
}
