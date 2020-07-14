package com.owner.identity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.TitleBarView;
import com.owner.R;
import com.owner.adapter.PropertyOwnershipCertificateAdapter;
import com.owner.adapter.RentalAgreementAdapter;

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
@EActivity(resName = "activity_id_company")
public class CompanyActivity extends BaseActivity implements
        PropertyOwnershipCertificateAdapter.CertificateListener,
        RentalAgreementAdapter.RentalAgreementListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int TYPE_CER = 1;
    private static final int TYPE_REN = 2;
    private static final int TYPE_BUI = 3;

    private String localCerPath, localRenPath, localBuildingPath;
    private Uri localPhotoUri;

    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "rv_property_ownership_certificate")
    RecyclerView rvPropertyOwnershipCertificate;
    @ViewById(resName = "rv_rental_agreement")
    RecyclerView rvRentalAgreement;
    @ViewById(resName = "iv_building_introduce")
    ImageView ivBuildingIntroduce;

    private List<String> listCertificate = new ArrayList<>();
    private List<String> listRental = new ArrayList<>();
    private List<String> listBuilding = new ArrayList<>();
    private PropertyOwnershipCertificateAdapter certificateAdapter;
    private RentalAgreementAdapter rentalAdapter;

    private int mUploadType;

    @AfterViews
    void init() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        localCerPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "certificate.jpg";
        localRenPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "rental.jpg";
        localBuildingPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "buildingdec.jpg";

        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        rvPropertyOwnershipCertificate.setLayoutManager(layoutManager);
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        rvRentalAgreement.setLayoutManager(layoutManager1);
        rvPropertyOwnershipCertificate.setNestedScrollingEnabled(false);
        rvRentalAgreement.setNestedScrollingEnabled(false);
        //初始化默认添加一个
        listCertificate.add("");
        listRental.add("");
        //房产证
        certificateAdapter = new PropertyOwnershipCertificateAdapter(context, listCertificate);
        certificateAdapter.setCertificateListener(this);
        rvPropertyOwnershipCertificate.setAdapter(certificateAdapter);
        //租赁协议
        rentalAdapter = new RentalAgreementAdapter(context, listRental);
        rentalAdapter.setAgreementListener(this);
        rvRentalAgreement.setAdapter(rentalAdapter);
    }

    @Click(resName = "rl_identity")
    void identityClick() {

    }

    @Click(resName = "rl_company_name")
    void companyNameClick() {

    }

    private void selectedDialog() {
        final String[] items = {"拍照", "相册"};
        new AlertDialog.Builder(CompanyActivity.this)
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
        File fileUri;
        if (TYPE_CER == mUploadType) {
            fileUri = new File(localCerPath);
        } else if (TYPE_REN == mUploadType) {
            fileUri = new File(localRenPath);
        } else {
            fileUri = new File(localBuildingPath);
        }
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
        //限数量的多选(比如最多9张)
        List<String> selectList = new ArrayList<>();
        selectList.clear();
        if (TYPE_CER == mUploadType) {
            selectList.addAll(listCertificate);
        } else if (TYPE_REN == mUploadType) {
            selectList.addAll(listRental);
        } else if (TYPE_BUI == mUploadType) {
            selectList.addAll(listBuilding);
        }
        ImageSelector.builder()
                .useCamera(false) // 设置是否使用拍照
                .setSingle(false)  //设置是否单选
                .setMaxSelectCount(TYPE_BUI == mUploadType ? 1 : 9)
                .setSelected((ArrayList<String>) selectList)
                .canPreview(true) //是否可以预览图片，默认为true
                .start(this, REQUEST_GALLERY); // 打开相册
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照
                if (TYPE_CER == mUploadType) {
                    listCertificate.add(listCertificate.size() - 1, localCerPath);
                    certificateAdapter.notifyDataSetChanged();
                } else if (TYPE_REN == mUploadType) {
                    listRental.add(listRental.size() - 1, localRenPath);
                    rentalAdapter.notifyDataSetChanged();
                } else if (TYPE_BUI == mUploadType) {
                    ivBuildingIntroduce.setImageBitmap(BitmapFactory.decodeFile(localBuildingPath));
                }
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                if (TYPE_CER == mUploadType) {
                    listCertificate.addAll(listCertificate.size() - 1, images);
                    certificateAdapter.notifyDataSetChanged();
                } else if (TYPE_REN == mUploadType) {
                    listRental.addAll(listRental.size() - 1, images);
                    rentalAdapter.notifyDataSetChanged();
                } else if (TYPE_BUI == mUploadType) {
                    listBuilding.clear();
                    listBuilding.add(images.get(0));
                    ivBuildingIntroduce.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
                }
            }
        }
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

    @Override
    public void addCertificate() {
        mUploadType = TYPE_CER;
        selectedDialog();
    }

    @Override
    public void deleteCertificate(int position) {
        listCertificate.remove(position);
        certificateAdapter.notifyDataSetChanged();
    }

    @Override
    public void addRentalAgreement() {
        mUploadType = TYPE_REN;
        selectedDialog();
    }

    @Override
    public void deleteRentalAgreement(int position) {
        listRental.remove(position);
        rentalAdapter.notifyDataSetChanged();
    }

    @Click(resName = "iv_building_introduce")
    void addBuildingIntroduceClick() {
        mUploadType = TYPE_BUI;
        selectedDialog();
    }
}
