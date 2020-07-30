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
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.TitleBarView;
import com.owner.R;
import com.owner.adapter.IdentityBuildingAdapter;
import com.owner.adapter.IdentityCompanyAdapter;
import com.owner.adapter.IdentityJointWorkAdapter;
import com.owner.adapter.PropertyOwnershipCertificateAdapter;
import com.owner.adapter.RentalAgreementAdapter;
import com.owner.identity.contract.JointWorkContract;
import com.owner.identity.dialog.SwitchRoleDialog;
import com.owner.identity.model.GetIdentityInfoBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.identity.model.IdentityJointWorkBean;
import com.owner.identity.model.ImageBean;
import com.owner.identity.model.SendMsgBean;
import com.owner.identity.presenter.JointWorkPresenter;
import com.owner.utils.CommUtils;

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
@EActivity(resName = "activity_id_jointwork")
public class JointWorkActivity extends BaseMvpActivity<JointWorkPresenter> implements
        JointWorkContract.View,
        PropertyOwnershipCertificateAdapter.CertificateListener,
        RentalAgreementAdapter.RentalAgreementListener,
        IdentityJointWorkAdapter.IdentityJointWorkListener,
        IdentityBuildingAdapter.IdentityBuildingListener,
        IdentityCompanyAdapter.IdentityCompanyListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int REQUEST_CREATE_COMPANY = 0xa2;
    private static final int REQUEST_CREATE_BUILDING = 0xa3;
    private static final int REQUEST_CREATE_JOINT_WORK = 0xa4;
    private static final int TYPE_CER = 1;
    private static final int TYPE_REN = 2;
    private static final int IDENTITY_JOINT_WORK = 2; //0个人1企业2联合

    private String localCerPath, localRenPath;
    private Uri localPhotoUri;
    //title
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    //搜索list
    @ViewById(resName = "rv_recommend_jointwork")
    RecyclerView rvRecommendJointwork;
    @ViewById(resName = "rl_recommend_building")
    RelativeLayout rlRecommendBuilding;
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
    @ViewById(resName = "tv_jointwork_address")
    TextView tvJointworkAddress;
    @ViewById(resName = "cet_company_name")
    ClearableEditText cetCompanyName;
    @ViewById(resName = "cet_office_name")
    ClearableEditText cetOfficeName;
    //    @ViewById(resName = "tv_address")
