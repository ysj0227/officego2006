package com.officego.ui.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.CircleImage;
import com.officego.ui.mine.contract.ViewingDateDetailsContract;
import com.officego.ui.mine.model.ViewingDateDetailsBean;
import com.officego.ui.mine.presenter.ViewingDateDetailsPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/20.
 * Descriptions:
 **/
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.mine_activity_viewing_date_details)
public class ViewingDateDetailActivity extends BaseMvpActivity<ViewingDateDetailsPresenter>
        implements ViewingDateDetailsContract.View {
    @ViewById(R.id.tv_status)
    TextView tvStatus;
    @ViewById(R.id.tv_building_name)
    TextView tvBuildingName;
    @ViewById(R.id.civ_avatar)
    CircleImage civAvatar;
    @ViewById(R.id.tv_name)
    TextView tvName;
    @ViewById(R.id.tv_position)
    TextView tvPosition;
    @ViewById(R.id.tv_time)
    TextView tvTime;
    @ViewById(R.id.tv_address)
    TextView tvAddress;
    @ViewById(R.id.tv_bus)
    TextView tvBus;
    @ViewById(R.id.iv_mobile)
    ImageView ivMobile;

    @Extra
    int scheduleId;
    private String strStatus = "";
    private String mobile = "";

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new ViewingDateDetailsPresenter();
        mPresenter.attachView(this);
        if (getIntent().getExtras() != null) {//房东行程详情
            scheduleId = getIntent().getExtras().getInt("scheduleId");
        }
        mPresenter.getViewingDateDetails(scheduleId);
    }

    @Click(R.id.rl_back)
    void backClick() {
        finish();
    }

    @Click(R.id.iv_mobile)
    void mobileClick() {
        if (!TextUtils.isEmpty(mobile)) {
            CommonHelper.callPhone(context,mobile);
        } else {
            shortTip("手机号码错误");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void dateSuccess(ViewingDateDetailsBean data) {
        mobile = data.getBuilding().getPhone();
        int auditStatus = data.getBuilding().getAuditStatus();
        if (auditStatus == 0) {
            strStatus = "待接受";
            ivMobile.setVisibility(View.GONE);
        } else if (auditStatus == 1) {
            strStatus = "已预约";
            ivMobile.setVisibility(View.VISIBLE);
        } else if (auditStatus == 2) {
            strStatus = "预约失败";
            ivMobile.setVisibility(View.GONE);
        } else if (auditStatus == 3) {
            strStatus = "已完成";
            ivMobile.setVisibility(View.VISIBLE);
        } else if (auditStatus == 4) {
            strStatus = "未看房";
            ivMobile.setVisibility(View.GONE);
        }
        tvStatus.setText(strStatus);
        if (!TextUtils.isEmpty(data.getBuilding().getContact())) {
            tvName.setText(data.getBuilding().getContact());
        }
        if (!TextUtils.isEmpty(data.getBuilding().getJob())) {
            tvPosition.setText(" · " + data.getBuilding().getJob());
        }
        tvTime.setText(DateTimeUtils.StampToDate(data.getBuilding().getTime() + "000", "yyyy-MM-dd HH:mm"));
        tvAddress.setText(data.getBuilding().getAddress());
        Glide.with(context).load(data.getBuilding().getMainPic()).into(civAvatar);
        if (data.getBuilding().getBtype() != 0) {
            tvBuildingName.setText(String.format("约看：%s", Constants.TYPE_BUILDING == data.getBuilding().getBtype() ?
                    data.getBuilding().getBuildingName() : data.getBuilding().getBranchesName()));
        }
        List<String> stationLine = data.getBuilding().getStationline();
        List<String> stationName = data.getBuilding().getStationNames();
        StringBuffer linePlan = new StringBuffer();
        for (int i = 0; i < stationLine.size(); i++) {
            if (stationLine.size() == 1 || i == stationLine.size() - 1) {
                linePlan.append(stationLine.get(i)).append("号线 ·").append(stationName.get(i));
            } else {
                linePlan.append(stationLine.get(i)).append("号线 ·").append(stationName.get(i)).append("\n");
            }
        }
        tvBus.setText(linePlan);
    }

}
