package com.officego.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class MeetingAdapter extends CommonListAdapter<String> {
    private Context context;

    public MeetingAdapter(Context context, List<String> list) {
        super(context, R.layout.item_houme_meeting, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final String bean) {
        RoundImageView ivImage = holder.getView(R.id.riv_image);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(ivImage);
        TextView tvLabel = holder.getView(R.id.tv_label);
        TextView tvRmbMoney = holder.getView(R.id.tv_rmb_money);
        TextView tvDiscount = holder.getView(R.id.tv_discount);
        tvDiscount.getPaint().setAntiAlias(true);//抗锯齿
        tvDiscount.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        tvLabel.setText("可用优惠券");
        tvRmbMoney.setText("20");
        tvDiscount.setText("¥100/时");
    }
}