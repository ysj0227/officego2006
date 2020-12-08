package com.owner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 * 特色
 **/
public class WriteOffAdapter extends CommonListAdapter<String> {

    @SuppressLint("UseSparseArrays")
    public WriteOffAdapter(Context context, List<String> list) {
        super(context, R.layout.item_write_off, list);
    }


    @Override
    public void convert(ViewHolder holder, String s) {
        TextView tvUseDate = holder.getView(R.id.tv_use_date);
        TextView tvWriteOffStatus = holder.getView(R.id.tv_write_off_status);
        TextView tvWriteOffCode = holder.getView(R.id.tv_write_off_code);
        TextView tvMeetingName = holder.getView(R.id.tv_meeting_name);
        TextView tvCouponName = holder.getView(R.id.tv_coupon_name);
        TextView tvCouponMoney = holder.getView(R.id.tv_coupon_money);
    }
}
