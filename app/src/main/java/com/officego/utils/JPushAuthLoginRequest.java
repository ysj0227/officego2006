package com.officego.utils;

import android.content.Context;

import com.officego.R;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.LoadingDialog;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.dialog.CommonDialog;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.LoginSettings;

/**
 * Created by shijie
 * Date 2020/12/15
 * 极光授权登录
 **/
public class JPushAuthLoginRequest {

    public static final String TAG = "JPushAuthLoginRequest";
    private static final int LOGIN_SUCCESS = 6000;
    private static final int LOGIN_EXCEPTION = 6003;
    private static final int LOGIN_NETWORK_FAIL = 2016;
    private LoadingDialog loadingDialog;

    public static JPushAuthLoginRequest getInstance() {
        return Singleton.INSTANCE;
    }

    private static final class Singleton {
        private static final JPushAuthLoginRequest INSTANCE = new JPushAuthLoginRequest();
    }

    //sdk集成页面
    public void authLogin(Context mContext) {
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(mContext);
        if (!verifyEnable) {
            CommonDialog dialog = new CommonDialog.Builder(mContext)
                    .setTitle("当前网络环境不支持一键登录\n请开启移动数据")
                    .setConfirmButton(R.string.str_confirm).create();
            dialog.showWithOutTouchable(true);
            return;
        }
        showLoadingDialog(mContext);
        LoginSettings settings = new LoginSettings();
        settings.setAutoFinish(true);//设置登录完成后是否自动关闭授权页
        settings.setTimeout(15 * 1000);//设置超时时间，单位毫秒。 合法范围（0，30000],范围以外默认设置为10000
        settings.setAuthPageEventListener(new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
                LogCat.e(TAG, "cmd=" + cmd + "  msg=" + msg);
            }
        });
        JVerificationInterface.setCustomUIWithConfig(builder());
        JVerificationInterface.loginAuth(mContext, settings, (code, content, operator) -> {
            hideLoadingDialog();
            if (code == LOGIN_SUCCESS) {
                BaseNotification.newInstance().postNotificationName(CommonNotifications.JPushSendPhone, content);
            } else if (code == LOGIN_NETWORK_FAIL) {
                ToastUtils.toastForShort(mContext, "请开启手机移动数据");
            } else if (code == LOGIN_EXCEPTION) {
                ToastUtils.toastForShort(mContext, "手机号获取失败");
            }
        });
    }

//    /**
//     * 获取手机号
//     */
//    private void getJPushPhone(Context context, String loginToken) throws JSONException {
//        //Base64加密
//        String jpushSercet = String.format("%s:%s", AppConfig.JPHSH_KEY, AppConfig.JPHSH_SECRET);
//        BASE64Encoder base64Encoder = new BASE64Encoder();
//        String encoded = base64Encoder.encode(jpushSercet.getBytes());
//        String params = new JSONObject()
//                .put("loginToken", loginToken)
//                .put("exID", "123456")
//                .toString();
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody requestBody = RequestBody.create(JSON, params);//请求json内容
//        Request.Builder request = new Request.Builder()
//                .addHeader("Authorization", "Basic " + encoded)
//                .url("https://api.verification.jpush.cn/v1/web/loginTokenVerify")
//                .post(requestBody);
//        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
//        mBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        mBuilder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
//        OkHttpClient okHttpClient = mBuilder.build();
//        Call call = okHttpClient.newCall(request.build());
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                ToastUtils.toastForShort(context, R.string.str_server_exception);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                //注意此处必须new 一个string 不然的话直接调用response.body().string()会崩溃，因为此处流只调用一次然后关闭了
//                String result = response.body().string();
//                resultData(context, result);
//            }
//        });
//    }
//
//    private void resultData(Context context, String result) {
//        try {
//            if (TextUtils.isEmpty(result)) {
//                ToastUtils.toastForShort(context, "手机号获取失败");
//            } else {
//                JSONObject object = new JSONObject(result);
//                if (8000 == object.getInt("code")) {
//                    String phone = decrypt(object.getString("phone"), jpushPrikey(context));
//                    BaseNotification.newInstance().postNotificationName(CommonNotifications.JPushSendPhone, phone);
//                } else {
//                    ToastUtils.toastForShort(context, "手机号获取失败");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String decrypt(String cryptograph, String prikey) throws Exception {
//        BASE64Decoder base64Decoder = new BASE64Decoder();
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64Decoder.decodeBuffer(prikey));
//        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
//
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        cipher.init(Cipher.PRIVATE_KEY, privateKey);
//
//        byte[] b = base64Decoder.decodeBuffer(cryptograph);
//        return new String(cipher.doFinal(b));
//    }
//
//    private String jpushPrikey(Context context) {
//        StringBuilder stringbuilder = new StringBuilder();
//        try {
//            AssetManager assetmanager = context.getAssets();
//            BufferedReader bf = new BufferedReader(new InputStreamReader(
//                    assetmanager.open("jpush_prikey.txt")));
//            String line;
//            while ((line = bf.readLine()) != null) {
//                stringbuilder.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stringbuilder.toString();
//    }

    //自定义ui
    public JVerifyUIConfig builder() {
        JVerifyUIConfig uiConfig = new JVerifyUIConfig.Builder()
                .setLogoImgPath("jpush_app_logo")
                .setLogoWidth(80)
                .setLogoHeight(80)
                .setLogoHidden(false)
                .setNavTextSize(20)
                .setNavColor(0xff46C3C2)
                .setNavTextColor(0xffffffff)
                .setNumberColor(0xff46C3C2)
                .setNumberSize(24)
                .setLogBtnHeight(50)
                .setLogBtnTextSize(18)
                .setAppPrivacyColor(0xFFBBBCC5, 0XFF46C3C2)
                .setPrivacyText("登录即同意《", "", "", "》并授权OfficeGo平台")
                .setPrivacyCheckboxHidden(false)
                .setPrivacyTextCenterGravity(true)
                .setSloganTextSize(14) //移动，联通，电信
                .setUncheckedImgPath("umcsdk_uncheck_image2")
                .setCheckedImgPath("umcsdk_check_image2")
                .setPrivacyCheckboxSize(12)
                .setPrivacyTextSize(13)
                .setPrivacyNavColor(0XFF46C3C2)
                .build();
        return uiConfig;
    }

    /**
     * 显示加载框
     */
    public void showLoadingDialog(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setLoadingContent(null);
            loadingDialog.show();
        }
    }

    /**
     * 关闭加载框
     */
    public void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.setCancelable(true);
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
