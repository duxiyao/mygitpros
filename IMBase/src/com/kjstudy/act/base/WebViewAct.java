package com.kjstudy.act.base;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.imbase.R;

public class WebViewAct extends KJActivity {

    public static final String EXTRA_RAWURL = "rawUrl";

    public static final String EXTRA_TITLE = "title";

    protected WebView mWebView;

    @BindView(id = R.id.rl_loading)
    private RelativeLayout mRlLoding;

    @BindView(id = R.id.loading_progress)
    private ImageView mIvAnim;

    @BindView(id = R.id.tv_progress)
    private TextView mTvProgress;

    @BindView(id = R.id.tv_tips)
    private TextView mTvTips;

    private String mRawUrl;

    protected String mTitle;

    protected boolean isUsePageTitle = false;

    private PullToRefreshWebView mPullToRefreshWebView;

    private void initWebView() {
        setContentView(R.layout.layout_webview_ui);
        mPullToRefreshWebView = (PullToRefreshWebView) findViewById(R.id.wv_pull_refresh);
        mPullToRefreshWebView
                .setOnRefreshListener(new OnRefreshListener<WebView>() {

                    @Override
                    public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                        try {
                            if (mWebView != null)
                                mWebView.reload();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        mWebView = mPullToRefreshWebView.getRefreshableView();
        mRlLoding = (RelativeLayout) findViewById(R.id.rl_loading);
        mIvAnim = (ImageView) findViewById(R.id.loading_progress);
        mTvProgress = (TextView) findViewById(R.id.tv_progress);
        mTvTips = (TextView) findViewById(R.id.tv_tips);
        if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP,
                    Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        }

        boolean zoom = getIntent().getBooleanExtra("zoom", true);
        boolean useJs = getIntent().getBooleanExtra("useJs", true);

        mWebView.getSettings().setJavaScriptEnabled(useJs);
        mWebView.getSettings().setPluginState(PluginState.ON);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(zoom);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setSaveFormData(false);
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                onSetTitle(view.getTitle());
            }

            @Override
            public void onReceivedSslError(WebView view,
                    SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)
                        && !(url.startsWith("http") || url.startsWith("file:"))) {
                    Uri uri = Uri.parse(url);
                    if (uri != null) {
                        Intent intent = new Intent();
                        intent.setData(uri);
                        try {
                            startActivity(intent);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    onResetBar();
                    mWebView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (isUsePageTitle)
                    mTitle = title;
                onSetTitle(mTitle);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mTvProgress.setText(newProgress + "%");
                // mTvTips.setText(R.string.tips_get_data);
                if (newProgress >= 100) {
                    mPullToRefreshWebView.onRefreshComplete();
                    mRlLoding.setVisibility(View.GONE);
                    mPullToRefreshWebView.setVisibility(View.VISIBLE);
                }
            }
        });
        init();
    }

    protected void onResetBar() {
    }

    private void init() {
        mRawUrl = getRawUrl();

        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        if (TextUtils.isEmpty(mTitle))
            mTitle = mRawUrl;
        // setActionBarTitle(mTitle);
        openUrl(mRawUrl);
    }

    protected void openUrl(String url) {
        // mWebView.loadData(url, "text/html", "utf-8");
        mWebView.loadUrl(url);
    }

    protected String getRawUrl() {
        String url = getIntent().getStringExtra(EXTRA_RAWURL);
        if (!TextUtils.isEmpty(url)) {
            return url;
        }

        Uri uri = getIntent().getData();
        if (uri == null)
            return "";
        return uri.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPullToRefreshWebView.getState() == State.RESET) {
            mIvAnim.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.loading_progress_animation);
            mIvAnim.setAnimation(animation);
        }
    }

    protected void onSetTitle(String title) {

    }

    protected void goBack() {
        onResetBar();
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        finish();
    }

    @Override
    public void setRootView() {
        initWebView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView != null
                && mWebView.canGoBack()) {
            goBack();
        } else
            finish();
        // return super.onKeyDown(keyCode, event);
        return true;
    }
}
