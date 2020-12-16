package com.officego.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.officego.R;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.ssl.HttpsUtils;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.LoginSettings;
import cn.jiguang.verifysdk.api.VerifyListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by shijie
 * Date 2020/12/15
 * 极光授权登录
 **/
public class JPushAuthLoginDialogRequest {

    public static final String TAG = "JPushAuthLoginRequest";

    public static JPushAuthLoginDialogRequest getInstance() {
        return Singleton.INSTANCE;
    }

    private static final class Singleton {
        private static final JPushAuthLoginDialogRequest INSTANCE = new JPushAuthLoginDialogRequest();
    }

    //sdk集成页面
    public void authLogin(Context mContext) {
        LoginSettings settings = new LoginSettings();
        settings.setAutoFinish(true);//设置登录完成后是否自动关闭授权页
        settings.setTimeout(15 * 1000);//设置超时时间，单位毫秒。 合法范围（0，30000],范围以外默认设置为10000
        settings.setAuthPageEventListener(new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
                LogCat.e(TAG, "cmd=" + cmd + "  msg=" + msg);
            }
        });
//        JVerificationInterface.setCustomUIWithConfig(builder());
//        JVerificationInterface.loginAuth(mContext, settings, (code, content, operator) -> {
//            try {
//                if (code == 6000) {
//                    getJPushPhone(mContext, content);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        });

        JVerificationInterface.setCustomUIWithConfig(getDialogPortraitConfig(mContext));
        JVerificationInterface.loginAuth(mContext, new VerifyListener() {
            @Override
            public void onResult(final int code, final String token, String operator) {
                if (code == 6000) {
                    LogCat.e(TAG, "onResult: loginSuccess");
                } else {
                    LogCat.e(TAG, "onResult: loginError");
                }
            }
        });
    }

    /**
     * 获取手机号
     */
    private void getJPushPhone(Context context, String loginToken) throws JSONException {
        //Base64加密
        String jpushSercet = String.format("%s:%s", AppConfig.JPHSH_KEY, AppConfig.JPHSH_SECRET);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String encoded = base64Encoder.encode(jpushSercet.getBytes());
        String params = new JSONObject()
                .put("loginToken", loginToken)
                .put("exID", "123456")
                .toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, params);//请求json内容
        Request.Builder request = new Request.Builder()
                .addHeader("Authorization", "Basic " + encoded)
                .url("https://api.verification.jpush.cn/v1/web/loginTokenVerify")
                .post(requestBody);
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        mBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        mBuilder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        OkHttpClient okHttpClient = mBuilder.build();
        Call call = okHttpClient.newCall(request.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.toastForShort(context, R.string.str_server_exception);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //注意此处必须new 一个string 不然的话直接调用response.body().string()会崩溃，因为此处流只调用一次然后关闭了
                String result = response.body().string();
                //LogCat.e(TAG, "result=" + result);
                resultData(context, result);
            }
        });
    }

    private void resultData(Context context, String result) {
        try {
            if (TextUtils.isEmpty(result)) {
                ToastUtils.toastForShort(context, "手机号获取失败");
            } else {
                JSONObject object = new JSONObject(result);
                if (8000 == object.getInt("code")) {
                    String phone = decrypt(object.getString("phone"), jpushPrikey(context));
                    BaseNotification.newInstance().postNotificationName(CommonNotifications.JPushSendPhone, phone.substring(phone.length() - 11));
                } else {
                    ToastUtils.toastForShort(context, "手机号获取失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String decrypt(String cryptograph, String prikey) throws Exception {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64Decoder.decodeBuffer(prikey));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PRIVATE_KEY, privateKey);

        byte[] b = base64Decoder.decodeBuffer(cryptograph);
        return new String(cipher.doFinal(b));
    }

    private String jpushPrikey(Context context) {
        StringBuilder stringbuilder = new StringBuilder();
        try {
            AssetManager assetmanager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetmanager.open("jpush_prikey.txt")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringbuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringbuilder.toString();
    }


    private JVerifyUIConfig getDialogPortraitConfig(Context context) {
        int widthDp = CommonHelper.px2dp(context, 660);
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder().setDialogTheme(widthDp - 60, 300, 0, 0, false);
//        uiConfigBuilder.setLogoHeight(30);
//        uiConfigBuilder.setLogoWidth(30);
//        uiConfigBuilder.setLogoOffsetY(-15);
//        uiConfigBuilder.setLogoOffsetX((widthDp-40)/2-15-20);
//        uiConfigBuilder.setLogoImgPath("logo_login_land");
        uiConfigBuilder.setLogoHidden(true);

        uiConfigBuilder.setNumFieldOffsetY(104).setNumberColor(Color.BLACK);
        uiConfigBuilder.setSloganOffsetY(135);
        uiConfigBuilder.setSloganTextColor(0xFFD0D0D9);
        uiConfigBuilder.setLogBtnOffsetY(161);

        uiConfigBuilder.setPrivacyOffsetY(15);
//        uiConfigBuilder.setCheckedImgPath("cb_chosen");
//        uiConfigBuilder.setUncheckedImgPath("cb_unchosen");
        uiConfigBuilder.setNumberColor(0xFF222328);
        uiConfigBuilder.setNumberSize(20);
//        uiConfigBuilder.setLogBtnImgPath("selector_btn_normal");
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnHeight(44);
        uiConfigBuilder.setLogBtnWidth(250);
        uiConfigBuilder.setAppPrivacyColor(0xFFBBBCC5, 0xFF8998FF);
        uiConfigBuilder.setPrivacyText("登录即同意《", "", "", "》并授权OfficeGo获取本机号码");
        uiConfigBuilder.setPrivacyCheckboxHidden(true);
        uiConfigBuilder.setPrivacyTextCenterGravity(true);
//        uiConfigBuilder.setPrivacyOffsetX(52-15);
        uiConfigBuilder.setPrivacyTextSize(10);

        // 图标和标题
        LinearLayout layoutTitle = new LinearLayout(context);
        RelativeLayout.LayoutParams layoutTitleParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTitleParam.setMargins(0, CommonHelper.dp2px(context, 50), 0, 0);
        layoutTitleParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutTitleParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutTitleParam.setLayoutDirection(LinearLayout.HORIZONTAL);
        layoutTitle.setLayoutParams(layoutTitleParam);

        ImageView img = new ImageView(context);
        img.setImageResource(R.mipmap.ic_logo);
        TextView tvTitle = new TextView(context);
//        tvTitle.setText("极光认证");
//        tvTitle.setTextSize(19);
//        tvTitle.setTextColor(Color.BLACK);
//        tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParam.setMargins(0, 0, CommonHelper.dp2px(context, 6), 0);
        LinearLayout.LayoutParams titleParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParam.setMargins(0, 0, CommonHelper.dp2px(context, 4), 0);
        layoutTitle.addView(img, imgParam);
        layoutTitle.addView(tvTitle, titleParam);
        uiConfigBuilder.addCustomView(layoutTitle, false, null);

        // 关闭按钮
        ImageView closeButton = new ImageView(context);

        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.setMargins(0, CommonHelper.dp2px(context, 10.0f), CommonHelper.dp2px(context, 10), 0);
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        closeButton.setLayoutParams(mLayoutParams1);
        closeButton.setImageResource(R.mipmap.ic_delete);
        uiConfigBuilder.addCustomView(closeButton, true, null);

        return uiConfigBuilder.build();
    }

}
