package com.officego.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.ui.coupon.CouponDetailsActivity_;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/4
 **/
public class CouponAdapter extends CommonListAdapter<String> {
    private Context context;
    private boolean isValid;

    /**
     * @param context 上下文
     * @param list    列表数据
     */
    public CouponAdapter(Context context, boolean isValid, List<String> list) {
        super(context, R.layout.item_coupon, list);
        this.isValid = isValid;
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        RelativeLayout rlCouponBg = holder.getView(R.id.rl_coupon_bg);
        TextView tvRmbUnit = holder.getView(R.id.tv_rmb_unit);
        TextView tvRmb = holder.getView(R.id.tv_rmb);
        TextView tvUseRange = holder.getView(R.id.tv_use_range);
        ImageView ivFlag = holder.getView(R.id.iv_flag);
        TextView tvActiveName = holder.getView(R.id.tv_active_name);
        TextView tvUseWay = holder.getView(R.id.tv_use_way);
        TextView tvUseDate = holder.getView(R.id.tv_use_date);
        Button btnUse = holder.getView(R.id.btn_use);
        ImageView ivCouponFlag = holder.getView(R.id.iv_coupon_flag);
        rlCouponBg.setBackground(ContextCompat.getDrawable(context, isValid ? R.mipmap.ic_coupon_valid : R.mipmap.ic_coupon_invalid));
        //颜色
        if (isValid) {
            ivFlag.setVisibility(View.VISIBLE);
            btnUse.setVisibility(View.VISIBLE);
            ivCouponFlag.setVisibility(View.GONE);
            tvRmbUnit.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            tvRmb.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            tvUseRange.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            tvActiveName.setTextColor(ContextCompat.getColor(context, R.color.black));
            tvUseWay.setTextColor(ContextCompat.getColor(context, R.color.common_5c));
            tvUseDate.setTextColor(ContextCompat.getColor(context, R.color.common_5c));
//            ivFlag.setBackgroundResource();
        } else {
            ivFlag.setVisibility(View.GONE);
            btnUse.setVisibility(View.GONE);
            ivCouponFlag.setVisibility(View.VISIBLE);
            tvRmbUnit.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvRmb.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvUseRange.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvActiveName.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvUseWay.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvUseDate.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            ivCouponFlag.setBackgroundResource(R.mipmap.ic_coupon_flag_expire);
        }

        btnUse.setOnClickListener(view -> CouponDetailsActivity_.intent(context).start());

    }

}