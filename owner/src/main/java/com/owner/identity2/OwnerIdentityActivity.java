package com.owner.identity2;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.SearchAdapter;
import com.owner.adapter.UploadImageAdapter;
import com.owner.dialog.IdentityTypeDialog;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.ImageBean;
import com.owner.identity2.contract.IdentityContract;
import com.owner.identity2.presenter.IdentityPresenter;
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

/**
 * Created by shijie
 * Date 2020/11/17
 * 认证
 **/
@EActivity(resName = "activity_owner_identity")
public class OwnerIdentityActivity extends BaseMvpActivity<IdentityPresenter>
        implements IdentityContract.View, SearchBuildingTextWatcher.SearchListener,
        IdentityTypeDialog.IdentityTypeListener,
        SearchAdapter.IdentityBuildingListener, UploadImageAdapter.UploadListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int REQUEST_CREATE_BUILDING = 0xa3;
    private static final int TYPE_IDCARD_FRONT = 1;
    private static final int TYPE_IDCARD_BACK = 2;
    private static final int TYPE_CER = 3;//产证
    private static final int TYPE_LICE = 4;//营业执照
    private static final int TYPE_ADDI = 5;//补充材料
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "tv_reject_reason")
    TextView tvRejectReason;
    @ViewById(resName = "iv_expand")
    ImageView ivExpand;
    @ViewById(resName = "cet_name")
    EditText cetName;
    @ViewById(resName = "tv_address")
    TextView tvAddress;
    @ViewById(resName = "tv_building_flay")
    TextView tvBuildingFlay;
    @ViewById(resName = "iv_edit")
    ImageView ivEdit;
    @ViewById(resName = "iv_delete")
    ImageView ivDelete;
    @ViewById(resName = "sil_select_type")
    SettingItemLayout silSelectType;
    @ViewById(resName = "rv_recommend_building")
    RecyclerView rvRecommendBuilding;
    @ViewById(resName = "btn_upload")
    Button btnUpload;
    @ViewById(resName = "include_business_license")
    View includeBusinessLicense;
    @ViewById(resName = "include_owner_personal_id")
    View includeOwnerPersonalId;
    @ViewById(resName = "rv_property_ownership_certificate")
    RecyclerView rvCertificate;
    @ViewById(resName = "rv_business_licensee")
    RecyclerView rvBusinessLicensee;
    @ViewById(resName = "rv_additional_info")
    RecyclerView rvAddiInfo;
    //身份证
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

    //是否展开
    private boolean isSpread;
    //搜索
    private SearchAdapter searchAdapter;
    private List<IdentityBuildingBean.DataBean> mList = new ArrayList<>();
    //上传类型
    private int mUploadType;
    //本地路径
    private String localIdCardFrontPath, localIdCardBackPath, localCerPath, localLicePath, localAddiPath;
    private Uri localPhotoUri;
    //上传图片
    private List<ImageBean> listCertificate = new ArrayList<>();
    private List<ImageBean> listBusinessLice = new ArrayList<>();
    private List<ImageBean> listAdditionalInfo = new ArrayList<>();
    private UploadImageAdapter cerAdapter, liceAdapter, addiAdapter;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new IdentityPresenter();
        mPresenter.attachView(this);
        initViews();
        initData();
    }

    private void initViews() {
        //搜索列表
        LinearLayoutManager buildingManager = new LinearLayoutManager(context);
        rvRecommendBuilding.setLayoutManager(buildingManager);
        SearchBuildingTextWatcher textWatcher=new SearchBuildingTextWatcher();
        textWatcher.setListener(this);
        cetName.addTextChangedListener(textWatcher);
        //上传产证图片
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        layoutManager1.setSmoothScrollbarEnabled(true);
        layoutManager1.setAutoMeasureEnabled(true);
        rvCertificate.setLayoutManager(layoutManager1);
        rvCertificate.setNestedScrollingEnabled(false);
        //营业执照
        GridLayoutManager layoutManager2 = new GridLayoutManager(context, 3);
        layoutManager2.setSmoothScrollbarEnabled(true);
        layoutManager2.setAutoMeasureEnabled(true);
        rvBusinessLicensee.setLayoutManager(layoutManager2);
        rvBusinessLicensee.setNestedScrollingEnabled(false);
        //补充材料
        GridLayoutManager layoutManager3 = new GridLayoutManager(context, 3);
        layoutManager3.setSmoothScrollbarEnabled(true);
        layoutManager3.setAutoMeasureEnabled(true);
        rvAddiInfo.setLayoutManager(layoutManager3);
        rvAddiInfo.setNestedScrollingEnabled(false);
    }

    private void initData() {
        //初始化本地图路径 产证
        localCerPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "localCerPath.jpg";
        //初始化图片默认添加一个
        listCertificate.add(new ImageBean(false, 0, ""));
        cerAdapter = new UploadImageAdapter(context, TYPE_CER, listCertificate);
        cerAdapter.setListener(this);
        rvCertificate.setAdapter(cerAdapter);

        //初始化本地图路径 营业执照
        localLicePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "localLicePath.jpg";
        //初始化图片默认添加一个
        listBusinessLice.add(new ImageBean(false, 0, ""));
        liceAdapter = new UploadImageAdapter(context, TYPE_LICE, listBusinessLice);
        liceAdapter.setListener(this);
        rvBusinessLicensee.setAdapter(liceAdapter);

        //初始化本地图路径 补充材料
        localAddiPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "localAddiPath.jpg";
        //初始化图片默认添加一个
        listAdditionalInfo.add(new ImageBean(false, 0, ""));
        addiAdapter = new UploadImageAdapter(context, TYPE_ADDI, listAdditionalInfo);
        addiAdapter.setListener(this);
        rvAddiInfo.setAdapter(addiAdapter);
    }

    @Click(resName = "iv_expand")
    void spreadOnClick() {
        ivExpand.setBackgroundResource(isSpread ?
                com.officego.commonlib.R.mipmap.ic_down_arrow_gray :
                com.officego.commonlib.R.mipmap.ic_up_arrow_gray);
        tvRejectReason.setSingleLine(isSpread);
        isSpread = !isSpread;
    }

    @Click(resName = "sil_select_type")
    void selectTypeOnClick() {
        new IdentityTypeDialog(context).setListener(this);
    }

    @Click(resName = "iv_edit")
    void editOnClick() {
        OwnerCreateBuildingActivity_.intent(context)
                .isEdit(true)
                .startForResult(REQUEST_CREATE_BUILDING);
    }

    @Click(resName = "iv_delete")
    void deleteOnClick() {
        cetName.setText("");
        cetName.setEnabled(true);
        tvAddress.setVisibility(View.INVISIBLE);
        tvBuildingFlay.setVisibility(View.GONE);
        ivEdit.setVisibility(View.GONE);
        hideSearchListView();
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

    @Override
    public void sureType(String text, int type) {
        //1公司 2个人（自定义参数）
        silSelectType.setCenterText(text);
        includeBusinessLicense.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        includeOwnerPersonalId.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
    }
    //搜索楼盘网点
    @Override
    public void searchBuilding(String str) {
        showSearchListView();
        mPresenter.searchBuilding(str);
    }

    @Override
    public void searchBuildingSuccess(List<IdentityBuildingBean.DataBean> data) {
        mList.clear();
        mList.addAll(data);
        mList.add(data.size(), new IdentityBuildingBean.DataBean());
        if (searchAdapter == null) {
            searchAdapter = new SearchAdapter(context, mList);
            searchAdapter.setListener(this);
            rvRecommendBuilding.setAdapter(searchAdapter);
            return;
        }
        searchAdapter.setData(mList);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void associateBuilding(IdentityBuildingBean.DataBean bean, boolean isCreate) {
        if (isCreate) {
            String inputTx = cetName.getText().toString().trim();
            OwnerCreateBuildingActivity_.intent(context)
                    .inputText(inputTx)
                    .startForResult(REQUEST_CREATE_BUILDING);//创建楼盘、网点
        } else {
            buildingFlayView(bean.getBuildType());
            tvAddress.setVisibility(View.VISIBLE);
            CommUtils.showHtmlView(cetName, bean.getBuildingName());//名称
            CommUtils.showHtmlTextView(tvAddress, bean.getAddress());//地址
            cetName.setSelection(cetName.getText().length());//光标
            cetName.setEnabled(false);
            hideSearchListView();
        }
        ivDelete.setVisibility(View.VISIBLE);
    }

    //创建楼盘成功的回调
    @OnActivityResult(REQUEST_CREATE_BUILDING)
    void onCreateBuildingResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int buildingType = data.getIntExtra("buildingType", 1);
            String buildingName = data.getStringExtra("buildingName");
            String buildingAddress = data.getStringExtra("buildingAddress");
            buildingFlayView(buildingType);
            cetName.setText(buildingName);
            tvAddress.setText(buildingAddress);
            tvAddress.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.VISIBLE);
            cetName.setEnabled(false);
            hideSearchListView();
        }
    }

    private void showSearchListView() {
        btnUpload.setVisibility(View.GONE);
        rvRecommendBuilding.setVisibility(View.VISIBLE);
    }

    private void hideSearchListView() {
        btnUpload.setVisibility(View.VISIBLE);
        rvRecommendBuilding.setVisibility(View.GONE);
    }

    private void buildingFlayView(int type) {
        tvBuildingFlay.setVisibility(View.VISIBLE);
        if (Constants.TYPE_BUILDING == type) {
            tvBuildingFlay.setText("楼盘");
            tvBuildingFlay.setBackgroundResource(com.officego.commonlib.R.drawable.label_corners_solid_blue);
            silSelectType.setVisibility(View.VISIBLE);
            includeBusinessLicense.setVisibility(View.VISIBLE);
            includeOwnerPersonalId.setVisibility(View.GONE);
        } else {
            tvBuildingFlay.setText("网点");
            tvBuildingFlay.setBackgroundResource(com.officego.commonlib.R.drawable.label_corners_solid_purple);
            silSelectType.setVisibility(View.GONE);
            includeBusinessLicense.setVisibility(View.VISIBLE);
            includeOwnerPersonalId.setVisibility(View.GONE);
        }
    }

    @Override
    public void addImage(int imageType) {
        if (TYPE_CER == imageType) {//房产证
            mUploadType = TYPE_CER;
        } else if (TYPE_LICE == imageType) { //营业执照
            mUploadType = TYPE_LICE;
        } else if (TYPE_ADDI == imageType) {//补充材料
            mUploadType = TYPE_ADDI;
        }
        selectedDialog();
    }

    @Override
    public void deleteImage(int imageType, ImageBean bean, int position) {
        if (TYPE_CER == imageType) {//房产证
            listCertificate.remove(position);
            cerAdapter.notifyDataSetChanged();
        } else if (TYPE_LICE == imageType) { //营业执照
            listBusinessLice.remove(position);
            liceAdapter.notifyDataSetChanged();
        } else if (TYPE_ADDI == imageType) {//补充材料
            listAdditionalInfo.remove(position);
            addiAdapter.notifyDataSetChanged();
        }
    }

    //身份证照片
    private void idCardDialog(boolean isFront) {
        final String[] items = {"拍照", "从相册选择"};
        new AlertDialog.Builder(OwnerIdentityActivity.this)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        if (isFront) {
                            IDCardCamera.create(OwnerIdentityActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                        } else {
                            IDCardCamera.create(OwnerIdentityActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
                        }
                    } else if (i == 1) {
                        openGallery();
                    }
                }).create().show();
    }

    //房产认证拍照，相册
    private void selectedDialog() {
        final String[] items = {"拍照", "从相册选择"};
        new AlertDialog.Builder(OwnerIdentityActivity.this)
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
        File fileUri = null;
        if (TYPE_CER == mUploadType) {
            localCerPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + System.currentTimeMillis() + "certificate.jpg";
            fileUri = new File(localCerPath);
        } else if (TYPE_LICE == mUploadType) {
            localLicePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + System.currentTimeMillis() + "businessLice.jpg";
            fileUri = new File(localLicePath);
        } else if (TYPE_ADDI == mUploadType) {
            localAddiPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + System.currentTimeMillis() + "additionalInfo.jpg";
            fileUri = new File(localAddiPath);
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
        } else if (TYPE_LICE == mUploadType) {//营业执照
            if (listBusinessLice.size() >= 10) {
                shortTip(R.string.tip_image_upload_overlimit);
                return true;
            }
        } else if (TYPE_ADDI == mUploadType) {//补充材料
            if (listAdditionalInfo.size() >= 10) {
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
        } else if (TYPE_LICE == mUploadType) {//营业执照
            num = 10 - listBusinessLice.size();
        } else if (TYPE_ADDI == mUploadType) {//补充材料
            num = 10 - listAdditionalInfo.size();
        } else {
            num = 10;
        }
        return num;
    }

    private void openGallery() {
        if (!PermissionUtils.checkStoragePermission(this)) {
            return;
        }
        //是否上传房产证
        if (TYPE_CER == mUploadType || TYPE_LICE == mUploadType || TYPE_ADDI == mUploadType) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IDCardCamera.RESULT_CODE) {//身份证拍照
            //获取图片路径，显示图片
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面
                    localIdCardFrontPath = path;
                    rivImageFront.setImageBitmap(BitmapFactory.decodeFile(path));
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    localIdCardBackPath = path;
                    rivImageBack.setImageBitmap(BitmapFactory.decodeFile(path));
                }
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照
                if (TYPE_CER == mUploadType) {//房产证
                    ImageUtils.isSaveCropImageView(localCerPath);
                    listCertificate.add(listCertificate.size() - 1, new ImageBean(false, 0, localCerPath));
                    cerAdapter.notifyDataSetChanged();
                } else if (TYPE_LICE == mUploadType) {//营业执照
                    ImageUtils.isSaveCropImageView(localLicePath);
                    listBusinessLice.add(listBusinessLice.size() - 1, new ImageBean(false, 0, localLicePath));
                    liceAdapter.notifyDataSetChanged();
                } else if (TYPE_ADDI == mUploadType) {//补充材料
                    ImageUtils.isSaveCropImageView(localAddiPath);
                    listAdditionalInfo.add(listAdditionalInfo.size() - 1, new ImageBean(false, 0, localAddiPath));
                    addiAdapter.notifyDataSetChanged();
                }

            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                if (TYPE_CER == mUploadType) {//房产证
                    for (int i = 0; i < images.size(); i++) {
                        ImageUtils.isSaveCropImageView(images.get(i));
                        listCertificate.add(listCertificate.size() - 1, new ImageBean(false, 0, images.get(i)));
                    }
                    cerAdapter.notifyDataSetChanged();
                } else if (TYPE_LICE == mUploadType) {//营业执照
                    for (int i = 0; i < images.size(); i++) {
                        ImageUtils.isSaveCropImageView(images.get(i));
                        listBusinessLice.add(listBusinessLice.size() - 1, new ImageBean(false, 0, images.get(i)));
                    }
                    liceAdapter.notifyDataSetChanged();
                } else if (TYPE_ADDI == mUploadType) {//补充材料
                    for (int i = 0; i < images.size(); i++) {
                        ImageUtils.isSaveCropImageView(images.get(i));
                        listAdditionalInfo.add(listAdditionalInfo.size() - 1, new ImageBean(false, 0, images.get(i)));
                    }
                    addiAdapter.notifyDataSetChanged();
                } else if (TYPE_IDCARD_FRONT == mUploadType) {//身份证正面--相册
                    localIdCardFrontPath = images.get(0);
                    rivImageFront.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
                } else if (TYPE_IDCARD_BACK == mUploadType) {//身份证反面--相册
                    localIdCardBackPath = images.get(0);
                    rivImageBack.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissions(context, requestCode, permissions, grantResults);
    }

}
