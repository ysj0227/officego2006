package com.owner.home;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.dialog.YearDateDialog;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.adapter.HouseUniqueAdapter;
import com.owner.dialog.ServiceSelectedDialog;
import com.owner.home.contract.HouseContract;
import com.owner.home.presenter.HousePresenter;
import com.owner.identity.dialog.AreaDialog;
import com.owner.utils.SpaceItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_house_manager")
public class HouseAddActivity extends BaseMvpActivity<HousePresenter>
        implements HouseContract.View, YearDateDialog.SureClickListener,
        AreaDialog.AreaSureListener{
    @ViewById(resName = "sil_complete_time")
    SettingItemLayout silCompleteTime;
    @ViewById(resName = "sil_recomplete_time")
    SettingItemLayout silReCompleteTime;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;

    @ViewById(resName = "rv_house_characteristic")
    RecyclerView rvHouseUnique;

    private boolean isCompleteTime;//是否竣工时间
    private int district, business;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new HousePresenter();
        mPresenter.attachView(this);
        initViews();
        mPresenter.getHouseUnique();
    }

    private void initViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        rvHouseUnique.setLayoutManager(layoutManager);
        rvHouseUnique.addItemDecoration(new SpaceItemDecoration(context, 3));
    }

    @Click(resName = "sil_area")
    void areaOnClick() {
        new AreaDialog(context, district, business).setListener(this);
    }

    @Click(resName = "btn_company_service")
    void companyServiceOnClick() {
        mPresenter.getCompanyServices();
    }

    @Click(resName = "btn_base_service")
    void baseServiceOnClick() {
        mPresenter.getBaseServices();
    }

    @Click(resName = "sil_complete_time")
    void completeTimeOnClick() {
        isCompleteTime = true;
        YearDateDialog dateDialog = new YearDateDialog(context);
        dateDialog.setSureListener(this);
    }

    @Click(resName = "sil_recomplete_time")
    void reCompleteTimeOnClick() {
        isCompleteTime = false;
        YearDateDialog dateDialog = new YearDateDialog(context);
        dateDialog.setSureListener(this);
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
        rvHouseUnique.setAdapter(new HouseUniqueAdapter(context, data));
    }

    @Override
    public void decoratedTypeSuccess(List<DirectoryBean.DataBean> data) {

    }

    @Override
    public void selectedDate(String date) {
        if (isCompleteTime) {
            silCompleteTime.setCenterText(date);
        } else {
            silReCompleteTime.setCenterText(date);
        }
    }

    @Override
    public void AreaSure(String area, int district, int business) {
        silArea.setCenterText(area);
    }
}
