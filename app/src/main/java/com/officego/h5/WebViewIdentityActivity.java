package com.officego.h5;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
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
import android.widget.TextView;

import com.officego.MainOwnerActivity_;
import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.model.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.common.rongcloud.RCloudConnectUtils;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.h5.call.JSIdentityCall;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.login.LoginActivity_;
import com.officego.view.webview.SMWebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/5/26.
 * Descriptions:WebView
 **/
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.activity_webview_coupon)
public class WebViewIdentityActivity extends BaseActivity implements
        JSIdentityCall.JSIdentityCallListener {
    @ViewById(R.id.wv_view)
    WebView webView;
    @ViewById(R.id.rl_exception)
    RelativeLayout rlException;
    @ViewById(R.id.btn_again)
    Button btnAgain;

    @SuppressLint("SetTextI18n")
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        setWebChromeClient();
        loadWebView(strPath());
    }

    private String strPath() {
        return AppConfig.TO_IDENTITY_URL +
                "?channel=2" +
                "&token=" + (TextUtils.isEmpty(SpUtils.getSignToken()) ? "" : SpUtils.getSignToken());
    }

    //设置setWebChromeClient对象
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
        showLoadingDialog();
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
        //webview在5.0后默认关闭混合加载http不能加载https资源
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        JSIdentityCall jsIdentityCall = new JSIdentityCall(this);
        jsIdentityCall.setListener(this);
        webView.addJavascriptInterface(jsIdentityCall, "android");
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
                hideLoadingDialog();
                super.onPageFinished(view, url);
            }

            @Override
            protected void receiverError(WebView view, WebResourceRequest request, WebResourceError error) {
                hideLoadingDialog();
                exceptionPageError(view, request);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                hideLoadingDialog();
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });
    }


    /**
     * 网络异常
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
            webView.loadUrl(AppConfig.TO_IDENTITY_URL);
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
        if (webView != null) {
            webView.destroy();
        }
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
    @SuppressLint("SetTextI18n")
    private void exceptionPageHttpError(WebView view, WebResourceResponse errorResponse) {
        int statusCode = errorResponse.getStatusCode();
        if (404 == statusCode || 500 == statusCode) {
            view.loadUrl("about:blank");// 避免出现默认的错误界面
            view.removeAllViews();
            receiverExceptionError(view);
            TextView tv = findViewById(R.id.tv_net_tip);
            Button button = findViewById(R.id.btn_again);
            tv.setText("VR链接打开异常 \n 请稍后再试");
            button.setVisibility(View.GONE);
        }
    }

    @Override
    public void toSwitchOwner() {
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            LoginActivity_.intent(context).isFinishCurrentView(true).start();
            return;
        }
        switchDialog();
    }

    private void switchDialog() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(R.string.are_you_sure_switch_owner)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    switchId();
                    //神策
                    SensorsTrack.tenantToOwner();
                })
                .setCancelButton(R.string.sm_cancel, (dialog1, which) -> dialog1.dismiss()).create();
        dialog.showWithOutTouchable(false);
    }

    //租户端---用户身份标：0租户，1户主
    private void switchId() {
        showLoadingDialog();
        OfficegoApi.getInstance().switchId(Constants.TYPE_OWNER, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                hideLoadingDialog();
                SpUtils.saveLoginInfo(data, SpUtils.getPhoneNum());
                SpUtils.saveRole(String.valueOf(data.getRid()));
                new RCloudConnectUtils();//连接融云
                if (TextUtils.equals(Constants.TYPE_TENANT, String.valueOf(data.getRid()))) {
                    GotoActivityUtils.mainActivity(context); //跳转租户首页
                } else if (TextUtils.equals(Constants.TYPE_OWNER, String.valueOf(data.getRid()))) {
                    MainOwnerActivity_.intent(context).start(); //租户切换房东
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                if (code == Constants.ERROR_CODE_5009) {
                    hideLoadingDialog();
                    shortTip(msg);
                    SpUtils.clearLoginInfo();
                    GotoActivityUtils.loginClearActivity(context, false);
                }
            }
        });
    }
}
