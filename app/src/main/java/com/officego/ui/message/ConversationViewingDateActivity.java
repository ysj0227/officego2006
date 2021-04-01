package com.officego.ui.message;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.analytics.SensorsTrack;
import com.officego.commonlib.common.contract.ConversationViewDateContract;
import com.officego.commonlib.common.dialog.ViewingDateDialog;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.RenterBean;
import com.officego.commonlib.common.model.owner.ChatBuildingBean;
import com.officego.commonlib.common.presenter.ConversationViewDatePresenter;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.rpc.OfficegoApi;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions: 发起预约看房
 **/
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.conversation_activity_viewing_date)
public class ConversationViewingDateActivity extends BaseMvpActivity<ConversationViewDatePresenter>
        implements ConversationViewDateContract.View, ViewingDateDialog.SureClickListener {
    @ViewById(R.id.tv_select_time)
    TextView tvSelectTime;
    @ViewById(R.id.iv_house)
    RoundImageView ivHouseImg;
    @ViewById(R.id.tv_house_name)
    TextView tvHouseName;
    @ViewById(R.id.tv_location)
    TextView tvLocation;
    @ViewById(R.id.tv_bus)
    TextView tvRouteMap;
    @ViewById(R.id.tv_km)
    TextView tvKm;
    @ViewById(R.id.tv_price)
    TextView tvPrice;
    @ViewById(R.id.btn_viewing_date)
    Button btnViewingDate;

    @Extra
    String targetId;
    @Extra
    int buildingId;//楼盘详情传入
    @Extra
    int houseId;//房源详情传入
    @Extra
    String sensorEventDate;//神策点击时间
    private ChatHouseBean mData;

    @AfterExtras
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new ConversationViewDatePresenter();
        mPresenter.attachView(this);

        if (!TextUtils.isEmpty(targetId) && targetId.length() > 1) {
            String getHouseChatId = targetId.substring(0, targetId.length() - 1);
            if (TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole())) {
                mPresenter.getOwnerHouseList(getHouseChatId);
            } else {
                //租户
                mPresenter.getHouseDetails(buildingId, houseId, getHouseChatId);
            }
        }
    }

    @Click(R.id.rl_back)
    void backClick() {
        finish();
    }

    @Click(R.id.tv_select_time)
    void selectTimeClick() {
        showDialog();
        //神策
        SensorsTrack.orderSeeHouseTime(mData == null ? 0 : mData.getIsBuildOrHouse(),
                mData == null ? 0 : mData.getBuilding().getBtype(),
                buildingId, sensorEventDate);
    }

    @Click(R.id.iv_arrow_right)
    void arrowRightClick() {
        showDialog();
    }

    private void showDialog() {
        ViewingDateDialog viewingDateDialog = new ViewingDateDialog(context);
        viewingDateDialog.setSureListener(this);
    }

    @Click(R.id.btn_viewing_date)
    void viewingDateClick() {
        if (TextUtils.isEmpty(tvSelectTime.getText())) {
            return;
        }
        if (mData == null) {
            shortTip("预约失败");
            return;
        }
        String time = tvSelectTime.getText().toString().trim();
        long currentStamp = DateTimeUtils.currentTimeSecond();
        long selectStamp = DateTimeUtils.dateToSecondStampLong(time);
        if (selectStamp <= currentStamp) {
            shortTip("预约时间不能小于当前时间");
            return;
        }
        addRenter(mData.getBuilding().getBuildingId(), DateTimeUtils.dateToSecondStamp(time), targetId);
        //神策
        SensorsTrack.submitBookingSeeHouse(mData == null ? 0 : mData.getIsBuildOrHouse(),
                mData == null ? 0 : mData.getBuilding().getBtype(),
                buildingId, sensorEventDate, time + ":00",
                mData == null ? "" : mData.getChatted().getTargetId(),
                mData == null ? "" : mData.getChatted().getNickname(),
                DateTimeUtils.formatDate("yyyy-MM-dd HH:mm:ss", new Date()));
    }

    private void enableButton() {
        btnViewingDate.setBackgroundResource(R.drawable.btn_common_bg_primary);
        btnViewingDate.setEnabled(Boolean.TRUE);//启用按钮
    }

    @Override
    public void selectedDate(String date) {
        tvSelectTime.setText(date);
        enableButton();
        //神策
        SensorsTrack.confirmSeeHouseTime(mData == null ? 0 : mData.getIsBuildOrHouse(),
                mData == null ? 0 : mData.getBuilding().getBtype(),
                buildingId, sensorEventDate, date + ":00");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void houseSuccess(ChatHouseBean data) {
        mData = data;
        tvKm.setVisibility(View.GONE);
        if (data == null || data.getBuilding() == null) {
            shortTip("暂无楼盘信息");
            return;
        }
        if (data.getBuilding() != null) {
            Glide.with(context).load(data.getBuilding().getMainPic()).into(ivHouseImg);
            tvHouseName.setText(data.getBuilding().getBuildingName());
            if (TextUtils.isEmpty(data.getBuilding().getDistrict())) {
                tvLocation.setVisibility(View.GONE);
            } else {
                tvLocation.setVisibility(View.VISIBLE);
                tvLocation.setText(data.getBuilding().getDistrict());
            }
            if (data.getBuilding().getStationline().size() > 0) {
                tvRouteMap.setVisibility(View.VISIBLE);
                String workTime = data.getBuilding().getNearbySubwayTime().get(0);
                String stationLine = data.getBuilding().getStationline().get(0);
                String stationName = data.getBuilding().getStationNames().get(0);
                tvRouteMap.setText("步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName);
            } else {
                tvRouteMap.setVisibility(View.GONE);
            }
            if (data.getBuilding().getBtype() == Constants.TYPE_BUILDING) {
                tvPrice.setText("¥" + (data.getBuilding().getMinSinglePrice() == null ? "0.0" : data.getBuilding().getMinSinglePrice()) + "/㎡/天起");
            } else {
                tvPrice.setText("¥" + (data.getBuilding().getMinSinglePrice() == null ? "0.0" : data.getBuilding().getMinSinglePrice()) + "/位/月起");
            }
        }
    }

    @Override
    public void buildingListSuccess(List<ChatBuildingBean.DataBean> data) {

    }

    public void addRenter(int buildingId, String time, String targetId) {
        showLoadingDialog();
        OfficegoApi.getInstance().addRenter(buildingId, time, targetId,
                new RetrofitCallback<RenterBean>() {
                    @Override
                    public void onSuccess(int code, String msg, RenterBean data) {
                        hideLoadingDialog();
                        //发起预约请求
                        SendMessageManager.getInstance().sendViewingDateMessage(
                                targetId,//对方id
                                String.valueOf(data.getId()),
                                time + "000",//毫秒
                                mData == null || mData.getBuilding() == null ? "" : mData.getBuilding().getBuildingName(),
                                mData == null || mData.getBuilding() == null ? "" : mData.getBuilding().getAddress(),
                                "你发起的看房邀约，等待对方接受",
                                "");
                        finish();
                    }

                    @Override
                    public void onFail(int code, String msg, RenterBean data) {
                        hideLoadingDialog();
                        shortTip("预约失败");
                    }
                });
    }

}
