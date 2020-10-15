package com.owner.home;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.StatusBarUtils;
import com.owner.dialog.ServiceSelectedDialog;
import com.owner.home.contract.HouseContract;
import com.owner.home.presenter.HousePresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_house_manager")
public class HouseAddActivity extends BaseMvpActivity<HousePresenter>
        implements HouseContract.View {

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new HousePresenter();
        mPresenter.attachView(this);
    }

    @Click(resName = "btn_company_service")
    void companyServiceOnClick() {
        mPresenter.getCompanyServices();
    }

    @Click(resName = "btn_base_service")
    void baseServiceOnClick() {
        mPresenter.getBaseServices();
    }

    @Override
    public void baseServices(List<DirectoryBean.DataBean> data) {
        new ServiceSelectedDialog(context, "基础服务", data);
    }

    @Override
    public void companyServices(List<DirectoryBean.DataBean> data) {
        new ServiceSelectedDialog(context, "企业服务", data);
    }

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {

    }

    @Override
    public void decoratedTypeSuccess(List<DirectoryBean.DataBean> data) {

    }
}
