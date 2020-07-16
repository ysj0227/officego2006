package com.owner.identity.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.identity.contract.PersonalContract;
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
}