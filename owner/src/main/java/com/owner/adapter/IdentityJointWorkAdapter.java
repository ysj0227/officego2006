package com.owner.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;
import com.owner.identity.model.IdentityJointWorkBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class IdentityJointWorkAdapter extends CommonListAdapter<IdentityJointWorkBean.DataBean> {

    public IdentityJointWorkListener getListener() {
        return listener;
    }

    public void setListener(IdentityJointWorkListener listener) {
        this.listener = listener;
    }

    private IdentityJointWorkListener listener;

    public interface IdentityJointWorkListener {
        void associateJointWork(IdentityJointWorkBean.DataBean bean, boolean isCreate);
    }

    private Context context;
    private List<IdentityJointWorkBean.DataBean> list;

    public IdentityJointWorkAdapter(Context context, List<IdentityJointWorkBean.DataBean> list) {
        super(context, R.layout.item_id_building_search, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public void convert(ViewHolder holder, final IdentityJointWorkBean.DataBean bean) {
        TextView tvBuildingName = holder.getView(R.id.tv_building_name);
        TextView tvAddress = holder.getView(R.id.tv_address);
        TextView tvAdd = holder.getView(R.id.tv_add);
        if (list != null && list.size() > 0 && holder.getAdapterPosition() == list.size() - 1) {
            tvAddress.setTextColor(ContextCompat.getColor(context, R.color.text_33));
            tvBuildingName.setVisibility(View.GONE);
            tvAdd.setText("创建网点");
            tvAddress.setText("网点不存在，去创建网点");
            holder.itemView.setOnClickListener(v -> listener.associateJointWork(bean, true));
        } else {
            tvAddress.setTextColor(ContextCompat.getColor(context, R.color.text_66_p50));
            tvBuildingName.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(bean.getBuildingName())) {
                tvBuildingName.setText(Html.fromHtml(bean.getBuildingName()));
            }
            if (!TextUtils.isEmpty(bean.getAddress())) {
                tvAddress.setText(Html.fromHtml(bean.getAddress()));
            }
            tvAdd.setText("关联网点");
            holder.itemView.setOnClickListener(v -> listener.associateJointWork(bean, false));
        }
    }
}