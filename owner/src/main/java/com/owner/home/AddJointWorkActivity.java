package com.owner.home;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.UniqueAdapter;
import com.owner.adapter.JointCompanyAdapter;
import com.owner.dialog.ConditionedDialog;
import com.owner.dialog.FloorTypeDialog;
import com.owner.dialog.ServiceSelectedDialog;
import com.owner.home.contract.JointWorkContract;
import com.owner.home.presenter.JointWorkPresenter;
import com.owner.identity.dialog.AreaDialog;
import com.owner.utils.SpaceItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_jointwork_manager")
public class AddJointWorkActivity extends BaseMvpActivity<JointWorkPresenter>
        implements JointWorkContract.View, FloorTypeDialog.FloorListener,
        AreaDialog.AreaSureListener, ConditionedDialog.ConditionedListener {
    @ViewById(resName = "iv_mark_image_lift")
    ImageView ivMarkImageLift;
    @ViewById(resName = "rv_house_characteristic")
    RecyclerView rvHouseUnique;
    @ViewById(resName = "rv_create_service")
    RecyclerView rvCreateService;
    @ViewById(resName = "rv_base_service")
    RecyclerView rvBaseService;
    @ViewById(resName = "rv_join_company")
    RecyclerView rvJoinCompany;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "sil_floor_no")
    SettingItemLayout silFloorNo;
    @ViewById(resName = "sil_conditioned")
    SettingItemLayout silConditioned;
    @ViewById(resName = "sil_conditioned_fee")
    SettingItemLayout silConditionedFee;
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;
    //区域
    private int district, business;
    //加入企业
    private JointCompanyAdapter adapter;
    private List<String> jointCompanyList = new ArrayList<String>();
    //服务
    private List<DirectoryBean.DataBean> serviceList = new ArrayList<>();

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new JointWorkPresenter();
        mPresenter.attachView(this);
        initViews();
        mPresenter.getHouseUnique();
    }

    private void initViews() {
        ivMarkImageLift.setVisibility(View.INVISIBLE);
        //入住企业
        rvJoinCompany.setLayoutManager(new LinearLayoutManager(context));
        jointCompanyList.add(0, "");
        adapter = new JointCompanyAdapter(this, jointCompanyList);
        rvJoinCompany.setAdapter(adapter);
        //特色
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        rvHouseUnique.setLayoutManager(layoutManager);
        rvHouseUnique.addItemDecoration(new SpaceItemDecoration(context, 3));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
        UploadVideoVrActivity_.intent(context).start();
    }

    @Click(resName = "iv_close_scan")
    void closeScanOnClick() {
        btnScan.setVisibility(View.GONE);
        ivCloseScan.setVisibility(View.GONE);
    }

    @Click(resName = "sil_floor_no")
    void floorNoOnClick() {
        new FloorTypeDialog(context).setListener(this);
    }

    @Click(resName = "sil_area")
    void areaOnClick() {
        new AreaDialog(context, district, business).setListener(this);
    }

    //service logo dialog
    @Click(resName = "iv_arrow_create")
    void serviceCompanyClick() {
        mPresenter.getCompanyService();
    }

    @Click(resName = "iv_arrow_base")
    void serviceBaseClick() {
        mPresenter.getBaseService();
    }

    @Click(resName = "sil_conditioned")
    void conditionedOnClick() {
        new ConditionedDialog(context).setListener(this);
    }

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        rvHouseUnique.setAdapter(new UniqueAdapter(context,new HashMap<>(), data));
    }

    @Override
    public void baseServiceSuccess(List<DirectoryBean.DataBean> data) {
        new ServiceSelectedDialog(context, getString(R.string.str_title_base_sservice), data);
    }

    @Override
    public void companyServiceSuccess(List<DirectoryBean.DataBean> data) {
        new ServiceSelectedDialog(context, getString(R.string.str_title_create_service), data);
    }

    @Override
    public void sureFloor(String text) {
        silFloorNo.setLeftToArrowText(text);
    }

    @Override
    public void AreaSure(String area, int district, int business) {
        silArea.setCenterText(area);
    }

    @Override
    public void sureConditioned(String string, int flag) {
        silConditioned.setCenterText(string);
        silConditionedFee.setVisibility(View.VISIBLE);
        if (flag == 0) {
            silConditionedFee.setCenterText("包含在物业费内，加时另计");
        } else if (flag == 1) {
            silConditionedFee.setCenterText("按电表计费");
        } else {
            silConditionedFee.setCenterText("无");
        }
    }
}
