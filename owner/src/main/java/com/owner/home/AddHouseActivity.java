package com.owner.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.dialog.RentDialog;
import com.officego.commonlib.common.model.BuildingManagerBean;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.EditInputFilter;
import com.officego.commonlib.utils.FileHelper;
import com.officego.commonlib.utils.FileUtils;
import com.officego.commonlib.utils.ImageUtils;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.PhotoUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.adapter.HouseDecorationAdapter;
import com.owner.adapter.UniqueAdapter;
import com.owner.adapter.UploadBuildingImageAdapter;
import com.owner.dialog.FloorTypeDialog;
import com.owner.home.contract.HouseContract;
import com.owner.home.presenter.HousePresenter;
import com.owner.home.rule.BuildingHouseAreaTextWatcher;
import com.owner.home.rule.EstateFeeTextWatcher;
import com.owner.home.rule.FloorHeightTextWatcher;
import com.owner.home.rule.IntegerTextWatcher;
import com.owner.home.rule.RentSingleTextWatcher;
import com.owner.home.rule.RentSumTextWatcher;
import com.owner.home.rule.TextCountsWatcher;
import com.owner.identity.model.ImageBean;
import com.owner.utils.SpaceItemDecoration;
import com.owner.zxing.QRScanActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
@EActivity(resName = "activity_home_house_manager")
public class AddHouseActivity extends BaseMvpActivity<HousePresenter>
        implements HouseContract.View, RentDialog.SureClickListener,
        FloorTypeDialog.FloorListener, UniqueAdapter.UniqueListener,
        HouseDecorationAdapter.DecorationListener,
        UploadBuildingImageAdapter.UploadImageListener {
    private static final int REQUEST_GALLERY = 0xa0;
    private static final int REQUEST_CAMERA = 0xa1;
    private static final int TYPE_INTRODUCE = 1;
    private static final int TYPE_BANNER = 2;
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "tv_upload_title")
    TextView tvUploadTitle;
    @ViewById(resName = "tv_des_title")
    TextView tvDesTitle;
    @ViewById(resName = "tv_house_characteristic")
    TextView tvHouseCharacteristic;
    @ViewById(resName = "sil_house_title")
    SettingItemLayout silHouseTitle;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "et_seat_start")
    EditText etSeatStart;
    @ViewById(resName = "et_seat_end")
    EditText etSeatEnd;
    @ViewById(resName = "sil_rent_single")
    SettingItemLayout silRentSingle;
    @ViewById(resName = "tv_rent_sum_tip")
    TextView tvRentSumTip;
    @ViewById(resName = "sil_rent_sum")
    SettingItemLayout silRentSum;
    @ViewById(resName = "sil_floor_no")
    SettingItemLayout silFloorNo;
    //第几层 总楼层
    @ViewById(resName = "et_floors")
    EditText etFloors;
    @ViewById(resName = "tv_counts_floor")
    TextView tvCountsFloor;
    //净高
    @ViewById(resName = "sil_storey_height")
    SettingItemLayout silStoreyHeight;
    @ViewById(resName = "sil_tier_height")
    SettingItemLayout silTierHeight;
    @ViewById(resName = "sil_rent_time")
    SettingItemLayout silRentTime;
    @ViewById(resName = "sil_free_rent")
    SettingItemLayout silFreeRent;
    @ViewById(resName = "sil_estate_fee")
    SettingItemLayout silEstateFee;
    //介绍
    @ViewById(resName = "tv_counts")
    TextView tvCounts;
    @ViewById(resName = "cet_desc_content")
    ClearableEditText cetDescContent;
    @ViewById(resName = "rv_decoration_type")
    RecyclerView rvDecorationType;
    @ViewById(resName = "rv_house_characteristic")
    RecyclerView rvHouseUnique;
    //户型图
    @ViewById(resName = "iv_desc_image")
    ImageView ivDescImage;
    //图片list
    @ViewById(resName = "rv_upload_image")
    RecyclerView rvUploadImage;
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;
    //是否添加还是编辑
    @Extra
    int buildingFlag;
    @Extra
    BuildingManagerBean buildingManagerBean;

    //记录上次面积
    private String recordArea;
    //面积是否修改
    private boolean isFixArea;
    //租金总价
    private int rentCounts;
    //特色
    private Map<Integer, String> uniqueMap;
    //装修类型
    private int decorationId;
    //上传图片
    private List<ImageBean> uploadImageList = new ArrayList<>();
    private UploadBuildingImageAdapter imageAdapter;
    private String localImagePath;
    //户型介绍图片
    private String introduceImageUrl;
    //图片上传类型
    private int mUploadType;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new HousePresenter();
        mPresenter.attachView(this);
        initViews();
        initDigits();
        itemListener();
        if (buildingFlag == Constants.BUILDING_FLAG_EDIT) {
            mPresenter.getHouseEdit(buildingManagerBean.getBuildingId(), buildingManagerBean.getIsTemp());
        } else {
            mPresenter.getDecoratedType();
            mPresenter.getHouseUnique();
        }
    }

    private void initViews() {
        titleBar.setAppTitle(buildingFlag == Constants.BUILDING_FLAG_ADD ? "添加办公室" : "编辑办公室");
        tvUploadTitle.setText("上传办公室图片");
        tvHouseCharacteristic.setText("办公室特色");
        tvDesTitle.setText("户型格局介绍");
        //特色
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        rvHouseUnique.setLayoutManager(layoutManager);
        rvHouseUnique.addItemDecoration(new SpaceItemDecoration(context, 3));
        //装修类型
        GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
        rvDecorationType.setLayoutManager(layoutManager1);
        rvDecorationType.addItemDecoration(new SpaceItemDecoration(context, 3));
        //上传图片
        GridLayoutManager layoutManager2 = new GridLayoutManager(context, 3);
        layoutManager2.setSmoothScrollbarEnabled(true);
        layoutManager2.setAutoMeasureEnabled(true);
        rvUploadImage.setLayoutManager(layoutManager2);
        rvUploadImage.setNestedScrollingEnabled(false);
        initData();
    }

    private void initData() {
        //初始化本地图路径
        localImagePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + SpUtils.getUserId() + "addBuildingHousePath.jpg";
        //初始化图片默认添加一个
        uploadImageList.add(new ImageBean(false, 0, ""));
        imageAdapter = new UploadBuildingImageAdapter(context, uploadImageList);
        imageAdapter.setListener(this);
        rvUploadImage.setAdapter(imageAdapter);
    }

    private void initDigits() {
        //标题 长度最大25
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silHouseTitle.getEditTextView(), 25);
        //面积 10-100000正数数字，保留2位小数，单位 M
        silArea.getEditTextView().addTextChangedListener(new BuildingHouseAreaTextWatcher(context, silArea.getEditTextView()));
        //净高 层高 0-8或一位小数
        silStoreyHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(context, silStoreyHeight.getEditTextView()));
        silTierHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(context, silTierHeight.getEditTextView()));
        //最短租期
        silRentTime.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 60, silRentTime.getEditTextView()));
        //租金单价
        silRentSingle.getEditTextView().addTextChangedListener(new RentSingleTextWatcher(context, silRentSingle.getEditTextView()));
        //租金总价
        silRentSum.getEditTextView().addTextChangedListener(new RentSumTextWatcher(context, silRentSum.getEditTextView()));
        //物业费 0-100000之间正数，保留1位小数
        silEstateFee.getEditTextView().addTextChangedListener(new EstateFeeTextWatcher(context, 100000, silEstateFee.getEditTextView()));
        //介绍
        cetDescContent.addTextChangedListener(new TextCountsWatcher(tvCounts, cetDescContent));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
