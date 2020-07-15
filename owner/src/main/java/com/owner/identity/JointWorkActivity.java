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
@EActivity(resName = "activity_id_jointwork")
public class JointWorkActivity extends BaseMvpActivity<JointWorkPresenter> implements
        JointWorkContract.View,
        PropertyOwnershipCertificateAdapter.CertificateListener,
        RentalAgreementAdapter.RentalAgreementListener,
        IdentityBuildingAdapter.IdentityBuildingListener,
        IdentityCompanyAdapter.IdentityCompanyListener {
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
    //搜索list
    @ViewById(resName = "rv_recommend_jointwork")
    RecyclerView rvRecommendJointwork;
    @ViewById(resName = "rv_recommend_company")
    RecyclerView rvRecommendCompany;
    @ViewById(resName = "rv_recommend_building")
    RecyclerView rvRecommendBuilding;
    //图片list
    @ViewById(resName = "rv_property_ownership_certificate")
    RecyclerView rvPropertyOwnershipCertificate;
    @ViewById(resName = "rv_rental_agreement")
    RecyclerView rvRentalAgreement;
    //编辑框
    @ViewById(resName = "cet_jointwork_name")
    ClearableEditText cetJointworkName;
    @ViewById(resName = "cet_company_name")
    ClearableEditText cetCompanyName;
    @ViewById(resName = "cet_office_name")
    ClearableEditText cetOfficeName;
    @ViewById(resName = "cet_office_address")
    ClearableEditText cetOfficeAddress;
    //布局
    @ViewById(resName = "v_gray_spaces")
    View vGraySpaces;
    @ViewById(resName = "rl_office")
    RelativeLayout rlOffice;
    @ViewById(resName = "rl_office_address")
    RelativeLayout rlOfficeAddress;
    @ViewById(resName = "rl_type")
    RelativeLayout rlType;
    @ViewById(resName = "ctl_identity_root")
    ConstraintLayout ctlIdentityRoot;
    @ViewById(resName = "btn_upload")
    Button btnUpload;

    private List<String> listCertificate = new ArrayList<>();
    private List<String> listRental = new ArrayList<>();
    private PropertyOwnershipCertificateAdapter certificateAdapter;
    private RentalAgreementAdapter rentalAdapter;
    private int mUploadType;
    //搜索公司,办公室
    private IdentityCompanyAdapter companyAdapter;
    private IdentityBuildingAdapter buildingAdapter;
    private List<IdentityCompanyBean.DataBean> mCompanyList = new ArrayList<>();
    private List<IdentityBuildingBean.DataBean> mList = new ArrayList<>();

    @AfterViews
    void init() {
        mPresenter = new JointWorkPresenter();
        mPresenter.attachView(this);
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        //房产证，租赁，封面图path
        localCerPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "certificate.jpg";
        localRenPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "rental.jpg";
        localBuildingPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "buildingdec.jpg";
        //搜索列表
        LinearLayoutManager jointWorkManager = new LinearLayoutManager(context);
        rvRecommendJointwork.setLayoutManager(jointWorkManager);
        LinearLayoutManager companyManager = new LinearLayoutManager(context);
        rvRecommendCompany.setLayoutManager(companyManager);
        LinearLayoutManager buildingManager = new LinearLayoutManager(context);
        rvRecommendBuilding.setLayoutManager(buildingManager);
        //图片
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvPropertyOwnershipCertificate.setLayoutManager(layoutManager);
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        layoutManager1.setSmoothScrollbarEnabled(true);
        layoutManager1.setAutoMeasureEnabled(true);
        rvRentalAgreement.setLayoutManager(layoutManager1);
        rvPropertyOwnershipCertificate.setNestedScrollingEnabled(false);
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
        hideView();
        final String[] items = {"拍照", "相册"};
        new AlertDialog.Builder(JointWorkActivity.this)
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
        }
        ImageSelector.builder()
                .useCamera(false) // 设置是否使用拍照
                .setSingle(false)  //设置是否单选
                .setMaxSelectCount(TYPE_BUI == mUploadType ? 1 : 9)
//                .setSelected((ArrayList<String>) selectList)
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
                }
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                if (TYPE_CER == mUploadType) {
                    listCertificate.addAll(listCertificate.size() - 1, images);
                    certificateAdapter.notifyDataSetChanged();
                } else if (TYPE_REN == mUploadType) {
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

    private void hideView() {
        rvRecommendCompany.setVisibility(View.GONE);
        rvRecommendBuilding.setVisibility(View.GONE);
    }

    //search
    private void searchList() {
        cetJointworkName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    hideView();
                } else {
                    rvRecommendCompany.setVisibility(View.VISIBLE);
                    rvRecommendBuilding.setVisibility(View.GONE);
                    mPresenter.getJointWork(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cetCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    hideView();
                } else {
                    rvRecommendCompany.setVisibility(View.VISIBLE);
                    rvRecommendBuilding.setVisibility(View.GONE);
                    mPresenter.getCompany(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cetOfficeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    hideView();
                } else {
                    rvRecommendCompany.setVisibility(View.GONE);
                    rvRecommendBuilding.setVisibility(View.VISIBLE);
                    mPresenter.getBuilding(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 网点
     */
    @Override
    public void searchJointWorkSuccess(List<IdentityJointWorkBean.DataBean> data) {

    }

    /**
     * 公司
     */
    @Override
    public void searchCompanySuccess(List<IdentityCompanyBean.DataBean> data) {
        mCompanyList.clear();
        mCompanyList.addAll(data);
        mCompanyList.add(data.size(), new IdentityCompanyBean.DataBean());
        if (companyAdapter == null) {
            companyAdapter = new IdentityCompanyAdapter(context, mCompanyList);
            companyAdapter.setListener(this);
            rvRecommendCompany.setAdapter(companyAdapter);
            return;
        }
        companyAdapter.setData(mCompanyList);
        companyAdapter.notifyDataSetChanged();
    }

    /**
     * 楼盘
     */
    @Override
    public void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data) {
        mList.clear();
        mList.addAll(data);
        mList.add(data.size(), new IdentityBuildingBean.DataBean());
        if (buildingAdapter == null) {
            buildingAdapter = new IdentityBuildingAdapter(context, mList);
            buildingAdapter.setListener(this);
            rvRecommendBuilding.setAdapter(buildingAdapter);
            return;
        }
        buildingAdapter.setData(mList);
        buildingAdapter.notifyDataSetChanged();
    }

    @Override
    public void associateCompany(IdentityCompanyBean.DataBean bean, boolean isCreate) {
        if (isCreate) {
            //创建公司
            CreateCompanyActivity_.intent(context).start();
            return;
        }
        CommUtils.showHtmlView(cetCompanyName, bean.getCompany());
        hideView();
        rlOffice.setVisibility(View.VISIBLE);
        rlOfficeAddress.setVisibility(View.VISIBLE);
    }

    @Override
    public void associateBuilding(IdentityBuildingBean.DataBean bean, boolean isCreate) {
        if (!isCreate) {
            CommUtils.showHtmlView(cetOfficeName, bean.getBuildingName());
            CommUtils.showHtmlView(cetOfficeAddress, bean.getAddress());
        }
        hideView();
        rlType.setVisibility(View.VISIBLE);
        ctlIdentityRoot.setVisibility(View.VISIBLE);
        btnUpload.setVisibility(View.VISIBLE);
    }
}
