package com.officego.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.DirectoryBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:
 **/
public class HouseServiceAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

    private final Context context;

    public HouseServiceAdapter(Context context, List<DirectoryBean.DataBean> list) {
        super(context, R.layout.home_include_house_service_item_logo_details, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
        ImageView imageView = holder.getView(R.id.iv_service_logo_details);
        holder.setText(R.id.tv_service_logo, bean.getDictCname());
        Glide.with(context).load(bean.getDictImg()).into(imageView);
    }
}
