package com.officego.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.rongcloud.IMManager;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.HttpsUtils;
import com.officego.commonlib.utils.Utils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.config.DbConfig;
import com.sensorsdata.analytics.android.sdk.SAConfigOptions;
import com.sensorsdata.analytics.android.sdk.SensorsAnalyticsAutoTrackEventType;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.tencent.bugly.crashreport.CrashReport;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.RequestCallback;

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
        //JPush一键登录
        quickLogin();
        //create file
        FileHelper.getInstance();
        handleSSLHandshake();
        //初始化创建数据库
        DbConfig.init(context);
        //log 开关
        LogCat.init(!TextUtils.equals(env, AppConfig.ENV_RELEASE));
        //IM
        IMManager.getInstance().init(context);
        //SensorsData
        initSensorsData(env);
        //bugly
        CrashReport.initCrashReport(context, AppConfig.BUGLY_ID, true);
        if (!TextUtils.isEmpty(SpUtils.getUserId())) {
            CrashReport.setUserId(SpUtils.getUserId());
        }
    }

    //神策初始化
    private void initSensorsData(String env) {
        boolean isOpenLog = !TextUtils.equals(env, AppConfig.ENV_RELEASE);
        // 初始化配置
        SAConfigOptions saConfigOptions = new SAConfigOptions(AppConfig.SA_SERVER_URL);
        // 开启全埋点
        saConfigOptions
                .setAutoTrackEventType(SensorsAnalyticsAutoTrackEventType.APP_CLICK |
                        SensorsAnalyticsAutoTrackEventType.APP_START |
                        SensorsAnalyticsAutoTrackEventType.APP_END |
                        SensorsAnalyticsAutoTrackEventType.APP_VIEW_SCREEN)
                //开启 Log
                .enableLog(isOpenLog);
        // 开启可视化全埋点
        saConfigOptions.enableVisualizedAutoTrack(true);
        // 需要在主线程初始化神策 SDK
        SensorsDataAPI.startWithConfigOptions(context, saConfigOptions);
        // 开启 App 打通  H5支持API level 16 上版本
        saConfigOptions.enableJavaScriptBridge(false);
        //初始化公共属性
        SensorsTrack.superProperties();
        //初始化动态公共属性
        SensorsTrack.dynamicSuperProperties();
    }

    //JPush一键登录
    private void quickLogin() {
        JVerificationInterface.setDebugMode(true);
        final long start = System.currentTimeMillis();
        JVerificationInterface.init(context, (code, result) ->
                LogCat.d("RootLoader", "[JPush init] code = " + code + " result = " +
                result + " consists = " + (System.currentTimeMillis() - start)));
    }

    //Glide加载https部分失败，设置信任证书
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
