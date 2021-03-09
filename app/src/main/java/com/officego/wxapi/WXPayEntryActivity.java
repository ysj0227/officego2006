package com.officego.wxapi;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.officego.R;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.HttpsUtils;
import com.officego.model.PayData;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private String outTradeNo, returnUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_result);
        Constants.WXapi.handleIntent(getIntent(), this);
        Intent intent = getIntent();
        String mData = intent.getStringExtra(Constants.WX_PAY);
        if (TextUtils.isEmpty(mData)) {
            Toast.makeText(this, R.string.pay_error, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        pay();
    }
    /**
     * 开启支付
     */
    private void pay() {
        Intent intent = getIntent();
        String mData = intent.getStringExtra(Constants.WX_PAY);
        PayData bean = new Gson().fromJson(mData, PayData.class);
        outTradeNo = bean.getOuttradeno();
        returnUrl = bean.getUrl();
        PayReq req = new PayReq();
        req.appId = Constants.APP_ID;
        req.partnerId = bean.getPartnerid();
        req.prepayId = bean.getPrepayid();
        req.nonceStr = bean.getNoncestr();
        req.timeStamp = bean.getTimestamp() + "";
        req.packageValue = bean.getPackageX();
        req.sign = bean.getSign();
        req.extData = "app_officego_pay"; // optional
        Constants.WXapi.sendReq(req);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Constants.WXapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode + " , transaction=" + resp.transaction);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = resp.errCode;
            String tip;
            // 0 则代表支付成功
            // -1为支付失败，包括用户主动取消支付，或者系统返回的错误
            // -2为取消支付，或者系统返回的错误
            // 其他为系统返回的错误
            if (errCode == 0) {
                queryTradeStatus();
                return;
            }
            if (errCode == -1) {
                tip = getString(R.string.pay_fail);
            } else if (errCode == -2) {
                tip = getString(R.string.pay_cancel);
            } else {
                tip = getString(R.string.pay_error);
            }
            result(tip);
        }
    }


    private void queryTradeStatus() {
//        JSONObject object = new JSONObject();
//        try {
//            object.put("out_trade_no", outTradeNo);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        //requestBody
//        MediaType json = MediaType.parse("application/json; charset=utf-8");
//        RequestBody requestBody = RequestBody.create(json, object.toString());
//        //requestBody
//        Request.Builder request = new Request.Builder().url(InitAppConfig.QUERY_TRADE_STATUS).post(requestBody);
//        //OkHttpClient
//        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
//        mBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        mBuilder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
//        OkHttpClient okHttpClient = mBuilder.build();
//        //call
//        Call call = okHttpClient.newCall(request.build());
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("TAG", "pay trade onFailure");
//                postJsPaySuccess(-1);
//                result(getString(R.string.pay_exception));
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                assert response.body() != null;
//                String res = response.body().string();
//                Log.d("TAG", "pay trade onResponse=" + res);
//                queryResult(res);
//            }
//        });
    }

    private void queryResult(String response) {
        try {
            JSONObject object = new JSONObject(response);
            if (!object.has("code")) {
                postJsPaySuccess(-1);
                result(getString(R.string.pay_error));
                return;
            }
            int code = object.getInt("code");
            String msg = object.getString("msg");
            postJsPaySuccess(code);
            if (code == 0) {
                result(getString(R.string.pay_success));
            } else {
                result(TextUtils.isEmpty(msg) ? getString(R.string.pay_exception) : msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //TODO 支付成功通知
    private void postJsPaySuccess(int code) {
      //  BaseNotification.newInstance().postNotificationName(CommonNotifications.weChatPayStatus, code, returnUrl);
    }

    /**
     * 支付结果dialog
     *
     * @param tip tip
     */
    private void result(String tip) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}