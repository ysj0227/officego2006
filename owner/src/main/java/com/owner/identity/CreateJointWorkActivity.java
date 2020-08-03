package com.owner.identity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.identity.contract.CreateSubmitContract;
import com.owner.identity.dialog.AreaDialog;
import com.owner.identity.model.GetIdentityInfoBean;
import com.owner.identity.presenter.CreateSubmitPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/7/14.
 * Descriptions:
 **/
@EActivity(resName = "activity_id_jointwork_create")
public class CreateJointWorkActivity extends BaseMvpActivity<CreateSubmitPresenter>
        implements CreateSubmitContract.View, AreaDialog.AreaSureListener,
        RequestPermissionsResult.PermissionsListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;

    private String localCoverImagePath;
    private Uri localPhotoUri;
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "et_name_content")
    ClearableEditText etNameContent;
    @ViewById(resName = "tv_area")
    TextView tvArea;
    @ViewById(resName = "rl_area")
    RelativeLayout rlArea;
    @ViewById(resName = "et_address_content")
    ClearableEditText etAddressContent;
    @ViewById(resName = "iv_image")
    ImageView ivImage;
    @ViewById(resName = "btn_save")
    Button btnSave;

    @Extra
    String mJointWorkName;
    @Extra
    boolean isEdit;
    private int district, business;
    private String name, address;
    //是否从相机拍照或相册选择了图片
    private boolean isTakePhotoOrGallery;

    @AfterViews
    void init() {
        mPresenter = new CreateSubmitPresenter();
        mPresenter.attachView(this);
        StatusBarUtils.setStatusBarColor(this);
        titleBar.getLeftImg().setOnClickListener(view -> onBackPressed());
        etNameContent.setText(mJointWorkName);
        if (isEdit) {
            mPresenter.getIdentityInfo(Constants.TYPE_IDENTITY_JOINT_WORK, true);
        }
    }

    @Click(resName = "iv_image")
    void imageClick() {
        selectedDialog();
    }

    @Click(resName = "rl_area")
    void areaClick() {
        new AreaDialog(context, district, business).setListener(this);
    }

    @Click(resName = "btn_save")
    void saveClick() {
        name = etNameContent.getText() == null ? "" : etNameContent.getText().toString().trim();
        String area = tvArea.getText() == null ? "" : tvArea.getText().toString().trim();
        address = etAddressContent.getText() == null ? "" : etAddressContent.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.toastForShort(context, "请输入网点名称");
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
        if (!isTakePhotoOrGallery) {
            shortTip("请上传网点封面图");
            return;
        }
        mPresenter.getIdentityInfo(Constants.TYPE_IDENTITY_JOINT_WORK, false);
    }


    @Override
    public void AreaSure(String area, int district, int business) {
        tvArea.setText(area);
        this.district = district;
        this.business = business;
    }

    @Override
    public void onBackPressed() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("确认离开吗？")
                .setMessage("网点未创建成功，点击离开，已编辑信息不保存")
                .setCancelButton(R.string.sm_cancel)
                .setConfirmButton(R.string.str_go_away, (dialog12, which) -> {
                    super.onBackPressed();
                }).create();
        dialog.showWithOutTouchable(false);
    }

    private void selectedDialog() {
        final String[] items = {"拍照", "相册"};
        new AlertDialog.Builder(CreateJointWorkActivity.this)
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
        localCoverImagePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "cover_image.jpg";
        File fileUri = new File(localCoverImagePath);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照
                isTakePhotoOrGallery = true;
                ImageUtils.isSaveCropImageView(localCoverImagePath);//图片处理
                ivImage.setImageBitmap(BitmapFactory.decodeFile(localCoverImagePath));
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                isTakePhotoOrGallery = true;
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                localCoverImagePath = images.get(0);
                ImageUtils.isSaveCropImageView(localCoverImagePath);//图片处理
                ivImage.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new RequestPermissionsResult(context, requestCode, permissions, grantResults).setListener(this);
    }

    @Override
    public void getIdentityInfoSuccess(GetIdentityInfoBean data, boolean isFirstGetInfo) {
        if (isEdit && isFirstGetInfo) {
            etNameContent.setText(data.getBranchesName());
            etAddressContent.setText(data.getBuildingAddress());
            if (!TextUtils.isEmpty(data.getDistrict())) {
                district = Integer.valueOf(data.getDistrict());
            }
            if (!TextUtils.isEmpty(data.getBusiness())) {
                business = Integer.valueOf(data.getBusiness());
            }
            if (!TextUtils.isEmpty(data.getMainPic())) {
                isTakePhotoOrGallery = true;
                Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(data.getMainPic()).into(ivImage);
            }
            mPresenter.getDistrictList(data.getDistrict(), data.getBusiness());
            return;
        }
        mPresenter.submitJointWork(data, Constants.TYPE_CREATE_FROM_JOINT_BUILDING, Constants.TYPE_IDENTITY_JOINT_WORK,
                name, address, district, business, localCoverImagePath);
    }

    @Override
    public void districtListSuccess(String str) {
        tvArea.setText(str);
    }

    @Override
    public void submitSuccess() {
        shortTip(R.string.tip_create_success);
        Intent intent = getIntent();
        intent.putExtra("jointworkName", name);
        intent.putExtra("jointworkAddress", address);
        setResult(RESULT_OK, intent);
        finish();
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
