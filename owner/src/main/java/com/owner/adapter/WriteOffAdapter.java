package com.owner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.CouponWriteOffListBean;
import com.owner.R;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 * 特色
 **/
public class WriteOffAdapter extends CommonListAdapter<CouponWriteOffListBean.ListBean> {

    @SuppressLint("UseSparseArrays")
    public WriteOffAdapter(Context context, List<CouponWriteOffListBean.ListBean> list) {
        super(context, R.layout.item_write_off, list);
    }


    @Override
    public void convert(ViewHolder holder, CouponWriteOffListBean.ListBean bean) {
        TextView tvWriteOffStatus = holder.getView(R.id.tv_write_off_status);
        TextView tvUseDate = holder.getView(R.id.tv_use_date);
        TextView tvWriteOffCode = holder.getView(R.id.tv_write_off_code);
        TextView tvMeetingName = holder.getView(R.id.tv_meeting_name);
        TextView tvCouponName = holder.getView(R.id.tv_coupon_name);
        TextView tvCouponMoney = holder.getView(R.id.tv_coupon_money);
        tvWriteOffStatus.setText(bean.getStatus() == 6 ? "核销成功" : "已过期");
        tvUseDate.setText(bean.getUpdateTime());
        tvWriteOffCode.setText(bean.getBatchCode());
        tvMeetingName.setText(bean.getTitle());
        tvCouponName.setText(String.format("【%s】", bean.getBatchTitle()));
        tvCouponMoney.setText(String.format("¥%s", bean.getDiscountMax()));
    }
}
