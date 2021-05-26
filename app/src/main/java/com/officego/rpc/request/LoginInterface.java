package com.officego.rpc.request;

import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.LoginBean;
import com.officego.commonlib.retrofit.BaseResponse;
import com.officego.test.RxLoginBean;
import com.officego.test.RxUnitBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by YangShiJie
 * Data 2020/5/8.
 * Descriptions:
 **/
public interface LoginInterface {

    String path = "api/";

    /**
     * 获取验证码
     * 表单形式
     *
     * @return
     */
    @Multipart
    @POST(path + "login/sms_code")
    Call<BaseResponse<Object>> getSmsCode(@PartMap Map<String, RequestBody> params);

    /**
     * @param params
     * @return 登录
     */
    @Multipart
    @POST(path + "login/loginCode")
    Call<BaseResponse<LoginBean>> login(@PartMap Map<String, RequestBody> params);

    /**
     * 手机免密登录
     *
     * @param params
     * @return
     */
    @Multipart
    @POST(path + "login/loginByPhone")
    Call<BaseResponse<LoginBean>> loginOnlyPhone(@PartMap Map<String, RequestBody> params);


    //RXJAVA
    @Multipart
    @POST(path + "login/loginCode")
    Observable<BaseResponse<LoginBean>> rxLogin(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST(path + "dictionary/getDictionary")
    Observable<BaseResponse<List<DirectoryBean.DataBean>>> rxHouseUnique(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST(path + "login/loginCode")
    Observable<RxLoginBean> rxLoginString(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST(path + "dictionary/getDictionary")
    Observable<RxUnitBean> rxHouseUniqueString(@PartMap Map<String, RequestBody> params);

}
