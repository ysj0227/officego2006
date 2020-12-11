package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.CouponListBean;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.view.widget.AutoFitTextView;
import com.officego.ui.coupon.CouponDetailsActivity_;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/4
 **/
public class CouponAdapter extends CommonListAdapter<CouponListBean.ListBean> {
    private Context context;
    private boolean isValid;

    /**
     * @param context 上下文
     * @param list    列表数据
     */
    public CouponAdapter(Context context, boolean isValid, List<CouponListBean.ListBean> list) {
        super(context, R.layout.item_coupon, list);
        this.isValid = isValid;
        this.context = context;
    }

    /**
     * "offline": 1609407811,//下架时间
     * "discountMax": "300",//最大折扣金额/券金额/减至金额
     * "couponType": 3,//券类型 1:折扣券,2:满减券,3:减至券
     * "amountRange": "1000,1500",//使用金额范围
     * "useRange": 1,//使用范围
     * "batchCode": "859F7588CB5D472B",//券码
     * "discount": "",//折扣
     * "online": 1606902208,、、上架时间
     * "batchTitle": "减至券啊",//券名称
     * "batchId": "20201202174338",//批次id
     * "shelfLife": "2020.12.31 - 2020.12.02",//有效期
     * "status":  0:未启用/未绑定,1:已绑定/待使用,2:废弃,3:暂停,4:过期,5:冻结,6:已核销
     */

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, CouponListBean.ListBean bean) {
        holder.setIsRecyclable(false);//禁止复用
        RelativeLayout rlCouponBg = holder.getView(R.id.rl_coupon_bg);
        TextView tvRmbUnit = holder.getView(R.id.tv_rmb_unit);
        AutoFitTextView tvRmb = holder.getView(R.id.tv_rmb);
        AutoFitTextView tvUseRange = holder.getView(R.id.tv_use_range);
        ImageView ivFlag = holder.getView(R.id.iv_flag);
        TextView tvActiveName = holder.getView(R.id.tv_active_name);
        TextView tvUseWay = holder.getView(R.id.tv_use_way);
        TextView tvUseDate = holder.getView(R.id.tv_use_date);
        TextView btnUse = holder.getView(R.id.btn_use);
        ImageView ivCouponFlag = holder.getView(R.id.iv_coupon_flag);

        //券类型 1:折扣券,2:满减券,3:减至券
        if (bean.getCouponType() == 1) {
            tvRmbUnit.setVisibility(View.GONE);
            tvRmbUnit.setTextSize(20f);
            tvRmbUnit.setText("");
            if (!TextUtils.isEmpty(bean.getDiscount())) {
                tvRmb.setText(CommonHelper.digits(Integer.parseInt(bean.getDiscount()), 10) + "折");
            }
        } else if (bean.getCouponType() == 2) {
            tvRmbUnit.setVisibility(View.VISIBLE);
            tvRmbUnit.setTextSize(20f);
            tvRmbUnit.setText("¥");
            tvRmb.setText(bean.getDiscountMax());
        } else {
            tvRmbUnit.setVisibility(View.VISIBLE);
            tvRmbUnit.setTextSize(10f);
            tvRmbUnit.setText("减至");
            tvRmb.setText(bean.getDiscountMax());
        }
        tvUseRange.setText(bean.getAmountRangeText());
        tvActiveName.setText("【" + bean.getBatchTitle() + "】");
        tvUseWay.setText("仅限到店核销使用");
        tvUseDate.setText(bean.getShelfLife());
        //颜色
        if (isValid) {
            rlCouponBg.setBackground(ContextCompat.getDrawable(context, R.mipmap.ic_coupon_valid));
            ivFlag.setVisibility(View.GONE);
            btnUse.setVisibility(View.VISIBLE);
            ivCouponFlag.setVisibility(View.GONE);
            tvRmbUnit.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            tvRmb.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            tvUseRange.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            tvActiveName.setTextColor(ContextCompat.getColor(context, R.color.black));
            tvUseWay.setTextColor(ContextCompat.getColor(context, R.color.common_5c));
            tvUseDate.setTextColor(ContextCompat.getColor(context, R.color.common_5c));
        } else {
            rlCouponBg.setBackground(ContextCompat.getDrawable(context, R.mipmap.ic_coupon_invalid));
            ivFlag.setVisibility(View.GONE);
            btnUse.setVisibility(View.GONE);
            ivCouponFlag.setVisibility(View.VISIBLE);
            tvRmbUnit.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvRmb.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvUseRange.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvActiveName.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvUseWay.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            tvUseDate.setTextColor(ContextCompat.getColor(context, R.color.common_c1));
            // 0:未启用/未绑定,1:已绑定/待使用,2:废弃,3:暂停,4:过期,5:冻结,6:已核销
            if (bean.getStatus() == 6) {
                ivCouponFlag.setBackgroundResource(R.mipmap.ic_coupon_flag_used);
            } else if (bean.getStatus() == 4) {
                ivCouponFlag.setBackgroundResource(R.mipmap.ic_coupon_flag_expire);
            } else {
                ivCouponFlag.setBackgroundResource(R.mipmap.ic_coupon_flag_expire2);
            }
        }
        //进入会议室
        if (isValid) {
            holder.itemView.setOnClickListener(view ->
                    CouponDetailsActivity_.intent(context)
                            .couponBean(bean)
                            .start());
        }
    }
}