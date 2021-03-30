package com.owner.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;
import com.owner.pay.RightsBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class PayRightsAdapter extends CommonListAdapter<RightsBean> {
    private Context context;

    public PayRightsAdapter(Context context, List<RightsBean> list) {
        super(context, R.layout.item_pay_rights, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final RightsBean bean) {
        ImageView imageView = holder.getView(R.id.iv_name);
        TextView title = holder.getView(R.id.tv_title);
        TextView des = holder.getView(R.id.tv_des);

        imageView.setBackgroundResource(bean.getDrawable());
        title.setText(bean.getTitle());
        des.setText(bean.getDes());
    }
}