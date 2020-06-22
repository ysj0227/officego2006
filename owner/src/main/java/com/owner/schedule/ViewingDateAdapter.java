package com.owner.schedule;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.DateTimeUtils;
import com.owner.R;
import com.owner.schedule.model.ViewingDateBean;
import com.owner.utils.GotoActivityUtils;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions: 看房日程
 **/
public class ViewingDateAdapter extends CommonListAdapter<ViewingDateBean.DataBean.ScheduleListBean> {

    private Context context;
    private List<ViewingDateBean.DataBean.ScheduleListBean> list;

    public ViewingDateAdapter(Context context, List<ViewingDateBean.DataBean.ScheduleListBean> list) {
        super(context, R.layout.mine_item_viewing_date, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public void convert(ViewHolder holder, final ViewingDateBean.DataBean.ScheduleListBean bean) {

        holder.setText(R.id.tv_date, DateTimeUtils.StampToDate(String.valueOf(bean.getTime()) + "000", "MM月dd日"));
        holder.setText(R.id.tv_time, DateTimeUtils.StampToDate(String.valueOf(bean.getTime()) + "000", "HH:mm"));
        holder.setText(R.id.tv_name, bean.getContact());
        holder.setText(R.id.tv_position, bean.getJob());
        holder.setText(R.id.tv_building_name, "约看 「" + bean.getBuildingName() + "」");
        holder.setText(R.id.tv_location, bean.getBusinessDistrict());
        RelativeLayout rlDetails = holder.getView(R.id.rl_details);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvBuildingName = holder.getView(R.id.tv_building_name);
        TextView tvStatus = holder.getView(R.id.tv_status);
        View vLineTop = holder.getView(R.id.v_line_top);
        ImageView ivDateFlag = holder.getView(R.id.iv_date_flag);
        View vLineBottom = holder.getView(R.id.v_line_bottom);
        setShowLine(holder, vLineTop, vLineBottom);
        // 	0预约1预约成功2预约失败3已看房4未看房（审核状态）
        int status = bean.getAuditStatus();
        String strStatus = "";
        if (status == 0) {
            strStatus = "预约";
            status(false, rlDetails, tvName, tvBuildingName, tvStatus, vLineTop, ivDateFlag, vLineBottom);
        } else if (status == 1) {
            strStatus = "已预约";
            status(false, rlDetails, tvName, tvBuildingName, tvStatus, vLineTop, ivDateFlag, vLineBottom);
        } else if (status == 2) {
            strStatus = "预约失败";
        } else if (status == 3) {
            strStatus = "已完成";
            status(true, rlDetails, tvName, tvBuildingName, tvStatus, vLineTop, ivDateFlag, vLineBottom);
        } else if (status == 4) {
            strStatus = "未看房";
        }
        holder.setText(R.id.tv_status, strStatus);
        holder.itemView.setOnClickListener(v ->
                GotoActivityUtils.viewingDateDetailActivity(context, bean.getScheduleId()));
    }

    private void status(boolean isComplete, RelativeLayout rlDetails,
                        TextView tvName,
                        TextView tvBuildingName,
                        TextView tvStatus,
                        View vLineTop,
                        ImageView ivDateFlag,
                        View vLineBottom) {
        if (isComplete) {
            rlDetails.setBackgroundColor(ContextCompat.getColor(context, R.color.text_f7));
            tvName.setTextColor(ContextCompat.getColor(context, R.color.text_66));
            tvBuildingName.setTextColor(ContextCompat.getColor(context, R.color.text_66));
            tvStatus.setTextColor(ContextCompat.getColor(context, R.color.text_66));
            vLineTop.setBackgroundColor(ContextCompat.getColor(context, R.color.text_disable));
            vLineBottom.setBackgroundColor(ContextCompat.getColor(context, R.color.text_disable));
            ivDateFlag.setBackgroundResource(R.mipmap.ic_date_gray);
        } else {
            rlDetails.setBackgroundColor(ContextCompat.getColor(context, R.color.common_blue_normal));
            tvName.setTextColor(ContextCompat.getColor(context, R.color.text_33));
            tvBuildingName.setTextColor(ContextCompat.getColor(context, R.color.text_33));
            tvStatus.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            vLineTop.setBackgroundColor(ContextCompat.getColor(context, R.color.common_blue_main));
            vLineBottom.setBackgroundColor(ContextCompat.getColor(context, R.color.common_blue_main));
            ivDateFlag.setBackgroundResource(R.mipmap.ic_date_blue);
        }
    }

    private void setShowLine(ViewHolder holder,
                             View vLineTop,
                             View vLineBottom) {
        if (list.size() == 1) {
            vLineTop.setVisibility(View.INVISIBLE);
            vLineBottom.setVisibility(View.INVISIBLE);
        } else if (holder.getAdapterPosition() == 0 && list.size() > 1) {
            vLineTop.setVisibility(View.INVISIBLE);
            vLineBottom.setVisibility(View.VISIBLE);
        } else if (holder.getAdapterPosition() == list.size() - 1) {
            vLineTop.setVisibility(View.VISIBLE);
            vLineBottom.setVisibility(View.INVISIBLE);
        } else {
            vLineTop.setVisibility(View.VISIBLE);
            vLineBottom.setVisibility(View.VISIBLE);
        }
    }
}
