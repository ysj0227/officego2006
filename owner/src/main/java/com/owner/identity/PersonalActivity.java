package com.owner.identity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.adapter.IdentityBuildingAdapter;
import com.owner.adapter.IdentityCompanyAdapter;
import com.owner.adapter.IdentityJointWorkAdapter;
import com.owner.adapter.PropertyOwnershipCertificateAdapter;
import com.owner.adapter.RentalAgreementAdapter;
import com.owner.identity.contract.JointWorkContract;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.identity.model.IdentityJointWorkBean;
import com.owner.identity.presenter.JointWorkPresenter;
import com.owner.utils.CommUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/7/13.
 * Descriptions:
 **/
@EActivity(resName = "activity_id_personal")
public class PersonalActivity extends BaseActivity  {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int TYPE_CER = 1;
    private static final int TYPE_REN = 2;
    private static final int TYPE_BUI = 3;

    private String localCerPath, localRenPath, localBuildingPath;
    private Uri localPhotoUri;
    //title
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "btn_upload")
    Button btnUpload;

    private IdentityBuildingAdapter buildingAdapter;
    private List<IdentityBuildingBean.DataBean> mList = new ArrayList<>();
    @AfterViews
    void init() {

    }

    private void initRecyclerView() {
        //房产证，租赁，封面图path
        localCerPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "certificate.jpg";
        localRenPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "rental.jpg";
        localBuildingPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "buildingdec.jpg";

        //图片);
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        layoutManager1.setSmoothScrollbarEnabled(true);
        layoutManager1.setAutoMeasureEnabled(true);
//        rvRentalAgreement.setLayoutManager(layoutManager1);
//        rvPropertyOwnershipCertificate.setNestedScrollingEnabled(false);
//        rvRentalAgreement.setNestedScrollingEnabled(false);
    }

    private void initData() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //返回二次确认
    private void backDialog() {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("确认离开吗？")
                .setMessage("网点未创建成功，点击保存下次可继续编辑。点击离开，已编辑信息不保存")
                .setConfirmButton(R.string.str_save, (dialog12, which) -> {
                    //TODO
                })
                .setCancelButton(R.string.str_go_away, (dialog12, which) -> {
                    //TODO
                }).create();
        dialog.showWithOutTouchable(false);
    }

    @Click(resName = "rl_identity")
    void identityClick() {
        finish();
    }

    private void selectedDialog() {
//        hideView();
        final String[] items = {"拍照", "相册"};
        new AlertDialog.Builder(PersonalActivity.this)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        takePhoto();
                    } else if (i == 1) {
                        openGallery();
                    }
                }).create().show();
    }


    private void takePhoto() {
//        if (!PermissionUtils.checkSDCardCameraPermission(this)) {
//            return;
//        }
//        if (!FileUtils.isSDExist()) {
//            shortTip(R.string.str_no_sd);
//            return;
//        }
//        File fileUri;
//        if (TYPE_CER == mUploadType) {
//            fileUri = new File(localCerPath);
//        } else if (TYPE_REN == mUploadType) {
//            fileUri = new File(localRenPath);
//        } else {
//            fileUri = new File(localBuildingPath);
//        }
//        localPhotoUri = Uri.fromFile(fileUri);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //通过FileProvider创建一个content类型的Uri
//            localPhotoUri = FileProvider.getUriForFile(this, Constants.FILE_PROVIDER_AUTHORITY, fileUri);
//        }
//        PhotoUtils.takePicture(this, localPhotoUri, REQUEST_CAMERA);
    }

    private void openGallery() {
        if (!PermissionUtils.checkStoragePermission(this)) {
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                        ToastUtils.toastForShort(this, getString(R.string.str_no_sd));
                    }
                } else {
                    ToastUtils.toastForShort(this, getString(R.string.str_please_open_camera));
                }
                break;
            case PermissionUtils.REQ_PERMISSIONS_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    ToastUtils.toastForShort(this, getString(R.string.str_please_open_sd));
                }
                break;
            default:
        }
    }

}
