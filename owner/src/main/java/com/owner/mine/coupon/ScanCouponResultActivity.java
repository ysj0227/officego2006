package com.owner.mine.coupon;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.CouponDetailsBean;
import com.officego.commonlib.common.model.CouponWriteOffBean;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.widget.TextViewItemLayout;
import com.owner.R;
import com.owner.mine.contract.CouponDetailsContract;
import com.owner.mine.presenter.CouponDetailsPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/7
 **/
@EActivity(resName = "coupon_activity_scan_result")
public class ScanCouponResultActivity extends BaseMvpActivity<CouponDetailsPresenter>
        implements CouponDetailsContract.View {
    @ViewById(resName = "title_bar")
    TitleBarView titleBar;
    @ViewById(resName = "rl_success")
    RelativeLayout rlSuccess;
    @ViewById(resName = "til_name")
    TextViewItemLayout tilName;
    @ViewById(resName = "til_money")
    TextViewItemLayout tilMoney;
    @ViewById(resName = "til_type")
    TextViewItemLayout tilType;
    @ViewById(resName = "til_user")
    TextViewItemLayout tilUser;
    @ViewById(resName = "til_qrcode")
    TextViewItemLayout tilQrcode;
    @ViewById(resName = "til_valid_date")
    TextViewItemLayout tilValidDate;
    @ViewById(resName = "til_meeting_room")
    TextViewItemLayout tilMeetingRoom;
    @ViewById(resName = "til_use_date")
    TextViewItemLayout tilUseDate;
    @ViewById(resName = "btn_sure")
    Button btnSure;
    @Extra
    String qrCode;

    private int roomPosition;
    private List<CouponDetailsBean.BuildingMeetingroomListBean> arrayList = new ArrayList<>();
    private int couponId;
    private String roomName;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        mPresenter = new CouponDetailsPresenter();
        mPresenter.attachView(this);
        tilMeetingRoom.getContextView().setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, R.mipmap.ic_down_grey, 0);
        mPresenter.getCouponDetails(qrCode);
    }

    @Click(resName = "btn_sure")
    void btnSureClick() {
        if (isFastClick(1500)) {
            return;
        }
        String room = tilMeetingRoom.getContextView().getText().toString();
        if (TextUtils.isEmpty(room) || room.contains("请选择")) {
            shortTip("请选择会议室");
            return;
        }
        mPresenter.sureWriteOff(arrayList, couponId, roomName);
    }

    @Click(resName = "til_meeting_room")
    void meetingRoomClick() {
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        String[] rooms = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            rooms[i] = arrayList.get(i).getTitle();
        }
        meetingRoomAlertDialog(rooms);
    }

    @Override
    public void couponDetailsSuccess(CouponDetailsBean data) {
        couponId = data.getId();
        arrayList = data.getBuildingMeetingroomList();//会议室列表
        if (arrayList != null && arrayList.size() == 1) {
            roomName = data.getBuildingMeetingroomList().get(0).getTitle();
            setRoomText(roomName);
        }
        tilName.setContext(data.getBatchTitle());
        if (data.getCouponType() == 1) {
            tilType.setContext("折扣券");
            if (!TextUtils.isEmpty(data.getDiscount())){
                tilMoney.setContext(CommonHelper.digits(Integer.parseInt(data.getDiscount()), 10) + "折");
            }
        } else if (data.getCouponType() == 2) {
            tilType.setContext("满减券");
            tilMoney.setContext(String.format("¥%s", data.getDiscountMax()));
        } else {
            tilType.setContext("减至券");
            tilMoney.setContext(String.format("¥%s", data.getDiscountMax()));
        }
        tilQrcode.setContext(data.getBatchCode());
        tilValidDate.setContext(data.getShelfLife());
        if (!TextUtils.isEmpty(data.getPhone()) && data.getPhone().length() == 11) {
            String phoneNumber = data.getPhone().substring(0, 3) + "****" + data.getPhone().substring(7);
            tilUser.setContext(phoneNumber);
        }
    }

    @Override
    public void writeOffSuccess(CouponWriteOffBean data) {
        titleBar.setRightTextViewText(R.string.str_complete);
        titleBar.getRightTextView().setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
        titleBar.getRightTextView().setOnClickListener(view -> finish());
        rlSuccess.setVisibility(View.VISIBLE);
        btnSure.setVisibility(View.GONE);
        tilUseDate.setVisibility(View.VISIBLE);
        tilUseDate.setContext(data.getUpdateTime());
    }

    @Override
    public void writeOffFail() {

    }

    //选择免费会议室
    private void meetingRoomAlertDialog(String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(items, roomPosition, (dialog, which) -> {
            roomPosition = which;
            roomName = items[which];
            setRoomText(roomName);
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void setRoomText(String name) {
        tilMeetingRoom.setContext(name);
        tilMeetingRoom.getContextView().setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, 0, 0);
    }
}
