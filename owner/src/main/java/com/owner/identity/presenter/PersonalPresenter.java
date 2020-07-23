package com.owner.identity.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.identity.contract.PersonalContract;
import com.owner.identity.model.CheckIdentityBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.rpc.OfficegoApi;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getBuilding(String keyword) {
        OfficegoApi.getInstance().searchListBuild(keyword, new RetrofitCallback<List<IdentityBuildingBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<IdentityBuildingBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.searchBuildingSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<IdentityBuildingBean.DataBean> data) {
                LogCat.e(TAG, "searchListBuild onFail code=" + code + "  msg=" + msg);
            }
        });
    }

    @Override
    public void checkBuilding(int identityType, String name) {
        OfficegoApi.getInstance().checkBuildingByName(identityType, name, new RetrofitCallback<CheckIdentityBean>() {
            @Override
            public void onSuccess(int code, String msg, CheckIdentityBean data) {
                if (isViewAttached()) {
                    //0不存在1存在
                    if (data.getFlag() == 1) {
                        mView.shortTip(data.getExplain());
                    } else {
                        mView.checkBuildingInfoSuccess();
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, CheckIdentityBean data) {
                if (isViewAttached()) {
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }
}