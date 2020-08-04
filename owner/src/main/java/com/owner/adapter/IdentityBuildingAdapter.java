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
import com.owner.identity.model.IdentityBuildingBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class IdentityBuildingAdapter extends CommonListAdapter<IdentityBuildingBean.DataBean> {

    public IdentityBuildingListener getListener() {
        return listener;
    }

    public void setListener(IdentityBuildingListener listener) {
        this.listener = listener;
    }

    private IdentityBuildingListener listener;

    public interface IdentityBuildingListener {
        void associateBuilding(IdentityBuildingBean.DataBean bean, boolean isCreate);
    }

    private Context context;
    private List<IdentityBuildingBean.DataBean> list;
    private boolean isJointWork;

    public IdentityBuildingAdapter(Context context, List<IdentityBuildingBean.DataBean> list, boolean isJointWork) {
        super(context, R.layout.item_id_building_search, list);
        this.context = context;
        this.list = list;
        this.isJointWork = isJointWork;
    }

    @Override
    public void convert(ViewHolder holder, final IdentityBuildingBean.DataBean bean) {
        TextView tvBuildingName = holder.getView(R.id.tv_building_name);
        TextView tvAddress = holder.getView(R.id.tv_address);
        TextView tvAdd = holder.getView(R.id.tv_add);
        if (!isJointWork && list != null && list.size() > 0 && holder.getAdapterPosition() == list.size() - 1) {
            tvBuildingName.setVisibility(View.GONE);
            tvAdd.setText("创建写字楼");
            tvAddress.setText("写字楼不存在，去创建写字楼");
            tvAddress.setTextColor(ContextCompat.getColor(context, R.color.text_33));
            holder.itemView.setOnClickListener(v -> listener.associateBuilding(bean, true));
        } else {
            tvAddress.setTextColor(ContextCompat.getColor(context, R.color.text_66_p50));
            if (isJointWork) {
                tvAddress.setVisibility(View.GONE);
                tvAdd.setVisibility(View.GONE);
            } else {
                tvAddress.setVisibility(View.VISIBLE);
                tvAdd.setVisibility(View.VISIBLE);
            }
            tvBuildingName.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(bean.getBuildingName())) {
                tvBuildingName.setText(Html.fromHtml(bean.getBuildingName()));
            }
            if (!TextUtils.isEmpty(bean.getAddress())) {
                tvAddress.setText(Html.fromHtml(bean.getAddress()));
            }
            tvAdd.setText("关联写字楼");
            holder.itemView.setOnClickListener(v -> listener.associateBuilding(bean, false));
        }
    }
}