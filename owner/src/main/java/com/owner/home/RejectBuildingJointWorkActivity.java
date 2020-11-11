package com.owner.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.owner.RejectBuildingBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.SearchBuildingAdapter;
import com.owner.adapter.UploadAddImageAdapter;
import com.owner.dialog.AreaDialog;
import com.owner.home.contract.AddContract;
import com.owner.home.presenter.AddPresenter;
import com.owner.home.utils.CommonUtils;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.ImageBean;
import com.owner.utils.CommUtils;
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
 * Date 2020/11/3
 **/
@EActivity(resName = "activity_home_reject_manager")
public class RejectBuildingJointWorkActivity extends BaseMvpActivity<AddPresenter>
        implements AddContract.View, AreaDialog.AreaSureListener,
        SearchBuildingAdapter.IdentityBuildingListener,
        UploadAddImageAdapter.UploadImageListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int TYPE_INTRODUCE = 1;
    private static final int TYPE_BANNER = 2;
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "rl_reason")
    RelativeLayout rlReason;
    @ViewById(resName = "tv_reason")
    TextView tvReason;
    @ViewById(resName = "sil_name")
    SettingItemLayout silName;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "sil_address")
    SettingItemLayout silAddress;
    @ViewById(resName = "tv_set_first_image")
    TextView tvSetFirstImage;
    @ViewById(resName = "iv_desc_image")
    ImageView ivDescImage;
    @ViewById(resName = "rv_upload_image")
    RecyclerView rvUploadImage;
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;
    @ViewById(resName = "tv_upload_title")
    TextView tvUploadTitle;
    @ViewById(resName = "rv_recommend_building")
    RecyclerView rvRecommendBuilding;

    @Extra
    int flay;
    @Extra
    int buildingId;
    //区域
    private int district, business;
    //上传图片
    private List<ImageBean> uploadImageList = new ArrayList<>();
    private UploadAddImageAdapter imageAdapter;
    private String localImagePath;
    //户型介绍图片
    private String introduceImageUrl;
    //图片上传类型
    private int mUploadType;
    //搜索
    private SearchBuildingAdapter buildingAdapter;
    private List<IdentityBuildingBean.DataBean> mList = new ArrayList<>();
    //是否关联的楼
    private boolean isCreateBuilding;
    private int mBuildId;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new AddPresenter();
        mPresenter.attachView(this);
        initViews();
        mPresenter.rejectBuildingMsg(buildingId);
    }

    private void initViews() {
        titleBar.setAppTitle(flay == 0 ? "重新认证" : "重新认证");
        silName.setTitle(flay == 0 ? "网点名称" : "楼盘名称");
        silName.getEditTextView().setHint(flay == 0 ? "请输入网点名称" : "请输入楼盘名称");
        silName.getEditTextView().setHintTextColor(ContextCompat.getColor(context, R.color.text_66_p50));
        tvUploadTitle.setText("上传房产证");
        //位置
        int reasonHeight = rlReason.getLayoutParams().height;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rvRecommendBuilding.getLayoutParams();
        params.topMargin = (int) (reasonHeight + context.getResources().getDimension(R.dimen.dp_106));
        rvRecommendBuilding.setLayoutParams(params);
        //搜索列表
        LinearLayoutManager buildingManager = new LinearLayoutManager(context);
        rvRecommendBuilding.setLayoutManager(buildingManager);
        //上传图片
        GridLayoutManager layoutManager2 = new GridLayoutManager(context, 3);
        layoutManager2.setSmoothScrollbarEnabled(true);
        layoutManager2.setAutoMeasureEnabled(true);
        rvUploadImage.setLayoutManager(layoutManager2);
        rvUploadImage.setNestedScrollingEnabled(false);
        initData();
        searchBuilding();
    }

    private void initData() {
        //初始化本地图路径
        localImagePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "addBuildingJointWorkPath.jpg";
        //初始化图片默认添加一个
        uploadImageList.add(new ImageBean(false, 0, ""));
        imageAdapter = new UploadAddImageAdapter(context, uploadImageList);
        imageAdapter.setListener(this);
        rvUploadImage.setAdapter(imageAdapter);
    }

    @Click(resName = "iv_close_scan")
    void closeScanOnClick() {
        btnScan.setVisibility(View.GONE);
        ivCloseScan.setVisibility(View.GONE);
    }

    @Click(resName = "iv_desc_image")
    void layoutIntroduceOnClick() {
        mUploadType = TYPE_INTRODUCE;
        selectedDialog();
    }

    @Click(resName = "sil_area")
    void areaOnClick() {
        if (isCreateBuilding) {
            new AreaDialog(context, district, business).setListener(this);
        }
    }

    @Click(resName = "btn_scan")
    void toWebEditOnClick() {
        if (!PermissionUtils.checkSDCardCameraPermission(this)) {
            return;
        }
        startActivity(new Intent(context, QRScanActivity.class));
    }

    @Click(resName = "btn_next")
    void saveOnClick() {
        String name = silName.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(name)) {
            shortTip("请输入名称");
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
        if (isCreateBuilding) {
            if (TextUtils.isEmpty(introduceImageUrl)) {
                shortTip("请上传封面图");
                return;
            }
        }
        if (uploadImageList == null || uploadImageList.size() <= 1) {
            shortTip("请上传房产证");
            return;
        }
        //添加图片
        String addImage = CommonUtils.addAllUploadImage(uploadImageList);
        mPresenter.addRejectBuilding(flay == 0 ? Constants.TYPE_JOINTWORK : Constants.TYPE_BUILDING,
                name, district, business, address, introduceImageUrl, addImage, mBuildId, buildingId);
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
        uploadImageList.remove(position);
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
                    mPresenter.uploadImage(Constants.TYPE_IMAGE_BUILDING, uploadImageList);
                } else {
                    mPresenter.uploadSingleImage(Constants.TYPE_IMAGE_BUILDING, localImagePath);
                }
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                if (TYPE_BANNER == mUploadType) {
                    for (int i = 0; i < images.size(); i++) {
                        ImageUtils.isSaveCropImageView(images.get(i));//图片处理
                        uploadImageList.add(uploadImageList.size() - 1, new ImageBean(false, 0, images.get(i)));
                    }
                    mPresenter.uploadImage(Constants.TYPE_IMAGE_BUILDING, uploadImageList);
                } else {
                    ImageUtils.isSaveCropImageView(images.get(0));//图片处理
                    mPresenter.uploadSingleImage(Constants.TYPE_IMAGE_BUILDING, images.get(0));//介绍图单张上传
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

    @Override
    public void uploadSuccess(boolean isIntroduceLayout, UploadImageBean data) {
        if (data != null && data.getUrls() != null && data.getUrls().size() > 0) {
            int urlSize = data.getUrls().size();
            if (isIntroduceLayout) {//封面
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
    public void AreaSure(String area, int district, int business) {
        this.district = district;
        this.business = business;
        silArea.setCenterText(area);
    }

    //search
    private void searchBuilding() {
        silName.getEditTextView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rvRecommendBuilding.setVisibility(View.VISIBLE);
                mPresenter.searchBuilding(flay, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data) {
        mList.clear();
        mList.addAll(data);
        mList.add(data.size(), new IdentityBuildingBean.DataBean());
        if (buildingAdapter == null) {
            buildingAdapter = new SearchBuildingAdapter(context, mList, flay == 0);
            buildingAdapter.setListener(this);
            rvRecommendBuilding.setAdapter(buildingAdapter);
            return;
        }
        buildingAdapter.setData(mList);
        buildingAdapter.notifyDataSetChanged();
    }

    @Override
    public void addSuccess() {
        finish();
        shortTip("提交成功");
        BaseNotification.newInstance().postNotificationName(
                CommonNotifications.updateBuildingSuccess, "updateBuildingSuccess");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void rejectBuildingResultSuccess(RejectBuildingBean data) {
        boolean isBuildId = TextUtils.isEmpty(data.getBuildId()) || TextUtils.equals("0", data.getBuildId());
        isCreateBuilding = isBuildId;//0创建的 1关联的
        tvReason.setText("驳回原因：" + data.getRemark());
        silName.getEditTextView().setText(data.getBuildingName());
        silAddress.getEditTextView().setText(data.getAddress());
        silAddress.getEditTextView().setEnabled(isBuildId);
        tvSetFirstImage.setVisibility(isBuildId ? View.VISIBLE : View.GONE);
        //封面图
        if (!TextUtils.isEmpty(data.getMainPic())) {
            introduceImageUrl = data.getMainPic();
            Glide.with(context).load(introduceImageUrl).into(ivDescImage);
        }
        //产证
        for (int i = 0; i < data.getBuildingCardTemp().size(); i++) {
            String path = data.getBuildingCardTemp().get(i).getImgUrl();
            uploadImageList.add(uploadImageList.size() - 1, new ImageBean(true, 0, path));
        }
        imageAdapter.notifyDataSetChanged();
        //获取区域名称
        mPresenter.getDistrictList(data.getDistrictId(), data.getBusinessDistrict());
        rvRecommendBuilding.setVisibility(View.GONE);
    }

    @Override
    public void districtListSuccess(String str, String districtName, String businessName) {
        silArea.setCenterText(str);
    }

    @Override
    public void associateBuilding(IdentityBuildingBean.DataBean bean, boolean isCreate) {
        isCreateBuilding = isCreate;
        if (isCreate) {
            mBuildId = 0;
            silArea.getContextView().setText("");
            silAddress.getEditTextView().setText("");
        } else {
            CommUtils.showHtmlView(silName.getEditTextView(), bean.getBuildingName());
            CommUtils.showHtmlTextView(silArea.getContextView(), bean.getDistrict());
            CommUtils.showHtmlTextView(silAddress.getEditTextView(), bean.getAddress());
            introduceImageUrl = bean.getMainPic();
            mBuildId = bean.getBid();
        }
        silAddress.getEditTextView().setEnabled(isCreate);
        rvRecommendBuilding.setVisibility(View.GONE);
        tvSetFirstImage.setVisibility(isCreate ? View.VISIBLE : View.GONE);
        ivDescImage.setVisibility(isCreate ? View.VISIBLE : View.GONE);
    }
}
