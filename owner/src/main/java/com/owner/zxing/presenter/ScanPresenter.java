package com.owner.zxing.presenter;

import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.rpc.OfficegoApi;
import com.owner.zxing.contract.ScanContract;

public class ScanPresenter extends BasePresenter<ScanContract.View>
        implements ScanContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void scanLogin(String scanContent) {
        if (!TextUtils.isEmpty(scanContent)) {
            mView.showLoadingDialog();
            OfficegoApi.getInstance().scanWebLogin(scanContent, new RetrofitCallback<Object>() {
                @Override
                public void onSuccess(int code, String msg, Object data) {
                    if (isViewAttached()) {
                        mView.hideLoadingDialog();
                        mView.scanSuccess();
                    }
                }

                @Override
                public void onFail(int code, String msg, Object data) {
                    if (isViewAttached()) {
                        mView.hideLoadingDialog();
                        if (code == Constants.DEFAULT_ERROR_CODE) {
                            mView.shortTip(msg);
                        }
                    }
                }
            });
        }
    }
}
