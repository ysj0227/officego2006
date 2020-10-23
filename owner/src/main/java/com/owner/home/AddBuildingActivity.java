package com.owner.home;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.dialog.YearDateDialog;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.EditInputFilter;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.JointCompanyAdapter;
import com.owner.adapter.UniqueAdapter;
import com.owner.adapter.UploadBuildingImageAdapter;
import com.owner.dialog.BuildingTypeDialog;
import com.owner.dialog.ConditionedDialog;
import com.owner.home.contract.BuildingContract;
import com.owner.home.presenter.BuildingPresenter;
import com.owner.home.rule.AreaTextWatcher;
import com.owner.home.rule.CarFeeTextWatcher;
import com.owner.home.rule.EstateFeeTextWatcher;
import com.owner.home.rule.FloorHeightTextWatcher;
import com.owner.home.rule.FloorNumTextWatcher;
import com.owner.home.rule.LiftTextWatcher;
import com.owner.identity.dialog.AreaDialog;
import com.owner.identity.model.ImageBean;
import com.owner.utils.SpaceItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_building_manager")
public class AddBuildingActivity extends BaseMvpActivity<BuildingPresenter>
        implements BuildingContract.View, YearDateDialog.SureClickListener,
        AreaDialog.AreaSureListener, UploadBuildingImageAdapter.UploadImageListener,
        BuildingTypeDialog.BuildingTypeListener, ConditionedDialog.ConditionedListener,
        UniqueAdapter.UniqueListener {
    @ViewById(resName = "sil_building_type")
    SettingItemLayout silBuildingType;
    @ViewById(resName = "sil_garden_name")
    SettingItemLayout silGardenName;
    @ViewById(resName = "sil_no")
    SettingItemLayout silNo;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "sil_address")
    SettingItemLayout silAddress;
    @ViewById(resName = "sil_storey")
    SettingItemLayout silStorey;
    @ViewById(resName = "sil_complete_time")
    SettingItemLayout silCompleteTime;
    @ViewById(resName = "sil_recomplete_time")
    SettingItemLayout silReCompleteTime;
    @ViewById(resName = "sil_gross_area")
    SettingItemLayout silGrossArea;
    @ViewById(resName = "sil_storey_height")
    SettingItemLayout silStoreyHeight;
    @ViewById(resName = "sil_tier_height")
    SettingItemLayout silTierHeight;
    @ViewById(resName = "sil_estate")
    SettingItemLayout silEstate;
    @ViewById(resName = "sil_estate_fee")
    SettingItemLayout silEstateFee;
    @ViewById(resName = "sil_car_num")
    SettingItemLayout silCarNum;
    @ViewById(resName = "sil_car_fee")
    SettingItemLayout silCarFee;
    //空调
    @ViewById(resName = "sil_conditioned")
    SettingItemLayout silConditioned;
    @ViewById(resName = "sil_conditioned_fee")
    SettingItemLayout silConditionedFee;
    //电梯
    @ViewById(resName = "et_customer_lift")
    EditText etCustomerLift;
    @ViewById(resName = "et_passenger_lift")
    EditText etPassengerLift;
    //介绍
    @ViewById(resName = "cet_desc_content")
    ClearableEditText cetDescContent;
    //特色
    @ViewById(resName = "rv_house_characteristic")
    RecyclerView rvHouseUnique;
    //入住企业
    @ViewById(resName = "rv_join_company")
    RecyclerView rvJoinCompany;
    //网络 电信 联通 移动
    @ViewById(resName = "rb_telecom")
    CheckBox rbTelecom;
    @ViewById(resName = "rb_unicom")
    CheckBox rbUnicom;
    @ViewById(resName = "rb_mobile")
    CheckBox rbMobile;
    //图片list
    @ViewById(resName = "rv_upload_image")
    RecyclerView rvUploadImage;
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;

    //是否竣工时间
    private boolean isCompleteTime;
    //区域
    private int district, business;
    //特色
    private String uniqueTags;
    private Map<Integer, String> uniqueMap;
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
        initDigits();
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

    private void initDigits() {
        //园区名称 长度最大25
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silGardenName.getEditTextView(), 25);
        //物业名称 长度最大20
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silEstate.getEditTextView(), 20);
        //面积 0.1-1000正数数字，保留1位小数，单位“万  M²
        silGrossArea.getEditTextView().addTextChangedListener(new AreaTextWatcher(context, silGrossArea.getEditTextView()));
        //物业费 0-100之间正数，保留1位小数
        silEstateFee.getEditTextView().addTextChangedListener(new EstateFeeTextWatcher(context, silEstateFee.getEditTextView()));
        //净高 层高 0-8或一位小数
        silStoreyHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(silStoreyHeight.getEditTextView()));
        silTierHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(silTierHeight.getEditTextView()));
        //总楼层0-150整数
        silStorey.getEditTextView().addTextChangedListener(new FloorNumTextWatcher(context, 150,3,silStorey.getEditTextView()));
        //车位费0-5000整数
        silCarFee.getEditTextView().addTextChangedListener(new CarFeeTextWatcher(context, silCarFee.getEditTextView()));
        //电梯0-20整数
        etCustomerLift.addTextChangedListener(new LiftTextWatcher(context, etCustomerLift));
        etPassengerLift.addTextChangedListener(new LiftTextWatcher(context, etPassengerLift));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
        //网络
        StringBuilder buffer = new StringBuilder();
        if (rbTelecom.isChecked()) {
            buffer.append(rbTelecom.getText().toString()).append(",");
        }
        if (rbUnicom.isChecked()) {
            buffer.append(rbUnicom.getText().toString()).append(",");
        }
        if (rbMobile.isChecked()) {
            buffer.append(rbMobile.getText().toString()).append(",");
        }
        String net = buffer.toString();
        if (!TextUtils.isEmpty(net)) {
            LogCat.e(TAG, "111111 net=" + buffer.toString().substring(0, buffer.toString().length() - 1));
        }
        LogCat.e(TAG, "111111 uniqueTags=" + uniqueTags);
        //入住企业
        for (int i = 0; i < jointCompanyList.size(); i++) {
            LogCat.e(TAG, "111111 jointCompanyList=" + jointCompanyList.get(i));
        }
        //UploadVideoVrActivity_.intent(context).start();
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
        UniqueAdapter adapter = new UniqueAdapter(context, uniqueMap, data);
        adapter.setListener(this);
        rvHouseUnique.setAdapter(adapter);
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
        this.district = district;
        this.business = business;
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
        silConditionedFee.setVisibility(View.VISIBLE);
        if (flag == 0) {
            silConditionedFee.setCenterText("包含在物业费内，加时另计");
        } else if (flag == 1) {
            silConditionedFee.setCenterText("按电表计费");
        } else {
            silConditionedFee.setCenterText("无");
        }
    }

    @Override
    public void uniqueResult(Map<Integer, String> uniqueMap) {
        this.uniqueMap = uniqueMap;
        uniqueTags = CommonHelper.getKey(uniqueMap);
    }
}
