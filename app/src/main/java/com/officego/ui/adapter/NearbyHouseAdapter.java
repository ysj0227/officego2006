package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.NearbyBuildingBean;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.RoundImageView;
import com.officego.ui.holder.HotsHolder;
import com.officego.ui.home.BuildingDetailsActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkActivity_;
import com.officego.ui.home.model.HomeHotBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:
 **/
public class NearbyHouseAdapter extends CommonListAdapter<NearbyBuildingBean.DataBean> {
    private TextView tvName, tvType, tvOfficeIndependent, tvOpenSeats,
            tvLocation, tvLines, tvRmbMoney,tvUnit, tvChatTime;
    private RoundImageView ivImageHots;
    private ImageView ivVrFlag;

    private  Context context;
    private final String DAY_UNIT = "/m²/天起";
    private final String MONTH_UNIT = "/位/月起";

    public NearbyHouseAdapter(Context context, List<NearbyBuildingBean.DataBean> list) {
        super(context, R.layout.item_home_nearby_house1, list);
        this.context=context;

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void convert(ViewHolder holder, final NearbyBuildingBean.DataBean bean) {
        tvName = holder.getView(R.id.tv_house_name);
        ivImageHots = holder.getView(R.id.iv_house);
        ivVrFlag = holder.getView(R.id.iv_vr_flag);
        tvType = holder.getView(R.id.tv_type);
        tvOfficeIndependent = holder.getView(R.id.tv_office_independent);
        tvOpenSeats = holder.getView(R.id.tv_open_seats);
        tvLocation = holder.getView(R.id.tv_location);
        tvLines = holder.getView(R.id.tv_lines);
        tvRmbMoney = holder.getView(R.id.tv_rmb_money);
        tvUnit = holder.getView(R.id.tv_unit);
        tvChatTime = holder.getView(R.id.tv_chat_time);

        String mPrice = bean.getMinDayPrice() == null ? "0" : bean.getMinDayPrice().toString();
        String distance = TextUtils.isEmpty(bean.getDistance()) ? "" : bean.getDistance() + " | ";
        String business = bean.getBusinessDistrict();
        String line;
        if (bean.getBuildingMap() != null && bean.getBuildingMap().getStationline().size() > 0) {
            String workTime = bean.getBuildingMap().getNearbySubwayTime().get(0);
            String stationLine = bean.getBuildingMap().getStationline().get(0);
            String stationName = bean.getBuildingMap().getStationNames().get(0);
            line = lines(workTime, stationLine, stationName);
            tvLines.setVisibility(View.VISIBLE);
        } else {
            line = "";
            tvLines.setVisibility(View.GONE);
        }
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getMainPic()).into(ivImageHots);
        ivVrFlag.setVisibility(TextUtils.equals("1", bean.getVr()) ? View.VISIBLE : View.GONE);
        tvType.setVisibility(bean.getBtype() == Constants.TYPE_BUILDING ? View.GONE : View.VISIBLE);
        tvName.setText(bean.getName());
        tvLocation.setText(String.format("%s%s", distance, business));
        tvLines.setText(line);
        tvChatTime.setText(bean.getInfotext());
        tvRmbMoney.setText(mPrice);
        tvUnit.setText(bean.getBtype() == Constants.TYPE_BUILDING ? DAY_UNIT : MONTH_UNIT);
        if (bean.getBtype() == Constants.TYPE_BUILDING) {
            tvOpenSeats.setVisibility(View.GONE);
            setOfficeCounts(holder, bean.getHouseCount());
        } else {
            setOfficeCounts(holder, bean.getIndependenceOffice());
            setOpenSeatsCounts(holder, bean.getOpenStation());
        }
        gotoDetails(holder, bean);
    }
    private String lines(String workTime, String stationLine, String stationName) {
        return "步行" + workTime + "分钟到「" + stationLine + "号线 · " + stationName + "」";
    }

    private void setOfficeCounts(RecyclerView.ViewHolder holder, int counts) {
        if (counts == 0) {
          tvOfficeIndependent.setVisibility(View.GONE);
        } else {
            tvOfficeIndependent.setVisibility(View.VISIBLE);
            String source = "办公室<font color='#46C3C2'>" + counts + "</font>间";
            tvOfficeIndependent.setText(Html.fromHtml(source));
        }
    }

    private void setOpenSeatsCounts(RecyclerView.ViewHolder holder, int counts) {
        if (counts == 0) {
            tvOpenSeats.setVisibility(View.GONE);
        } else {
            tvOpenSeats.setVisibility(View.VISIBLE);
            String source = "开放工位<font color='#46C3C2'>" + counts + "</font>个";
            tvOpenSeats.setText(Html.fromHtml(source));
        }
    }

    //hot查看详情
    private void gotoDetails(RecyclerView.ViewHolder holder, NearbyBuildingBean.DataBean bean) {
        holder.itemView.setOnClickListener(v -> {
            if (bean.getBtype() == Constants.TYPE_BUILDING) {
                BuildingDetailsActivity_.intent(context).mConditionBean(null)
                        .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_BUILDING, bean.getId())).start();
            } else if (bean.getBtype() == Constants.TYPE_JOINTWORK) {
                BuildingDetailsJointWorkActivity_.intent(context).mConditionBean(null)
                        .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_JOINTWORK, bean.getId())).start();
            }
        });
    }

}
