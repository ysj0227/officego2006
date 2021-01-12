package com.officego.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.ui.home.model.BuildingJointWorkBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions: 看房日程
 **/
public class ServiceBaseLogoAdapter extends CommonListAdapter<BuildingJointWorkBean.BuildingBean.BasicServicesBean> {

    private final Context context;

    public ServiceBaseLogoAdapter(Context context, List<BuildingJointWorkBean.BuildingBean.BasicServicesBean> list) {
        super(context, R.layout.home_include_house_service_item_logo, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final BuildingJointWorkBean.BuildingBean.BasicServicesBean bean) {
        ImageView ivLogo = holder.getView(R.id.iv_service_logo);
        if (!bean.getDictImg().contains("https")){
            Glide.with(context).load(bean.getDictImg().replace("http","https")).into(ivLogo);
        }else {
            Glide.with(context).load(bean.getDictImgBlack()).into(ivLogo);
        }
    }
}
