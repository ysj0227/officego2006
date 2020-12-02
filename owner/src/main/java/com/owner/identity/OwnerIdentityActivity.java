package com.owner.identity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.IdentityRejectBean;
import com.officego.commonlib.common.model.SearchListBean;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.CircleImage;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.RoundImageView;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.SearchAdapter;
import com.owner.adapter.UploadImageAdapter;
import com.owner.dialog.ExitConfirmDialog;
import com.owner.dialog.IdentityTypeDialog;
import com.owner.home.utils.CommonUtils;
import com.owner.identity.contract.IdentityContract;
import com.owner.identity.model.BuildingBean;
import com.owner.identity.model.ImageBean;
import com.owner.identity.presenter.IdentityPresenter;
import com.owner.mine.MineMessageActivity_;
import com.owner.utils.CommUtils;
import com.wildma.idcardcamera.camera.IDCardCamera;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shijie
 * Date 2020/11/17
 * 认证
 **/
@EActivity(resName = "activity_owner_identity")
public class OwnerIdentityActivity extends BaseMvpActivity<IdentityPresenter>
        implements IdentityContract.View, SearchBuildingTextWatcher.SearchListener,
        IdentityTypeDialog.IdentityTypeListener, SearchAdapter.IdentityBuildingListener,
        UploadImageAdapter.UploadListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int REQUEST_CREATE_BUILDING = 0xa3;
    private static final int TYPE_IDCARD_FRONT = 1;
    private static final int TYPE_IDCARD_BACK = 2;
    private static final int TYPE_CER = 3;//产证
    private static final int TYPE_LICE = 4;//营业执照
    private static final int TYPE_ADDI = 5;//补充材料
    private static final int TYPE_AVATAR = 6;//头像
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
    @ViewById(resName = "tv_text_business_license")
    TextView tvTextBusinessLicense;
    @ViewById(resName = "tv_tip_business_license")
    TextView tvTipBusinessLicense;
    @ViewById(resName = "tv_tip_additional_info")
    TextView tvTipAdditionalInfo;
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
    //驳回
    @ViewById(resName = "rl_reason")
    RelativeLayout rlReason;
    @ViewById(resName = "iv_reject_name")
    ImageView ivRejectName;
    @ViewById(resName = "iv_reject_ownership_certificate")
    ImageView ivRejectOwnershipCertificate;
    @ViewById(resName = "iv_reject_business_license")
    ImageView ivRejectBusinessLicense;
    @ViewById(resName = "iv_reject_idcard")
    ImageView ivRejectIdcard;
    @ViewById(resName = "iv_reject_additional_info")
    ImageView ivRejectAdditionalInfo;
    @Extra
    int flag;
    @Extra
    int buildingId;

    //是否展开
    private boolean isSpread;
    //搜索
    private SearchAdapter searchAdapter;
    private List<SearchListBean.DataBean> mList = new ArrayList<>();
    //上传类型
    private int mUploadType;
    //本地路径
    private String localCerPath, localLicePath, localAddiPath, localAvatarPath;
    private Uri localPhotoUri;
    //上传图片
    private List<ImageBean> listCertificate = new ArrayList<>();
    private List<ImageBean> listBusinessLice = new ArrayList<>();
    private List<ImageBean> listAdditionalInfo = new ArrayList<>();
    private UploadImageAdapter cerAdapter, liceAdapter, addiAdapter;
    //驳回原因
    @SuppressLint("UseSparseArrays")
    private Map<Integer, String> map = new HashMap<>();
    //提交信息
    private int buildingType;
    private String buildingName;
    private String mainPic;
    private int isHolder; //权利人类型1个人2公司
    private String area;//区域
    private int districtId, businessId;
    private String address;
    private String idFront, idBack;
    private String buildId = "";//关联楼id
    //驳回-如果驳回的名称提交校验身份改变
    private IdentityRejectBean currentRejectData;
    //个人信息
    private String avatarUrl;
    private CircleImage civAvatar;
    private UserMessageBean mUserBean;
    private Dialog userDialog;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new IdentityPresenter(this, flag);
        mPresenter.attachView(this);
        titleBar.getLeftImg().setOnClickListener(view -> new ExitConfirmDialog(this));
        initViews();
        initData();
        mPresenter.getUserInfo(context);   //是否填写名片
        if (Constants.IDENTITY_REJECT == flag) {  //驳回
            mPresenter.getIdentityMessage(buildingId);
        }
    }

    private void initViews() {
        //title
        titleBar.setAppTitle(Constants.IDENTITY_NO_FIRST == flag ? "添加楼盘/网点" : "房东认证");
        //搜索列表
        LinearLayoutManager buildingManager = new LinearLayoutManager(context);
        rvRecommendBuilding.setLayoutManager(buildingManager);
        SearchBuildingTextWatcher textWatcher = new SearchBuildingTextWatcher();
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

    @Click(resName = "rl_reason")
    void spreadOnClick() {
        isSpread = !isSpread;
        tvRejectReason.setSingleLine(isSpread);
        ivExpand.setBackgroundResource(isSpread ?
                R.mipmap.ic_down_arrow_gray :
                R.mipmap.ic_up_arrow_gray);
    }

    @Click(resName = "sil_select_type")
    void selectTypeOnClick() {
        new IdentityTypeDialog(context).setListener(this);
    }

    @Click(resName = "iv_edit")
    void editOnClick() {
        BuildingBean bean = new BuildingBean();
        bean.setName(buildingName);
        bean.setBuildingType(buildingType);
        bean.setDistrictId(districtId);
        bean.setBusinessId(businessId);
        bean.setAddress(address);
        bean.setMainPic(mainPic);
        bean.setArea(area);
        OwnerCreateBuildingActivity_.intent(context)
                .isEdit(true)
                .buildingBean(bean)
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

    @Click(resName = "btn_upload")
    void submitClick() {
        if (TextUtils.isEmpty(buildingName)) {
            shortTip("请输入要认证的楼盘/网点名称");
            return;
        }
        if (listCertificate.size() <= 1) {
            shortTip("请上传房产证");
            return;
        }
        String selectType = silSelectType.getContextView().getText() == null ? "" :
                silSelectType.getContextView().getText().toString();
        if (silSelectType.getVisibility() == View.VISIBLE && TextUtils.isEmpty(selectType)) {
            shortTip("请选择权利人类型");
            return;
        }
        if (isHolder == 1) {
            if (TextUtils.isEmpty(idFront)) {
                shortTip("请上传身份证正面");
                return;
            }
            if (TextUtils.isEmpty(idBack)) {
                shortTip("请上传身份证反面");
                return;
            }
        }
        if (isHolder == 2 || Constants.TYPE_JOINTWORK == buildingType) {
            if (listBusinessLice.size() <= 1) {
                shortTip("请上传营业执照");
                return;
            }
        }
        //如果驳回名称
        if (isRejectModifyNameMessage()) {
            shortTip("请修改楼盘/网点信息");
            return;
        }
        //上传图片
        String premisesPermit = CommonUtils.addAllUploadImage(listCertificate);
        //营业执照
        String businessLicense = CommonUtils.addAllUploadImage(listBusinessLice);
        //补充材料
        String materials = CommonUtils.addAllUploadImage(listAdditionalInfo);
        mPresenter.submitIdentityMessage(buildingType, Constants.IDENTITY_FIRST == flag ? 1 : 2, buildingName,
                mainPic, premisesPermit, businessLicense,
                materials, idFront, idBack, isHolder,
                buildId, buildingId, districtId, businessId,
                address);
    }

    //驳回-获取认证信息
    @Override
    public void identityMessageSuccess(IdentityRejectBean bean) {
        currentRejectData = bean;
        //驳回原因
        rejectReasons(bean);
        //编辑删除
        ivDelete.setVisibility(View.VISIBLE);
        //楼盘网点名称信息
        ivEdit.setVisibility(TextUtils.isEmpty(bean.getBuildId()) ||
                TextUtils.equals("0", bean.getBuildId()) ? View.VISIBLE : View.GONE);
        buildId = bean.getBuildId(); //buildId 关联楼id
        mainPic = bean.getMainPic();
        buildingType = Integer.valueOf(bean.getBtype());
        buildingName = bean.getBuildingName();
        if (!TextUtils.isEmpty(bean.getDistrictId())) {
            districtId = Integer.valueOf(bean.getDistrictId());
        }
        if (!TextUtils.isEmpty(bean.getBusinessDistrict())) {
            businessId = Integer.valueOf(bean.getBusinessDistrict());
        }
        area = bean.getProvincetName() + bean.getDistrictIdName() + bean.getBusinessDistrictName();
        address = bean.getAddress();
        //信息回显
        buildingFlayView(buildingType);
        cetName.setText(buildingName);
        cetName.setSelection(cetName.getText().length());//光标
        cetName.setEnabled(false);
        if (!TextUtils.isEmpty(bean.getAddress())) {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(new StringBuilder().append(bean.getProvincetName()).append(bean.getDistrictIdName())
                    .append(bean.getBusinessDistrictName()).append(bean.getAddress()).toString());
        }
        hideSearchListView();//隐藏搜索
        //权利人类型
        if (!TextUtils.isEmpty(bean.getIsHolder()) &&
                !TextUtils.equals("0", bean.getIsHolder())) {
            setIdentityType(Integer.valueOf(bean.getIsHolder()));
        }
        if (map.containsKey(1)) {
            ivRejectName.setVisibility(View.VISIBLE);
        }
        //房产证
        if (!map.containsKey(2)) {
            showImageList(cerAdapter, bean.getPremisesPermit(), listCertificate);
        } else {
            ivRejectOwnershipCertificate.setVisibility(View.VISIBLE);
        }
        //营业执照
        if (!map.containsKey(3)) {
            showImageList(liceAdapter, bean.getBusinessLicense(), listBusinessLice);
        } else {
            ivRejectBusinessLicense.setVisibility(View.VISIBLE);
        }
        //身份证
        if (!map.containsKey(4)) {
            idFrontImage(bean.getIdFront());
            idBackImage(bean.getIdBack());
        } else {
            ivRejectIdcard.setVisibility(View.VISIBLE);
        }
        //补充材料
        if (!map.containsKey(5)) {
            showImageList(addiAdapter, bean.getMaterials(), listAdditionalInfo);
        } else {
            ivRejectAdditionalInfo.setVisibility(View.VISIBLE);
        }
    }

    //图片
    private void showImageList(UploadImageAdapter adapter, List<String> list,
                               List<ImageBean> imageBeanList) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String path = list.get(i);
                imageBeanList.add(imageBeanList.size() - 1, new ImageBean(true, 0, path));
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 驳回原因
     * 1 名称不规范
     * 2 地址不规范
     * 3 房产证不清楚
     * 4 房产证与认证楼盘不一致
     * 5 营业执照不清楚
     * 6 营业执照与权利人不一致
     * 7 身份证不清楚
     * 8 身份证与房产证不一致
     * 9 补充材料不清楚
     * 10 无相关补充材料或材料无法证明其关系
     */
    private void rejectReasons(IdentityRejectBean bean) {
        rlReason.setVisibility(View.VISIBLE);
        tvRejectReason.setVisibility(View.VISIBLE);
        StringBuilder builder = new StringBuilder();
        if (bean.getRemark() != null && bean.getRemark().size() > 0) {
            int key;
            String value;
            for (int i = 0; i < bean.getRemark().size(); i++) {
                key = bean.getRemark().get(i).getDictCode();
                value = bean.getRemark().get(i).getDictCname();
                if (i == bean.getRemark().size() - 1) {
                    builder.append(value);
                } else {
                    builder.append(value).append("\n");
                }
                map.put(key, value);
            }
            tvRejectReason.setText("驳回原因：" + builder.toString());
        } else {
            tvRejectReason.setText("驳回原因：无");
        }
    }

    /**
     * 驳回-是否修改名称信息
     * 1 名称不规范
     * 2 地址不规范
     */
    private boolean isRejectModifyNameMessage() {
        if (Constants.IDENTITY_REJECT == flag) {
            if (map.containsKey(1)) {
                return TextUtils.equals(buildingName, currentRejectData.getBuildingName()) &&
                        TextUtils.equals(address, currentRejectData.getAddress()) &&
                        TextUtils.equals(String.valueOf(districtId),
                                TextUtils.isEmpty(currentRejectData.getDistrictId()) ? "0" : currentRejectData.getDistrictId()) &&
                        TextUtils.equals(String.valueOf(businessId),
                                TextUtils.isEmpty(currentRejectData.getBusinessDistrict()) ? "0" : currentRejectData.getBusinessDistrict());
            }
            return false;
        }
        return false;
    }

    @Override
    public void sureType(String text, int type) {
        setIdentityType(type);
    }

    //身份选择
    private void setIdentityType(int type) {
        //1公司 2个人（自定义参数）
        isHolder = type;
        if (type == 1) {
            silSelectType.setCenterText("个人");
            tvTipAdditionalInfo.setText("若需要，请上传身份信息与产证信息一致的相关材料");
        } else if (type == 2) {
            silSelectType.setCenterText("公司");
            tvTextBusinessLicense.setText("上传营业执照");
            tvTipBusinessLicense.setText("请确保上传与房产证上权利人名称相同的公司营业执照");
            tvTipAdditionalInfo.setText("请上传以公司为主体的房屋租赁协议或其他相关材料");
        }
        includeBusinessLicense.setVisibility(type == 2 ? View.VISIBLE : View.GONE);
        includeOwnerPersonalId.setVisibility(type == 2 ? View.GONE : View.VISIBLE);
    }

    //搜索楼盘网点
    @Override
    public void searchEditTextList(String str) {
        showSearchListView();
        mPresenter.searchList(str);
    }

    @Override
    public void searchBuildingSuccess(List<SearchListBean.DataBean> data) {
        mList.clear();
        mList.addAll(data);
        mList.add(data.size(), new SearchListBean.DataBean());
        if (searchAdapter == null) {
            searchAdapter = new SearchAdapter(context, mList);
            searchAdapter.setListener(this);
            rvRecommendBuilding.setAdapter(searchAdapter);
            return;
        }
        searchAdapter.setData(mList);
        searchAdapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void associateBuilding(SearchListBean.DataBean bean, boolean isCreate) {
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
            //设置提交信息
            buildingType = bean.getBuildType();
            buildingName = cetName.getText().toString();
            mainPic = bean.getMainPic();
            districtId = 0;
            businessId = 0;
            buildId = String.valueOf(bean.getBid());
            area = (bean.getDistrict() == null ? "" : (String) bean.getDistrict()) +
                    (bean.getBusiness() == null ? "" : (String) bean.getBusiness());
            address = tvAddress.getText() == null ? "" : tvAddress.getText().toString();
            tvAddress.setText(area + address);
        }
        ivDelete.setVisibility(View.VISIBLE);
    }

    //创建楼盘成功的回调
    @OnActivityResult(REQUEST_CREATE_BUILDING)
    void onCreateBuildingResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            BuildingBean bean = (BuildingBean) data.getSerializableExtra("buildingMessage");
            if (bean != null) {
                //设置提交信息
                buildingType = bean.getBuildingType();
                buildingName = bean.getName();
                mainPic = bean.getMainPic();
                districtId = bean.getDistrictId();
                businessId = bean.getBusinessId();
                area = bean.getArea();
                address = bean.getAddress();
                buildId = "0";
                //
                tvAddress.setVisibility(View.VISIBLE);
                ivEdit.setVisibility(View.VISIBLE);
                buildingFlayView(buildingType);
                cetName.setText(buildingName);
                tvAddress.setText(area + address);
                cetName.setEnabled(false);
                hideSearchListView();
            }
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
            if (isHolder == 2) {//公司
                includeBusinessLicense.setVisibility(View.VISIBLE);
                includeOwnerPersonalId.setVisibility(View.GONE);
                tvTextBusinessLicense.setText("上传营业执照");
                tvTipBusinessLicense.setText("请确保上传与房产证上权利人名称相同的公司营业执照");
                tvTipAdditionalInfo.setText("请上传以公司为主体的房屋租赁协议或其他相关材料");
            } else if (isHolder == 1) {//个人
                includeBusinessLicense.setVisibility(View.GONE);
                includeOwnerPersonalId.setVisibility(View.VISIBLE);
            }
        } else {
            tvBuildingFlay.setText("网点");
            tvBuildingFlay.setBackgroundResource(com.officego.commonlib.R.drawable.label_corners_solid_purple);
            silSelectType.setVisibility(View.GONE);
            includeBusinessLicense.setVisibility(View.VISIBLE);
            includeOwnerPersonalId.setVisibility(View.GONE);
            tvTextBusinessLicense.setText("上传共享办公营业执照");
            tvTipBusinessLicense.setText("请确保上传的共享办公营业执照清晰可辨识");
            tvTipAdditionalInfo.setText("请上传以共享办公为主体的房屋租赁协议或其他相关材料");
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

    //上传图片成功
    @Override
    public void uploadSuccess(int imageType, UploadImageBean data) {
        if (data != null && data.getUrls() != null && data.getUrls().size() > 0) {
            int urlSize = data.getUrls().size();
            if (TYPE_CER == imageType) {//房产证
                addListImage(urlSize, data, listCertificate, cerAdapter);
            } else if (TYPE_LICE == imageType) { //营业执照
                addListImage(urlSize, data, listBusinessLice, liceAdapter);
            } else if (TYPE_ADDI == imageType) {//补充材料
                addListImage(urlSize, data, listAdditionalInfo, addiAdapter);
            } else if (TYPE_IDCARD_FRONT == imageType) {//身份证正面
                idFrontImage(data.getUrls().get(0).getUrl());
            } else if (TYPE_IDCARD_BACK == imageType) {//身份证反面
                idBackImage(data.getUrls().get(0).getUrl());
            } else if (TYPE_AVATAR == imageType) {//头像
                avatarUrl = data.getUrls().get(0).getUrl();
                Glide.with(context).load(avatarUrl).into(civAvatar);
            }
            if (TYPE_AVATAR != imageType) {
                shortTip("上传成功");
            }
        }
    }

    /**
     * 获取用户信息
     */
    @Override
    public void userInfoSuccess(UserMessageBean data) {
        if (data != null) {
            if (!data.isIsUserInfo()) {
                avatarUrl = data.getAvatar();
                mUserBean = data;
                cardDialog(data);
            }
        }
    }

    /**
     * 更新用户成功
     */
    @Override
    public void updateUserSuccess() {
        shortTip("名片保存成功");
        if (userDialog != null) {
            userDialog.dismiss();
        }
    }

    //上传成功添加列表图片
    private void addListImage(int urlSize, UploadImageBean data, List<ImageBean> list,
                              UploadImageAdapter adapter) {
        ImageBean bean;
        for (int i = 0; i < urlSize; i++) {
            bean = new ImageBean(true, 0, data.getUrls().get(i).getUrl());
            list.add(list.size() - 1, bean);
        }
        adapter.notifyDataSetChanged();
    }

    //身份证正面
    private void idFrontImage(String path) {
        if (!TextUtils.isEmpty(path)) {
            idFront = path;
            Glide.with(context).load(idFront).into(rivImageFront);
            tvUploadFront.setText(getString(R.string.str_re_upload));
            tvUploadFront.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    //身份证反面
    private void idBackImage(String path) {
        if (!TextUtils.isEmpty(path)) {
            idBack = path;
            Glide.with(context).load(idBack).into(rivImageBack);
            tvUploadBack.setText(getString(R.string.str_re_upload));
            tvUploadBack.setTextColor(ContextCompat.getColor(context, R.color.white));
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
        } else if (TYPE_AVATAR == mUploadType) {//头像
            localAvatarPath = FileHelper.SDCARD_CACHE_IMAGE_PATH + System.currentTimeMillis() + "avatarPath.jpg";
            fileUri = new File(localAvatarPath);
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
                    uploadSingleImage(TYPE_IDCARD_FRONT, path);
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    uploadSingleImage(TYPE_IDCARD_BACK, path);
                }
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照
                if (TYPE_CER == mUploadType) {//房产证
                    uploadSingleImage(TYPE_CER, localCerPath);
                } else if (TYPE_LICE == mUploadType) {//营业执照
                    uploadSingleImage(TYPE_LICE, localLicePath);
                } else if (TYPE_ADDI == mUploadType) {//补充材料
                    uploadSingleImage(TYPE_ADDI, localAddiPath);
                } else if (TYPE_AVATAR == mUploadType) {//头像图片
                    uploadSingleImage(TYPE_AVATAR, localAvatarPath);
                }
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                if (TYPE_CER == mUploadType) {//房产证
                    uploadMultiImage(TYPE_CER, images);
                } else if (TYPE_LICE == mUploadType) {//营业执照
                    uploadMultiImage(TYPE_LICE, images);
                } else if (TYPE_ADDI == mUploadType) {//补充材料
                    uploadMultiImage(TYPE_ADDI, images);
                } else if (TYPE_IDCARD_FRONT == mUploadType) {//身份证正面--相册
                    uploadSingleImage(TYPE_IDCARD_FRONT, images.get(0));
                } else if (TYPE_IDCARD_BACK == mUploadType) {//身份证反面--相册
                    uploadSingleImage(TYPE_IDCARD_BACK, images.get(0));
                } else if (TYPE_AVATAR == mUploadType) {//头像图片
                    uploadSingleImage(TYPE_AVATAR, images.get(0));
                }
            }
        }
    }

    //单张图片处理
    private void uploadSingleImage(int type, String path) {
        ImageUtils.isSaveCropImageView(path);
        List<String> stringList = new ArrayList<>();
        stringList.add(path);
        mPresenter.uploadImage(type, stringList);
    }

    //多张图片处理
    private void uploadMultiImage(int type, List<String> images) {
        List<String> imagesNew = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageUtils.isSaveCropImageView(images.get(i));
            imagesNew.add(images.get(i));
        }
        mPresenter.uploadImage(type, imagesNew);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissions(context, requestCode, permissions, grantResults);
    }

    /**
     * 更新卡片
     */
    private void cardDialog(UserMessageBean data) {
        userDialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_mine_card, null);
        userDialog.setContentView(viewLayout);
        handleLayout(viewLayout, userDialog, data);
        userDialog.setCanceledOnTouchOutside(false);
        userDialog.show();
    }

    private void handleLayout(View viewLayout, Dialog dialog, UserMessageBean data) {
        civAvatar = viewLayout.findViewById(R.id.civ_avatar);
        ClearableEditText cetName = viewLayout.findViewById(R.id.cet_name);
        ClearableEditText cetJob = viewLayout.findViewById(R.id.cet_job);
        Button save = viewLayout.findViewById(R.id.btn_save);
        TextView tvMore = viewLayout.findViewById(R.id.tv_more);
        viewLayout.findViewById(R.id.rl_exit).setOnClickListener(v -> dialog.dismiss());
        if (data.isNickname()) {
            cetName.setText(data.getNickname());
            cetName.setSelection(data.getNickname().length());
        }
        cetJob.setText(data.getJob());
        Glide.with(context).load(data.getAvatar()).into(civAvatar);
        View.OnClickListener listener = view -> {
            int id = view.getId();
            if (id == R.id.civ_avatar) { //头像
                mUploadType = TYPE_AVATAR;
                selectedDialog();
            } else if (id == R.id.btn_save) { //保存
                String nickName = cetName.getText() == null ? "" : cetName.getText().toString();
                if (TextUtils.isEmpty(nickName)) {
                    shortTip(R.string.str_input_nick_name);
                    return;
                }
                String job = cetJob.getText() == null ? "" : cetJob.getText().toString();
                if (TextUtils.isEmpty(job)) {
                    job = mUserBean.getJob();
                }
                //更新用户信息
                String sex = TextUtils.isEmpty(mUserBean.getSex()) ? "1" : mUserBean.getSex();
                mPresenter.updateUserInfo(avatarUrl, nickName, sex, job, mUserBean.getWxId());
            } else if (id == R.id.tv_more) { //个人信息
                MineMessageActivity_.intent(context).mUserInfo(data).start();
                dialog.dismiss();
            }
        };
        civAvatar.setOnClickListener(listener);
        save.setOnClickListener(listener);
        tvMore.setOnClickListener(listener);
    }
}
