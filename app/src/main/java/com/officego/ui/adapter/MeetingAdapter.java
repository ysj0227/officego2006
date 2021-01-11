package com.officego.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.h5.WebViewMeetingActivity_;
import com.officego.ui.home.model.HomeMeetingBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class MeetingAdapter extends CommonListAdapter<HomeMeetingBean.DataBean.ListBean> {

    private final Context context;

    public MeetingAdapter(Context context, List<HomeMeetingBean.DataBean.ListBean> list) {
        super(context, R.layout.item_houme_meeting, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final HomeMeetingBean.DataBean.ListBean bean) {
        //TextView tvDiscount = holder.getView(R.id.tv_discount);
        //tvDiscount.getPaint().setAntiAlias(true);//抗锯齿
        //tvDiscount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        RoundImageView ivImage = holder.getView(R.id.riv_image);
        View vShadow = holder.getView(R.id.v_shadow);
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getImg()).into(ivImage);
        TextView tvLabel = holder.getView(R.id.tv_label);
        TextView tvRmbMoney = holder.getView(R.id.tv_rmb_money);
        tvRmbMoney.setText(CommonHelper.bigDecimal(bean.getPrice(), true));
        if (bean.getTypeBean() == null || bean.getTypeBean().size() == 0) {
            vShadow.setVisibility(View.GONE);
        } else {
            vShadow.setVisibility(View.VISIBLE);
            tvLabel.setText(bean.getTypeBean().get(0).getDictCname());
        }
        holder.itemView.setOnClickListener(view -> WebViewMeetingActivity_.intent(context).isMeetingDetail(true).start());
    }
}