//        UploadVideoVrActivity_.intent(context).start();
        submit();
    }

    private void submit() {
        String area = silArea.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(area)) {
            shortTip("请输入面积");
            return;
        }
        String minSeats = etSeatStart.getText().toString();
        if (TextUtils.isEmpty(minSeats)) {
            shortTip("请输入最小工位数");
            return;
        }
        String maxSeats = etSeatEnd.getText().toString();
        if (TextUtils.isEmpty(maxSeats)) {
            shortTip("请输入最大工位数");
            return;
        }
        String rentSingle = silRentSingle.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(rentSingle)) {
            shortTip("请输入租金单价");
            return;
        }
        String rentSum = silRentSum.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(rentSum)) {
            shortTip("请输入租金总价");
            return;
        }
        String floors = etFloors.getText().toString();
        if (TextUtils.isEmpty(floors)) {
            shortTip("请输入楼层");
            return;
        }
        String storeyHeight = silStoreyHeight.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(storeyHeight)) {
            shortTip("请输入净高");
            return;
        }
        String rentTime = silRentTime.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(rentTime)) {
            shortTip("请输入最短租期");
            return;
        }
        if (decorationId == 0) {
            shortTip("请选择装修程度");
            return;
        }
    }

    @Click(resName = "iv_close_scan")
    void closeScanOnClick() {
        btnScan.setVisibility(View.GONE);
        ivCloseScan.setVisibility(View.GONE);
    }

    //web 去编辑
    @Click(resName = "btn_scan")
    void toWebEditOnClick() {
        if (!PermissionUtils.checkSDCardCameraPermission(this)) {
            return;
        }
        startActivity(new Intent(context, QRScanActivity.class));
    }

    @Click(resName = "sil_free_rent")
    void freeRentOnClick() {
        new RentDialog(context, getString(R.string.str_free_rent)).setSureListener(this);
    }

    @Click(resName = "iv_seat_tip")
    void seatOnClick() {
        CommonDialog dialog = new CommonDialog.Builder(this)
                .setTitle("可置工位根据面积生成，可修改")
                .setConfirmButton("我知道了", (dialog12, which) -> {
                    dialog12.dismiss();
                }).create();
        dialog.showWithOutTouchable(false);
    }

    @Click(resName = "sil_rent_sum")
    void rentSumOnClick() {
        CommonDialog dialog = new CommonDialog.Builder(this)
                .setTitle("总价根据单价生成，可修改")
                .setMessage("总价计算规则：单价X面积X天数\n总价：0.3万元/月（1元 X 100㎡ X 30天）")
                .setConfirmButton("我知道了", (dialog12, which) -> {
                    dialog12.dismiss();
                }).create();
        dialog.showWithOutTouchable(false);
    }

    @FocusChange(resName = "et_seat_start")
    void onStartSeatFocusChange(View view, boolean isFocus) {
        if (isFocus) setSeats();
    }

    @FocusChange(resName = "et_seat_end")
    void onEndSeatFocusChange(View view, boolean isFocus) {
        if (isFocus) setSeats();
    }

    @Click(resName = "tv_rent_sum_tip")
    void textRentSumTipOnClick() {
        silRentSum.setEditText(rentCounts + "");
        tvRentSumTip.setVisibility(View.GONE);
    }

    @Click(resName = "iv_desc_image")
    void layoutIntroduceOnClick() {
        mUploadType = TYPE_INTRODUCE;
        selectedDialog();
    }

    private void itemListener() {
        //租金总价
        silRentSum.getEditTextView().setOnFocusChangeListener((view, b) -> {
            String area = silArea.getEditTextView().getText().toString();//整数
            String rentSingle = silRentSingle.getEditTextView().getText().toString();//保留两位小数
            if (!TextUtils.isEmpty(area) && !TextUtils.isEmpty(rentSingle)) {
                if (b) {
                    rentCounts = (int) (Float.valueOf(rentSingle) * Float.valueOf(area) * 30);
                    tvRentSumTip.setVisibility(View.VISIBLE);
                    tvRentSumTip.setText("租金总价：" + rentCounts + "元/月");
                } else {
                    tvRentSumTip.setVisibility(View.GONE);
                }
            }
        });
        //面积监听 是否修改过
        silArea.getEditTextView().setOnFocusChangeListener((view, b) -> {
            if (b) {
                isFixArea = !TextUtils.equals(recordArea, silArea.getEditTextView().getText().toString());
            }
        });
        silArea.getEditTextView().addTextChangedListener(new MyTextWatcher(recordArea));
    }

    @SuppressLint("SetTextI18n")
    private void setSeats() {
        String seatStart = etSeatStart.getText().toString();
        String seatEnd = etSeatEnd.getText().toString();
        String area = silArea.getEditTextView().getText().toString();
        if (!TextUtils.isEmpty(area)) {
            if (isFixArea || (TextUtils.isEmpty(seatStart) && TextUtils.isEmpty(seatEnd))) {
                recordArea = silArea.getEditTextView().getText().toString();
                etSeatStart.setText((Integer.valueOf(area) / 5) + "");
                etSeatEnd.setText((Integer.valueOf(area) / 3) + "");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void houseEditSuccess(HouseEditBean data) {
        if (data == null) return;
        if (data.getHouseMsg() != null) {
            //标题
            silHouseTitle.getEditTextView().setText(data.getHouseMsg().getTitle());
            //面积
            silArea.getEditTextView().setText(data.getHouseMsg().getArea() + "");
            //工位
            String seats = data.getHouseMsg().getSimple();
            etSeatStart.setText(CommonHelper.minData(seats));
            etSeatEnd.setText(CommonHelper.maxData(seats));
            //租金单价总价
            silRentSingle.getEditTextView().setText(data.getHouseMsg().getDayPrice() + "");
            silRentSum.getEditTextView().setText(data.getHouseMsg().getMonthPrice() + "");
            //楼层
            etFloors.setText(data.getHouseMsg().getFloor());
            tvCountsFloor.setText("总" + "" + "层");
            //净高层高
            silStoreyHeight.getEditTextView().setText(data.getHouseMsg().getClearHeight());
            silTierHeight.getEditTextView().setText(data.getHouseMsg().getStoreyHeight());
            //租期
            silRentTime.getEditTextView().setText(data.getHouseMsg().getMinimumLease());
            silFreeRent.getEditTextView().setText(data.getHouseMsg().getRentFreePeriod());
            //物业费 0不包含，1包含
            silEstateFee.getEditTextView().setText(data.getHouseMsg().getPropertyHouseCosts());
            //装修类型 1毛坯房屋，2简装 ，3豪华，4标准，5精装，6不带窗口，7带家具，8带窗口
            decorationId = data.getHouseMsg().getDecoration();
            //特色
            List<String> result = CommonHelper.stringList(data.getHouseMsg().getTags());
            if (result != null) {
                uniqueMap = new HashMap<>();
                for (int i = 0; i < result.size(); i++) {
                    uniqueMap.put(Integer.valueOf(result.get(i)), "");
                }
            }
            mPresenter.getDecoratedType();
            mPresenter.getHouseUnique();
            //介绍
            cetDescContent.setText(data.getHouseMsg().getUnitPatternRemark());
            //户型介绍图
            introduceImageUrl = data.getHouseMsg().getUnitPatternImg();
            Glide.with(context).load(data.getHouseMsg().getUnitPatternImg()).into(ivDescImage);
            //办公室图片
            showImage(data);
        }
    }

    //办公室图片
    private void showImage(HouseEditBean data) {
        //封面图
        if (!TextUtils.isEmpty(data.getHouseMsg().getMainPic())) {
            uploadImageList.add(uploadImageList.size() - 1, new ImageBean(true, 0, data.getHouseMsg().getMainPic()));
        }
        //浏览图
        for (int i = 0; i < data.getBanner().size(); i++) {
            uploadImageList.add(uploadImageList.size() - 1, new ImageBean(true, 0, data.getBanner().get(i).getImgUrl()));
        }
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void houseUniqueSuccess(List<DirectoryBean.DataBean> data) {
        UniqueAdapter adapter = new UniqueAdapter(context, uniqueMap, data);
        adapter.setListener(this);
        rvHouseUnique.setAdapter(adapter);
    }

    @Override
    public void decoratedTypeSuccess(List<DirectoryBean.DataBean> data) {
        HouseDecorationAdapter decorationAdapter = new HouseDecorationAdapter(context, decorationId, data);
        decorationAdapter.setListener(this);
        rvDecorationType.setAdapter(decorationAdapter);
    }

    @Override
    public void uploadSuccess(boolean isIntroduceLayout, UploadImageBean data) {
        if (data != null && data.getUrls() != null && data.getUrls().size() > 0) {
            int urlSize = data.getUrls().size();
            if (isIntroduceLayout) {//户型介绍
                introduceImageUrl = data.getUrls().get(0).getUrl();
                Glide.with(context).load(introduceImageUrl).into(ivDescImage);
            } else {//多图
                ImageBean bean;
                for (int i = 0; i < urlSize; i++) {
                    bean = new ImageBean(true, 0, data.getUrls().get(i).getUrl());
                    uploadImageList.set(uploadImageList.size() - 1 - urlSize + i, bean);
                }
                imageAdapter.notifyDataSetChanged();
            }
            shortTip("上传成功");
        }
    }

    @Override
    public void selectedRent(String str) {
        silFreeRent.setLeftToArrowText(str);
    }

    @Override
    public void sureFloor(String text,String type) {
        silFloorNo.setLeftToArrowText(text);
    }

    @Override
    public void decorationResult(int decId) {
        this.decorationId = decId;
    }

    @Override
    public void uniqueResult(Map<Integer, String> uniqueMap) {
        this.uniqueMap = uniqueMap;
    }

    //图片上传
    @Override
    public void addUploadImage() {
        mUploadType = TYPE_BANNER;
        selectedDialog();
    }

    //图片删除
    @Override
    public void deleteUploadImage(ImageBean bean, int position) {
        if (isFastClick(1200)) {
            return;
        }
        uploadImageList.remove(position);
        imageAdapter.notifyDataSetChanged();
    }

    //设置封面图
    @Override
    public void setFirstImage(int position) {
        ImageBean imageBean = uploadImageList.get(0);
        uploadImageList.set(0, uploadImageList.get(position));
        uploadImageList.set(position, imageBean);
        imageAdapter.notifyDataSetChanged();
    }

    private void selectedDialog() {
        final String[] items = {"拍照", "从相册选择"};
        new AlertDialog.Builder(context)
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
        if (!PermissionUtils.checkSDCardCameraPermission(this)) return;
        if (!FileUtils.isSDExist()) {
            shortTip(R.string.str_no_sd);
            return;
        }
        localImagePath = FileHelper.SDCARD_CACHE_IMAGE_PATH + System.currentTimeMillis() + "certificate.jpg";
        File fileUri = new File(localImagePath);
        Uri localPhotoUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            localPhotoUri = FileProvider.getUriForFile(this, Constants.FILE_PROVIDER_AUTHORITY, fileUri);
        }
        PhotoUtils.takePicture(this, localPhotoUri, REQUEST_CAMERA);
    }

    private void openGallery() {
        if (isOverLimit()) return;
        if (!PermissionUtils.checkStoragePermission(this)) return;
        if (TYPE_BANNER == mUploadType) {
            ImageSelector.builder()
                    .useCamera(false) // 设置是否使用拍照
                    .setSingle(false)  //设置是否单选
                    .setMaxSelectCount(10 - uploadImageList.size())
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

    private boolean isOverLimit() {
        if (TYPE_BANNER == mUploadType) {
            if (uploadImageList.size() >= 10) {
                shortTip(R.string.tip_image_upload_overlimit);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照
                ImageUtils.isSaveCropImageView(localImagePath);//图片处理
                if (TYPE_BANNER == mUploadType) {
                    uploadImageList.add(uploadImageList.size() - 1, new ImageBean(false, 0, localImagePath));
                    mPresenter.uploadImage(uploadImageList);
                } else {
                    mPresenter.uploadSingleImage(localImagePath);
                }
            } else if (requestCode == REQUEST_GALLERY && data != null) {//相册
                List<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                if (TYPE_BANNER == mUploadType) {
                    for (int i = 0; i < images.size(); i++) {
                        ImageUtils.isSaveCropImageView(images.get(i));//图片处理
                        uploadImageList.add(uploadImageList.size() - 1, new ImageBean(false, 0, images.get(i)));
                    }
                    mPresenter.uploadImage(uploadImageList);
                } else {
                    mPresenter.uploadSingleImage(images.get(0));//介绍图单张上传
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

    private class MyTextWatcher implements TextWatcher {
        private String text;

        MyTextWatcher(String text) {
            this.text = text;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            isFixArea = false;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.equals(text, editable.toString())) {
                isFixArea = true;
            }
        }
    }
}
