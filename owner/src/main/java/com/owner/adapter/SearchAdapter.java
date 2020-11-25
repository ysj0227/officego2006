package com.owner.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.SearchListBean;
import com.owner.R;
import com.owner.identity.model.IdentityBuildingBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class SearchAdapter extends CommonListAdapter<SearchListBean.DataBean> {
    private Context context;
    private List<SearchListBean.DataBean> list;

    public IdentityBuildingListener getListener() {
        return listener;
    }

    public void setListener(IdentityBuildingListener listener) {
        this.listener = listener;
    }

    private IdentityBuildingListener listener;

    public interface IdentityBuildingListener {
        void associateBuilding(SearchListBean.DataBean bean, boolean isCreate);
    }

    public SearchAdapter(Context context, List<SearchListBean.DataBean> list) {
        super(context, R.layout.item_id_building_search, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public void convert(ViewHolder holder, final SearchListBean.DataBean bean) {
        TextView tvBuildingName = holder.getView(R.id.tv_building_name);
        TextView tvAddress = holder.getView(R.id.tv_address);
        TextView tvAdd = holder.getView(R.id.tv_add);
        View vLine = holder.getView(R.id.v_line);
        vLine.setBackgroundColor(ContextCompat.getColor(context, R.color.black_20));
        if (list != null && list.size() > 0 && holder.getAdapterPosition() == list.size() - 1) {
            tvBuildingName.setVisibility(View.GONE);
            tvAdd.setText("创建楼盘/网点");
            tvAddress.setText("楼盘/网点不存在");
            tvAddress.setTextColor(ContextCompat.getColor(context, R.color.text_33));
            holder.itemView.setOnClickListener(v -> listener.associateBuilding(bean, true));
        } else {
            tvAddress.setTextColor(ContextCompat.getColor(context, R.color.text_66_p50));
            tvBuildingName.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(bean.getBuildingName())) {
                tvBuildingName.setText(Html.fromHtml(bean.getBuildingName()));
            }
            if (!TextUtils.isEmpty(bean.getAddress())) {
                tvAddress.setText(Html.fromHtml(bean.getAddress()));
            }
            tvAdd.setText(bean.getBuildType() == 1 ? "关联楼盘" : "关联网点");
            holder.itemView.setOnClickListener(v -> listener.associateBuilding(bean, false));
        }
    }
}