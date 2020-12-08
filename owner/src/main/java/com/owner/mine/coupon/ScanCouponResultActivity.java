package com.owner.mine.coupon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.TitleBarView;
import com.officego.commonlib.view.widget.TextViewItemLayout;
import com.owner.R;
import com.owner.mine.model.MeetingBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/7
 **/
@EActivity(resName = "coupon_activity_scan_result")
public class ScanCouponResultActivity extends BaseActivity {
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

    private int roomPosition;

    @SuppressLint("SetTextI18n")
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        tilMeetingRoom.getContextView().setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, R.mipmap.ic_down_grey, 0);
        setTextView();
    }

    private void setTextView() {
        tilName.setContext("【1212办公节】天降优惠券");
        tilMoney.setContext("500");
        tilType.setContext("满减券");
        tilUser.setContext("150****7866");
        tilQrcode.setContext("1111");
        tilValidDate.setContext("2020.11.01 - 2020.11.30");
    }

    @Click(resName = "btn_sure")
    void btnSureClick() {
        successView();
    }

    @Click(resName = "til_meeting_room")
    void meetingRoomClick() {
        List<MeetingBean> arrayList = new ArrayList<>();
        MeetingBean bean;
        for (int i = 0; i < 5; i++) {
            bean = new MeetingBean();
            bean.setId(i);
            bean.setName("免费会议室+i");
            arrayList.add(bean);
        }
        String[] teachers = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            teachers[i] = arrayList.get(i).getName();
        }
        meetingRoomAlertDialog(teachers);
    }

    private void successView() {
        titleBar.setRightTextViewText(R.string.str_complete);
        titleBar.getRightTextView().setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
        titleBar.getRightTextView().setOnClickListener(view -> finish());
        rlSuccess.setVisibility(View.VISIBLE);
        btnSure.setVisibility(View.GONE);
    }

    //选择免费会议室
    private void meetingRoomAlertDialog(String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(items, roomPosition, (dialog, which) -> {
            roomPosition = which;
            tilMeetingRoom.setContext(items[which]);
            tilMeetingRoom.getContextView().setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, 0, 0);
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
