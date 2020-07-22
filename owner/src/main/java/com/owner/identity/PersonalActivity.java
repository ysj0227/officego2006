package com.owner.identity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.RoundImageView;
import com.officego.commonlib.view.TitleBarView;
import com.owner.R;
import com.owner.adapter.IdentityBuildingAdapter;
import com.owner.adapter.PropertyOwnershipCertificateAdapter;
import com.owner.adapter.RentalAgreementAdapter;
import com.owner.identity.contract.PersonalContract;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.presenter.PersonalPresenter;
import com.owner.utils.CommUtils;
import com.wildma.idcardcamera.camera.IDCardCamera;

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
public class PersonalActivity extends BaseMvpActivity<PersonalPresenter> implements
        PersonalContract.View,
        PropertyOwnershipCertificateAdapter.CertificateListener,
        RentalAgreementAdapter.RentalAgreementListener,
        IdentityBuildingAdapter.IdentityBuildingListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int TYPE_IDCARD_FRONT = 1;
    private static final int TYPE_IDCARD_BACK = 2;
    private static final int TYPE_CER = 3;
    private static final int TYPE_REN = 4;

    private String localIdCardFrontPath, localIdCardBackPath, localCerPath, localRenPath;
    private Uri localPhotoUri;
    //title
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    //编辑框
    @ViewById(resName = "cet_name")
    ClearableEditText cetName;
    @ViewById(resName = "cet_personal_id")
    ClearableEditText cetPersonalId;
    //布局
    @ViewById(resName = "v_gray_spaces")
    View vGraySpaces;
    @ViewById(resName = "rl_image_front")
    RelativeLayout rlImageFront;
    @ViewById(resName = "rl_image_back")
    RelativeLayout rlImageBack;
    @ViewById(resName = "riv_image_front")
    RoundImageView rivImageFront;
    @ViewById(resName = "tv_upload_front")
    TextView tvUploadFront;
    @ViewById(resName = "riv_image_back")
    RoundImageView rivImageBack;
    @ViewById(resName = "tv_upload_back")
    TextView tvUploadBack;
    //楼盘
    @ViewById(resName = "rl_office")
    RelativeLayout rlOffice;
    @ViewById(resName = "cet_office_name")
    ClearableEditText cetOfficeName;
    @ViewById(resName = "tv_address")
    TextView tvAddress;
    @ViewById(resName = "rl_type")
    RelativeLayout rlType;
    @ViewById(resName = "ctl_identity_root")
    ConstraintLayout ctlIdentityRoot;
    //房产类型
    @ViewById(resName = "tv_type")
    TextView tvType;
    //图片list
    @ViewById(resName = "rv_property_ownership_certificate")
    RecyclerView rvPropertyOwnershipCertificate;
    @ViewById(resName = "tv_text_property_ownership_certificate")
    TextView tvTextPropertyOwnershipCertificate;
    @ViewById(resName = "tv_tip_property_ownership_certificate")
    TextView tvTipPropertyOwnershipCertificate;
    @ViewById(resName = "rv_rental_agreement")
    RecyclerView rvRentalAgreement;
    @ViewById(resName = "tv_text_rental_agreement")
    TextView tvTextRentalAgreement;
    @ViewById(resName = "tv_tip_rental_agreement")
    TextView tvTipRentalAgreement;
    //搜索list
    @ViewById(resName = "rv_recommend_building")
    RecyclerView rvRecommendBuilding;
    //上传
    @ViewById(resName = "btn_upload")
    Button btnUpload;
    private int mUploadType;
    private List<String> listCertificate = new ArrayList<>();
    private List<String> listRental = new ArrayList<>();
    private PropertyOwnershipCertificateAdapter certificateAdapter;
    private RentalAgreementAdapter rentalAdapter;

    private IdentityBuildingAdapter buildingAdapter;
    private List<IdentityBuildingBean.DataBean> mList = new ArrayList<>();

    @AfterViews
    void init() {
        mPresenter = new PersonalPresenter();
        mPresenter.attachView(this);
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        //房产证
        localIdCardFrontPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "idcardFront.jpg";
        localIdCardBackPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "idcardBack.jpg";
        localCerPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "buildingdec.jpg";
        localRenPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "rental.jpg";
        //搜索list
        LinearLayoutManager buildingManager = new LinearLayoutManager(context);
        rvRecommendBuilding.setLayoutManager(buildingManager);
        //图片
        int screenWidth = CommonHelper.getScreenWidth(context) - CommonHelper.dp2px(context, 36);
        int itemWidth = CommonHelper.dp2px(context, 100); //每个item的宽度
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvPropertyOwnershipCertificate.setLayoutManager(layoutManager);
        rvPropertyOwnershipCertificate.addItemDecoration(new SpaceItemDecoration((screenWidth - itemWidth * 3) / 6));
        rvPropertyOwnershipCertificate.setNestedScrollingEnabled(false);
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        layoutManager1.setSmoothScrollbarEnabled(true);
        layoutManager1.setAutoMeasureEnabled(true);
        rvRentalAgreement.setLayoutManager(layoutManager1);
        rvRentalAgreement.addItemDecoration(new SpaceItemDecoration((screenWidth - itemWidth * 3) / 6));
        rvRentalAgreement.setNestedScrollingEnabled(false);
    }

    private void initData() {
        searchList();
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

    @Click(resName = "rl_type")
    void typeClick() {
        selectedBuildingType();
    }

    private void selectedBuildingType() {
        final String[] items = {"自有房产", "租赁房产"};
        new AlertDialog.Builder(this)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        showCertificateView();
                    } else {
                        showCerAgreementView();
                    }
                    tvType.setText(items[i]);
                }).create().show();
    }

    private void showCertificateView() {
        ctlIdentityRoot.setVisibility(View.VISIBLE);
        rvRentalAgreement.setVisibility(View.GONE);
        tvTextRentalAgreement.setVisibility(View.GONE);
        tvTipRentalAgreement.setVisibility(View.GONE);
    }

    private void showCerAgreementView() {
        ctlIdentityRoot.setVisibility(View.VISIBLE);
        rvRentalAgreement.setVisibility(View.VISIBLE);
        tvTextRentalAgreement.setVisibility(View.VISIBLE);
        tvTipRentalAgreement.setVisibility(View.VISIBLE);
    }

    @Click(resName = "rl_identity")
    void identityClick() {
        SwitchRoleDialog.switchDialog(this);
    }

    @Click(resName = "rl_image_front")
    void idFrontClick() {
        mUploadType = TYPE_IDCARD_FRONT;
        idCardDialog(true);
    }

    @Click(resName = "rl_image_back")
    void idBackClick() {
        mUploadType = TYPE_IDCARD_BACK;
        idCardDialog(false);
    }

    //身份证照片
    private void idCardDialog(boolean isFront) {
        hideView();
        final String[] items = {isFront ? "身份证正面" : "身份证背面", "相册"};
        new AlertDialog.Builder(PersonalActivity.this)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        if (isFront) {
                            IDCardCamera.create(PersonalActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                        } else {
                            IDCardCamera.create(PersonalActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
                        }
                    } else if (i == 1) {
                        openGallery();
                    }
                }).create().show();
    }

    //房产认证拍照，相册
    private void selectedDialog() {
        hideView();
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

    //拍照
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
        } else {
            fileUri = new File(localRenPath);
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
        //是否上传房产证
        if (TYPE_CER == mUploadType || TYPE_REN == mUploadType) {
            ImageSelector.builder()
                    .useCamera(false) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setMaxSelectCount(9)
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

    private void hideIdCardFrontView() {
        tvUploadFront.setVisibility(View.GONE);
    }

    private void hideIdCardBackView() {
        tvUploadBack.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面
                    hideIdCardFrontView();
                    rivImageFront.setImageBitmap(BitmapFactory.decodeFile(path));
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    hideIdCardBackView();
                    rivImageBack.setImageBitmap(BitmapFactory.decodeFile(path));
                }
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//房产证拍照
                if (TYPE_CER == mUploadType) {
                    listCertificate.add(listCertificate.size() - 1, localCerPath);
                    certificateAdapter.notifyDataSetChanged();
                }else if (TYPE_REN == mUploadType) {
                    listRental.add(listRental.size() - 1, localRenPath);
                    rentalAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                if (TYPE_IDCARD_FRONT == mUploadType) {//身份证正面相册
                    hideIdCardFrontView();
                    rivImageFront.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
                } else if (TYPE_IDCARD_BACK == mUploadType) {//身份证反面相册
                    hideIdCardBackView();
                    rivImageBack.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
                } else if (TYPE_CER == mUploadType) {//房产证相册
                    listCertificate.addAll(listCertificate.size() - 1, images);
                    certificateAdapter.notifyDataSetChanged();
                }else if (TYPE_REN == mUploadType) {
                    listRental.addAll(listRental.size() - 1, images);
                    rentalAdapter.notifyDataSetChanged();
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
                        shortTip(getString(R.string.str_no_sd));
                    }
                } else {
                    shortTip(getString(R.string.str_please_open_camera));
                }
                break;
            case PermissionUtils.REQ_PERMISSIONS_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    shortTip(getString(R.string.str_please_open_sd));
                }
                break;
            default:
        }
    }

    private void hideView() {
        rvRecommendBuilding.setVisibility(View.GONE);
    }

    //search
    private void searchList() {
        cetOfficeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    hideView();
                    tvAddress.setText("");
                } else {
                    rvRecommendBuilding.setVisibility(View.VISIBLE);
                    mPresenter.getBuilding(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void associateBuilding(IdentityBuildingBean.DataBean bean, boolean isCreate) {
        if (!isCreate) {
            CommUtils.showHtmlView(cetOfficeName, bean.getBuildingName());
            CommUtils.showHtmlTextView(tvAddress, bean.getAddress());
        }
        hideView();
        rlType.setVisibility(View.VISIBLE);
        ctlIdentityRoot.setVisibility(View.VISIBLE);
        btnUpload.setVisibility(View.VISIBLE);
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


    /**
     * @param data
     */
    @Override
    public void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data) {
        mList.clear();
        mList.addAll(data);
        mList.add(data.size(), new IdentityBuildingBean.DataBean());
        if (buildingAdapter == null) {
            buildingAdapter = new IdentityBuildingAdapter(context, mList,false);
            buildingAdapter.setListener(this);
            rvRecommendBuilding.setAdapter(buildingAdapter);
            return;
        }
        buildingAdapter.setData(mList);
        buildingAdapter.notifyDataSetChanged();
    }
}
