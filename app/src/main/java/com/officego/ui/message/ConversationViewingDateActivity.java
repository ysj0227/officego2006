package com.officego.ui.message;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.contract.ConversationContract;
import com.officego.commonlib.common.dialog.ViewingDateDialog;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.RenterBean;
import com.officego.commonlib.common.presenter.ConversationPresenter;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.CircleImage;
import com.officego.commonlib.view.RoundImageView;
import com.officego.rpc.OfficegoApi;

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
@EActivity(R.layout.conversation_activity_viewing_date)
public class ConversationViewingDateActivity extends BaseMvpActivity<ConversationPresenter>
        implements ConversationContract.View, ViewingDateDialog.SureClickListener {
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
    @ViewById(R.id.tv_name)
    TextView tvName;
    @ViewById(R.id.tv_mobile)
    TextView tvMobile;
    @ViewById(R.id.civ_avatar)
    CircleImage civAvatar;
    @ViewById(R.id.tv_owner_name)
    TextView tvOwnerName;
    @ViewById(R.id.tv_position)
    TextView tvPosition;
    @ViewById(R.id.btn_viewing_date)
    Button btnViewingDate;
    @Extra
    String targetId;
    @Extra
    int buildingId;//楼盘详情传入
    @Extra
    int houseId;//房源详情传入
    private ChatHouseBean mData;

    @AfterExtras
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new ConversationPresenter();
        mPresenter.attachView(this);
        //去除 targetId  的最后一位 ,产品定义
        String getHouseChatId = targetId.substring(0, targetId.length() - 1);
        mPresenter.getHouseDetails(buildingId, houseId, getHouseChatId);
    }

    @Click(R.id.rl_back)
    void backClick() {
        finish();
    }

    @Click(R.id.tv_select_time)
    void selectTimeClick() {
        showDialog();
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
        String time = tvSelectTime.getText().toString().trim();
        if (mData != null) {
            addRenter(mData.getBuilding().getBuildingId(), DateTimeUtils.dateToSecondStamp(time), targetId);
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
        mData = data;
        tvKm.setVisibility(View.GONE);
        if (data == null || data.getBuilding() == null) {
            shortTip("暂无楼盘信息");
            return;
        }
        if (data.getBuilding() != null) {
            Glide.with(context).load(data.getBuilding().getMainPic()).into(ivHouseImg);
            tvHouseName.setText(data.getBuilding().getBuildingName());
            tvLocation.setText(data.getBuilding().getDistrict());
            if (data.getBuilding().getStationline().size() > 0) {
                String workTime = data.getBuilding().getNearbySubwayTime().get(0);
                String stationLine = data.getBuilding().getStationline().get(0);
                String stationName = data.getBuilding().getStationNames().get(0);
                tvRouteMap.setText("步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName);
            }
            if (data.getBuilding().getMinSinglePrice() != null) {
                tvPrice.setText("¥" + data.getBuilding().getMinSinglePrice());
            }
        }
//        tvName.setText("姓名  " + data.getCreateUser());
//        tvMobile.setText("联系方式  " + data.getUser().getPhone());
//        if (data.getChatted() != null) {
//            Glide.with(context).load(data.getChatted().getAvatar()).into(civAvatar);
//            tvOwnerName.setText(data.getChatted().getNickname());
//            tvPosition.setText(data.getChatted().getJob());
//        }
    }

    @Override
    public void firstChatSuccess(FirstChatBean data) {

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
                                "",
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
