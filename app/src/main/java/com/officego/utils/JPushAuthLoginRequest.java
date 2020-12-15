package com.officego.utils;

import android.content.Context;
import android.text.TextUtils;

import com.officego.R;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.ssl.HttpsUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.LoginSettings;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
//        showLoadingDialog();
        LoginSettings settings = new LoginSettings();
        settings.setAutoFinish(true);//设置登录完成后是否自动关闭授权页
        settings.setTimeout(15 * 1000);//设置超时时间，单位毫秒。 合法范围（0，30000],范围以外默认设置为10000
        settings.setAuthPageEventListener(new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
                LogCat.e(TAG, "cmd=" + cmd + "  msg=" + msg);
                //do something...
            }
        });//设置授权页事件监听
        JVerificationInterface.loginAuth(mContext, settings, (code, content, operator) -> {
//            hideLoadingDialog();
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
        String encoded = EncryptBase64Utils.encode(jpushSercet);
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
                LogCat.e(TAG, "result=" + result);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        String phone=decrypt(secretPhone(result),jpushPrikey());
                        ToastUtils.toastForShort(context, phone);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String secretPhone(String result) throws JSONException {
        JSONObject object = new JSONObject(result);
        if (8000 == object.getInt("code")) {
            return object.getString("phone");
        }
        return "";
    }

//    public static String decrypt(String cryptograph, String prikey) throws Exception {
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey));
//        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
//
//        Cipher cipher=Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//
//        byte [] b = Base64.getDecoder().decode(cryptograph);
//        return new String(cipher.doFinal(b));
//    }

    private String decrypt(String cryptograph, String prikey) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(EncryptBase64Utils.decode(prikey).getBytes());
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] b = EncryptBase64Utils.decode(cryptograph).getBytes();
        return new String(cipher.doFinal(b));
    }

    private String jpushPrikey() {
        return "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL5yhQH2NlwIjKPI" +
                "XBKy5luMzoouxR2jQmQiDGBVqx8UfKJsS1yRzZ0V+DKTaLbzg3nZ2OV2lhm/PI8f" +
                "qeJ2va/du1+E3FuL4enPKU46UuDYPOSZ4+Yv7PaVtv0/py+ZfanIdf3oepsyLhLO" +
                "5+k8A51sUag/QKCGbZigRsCoLOCdAgMBAAECgYEAh9R51NpcDuvyEuZV9Ogvr+AP" +
                "dzLAV9EXCv/Vv+eZ8sLT2axPW6iJ8521tay5JLMtdHzRW2dmytpAQPweWGzEAC0s" +
                "Ut0S+pb6FOmo9hq6195W71DL6Vfn3AcHT3pwhEyyq/tRM5wRbNtpYk0R6NR/ZWbv" +
                "XiaQh7Z271TNzglikakCQQDqKXOuDq9u+n0YJXHTFf9xATE8NJxpfcwbZvpmhK+5" +
                "efggI07DVsYPupgonjYr+ItIGPOnXVrtUSz+u3JMC2ETAkEA0DVlDnihF/tqdbhy" +
                "GU7DwYVBxu67RJT6h0N0kkOZAdsu7LNjw/EUKzZ34wDbi6TiSGL1hDun995Kujd7" +
                "wbOdjwJAQkSW41Rum5ayHkpCiz13fBCdUW+GMGiUgdDD3o2AeZol+VpkqO1+webJ" +
                "tv0HUaxWJoJBOkU2PWEyvahosVKrFwJATW3N6xfJMNX6vVSeefLksa8Qyx403khY" +
                "8ryPOWWSHTKZvMTpsRCSY4+Z/f1vp/rle+3xR/+3c2Jjf+TqTRqy2QJAAzrFGWIY" +
                "U8ke/Td9cnixsZOchRW6E8RYcSqfxk7EZKKxP7Snixw7A1qi7Vb2uA4EjlRqEoWK" +
                "SHClAh9Wa7LSHA==";
    }
}
