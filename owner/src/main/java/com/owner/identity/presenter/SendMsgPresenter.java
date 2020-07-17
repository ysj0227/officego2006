package com.owner.identity.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.identity.contract.CompanyContract;
import com.owner.identity.contract.SendMsgContract;
import com.owner.identity.model.ApplyLicenceBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.rpc.OfficegoApi;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class SendMsgPresenter extends BasePresenter<SendMsgContract.View>
        implements SendMsgContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();


    @Override
    public void getDetails(int identityType, int id) {
        OfficegoApi.getInstance().selectApplyLicence(identityType, id, new RetrofitCallback<ApplyLicenceBean>() {
            @Override
            public void onSuccess(int code, String msg, ApplyLicenceBean data) {
                if (isViewAttached()) {
                    mView.messageSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, ApplyLicenceBean data) {
                LogCat.e(TAG, "searchCompany onFail code=" + code + "  msg=" + msg);
            }
        });
    }
}