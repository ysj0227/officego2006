package com.owner.adapter;

import android.content.Context;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;
import com.owner.mine.model.OrderBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class OrderAdapter extends CommonListAdapter<OrderBean> {
    private Context context;

    public OrderAdapter(Context context, List<OrderBean> list) {
        super(context, R.layout.item_pay_order, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final OrderBean bean) {
//        TextView tvBuildingName = holder.getView(R.id.tv_building_name);

    }
}