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
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.EditInputFilter;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.JointCompanyAdapter;
import com.owner.adapter.ServiceLogoAdapter;
import com.owner.adapter.UniqueAdapter;
import com.owner.adapter.UploadBuildingImageAdapter;
import com.owner.dialog.AreaDialog;
import com.owner.dialog.ConditionedDialog;
import com.owner.dialog.FloorTypeDialog;
import com.owner.dialog.ServiceSelectedDialog;
import com.owner.home.contract.JointWorkContract;
import com.owner.home.presenter.JointWorkPresenter;
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
@EActivity(resName = "activity_home_jointwork_manager")
public class AddJointWorkActivity extends BaseMvpActivity<JointWorkPresenter>
        implements JointWorkContract.View, ServiceSelectedDialog.ServiceLogoListener,
        FloorTypeDialog.FloorListener, UniqueAdapter.UniqueListener,
        AreaDialog.AreaSureListener, UploadBuildingImageAdapter.UploadImageListener,
        ConditionedDialog.ConditionedListener {

    private final int serviceMeetingFlay = 0;
    private final int serviceCompanyFlay = 1;
    private final int serviceBaseFlay = 2;
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;

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
    private Map<Integer, String> uniqueMap;
    //上传图片
    private List<ImageBean> uploadImageList = new ArrayList<>();
    private UploadBuildingImageAdapter imageAdapter;
    private String localImagePath;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new JointWorkPresenter();
        mPresenter.attachView(this);
        initViews();
        initDigits();
        mPresenter.getBranchUnique();
    }

    private void initViews() {
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
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silCarFee.getEditTextView(), 20);
        //电梯0-20整数
        etCustomerLift.addTextChangedListener(new LiftTextWatcher(context, etCustomerLift));
        etPassengerLift.addTextChangedListener(new LiftTextWatcher(context, etPassengerLift));
        //会议室数量
        silMeetingRoom.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 10, silMeetingRoom.getEditTextView()));
        //最多容纳人数
        silContainsPersons.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 10, silContainsPersons.getEditTextView()));
        //介绍
        cetDescContent.addTextChangedListener(new TextCountsWatcher(tvCounts, cetDescContent));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
//        UploadVideoVrActivity_.intent(context).start();
        submit();
    }

    private void submit() {
        String jointWorkName = silJointWorkName.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(jointWorkName)) {
            shortTip("请输入网点名称");
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
        String floorNo = silFloorNo.getLeftToArrowTextView().getText().toString();
        if (TextUtils.isEmpty(floorNo)) {
            shortTip("请选择所在楼层");
            return;
        }
        String floors = etFloors.getText().toString();
        if (TextUtils.isEmpty(floors)) {
            shortTip("请输入第N层或第M-N层");
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

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        UniqueAdapter adapter = new UniqueAdapter(context, uniqueMap, data);
        adapter.setListener(this);
        rvHouseUnique.setAdapter(adapter);
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
    public void serviceLogoResult(int flay, Map<Integer, String> mapLogo, List<DirectoryBean.DataBean> list) {
        // String mStrLogo = CommonHelper.getKey(mapLogo);
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
