package com.owner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.OrderBean;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class OrderAdapter extends CommonListAdapter<OrderBean.DataBean> {

    public OrderListener getListener() {
        return listener;
    }

    public void setListener(OrderListener listener) {
        this.listener = listener;
    }

    public OrderListener listener;

    public interface OrderListener {
        void mobile();
    }


    public OrderAdapter(Context context, List<OrderBean.DataBean> list) {
        super(context, R.layout.item_pay_order, list);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, final OrderBean.DataBean bean) {
        TextView tvNo = holder.getView(R.id.tv_no);
        TextView tvStatus = holder.getView(R.id.tv_status);
        RoundImageView ivPic = holder.getView(R.id.iv_pic);
        TextView tvTitle = holder.getView(R.id.tv_title);
        TextView tvType = holder.getView(R.id.tv_type);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvCount = holder.getView(R.id.tv_count);
        TextView tvPayTime = holder.getView(R.id.tv_pay_time);
        TextView tvConsult = holder.getView(R.id.tv_consult);

        tvNo.setText("订单号：" + bean.getOrderNo());
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getImg()).into(ivPic);
        tvStatus.setText(bean.getStatus() == 2 ? "支付成功" : "支付失败");
        tvTitle.setText(bean.getTitle());
        tvType.setText("套餐类型：" + bean.getType());
        tvTime.setText("有效期："+DateTimeUtils.stampMinuteToDate(bean.getEndtime(), "yyyy年MM月dd日"));
        tvPayTime.setText("下单时间：" + DateTimeUtils.stampMinuteToDate(bean.getCreatAt(), "yyyy-MM-dd HH:mm"));
        tvCount.setText("总计：¥" + bean.getAmount());
        tvConsult.setOnClickListener(view -> {
            if (listener != null) {
                listener.mobile();
            }
        });
    }
}