package com.owner.home;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.dialog.YearDateDialog;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.HouseUniqueAdapter;
import com.owner.adapter.JointCompanyAdapter;
import com.owner.adapter.UploadBuildingImageAdapter;
import com.owner.dialog.BuildingTypeDialog;
import com.owner.dialog.ConditionedDialog;
import com.owner.home.contract.BuildingContract;
import com.owner.home.presenter.BuildingPresenter;
import com.owner.identity.dialog.AreaDialog;
import com.owner.identity.model.ImageBean;
import com.owner.utils.SpaceItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_building_manager")
public class AddBuildingActivity extends BaseMvpActivity<BuildingPresenter>
        implements BuildingContract.View, YearDateDialog.SureClickListener,
        AreaDialog.AreaSureListener, UploadBuildingImageAdapter.UploadImageListener,
        BuildingTypeDialog.BuildingTypeListener, ConditionedDialog.ConditionedListener {
    @ViewById(resName = "sil_building_type")
    SettingItemLayout silBuildingType;
    @ViewById(resName = "sil_garden_name")
    SettingItemLayout silGardenName;
    @ViewById(resName = "sil_no")
    SettingItemLayout silNo;
    @ViewById(resName = "sil_complete_time")
    SettingItemLayout silCompleteTime;
    @ViewById(resName = "sil_recomplete_time")
    SettingItemLayout silReCompleteTime;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "sil_conditioned")
    SettingItemLayout silConditioned;
    @ViewById(resName = "sil_conditioned_fee")
    SettingItemLayout silConditionedFee;
    @ViewById(resName = "rv_house_characteristic")
    RecyclerView rvHouseUnique;
    @ViewById(resName = "rv_join_company")
    RecyclerView rvJoinCompany;
    //图片list
    @ViewById(resName = "rv_upload_image")
    RecyclerView rvUploadImage;
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;

    private boolean isCompleteTime;//是否竣工时间
    private int district, business;//区域

    //加入企业
    private JointCompanyAdapter adapter;
    private List<String> jointCompanyList = new ArrayList<String>();
    //上传图片
    private List<ImageBean> listUploadImage = new ArrayList<>();
    private UploadBuildingImageAdapter imageAdapter;
    private String localImagePath;
    private Uri localPhotoUri;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new BuildingPresenter();
        mPresenter.attachView(this);
        initViews();
        mPresenter.getHouseUnique();
    }

    private void initViews() {
        //特色
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        rvHouseUnique.setLayoutManager(layoutManager);
        rvHouseUnique.addItemDecoration(new SpaceItemDecoration(context, 3));
        //入住企业
        rvJoinCompany.setLayoutManager(new LinearLayoutManager(context));
        jointCompanyList.add(0, "");
        adapter = new JointCompanyAdapter(this, jointCompanyList);
        rvJoinCompany.setAdapter(adapter);
        //上传图片
        GridLayoutManager layoutManager2 = new GridLayoutManager(context, 3);
        layoutManager2.setSmoothScrollbarEnabled(true);
        layoutManager2.setAutoMeasureEnabled(true);
        rvUploadImage.setLayoutManager(layoutManager2);
        rvUploadImage.setNestedScrollingEnabled(false);
        initData();
    }

    private void initData() {
        //初始化图片默认添加一个
        listUploadImage.add(new ImageBean(false, 0, ""));
        imageAdapter = new UploadBuildingImageAdapter(context, listUploadImage);
        imageAdapter.setListener(this);
        rvUploadImage.setAdapter(imageAdapter);
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

    @Click(resName = "sil_building_type")
    void buildingTypeOnClick() {
        new BuildingTypeDialog(context).setListener(this);
    }

    @Click(resName = "sil_conditioned")
    void conditionedOnClick() {
        new ConditionedDialog(context).setListener(this);
    }

    @Click(resName = "sil_area")
    void areaOnClick() {
        new AreaDialog(context, district, business).setListener(this);
    }

    @Click(resName = "sil_complete_time")
    void completeTimeOnClick() {
        isCompleteTime = true;
        YearDateDialog dateDialog = new YearDateDialog(context, getString(R.string.str_text_year));
        dateDialog.setSureListener(this);
    }

    @Click(resName = "sil_recomplete_time")
    void reCompleteTimeOnClick() {
        isCompleteTime = false;
        YearDateDialog dateDialog = new YearDateDialog(context, getString(R.string.str_text_year));
        dateDialog.setSureListener(this);
    }

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        rvHouseUnique.setAdapter(new HouseUniqueAdapter(context, data));
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
    public void sureBuildingType(String type, boolean isOffice) {
        silBuildingType.setCenterText(type);
        silGardenName.setVisibility(isOffice ? View.GONE : View.VISIBLE);
        silNo.setVisibility(isOffice ? View.GONE : View.VISIBLE);
    }

    @Override
    public void AreaSure(String area, int district, int business) {
        silArea.setCenterText(area);
    }

    //图片上传
    @Override
    public void addUploadImage() {

    }

    @Override
    public void deleteUploadImage(ImageBean bean, int position) {

    }

    @Override
    public void sureConditioned(String string, int flag) {
        silConditioned.setCenterText(string);
        if (flag == 0) {
            silConditionedFee.setCenterText("包含在物业费内，加时另计");
        } else if (flag == 1) {
            silConditionedFee.setCenterText("按电表计费");
        } else {
            silConditionedFee.setCenterText("无");
        }
    }
}
