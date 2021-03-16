package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.NearbyBuildingBean;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:
 **/
public class NearbyHouseAdapter extends CommonListAdapter<NearbyBuildingBean.DataBean> {


    public NearbyHouseAdapter(Context context, List<NearbyBuildingBean.DataBean> list) {
        super(context, R.layout.item_home_nearby_house, list);

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void convert(ViewHolder holder, final NearbyBuildingBean.DataBean bean) {
        RoundImageView ivHouse = holder.getView(R.id.iv_house);
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getMainPic()).into(ivHouse);
        holder.setText(R.id.tv_house_name, bean.getBuildingName());
    }

}
