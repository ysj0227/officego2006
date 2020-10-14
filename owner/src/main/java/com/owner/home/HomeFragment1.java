package com.owner.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.update.VersionDialog;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.commonlib.view.webview.SMWebChromeClient;
import com.officego.commonlib.view.webview.SMWebViewClient;
import com.owner.R;
import com.owner.home.contract.HomeContract;
import com.owner.home.presenter.HomePresenter;
import com.owner.identity.SelectIdActivity_;
import com.owner.mine.model.UserOwnerBean;
import com.owner.utils.UnIdifyDialog;
import com.owner.zxing.QRScanActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import static com.officego.commonlib.utils.PermissionUtils.REQ_PERMISSIONS_CAMERA_STORAGE;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint("NewApi")
@EFragment(resName = "home_owner_fragment")
public class HomeFragment1 extends BaseMvpFragment<HomePresenter> implements HomeContract.View {

    @ViewById(resName = "wv_view")
    WebView webView;
    @ViewById(resName = "rl_exception")
    RelativeLayout rlException;
    @ViewById(resName = "btn_again")
    Button btnAgain;
    @ViewById(resName = "iv_scan")
    ImageView tvScan;

    private String webViewUrl;
    private SMWebChromeClient webChrome;

    @AfterViews
    void init() {
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
        StatusBarUtils.setStatusBarColor(mActivity);
        CommonHelper.setRelativeLayoutParams(mActivity, webView);
        setWebChromeClient();
        if (!fragmentCheckSDCardCameraPermission()) {
            return;
        }
        //版本更新
        new VersionDialog(mActivity);
        mPresenter.getUserInfo();
    }

    //扫一扫
    @Click(resName = "iv_scan")
    void scanClick() {
//        scanDialog(getContext());
//        new ServiceSelectedDialog(mActivity);
        UploadVideoVrActivity_.intent(getContext()).start();
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
//        webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSetting.setAllowFileAccess(true);// 设置允许访问文件数据
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setBlockNetworkImage(false);//解决图片不显示
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
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
                showLoadingDialog();
                webViewUrl = url;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoadingDialog();
            }

            @Override
            protected void receiverError(WebView view, WebResourceRequest request, WebResourceError error) {
                hideLoadingDialog();
                exceptionPageError(view, request);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                hideLoadingDialog();
//                exceptionPageHttpError(view, errorResponse);
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });
    }

    // SD卡,相机 fragment
    private boolean fragmentCheckSDCardCameraPermission() {
        //mActivity1 必须使用this 在fragment
        String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission1 = mActivity.checkSelfPermission(PERMISSIONS_STORAGE[0]);
            int permission2 = mActivity.checkSelfPermission(PERMISSIONS_STORAGE[1]);
            if (permission1 != PackageManager.PERMISSION_GRANTED ||
                    permission2 != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{
                        PERMISSIONS_STORAGE[0], PERMISSIONS_STORAGE[1]}, REQ_PERMISSIONS_CAMERA_STORAGE);
                return false;
            } else {
                return true;
            }
        }
        return true;
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
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPresenter.getUserInfo();
        }
        if (webChrome != null) {
            webChrome.onPermissionResult(requestCode, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 网络异常
     *
     * @param view view
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
            if (TextUtils.isEmpty(webViewUrl)) {
                webView.loadUrl(AppConfig.H5_OWNER_BUILDINGlIST);
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

    /**
     * 身份类型 0个人1企业2联合
     * getAuditStatus 0待审核1审核通过2审核未通过 3过期(和2未通过一样处理)-1未认证
     */
    @Override
    public void userInfoSuccess(UserOwnerBean data) {
        if (isIdentity(data)) {
            tvScan.setVisibility(View.GONE);
            if (data.getAuditStatus() == -1) { //未认证
                SelectIdActivity_.intent(getContext()).start();
            } else {
                new UnIdifyDialog(mActivity, data);
            }
        } else {
            tvScan.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(SpUtils.getEditToWeb())) {
                scanDialog(getContext());
                SpUtils.saveEditToWeb();
            }
            if (data.getIdentityType() == 2) {
                loadWebView(AppConfig.H5_OWNER_HOUSElIST + identity());
            } else {
                loadWebView(AppConfig.H5_OWNER_BUILDINGlIST + identity());
            }
        }
    }

    @Override
    public void userInfoFail(int code, String msg) {

    }

    // 0待审核1审核通过2审核未通过
    private boolean isIdentity(UserOwnerBean mUserInfo) {
        if (mUserInfo != null) {
            return mUserInfo.getAuditStatus() != 0 && mUserInfo.getAuditStatus() != 1;
        }
        return false;
    }

    private String identity() {
        return "?token=" + SpUtils.getSignToken() + "&channel=2&identity=1";
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{
                CommonNotifications.ownerIdentityHandle};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (args == null) {
            return;
        }
        if (id == CommonNotifications.ownerIdentityHandle) {
            mPresenter.getUserInfo();
        }
    }

    //扫一扫
    private void scanDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_scan, null);
        dialog.setContentView(inflate);
        inflate.findViewById(R.id.btn_close).setOnClickListener(v -> dialog.dismiss());
        inflate.findViewById(R.id.btn_app).setOnClickListener(v -> dialog.dismiss());
        inflate.findViewById(R.id.btn_web).setOnClickListener(v -> {
            if (!fragmentCheckSDCardCameraPermission()) {
                return;
            }
            dialog.dismiss();
            startActivity(new Intent(getActivity(), QRScanActivity.class));
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            CommonDialog dialog = new CommonDialog.Builder(getContext())
                    .setMessage("账号已退出，请重新登录")
                    .setConfirmButton(com.officego.commonlib.R.string.str_login, (dialog12, which) -> {
                        GotoActivityUtils.gotoLoginActivity(getActivity());
                        dialog12.dismiss();
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        }
    }
}
