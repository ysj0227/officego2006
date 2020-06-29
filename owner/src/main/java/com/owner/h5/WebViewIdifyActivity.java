package com.owner.h5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.webview.SMWebChromeClient;
import com.officego.commonlib.view.webview.SMWebViewClient;
import com.owner.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/5/26.
 * Descriptions:WebView
 * 业主身份认证
 **/
@SuppressLint("Registered")
@EActivity(resName = "activity_owner_idify_webview")
public class WebViewIdifyActivity extends BaseActivity {
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "wv_view")
    WebView webView;
    @ViewById(resName = "rl_exception")
    RelativeLayout rlException;
    @ViewById(resName = "btn_again")
    Button btnAgain;
    private String webViewUrl;
    private SMWebChromeClient webChrome;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        CommonHelper.setRelativeLayoutParams(context, webView);
        setWebChromeClient();
        loadWebView(AppConfig.H5_OWNER_idify + identity());//认证
    }

    private String identity() {
        return "?token=" + SpUtils.getSignToken() + "&channel=2&identity=1";
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
//        String postData = String.format("rongyuntoken=%s&token=%s",SpUtils.getRongToken(),SpUtils.getSignToken());
//        webView.postUrl(url, postData.getBytes());
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setDefaultTextEncodingName("utf-8");
        webSetting.setSupportZoom(false);
        webSetting.setDomStorageEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadsImagesAutomatically(true);
        webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSetting.setAllowFileAccess(true);// 设置允许访问文件数据
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setBlockNetworkImage(false);//解决图片不显示
        webView.addJavascriptInterface(new JsInterface(this), "android");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.setWebChromeClient(new WebChromeClient());//
        webChrome = new SMWebChromeClient(this);
        webView.setWebChromeClient(webChrome);
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
                LogCat.e(TAG, "onPageStarted");
                webViewUrl = url;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogCat.e(TAG, "onPageFinished");
            }

            @Override
            protected void receiverError(WebView view, WebResourceRequest request, WebResourceError error) {
                titleBar.setVisibility(View.VISIBLE);
                exceptionPageError(view, request);
                LogCat.e(TAG, "receiverError");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                titleBar.setVisibility(View.VISIBLE);
//                exceptionPageHttpError(view, errorResponse);
                LogCat.e(TAG, "onReceivedHttpError");
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });
    }

    //上传图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (webChrome != null) {
            webChrome.uploadImage(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (webChrome != null) {
            webChrome.onPermissionResult(requestCode, grantResults);
        }
    }

    /**
     * 网络异常
     *
     * @param view
     */
    private void receiverExceptionError(WebView view) {
        titleBar.setVisibility(View.VISIBLE);
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
            if (TextUtils.isEmpty(webViewUrl)) {
                webView.loadUrl(AppConfig.H5_OWNER_idify);
            } else {
                webView.loadUrl(webViewUrl);
            }
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

    //js传递给Android
    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void closeView() {
            Log.d("TAG", "js to android closeView");
            finish();
        }

        @JavascriptInterface
        public void identifyComplete() {
            Log.d("TAG", "js to android identifyComplete");
            //认证完成
            BaseNotification.newInstance().postNotificationName(CommonNotifications.ownerIdentityComplete, "ownerIdentityComplete");
            finish();
        }
    }

}
