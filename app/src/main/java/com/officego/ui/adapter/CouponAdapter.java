package com.officego.ui.adapter;

import android.content.Context;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;

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
        rlCouponBg.setBackground(ContextCompat.getDrawable(context, isValid ? R.mipmap.ic_coupon_valid : R.mipmap.ic_coupon_invalid));
    }
}