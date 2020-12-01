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
import com.officego.commonlib.common.dialog.YearDateDialog;
import com.officego.commonlib.common.model.BuildingManagerBean;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.BuildingEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
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
import com.owner.adapter.UniqueAdapter;
import com.owner.adapter.UploadBuildingImageAdapter;
import com.owner.dialog.AreaDialog;
import com.owner.dialog.BuildingTypeDialog;
import com.owner.dialog.ConditionedDialog;
import com.owner.dialog.ExitConfirmDialog;
import com.owner.home.contract.BuildingContract;
import com.owner.home.presenter.BuildingPresenter;
import com.owner.home.rule.AreaTextWatcher;
import com.owner.home.rule.CarFeeTextWatcher;
import com.owner.home.rule.EstateFeeTextWatcher;
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
@EActivity(resName = "activity_home_building_manager")
public class EditBuildingActivity extends BaseMvpActivity<BuildingPresenter>
        implements BuildingContract.View, YearDateDialog.SureClickListener,
        AreaDialog.AreaSureListener, UploadBuildingImageAdapter.UploadImageListener,
        BuildingTypeDialog.BuildingTypeListener, ConditionedDialog.ConditionedListener,
        UniqueAdapter.UniqueListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int REQUEST_SAVE_PUBLISH = 11000;

    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
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
    //图片list
    @ViewById(resName = "rv_upload_image")
    RecyclerView rvUploadImage;
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;

    //编辑
    @Extra
    BuildingManagerBean buildingManagerBean;
    //是否竣工时间
    private boolean isCompleteTime;
    //区域
    private int district, business;
    //特色
    private UniqueAdapter uniqueAdapter;
    private Map<Integer, String> uniqueMap;
    //加入企业
    private JointCompanyAdapter adapter;
    private List<String> jointCompanyList = new ArrayList<String>();
    //上传图片
    private List<ImageBean> uploadImageList = new ArrayList<>();
    private UploadBuildingImageAdapter imageAdapter;
    private String localImagePath;
    //删除的图片
    private List<String> deleteList = new ArrayList<>();
    //楼盘类型
    private int mBuildingType;
    //楼号
    private String gardenNo = "";
    //vr url
    private String vrUrl = "";

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new BuildingPresenter();
        mPresenter.attachView(this);
        initViews();
        initDigits();
        mPresenter.getBuildingEdit(buildingManagerBean.getBuildingId(), buildingManagerBean.getIsTemp());
    }

    private void initViews() {
        titleBar.getLeftImg().setOnClickListener(view -> new ExitConfirmDialog(this));
        titleBar.setAppTitle("编辑楼盘");
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
        //初始化本地图路径
        localImagePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "addBuildingPath.jpg";
        //初始化图片默认添加一个
        uploadImageList.add(new ImageBean(false, 0, ""));
        imageAdapter = new UploadBuildingImageAdapter(context, uploadImageList);
        imageAdapter.setListener(this);
        rvUploadImage.setAdapter(imageAdapter);
    }

    private void initDigits() {
        //园区名称 长度最大25
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silGardenName.getEditTextView(), 25);
        //物业名称 长度最大20
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silEstate.getEditTextView(), 20);
        //面积 0.1-1000正数数字，保留1位小数，单位 万M²
        silGrossArea.getEditTextView().addTextChangedListener(new AreaTextWatcher(context, 1000, silGrossArea.getEditTextView()));
        //物业费 0-100之间正数，保留1位小数
        silEstateFee.getEditTextView().addTextChangedListener(new EstateFeeTextWatcher(context, 100, silEstateFee.getEditTextView()));
        //净高 层高 0-8或一位小数
        silStoreyHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(context, silStoreyHeight.getEditTextView()));
        silTierHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(context, silTierHeight.getEditTextView()));
        //总楼层0-150整数
        silStorey.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 150, silStorey.getEditTextView()));
        //车位费0-5000整数
        silCarFee.getEditTextView().addTextChangedListener(new CarFeeTextWatcher(context, silCarFee.getEditTextView()));
        //电梯 货梯0-20整数
        etCustomerLift.addTextChangedListener(new LiftTextWatcher(context, etCustomerLift));
        etPassengerLift.addTextChangedListener(new LiftTextWatcher(context, etPassengerLift));
        //介绍
        cetDescContent.addTextChangedListener(new TextCountsWatcher(tvCounts, cetDescContent));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
        submit();
    }

    private void submit() {
        String buildingName = silBuildingType.getContextView().getText().toString();
        if (TextUtils.isEmpty(buildingName)) {
            shortTip("请选择楼盘类型");
            return;
        }
        String gardenName = silGardenName.getContextView().getText().toString();
        if (TextUtils.isEmpty(gardenName)) {
            shortTip("请输入名称");
            return;
        }
        if (mBuildingType != 1) {
            gardenNo = silNo.getEditTextView().getText().toString();
            if (TextUtils.isEmpty(gardenNo)) {
                shortTip("请输入楼号");
                return;
            }
        }
        String buildingArea = silArea.getContextView().getText().toString();
        if (TextUtils.isEmpty(buildingArea)) {
            shortTip("请选择所在区域");
            return;
        }
        if (district == 0 || business == 0) {
            shortTip("请选择完整的区域");
            return;
        }
        String address = silAddress.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(address)) {
            shortTip("请输入详细地址");
            return;
        }
        String floorStorey = silStorey.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(floorStorey)) {
            shortTip("请输入总楼层");
            return;
        }
        String completeTime = silCompleteTime.getContextView().getText().toString();
        String refurbishedTime = silReCompleteTime.getContextView().getText().toString();
        if (TextUtils.isEmpty(completeTime)) {
            shortTip("请选择竣工时间");
            return;
        }
        if (!TextUtils.isEmpty(completeTime) && !TextUtils.isEmpty(refurbishedTime)) {
            if (Integer.valueOf(completeTime) > Integer.valueOf(refurbishedTime)) {
                shortTip("竣工时间不能大于翻新时间");
                return;
            }
        }
        String clearHeight = silStoreyHeight.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(clearHeight)) {
            shortTip("请输入净高");
            return;
        }
        String estate = silEstate.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(estate)) {
            shortTip("请输入物业公司");
            return;
        }
        String estateFee = silEstateFee.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(estateFee)) {
            shortTip("请输入物业费");
            return;
        }
        String carNum = silCarNum.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(carNum)) {
            shortTip("请输入车位数");
            return;
        }
        String conditioned = silConditioned.getContextView().getText().toString();
        if (TextUtils.isEmpty(conditioned)) {
            shortTip("请选择空调类型");
            return;
        }
        String customerLift = etCustomerLift.getText().toString();
        if (TextUtils.isEmpty(customerLift)) {
            shortTip("请输入客梯数量");
            return;
        }
        String passengerLift = etPassengerLift.getText().toString();
        if (TextUtils.isEmpty(passengerLift)) {
            shortTip("请输入货梯数量");
            return;
        }
        if (uploadImageList == null || uploadImageList.size() <= 1) {
            shortTip("请上传楼盘图片");
            return;
        }
        //网络
        String net = CommonUtils.internet(rbTelecom, rbUnicom, rbMobile);
        //入住企业
        String addCompany = CommonUtils.company(jointCompanyList);
        //特色
        String uniqueTags = uniqueMap == null || uniqueMap.size() == 0 ? "" : CommonHelper.getKey(uniqueMap);
        //封面图片
        String mainPic = uploadImageList.get(0).getPath();
        //添加图片
        String addImage = CommonUtils.addUploadImage(uploadImageList);
        //删除图片
        String deleteImage = CommonUtils.delUploadImage(deleteList);
        //建筑面积
        String constructionArea = silGrossArea.getEditTextView().getText().toString();
        //层高
        String tireHeight = silTierHeight.getEditTextView().getText().toString();
        //停车费
        String carFee = silCarFee.getEditTextView().getText().toString();
        //空调费
        String conditionedFee = silConditionedFee.getContextView().getText().toString();
        //介绍
        String buildingIntroduction = cetDescContent.getText() == null ? "" : cetDescContent.getText().toString();
        mPresenter.saveEdit(buildingManagerBean.getBuildingId(), buildingManagerBean.getIsTemp(),
                mBuildingType, gardenNo, district, business, address, floorStorey, completeTime, refurbishedTime,
                constructionArea, clearHeight, tireHeight, estate, estateFee, carNum, carFee,
                conditioned, conditionedFee, customerLift, passengerLift, buildingIntroduction, net,
                addCompany, uniqueTags, mainPic, addImage, deleteImage);
    }

    @Click(resName = "iv_close_scan")
    void closeScanOnClick() {
        btnScan.setVisibility(View.GONE);
        ivCloseScan.setVisibility(View.GONE);
    }

    @Click(resName = "btn_scan")
    void toWebEditOnClick() {
        if (!PermissionUtils.checkSDCardCameraPermission(this)) {
            return;
        }
        startActivity(new Intent(context, QRScanActivity.class));
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
        uniqueAdapter = new UniqueAdapter(context, uniqueMap, data);
        uniqueAdapter.setListener(this);
        rvHouseUnique.setAdapter(uniqueAdapter);
    }

    @SuppressLint("UseSparseArrays")
    @Override
    public void buildingEditSuccess(BuildingEditBean data) {
        if (data == null) return;
        if (data.getBuildingMsg() != null) {
            //vr
            if (data.getVr() != null && data.getVr().size() > 0) {
                vrUrl = data.getVr().get(0).getImgUrl();
            }
            //楼盘类型1写字楼 2商务园 3创意园 4共享空间 5公寓  6产业园
            silGardenName.setVisibility(View.VISIBLE);
            silGardenName.setCenterText(data.getBuildingMsg().getBuildingName());
            silGardenName.getContextView().setTextColor(ContextCompat.getColor(context, R.color.text_99));
            mBuildingType = data.getBuildingMsg().getBuildingType();
            if (data.getBuildingMsg().getBuildingType() == 1) {
                silNo.setVisibility(View.GONE);
                silGardenName.setTitle("写字楼名称");
                silBuildingType.setCenterText("写字楼");
            } else if (data.getBuildingMsg().getBuildingType() == 3) {
                silNo.setVisibility(View.VISIBLE);
                silGardenName.setTitle("园区名称");
                silBuildingType.setCenterText("创意园");
                silNo.getEditTextView().setText(data.getBuildingMsg().getBuildingNum());
            } else if (data.getBuildingMsg().getBuildingType() == 6) {
                silNo.setVisibility(View.VISIBLE);
                silGardenName.setTitle("园区名称");
                silBuildingType.setCenterText("产业园");
                silNo.getEditTextView().setText(data.getBuildingMsg().getBuildingNum());
            }
            //区域
            silArea.setCenterText(data.getAddress());
            district = TextUtils.isEmpty(data.getBuildingMsg().getDistrictId()) ? 0 : Integer.valueOf(data.getBuildingMsg().getDistrictId());
            business = TextUtils.isEmpty(data.getBuildingMsg().getBusinessDistrict()) ? 0 : Integer.valueOf(data.getBuildingMsg().getBusinessDistrict());
            silAddress.getEditTextView().setText(data.getBuildingMsg().getAddress());
            //总楼层
            silStorey.getEditTextView().setText(data.getBuildingMsg().getTotalFloor());
            //竣工翻新时间
            silCompleteTime.setCenterText(data.getBuildingMsg().getCompletionTime());
            silReCompleteTime.setCenterText(data.getBuildingMsg().getRefurbishedTime());
            //建筑面积
            silGrossArea.getEditTextView().setText(data.getBuildingMsg().getConstructionArea());
            //净高层高
            silStoreyHeight.getEditTextView().setText(data.getBuildingMsg().getClearHeight());
            silTierHeight.getEditTextView().setText(data.getBuildingMsg().getStoreyHeight());
            //物业公司 物业费
            silEstate.getEditTextView().setText(data.getBuildingMsg().getProperty());
            silEstateFee.getEditTextView().setText(data.getBuildingMsg().getPropertyCosts());
            //车位数 车位费
            silCarNum.getEditTextView().setText(data.getBuildingMsg().getParkingSpace());
            silCarFee.getEditTextView().setText(data.getBuildingMsg().getParkingSpaceRent());
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
            mPresenter.getBuildingUnique();
            //楼盘图片
            showImage(data);
        }
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
    public void uploadSuccess(UploadImageBean data) {
        if (data != null && data.getUrls() != null && data.getUrls().size() > 0) {
            int urlSize = data.getUrls().size();
            ImageBean bean;
            for (int i = 0; i < urlSize; i++) {
                bean = new ImageBean(true, 0, data.getUrls().get(i).getUrl());
                uploadImageList.set(uploadImageList.size() - 1 - urlSize + i, bean);
            }
            imageAdapter.notifyDataSetChanged();
            shortTip("上传成功");
        }
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
    public void sureBuildingType(String type, int buildingType, boolean isOffice) {
        silNo.setVisibility(isOffice ? View.GONE : View.VISIBLE);
        mBuildingType = buildingType;
        silBuildingType.setCenterText(type);
        silGardenName.setVisibility(View.VISIBLE);
        if (isOffice) {
            silGardenName.setTitle("写字楼名称");
        } else {
            silGardenName.setTitle("园区名称");
        }
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
        //更新总楼层
        Constants.FLOOR_COUNTS = silStorey.getEditTextView().getText().toString();
        UploadVideoVrActivity_.intent(context)
                .flay(Constants.FLAG_BUILDING)
                .buildingManagerBean(buildingManagerBean)
                .vrUrl(vrUrl).startForResult(REQUEST_SAVE_PUBLISH);
    }
}
