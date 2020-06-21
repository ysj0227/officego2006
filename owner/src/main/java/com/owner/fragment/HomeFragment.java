package com.owner.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
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

import androidx.annotation.NonNull;

import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.webview.SMWebChromeClient;
import com.officego.commonlib.view.webview.SMWebViewClient;
import com.owner.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@EFragment(resName = "home_owner_fragment")
public class HomeFragment extends BaseFragment {

    @ViewById(resName = "wv_view")
    WebView webView;
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "rl_exception")
    RelativeLayout rlException;
    @ViewById(resName = "btn_again")
    Button btnAgain;

    private SMWebChromeClient webChrome;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(mActivity);
        setWebChromeClient();
        loadWebView("https://www.baidu.com/");
    }

    /**
     * 设置setWebChromeClient对象
     */
    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(title) || title.contains("http")) {
                    titleBar.getAppTitle().setText(R.string.app_name);
                } else {
                    titleBar.getAppTitle().setText(title);
                }
                exceptionPageReceivedTitle(view, title);
            }
        });
    }
    //todo 返回操作
//
//    @Override
//    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

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
        webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSetting.setAllowFileAccess(true);// 设置允许访问文件数据
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setBlockNetworkImage(false);//解决图片不显示
//        webView.addJavascriptInterface(new JsInterface(this), "android");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.setWebChromeClient(new WebChromeClient());//
        webChrome = new SMWebChromeClient(mActivity);
        webView.setWebChromeClient(webChrome);
        webView.loadUrl(url);
        webView.setWebViewClient(new SMWebViewClient(mActivity) {
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
                Log.d(TAG, "webview onPageFinished url=" + url);
            }

            @Override
            protected void receiverError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d(TAG, "webview receiverError");
                exceptionPageError(view, request);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                exceptionPageHttpError(view, errorResponse);
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
        webView.setVisibility(View.GONE);
        rlException.setVisibility(View.VISIBLE);
        btnAgain.setOnClickListener(v -> {
            if (!NetworkUtils.isNetworkAvailable(mActivity)) {
                shortTip(getString(R.string.str_check_net));
                return;
            }
            webView.setVisibility(View.VISIBLE);
            rlException.setVisibility(View.GONE);
            view.clearCache(true);
            view.clearHistory();
             //TODO
//            webView.loadUrl(AppConfig.H5_MINE_CENTER_URL);
//            if (TextUtils.isEmpty(webViewUrl)) {
////                loadWebView(AppConfig.H5_MINE_CENTER_URL);
//                webView.loadUrl(AppConfig.H5_MINE_CENTER_URL);
//            } else {
//                webView.loadUrl(webViewUrl);
//            }
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
    public void onDestroyView() {
        super.onDestroyView();
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