//    TextView tvAddress;
    //布局
    @ViewById(resName = "v_gray_spaces")
    View vGraySpaces;
    @ViewById(resName = "rl_company_name")
    RelativeLayout rlCompanyName;
    @ViewById(resName = "rl_office")
    RelativeLayout rlOffice;
    @ViewById(resName = "rl_type")
    RelativeLayout rlType;
    @ViewById(resName = "ctl_identity_root")
    ConstraintLayout ctlIdentityRoot;
    @ViewById(resName = "btn_upload")
    Button btnUpload;

    private List<ImageBean> listCertificate = new ArrayList<>();
    private List<ImageBean> listRental = new ArrayList<>();
    private PropertyOwnershipCertificateAdapter certificateAdapter;
    private RentalAgreementAdapter rentalAdapter;
    private int mUploadType;
    //搜索公司,办公室
    private IdentityJointWorkAdapter jointWorkAdapter;
    private IdentityCompanyAdapter companyAdapter;
    private IdentityBuildingAdapter buildingAdapter;
    private List<IdentityJointWorkBean.DataBean> mJointWorkList = new ArrayList<>();
    private List<IdentityCompanyBean.DataBean> mCompanyList = new ArrayList<>();
    private List<IdentityBuildingBean.DataBean> mList = new ArrayList<>();

    @AfterViews
    void init() {
        mPresenter = new JointWorkPresenter();
        mPresenter.attachView(this);
        initRecyclerView();
        initData();
        mPresenter.getIdentityInfo(Constants.TYPE_IDENTITY_JOINT_WORK, true);
    }

    private void initRecyclerView() {
        //房产证，租赁，封面图path
        localCerPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "certificate.jpg";
        localRenPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "rental.jpg";
        //搜索列表
        LinearLayoutManager jointWorkManager = new LinearLayoutManager(context);
        jointWorkManager.setSmoothScrollbarEnabled(true);
        jointWorkManager.setAutoMeasureEnabled(true);
        rvRecommendJointwork.setLayoutManager(jointWorkManager);
        rvRecommendJointwork.setNestedScrollingEnabled(false);
        LinearLayoutManager companyManager = new LinearLayoutManager(context);
        companyManager.setSmoothScrollbarEnabled(true);
        companyManager.setAutoMeasureEnabled(true);
        rvRecommendCompany.setLayoutManager(companyManager);
        rvRecommendCompany.setNestedScrollingEnabled(false);
        LinearLayoutManager buildingManager = new LinearLayoutManager(context);
        buildingManager.setSmoothScrollbarEnabled(true);
        buildingManager.setAutoMeasureEnabled(true);
        rvRecommendBuilding.setLayoutManager(buildingManager);
        rvRecommendBuilding.setNestedScrollingEnabled(false);
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

    @Click(resName = "rl_identity")
    void identityClick() {
        SwitchRoleDialog.switchDialog(this);
    }

    @Click(resName = "btn_upload")
    void uploadClick() {
        String name = cetCompanyName.getText() == null ? "" : cetCompanyName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            shortTip("请输入网点名称");
            return;
        }
        String companyName = cetCompanyName.getText() == null ? "" : cetCompanyName.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            shortTip("请输入公司名称");
            return;
        }
        String buildingName = cetOfficeName.getText() == null ? "" : cetOfficeName.getText().toString().trim();
        if (TextUtils.isEmpty(buildingName)) {
            shortTip("请输入楼盘名称");
            return;
        }
        if (listCertificate == null || listCertificate.size() <= 1) {
            shortTip("请上传房产证");
            return;
        }
        if (listRental == null || listRental.size() <= 1) {
            shortTip("请上传租赁协议");
            return;
        }
        mPresenter.getIdentityInfo(Constants.TYPE_IDENTITY_JOINT_WORK, false);
    }

    @Click(resName = "ibt_close_keyboard")
    void closeKeyboardClick() {
        hideView();
        ctlIdentityRoot.setVisibility(View.VISIBLE);
        rlType.setVisibility(View.VISIBLE);
        btnUpload.setVisibility(View.VISIBLE);
    }

    private void selectedDialog() {
        hideView();
        final String[] items = {"拍照", "从相册选择"};
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

    private boolean isOverLimit() {
        if (TYPE_CER == mUploadType) {//房产证
            if (listCertificate.size() >= 10) {
                shortTip(R.string.tip_image_upload_overlimit);
                return true;
            }
        } else if (TYPE_REN == mUploadType) {//租赁合同
            if (listRental.size() >= 10) {
                shortTip(R.string.tip_image_upload_overlimit);
                return true;
            }
        }
        return false;
    }

    private int num() {
        int num;
        if (TYPE_CER == mUploadType) {//房产证
            num = 10 - listCertificate.size();
        } else if (TYPE_REN == mUploadType) {//租赁合同
            num = 10 - listRental.size();
        } else {
            num = 9;
        }
        return num;
    }

    private void openGallery() {
        if (!PermissionUtils.checkStoragePermission(this)) {
            return;
        }
        if (isOverLimit()) return;
        ImageSelector.builder()
                .useCamera(false) // 设置是否使用拍照
                .setSingle(false)  //设置是否单选
                .setMaxSelectCount(num())
                .canPreview(true) //是否可以预览图片，默认为true
                .start(this, REQUEST_GALLERY); // 打开相册
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照
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
                if (TYPE_CER == mUploadType) {
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

    private void hideView() {
        rvRecommendJointwork.setVisibility(View.GONE);
        rvRecommendCompany.setVisibility(View.GONE);
        rlRecommendBuilding.setVisibility(View.GONE);
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
                    tvJointworkAddress.setText("");
                } else {
                    rvRecommendJointwork.setVisibility(View.VISIBLE);
                    rvRecommendCompany.setVisibility(View.GONE);
                    rlRecommendBuilding.setVisibility(View.GONE);
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
                    rvRecommendJointwork.setVisibility(View.GONE);
                    rvRecommendCompany.setVisibility(View.VISIBLE);
                    rlRecommendBuilding.setVisibility(View.GONE);
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
//                    tvAddress.setText("");
                } else {
                    rvRecommendJointwork.setVisibility(View.GONE);
                    rvRecommendCompany.setVisibility(View.GONE);
                    rlRecommendBuilding.setVisibility(View.VISIBLE);
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
        mJointWorkList.clear();
        mJointWorkList.addAll(data);
        mJointWorkList.add(data.size(), new IdentityJointWorkBean.DataBean());
        if (jointWorkAdapter == null) {
            jointWorkAdapter = new IdentityJointWorkAdapter(context, mJointWorkList);
            jointWorkAdapter.setListener(this);
            rvRecommendJointwork.setAdapter(jointWorkAdapter);
            return;
        }
        jointWorkAdapter.setData(mJointWorkList);
        jointWorkAdapter.notifyDataSetChanged();
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
            companyAdapter = new IdentityCompanyAdapter(context, mCompanyList, false);
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
//        mList.add(data.size(), new IdentityBuildingBean.DataBean()); //联办没有创建楼盘
        if (buildingAdapter == null) {
            buildingAdapter = new IdentityBuildingAdapter(context, mList, true);
            buildingAdapter.setListener(this);
            rvRecommendBuilding.setAdapter(buildingAdapter);
            return;
        }
        buildingAdapter.setData(mList);
        buildingAdapter.notifyDataSetChanged();
    }

    @Override
    public void getIdentityInfoSuccess(GetIdentityInfoBean data, boolean isFirstGetInfo) {
        if (isFirstGetInfo) {
            if (data != null && !checkObjAllFieldsIsNull(data)) {
                cetJointworkName.setText(data.getBranchesName());
                tvJointworkAddress.setText(data.getBuildingAddress());
                hideView();
                //auditStatus 为2 驳回  authority 如果是1(普通) 就是创建 ，如果是0(管理员)就是关联
                if (IdentityRejectInfo.isCreateReject(data)) return;
                cetCompanyName.setText(data.getCompany());
                cetOfficeName.setText(data.getBuildingName());
                rlCompanyName.setVisibility(View.VISIBLE);
                rlOffice.setVisibility(View.VISIBLE);
                rlType.setVisibility(View.VISIBLE);
                buildingNextView();
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
            }
        } else {
            LogCat.e(TAG, "111111111111 listRental=" + listRental.size());
            //提交信息 租赁房产 leaseType==1
            mPresenter.submit(data, Constants.TYPE_CREATE_FROM_ALL, Constants.TYPE_IDENTITY_JOINT_WORK, 1,
                    cetOfficeName.getText().toString(), listCertificate, listRental);
        }
    }

    @Override
    public void submitSuccess() {
        //返回业主个人中心
        SwitchRoleDialog.submitIdentitySuccessDialog(this);
    }

    @Override
    public void checkCompanyInfoSuccess() {
        //创建公司
        CreateCompanyActivity_.intent(context)
                .createCompany(Constants.TYPE_CREATE_FROM_COMPANY)
                .identityType(Constants.TYPE_IDENTITY_JOINT_WORK)
                .relevanceCompanyName(cetCompanyName.getText() == null ? "" : cetCompanyName.getText().toString())
                .startForResult(REQUEST_CREATE_COMPANY);
    }

    @Override
    public void checkJointWorkInfoSuccess() {
        //创建网点
        CreateJointWorkActivity_.intent(context)
                .mJointWorkName(cetJointworkName.getText() == null ? "" : cetJointworkName.getText().toString())
                .startForResult(REQUEST_CREATE_JOINT_WORK);
    }

    /**
     * 网点
     */
    @Override
    public void associateJointWork(IdentityJointWorkBean.DataBean bean, boolean isCreate) {
        if (isCreate) {
            mPresenter.checkJointWork(IDENTITY_JOINT_WORK, Objects.requireNonNull(cetJointworkName.getText()).toString());
            return;
        }
        //关联网点--发送消息 0个人1企业2联合
        SendMsgBean sb = new SendMsgBean();
        sb.setId(bean.getBid());
        sb.setName(bean.getBuildingName());
        sb.setAddress(bean.getAddress());
        sb.setIdentityType(2);
        IdentitySendMsgActivity_.intent(context).sendMsgBean(sb).start();
        CommUtils.showHtmlView(cetJointworkName, bean.getBuildingName());
        CommUtils.showHtmlTextView(tvJointworkAddress, bean.getAddress());
        hideView();
    }

    //创建网点成功的回调
    @OnActivityResult(REQUEST_CREATE_JOINT_WORK)
    void onCreateJointWorkResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String jointworkName = data.getStringExtra("jointworkName");
            String jointworkAddress = data.getStringExtra("jointworkAddress");
            cetJointworkName.setText(jointworkName);
            tvJointworkAddress.setText(jointworkAddress);
            //显示下一步的view
            hideView();
            rlCompanyName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void associateCompany(IdentityCompanyBean.DataBean bean, boolean isCreate) {
        if (isCreate) {
            mPresenter.checkCompany(IDENTITY_JOINT_WORK, Objects.requireNonNull(cetCompanyName.getText()).toString());
            return;
        }
        //联办的关联公司进入创建
        CreateCompanyActivity_.intent(context)
                .createCompany(Constants.TYPE_CREATE_FROM_COMPANY)
                .identityType(Constants.TYPE_IDENTITY_JOINT_WORK)
                .relevanceCompanyName(bean.getCompany())
                .relevanceCompanyAddress(bean.getAddress())
                .startForResult(REQUEST_CREATE_COMPANY);
        cetCompanyName.setText("");
        rlOffice.setVisibility(View.VISIBLE);
        hideView();
    }

    //创建公司成功的回调
    @OnActivityResult(REQUEST_CREATE_COMPANY)
    void onCreateCompanyResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String companyName = data.getStringExtra("companyName");
            cetCompanyName.setText(companyName);
            //显示下一步的view
            rlOffice.setVisibility(View.VISIBLE);
            hideView();
        }
    }

    @Override
    public void associateBuilding(IdentityBuildingBean.DataBean bean, boolean isCreate) {
        //关联楼盘
        CommUtils.showHtmlView(cetOfficeName, bean.getBuildingName());
        buildingNextView();
    }

    //创建楼盘成功的回调
    @OnActivityResult(REQUEST_CREATE_BUILDING)
    void onCreateBuildingResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String buildingName = data.getStringExtra("buildingName");
            String buildingAddress = data.getStringExtra("buildingAddress");
            cetOfficeName.setText(buildingName);
            //显示下一步的view
            buildingNextView();
        }
    }

    private void buildingNextView() {
        hideView();
        ctlIdentityRoot.setVisibility(View.VISIBLE);
        rlType.setVisibility(View.VISIBLE);
        btnUpload.setVisibility(View.VISIBLE);
    }
}
