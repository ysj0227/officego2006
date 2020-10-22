package com.owner.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.DirectoryBean;
import com.owner.R;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:服务
 **/
public class ServiceLogoAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

    private Context context;

    public ServiceLogoAdapter(Context context, List<DirectoryBean.DataBean> list) {
        super(context, R.layout.item_service_item_logo, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
        ImageView ivLogo = holder.getView(R.id.iv_service_logo);
        if (!bean.getDictImg().contains("https")) {
            Glide.with(context).load(bean.getDictImg().replace("http", "https")).into(ivLogo);
        } else {
            Glide.with(context).load(bean.getDictImgBlack()).into(ivLogo);
        }
    }
}
