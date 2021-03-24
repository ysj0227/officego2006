package com.officego.commonlib.common.rpc.request;

import com.officego.commonlib.common.model.AlipayBean;
import com.officego.commonlib.common.model.PayData;
import com.officego.commonlib.retrofit.BaseResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by shijie
 * Date 2021/3/15
 **/
public interface PayInterface {

    String path = "api/";

    /**
     * 微信支付
     */
    @Multipart
    @POST(path + "pay/wxpay")
    Call<BaseResponse<PayData>> wxPay(@PartMap Map<String, RequestBody> params);

    /**
     * 支付宝支付
     */
    @Multipart
    @POST(path + "pay/alipayapp")
    Call<BaseResponse<Object>> alipay(@PartMap Map<String, RequestBody> params);

}
