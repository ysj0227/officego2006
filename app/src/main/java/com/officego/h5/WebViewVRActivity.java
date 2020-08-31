package com.officego.h5;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.TitleBarView;
import com.officego.view.webview.SMWebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/5/26.
 * Descriptions:WebView
 **/
@SuppressLint("Registered")
@EActivity(R.layout.activity_webview_vr)
public class WebViewVRActivity extends BaseActivity {
    @ViewById(R.id.wv_view)
    WebView webView;
    @ViewById(R.id.title_bar)
    TitleBarView titleBar;
    @ViewById(R.id.rl_exception)
    RelativeLayout rlException;
    @ViewById(R.id.btn_again)
    Button btnAgain;
    @Extra
    String vrUrl;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        setWebChromeClient();
        titleBar.getAppTitle().setText("");
        vrUrl = "https://sky.city8.com/panoramic-images/IMG_339520200828-125117-122787.html";
        loadWebView(vrUrl);
    }

    /**
     * 设置setWebChromeClient对象
     */
    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                exceptionPageReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView(String url) {
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setDefaultTextEncodingName("utf-8");
        webSetting.setSupportZoom(false);
        webSetting.setDomStorageEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadsImagesAutomatically(true);
        webSetting.setAllowFileAccess(true);// 设置允许访问文件数据
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setBlockNetworkImage(false);//解决图片不显示
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
        webView.setWebViewClient(new SMWebViewClient(this) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            protected void receiverError(WebView view, WebResourceRequest request, WebResourceError error) {
                exceptionPageError(view, request);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                exceptionPageHttpError(view, errorResponse);
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });
    }


    /**
     * 网络异常
     *
     * @param view
     */
    private void receiverExceptionError(WebView view) {
        webView.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        btnAgain.setOnClickListener(v -> {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                shortTip(getString(R.string.str_check_net));
                return;
            }
            webView.setVisibility(View.VISIBLE);
            rlException.setVisibility(View.GONE);
            view.clearCache(true);
            view.clearHistory();
            webView.loadUrl(vrUrl);
        });
    }

    private void clearCache() {
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookies(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearCache();
    }

    /**
     * Android 6.0以下处理方法：
     * onReceivedHttpError
     */
    private void exceptionPageReceivedTitle(WebView view, String title) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (title.contains("404") || title.contains("500") ||
                    title.contains("Error") || title.contains(getString(R.string.webview_cannot_find_page))
                    || title.contains(getString(R.string.webview_cannot_open_page))) {
                view.loadUrl("about:blank");// 避免出现默认的错误界面
                view.removeAllViews();
                receiverExceptionError(view);
            }
        }
    }

    /**
     * Android 6.0以上处理方法
     * receiverError
     */
    private void exceptionPageError(WebView view, WebResourceRequest request) {
        if (request.isForMainFrame()) {//是否是为 main frame创建
            view.loadUrl("about:blank");// 避免出现默认的错误界面
            view.removeAllViews();
            receiverExceptionError(view);// 加载自定义错误页面
        }
    }

    /**
     * Android 6.0以上处理方法
     * onReceivedHttpError
     */
    private void exceptionPageHttpError(WebView view, WebResourceResponse errorResponse) {
        int statusCode = errorResponse.getStatusCode();
        if (404 == statusCode || 500 == statusCode) {
            view.loadUrl("about:blank");// 避免出现默认的错误界面
            view.removeAllViews();
            receiverExceptionError(view);
        }
    }
}
