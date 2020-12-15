package com.officego.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.officego.R;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.ssl.HttpsUtils;
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
public class JPushAuthLoginRequest {

    public static final String TAG = "JPushAuthLoginRequest";

    public static JPushAuthLoginRequest getInstance() {
        return Singleton.INSTANCE;
    }

    private static final class Singleton {
        private static final JPushAuthLoginRequest INSTANCE = new JPushAuthLoginRequest();
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
        JVerificationInterface.setCustomUIWithConfig(builder());
        JVerificationInterface.loginAuth(mContext, settings, (code, content, operator) -> {
            try {
                if (code == 6000) {
                    getJPushPhone(mContext, content);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
                    BaseNotification.newInstance().postNotificationName(CommonNotifications.JPushSendPhone, phone.substring(phone.length()-11));
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

    //自定义ui
    public JVerifyUIConfig builder() {
        JVerifyUIConfig uiConfig = new JVerifyUIConfig.Builder()
//                .setAuthBGImgPath()
//                .setNavText("登录")
//                .setNavReturnImgPath("")
                .setNavColor(0xff46C3C2)
                .setNavTextColor(0xffffffff)
                .setLogoWidth(80)
                .setLogoHeight(80)
                .setLogoHidden(false)
                .setNumberColor(0xff46C3C2)
                .setNumberSize(20)
                .setLogBtnHeight(50)
                .setLogBtnTextSize(18)
//                .setLogBtnText("本机号码一键登录")
//                .setLogBtnTextColor(0x00000000)
//                .setLogBtnImgPath("")
//                .setAppPrivacyOne("服务", "sssss")
//                .setAppPrivacyTwo()
//                .setAppPrivacyColor(0xff000000, 0xff000000)
//                .setUncheckedImgPath("ic_circle_uncheck")
//                .setCheckedImgPath("ic_circle_check")
//                .setPrivacyTextSize(12)
//                .setSloganTextColor(0xff999999) //移动，联通，电信
//                .setLogoOffsetY(50)
//                .setLogoImgPath("")
//                .setNumFieldOffsetY(190)
//                .setSloganOffsetY(220)
//                .setLogBtnOffsetY(254)
//                .setPrivacyOffsetY(35)
                .build();
        return uiConfig;
    }
}
