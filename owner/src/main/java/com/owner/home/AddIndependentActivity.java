package com.owner.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.dialog.RentDialog;
import com.officego.commonlib.utils.EditInputFilter;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.commonlib.view.widget.SettingItemLayout;
import com.owner.R;
import com.owner.dialog.ConditionedDialog;
import com.owner.dialog.FloorTypeDialog;
import com.owner.home.rule.AreaTextWatcher;
import com.owner.home.rule.CarFeeTextWatcher;
import com.owner.home.rule.FloorHeightTextWatcher;
import com.owner.home.rule.IntegerTextWatcher;
import com.owner.home.rule.TextCountsWatcher;
import com.owner.zxing.QRScanActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by shijie
 * Date 2020/10/19
 **/
@EActivity(resName = "activity_home_independent_manager")
public class AddIndependentActivity extends BaseActivity
        implements RentDialog.SureClickListener,
        FloorTypeDialog.FloorListener, ConditionedDialog.ConditionedListener {
    @ViewById(resName = "tv_upload_title")
    TextView tvUploadTitle;
    @ViewById(resName = "tv_des_title")
    TextView tvDesTitle;
    @ViewById(resName = "sil_title")
    SettingItemLayout silTitle;
    @ViewById(resName = "sil_seats")
    SettingItemLayout silSeats;
    @ViewById(resName = "sil_area")
    SettingItemLayout silArea;
    @ViewById(resName = "sil_rent_single")
    SettingItemLayout silRentSingle;
    @ViewById(resName = "sil_floor_no")
    SettingItemLayout silFloorNo;
    @ViewById(resName = "sil_rent_sum")
    SettingItemLayout silRentSum;
    //第几层 总楼层
    @ViewById(resName = "et_floors")
    EditText etFloors;
    @ViewById(resName = "tv_counts_floor")
    TextView tvCountsFloor;
    //租期
    @ViewById(resName = "sil_rent_time")
    SettingItemLayout silRentTime;
    @ViewById(resName = "sil_free_rent")
    SettingItemLayout silFreeRent;
    //空调
    @ViewById(resName = "sil_conditioned")
    SettingItemLayout silConditioned;
    @ViewById(resName = "sil_conditioned_fee")
    SettingItemLayout silConditionedFee;
    @ViewById(resName = "sil_car_num")
    SettingItemLayout silCarNum;
    @ViewById(resName = "sil_car_fee")
    SettingItemLayout silCarFee;
    @ViewById(resName = "sil_storey_height")
    SettingItemLayout silStoreyHeight;
    //介绍
    @ViewById(resName = "tv_counts")
    TextView tvCounts;
    @ViewById(resName = "cet_desc_content")
    ClearableEditText cetDescContent;
    //扫一扫
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;
    @ViewById(resName = "iv_desc_image")
    ImageView ivDescImage;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        initViews();
        initDigits();
    }

    private void initViews() {
        tvUploadTitle.setText("上传办公室图片");
        tvDesTitle.setText("户型格局介绍");
    }

    private void initDigits() {
        //名称
        EditInputFilter.setOfficeGoEditProhibitSpeChat(silTitle.getEditTextView(), 20);
        //工位数0-100
        silSeats.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 100, silSeats.getEditTextView()));
        //面积 0-10000
        silArea.getEditTextView().addTextChangedListener(new AreaTextWatcher(context, 10000, silArea.getEditTextView()));
        //租金 TODO

        //最短租期
        silRentTime.getEditTextView().addTextChangedListener(new IntegerTextWatcher(context, 60, silRentTime.getEditTextView()));
        //净高 层高 0-8或一位小数
        silStoreyHeight.getEditTextView().addTextChangedListener(new FloorHeightTextWatcher(context, silStoreyHeight.getEditTextView()));
        //车位费0-5000整数
        silCarFee.getEditTextView().addTextChangedListener(new CarFeeTextWatcher(context, silCarFee.getEditTextView()));
        //介绍
        cetDescContent.addTextChangedListener(new TextCountsWatcher(tvCounts, cetDescContent));
    }

    @Click(resName = "btn_next")
    void nextOnClick() {
//        UploadVideoVrActivity_.intent(context).start();
        submit();
    }

    private void submit() {
        String seats = silSeats.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(seats)) {
            shortTip("请输入工位数");
            return;
        }
        String rentSingle = silRentSingle.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(rentSingle)) {
            shortTip("请输入租金");
            return;
        }
        String floorNo = silFloorNo.getLeftToArrowTextView().getText().toString();
        if (TextUtils.isEmpty(floorNo)) {
            shortTip("请选择楼层");
            return;
        }
        String floors = etFloors.getText().toString();
        if (TextUtils.isEmpty(floors)) {
            shortTip("请输入楼层");
            return;
        }
        String rentTime = silRentTime.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(rentTime)) {
            shortTip("请输入最短租期");
            return;
        }
        String conditioned = silConditioned.getContextView().getText().toString();
        if (TextUtils.isEmpty(conditioned)) {
            shortTip("请选择空调类型");
            return;
        }
        String storeyHeight = silStoreyHeight.getEditTextView().getText().toString();
        if (TextUtils.isEmpty(storeyHeight)) {
            shortTip("请输入净高");
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

    @Click(resName = "sil_floor_no")
    void floorNoOnClick() {
        new FloorTypeDialog(context).setListener(this);
    }

    @Click(resName = "sil_free_rent")
    void freeRentOnClick() {
        new RentDialog(context, getString(R.string.str_free_rent)).setSureListener(this);
    }

    @Click(resName = "sil_conditioned")
    void conditionedOnClick() {
        new ConditionedDialog(context).setListener(this);
    }

    @Override
    public void selectedRent(String str) {
        silFreeRent.setLeftToArrowText(str);
    }

    @Override
    public void sureFloor(String text) {
        silFloorNo.setLeftToArrowText(text);
    }

    @Override
    public void sureConditioned(String string, int flag) {
        silConditioned.setCenterText(string);
        silConditionedFee.setVisibility(View.VISIBLE);
        if (flag == 0) {
            silConditionedFee.setCenterText("包含在物业费内，加时另计");
        } else if (flag == 1) {
            silConditionedFee.setCenterText("按电表计费");
        } else {
            silConditionedFee.setCenterText("无");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissions(context, requestCode, permissions, grantResults);
    }
}
