package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.ui.home.model.BuildingInfoBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 **/
public class BuildingInfoAdapter extends CommonListAdapter<BuildingInfoBean> {

    public BuildingInfoAdapter(Context context, List<BuildingInfoBean> list) {
        super(context, R.layout.item_house_introduce_info, list);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, final BuildingInfoBean bean) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvValue = holder.getView(R.id.tv_value);
        tvName.setText(bean.getName());
        tvValue.setText(bean.getValue());
    }
}
