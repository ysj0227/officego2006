package com.officego.ui.login.presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.officego.commonlib.common.analytics.GoogleTrack;
import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.JPushLoginBean;
import com.officego.commonlib.common.rongcloud.ConnectRongCloudUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.login.contract.LoginContract;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    /**
     * 获取验证码
     */
    @Override
    public void sendSmsCode(String mobile) {
        OfficegoApi.getInstance().getSmsCode(mobile, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
            }

            @Override
            public void onFail(int code, String msg, Object data) {
            }
        });
    }

    @Override
    public void login(String mobile, String code) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().login(context, mobile, code, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                if (isViewAttached()) {
                    BaseNotification.newInstance().postNotificationName(
                            CommonNotifications.loginIn, "loginIn");
                    SpUtils.saveLoginInfo(data, mobile);
                    new ConnectRongCloudUtils();
                    mView.hideLoadingDialog();
                    mView.loginSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                if (isViewAttached()) {
                    mView.loginFail(code, msg);
                    mView.hideLoadingDialog();
                }
            }
        });
    }

    /**
     * 手机免密登录
     *
     * @param mobile mobile
     */
    @Override
    public void loginOnlyPhone(String mobile) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().loginOnlyPhone(context, mobile, new RetrofitCallback<LoginBean>() {
            @Override
            public void onSuccess(int code, String msg, LoginBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    BaseNotification.newInstance().postNotificationName(
                            CommonNotifications.loginIn, "loginIn");
                    SpUtils.saveLoginInfo(data, mobile);
                    new ConnectRongCloudUtils();
                    mView.loginSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, LoginBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.loginFail(code, msg);
                }
            }
        });
    }

    /**
     * 一键登录 获取手机号
     */
    @Override
    public void getJPushPhone(String loginToken) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().jPushLogin(
                loginToken, new RetrofitCallback<JPushLoginBean>() {
            @Override
            public void onSuccess(int code, String msg, JPushLoginBean data) {
                if (isViewAttached()) {
                    loginOnlyPhone(data.getPhone());//免密登录
                }
            }

            @Override
            public void onFail(int code, String msg, JPushLoginBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

//    /**
//     * 一键登录 获取手机号
//     */
//    @Override
//    public void getJPushPhone(String loginToken) {
//        String jpushSercet = String.format("%s:%s", AppConfig.JPHSH_KEY, AppConfig.JPHSH_SECRET);
//        BASE64Encoder base64Encoder = new BASE64Encoder();
//        String encoded = base64Encoder.encode(jpushSercet.getBytes());
//        String params = null;
//        try {
//            params = new JSONObject()
//                    .put("loginToken", loginToken)
//                    .put("exID", "123456")
//                    .toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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

//    private void resultData(Context context, String result) {
//        try {
//            if (TextUtils.isEmpty(result)) {
//                ToastUtils.toastForShort(context, "手机号获取失败");
//            } else {
//                JSONObject object = new JSONObject(result);
//                if (8000 == object.getInt("code")) {
//                    String phone = decrypt(object.getString("phone"), jpushPrikey(context));
//                    loginOnlyPhone(phone);//免密登录
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

}
