package com.officego.ui.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.contract.ConversationViewDateContract;
import com.officego.commonlib.common.dialog.ViewingDateDialog;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.RenterBean;
import com.officego.commonlib.common.model.owner.ChatBuildingBean;
import com.officego.commonlib.common.presenter.ConversationViewDatePresenter;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.adapter.OwnerBuildingListAdapter;

import java.util.List;

public class ConversationViewingDateOwnerActivity extends BaseMvpActivity<ConversationViewDatePresenter>
        implements ConversationViewDateContract.View,
        ViewingDateDialog.SureClickListener,
        OwnerBuildingListAdapter.ViewDateBuildingListener {

    private String targetId;
    private int buildingId;//楼盘详情传入
    private int houseId;//房源详情传入
    private String sensorEventDate;//神策点击时间
    private ChatBuildingBean.DataBean mData;

    private RelativeLayout rlBack, rlDate;
    private RecyclerView rvBuildingList;
    private TextView tvSelectTime;
    private Button btnViewingDate;

    @Override
    protected int activityLayoutId() {
        return R.layout.conversation_activity_viewing_date_other;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setStatusBarFullTransparent(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            buildingId = bundle.getInt("buildingId");
            houseId = bundle.getInt("houseId");
            targetId = bundle.getString("targetId");
            sensorEventDate = bundle.getString("sensorEventDate");
        }
        mPresenter = new ConversationViewDatePresenter();
        mPresenter.attachView(this);
        initViews();
        if (!TextUtils.isEmpty(targetId) && targetId.length() > 1) {
            String getHouseChatId = targetId.substring(0, targetId.length() - 1);
            mPresenter.getOwnerHouseList(getHouseChatId);
        }
    }

    private void initViews() {
        tvSelectTime = findViewById(R.id.tv_select_time);
        btnViewingDate = findViewById(R.id.btn_viewing_date);
        rlBack = findViewById(R.id.rl_back);
        rlDate = findViewById(R.id.rl_date);
        rvBuildingList = findViewById(R.id.rv_building_list);
        rvBuildingList.setLayoutManager(new LinearLayoutManager(context));

        rlBack.setOnClickListener(view -> finish());
        rlDate.setOnClickListener(view -> showDialog());
        btnViewingDate.setOnClickListener(view -> viewDate());
    }

    private void viewDate() {
        if (TextUtils.isEmpty(tvSelectTime.getText()==null?"":tvSelectTime.getText().toString().trim())) {
            shortTip("请选择预约时间");
            return;
        }
        if (mData == null) {
            shortTip("请选择楼盘");
            return;
        }
        String time = tvSelectTime.getText().toString().trim();
        long currentStamp = DateTimeUtils.currentTimeSecond();
        long selectStamp = DateTimeUtils.dateToSecondStampLong(time);
        if (selectStamp <= currentStamp) {
            shortTip("预约时间不能小于当前时间");
            return;
        }
        addRenter(mData.getBuildingId(), DateTimeUtils.dateToSecondStamp(time), targetId);
    }

    private void showDialog() {
        ViewingDateDialog viewingDateDialog = new ViewingDateDialog(context);
        viewingDateDialog.setSureListener(this);
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

    @Override
    public void houseSuccess(ChatHouseBean data) {
    }

    @Override
    public void buildingListSuccess(List<ChatBuildingBean.DataBean> data) {
        OwnerBuildingListAdapter adapter = new OwnerBuildingListAdapter(context, data);
        adapter.setListener(this);
        rvBuildingList.setAdapter(adapter);
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
                                mData == null || mData == null ? "" : mData.getBuildingName(),
                                mData == null || mData == null ? "" : mData.getAddress(),
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

    @Override
    public void selectedBuilding(ChatBuildingBean.DataBean dataBean) {
        mData = dataBean;
    }
}
