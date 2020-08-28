package com.owner.zxing.presenter;

import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.rpc.OfficegoApi;
import com.owner.zxing.contract.ScanContract;
import com.owner.zxing.model.ScanBean;

public class ScanPresenter extends BasePresenter<ScanContract.View>
        implements ScanContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void scanLogin(String scanContent) {
        if (!TextUtils.isEmpty(scanContent)) {
            mView.showLoadingDialog();
            OfficegoApi.getInstance().scanWebLogin(scanContent, new RetrofitCallback<ScanBean>() {
                @Override
                public void onSuccess(int code, String msg, ScanBean data) {
                    if (isViewAttached()) {
                        mView.hideLoadingDialog();
                        mView.scanSuccess();
                    }
                }

                @Override
                public void onFail(int code, String msg, ScanBean data) {
                    if (isViewAttached()) {
                        mView.hideLoadingDialog();
                    }
                }
            });
        }
    }
}
