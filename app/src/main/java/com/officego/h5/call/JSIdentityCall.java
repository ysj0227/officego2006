package com.officego.h5.call;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.officego.MainOwnerActivity_;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.rongcloud.ConnectRongCloudUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.rpc.OfficegoApi;

/**
 * Created by shijie
 * Date 2021/1/6
 **/
public class JSIdentityCall {
    private final Activity context;
    private JSIdentityCallListener listener;

    public interface JSIdentityCallListener {
        void toSwitchOwner();
    }

    public JSIdentityCallListener getListener() {
        return listener;
    }

    public void setListener(JSIdentityCallListener listener) {
        this.listener = listener;
    }

    public JSIdentityCall(Activity context) {
        this.context = context;
    }

    @JavascriptInterface
    public void closeView() {
        context.finish();
    }

    @JavascriptInterface
    public void toSwitchOwner() {
        if (listener!=null){
            listener.toSwitchOwner();
        }
    }
}
