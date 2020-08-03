package com.owner.identity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.RoundImageView;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.identity.contract.CreateSubmitContract;
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
@EActivity(resName = "activity_id_company_create")
public class CreateCompanyActivity extends BaseMvpActivity<CreateSubmitPresenter>
        implements CreateSubmitContract.View,
        RequestPermissionsResult.PermissionsListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;

    private String localLicensePath;
    private Uri localPhotoUri;
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "et_name_content")
    ClearableEditText etNameContent;
    @ViewById(resName = "et_address_content")
    ClearableEditText etAddressContent;
    @ViewById(resName = "et_register_no_content")
    ClearableEditText etRegisterNoContent;
    @ViewById(resName = "riv_image")
    RoundImageView rivImage;
    @ViewById(resName = "tv_upload")
    TextView tvUpload;
    @ViewById(resName = "btn_save")
    Button btnSave;
    @Extra
    int createCompany;
    @Extra
    int identityType;
    //联办创建公司--关联的名称和地址
    @Extra
    String relevanceCompanyName;
    @Extra
    String relevanceCompanyAddress;
    @Extra
    boolean isEdit;
    private String name, address, regNo;
    //是否从相机拍照或相册选择了图片
    private boolean isTakePhotoOrGallery;

    @AfterViews
    void init() {
        mPresenter = new CreateSubmitPresenter();
        mPresenter.attachView(this);
        StatusBarUtils.setStatusBarColor(this);
        titleBar.getLeftImg().setOnClickListener(view -> onBackPressed());
        setImageViewLayoutParams(context, rivImage);
        if (!TextUtils.isEmpty(relevanceCompanyName)) {
            //有html标签 恒源<strong style='color:#06d2e7'>大<\/strong>楼的
            if (relevanceCompanyName.contains("<strong style='color:#06d2e7'>")) {
                String name = relevanceCompanyName.replace("<strong style='color:#06d2e7'>", "");
                String name1 = name.replace("</strong>", "");
                etNameContent.setText(name1);
            } else {
                etNameContent.setText(relevanceCompanyName);
            }
        }
        if (!TextUtils.isEmpty(relevanceCompanyAddress)) {
            if (relevanceCompanyAddress.contains("<strong style='color:#06d2e7'>")) {
                String name = relevanceCompanyAddress.replace("<strong style='color:#06d2e7'>", "");
                String name1 = name.replace("</strong>", "");
                etAddressContent.setText(name1);
            } else {
                etAddressContent.setText(relevanceCompanyAddress);
            }
        }
        //初始化
        if (isEdit) {
            mPresenter.getIdentityInfo(identityType, true);
        }
    }

    public static void setImageViewLayoutParams(Context context, View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = CommonHelper.getScreenWidth(context) * 3 / 4;
        view.setLayoutParams(params);
    }

    @Click(resName = "rl_license")
    void licenseImageClick() {
        selectedDialog();
    }

    @Click(resName = "btn_save")
    void saveClick() {
        name = etNameContent.getText() == null ? "" : etNameContent.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            shortTip("请输入公司名称");
            return;
        }
        address = etAddressContent.getText() == null ? "" : etAddressContent.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            shortTip("请输入公司地址");
            return;
        }
        regNo = etRegisterNoContent.getText() == null ? "" : etRegisterNoContent.getText().toString().trim();
        if (TextUtils.isEmpty(regNo)) {
            shortTip("请输入营业执照注册号");
            return;
        }
        if (!isTakePhotoOrGallery) {
            shortTip("请上传营业执照");
            return;
        }
        //获取提交公司的信息
        mPresenter.getIdentityInfo(identityType, false);
    }

    @Override
    public void onBackPressed() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("确认离开吗？")
                .setMessage("公司未创建成功，点击离开，已编辑信息不保存")
                .setCancelButton(R.string.sm_cancel)
                .setConfirmButton(R.string.str_go_away, (dialog12, which) -> {
                    super.onBackPressed();
                }).create();
        dialog.showWithOutTouchable(false);
    }

    private void selectedDialog() {
        final String[] items = {"拍照", "相册"};
        new AlertDialog.Builder(CreateCompanyActivity.this)
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
        localLicensePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "businessLicense.jpg";
        File fileUri = new File(localLicensePath);
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

    private void hideView() {
        tvUpload.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            hideView();
            if (requestCode == REQUEST_CAMERA) {//拍照
                isTakePhotoOrGallery = true;
                ImageUtils.isSaveCropImageView(localLicensePath);//图片处理
                rivImage.setImageBitmap(BitmapFactory.decodeFile(localLicensePath));
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                isTakePhotoOrGallery = true;
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                localLicensePath = images.get(0);
                ImageUtils.isSaveCropImageView(localLicensePath);//图片处理
                rivImage.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
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
            isTakePhotoOrGallery = true;
            etNameContent.setText(data.getCompany());
            etAddressContent.setText(data.getAddress());
            etRegisterNoContent.setText(data.getCreditNo());
            Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(data.getBusinessLicense()).into(rivImage);
            return;
        }
        //提交信息
        mPresenter.submitCompany(data, createCompany, identityType, name, address, regNo, localLicensePath);
    }

    @Override
    public void districtListSuccess(String str) {

    }

    @Override
    public void submitSuccess() {
        shortTip(R.string.tip_create_success);
        Intent intent = getIntent();
        intent.putExtra("companyName", name);
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
