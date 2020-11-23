package com.owner.identity2;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.dialog.AreaDialog;
import com.owner.identity.RequestPermissionsResult;
import com.owner.identity2.contract.CreateBuildingContract;
import com.owner.identity2.model.BuildingBean;
import com.owner.identity2.presenter.CreateBuildingPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/18
 **/
@EActivity(resName = "activity_create_building_jointwork")
public class OwnerCreateBuildingActivity extends BaseMvpActivity<CreateBuildingPresenter>
        implements CreateBuildingContract.View, AreaDialog.AreaSureListener,
        RequestPermissionsResult.PermissionsListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "sil_name")
    SettingItemLayout silName;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "sil_address")
    SettingItemLayout silAddress;
    @ViewById(resName = "iv_image")
    ImageView ivImage;
    @ViewById(resName = "btn_save")
    Button btnSave;
    @ViewById(resName = "rb_building")
    RadioButton rbBuilding;
    @ViewById(resName = "rb_joint_work")
    RadioButton rbJointWork;

    @Extra
    String inputText;
    @Extra
    boolean isEdit;
    @Extra
    BuildingBean buildingBean;

    private int district, business;
    private String localBuildingPath;
    private Uri localPhotoUri;
    private String name, address;
    private String introduceImageUrl;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new CreateBuildingPresenter();
        mPresenter.attachView(this);
        titleBar.getLeftImg().setOnClickListener(view -> onBackPressed());
        initViews();
    }

    private void initViews() {
        if (isEdit) {
            if (buildingBean != null) {
                silName.getEditTextView().setText(buildingBean.getName());
                rbBuilding.setChecked(buildingBean.getBuildingType() == Constants.TYPE_BUILDING);
                rbJointWork.setChecked(buildingBean.getBuildingType() == Constants.TYPE_JOINTWORK);
                silArea.setCenterText(buildingBean.getArea());
                silAddress.getEditTextView().setText(buildingBean.getAddress());
                if (!TextUtils.isEmpty(buildingBean.getMainPic())) {
                    introduceImageUrl = buildingBean.getMainPic();
                    Glide.with(context).load(introduceImageUrl).into(ivImage);
                }
                district = buildingBean.getDistrictId();
                business = buildingBean.getBusinessId();
            }
        } else {
            silName.getEditTextView().setText(inputText);
            silName.getEditTextView().setSelection(TextUtils.isEmpty(inputText) ? 0 : inputText.length());
        }
    }

    @Click(resName = "sil_area")
    void areaClick() {
        new AreaDialog(context, district, business).setListener(this);
    }

    @Click(resName = "iv_image")
    void buildingIntroduceClick() {
        selectedDialog();
    }

    @Click(resName = "btn_save")
    void saveClick() {
        name = silName.getEditTextView() == null ? "" : silName.getEditTextView().getText().toString();
        String area = silArea.getContextView() == null ? "" : silArea.getContextView().getText().toString();
        address = silAddress.getEditTextView() == null ? "" : silAddress.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.toastForShort(context, "请输入楼盘/网点名称");
            return;
        }
        if (!rbBuilding.isChecked() && !rbJointWork.isChecked()) {
            ToastUtils.toastForShort(context, "请选择类型");
            return;
        }
        if (TextUtils.isEmpty(area)) {
            ToastUtils.toastForShort(context, "请选择区域");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.toastForShort(context, "请输入详细地址");
            return;
        }
        if (TextUtils.isEmpty(introduceImageUrl)) {
            shortTip("请上传封面图");
            return;
        }
        //save
        BuildingBean bean = new BuildingBean();
        bean.setName(name);
        bean.setBuildingType(rbBuilding.isChecked() ? Constants.TYPE_BUILDING : Constants.TYPE_JOINTWORK);
        bean.setDistrictId(district);
        bean.setBusinessId(business);
        bean.setAddress(address);
        bean.setMainPic(introduceImageUrl);
        bean.setArea(area);
        Intent intent = getIntent();
        intent.putExtra("buildingMessage", bean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void AreaSure(String area, int district, int business) {
        silArea.setCenterText(area);
        this.district = district;
        this.business = business;
    }

    @Override
    public void onBackPressed() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("确认离开吗？")
                .setMessage("楼盘/网点未创建成功，点击离开，已编辑信息不保存")
                .setCancelButton(R.string.sm_cancel)
                .setConfirmButton(R.string.str_go_away, (dialog12, which) -> {
                    super.onBackPressed();
                }).create();
        dialog.showWithOutTouchable(false);
    }

    private void selectedDialog() {
        final String[] items = {"拍照", "相册"};
        new AlertDialog.Builder(this)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        takePhoto();
                    } else if (i == 1) {
                        openGallery();
                    }
                }).create().show();
    }

    private void takePhoto() {
        if (!PermissionUtils.checkSDCardCameraPermission(this)) {
            return;
        }
        if (!FileUtils.isSDExist()) {
            shortTip(R.string.str_no_sd);
            return;
        }
        localBuildingPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "buildingdec.jpg";
        File fileUri = new File(localBuildingPath);
        localPhotoUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            localPhotoUri = FileProvider.getUriForFile(this, Constants.FILE_PROVIDER_AUTHORITY, fileUri);
        }
        PhotoUtils.takePicture(this, localPhotoUri, REQUEST_CAMERA);
    }

    private void openGallery() {
        if (!PermissionUtils.checkStoragePermission(this)) {
            return;
        }
        ImageSelector.builder()
                .useCamera(false) // 设置是否使用拍照
                .setSingle(true)  //设置是否单选
                .canPreview(true) //是否可以预览图片，默认为true
                .start(this, REQUEST_GALLERY); // 打开相册
    }

    @Override
    public void uploadSuccess(UploadImageBean data) {
        if (data != null && data.getUrls() != null && data.getUrls().size() > 0) {
            introduceImageUrl = data.getUrls().get(0).getUrl();
            Glide.with(context).load(data.getUrls().get(0).getUrl()).into(ivImage);
            shortTip("上传成功");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照
                uploadSingleImage(localBuildingPath);
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                localBuildingPath = images.get(0);
                uploadSingleImage(localBuildingPath);
            }
        }
    }

    //单张图片处理
    private void uploadSingleImage(String path) {
        ImageUtils.isSaveCropImageView(path);
        mPresenter.uploadImage(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new RequestPermissionsResult(context, requestCode, permissions, grantResults).setListener(this);
    }

    @Override
    public void gotoTakePhoto() {
        takePhoto();
    }

    @Override
    public void gotoOpenGallery() {
        openGallery();
    }

}
