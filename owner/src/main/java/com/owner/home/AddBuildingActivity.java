package com.owner.home;

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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.dialog.YearDateDialog;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.EditInputFilter;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.JointCompanyAdapter;
import com.owner.adapter.UniqueAdapter;
import com.owner.adapter.UploadBuildingImageAdapter;
import com.owner.dialog.AreaDialog;
import com.owner.dialog.BuildingTypeDialog;
import com.owner.dialog.ConditionedDialog;
import com.owner.home.contract.BuildingContract;
import com.owner.home.presenter.BuildingPresenter;
import com.owner.home.rule.AreaTextWatcher;
import com.owner.home.rule.CarFeeTextWatcher;
import com.owner.home.rule.EstateFeeTextWatcher;
import com.owner.home.rule.FloorHeightTextWatcher;
import com.owner.home.rule.IntegerTextWatcher;
import com.owner.home.rule.LiftTextWatcher;
import com.owner.home.rule.TextCountsWatcher;
import com.owner.identity.model.ImageBean;
import com.owner.utils.SpaceItemDecoration;
import com.owner.zxing.QRScanActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
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
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
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
    private List<ImageBean> uploadImageList = new ArrayList<>();
    private UploadBuildingImageAdapter imageAdapter;
    private String localImagePath;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new BuildingPresenter();
        mPresenter.attachView(this);
        initViews();
        initDigits();
        mPresenter.getBuildingUnique();
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
        submit();
    }

    private void submit() {
        String buildingName = silBuildingType.getContextView().getText().toString();
        if (TextUtils.isEmpty(buildingName)) {
            shortTip("请选择楼盘类型");
            return;
        }
        String gardenName = silGardenName.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(gardenName)) {
            shortTip("请输入园区名称");
            return;
        }
        String gardenNo = silNo.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(gardenNo)) {
            shortTip("请输入楼号");
            return;
        }
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
        String floorStorey = silStorey.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(floorStorey)) {
            shortTip("请输入总楼层");
            return;
        }
        String completeTime = silCompleteTime.getContextView().getText().toString();
        if (TextUtils.isEmpty(completeTime)) {
            shortTip("请选择竣工时间");
            return;
        }
        String storeyHeight = silStoreyHeight.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(storeyHeight)) {
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
        silGardenName.setEditText("");
        silGardenName.setVisibility(View.VISIBLE);
        if (isOffice) {
            silGardenName.setTitle("写字楼名称");
            silGardenName.getEditTextView().setHint("请输入写字楼名称");
        } else {
            silGardenName.setTitle("园区名称");
            silGardenName.getEditTextView().setHint("请输入园区名称");
        }
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
        selectedDialog();
    }

    //图片删除
    @Override
    public void deleteUploadImage(ImageBean bean, int position) {
        if (isFastClick(1200)) {
            return;
        }
        //如果是网络图片
        if (bean.isNetImage()) {
            //删除网络图片成功 TODO
            //mPresenter.deleteImage(true, bean.getId(), position);
        } else {
            uploadImageList.remove(position);
            imageAdapter.notifyDataSetChanged();
        }
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
        uniqueTags = CommonHelper.getKey(uniqueMap);
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
                imageAdapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                for (int i = 0; i < images.size(); i++) {
                    ImageUtils.isSaveCropImageView(images.get(i));//图片处理
                    uploadImageList.add(uploadImageList.size() - 1, new ImageBean(false, 0, images.get(i)));
                }
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissions(context, requestCode, permissions, grantResults);
    }
}
