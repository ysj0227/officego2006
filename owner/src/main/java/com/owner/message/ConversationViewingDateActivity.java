package com.owner.message;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.contract.ConversationContract;
import com.officego.commonlib.common.dialog.ViewingDateDialog;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.RenterBean;
import com.officego.commonlib.common.presenter.ConversationPresenter;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.CircleImage;
import com.officego.commonlib.view.RoundImageView;
import com.owner.R;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions: 发起预约看房
 **/
@SuppressLint("Registered")
@EActivity(resName = "conversation_activity_viewing_date")
public class ConversationViewingDateActivity extends BaseMvpActivity<ConversationPresenter>
        implements ConversationContract.View, ViewingDateDialog.SureClickListener {
    @ViewById(resName = "tv_select_time")
    TextView tvSelectTime;
    @ViewById(resName = "iv_house")
    RoundImageView ivHouseImg;
    @ViewById(resName = "tv_house_name")
    TextView tvHouseName;
    @ViewById(resName = "tv_location")
    TextView tvLocation;
    @ViewById(resName = "tv_bus")
    TextView tvRouteMap;
    @ViewById(resName = "tv_km")
    TextView tvKm;
    @ViewById(resName = "tv_price")
    TextView tvPrice;
    @ViewById(resName = "tv_name")
    TextView tvName;
    @ViewById(resName = "tv_mobile")
    TextView tvMobile;
    @ViewById(resName = "civ_avatar")
    CircleImage civAvatar;
    @ViewById(resName = "tv_owner_name")
    TextView tvOwnerName;
    @ViewById(resName = "tv_position")
    TextView tvPosition;
    @ViewById(resName = "btn_viewing_date")
    Button btnViewingDate;
    @Extra
    String targetId;
    private ChatHouseBean mData;

    @AfterExtras
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new ConversationPresenter();
        mPresenter.attachView(this);
        //去除 targetId  的最后一位 ,产品定义
        String getHouseChatId = targetId.substring(0, targetId.length() - 1);
        mPresenter.getHouseDetails(getHouseChatId);
    }

    @Click(resName = "rl_back")
    void backClick() {
        finish();
    }

    @Click(resName = "tv_select_time")
    void selectTimeClick() {
        showDialog();
    }

    @Click(resName = "iv_arrow_right")
    void arrowRightClick() {
        showDialog();
    }

    private void showDialog() {
        ViewingDateDialog viewingDateDialog = new ViewingDateDialog(context);
        viewingDateDialog.setSureListener(this);
    }

    @Click(resName = "btn_viewing_date")
    void viewingDateClick() {
        if (TextUtils.isEmpty(tvSelectTime.getText())) {
            return;
        }
        String time = tvSelectTime.getText().toString().trim();
        if (mData != null) {
            addRenter(mData.getHouse().getBuildingId(), mData.getHouse().getHouseId(),
                    DateTimeUtils.dateToSecondStamp(time), targetId);
        } else {
            shortTip("预约失败");
        }
    }

    private void enableButton() {
        btnViewingDate.setBackgroundResource(R.drawable.btn_common_bg_primary);
        btnViewingDate.setEnabled(Boolean.TRUE);//启用按钮
    }

    @Override
    public void selectedDate(String date) {
        tvSelectTime.setText(date);
        enableButton();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void houseSuccess(ChatHouseBean data) {
        if (data == null || data.getHouse() == null) {
            shortTip("暂无楼盘信息");
            return;
        }
        mData = data;
        Glide.with(context).load(data.getHouse().getMainPic()).into(ivHouseImg);
        tvHouseName.setText(data.getHouse().getBuildingName());
        tvLocation.setText(data.getHouse().getDistrict());
        if (data.getHouse() != null && data.getHouse().getStationline().size() > 0) {
            String workTime = data.getHouse().getNearbySubwayTime().get(0);
            String stationLine = data.getHouse().getStationline().get(0);
            String stationName = data.getHouse().getStationNames().get(0);
            tvRouteMap.setText("步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName);
        }
        if (data.getHouse() == null || TextUtils.isEmpty(data.getHouse().getDistance())) {
            tvKm.setVisibility(View.GONE);
        } else {
            tvKm.setVisibility(View.VISIBLE);
            tvKm.setText(data.getHouse().getDistance() + "Km");
        }
        tvPrice.setText("￥" + data.getHouse().getMinSinglePrice());
        tvName.setText("姓名  " + data.getCreateUser());
        tvMobile.setText("联系方式  " + data.getUser().getPhone());
        Glide.with(context).load(data.getChatted().getAvatar()).into(civAvatar);
        tvOwnerName.setText(data.getChatted().getNickname());
        tvPosition.setText(data.getChatted().getJob());
    }

    public void addRenter(int buildingId, int houseIds, String time, String targetId) {
        showLoadingDialog();
        OfficegoApi.getInstance().addRenter(buildingId, houseIds, time, targetId,
                new RetrofitCallback<RenterBean>() {
                    @Override
                    public void onSuccess(int code, String msg, RenterBean data) {
                        LogCat.e(TAG, "1111 addRenter onSuccess =" + data.getId() + " name=" + data.getRenterName() + " time=" + time);
                        hideLoadingDialog();
                        //发起预约请求
                        SendMessageManager.getInstance().sendViewingDateMessage(
                                targetId,//对方id
                                houseIds,
                                time,
                                "",
                                "");
                        finish();
                    }

                    @Override
                    public void onFail(int code, String msg, RenterBean data) {
                        LogCat.e(TAG, "addRenter onFail code=" + code + "  msg=" + msg);
                        hideLoadingDialog();
                        shortTip("预约失败");
                    }
                });
    }

}
