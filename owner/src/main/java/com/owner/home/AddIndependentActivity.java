package com.owner.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.dialog.RentDialog;
import com.officego.commonlib.common.model.BuildingManagerBean;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
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
import com.owner.adapter.UploadBuildingImageAdapter;
import com.owner.dialog.ConditionedDialog;
import com.owner.dialog.FloorTypeDialog;
import com.owner.home.contract.IndependentContract;
import com.owner.home.presenter.IndependentPresenter;
import com.owner.home.rule.AreaTextWatcher;
import com.owner.home.rule.CarFeeTextWatcher;
import com.owner.home.rule.FloorHeightTextWatcher;
import com.owner.home.rule.IntegerTextWatcher;
import com.owner.home.rule.TextCountsWatcher;
import com.owner.home.utils.CommonUtils;
import com.owner.identity.model.ImageBean;
import com.owner.zxing.QRScanActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/19
 **/
@EActivity(resName = "activity_home_independent_manager")
public class AddIndependentActivity extends BaseMvpActivity<IndependentPresenter>
        implements IndependentContract.View, RentDialog.SureClickListener,
        FloorTypeDialog.FloorListener, ConditionedDialog.ConditionedListener,
        UploadBuildingImageAdapter.UploadImageListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int TYPE_INTRODUCE = 1;
    private static final int TYPE_BANNER = 2;

    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "tv_upload_title")
    TextView tvUploadTitle;
    @ViewById(resName = "tv_des_title")
    TextView tvDesTitle;
    @ViewById(resName = "sil_title")
    SettingItemLayout silTitle;
    @ViewById(resName = "sil_seats")
    SettingItemLayout silSeats;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "sil_rent_single")
    SettingItemLayout silRentSingle;
    @ViewById(resName = "sil_floor_no")
    SettingItemLayout silFloorNo;
    //第几层 总楼层
    @ViewById(resName = "et_floors")
    EditText etFloors;
    @ViewById(resName = "tv_counts_floor")
    TextView tvCountsFloor;
    //租期
    @ViewById(resName = "sil_rent_time")
    SettingItemLayout silRentTime;
    @ViewById(resName = "sil_free_rent")
    SettingItemLayout silFreeRent;
    //空调
    @ViewById(resName = "sil_conditioned")
    SettingItemLayout silConditioned;
    @ViewById(resName = "sil_conditioned_fee")
    SettingItemLayout silConditionedFee;
    @ViewById(resName = "sil_car_num")
    SettingItemLayout silCarNum;
    @ViewById(resName = "sil_car_fee")
    SettingItemLayout silCarFee;
    @ViewById(resName = "sil_storey_height")
    SettingItemLayout silStoreyHeight;
    //介绍
    @ViewById(resName = "tv_counts")
    TextView tvCounts;
    @ViewById(resName = "cet_desc_content")
    ClearableEditText cetDescContent;
    //图片list
    @ViewById(resName = "rv_upload_image")
    RecyclerView rvUploadImage;
    //扫一扫
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;
    @ViewById(resName = "iv_desc_image")
    ImageView ivDescImage;
    //是否添加还是编辑
    @Extra
    int buildingFlag;
    @Extra
    BuildingManagerBean buildingManagerBean;
    //上传图片
    private List<ImageBean> uploadImageList = new ArrayList<>();
    private UploadBuildingImageAdapter imageAdapter;
    private String localImagePath;
    //删除的图片
    private List<String> deleteList = new ArrayList<>();
    //户型介绍图片
    private String introduceImageUrl;
    //图片上传类型
    private int mUploadType;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new IndependentPresenter();
        mPresenter.attachView(this);
        initViews();
        initDigits();
        if (buildingFlag == Constants.BUILDING_FLAG_EDIT) {
            mPresenter.getHouseEdit(buildingManagerBean.getBuildingId(), buildingManagerBean.getIsTemp());
        }
    }

    private void initViews() {
        titleBar.setAppTitle(buildingFlag == Constants.BUILDING_FLAG_ADD ? "添加独立办公室" : "编辑独立办公室");
        tvUploadTitle.setText("上传办公室图片");
        tvDesTitle.setText("户型格局介绍");
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
        localImagePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "addIndependentPath.jpg";
        //初始化图片默认添加一个
        uploadImageList.add(new ImageBean(false, 0, ""));
        imageAdapter = new UploadBuildingImageAdapter(context, uploadImageList);
        imageAdapter.setListener(this);
        rvUploadImage.setAdapter(imageAdapter);
    }

    private void initDigits() {
        //名称
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silTitle.getEditTextView(), 20);
        //工位数0-100
        silSeats.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 100, silSeats.getEditTextView()));
        //面积 0-10000
        silArea.getEditTextView().addTextChangedListener(new AreaTextWatcher(context, 10000, silArea.getEditTextView()));
        //租金100-100000
        silRentSingle.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 100000, silRentSingle.getEditTextView()));
        //最短租期
        silRentTime.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 60, silRentTime.getEditTextView()));
        //净高 层高 0-8或一位小数
        silStoreyHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(context, silStoreyHeight.getEditTextView()));
        //车位费0-5000整数
        silCarFee.getEditTextView().addTextChangedListener(new CarFeeTextWatcher(context, silCarFee.getEditTextView()));
        //介绍
        cetDescContent.addTextChangedListener(new TextCountsWatcher(tvCounts, cetDescContent));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
        submit();
    }

    private void submit() {
        String seats = silSeats.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(seats)) {
            shortTip("请输入工位数");
            return;
        }
        String rentSingle = silRentSingle.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(rentSingle)) {
            shortTip("请输入租金");
            return;
        }
        String floors = etFloors.getText().toString();
        if (TextUtils.isEmpty(floors)) {
            shortTip("请输入楼层");
            return;
        }
        String minimumLease = silRentTime.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(minimumLease)) {
            shortTip("请输入最短租期");
            return;
        }
        String conditioned = silConditioned.getContextView().getText().toString();
        if (TextUtils.isEmpty(conditioned)) {
            shortTip("请选择空调类型");
            return;
        }
        String storeyHeight = silStoreyHeight.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(storeyHeight)) {
            shortTip("请输入净高");
            return;
        }
        String title = silTitle.getEditTextView().getText().toString();
        //面积
        String area = silArea.getEditTextView().getText().toString();
        //净高
        String clearHeight = silStoreyHeight.getEditTextView().getText().toString();
        //免租期
        String freeRent = silFreeRent.getLeftToArrowTextView().getText().toString();
        //空调费
        String conditionedFee = silConditionedFee.getContextView().getText().toString();
        //介绍
        String buildingIntroduction = cetDescContent.getText() == null ? "" : cetDescContent.getText().toString();
        //封面图片
        String mainPic = uploadImageList.get(0).getPath();
        //添加图片
        String addImage = CommonUtils.addUploadImage(uploadImageList);
        //删除图片
        String deleteImage = CommonUtils.delUploadImage(deleteList);
        mPresenter.saveEdit(buildingManagerBean.getBuildingId(), buildingManagerBean.getIsTemp(), title,
                seats, area, rentSingle, floors, minimumLease, freeRent,
                conditioned, conditionedFee, clearHeight, buildingIntroduction,
                introduceImageUrl, mainPic, addImage, deleteImage);
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

    @Click(resName = "sil_free_rent")
    void freeRentOnClick() {
        new RentDialog(context, getString(R.string.str_free_rent)).setSureListener(this);
    }

    @Click(resName = "sil_conditioned")
    void conditionedOnClick() {
        new ConditionedDialog(context).setListener(this);
    }

    @Click(resName = "iv_desc_image")
    void layoutIntroduceOnClick() {
        mUploadType = TYPE_INTRODUCE;
        selectedDialog();
    }

    @Override
    public void selectedRent(String str) {
        silFreeRent.setLeftToArrowText(str);
    }

    @Override
    public void sureFloor(String text, String type) {
        silFloorNo.setLeftToArrowText(text);
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
        if (TYPE_BANNER == mUploadType) {
            ImageSelector.builder()
                    .useCamera(false) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setMaxSelectCount(10 - uploadImageList.size())
                    .canPreview(true) //是否可以预览图片，默认为true
                    .start(this, REQUEST_GALLERY); // 打开相册
        } else {
            ImageSelector.builder()
                    .useCamera(false) // 设置是否使用拍照
                    .setSingle(true)  //设置是否单选
                    .canPreview(true) //是否可以预览图片，默认为true
                    .start(this, REQUEST_GALLERY); // 打开相册
        }
    }

    private boolean isOverLimit() {
        if (TYPE_BANNER == mUploadType) {
            if (uploadImageList.size() >= 10) {
                shortTip(R.string.tip_image_upload_overlimit);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照
                ImageUtils.isSaveCropImageView(localImagePath);//图片处理
                if (TYPE_BANNER == mUploadType) {
                    uploadImageList.add(uploadImageList.size() - 1, new ImageBean(false, 0, localImagePath));
                    mPresenter.uploadImage(uploadImageList);
                } else {
                    mPresenter.uploadSingleImage(localImagePath);
                }
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                if (TYPE_BANNER == mUploadType) {
                    for (int i = 0; i < images.size(); i++) {
                        ImageUtils.isSaveCropImageView(images.get(i));//图片处理
                        uploadImageList.add(uploadImageList.size() - 1, new ImageBean(false, 0, images.get(i)));
                    }
                    mPresenter.uploadImage(uploadImageList);
                } else {
                    mPresenter.uploadSingleImage(images.get(0));//介绍图单张上传
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissions(context, requestCode, permissions, grantResults);
    }

    //图片上传
    @Override
    public void addUploadImage() {
        mUploadType = TYPE_BANNER;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void houseEditSuccess(HouseEditBean data) {
        if (data == null) return;
        if (data.getHouseMsg() != null) {
            //标题
            silTitle.getEditTextView().setText(data.getHouseMsg().getTitle());
            //工位
            silSeats.getEditTextView().setText(data.getHouseMsg().getSeats() + "");
            //面积
            silArea.getEditTextView().setText(data.getHouseMsg().getArea() + "");
            //租金
            silRentSingle.getEditTextView().setText(data.getHouseMsg().getMonthPrice() + "");
            //楼层
            etFloors.setText(data.getHouseMsg().getFloor());
            tvCountsFloor.setText("总" + "" + "层");
            //租期
            silRentTime.getEditTextView().setText(data.getHouseMsg().getMinimumLease());
            silFreeRent.getEditTextView().setText(data.getHouseMsg().getRentFreePeriod());
            //空调类型
            String ariCondition = data.getHouseMsg().getConditioningType();
            silConditioned.setCenterText(ariCondition);
            silConditionedFee.setVisibility(View.VISIBLE);
            if (ariCondition.contains("中央")) {
                silConditionedFee.setCenterText("包含在物业费内，加时另计");
            } else if (ariCondition.contains("独立")) {
                silConditionedFee.setCenterText("按电表计费");
            } else {
                silConditionedFee.setCenterText("无");
            }
            //车位数 车位费
//            silCarNum.setCenterText(data.getHouseMsg().getParkingSpace);
//            silCarFee.setCenterText(data.getHouseMsg().getParkingSpaceRent());
            //净高
            silStoreyHeight.getEditTextView().setText(data.getHouseMsg().getClearHeight());
            //介绍
            cetDescContent.setText(data.getHouseMsg().getUnitPatternRemark());
            //户型介绍图
            Glide.with(context).load(data.getHouseMsg().getUnitPatternImg()).into(ivDescImage);
            //办公室图片
            showImage(data);
        }
    }

    //办公室图片
    private void showImage(HouseEditBean data) {
        //封面图
        if (!TextUtils.isEmpty(data.getHouseMsg().getMainPic())) {
            uploadImageList.add(uploadImageList.size() - 1, new ImageBean(true, 0, data.getHouseMsg().getMainPic()));
        }
        //浏览图
        for (int i = 0; i < data.getBanner().size(); i++) {
            uploadImageList.add(uploadImageList.size() - 1, new ImageBean(true, 0, data.getBanner().get(i).getImgUrl()));
        }
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void uploadSuccess(boolean isIntroduceLayout, UploadImageBean data) {
        if (data != null && data.getUrls() != null && data.getUrls().size() > 0) {
            int urlSize = data.getUrls().size();
            if (isIntroduceLayout) {//户型介绍
                introduceImageUrl = data.getUrls().get(0).getUrl();
                Glide.with(context).load(introduceImageUrl).into(ivDescImage);
            } else {//多图
                ImageBean bean;
                for (int i = 0; i < urlSize; i++) {
                    bean = new ImageBean(true, 0, data.getUrls().get(i).getUrl());
                    uploadImageList.set(uploadImageList.size() - 1 - urlSize + i, bean);
                }
                imageAdapter.notifyDataSetChanged();
            }
            shortTip("上传成功");
        }
    }

    @Override
    public void editSaveSuccess() {
        finish();
        UploadVideoVrActivity_.intent(context).start();
    }
}
