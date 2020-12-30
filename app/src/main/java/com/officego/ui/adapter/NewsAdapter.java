package com.officego.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.h5.WebViewBannerActivity_;
import com.officego.ui.home.model.TodayReadBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 * 外链打开
 **/
public class NewsAdapter extends CommonListAdapter<TodayReadBean.DataBean> {
    private final Context context;

    public NewsAdapter(Context context, List<TodayReadBean.DataBean> list) {
        super(context, R.layout.item_home_news, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final TodayReadBean.DataBean bean) {
        RoundImageView ivBrand = holder.getView(R.id.riv_image);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getImg()).into(ivBrand);
        TextView tvLabel = holder.getView(R.id.tv_label);
        TextView tvDes = holder.getView(R.id.tv_des);
        tvLabel.setText(bean.getTitleName());
        tvDes.setText(bean.getSubTitleName());
        holder.itemView.setOnClickListener(view ->
                WebViewBannerActivity_.intent(context).url(bean.getWurl()).start());
    }
}