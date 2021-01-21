package com.officego.h5.call;

import android.app.Activity;
import android.webkit.JavascriptInterface;

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
