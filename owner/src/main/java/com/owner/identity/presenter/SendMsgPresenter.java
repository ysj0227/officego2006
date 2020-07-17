package com.owner.identity.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.identity.contract.CompanyContract;
import com.owner.identity.contract.SendMsgContract;
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
    public void getDetails(int id) {
//        OfficegoApi.getInstance().searchCompany(keyword, new RetrofitCallback<List<IdentityCompanyBean.DataBean>>() {
//            @Override
//            public void onSuccess(int code, String msg, List<IdentityCompanyBean.DataBean> data) {
//                if (isViewAttached()) {
//                    mView.messageSuccess(data);
//                }
//            }
//
//            @Override
//            public void onFail(int code, String msg, List<IdentityCompanyBean.DataBean> data) {
//                LogCat.e(TAG, "searchCompany onFail code=" + code + "  msg=" + msg);
//            }
//        });
    }
}