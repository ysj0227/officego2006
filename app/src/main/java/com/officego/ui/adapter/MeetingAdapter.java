package com.officego.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.ui.home.model.HomeMeetingBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class MeetingAdapter extends CommonListAdapter<HomeMeetingBean.DataBean.ListBean> {

    public MeetingAdapter(Context context, List<HomeMeetingBean.DataBean.ListBean> list) {
        super(context, R.layout.item_houme_meeting, list);
    }

    @Override
    public void convert(ViewHolder holder, final HomeMeetingBean.DataBean.ListBean bean) {
        RoundImageView ivImage = holder.getView(R.id.riv_image);
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getImg()).into(ivImage);
        TextView tvLabel = holder.getView(R.id.tv_label);
        TextView tvRmbMoney = holder.getView(R.id.tv_rmb_money);
        TextView tvDiscount = holder.getView(R.id.tv_discount);
        tvDiscount.getPaint().setAntiAlias(true);//抗锯齿
        tvDiscount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        tvLabel.setText(bean.getTitle());
        tvRmbMoney.setText(CommonHelper.bigDecimal(bean.getPrice(), true));
        //tvDiscount.setText(String.format("¥%s/时", 200));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}