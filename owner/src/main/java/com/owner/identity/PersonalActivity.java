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
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.RoundImageView;
import com.officego.commonlib.view.TitleBarView;
import com.owner.R;
import com.owner.adapter.IdentityBuildingAdapter;
import com.owner.adapter.PropertyOwnershipCertificateAdapter;
import com.owner.adapter.RentalAgreementAdapter;
import com.owner.identity.contract.PersonalContract;
import com.owner.identity.dialog.SwitchRoleDialog;
import com.owner.identity.model.GetIdentityInfoBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.ImageBean;
import com.owner.identity.presenter.PersonalPresenter;
import com.owner.utils.ButtonUtils;
import com.owner.utils.CommUtils;
import com.wildma.idcardcamera.camera.IDCardCamera;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.officego.commonlib.utils.CommonHelper.checkObjAllFieldsIsNull;

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
    private static final int REQUEST_CREATE_BUILDING = 0xa2;
    private static final int TYPE_IDCARD_FRONT = 1;
    private static final int TYPE_IDCARD_BACK = 2;
    private static final int TYPE_CER = 3;
    private static final int TYPE_REN = 4;
    private static final int IDENTITY_PERSONAL = 0; //0个人1企业2联合

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
    @ViewById(resName = "tv_building_edit")
    TextView tvBuildingEdit;
    @ViewById(resName = "tv_building_clear")
    TextView tvBuildingClear;
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
    private List<ImageBean> listCertificate = new ArrayList<>();
    private List<ImageBean> listRental = new ArrayList<>();
    private PropertyOwnershipCertificateAdapter certificateAdapter;
    private RentalAgreementAdapter rentalAdapter;

    private IdentityBuildingAdapter buildingAdapter;
    private List<IdentityBuildingBean.DataBean> mList = new ArrayList<>();

    private int mLeaseType;
    private int mBuildingId;
    private boolean isSelectedBuilding;
    private String userName, idCard;
    //是否上传了身份证正反面
    private boolean isUploadIdCardFront, isUploadIdCardBack;

    @AfterViews
    void init() {
        mPresenter = new PersonalPresenter();
        mPresenter.attachView(this);
        initRecyclerView();
        initData();
        mPresenter.getIdentityInfo(Constants.TYPE_IDENTITY_PERSONAL, true);
        ButtonUtils.clickButton(btnUpload, false);
    }

    private void initRecyclerView() {
        //房产证
        localCerPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "certificate.jpg";
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
        //返回
        titleBar.getLeftLayout().setOnClickListener(view -> onBackPressed());
        //搜索
        searchList();
        //初始化默认添加一个
        listCertificate.add(new ImageBean(false, 0, ""));
        listRental.add(new ImageBean(false, 0, ""));
        //房产证
        certificateAdapter = new PropertyOwnershipCertificateAdapter(context, listCertificate);
        certificateAdapter.setCertificateListener(this);
        rvPropertyOwnershipCertificate.setAdapter(certificateAdapter);
        //租赁协议
        rentalAdapter = new RentalAgreementAdapter(context, listRental);
        rentalAdapter.setAgreementListener(this);
        rvRentalAgreement.setAdapter(rentalAdapter);
    }

    @Override
    public void onBackPressed() {
        SwitchRoleDialog.identityBackDialog(this);
    }

    @Click(resName = "btn_upload")
    void uploadClick() {
        userName = cetName.getText() == null ? "" : cetName.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            shortTip("请输入姓名");
            return;
        }
        idCard = cetPersonalId.getText() == null ? "" : cetPersonalId.getText().toString();
        if (TextUtils.isEmpty(idCard)) {
            shortTip("请输入身份证号");
            return;
        }
        if (!isUploadIdCardFront) {
            shortTip("请上传身份证正面图片");
            return;
        }
        if (!isUploadIdCardBack) {
            shortTip("请上传身份证背面图片");
            return;
        }
        String buildingName = cetOfficeName.getText() == null ? "" : cetOfficeName.getText().toString();
        if (TextUtils.isEmpty(buildingName)) {
            shortTip("请输入写字楼");
            return;
        }
        String type = tvType.getText() == null ? "" : tvType.getText().toString().trim();
        if (TextUtils.isEmpty(type)) {
            shortTip("请选择房产类型");
            return;
        }
        if (listCertificate == null || listCertificate.size() <= 1) {
            shortTip("请上传房产证");
            return;
        }
        if (mLeaseType == 1 && (listRental == null || listRental.size() <= 1)) {
            shortTip("请上传租赁协议");
            return;
        }
        mPresenter.getIdentityInfo(Constants.TYPE_IDENTITY_PERSONAL, false);
    }

    @Click(resName = "rl_type")
    void typeClick() {
        selectedBuildingType();
    }

    private void selectedBuildingType() {
        final String[] items = {"自有房产", "租赁房产"};
        new AlertDialog.Builder(this)
                .setItems(items, (dialogInterface, i) -> selectHouseType(i)).create().show();
    }

    private void selectHouseType(int type) {
        if (type == 0) {
            mLeaseType = 0;
            showCertificateView();
            tvType.setText("自有房产");
        } else {
            mLeaseType = 1;
            showCerAgreementView();
            tvType.setText("租赁房产");
        }
    }

    private void showCertificateView() {
        ctlIdentityRoot.setVisibility(View.VISIBLE);
        rvRentalAgreement.setVisibility(View.GONE);
        tvTextRentalAgreement.setVisibility(View.GONE);
        tvTipRentalAgreement.setVisibility(View.GONE);
        ButtonUtils.clickButton(btnUpload, true);
    }

    private void showCerAgreementView() {
        ctlIdentityRoot.setVisibility(View.VISIBLE);
        rvRentalAgreement.setVisibility(View.VISIBLE);
        tvTextRentalAgreement.setVisibility(View.VISIBLE);
        tvTipRentalAgreement.setVisibility(View.VISIBLE);
        ButtonUtils.clickButton(btnUpload, true);
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

    @Click(resName = "tv_building_clear")
    void clearBuildingClick() {
        cetOfficeName.setText("");
        tvAddress.setText("");
        cetOfficeName.setEnabled(true);
        tvBuildingEdit.setVisibility(View.GONE);
    }

    @Click(resName = "tv_building_edit")
    void editBuildingClick() {
        CreateBuildingActivity_.intent(context)
                .identityType(Constants.TYPE_IDENTITY_PERSONAL)
                .isEdit(true)
                .startForResult(REQUEST_CREATE_BUILDING);
    }


    //身份证照片
    private void idCardDialog(boolean isFront) {
        hideSearchView();
        final String[] items = {"拍照", "从相册选择"};
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
        hideSearchView();
        final String[] items = {"拍照", "从相册选择"};
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
        if (isOverLimit()) return;
        if (!PermissionUtils.checkSDCardCameraPermission(this)) {
            return;
        }
        if (!FileUtils.isSDExist()) {
            shortTip(R.string.str_no_sd);
            return;
        }
        File fileUri;
        if (TYPE_CER == mUploadType) {
            localCerPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + System.currentTimeMillis() + "certificate.jpg";
            fileUri = new File(localCerPath);
        } else {
            localRenPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + System.currentTimeMillis() + "rental.jpg";
            fileUri = new File(localRenPath);
        }
        localPhotoUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            localPhotoUri = FileProvider.getUriForFile(this, Constants.FILE_PROVIDER_AUTHORITY, fileUri);
        }
        PhotoUtils.takePicture(this, localPhotoUri, REQUEST_CAMERA);
    }

    private boolean isOverLimit() {
        if (TYPE_CER == mUploadType) {//房产证
            if (listCertificate.size() >= 5) {
                shortTip(R.string.tip_image_upload_overlimit);
                return true;
            }
        } else if (TYPE_REN == mUploadType) {//租赁合同
            if (listRental.size() >= 6) {
                shortTip(R.string.tip_image_upload_overlimit);
                return true;
            }
        }
        return false;
    }

    private int num() {
        int num;
        if (TYPE_CER == mUploadType) {//房产证
            num = 5 - listCertificate.size();
        } else if (TYPE_REN == mUploadType) {//租赁合同
            num = 6 - listRental.size();
        } else {
            num = 6;
        }
        return num;
    }

    private void openGallery() {
        if (!PermissionUtils.checkStoragePermission(this)) {
            return;
        }
        //是否上传房产证
        if (TYPE_CER == mUploadType || TYPE_REN == mUploadType) {
            if (isOverLimit()) return;
            ImageSelector.builder()
                    .useCamera(false) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setMaxSelectCount(num())
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
                    isUploadIdCardFront = true;
                    localIdCardFrontPath = path;
                    rivImageFront.setImageBitmap(BitmapFactory.decodeFile(path));
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    hideIdCardBackView();
                    isUploadIdCardBack = true;
                    localIdCardBackPath = path;
                    rivImageBack.setImageBitmap(BitmapFactory.decodeFile(path));
                }
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//房产证拍照
                if (TYPE_CER == mUploadType) {//房产证
                    ImageUtils.isSaveCropImageView(localCerPath);//图片处理
                    listCertificate.add(listCertificate.size() - 1, new ImageBean(false, 0, localCerPath));
                    certificateAdapter.notifyDataSetChanged();
                } else if (TYPE_REN == mUploadType) {//租赁合同
                    ImageUtils.isSaveCropImageView(localRenPath);//图片处理
                    listRental.add(listRental.size() - 1, new ImageBean(false, 0, localRenPath));
                    rentalAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                if (TYPE_IDCARD_FRONT == mUploadType) {//身份证正面相册
                    hideIdCardFrontView();
                    isUploadIdCardFront = true;
                    localIdCardFrontPath = images.get(0);
                    rivImageFront.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
                } else if (TYPE_IDCARD_BACK == mUploadType) {//身份证反面相册
                    hideIdCardBackView();
                    isUploadIdCardBack = true;
                    localIdCardBackPath = images.get(0);
                    rivImageBack.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
                } else if (TYPE_CER == mUploadType) {//房产证相册
                    for (int i = 0; i < images.size(); i++) {
                        ImageUtils.isSaveCropImageView(images.get(i));//图片处理
                        listCertificate.add(listCertificate.size() - 1, new ImageBean(false, 0, images.get(i)));
                    }
                    certificateAdapter.notifyDataSetChanged();
                } else if (TYPE_REN == mUploadType) {
                    for (int i = 0; i < images.size(); i++) {
                        ImageUtils.isSaveCropImageView(images.get(i));//图片处理
                        listRental.add(listRental.size() - 1, new ImageBean(false, 0, images.get(i)));
                    }
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

    //search
    private void searchList() {
        cetOfficeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    hideSearchView();
                    tvAddress.setText("");
                    ButtonUtils.clickButton(btnUpload, true);
                } else {
                    rvRecommendBuilding.setVisibility(View.VISIBLE);
                    ButtonUtils.clickButton(btnUpload, false);
                    mPresenter.getBuilding(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void checkBuildingInfoSuccess() {
        //创建楼盘
        CreateBuildingActivity_.intent(context)
                .identityType(Constants.TYPE_IDENTITY_PERSONAL)
                .mBuildingName(cetOfficeName.getText() == null ? "" : cetOfficeName.getText().toString())//编辑创建传入子页面
                .startForResult(REQUEST_CREATE_BUILDING);
    }

    private void setEditView(GetIdentityInfoBean data) {
        //公司0 空  无定义     1创建  2关联
        if (TextUtils.equals("1", IdentityInfo.strCreateBuilding(data))) {
            //创建
            cetOfficeName.setEnabled(false);
            tvBuildingEdit.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals("2", IdentityInfo.strCreateBuilding(data))) {
            //关联
            cetOfficeName.setEnabled(false);
            tvBuildingEdit.setVisibility(View.GONE);
        } else {
            //无定义
            cetOfficeName.setEnabled(true);
            tvBuildingEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public void getIdentityInfoSuccess(GetIdentityInfoBean data, boolean isFirstGetInfo) {
        if (isFirstGetInfo) {
            if (data != null && !checkObjAllFieldsIsNull(data)) {
                cetName.setText(data.getProprietorRealname());
                cetPersonalId.setText(data.getIdCard());
                setEditView(data);
                if (!TextUtils.isEmpty(data.getIdFront()) && data.getIdFront().contains("http")) {
                    isUploadIdCardFront = true;
                    hideIdCardFrontView();
                    Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(data.getIdFront()).into(rivImageFront);
                }
                if (!TextUtils.isEmpty(data.getIdBack()) && data.getIdFront().contains("http")) {
                    isUploadIdCardBack = true;
                    hideIdCardBackView();
                    Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(data.getIdBack()).into(rivImageBack);
                }
                cetOfficeName.setText(data.getBuildingName());
                tvAddress.setText(data.getBuildingAddress());
                showBuildingView();
                //当楼盘null
                if (TextUtils.isEmpty(data.getBuildingName())) {
                    hideImageHouseTypeView();
                } else {
                    if (TextUtils.isEmpty(data.getLeaseType())) {
                        //初始化类型是null
                        showHouseTypeView();
                        hideImageView();
                    } else {
                        if (data.getPremisesPermit() == null || data.getPremisesPermit().size() == 0) {
                            //房产证是null
                            showHouseTypeView();
                            hideImageView();
                        } else {
                            //有类型有图片时
                            showImageHouseTypeView();
                            selectHouseType(Integer.valueOf(data.getLeaseType()));
                        }
                    }
                }
                //房产证
                if (listCertificate != null && listCertificate.size() > 0) {
                    for (int i = 0; i < data.getPremisesPermit().size(); i++) {
                        listCertificate.add(listCertificate.size() - 1, new ImageBean(true,
                                data.getPremisesPermit().get(i).getId(), data.getPremisesPermit().get(i).getImgUrl()));
                    }
                    certificateAdapter.notifyDataSetChanged();
                }
                //租赁合同
                if (listRental != null && listRental.size() > 0) {
                    for (int i = 0; i < data.getContract().size(); i++) {
                        listRental.add(listRental.size() - 1, new ImageBean(true,
                                data.getContract().get(i).getId(), data.getContract().get(i).getImgUrl()));
                    }
                    rentalAdapter.notifyDataSetChanged();
                }
                //赋值--驳回上传
                if (!TextUtils.isEmpty(data.getLeaseType())) {
                    mLeaseType = Integer.valueOf(data.getLeaseType());
                }
                if (TextUtils.isEmpty(data.getBuildingId()) ||
                        TextUtils.equals("0", data.getBuildingId())) { //创建的
                    isSelectedBuilding = false;
                } else {//关联的
                    isSelectedBuilding = true;
                    mBuildingId = Integer.valueOf(data.getBuildingId());
                }
            }
        } else {
            String mBuildingName = cetOfficeName.getText() == null ? "" : cetOfficeName.getText().toString();
            String mUserName = cetName.getText() == null ? "" : cetName.getText().toString();
            String mPersonalId = cetPersonalId.getText() == null ? "" : cetPersonalId.getText().toString();
            //提交信息
            mPresenter.submit(data, Constants.TYPE_CREATE_FROM_ALL, Constants.TYPE_IDENTITY_PERSONAL, mLeaseType,
                    isSelectedBuilding, String.valueOf(mBuildingId), mBuildingName, tvAddress.getText().toString(),
                    mUserName, mPersonalId, localIdCardFrontPath, localIdCardBackPath, listCertificate, listRental);
        }
    }

    @Override
    public void submitSuccess() {
        //返回业主个人中心
        SwitchRoleDialog.submitIdentitySuccessDialog(this);
    }

    @Override
    public void submitTimeout() {
        //返回业主个人中心
        SwitchRoleDialog.submitIdentityTimeoutDialog(this);
    }

    /**
     * 关联,创建楼盘
     */
    @Override
    public void associateBuilding(IdentityBuildingBean.DataBean bean, boolean isCreate) {
        if (isCreate) {
            mPresenter.checkBuilding(IDENTITY_PERSONAL, Objects.requireNonNull(cetOfficeName.getText()).toString());
            return;
        }
        //关联楼盘
        isSelectedBuilding = true;
        mBuildingId = bean.getBid();
        CommUtils.showHtmlView(cetOfficeName, bean.getBuildingName());
        CommUtils.showHtmlTextView(tvAddress, bean.getAddress());
        showHouseTypeView();
        //关联
        cetOfficeName.setEnabled(false);
        tvBuildingEdit.setVisibility(View.GONE);
    }

    //创建楼盘成功的回调
    @OnActivityResult(REQUEST_CREATE_BUILDING)
    void onCreateBuildingResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            isSelectedBuilding = false;
            String buildingName = data.getStringExtra("buildingName");
            String buildingAddress = data.getStringExtra("buildingAddress");
            cetOfficeName.setText(buildingName);
            tvAddress.setText(buildingAddress);
            showHouseTypeView();
            //创建
            cetOfficeName.setEnabled(false);
            tvBuildingEdit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addCertificate() {
        mUploadType = TYPE_CER;
        selectedDialog();
    }

    @Override
    public void deleteCertificate(ImageBean bean, int position) {
        if (isFastClick(1200)) {
            return;
        }
        //网络图片
        if (bean.isNetImage()) {
            mPresenter.deleteImage(true, bean.getId(), position);
        } else {
            listCertificate.remove(position);
            certificateAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addRentalAgreement() {
        mUploadType = TYPE_REN;
        selectedDialog();
    }

    @Override
    public void deleteRentalAgreement(ImageBean bean, int position) {
        if (isFastClick(1200)) {
            return;
        }
        //网络图片
        if (bean.isNetImage()) {
            mPresenter.deleteImage(false, bean.getId(), position);
        } else {
            listRental.remove(position);
            rentalAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除图片
     *
     * @param isPremisesImage 是否房产证图片
     * @param position        pos
     */
    @Override
    public void deleteImageSuccess(boolean isPremisesImage, int position) {
        if (isPremisesImage) {
            listCertificate.remove(position);
            certificateAdapter.notifyDataSetChanged();
        } else {
            listRental.remove(position);
            rentalAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param data data
     */
    @Override
    public void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data) {
        mList.clear();
        mList.addAll(data);
        mList.add(data.size(), new IdentityBuildingBean.DataBean());
        if (buildingAdapter == null) {
            buildingAdapter = new IdentityBuildingAdapter(context, mList, false);
            buildingAdapter.setListener(this);
            rvRecommendBuilding.setAdapter(buildingAdapter);
            return;
        }
        buildingAdapter.setData(mList);
        buildingAdapter.notifyDataSetChanged();
    }

    //显示上传图片
    private void showImageHouseTypeView() {
        hideSearchView();
        rlType.setVisibility(View.VISIBLE);
        ctlIdentityRoot.setVisibility(View.VISIBLE);
    }

    //隐藏房产类型和上传图片
    private void hideImageHouseTypeView() {
        rlType.setVisibility(View.GONE);
        ctlIdentityRoot.setVisibility(View.GONE);
        hideSearchView();
        ButtonUtils.clickButton(btnUpload, false);
    }

    //显示楼盘
    private void showBuildingView() {
        rlOffice.setVisibility(View.VISIBLE);
    }

    //显示房产类型
    private void showHouseTypeView() {
        rlType.setVisibility(View.VISIBLE);
        rvRecommendBuilding.setVisibility(View.GONE);
        if (TextUtils.isEmpty(tvType.getText().toString())) {
            ButtonUtils.clickButton(btnUpload, false);
        } else {
            ButtonUtils.clickButton(btnUpload, true);
        }
    }

    //隐藏底部上传图片
    private void hideImageView() {
        ctlIdentityRoot.setVisibility(View.GONE);
    }

    //隐藏搜索list View
    private void hideSearchView() {
        rvRecommendBuilding.setVisibility(View.GONE);
    }
}
