package com.officego.commonlib.retrofit;


import android.database.Observable;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.officego.commonlib.R;
import com.officego.commonlib.base.BaseApplication;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Description:
 * Created by bruce on 2019/2/27.
 */
public abstract class RxJavaCallback<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "RetrofitCallback";
    private String PACKAGE_NAME = "com.officego";

    @Override
    public void onNext(BaseResponse<T> response) {
        if (response.getStatus() == RpcErrorCode.HTTP_RESP_FORBID) {//请求被拒
            ToastUtils.toastForShort(BaseApplication.getContext(), R.string.tip_forbid_by_server);
            onFail(response.getStatus(), response.getMessage(), null);
        } else if (response.getStatus() == RpcErrorCode.HTTP_RESP_UNKNOWN_REQUEST) {
            onFail(response.getStatus(), response.getMessage(), null);
        } else {
            LogCat.e(TAG, "response.body=" + response.getData()+ ", response.errorBody=" + response.getMessage());
//            if (response.getData() != null) {
//                responseBodyHandle(response.body());
//            } else if (response.errorBody() != null) {
//                errorResponseHandle(response.errorBody());
//            }
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {

    }

//    @Override
//    public void onResponse(@NonNull Call<BaseResponse<T>> call,
//                           @NonNull Response<BaseResponse<T>> response) {
//        if (response.code() == RpcErrorCode.HTTP_RESP_FORBID) {//请求被拒
//            ToastUtils.toastForShort(BaseApplication.getContext(), R.string.tip_forbid_by_server);
//            onFail(response.code(), response.message(), null);
//        } else if (response.code() == RpcErrorCode.HTTP_RESP_UNKNOWN_REQUEST) {
//            onFail(response.code(), response.message(), null);
//        } else {
//            LogCat.e(TAG, "response.body=" + response.body() + ", response.errorBody=" + response.errorBody());
//            if (response.body() != null) {
//                responseBodyHandle(response.body());
//            } else if (response.errorBody() != null) {
//                errorResponseHandle(response.errorBody());
//            }
//        }
//    }

    // error
    private void errorResponseHandle(ResponseBody errorResponse) {//errorResponse
        try {
            BaseResponse errorResp = new Gson().fromJson(
                    errorResponse.string(), BaseResponse.class);
            if (errorResp != null) {
                onFail(errorResp.getStatus(), errorResp.getMessage(), null);
            } else {
                onFail(0, errorResponse.string(), null);
            }
        } catch (Exception e) {
            onFail(0, "", null);
            e.printStackTrace();
        }
    }

    // success
    private void responseBodyHandle(BaseResponse<T> response) {
        if (response.getStatus() == RpcErrorCode.HTTP_RESP_OK) {
            onSuccess(response.getStatus(), response.getMessage(), response.getData());
            LogCat.e(TAG, "success: " + response.getStatus() + response.getMessage() + " data:" + response.getData());
        } else {
            onFail(response.getStatus(), response.getMessage(), response.getData());
            LogCat.e(TAG, "error: " + response.getStatus() + response.getMessage() + " data:" + response.getData());
//            if (response.getStatus() == RpcErrorCode.ERROR_CODE_5001) {
//                //未登录发送广播
//                Intent intent = new Intent();
//                intent.setAction(PACKAGE_NAME);
//                BaseApplication.getContext().sendBroadcast(intent);
//            } else
                if (response.getStatus() == Constants.ERROR_CODE_5009) {
                //当前身份发送变化，重新登录
                BaseNotification.newInstance().postNotificationName(
                        CommonNotifications.identityChangeToRelogin, response.getMessage());
            }
        }
    }

//    @Override
//    public void onFailure(@NonNull Call<BaseResponse<T>> call, @NonNull Throwable t) {
//        int errCode = RpcErrorCode.RPC_COMMON_ERROR;
//        if (t instanceof UnknownHostException || t instanceof TimeoutException) {
//            errCode = RpcErrorCode.RPC_ERR_TIMEOUT;
//        }
//        onFail(errCode, t.getMessage(), null);
//    }

    public abstract void onSuccess(int code, String msg, T data);

    public abstract void onFail(int code, String msg, T data);

}
