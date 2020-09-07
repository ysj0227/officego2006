package com.owner.mine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.CircleImage;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.TitleBarView;
import com.owner.R;
import com.owner.mine.contract.UpdateUserContract;
import com.owner.mine.model.UserOwnerBean;
import com.owner.mine.presenter.UpdateUserPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by YangShiJie
 * Data 2020/5/18.
 * Descriptions:
 **/
@SuppressLint("Registered")
@EActivity(resName = "message_activity_persion")
public class MineMessageActivity extends BaseMvpActivity<UpdateUserPresenter>
        implements UpdateUserContract.View, View.OnClickListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int REQUEST_CROP_RESULT = 0xa2;
    private static final int IMAGE_DIMENSION = 480;
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "civ_avatar")
    CircleImage civAvatar;
    @ViewById(resName = "et_sex_content")
    TextView etSexContent;
    @ViewById(resName = "et_name_content")
    ClearableEditText etNameContent;
    @ViewById(resName = "et_company_content")
    ClearableEditText etCompanyContent;
    @ViewById(resName = "et_job_content")
    ClearableEditText etJobContent;
    @ViewById(resName = "et_wx_content")
    ClearableEditText etWxContent;

    private String localAvatarPath;
    private Uri localPhotoUri;
    private File imageFile;
    private String avatarUrl;
    @Extra
    UserOwnerBean mUserInfo;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new UpdateUserPresenter();
        mPresenter.attachView(this);
        titleBar.getLeftImg().setOnClickListener(this);
        localAvatarPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "_avatar.jpg";
        if (mUserInfo != null) {
            if (!TextUtils.isEmpty(mUserInfo.getAvatar())) {
                Glide.with(context).load(mUserInfo.getAvatar()).into(civAvatar);
            }
            etNameContent.setText(mUserInfo.getProprietorRealname());
            etCompanyContent.setText(mUserInfo.getProprietorCompany());
            if (mUserInfo.getSex() != null) {
                etSexContent.setText((Double) mUserInfo.getSex() == 1 ? "男" : "女");
            }
            etJobContent.setText(TextUtils.isEmpty(mUserInfo.getProprietorJob()) ? "" : mUserInfo.getProprietorJob());
            etWxContent.setText(TextUtils.isEmpty(mUserInfo.getWxId()) ? "" : mUserInfo.getWxId());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        intent.putExtra("avatarUrl", avatarUrl);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Click(resName = "civ_avatar")
    void avatarClick() {
        chooseImage();
    }

    @Click(resName = "rl_sex")
    void sexClick() {
        chooseSex();
    }

    @Click(resName = "btn_save")
    void saveClick() {
        if (TextUtils.isEmpty(etSexContent.getText().toString())) {
            shortTip("请选择性别");
            return;
        }
        String nikeName = TextUtils.isEmpty(etNameContent.getText()) ? "" : etNameContent.getText().toString().trim();
        String sex = etSexContent.getText().toString().trim();
        String company = TextUtils.isEmpty(etCompanyContent.getText()) ? "" : etCompanyContent.getText().toString().trim();
        String job = TextUtils.isEmpty(etJobContent.getText()) ? "" : etJobContent.getText().toString().trim();
        String wx = TextUtils.isEmpty(etWxContent.getText()) ? "" : etWxContent.getText().toString().trim();
        mPresenter.UpdateUserInfo(nikeName, TextUtils.equals("男", sex) ? "1" : "0", company, job, wx);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQ_PERMISSIONS_CAMERA_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (FileUtils.isSDExist()) {
                        takePhoto();
                    } else {
                        shortTip(getString(R.string.str_no_sd));
                    }
                } else {
                    shortTip(getString(R.string.str_please_open_camera));
                }
                break;
            case PermissionUtils.REQ_PERMISSIONS_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, REQUEST_GALLERY);
                } else {
                    shortTip(getString(R.string.str_please_open_sd));
                }
                break;
            default:
        }
    }

    private void chooseImage() {
        final String[] items = {"拍照", "从相册选择"};
        new AlertDialog.Builder(this)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        takePhoto();
                    } else if (i == 1) {
                        openGallery();
                    }
                }).create().show();
    }

    private void chooseSex() {
        etSexContent.setFocusable(false);
        final String[] items = {"男", "女"};
        new AlertDialog.Builder(this)
                .setItems(items, (dialogInterface, i) -> {
                    etSexContent.setText(items[i]);
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
        File fileUri = new File(FileHelper.SDCARD_CACHE_IMAGE_PATH + "avatar.jpg");
        localPhotoUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            localPhotoUri = FileProvider.getUriForFile(this,
                    Constants.FILE_PROVIDER_AUTHORITY, fileUri);
        }
        PhotoUtils.takePicture(this, localPhotoUri, REQUEST_CAMERA);
    }

    private void openGallery() {
        if (!PermissionUtils.checkStoragePermission(this)) {
            return;
        }
        PhotoUtils.openPic(this, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageFile = new File(localAvatarPath);
            Uri cropImageUri;
            switch (requestCode) {
                case REQUEST_CAMERA:
                    //裁剪显示
                    cropImageUri = Uri.fromFile(imageFile);
                    PhotoUtils.cropImageUri(this, localPhotoUri, cropImageUri, 1, 1,
                            IMAGE_DIMENSION, IMAGE_DIMENSION, REQUEST_CROP_RESULT);
                    break;
                case REQUEST_GALLERY:
                    if (FileUtils.isSDExist()) {
                        cropImageUri = Uri.fromFile(imageFile);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(context, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(context,
                                    Constants.FILE_PROVIDER_AUTHORITY, new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1,
                                IMAGE_DIMENSION, IMAGE_DIMENSION, REQUEST_CROP_RESULT);
                    } else {
                        shortTip(R.string.str_no_sd);
                    }
                    break;
                case REQUEST_CROP_RESULT:
                    //更新头像
                    mPresenter.updateAvatar(imageFile);
                    break;
                default:
            }
        }
    }

    @UiThread
    void updateHeaderImg(File file) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(file)));
            civAvatar.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UpdateUserSuccess() {
        BaseNotification.newInstance().postNotificationName(CommonNotifications.updateUserOwnerInfoSuccess, "");
        shortTip(R.string.tip_save_success);
        finish();
    }

    @Override
    public void UpdateAvatarSuccess(String avatar) {
        if (imageFile != null) {
            BaseNotification.newInstance().postNotificationName(CommonNotifications.updateUserOwnerInfoSuccess, "");
            avatarUrl = avatar;
            updateHeaderImg(imageFile);
            shortTip(R.string.tip_save_success);
            imageFile = null;
        }
    }

    @Override
    public void UpdateUserFail(int code, String msg) {
        shortTip(R.string.tip_save_fail);
    }
}
