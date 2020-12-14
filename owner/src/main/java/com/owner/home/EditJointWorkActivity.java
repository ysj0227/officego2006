package com.owner.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.BuildingManagerBean;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.BuildingEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.EditInputFilter;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.JointCompanyAdapter;
import com.owner.adapter.ServiceLogoAdapter;
import com.owner.adapter.UniqueAdapter;
import com.owner.adapter.UploadBuildingImageAdapter;
import com.owner.dialog.AreaDialog;
import com.owner.dialog.ConditionedDialog;
import com.owner.dialog.ExitConfirmDialog;
import com.owner.dialog.FloorTypeDialog;
import com.owner.dialog.ServiceSelectedDialog;
import com.owner.home.contract.JointWorkContract;
import com.owner.home.presenter.JointWorkPresenter;
import com.owner.home.rule.CarFeeTextWatcher;
import com.owner.home.rule.FloorHeightTextWatcher;
import com.owner.home.rule.IntegerTextWatcher;
import com.owner.home.rule.LiftTextWatcher;
import com.owner.home.rule.TextCountsWatcher;
import com.owner.home.utils.CommonUtils;
import com.owner.identity.model.ImageBean;
import com.owner.utils.SpaceItemDecoration;
import com.owner.zxing.QRScanActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_jointwork_manager")
public class EditJointWorkActivity extends BaseMvpActivity<JointWorkPresenter>
        implements JointWorkContract.View, ServiceSelectedDialog.ServiceLogoListener,
        FloorTypeDialog.FloorListener, UniqueAdapter.UniqueListener,
        AreaDialog.AreaSureListener, UploadBuildingImageAdapter.UploadImageListener,
        ConditionedDialog.ConditionedListener {

    private final int serviceMeetingFlay = 0;
    private final int serviceCompanyFlay = 1;
    private final int serviceBaseFlay = 2;
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int REQUEST_SAVE_PUBLISH = 11000;

    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "tv_upload_title")
    TextView tvUploadTitle;
    @ViewById(resName = "tv_house_characteristic")
    TextView tvHouseCharacteristic;
    @ViewById(resName = "sil_joint_work_name")
    SettingItemLayout silJointWorkName;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "sil_address")
    SettingItemLayout silAddress;
    @ViewById(resName = "sil_floor_no")
    SettingItemLayout silFloorNo;
    @ViewById(resName = "et_floors")
    EditText etFloors;
    @ViewById(resName = "et_floors_count")
    EditText etFloorsCount;
    @ViewById(resName = "sil_storey_height")
    SettingItemLayout silStoreyHeight;
    @ViewById(resName = "sil_conditioned")
    SettingItemLayout silConditioned;
    @ViewById(resName = "sil_conditioned_fee")
    SettingItemLayout silConditionedFee;
    @ViewById(resName = "sil_meeting_room")
    SettingItemLayout silMeetingRoom;
    @ViewById(resName = "sil_contains_persons")
    SettingItemLayout silContainsPersons;
    @ViewById(resName = "sil_car_num")
    SettingItemLayout silCarNum;
    @ViewById(resName = "sil_car_fee")
    SettingItemLayout silCarFee;
    //电梯
    @ViewById(resName = "et_customer_lift")
    EditText etCustomerLift;
    @ViewById(resName = "et_passenger_lift")
    EditText etPassengerLift;
    //介绍
    @ViewById(resName = "tv_counts")
    TextView tvCounts;
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
    //服务
    @ViewById(resName = "rv_meeting_match")
    RecyclerView rvMeetingMatch;
    @ViewById(resName = "rv_create_service")
    RecyclerView rvCompanyService;
    @ViewById(resName = "rv_base_service")
    RecyclerView rvBaseService;
    @ViewById(resName = "iv_mark_image_lift")
    ImageView ivMarkImageLift;
    //图片list
    @ViewById(resName = "rv_upload_image")
    RecyclerView rvUploadImage;
    //扫描
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;
    //编辑
    @Extra
    BuildingManagerBean buildingManagerBean;
    @Extra
    boolean isRefreshHouseList;
    //区域
    private int district, business;
    //加入企业
    private JointCompanyAdapter adapter;
    private List<String> jointCompanyList = new ArrayList<String>();
    //会议室配套
    private Map<Integer, String> meetingMap;
    //企业服务
    private Map<Integer, String> companyMap;
    //基础服务
    private Map<Integer, String> baseMap;
    //特色
    private UniqueAdapter uniqueAdapter;
    private Map<Integer, String> uniqueMap;
    //上传图片
    private List<ImageBean> uploadImageList = new ArrayList<>();
    private UploadBuildingImageAdapter imageAdapter;
    private String localImagePath;
    //删除的图片
    private List<String> deleteList = new ArrayList<>();
    //楼层-单，多
    private String mFloorType;
    //vr url
    private String vrUrl = "";

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new JointWorkPresenter();
        mPresenter.attachView(this);
        initViews();
        initDigits();
        mPresenter.getBuildingEdit(buildingManagerBean.getBuildingId(), buildingManagerBean.getIsTemp());
    }

    private void initViews() {
        titleBar.getLeftImg().setOnClickListener(view -> new ExitConfirmDialog(this));
        titleBar.setAppTitle("编辑共享办公");
        tvUploadTitle.setText("上传网点图片");
        tvHouseCharacteristic.setText("共享办公特色");
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
        //服务
        LinearLayoutManager lmHorizontal = new LinearLayoutManager(this);
        lmHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCompanyService.setLayoutManager(lmHorizontal);
        LinearLayoutManager lmHorizontal2 = new LinearLayoutManager(this);
        lmHorizontal2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvBaseService.setLayoutManager(lmHorizontal2);
        //会议室配套
        LinearLayoutManager lmHorizontal3 = new LinearLayoutManager(this);
        lmHorizontal3.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvMeetingMatch.setLayoutManager(lmHorizontal3);
        //上传图片
        GridLayoutManager layoutManager2 = new GridLayoutManager(context, 3);
        layoutManager2.setSmoothScrollbarEnabled(true);
        layoutManager2.setAutoMeasureEnabled(true);
        rvUploadImage.setLayoutManager(layoutManager2);
        rvUploadImage.setNestedScrollingEnabled(false);
        initData();
    }

    private void initData() {
        //初始化本地图路径
        localImagePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "addJointWorkPath.jpg";
        //初始化图片默认添加一个
        uploadImageList.add(new ImageBean(false, 0, ""));
        imageAdapter = new UploadBuildingImageAdapter(context, uploadImageList);
        imageAdapter.setListener(this);
        rvUploadImage.setAdapter(imageAdapter);
    }

    private void initDigits() {
        // 园区名称 长度最大25
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silJointWorkName.getEditTextView(), 25);
        //净高 层高 0-8或一位小数
        silStoreyHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(context, silStoreyHeight.getEditTextView()));
        //车位数 车位费
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silCarNum.getEditTextView(), 20);
        //车位费0-5000整数
        silCarFee.getEditTextView().addTextChangedListener(new CarFeeTextWatcher(context, silCarFee.getEditTextView()));
        //电梯0-20整数
        etCustomerLift.addTextChangedListener(new LiftTextWatcher(context, etCustomerLift));
        etPassengerLift.addTextChangedListener(new LiftTextWatcher(context, etPassengerLift));
        //会议室数量
        silMeetingRoom.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 10, silMeetingRoom.getEditTextView()));
        //最多容纳人数
        silContainsPersons.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 50, silContainsPersons.getEditTextView()));
        //介绍
        cetDescContent.addTextChangedListener(new TextCountsWatcher(tvCounts, cetDescContent));
        //总楼层0-150
        etFloorsCount.addTextChangedListener(new IntegerTextWatcher(context, 150, etFloorsCount));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
        submit();
    }

    private void submit() {
        String buildingArea = silArea.getContextView().getText().toString();
        if (TextUtils.isEmpty(buildingArea)) {
            shortTip("请选择所在区域");
            return;
        }
        String address = silAddress.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(address)) {
            shortTip("请输入详细地址");
            return;
        }
        String floorNo = silFloorNo.getLeftToArrowTextView().getText().toString();
        if (TextUtils.isEmpty(floorNo)) {
            shortTip("请选择所在楼层");
            return;
        }
        String floors = etFloors.getText().toString();
        if (TextUtils.isEmpty(floors)) {
            shortTip("请输入所在楼层");
            return;
        }
        String floorsCount = etFloorsCount.getText().toString();
        if (TextUtils.isEmpty(floorsCount)) {
            shortTip("请输入总楼层");
            return;
        }
        String storeyHeight = silStoreyHeight.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(storeyHeight)) {
            shortTip("请输入净高");
            return;
        }
        String conditioned = silConditioned.getContextView().getText().toString();
        if (TextUtils.isEmpty(conditioned)) {
            shortTip("请选择空调类型");
            return;
        }
        String meetingRoom = silMeetingRoom.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(meetingRoom)) {
            shortTip("请输入会议室数量");
            return;
        }
        if (!TextUtils.isEmpty(meetingRoom) && TextUtils.equals("0", meetingRoom)) {
            shortTip("会议室数量不能为0");
            return;
        }
        if (uploadImageList == null || uploadImageList.size() <= 1) {
            shortTip("请上传网点图片");
            return;
        }
        //空调费
        String conditionedFee = silConditionedFee.getContextView().getText().toString();
        //介绍
        String buildingIntroduction = cetDescContent.getText() == null ? "" : cetDescContent.getText().toString();
        //容纳人数
        String containsPersons = silContainsPersons.getEditTextView().getText().toString();
        //配套
        String meetingMatch = CommonHelper.getKey(meetingMap);
        //停车费
        String carNum = silCarNum.getEditTextView().getText().toString();
        String carFee = silCarFee.getEditTextView().getText().toString();
        //电梯
        String customerLift = etCustomerLift.getText().toString();
        String passengerLift = etPassengerLift.getText().toString();
        //网络
        String net = CommonUtils.internet(rbTelecom, rbUnicom, rbMobile);
        //入住企业
        String addCompany = CommonUtils.company(jointCompanyList);
        //特色
        String uniqueTags = uniqueMap == null || uniqueMap.size() == 0 ? "" : CommonHelper.getKey(uniqueMap);
        //服务
        String companyService = companyMap == null || companyMap.size() == 0 ? "" : CommonHelper.getKey(companyMap);
        String baseService = baseMap == null || baseMap.size() == 0 ? "" : CommonHelper.getKey(baseMap);
        //封面图片
        String mainPic = uploadImageList.get(0).getPath();
        //添加图片
        String addImage = CommonUtils.addUploadImage(uploadImageList);
        //删除图片
        String deleteImage = CommonUtils.delUploadImage(deleteList);
        mPresenter.saveEdit(buildingManagerBean.getBuildingId(), buildingManagerBean.getIsTemp(),
                district, business, address, mFloorType, floors, floorsCount, storeyHeight,
                conditioned, conditionedFee, meetingRoom, containsPersons, meetingMatch,
                carNum, carFee, customerLift, passengerLift, buildingIntroduction,
                net, addCompany, uniqueTags, companyService,
                baseService, mainPic, addImage, deleteImage);
    }

    @Click(resName = "iv_close_scan")
    void closeScanOnClick() {
        btnScan.setVisibility(View.GONE);
        ivCloseScan.setVisibility(View.GONE);
    }

    //web 去编辑
    @Click(resName = "btn_scan")
    void toWebEditOnClick() {
        if (!PermissionUtils.checkSDCardCameraPermission(this)) {
            return;
        }
        startActivity(new Intent(context, QRScanActivity.class));
    }

    @Click(resName = "sil_floor_no")
    void floorNoOnClick() {
        new FloorTypeDialog(context).setListener(this);
    }

    @Click(resName = "sil_area")
    void areaOnClick() {
        new AreaDialog(context, district, business).setListener(this);
    }

    //service
    @Click(resName = "rl_meeting_resources")
    void serviceMeetingMatchClick() {
        mPresenter.getRoomMatching();
    }

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

    @SuppressLint("SetTextI18n")
    @Override
    public void buildingEditSuccess(BuildingEditBean data) {
        if (data == null) return;
        if (data.getBuildingMsg() != null) {
            //vr
            if (data.getVr() != null && data.getVr().size() > 0) {
                vrUrl = data.getVr().get(0).getImgUrl();
            }
            //网点名称
            silJointWorkName.setCenterText(data.getBuildingMsg().getBranchesName());
            silJointWorkName.getContextView().setTextColor(ContextCompat.getColor(context, R.color.text_99));
            silArea.setCenterText(data.getAddress());
            //区域
            district = TextUtils.isEmpty(data.getBuildingMsg().getDistrictId()) ? 0 : Integer.valueOf(data.getBuildingMsg().getDistrictId());
            business = TextUtils.isEmpty(data.getBuildingMsg().getBusinessDistrict()) ? 0 : Integer.valueOf(data.getBuildingMsg().getBusinessDistrict());
            silAddress.getEditTextView().setText(data.getBuildingMsg().getAddress());
            //所在楼层 1是单层2是多层
            mFloorType = data.getBuildingMsg().getFloorType();
            silFloorNo.setLeftToArrowText(TextUtils.equals("1", data.getBuildingMsg().getFloorType()) ? "单层" : "多层");
            etFloors.setText(data.getBuildingMsg().getTotalFloor());
            etFloorsCount.setText(data.getBuildingMsg().getBranchesTotalFloor());
            //净高
            silStoreyHeight.getEditTextView().setText(data.getBuildingMsg().getClearHeight());
            //空调类型
            String ariCondition = data.getBuildingMsg().getAirConditioning();
            silConditioned.setCenterText(ariCondition);
            silConditionedFee.setVisibility(View.VISIBLE);
            if (ariCondition.contains("中央")) {
                silConditionedFee.setCenterText("包含在物业费内，加时另计");
            } else if (ariCondition.contains("独立")) {
                silConditionedFee.setCenterText("按电表计费");
            } else {
                silConditionedFee.setCenterText("无");
            }
            //会议室数量
            silMeetingRoom.getEditTextView().setText(data.getBuildingMsg().getConferenceNumber() == 0 ? "" :
                    String.valueOf(data.getBuildingMsg().getConferenceNumber()));
            //容纳人数
            silContainsPersons.getEditTextView().setText(data.getBuildingMsg().getConferencePeopleNumber() == 0 ? "" :
                    String.valueOf(data.getBuildingMsg().getConferencePeopleNumber()));
            //车位数 车位费
            silCarNum.getEditTextView().setText(data.getBuildingMsg().getParkingSpace());
            silCarFee.getEditTextView().setText(data.getBuildingMsg().getParkingSpaceRent());
            //电梯数
            etCustomerLift.setText(data.getBuildingMsg().getPassengerLift());
            etPassengerLift.setText(data.getBuildingMsg().getCargoLift());
            //网络
            String internet = data.getBuildingMsg().getInternet();
            if (internet.contains("电信")) {
                rbTelecom.setChecked(true);
            }
            if (internet.contains("联通")) {
                rbUnicom.setChecked(true);
            }
            if (internet.contains("移动")) {
                rbMobile.setChecked(true);
            }
            //介绍
            String des = data.getBuildingMsg().getBuildingIntroduction();
            if (!TextUtils.isEmpty(des) && des.length() > 100) {
                cetDescContent.setText(des.substring(0, 96) + "...");
            } else {
                cetDescContent.setText(des);
            }
            //入住企业
            String settlementLicence = data.getBuildingMsg().getSettlementLicence();
            List<String> result = CommonHelper.stringList(settlementLicence);
            if (result != null) {
                jointCompanyList.clear();
                for (int i = 0; i < result.size(); i++) {
                    jointCompanyList.add(i, result.get(i));
                }
                adapter.notifyDataSetChanged();
            }
            //楼盘特色
            String tags = data.getBuildingMsg().getTags();
            List<String> resultUt = CommonHelper.stringList(tags);
            if (resultUt != null) {
                uniqueMap = new HashMap<>();
                for (int i = 0; i < resultUt.size(); i++) {
                    uniqueMap.put(Integer.valueOf(resultUt.get(i)), "");
                }
            }
            mPresenter.getBranchUnique();
            //共享服务
            services(data);
            //网点图片
            showImage(data);
        }
    }

    //服务
    private void services(BuildingEditBean data) {
        meetingMap = new HashMap<>();
        baseMap = new HashMap<>();
        companyMap = new HashMap<>();
        List<DirectoryBean.DataBean> meetingList = new ArrayList<>();
        List<DirectoryBean.DataBean> companyList = new ArrayList<>();
        List<DirectoryBean.DataBean> baseList = new ArrayList<>();
        //企业服务
        DirectoryBean.DataBean companyBean;
        for (int i = 0; i < data.getCompanyService().size(); i++) {
            companyBean = new DirectoryBean.DataBean();
            companyBean.setDictValue(data.getCompanyService().get(i).getDictValue());
            companyBean.setDictImg(data.getCompanyService().get(i).getDictImg());
            companyBean.setDictImgBlack(data.getCompanyService().get(i).getDictImgBlack());
            companyList.add(companyBean);
            companyMap.put(data.getCompanyService().get(i).getDictValue(), data.getCompanyService().get(i).getDictImg());
        }
        rvCompanyService.setAdapter(new ServiceLogoAdapter(context, companyList));
        //基础服务
        DirectoryBean.DataBean baseBean;
        for (int i = 0; i < data.getBasicServices().size(); i++) {
            baseBean = new DirectoryBean.DataBean();
            baseBean.setDictValue(data.getBasicServices().get(i).getDictValue());
            baseBean.setDictImg(data.getBasicServices().get(i).getDictImg());
            baseBean.setDictImgBlack(data.getBasicServices().get(i).getDictImgBlack());
            baseList.add(baseBean);
            baseMap.put(data.getBasicServices().get(i).getDictValue(), data.getBasicServices().get(i).getDictImg());
        }
        rvBaseService.setAdapter(new ServiceLogoAdapter(context, baseList));
        //会议室配套
        DirectoryBean.DataBean meetingBean;
        for (int i = 0; i < data.getRoomMatching().size(); i++) {
            meetingBean = new DirectoryBean.DataBean();
            meetingBean.setDictValue(data.getRoomMatching().get(i).getDictValue());
            meetingBean.setDictImg(data.getRoomMatching().get(i).getDictImg());
            meetingBean.setDictImgBlack(data.getRoomMatching().get(i).getDictImgBlack());
            meetingList.add(meetingBean);
            meetingMap.put(data.getRoomMatching().get(i).getDictValue(), data.getRoomMatching().get(i).getDictImg());
        }
        rvMeetingMatch.setAdapter(new ServiceLogoAdapter(context, meetingList));
    }

    //网点图片
    private void showImage(BuildingEditBean data) {
        //封面图
        if (!TextUtils.isEmpty(data.getBuildingMsg().getMainPic())) {
            uploadImageList.add(uploadImageList.size() - 1, new ImageBean(true, 0, data.getBuildingMsg().getMainPic()));
        }
        for (int i = 0; i < data.getBanner().size(); i++) {
            uploadImageList.add(uploadImageList.size() - 1, new ImageBean(true, 0, data.getBanner().get(i).getImgUrl()));
        }
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        uniqueAdapter = new UniqueAdapter(context, uniqueMap, data);
        uniqueAdapter.setListener(this);
        rvHouseUnique.setAdapter(uniqueAdapter);
    }

    @Override
    public void roomMatchingSuccess(List<DirectoryBean.DataBean> data) {
        new ServiceSelectedDialog(context, serviceMeetingFlay, meetingMap, data).setLogoListener(this);
    }

    @Override
    public void baseServiceSuccess(List<DirectoryBean.DataBean> data) {
        new ServiceSelectedDialog(context, serviceBaseFlay, baseMap, data).setLogoListener(this);
    }

    @Override
    public void companyServiceSuccess(List<DirectoryBean.DataBean> data) {
        new ServiceSelectedDialog(context, serviceCompanyFlay, companyMap, data).setLogoListener(this);
    }

    @Override
    public void uploadSuccess(UploadImageBean data) {
        if (data != null && data.getUrls() != null && data.getUrls().size() > 0) {
            int urlSize = data.getUrls().size();
            ImageBean bean;
            for (int i = 0; i < urlSize; i++) {
                bean = new ImageBean(true, 0, data.getUrls().get(i).getUrl());
                uploadImageList.set(uploadImageList.size() - 1 - urlSize + i, bean);
            }
            imageAdapter.notifyDataSetChanged();
        }
        shortTip("上传成功");
    }

    @Override
    public void serviceLogoResult(int flay, Map<Integer, String> mapLogo, List<DirectoryBean.DataBean> list) {
        if (flay == 0) {
            meetingMap = mapLogo;
            rvMeetingMatch.setAdapter(new ServiceLogoAdapter(context, list));
        } else if (flay == 1) {
            companyMap = mapLogo;
            rvCompanyService.setAdapter(new ServiceLogoAdapter(context, list));
        } else if (flay == 2) {
            baseMap = mapLogo;
            rvBaseService.setAdapter(new ServiceLogoAdapter(context, list));
        }
    }

    @Override
    public void sureFloor(String text, String type) {
        mFloorType = type;
        silFloorNo.setLeftToArrowText(text);
    }

    @Override
    public void AreaSure(String area, int district, int business) {
        this.district = district;
        this.business = business;
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

    @Override
    public void uniqueResult(Map<Integer, String> uniqueMap) {
        this.uniqueMap = uniqueMap;
    }

    //图片上传
    @Override
    public void addUploadImage() {
        selectedDialog();
    }

    //图片删除
    @Override
    public void deleteUploadImage(ImageBean bean, int position) {
        if (isFastClick(1200)) {
            return;
        }
        deleteList.add(uploadImageList.get(position).getPath());
        uploadImageList.remove(position);
        imageAdapter.notifyDataSetChanged();
    }

    //设置封面图
    @Override
    public void setFirstImage(int position) {
        ImageBean imageBean = uploadImageList.get(0);
        uploadImageList.set(0, uploadImageList.get(position));
        uploadImageList.set(position, imageBean);
        imageAdapter.notifyDataSetChanged();
    }

    private void selectedDialog() {
        final String[] items = {"拍照", "从相册选择"};
        new AlertDialog.Builder(context)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        takePhoto();
                    } else if (i == 1) {
                        openGallery();
                    }
                }).create().show();
    }

    private void takePhoto() {
        if (isOverLimit()) return;
        if (!PermissionUtils.checkSDCardCameraPermission(this)) return;
        if (!FileUtils.isSDExist()) {
            shortTip(R.string.str_no_sd);
            return;
        }
        localImagePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + System.currentTimeMillis() + "certificate.jpg";
        File fileUri = new File(localImagePath);
        Uri localPhotoUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            localPhotoUri = FileProvider.getUriForFile(this, Constants.FILE_PROVIDER_AUTHORITY, fileUri);
        }
        PhotoUtils.takePicture(this, localPhotoUri, REQUEST_CAMERA);
    }

    private void openGallery() {
        if (isOverLimit()) return;
        if (!PermissionUtils.checkStoragePermission(this)) return;
        ImageSelector.builder()
                .useCamera(false) // 设置是否使用拍照
                .setSingle(false)  //设置是否单选
                .setMaxSelectCount(10 - uploadImageList.size())
                .canPreview(true) //是否可以预览图片，默认为true
                .start(this, REQUEST_GALLERY); // 打开相册
    }

    private boolean isOverLimit() {
        if (uploadImageList.size() >= 10) {
            shortTip(R.string.tip_image_upload_overlimit);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照
                ImageUtils.isSaveCropImageView(localImagePath);//图片处理
                uploadImageList.add(uploadImageList.size() - 1, new ImageBean(false, 0, localImagePath));
                mPresenter.uploadImage(Constants.TYPE_IMAGE_BUILDING, uploadImageList);
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                for (int i = 0; i < images.size(); i++) {
                    ImageUtils.isSaveCropImageView(images.get(i));//图片处理
                    uploadImageList.add(uploadImageList.size() - 1, new ImageBean(false, 0, images.get(i)));
                }
                mPresenter.uploadImage(Constants.TYPE_IMAGE_BUILDING, uploadImageList);
            } else if (requestCode == REQUEST_SAVE_PUBLISH) {
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissions(context, requestCode, permissions, grantResults);
    }

    @Override
    public void editSaveSuccess() {
        if (isRefreshHouseList){
            //首页编辑楼盘网点成功
            BaseNotification.newInstance().postNotificationName(
                    CommonNotifications.refreshHouseSuccess, "updateBuildingSuccess");
        }else {
            //popup编辑楼盘网点成功
            BaseNotification.newInstance().postNotificationName(
                    CommonNotifications.updateBuildingSuccess, "updateBuildingSuccess");
        }
        //更新总楼层
        Constants.FLOOR_JOINT_WORK_COUNTS = etFloorsCount.getText().toString();
        UploadVideoVrActivity_.intent(context)
                .flay(Constants.FLAG_BUILDING)
                .buildingManagerBean(buildingManagerBean)
                .vrUrl(vrUrl).startForResult(REQUEST_SAVE_PUBLISH);
    }
}
