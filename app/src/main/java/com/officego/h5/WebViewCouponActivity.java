package com.officego.h5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
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
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.dialog.MapDialog;
import com.officego.commonlib.common.dialog.WeChatShareDialog;
import com.officego.commonlib.common.model.ShareBean;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.model.ChatsBean;
import com.officego.ui.login.LoginActivity_;
import com.officego.ui.message.ConversationActivity_;
import com.officego.view.webview.SMWebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YangShiJie
 * Data 2020/5/26.
 * Descriptions:WebView
 **/
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.activity_webview_coupon)
public class WebViewCouponActivity extends BaseActivity {
    @ViewById(R.id.wv_view)
    WebView webView;
    @ViewById(R.id.rl_exception)
    RelativeLayout rlException;
    @ViewById(R.id.btn_again)
    Button btnAgain;
    @Extra
    String amountRange;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        CommonHelper.setRelativeLayoutParams(context, webView, 8);
        setWebChromeClient();
        if (NetworkUtils.isNetworkAvailable(context)) {
            loadWebView(AppConfig.MEETING_ROOM_URL + strMap());
        } else {
            receiverExceptionError(webView);
        }
    }

    private String strMap() {
        return "?channel=2" +
                "&token=" + SpUtils.getSignToken() +
                "&amountRange=" + (TextUtils.isEmpty(amountRange) ? "" : amountRange);
    }

    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                exceptionPageReceivedTitle(view, title);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
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

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
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
        //webView在5.0后默认关闭混合加载http不能加载https资源
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //高德地图
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSetting.setGeolocationEnabled(true);
        webSetting.setGeolocationDatabasePath(dir);
        webView.addJavascriptInterface(new JsInterface(this), "android");
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
                showLoadingDialog();
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
            webView.loadUrl(AppConfig.MEETING_ROOM_URL);
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
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
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

    private class JsInterface {
        private final Context context;

        JsInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void closeView() {
            finish();
        }

        @JavascriptInterface
        public void shareClick(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            String officeTitle = object.getString("officeTitle");
            String url = object.getString("url");
            String mainPic = object.getString("mainPic");
            ShareBean bean = new ShareBean();
            bean.setbType(Constants.TYPE_MEETING_ROOM);
            bean.setTitle(officeTitle);
            bean.setDes("");
            bean.setImgUrl(mainPic);//图片url
            bean.setDetailsUrl(url);//分享url
            new WeChatShareDialog(context, bean);
        }

        @JavascriptInterface
        public void callPhoneClick(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            String phone = object.getString("phone");
            CommonDialog dialog = new CommonDialog.Builder(context)
                    .setTitle(phone)
                    .setCancelButton(R.string.sm_cancel)
                    .setConfirmButton("拨打", (dialog12, which) -> {
                        dialog12.dismiss();
                        CommonHelper.callPhone(context, phone);
                    }).create();
            dialog.showWithOutTouchable(true);
        }

        @JavascriptInterface
        public void chatClick(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            int buildingId = object.getInt("buildingId");
            if (TextUtils.isEmpty(SpUtils.getSignToken())) {
                LoginActivity_.intent(context).isFinishCurrentView(true).start();
                return;
            }
            gotoChat(buildingId);
        }

        @JavascriptInterface
        public void mapClick(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            double latitude = TextUtils.isEmpty(object.getString("latitude")) ? 0 :
                    Double.parseDouble(object.getString("latitude"));
            double longitude = TextUtils.isEmpty(object.getString("longitude")) ? 0 :
                    Double.parseDouble(object.getString("longitude"));
            String address = object.getString("address");
            new MapDialog(context, latitude, longitude, address);
        }
    }

    /**
     * 服务端创建聊天会话
     */
    public void gotoChat(int buildingId) {
        if (buildingId != 0) {
            showLoadingDialog();
            OfficegoApi.getInstance().getTargetId3(buildingId, new RetrofitCallback<ChatsBean>() {
                @Override
                public void onSuccess(int code, String msg, ChatsBean data) {
                    hideLoadingDialog();
                    ConversationActivity_.intent(context).buildingId(buildingId)
                            .targetId(data.getTargetId()).isMeetingEnter(true).start();
                }

                @Override
                public void onFail(int code, String msg, ChatsBean data) {
                    hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        shortTip(msg);
                    }
                }
            });
        }
    }
}
