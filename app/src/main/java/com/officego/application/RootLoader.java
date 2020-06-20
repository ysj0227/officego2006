package com.officego.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.HttpsUtils;
import com.officego.commonlib.utils.Utils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.constant.AppConfig;
import com.officego.config.DbConfig;
import com.officego.commonlib.common.rongcloud.IMManager;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 **/
public class RootLoader {
    private Context context;

    public RootLoader(Context context) {
        this.context = context;
    }

    public void init() {
        String env = Utils.getMetaValue(context, "ENV_DATA", AppConfig.ENV_TEST);
        new AppConfig().init(context, env);
        //create file
        FileHelper.getInstance();
        handleSSLHandshake();
        //初始化创建数据库
        DbConfig.init(context);
        //log 开关
        LogCat.init(!TextUtils.equals(env, AppConfig.ENV_RELEASE));
        IMManager.getInstance().init(context);
    }


    /**
     * Glide加载https部分失败，设置信任证书
     */
    private void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sc = SSLContext.getInstance("TLS"); // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
